package enums;

public enum Insurance {
	ANTHEM_HMO("Anthem HMO"), ANTHEM_HSA("Anthem HSA"), ANTHEM_PPO("Anthem PPO"), BLUE_CROSS_HMO(
			"Blue Cross HMO"), BLUE_CROSS_HSA("Blue Cross HSA"), BLUE_CROSS_PPO("Blue Cross PPO"), CIGNA_HMO(
					"Cigna HMO"), CIGNA_HSA("Cigna HSA"), CIGNA_PPO("Cigna PPO"), HCSC_HMO("HCSC HMO"), HCSC_HSA(
							"HCSC HSA"), HCSC_PPO("HCSC PPO"), KAISER_PERMANENTE_HMO(
									"Kaiser Permanente HMO"), KAISER_PERMANENTE_HSA(
											"Kaiser Permanente HSA"), KAISER_PERMANENTE_PPO("Kaiser Permanente PPO");

	private final String fullName;

	Insurance(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName() {
		return fullName;
	}

}
