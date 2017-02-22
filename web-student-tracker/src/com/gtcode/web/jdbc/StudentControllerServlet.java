package com.gtcode.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.sun.jmx.snmp.SnmpStringFixed;
import com.sun.xml.internal.bind.v2.model.core.ID;

import sun.security.util.DisabledAlgorithmConstraints;



@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	// This init() method is actually called by Tomcat
	// when the server is 1st loaded
	@Override
	public void init() throws ServletException {
		
		super.init();
		
		// create our student db util ... and pass in the conn pool / datasource
		try{
			
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch (Exception exc){
			throw new ServletException(exc);
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			// read the "command" parameter
			String theCommand = request.getParameter("command");
			
			if(theCommand == null){
				theCommand = "LIST"; // default
			}
			
			// route to the appropriate method	
			switch(theCommand){
			
			case"LIST": // list the students ... in MVC fashion
				listStudents(request, response);
				break;
				
			case"ADD":
				addStudent(request, response);
				break;
				
			case"LOAD":
				loadStudent(request, response);
				break;
				
			case"UPDATE":
				updateStudent(request, response);
				break;
				
			case"DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
			}			
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
	}


	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// read the student id from the form data
		String theStudentId = request.getParameter("studentID");
		
		// delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		// send them back to "list student" page
		listStudents(request, response);
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentID"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		// perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		// send them back to the "list students" page
		listStudents(request, response);
		
	}


	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// read student id from form data
		String theStudentId = request.getParameter("studentID");
		
		//get student form database
		Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}


	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		
		// add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		// send back to main page (the student list)
		listStudents(request, response);
		
	}


	private void listStudents(HttpServletRequest request, HttpServletResponse response) 
		throws Exception{

		// get students from db util
		List<Student> students = studentDbUtil.getStudents();
		
		// add students to the request
		request.setAttribute("STUDENT_LIST", students); // (name, reference)
		
		// send to JSP page(view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-student.jsp");
		dispatcher.forward(request, response);
	}

}
