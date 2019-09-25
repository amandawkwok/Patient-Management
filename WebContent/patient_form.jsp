<%@ include file="/init.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>${param.pageHeader}Patient</title>
</head>
<body>
	<nav class="navbar navbar-light bg-light justify-content-between">
		<a class="navbar-brand" href="index.jsp"> <img
			src="https://image.flaticon.com/icons/svg/149/149423.svg" alt="logo"
			style="width: 30px;"> &nbsp;&nbsp;&nbsp; ${param.pageHeader}
			Patient
		</a>
	</nav>

	<div style="padding: 20px;">
		<form method="get" action="ModifyPatient">
			<c:if test="${fn:length(errorMessages) gt 0}">
				<div class="alert alert-danger alert-dismissible fade show"
					role="alert">
					<strong>Error!</strong>
					<c:forEach items="${errorMessages}" var="error">
						<br>${error}
				</c:forEach>
					<button type="button" class="close" data-dismiss="alert"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
			</c:if>


			<!-- PATIENT INFORMATION -->
			<h4>
				<b>PATIENT INFORMATION</b>
			</h4>
			<div class="form-row">
				<div class="form-group col-md-4">
					<label for="ssn">SSN *</label>
					<input type="text" class="form-control" name="ssn" id="ssn"
						value="${ssn}">
				</div>

			</div>
			<div class="form-row">
				<div class="form-group col-md-4">
					<label for="first">First Name *</label>
					<input type="text" class="form-control" name="first"
						id="first" value="${first}">
				</div>
				<div class="form-group col-md-4">
					<label for="middle">Middle Name</label>
					<input type="text" class="form-control" name="middle"
						id="middle" value="${middle}">
				</div>
				<div class="form-group col-md-4">
					<label for="last">Last Name *</label>
					<input type="text" class="form-control" name="last"
						id="last" value="${last}">
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-4">
					<label for="dob">DOB *</label>
					<input type="date" class="form-control" name="dob"
						id="dob" placeholder="MM/DD/YYYY" value="${dob}">
				</div>
				<div class="form-group col-md-2">
					<label for="sex">Sex *</label>
					<select name="sex" id="sex" class="form-control">
						<option value="M" ${ 'M' eq sex ? 'selected' : ''}>M</option>
						<option value="F" ${ 'F' eq sex ? 'selected' : ''}>F</option>
						<option value="N/A" ${ 'N/A' eq sex ? 'selected' : ''}>N/A</option>
					</select>
				</div>
			</div>

			<!-- ADDRESS INFORMATION -->
			<hr>
			<h4>
				<b>ADDRESS</b>
			</h4>
			<div class="form-group">
				<label for="address1">Address 1 *</label>
				<input type="text" class="form-control" name="address1"
					id="address1" value="${address1}">
			</div>
			<div class="form-group">
				<label for="address2">Address 2</label>
				<input type="text" class="form-control" name="address2"
					id="address2" value="${address2}"
					placeholder="Apartment, studio, or floor">
			</div>
			<div class="form-row">
				<div class="form-group col-md-6">
					<label for="city">City *</label>
					<input type="text" class="form-control" name="city" id="city"
						value="${city}">
				</div>
				<div class="form-group col-md-4">
					<c:set var="stateValues" value="<%=State.values()%>" />
					<label for="state">State *</label>
					<select name="state" id="state" class="form-control">
						<c:forEach items="${stateValues}" var="stateValue">
							<option value="${stateValue}"
								${ stateValue eq state ? 'selected' : ''}>${stateValue}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group col-md-2">
					<label for="zip">Zip *</label>
					<input type="text" class="form-control" name="zip" id="zip"
						value="${zip}">
				</div>
			</div>

			<!-- CONTACT INFORMATION -->
			<hr>
			<h4>
				<b>CONTACT</b>
			</h4>
			<div class="form-row">
				<div class="form-group col-md-3">
					<label for="cellPhone">Cell Phone *</label>
					<input type="text" class="form-control" name="cellPhone"
						id="cellPhone" value="${cellPhone}">
				</div>
				<div class="form-group col-md-3">
					<label for="homePhone">Home Phone</label>
					<input type="text" class="form-control" name="homePhone"
						id="homePhone" value="${homePhone}">
				</div>
				<div class="form-group col-md-3">
					<label for="workPhone">Work Phone</label>
					<input type="text" class="form-control" name="workPhone"
						id="workPhone" value="${workPhone}">
				</div>

			</div>
			<div class="form-row">
				<div class="form-group col-md-3">
					<label for="email">Email</label>
					<input type="text" class="form-control" name="email" id="email"
						value="${email}">
				</div>
			</div>

			<!-- EMERGENCY CONTACT -->
			<hr>
			<h4>
				<b>EMERGENCY CONTACT</b>
			</h4>
			<div class="form-row">
				<div class="form-group col-md-3">
					<label for="emergencyName">Name *</label>
					<input type="text" class="form-control" name="emergencyName"
						id="emergencyName" value="${emergencyName}">
				</div>
				<div class="form-group col-md-3">
					<label for="emergencyRelationship">Relationship</label>
					<input type="text" class="form-control"
						name="emergencyRelationship" id="emergencyRelationship"
						value="${emergencyRelationship}">
				</div>
				<div class="form-group col-md-3">
					<label for="emergencyNumber">Phone Number *</label>
					<input type="text" class="form-control" name="emergencyNumber"
						id="emergencyNumber" value="${emergencyNumber}">
				</div>
			</div>

			<!-- INSURANCE -->
			<hr>
			<h4>
				<b>INSURANCE</b>
			</h4>
			<div class="form-row">
				<div class="form-group col-md-6">
					<label for="insuranceName">Name *</label>
					<select name="insuranceName" id="insuranceName"
						class="form-control">
						<option value="Anthem HMO"
							${ 'Anthem HMO' eq insuranceName ? 'selected' : ''}>Anthem
							HMO</option>
						<option value="Anthem HSA"
							${ 'Anthem HSA' eq insuranceName ? 'selected' : ''}>Anthem
							HSA</option>
						<option value="Anthem PPO"
							${ 'Anthem PPO' eq insuranceName ? 'selected' : ''}>Anthem
							PPO</option>
						<option value="Blue Cross HMO"
							${ 'Blue Cross HMO' eq insuranceName ? 'selected' : ''}>Blue
							Cross HMO</option>
						<option value="Blue Cross HSA"
							${ 'Blue Cross HSA' eq insuranceName ? 'selected' : ''}>Blue
							Cross HSA</option>
						<option value="Blue Cross PPO"
							${ 'Blue Cross PPO' eq insuranceName ? 'selected' : ''}>Blue
							Cross PPO</option>
						<option value="Kaiser Permanente HMO"
							${ 'Kaiser Permanente HMO' eq insuranceName ? 'selected' : ''}>Kaiser
							Permanente HMO</option>
						<option value="Kaiser Permanente HSA"
							${ 'Kaiser Permanente HSA' eq insuranceName ? 'selected' : ''}>Kaiser
							Permanente HSA</option>
						<option value="Kaiser Permanente PPO"
							${ 'Kaiser Permanente PPO' eq insuranceName ? 'selected' : ''}>Kaiser
							Permanente PPO</option>

					</select>
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-12">
					<label for="insuranceAddress">Address</label>
					<input type="text" class="form-control" name="insuranceAddress"
						id="insuranceAddress" value="${insuranceAddress}">
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-3">
					<label for="insuranceID">ID *</label>
					<input type="text" class="form-control" name="insuranceId"
						id="insuranceId" value="${insuranceId}">
				</div>
				<div class="form-group col-md-3">
					<label for="insuranceGroupNumber">Group ID</label>
					<input type="text" class="form-control" name="insuranceGroupNumber"
						id="insuranceGroupNumber" value="${insuranceGroupNumber}">
				</div>
				<div class="form-group col-md-3">
					<label for="insuranceCopay">Copay</label>
					<input type="text" class="form-control" name="insuranceCopay"
						id="insuranceCopay" value="${insuranceCopay}">
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-md-6">
					<label for="insuranceEffectiveDate">Effective Date *</label>
					<input type="date" class="form-control" name="insuranceEffectiveDate"
						id="insuranceEffectiveDate" placeholder="MM/DD/YYYY"
						value="${insuranceEffectiveDate}">
				</div>
			</div>
			<p>
			<div class="form-row">
				<div class="form-group col-md-3">
					<label for="policyHolderName">Policy Holder Name *</label>
					<input type="text" class="form-control" name="policyHolderName"
						id="policyHolderName" value="${policyHolderName}">
				</div>
				<div class="form-group col-md-3">
					<label for="policyHolderSSN">Policy Holder SSN</label>
					<input type="text" class="form-control" name="policyHolderSSN"
						id="policyHolderSSN" value="${policyHolderSSN}">
				</div>
				<div class="form-group col-md-6">
					<label for="policyHolderBirthday">Policy Holder DOB *</label>
					<input type="text" class="form-control" name="policyHolderBirthday"
						id="policyHolderBirthday" placeholder="MM/DD/YYYY"
						value="${policyHolderBirthday}">
				</div>
			</div>
			<button type="submit" class="btn btn-primary">Save</button>
			<input type="hidden" name="pageHeader" value="${param.pageHeader}" />
			<input type="hidden" name="primaryKey" value="${primaryKey}" />
		</form>
		<p />
		<c:choose>
			<c:when test="${param.pageHeader eq 'Add'}">
				<form class="form-inline" method="get" action="ViewPatients">
					<button type="submit" class="btn btn-warning">Cancel</button>
				</form>
			</c:when>
			<c:otherwise>
				<form class="form-inline" method="post" action="ViewPatients">
					<button type="submit" class="btn btn-warning">Cancel</button>
					<input type="hidden" name="primaryKey" value="${primaryKey}" />
				</form>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>

