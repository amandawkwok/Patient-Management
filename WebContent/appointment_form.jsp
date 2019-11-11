<%@ include file="/init.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>${param.pageHeader} Appointment</title>
</head>
<body>
	<nav class="navbar navbar-light bg-light justify-content-between">
		<a class="navbar-brand" href="index.jsp"> <img
			src="https://image.flaticon.com/icons/svg/149/149423.svg" alt="logo"
			style="width: 30px;"> &nbsp;&nbsp;&nbsp; ${param.pageHeader} Appointment
		</a>
	</nav>
	
	<div style="padding: 20px;">
		<form method="get" action="ModifyAppointment">
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
			
			
			<!-- APPOINTMENT INFORMATION -->
			<h4>
				<b>APPOINTMENT INFORMATION</b>
			</h4>
			
			<div class="form-row">
				<div class="form-group col-md-4">
					<label for="first">First Name *</label>
					<input type="text" class="form-control" name="first"
						id="first" placeholder="${first}" 
						value="${first}" readonly>
				</div>
				<div class="form-group col-md-4">
					<label for="middle">Middle Initial</label>
					<input type="text" class="form-control" name="middle"
						id="middle" placeholder="${middle}" 
						value="${middle}" readonly>
				</div>
				<div class="form-group col-md-4">
					<label for="last">Last Name *</label>
					<input type="text" class="form-control" name="last"
						id="last" placeholder="${last}" 
						value="${last}" readonly>
				</div>
			</div>
			
			<div class="form-row">
				<div class="form-group col-md-4">
					<label for="date">Date *</label>
					<input type="text" class="form-control" name="date"
						id="date" placeholder="MM/DD/YYYY" value="${date}">
				</div>
			</div>
			
			<div class="form-row">
				<div class="form-group col-md-4">
					<label for="time">Time *</label>
					<input type="text" class="form-control" name="time"
						id="time" placeholder="HH:mm AM/PM" value="${time}">
				</div>
			</div>

			
			<div class="form-row">
				<div class="form-group col-md-2">
					<label for="status">Status *</label>
					<select name="status" id="status" class="form-control">
						<option value="Upcoming" ${ 'Upcoming' eq status ? 'selected' : ''}>Upcoming</option>
						<option value="Completed" ${ 'Completed' eq status ? 'selected' : ''}>Completed</option>
						<option value="Cancelled" ${ 'Cancelled' eq status ? 'selected' : ''}>Cancelled</option>
					</select>
				</div>
			</div>
			
			<div class="form-row">
				<div class="form-group col-md-4">
					<label for="reason">Reason</label>
					<input type="text" class="form-control" name="reason"
						id="reason" placeholder="e.g. physical" value="${reason}">
				</div>
			</div>
			
			<button type="submit" class="btn btn-primary">Save</button>
			<input type="hidden" name="pageHeader" value="${param.pageHeader}" />
			<input type="hidden" name="ssn" value="${param.ssn}" />
		</form>
		
		<c:choose>
			<c:when test="${param.pageHeader eq 'Add'}">
				<form class="form-inline" method="post" action="ViewPatients">
					<button type="submit" class="btn btn-warning">Cancel</button>
					<input type="hidden" name="primaryKey" value="${param.ssn}" />
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