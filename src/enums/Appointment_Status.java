package enums;

public enum Appointment_Status {
	COMPLETED("Completed"), CANCELLED("Cancelled"), NA("N/A"), NO_SHOW("No Show");

	private final String fullName;

	Appointment_Status(String fullName) {
		this.fullName = fullName;
	}

	public String getFullname() {
		return fullName;
	}
}
