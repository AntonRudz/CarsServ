package telran.cars.entities.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.cars.dto.Driver;

@Document(collection = "drivers")
public class DriverCrud {
	@Id
	long licenseId;
	String name;
	int birthYear;
	String phone;
	public DriverCrud(Driver driver) {
		licenseId=driver.getLicenseId();
		name=driver.getName();
		birthYear=driver.getBirthYear();
		phone=driver.getPhone();
		
	}
	public DriverCrud() {
		
	}
	public Driver getDriver() {
		return new Driver(licenseId, name, birthYear, phone);
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public long getLicenseId() {
		return licenseId;
	}
	public String getName() {
		return name;
	}
	public int getBirthYear() {
		return birthYear;
	}
	
}
