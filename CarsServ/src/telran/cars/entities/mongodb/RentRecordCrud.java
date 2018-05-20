package telran.cars.entities.mongodb;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.cars.dto.RentRecord;

@Document(collection = "records")
public class RentRecordCrud {
	@Id
	long id;
	long licenseId;
	String carNumber;
	LocalDate rentDate;
	LocalDate returnDate;
	int gasTankPercent;
	int rentDays;
	float cost;
	int damages;
	
	public RentRecordCrud() {
		
	}

	public RentRecordCrud(long licenseId,String carNumber,LocalDate rentDate,int rentDays) {
		id = System.nanoTime();
		this.licenseId=licenseId;
		this.carNumber=carNumber;
		this.rentDate=rentDate;
		this.rentDays=rentDays;
	}
	public RentRecord getRecord() {
		RentRecord res=new RentRecord();
		res.setLicenseId(licenseId);
		res.setCarNumber(carNumber);
		res.setRentDate(rentDate);
		res.setReturnDate(returnDate);
		res.setGasTankPercent(gasTankPercent);
		res.setRentDays(rentDays);
		res.setCost(cost);
		res.setDamages(damages);
		return res;
	}

	public long getId() {
		return id;
	}

	public long getLicenseId() {
		return licenseId;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public LocalDate getRentDate() {
		return rentDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public int getGasTankPercent() {
		return gasTankPercent;
	}

	public int getRentDays() {
		return rentDays;
	}

	public float getCost() {
		return cost;
	}

	public int getDamages() {
		return damages;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public void setGasTankPercent(int gasTankPercent) {
		this.gasTankPercent = gasTankPercent;
	}

	public void setDamages(int damages) {
		this.damages = damages;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}
	
}
