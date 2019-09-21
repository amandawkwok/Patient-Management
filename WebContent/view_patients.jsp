<%@ include file="/init.jsp"%>
<!DOCTYPE html>
<html>
<head>
<style>
div.a {
	text-align: left;
}

div.b {
	text-align: right;
}
</style>
<title>View Patients</title>
</head>
<body>
	<nav class="navbar navbar-light bg-light justify-content-between">
		<a class="navbar-brand" href="index.jsp"> <img
			src="https://image.flaticon.com/icons/svg/149/149423.svg" alt="logo"
			style="width: 30px;"> &nbsp;&nbsp;&nbsp;View Patients
		</a>
		<form class="form-inline" action="patient_form.jsp">
			<input type="hidden" name="pageHeader" value="Add">
			<button class="btn btn-sm btn-outline-secondary" type="submit">New
				Patient</button>
		</form>
		<form class="form-inline" method="get" action="ViewPatients">
			<input class="form-control mr-sm-2" type="search"
				placeholder="Patient Name" aria-label="Search" name="filterClause">
			<button class="btn btn-sm btn-outline-secondary" type="submit">Search</button>
		</form>
	</nav>

	<h4>&nbsp;&nbsp;${searchHeader}</h4>

	<table class="table">
		<thead>
			<tr>
				<th scope="col">First</th>
				<th scope="col">Last</th>
				<th scope="col">Next Appointment</th>
				<th scope="col">Phone Number</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${arrayList}" var="outer">
				<tr>
					<c:forEach var="inner" items="${outer}">
						<td>${inner}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>