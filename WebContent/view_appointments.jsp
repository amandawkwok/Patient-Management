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
<title>View Appointments</title>
</head>
<body>
	<nav class="navbar navbar-light bg-light justify-content-between">
		<a class="navbar-brand" href="index.jsp"> <img
			src="https://image.flaticon.com/icons/svg/149/149423.svg" alt="logo"
			style="width: 30px;"> &nbsp;&nbsp;&nbsp;View Appointments
		</a>
		<form class="form-inline" method="get" action="ViewAppointments">
			<input class="form-control mr-sm-2" type="search"
				placeholder="Appointment Date" aria-label="Search" name="filterClause">
			<button class="btn btn-sm btn-outline-secondary" type="submit">Search</button>
		</form>
	</nav>
	
	<c:if test="${not empty bannerMessage}">
		<div class="alert alert-success alert-dismissible fade show" role="alert">
			${bannerMessage}
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>
	</c:if>
	
	<h4>&nbsp;&nbsp;${searchHeader}</h4>
	
	<table class="table">
		<thead>
			<tr>
				<th scope="col">Date/Time</th>
				<th scope="col">Status</th>
				<th scope="col">First Name</th>
				<th scope="col">Last Name</th>
				<th scope="col">Reason</th>
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