package telran.cars.entities.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.cars.dto.Car;
import telran.cars.dto.State;
@Document(collection="cars")
public class CarCrud {
	@Id
	String regNumber;
	String color;
	 State state;
	 String modelName;
	 boolean inUse;
	 boolean flRemoved;
	 
	 public CarCrud(Car car) {
		 regNumber=car.getRegNumber();
		 color=car.getColor();
		 state=car.getState();
		 modelName=car.getModelName();
		 inUse=car.isInUse();
		 flRemoved=car.isFlRemoved();
	 }
	 public CarCrud() {
		 
	 }
	 public Car getCar() {
		 Car res=new Car(regNumber, color, modelName);
		 res.setFlRemoved(flRemoved);
		 res.setInUse(inUse);
		 res.setState(state);
		 return res;
	 }
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	public boolean isFlRemoved() {
		return flRemoved;
	}
	public void setFlRemoved(boolean flRemoved) {
		this.flRemoved = flRemoved;
	}
	public String getRegNumber() {
		return regNumber;
	}
	public String getColor() {
		return color;
	}
	public String getModelName() {
		return modelName;
	}
	 
}
