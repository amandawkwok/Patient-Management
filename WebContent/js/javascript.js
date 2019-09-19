function validatePatientForm() {
	var firstName = document.forms["myForm"]["firstName"].value;
	var lastName = document.forms["myForm"]["lastName"].value;
	var birthMonth = document.forms["myForm"]["birthDay"].value;
	var birthDay = document.forms["myForm"]["birthDay"].value;
	var birthYear = document.forms["myForm"]["birthYear"].value;
	var SSN = document.forms["myForm"]["SSN"].value;

	var address1 = document.forms["myForm"]["address1"].value;
	var city = document.forms["myForm"]["city"].value;
	var zip = document.forms["myForm"]["zip"].value;

//	var cellPhone = document.forms["myForm"]["cellPhone"].value;
//	var workPhone = document.forms["myForm"]["workPhone"].value;
//	var homePhone = document.forms["myForm"]["homePhone"].value;
//
//	var emergencyName = document.forms["myForm"]["emergencyName"].value;
//	var emergencyNumber = document.forms["myForm"]["emergencyNumber"].value;
//
//	var insuranceID = document.forms["myForm"]["insuranceID"].value;
//	var copay = document.forms["myForm"]["insuranceCopay"].value;
//	var effectiveMonth = document.forms["myForm"]["effectiveMonth"].value;
//	var effectiveDay = document.forms["myForm"]["effectiveDay"].value;
//	var effectiveYear = document.forms["myForm"]["effectiveYear"].value;
//	var policyHolderName = document.forms["myForm"]["policyHolderName"].value;
//	var policyHolderSSN = document.forms["myForm"]["policyHolderSSN"].value;
//	var policyHolderBirthMonth = document.forms["myForm"]["policyHolderBirthMonth"].value;
//	var policyHolderBirthDay = document.forms["myForm"]["policyHolderBirthDay"].value;
//	var policyHolderBirthYear = document.forms["myForm"]["policyHolderBirthYear"].value;

	var errors = [];

	// Patient Information
	if (isWhiteSpace(firstName))
		errors.push("First name cannot be blank.");

	if (isWhiteSpace(lastName))
		errors.push("Last name cannot be blank.");

	if (!isValidDate(birthMonth, birthDay, birthYear)) 
		errors.push("Patient birthday is not valid.");
	
	if (!isWhiteSpace(SSN) && !SSN.match(/\d{9}/))
		errors.push("Patient SSN must be a 9 digit number.");
	
	// Address
	if (isWhiteSpace(address1))
		errors.push("Address 1 cannot be blank.");

	if (isWhiteSpace(city))
		errors.push("City cannot be blank.");

	if (!isWhiteSpace(zip) && !zip.match(/\d{5}/)){
		errors.push("Zipcode must be a 5 digit number.");
	}
	
	console.log(errors.length);
	if (errors.length != 0) {
		alert(errors.join('\n'));
		console.log(errors.join('\n'));
		return false;
	}
}

function isWhiteSpace(phrase) {
	return !phrase.replace(/\s/g, '').length;
}

function isValidDate(month, day, year) {

	if (isNaN(month) || isNaN(day) || isNaN(year) || !month || !day || !year)
		return false;
	if (year < 1000 || year > 3000 || month == 0 || month > 12)
		return false;

	var monthLength = [ 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 ];

	// Adjust for leap years
	if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
		monthLength[1] = 29;

	// Check the range of the day
	return day > 0 && day <= monthLength[month - 1];
};
