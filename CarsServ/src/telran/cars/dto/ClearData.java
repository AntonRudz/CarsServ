package telran.cars.dto;

import java.io.Serializable;
import java.time.LocalDate;

@SuppressWarnings("serial")
public class ClearData implements Serializable {
	private LocalDate currentDate;
	private int days;

	public ClearData(LocalDate currentDate, int days) {
		super();
		this.currentDate = currentDate;
		this.days = days;
	}

	public LocalDate getCurrentDate() {
		return currentDate;
	}

	public int getDays() {
		return days;
	}

	public ClearData() {

	}
}
