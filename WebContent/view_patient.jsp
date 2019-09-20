<%@ include file="/init.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>View Patient</title>
</head>
<body>
	<nav class="navbar navbar-light bg-light justify-content-between">
		<a class="navbar-brand" href="index.jsp"> <img
			src="https://image.flaticon.com/icons/svg/149/149423.svg" alt="logo"
			style="width: 30px;"> &nbsp;&nbsp;&nbsp; Viewing <strong>${firstName}
				${lastName}</strong>
		</a>
		<form class="form-inline" method="get" action="ViewPatients">
			<button type="submit" class="btn btn-warning">View All
				Patients</button>
		</form>
	</nav>

	<div style="padding: 20px;">
		<!-- PATIENT INFORMATION -->
		<h4>
			<b>PATIENT INFORMATION</b>
		</h4>
		<div class="form-row">
			<div class="form-group col-md-4">
				<b>First Name: </b> ${firstName}
			</div>
			<div class="form-group col-md-4">
				<b>Middle Name: </b> ${middleName}
			</div>
			<div class="form-group col-md-4">
				<b>Last Name: </b> ${lastName}
			</div>
		</div>
		<div class="form-row">
			<div class="form-group col-md-6">
				<b>Birthday: </b> ${birthday}
			</div>
		</div>
		<div class="form-row">
			<div class="form-group col-md-6">
				<b>SSN: </b> ${ssn}
			</div>
			<div class="form-group col-md-2">
				<b>Sex: </b> ${sex}
			</div>
		</div>

		<!-- ADDRESS INFORMATION -->
		<hr>
		<h4>
			<b>ADDRESS</b>
		</h4>
		<div class="form-group">
			<b>Address 1: </b> ${address1}
		</div>
		<div class="form-group">
			<b>Address 2: </b> ${address2}
		</div>
		<div class="form-row">
			<div class="form-group col-md-6">
				<b>City: </b> ${city}
			</div>
			<div class="form-group col-md-4">
				<b>State: </b> ${state}
			</div>
			<div class="form-group col-md-2">
				<b>Zip: </b> ${zip}
			</div>
		</div>

		<!-- CONTACT INFORMATION -->
		<hr>
		<h4>
			<b>CONTACT</b>
		</h4>
		<div class="form-row">
			<div class="form-group col-md-3">
				<b>Cell: </b> ${cellPhone}
			</div>
			<div class="form-group col-md-3">
				<b>Home: </b> ${homePhone}
			</div>
			<div class="form-group col-md-3">
				<b>Work: </b> ${workPhone}
			</div>

		</div>
		<div class="form-row">
			<div class="form-group col-md-3">
				<b>Email: </b> ${email}
			</div>
		</div>

		<!-- EMERGENCY CONTACT -->
		<hr>
		<h4>
			<b>EMERGENCY CONTACT</b>
		</h4>
		<div class="form-row">
			<div class="form-group col-md-3">
				<b>Name: </b> ${emergencyName}
			</div>
			<div class="form-group col-md-3">
				<b>Relationship: </b> ${emergencyRelationship}
			</div>
			<div class="form-group col-md-3">
				<b>Phone Number: </b> ${emergencyNumber}
			</div>
		</div>

		<!-- INSURANCE -->
		<hr>
		<h4>
			<b>INSURANCE</b>
		</h4>
		<div class="form-row">
			<div class="form-group col-md-6">
				<b>Name: </b> ${insuranceName}
			</div>
		</div>
		<div class="form-row">
			<div class="form-group col-md-6">
				<b>Address: </b> ${insuranceAddress}
			</div>

		</div>
		<div class="form-row">
			<div class="form-group col-md-3">
				<b>ID: </b> ${insuranceID}
			</div>
			<div class="form-group col-md-3">
				<b>Group: </b> ${insuranceGroup}
			</div>
			<div class="form-group col-md-3">
				<b>Copay: </b> ${insuranceCopay}
			</div>
		</div>
		<div class="form-row">
			<div class="form-group col-md-6">
				<b>Effective Date: </b> ${effectiveDate}
			</div>
		</div>
		<p>
		<div class="form-row">
			<div class="form-group col-md-3">

				<b>Policy Holder Name: </b> ${policyHolderName}
			</div>
			<div class="form-group col-md-3">

				<b>Policy Holder SSN: </b> ${policyHolderSSN}
			</div>
			<div class="form-group col-md-6">
				<b>Policy Holder Birthday: </b> ${policyHolderBirthday}
			</div>
		</div>
		<table>
			<tr>
				<td>
					<form class="form-inline" method="post" action="ModifyPatient">
						<input type="hidden" name="primaryKey" value="${primaryKey}" />
						<input type="hidden" name="pageHeader" value="Edit" />
						<input type="hidden" name="firstLoad" value="true" />
						<button type="submit" class="btn btn-primary">Edit</button>
					</form>
				</td>
				<td>
					<form class="form-inline" method="get" action="DeletePatient">
						<input type="hidden" name="primaryKey" value="${primaryKey}" />
						<button type="submit" class="btn btn-danger">Delete
							Patient</button>
					</form>
				</td>
			</tr>
		</table>
		<hr>
		<h4>
			<b>APPOINTMENT HISTORY</b>
		</h4>
		<h5>Upcoming</h5>
		<table class="table">
			<thead>
				<tr>
					<th scope="col">Day/Time</th>
					<th scope="col">Reason</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${upcomingAppts}" var="outer">
					<tr>
						<c:forEach var="inner" items="${outer}">
							<td>${inner}</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<h5>Past</h5>
		<table class="table">
			<thead>
				<tr>
					<th scope="col">Day/Time</th>
					<th scope="col">Reason</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pastAppts}" var="outer">
					<tr>
						<c:forEach var="inner" items="${outer}">
							<td>${inner}</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<form class="form-inline">
			<input type="hidden" name="primaryKey" value="${ssn}" />
			<button type="submit" class="btn btn-warning">Create New
				Appointment</button>
		</form>

	</div>
</body>
</html>

