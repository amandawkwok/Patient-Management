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

		<!--drop down list-->
		<form action="ViewAppointments" method="get">
			<label for="dateFilter">Date:</label>
			<select id="dateFilter" name="dateFilter">
				<option value="Today" ${'Today' eq dateFilter ? 'selected' : ''}>Today</option>
				<option value="Past" ${'Past' eq dateFilter ? 'selected' : ''}>Past</option>
				<option value="Upcoming"
					${'Upcoming' eq dateFilter ? 'selected' : ''}>Upcoming</option>
			</select>
		</form>
	</nav>

	<c:if test="${not empty bannerMessage}">
		<div class="alert alert-success alert-dismissible fade show"
			role="alert">
			${bannerMessage}
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
		</div>
	</c:if>

	<table class="table">
		<thead>
			<tr>
				<th scope="col">Date/Time</th>
				<th scope="col">First</th>
				<th scope="col">Last</th>
				<th scope="col">Status</th>
				<th scope="col">Reason</th>
				<th scope="col">
					<!--space for edit/delete-->
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${arrayList}" var="outer">
				<tr>
					<c:forEach var="inner" varStatus="status" items="${outer}">
						<c:choose>
							<c:when test="${(fn:length(outer)) == status.count}">
								<td>
									<form class="form-inline" method="post"
										action="ModifyAppointment">
										<input type="hidden" name="pageHeader" value="Edit">
										<input type="hidden" name="tag" value="fromAppointment">
										<input type="hidden" name="appointmentID" value="${inner}" />
										<button type="submit" class="btn btn-primary">Edit</button>
									</form>
								</td>
								<td>
									<form class="form-inline" method="get"
										action="DeleteAppointment">
										<input type="hidden" name="tag" value="fromAppointment">
										<input type="hidden" name="appointmentID" value="${inner}" />
										<button type="submit" class="btn btn-danger">Delete</button>
									</form>
								</td>
							</c:when>
							<c:otherwise>
								<td>${inner}</td>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script>
		$(function() {
			$('#dateFilter').change(function() {
				this.form.submit();
			});
		});
	</script>
</body>
</html>