package telran.cars.dto;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class RentRecord implements Serializable{
private long licenseId;
private String carNumber;
private LocalDate rentDate;
private LocalDate returnDate;
private int gasTankPercent;
private int rentDays;
private float cost;
private int damages;
public RentRecord(){}
public RentRecord(long licenseId, String carNumber, LocalDate rentDate, int rentDays) {
	super();
	this.licenseId = licenseId;
	this.carNumber = carNumber;
	this.rentDate = rentDate;
	this.rentDays = rentDays;
}
public RentRecord(long licenseId, String carNumber,LocalDate returnDate,int gasTankPercent,int damages ) {
	super();
	this.licenseId = licenseId;
	this.carNumber = carNumber;
	this.returnDate=returnDate;
	this.gasTankPercent=gasTankPercent;
	this.damages=damages;
}
public LocalDate getReturnDate() {
	return returnDate;
}
public void setReturnDate(LocalDate returnDate) {
	this.returnDate = returnDate;
}
public int getGasTankPercent() {
	return gasTankPercent;
}
public void setGasTankPercent(int gasTankPercent) {
	this.gasTankPercent = gasTankPercent;
}
public float getCost() {
	return cost;
}
public void setCost(float cost) {
	this.cost = cost;
}
public int getDamages() {
	return damages;
}
public void setDamages(int damages) {
	this.damages = damages;
}
public long getLicenseId() {
	return licenseId;
}

public void setLicenseId(long licenseId) {
	this.licenseId = licenseId;
}
public void setCarNumber(String carNumber) {
	this.carNumber = carNumber;
}
public void setRentDate(LocalDate rentDate) {
	this.rentDate = rentDate;
}
public void setRentDays(int rentDays) {
	this.rentDays = rentDays;
}
public String getCarNumber() {
	return carNumber;
}
public LocalDate getRentDate() {
	return rentDate;
}
public int getRentDays() {
	return rentDays;
}
@Override
public String toString() {
	return "RentRecord [licenseId=" + licenseId + ", carNumber=" + carNumber + ", rentDate=" + rentDate
			+ ", returnDate=" + returnDate + ", gasTankPercent=" + gasTankPercent + ", rentDays=" + rentDays + ", cost="
			+ cost + ", damages=" + damages + "]";
}

}
