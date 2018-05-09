package telran.cars.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.cars.model.IRentCompany;
import telran.cars.model.RentCompanyEmbedded;
import telran.cars.dto.*;

@SpringBootApplication
@RestController

public class CarsRestAppl {
	IRentCompany company = RentCompanyEmbedded.restoreFromFile("cars.data");

	@PostMapping(value = CarsApiConstants.ADD_CAR_MODEL)
	CarsReturnCode addCarModel(@RequestBody Model carModel) {
		return company.addModel(carModel);
	}

	@RequestMapping(value = CarsApiConstants.GET_MODEL)
	Model getModel(String modelName) {
		return company.getModel(modelName);
	}

	@PostMapping(value = CarsApiConstants.ADD_CAR)
	CarsReturnCode addCar(@RequestBody Car car) {
		return company.addCar(car);
	}

	@PostMapping(value = CarsApiConstants.SAVE)
	void save() {
		company.save();
	}

	@RequestMapping(value = CarsApiConstants.GET_CAR)
	Car getCar(String carNumber) {
		return company.getCar(carNumber);
	}

	@PostMapping(value = CarsApiConstants.ADD_DRIVER)
	CarsReturnCode addDriver(@RequestBody Driver driver) {
		return company.addDriver(driver);
	}

	@RequestMapping(value = CarsApiConstants.GET_DRIVER)
	Driver getDriver(String licenseId) {
		return company.getDriver(Long.parseLong(licenseId));
	}

	@PostMapping(value = CarsApiConstants.RENT_CAR)
	CarsReturnCode rentCar(@RequestBody RentRecord rent) {
		return company.rentCar(rent.getCarNumber(), rent.getLicenseId(), rent.getRentDate(), rent.getRentDays());
	}

	@PostMapping(value = CarsApiConstants.RETURN_CAR)
	CarsReturnCode returnCar(@RequestBody RentRecord returnCar) {
		return company.returnCar(returnCar.getCarNumber(), returnCar.getLicenseId(), returnCar.getReturnDate(),
				returnCar.getGasTankPercent(), returnCar.getDamages());
	}
	@PostMapping(value=CarsApiConstants.REMOVE_CAR)
	CarsReturnCode removeCar(String carNumber) {
		return company.removeCar(carNumber);
	}
	@PostMapping(value=CarsApiConstants.CLEAR_CARS)
	List<Car> clearCars(@RequestBody ClearData clear){
		return company.clear(clear.getCurrentDate(), clear.getDays());
	}
	@RequestMapping(value=CarsApiConstants.GET_CAR_DRIVERS)
	List<Driver> getCarDrivers(String carNumber){
		return company.getCarDrivers(carNumber);
	}
	@RequestMapping(value=CarsApiConstants.GET_DRIVER_CARS)
	List<Car> getDriverCars(String licenseId){
		return company.getDriverCars(Long.parseLong(licenseId));
	}
	@RequestMapping(value=CarsApiConstants.GET_ALL_MODELS)
	List<String> getAllModels(){
		return company.getAllModels();
	}
	@RequestMapping(value=CarsApiConstants.GET_ALL_CARS)
	List<Car> getAllCars(){
		List<Car> allCars=new ArrayList<>();
		company.getAllCars().forEach(allCars::add);
		return allCars;
	}
	@RequestMapping(value=CarsApiConstants.GET_ALL_DRIVERS)
	List<Driver> getAllDrivers(){
		List<Driver> allDrivers=new ArrayList<>();
		company.getAllDrivers().forEach(allDrivers::add);
		return allDrivers;
	}
	@RequestMapping(value=CarsApiConstants.GET_ALL_RECORDS)
	List<RentRecord> getAllRecords(){
		List<RentRecord> allRecords=new ArrayList<>();
		company.getAllRecords().forEach(allRecords::add);
		return allRecords;
	}
	public static void main(String[] args) {
		SpringApplication.run(CarsRestAppl.class, args);

	}
	

}
