package telran.cars.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.cars.dto.Car;
import telran.cars.dto.CarsReturnCode;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RentCompanyData;
import telran.cars.dto.RentRecord;
import telran.cars.dto.State;
import telran.cars.entities.mongodb.CarCrud;
import telran.cars.entities.mongodb.DriverCrud;
import telran.cars.entities.mongodb.ModelCrud;
import telran.cars.entities.mongodb.RentRecordCrud;
import telran.cars.mongodb.repo.CarsRepository;
import telran.cars.mongodb.repo.DriverRepository;
import telran.cars.mongodb.repo.ModelsRepository;
import telran.cars.mongodb.repo.RentRecordsRepository;

@Service
public class RentCompanyMongoDB implements IRentCompany {
	@Autowired
	CarsRepository cars;
	@Autowired
	DriverRepository drivers;
	@Autowired
	RentRecordsRepository records;
	@Autowired
	ModelsRepository models;

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCompanyData(RentCompanyData companyData) {
		// TODO Auto-generated method stub

	}

	@Override
	public CarsReturnCode addModel(Model model) {
		if (models.existsById(model.getModelName()))
			return CarsReturnCode.MODEL_EXISTS;
		ModelCrud modelCrud = new ModelCrud(model);
		models.save(modelCrud);
		return CarsReturnCode.OK;
	}

	@Override
	public CarsReturnCode addCar(Car car) {
		if (models.existsById(car.getModelName())) {
			if (cars.existsById(car.getRegNumber()))
				return CarsReturnCode.CAR_EXISTS;
			CarCrud carCrud = new CarCrud(car);
			cars.save(carCrud);
			return CarsReturnCode.OK;
		}

		return CarsReturnCode.NO_MODEL;
	}

	@Override
	public CarsReturnCode addDriver(Driver driver) {
		if (drivers.existsById(driver.getLicenseId()))
			return CarsReturnCode.DRIVER_EXISTS;
		drivers.save(new DriverCrud(driver));
		return CarsReturnCode.OK;
	}

	@Override
	public Model getModel(String modelName) {
		ModelCrud modelCrud = models.findById(modelName).orElse(null);
		return modelCrud == null ? null : modelCrud.getModel();
	}

	@Override
	public Car getCar(String carNumber) {
		CarCrud carCrud = cars.findById(carNumber).orElse(null);
		return carCrud == null ? null : carCrud.getCar();
	}

	@Override
	public Driver getDriver(long licenseId) {
		DriverCrud driver = drivers.findById(licenseId).orElse(null);
		return driver == null ? null : driver.getDriver();
	}

	@Override
	public CarsReturnCode rentCar(String carNumber, long licenseId, LocalDate rentDate, int rentDays) {
		CarCrud car = cars.findById(carNumber).orElse(null);
		if (car == null || car.isFlRemoved())
			return CarsReturnCode.NO_CAR;
		if (car.isInUse())
			return CarsReturnCode.CAR_IN_USE;
		if (drivers.findById(licenseId).orElse(null) == null)
			return CarsReturnCode.NO_DRIVER;
		RentRecordCrud rentCrud = new RentRecordCrud(licenseId, carNumber, rentDate, rentDays);
		records.save(rentCrud);
		car.setInUse(true);
		cars.save(car);
		return CarsReturnCode.OK;
	}

	@Override
	public CarsReturnCode returnCar(String carNumber, long licenseId, LocalDate returnDate, int gasTankPercent,
			int damages) {
		if (!drivers.existsById(licenseId))
			return CarsReturnCode.NO_DRIVER;
		CarCrud car = cars.findById(carNumber).orElse(null);
		if (car == null || !car.isInUse())
			return CarsReturnCode.CAR_NOT_RENTED;
		RentRecordCrud rent = records.findByLicenseId(licenseId).stream().filter(r -> r.getReturnDate() == null)
				.findFirst().get();
		if (rent == null)
			return CarsReturnCode.RECORD_DOESNT_EXISTS;
		if (returnDate.isBefore(rent.getRentDate()))
			return CarsReturnCode.RETURN_DATE_WRONG;
		rent.setDamages(damages);
		rent.setGasTankPercent(gasTankPercent);
		rent.setReturnDate(returnDate);
		setCost(rent, car);
		updateCarData(damages, car);
		cars.save(car);
		records.save(rent);
		return CarsReturnCode.OK;
	}

	private void updateCarData(int damages, CarCrud car) {
		if (damages > 0 && damages < 10)
			car.setState(State.GOOD);
		else if (damages >= 10 && damages < 30)
			car.setState(State.BAD);
		else if (damages >= 30)
			car.setFlRemoved(true);
		car.setInUse(false);
	}

	private void setCost(RentRecordCrud record, CarCrud car) {
		long period = ChronoUnit.DAYS.between(record.getRentDate(), record.getReturnDate());
		float costPeriod = 0;
		ModelCrud model = models.findById(car.getModelName()).orElse(null);
		float costGas = 0;
		costPeriod = getCostPeriod(record, period, model);
		costGas = getCostGas(record, model);
		record.setCost(costPeriod + costGas);

	}

	private float getCostGas(RentRecordCrud record, ModelCrud model) {
		float costGas;
		int gasTank = model.getGasTank();
		float litersCost = (float) (100 - record.getGasTankPercent()) * gasTank / 100;
		costGas = litersCost * 10;
		return costGas;
	}

	private float getCostPeriod(RentRecordCrud record, long period, ModelCrud model) {
		float costPeriod;
		long delta = period - record.getRentDays();
		float additionalPeriodCost = 0;

		if (model == null)
			throw new IllegalArgumentException("Car contains wrong model");
		int pricePerDay = model.getPriceDay();
		int rentDays = record.getRentDays();
		if (delta > 0) {
			additionalPeriodCost = getAdditionalPeriodCost(pricePerDay, delta);
		}
		costPeriod = rentDays * pricePerDay + additionalPeriodCost;
		return costPeriod;
	}

	private float getAdditionalPeriodCost(int pricePerDay, long delta) {
		float fineCostPerDay = pricePerDay * 15 / 100;
		return (pricePerDay + fineCostPerDay) * delta;
	}

	@Override
	public CarsReturnCode removeCar(String carNumber) {
		CarCrud car = cars.findById(carNumber).orElse(null);
		if (car == null)
			return CarsReturnCode.NO_CAR;
		if (car.isInUse())
			return CarsReturnCode.CAR_IN_USE;
		car.setFlRemoved(true);
		cars.save(car);
		return CarsReturnCode.OK;
	}

	@Override
	public List<Car> clear(LocalDate currentDate, int days) {
		LocalDate returnedDateDelete = currentDate.minusDays(days);
		List<RentRecordCrud> recordsForDelete = getRecordsForDelete(returnedDateDelete);
		records.deleteAll(recordsForDelete);
		List<CarCrud> carsForDelete = getCarsForDelete(recordsForDelete);
		cars.deleteAll(carsForDelete);

		return carsForDelete.stream().map(CarCrud::getCar).collect(Collectors.toList());
	}

	private List<RentRecordCrud> getRecordsForDelete(LocalDate returnedDateDelete) {

		return records.findAll().stream().filter(r -> r.getReturnDate().isBefore(returnedDateDelete))
				.filter(r -> cars.findById(r.getCarNumber()).orElse(null).isFlRemoved()).collect(Collectors.toList());
	}

	private List<CarCrud> getCarsForDelete(List<RentRecordCrud> recordsForDelete) {

		return recordsForDelete.stream().map(r -> cars.findById(r.getCarNumber()).orElse(null))
				.filter(c -> !c.isInUse()).collect(Collectors.toList());
	}

	@Override
	public List<Driver> getCarDrivers(String carNumber) {
		List<Driver> res = new ArrayList<>();
		if (records.count() > 0)
			records.findByCarNumber(carNumber).forEach(r -> res.add(getDriver(r.getLicenseId())));
		return res;
	}

	@Override
	public List<Car> getDriverCars(long licenseId) {
		List<Car> res = new ArrayList<>();
		if (records.count() > 0)
			records.findByLicenseId(licenseId).forEach(r -> res.add(getCar(r.getCarNumber())));
		return res;
	}

	@Override
	public Stream<Car> getAllCars() {
		List<Car> res = new ArrayList<>();
		cars.findAll().stream().forEach(c -> res.add(c.getCar()));
		return res.stream();
	}

	@Override
	public Stream<Driver> getAllDrivers() {
		List<Driver> res = new ArrayList<>();
		drivers.findAll().stream().forEach(d -> res.add(d.getDriver()));
		return res.stream();
	}

	@Override
	public Stream<RentRecord> getAllRecords() {
		List<RentRecord> rentRecords = new ArrayList<>();
		records.findAll().stream().forEach(r -> rentRecords.add(r.getRecord()));
		return rentRecords.stream();
	}

	@Override
	public List<String> getAllModels() {
		List<String> res = new ArrayList<>();
		models.findAll().stream().forEach(m -> res.add(m.getModelName()));
		return res;
	}

}
