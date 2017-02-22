<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
	<title>Student Tracker App</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>

<%
/*
Without JSTL
	// get the students from the request object (sent by servlet)
	List<Student> theStudents =
					(List<Student>) request.getAttribute("STUDENT_LIST");
*/

%>
<body>

	<!-- div is a container putting information -->
	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>
	
	<div id="container">
		<div id="content">
		
			<!-- put new button: Add student -->
			<!-- CSS style for line 37-->
			<input type="button" value="Add Student" 
				onclick="window.location.href='add-student-form.jsp'; return false;"
				class="add-student-button" 
			/>
			<table>
				
				<tr>
					<!-- actual headers for html table -->
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				
				<c:forEach var="tempStudent" items="${STUDENT_LIST}">
					
					<%-- without JSPL 
						<!-- table data -->
						<td> <%= tempStudent.getFirstName() %></td>
						<td> <%= tempStudent.getLastName() %></td>
						<td> <%= tempStudent.getEmail() %></td>
					--%>
					
					<!-- with JSTL -->
					
					<!-- set up a link for each student -->
					<c:url var="tempLink" value="StudentControllerServlet">
					<!-- two param wanna sent to StudentControllerServlet -->
						<c:param name="command" value="LOAD" />
						<c:param name="studentID" value="${tempStudent.id}" />
					</c:url>
					
					<!-- set up a link to delete a student -->
					<!-- if the link is purple means the link name is not corresponded -->
					<c:url var="deleteLink" value="StudentControllerServlet">
					<!-- two param wanna sent to StudentControllerServlet -->
						<c:param name="command" value="DELETE" />
						<c:param name="studentID" value="${tempStudent.id}" />
					</c:url>
					
					<!-- table row -->
					<tr>
						<td>${tempStudent.firstName}</td>
						<td>${tempStudent.lastName}</td>
						<td>${tempStudent.email}</td>
						<td> 
							<a href="${tempLink}">Update</a>
							 |
							<a href="${deleteLink}"
							onclick="if(!(confirm('Are U sure U wanna delete this student?'))) return false">
							Delete</a>
						</td>	 
					</tr>
				</c:forEach>
			</table>
		
		</div>	
	</div>


</body>
</html>

