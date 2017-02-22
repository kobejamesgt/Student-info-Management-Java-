package com.gtcode.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;


public class StudentDbUtil {
	
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource){
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		
		// list students from database
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try{
			// get a connection
			myConn = dataSource.getConnection();
			
			// create sql statement
			String sql = "select * from student order by last_name";
			
			myStmt = myConn.createStatement();
			
			// execute query
			myRs = myStmt.executeQuery(sql);
			
			// process result set
			while(myRs.next()){
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// create new student object
				Student tempStudent = new Student(id, firstName, lastName, email);
				
				// add it to the list of students
				students.add(tempStudent);
			}
	
			return students;
		}
		finally{
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try{
			if(myRs != null){
				myRs.close();
			}
			if(myStmt != null){
				myStmt.close();
			}
			if(myConn != null){
				// doesn't really close it, return to the pool
				myConn.close();
			}
		}catch(Exception exc){
			exc.printStackTrace();
		}		
	}

	public void addStudent(Student theStudent) throws Exception{
		
		Connection myConn = null;
		
		// preparedStatment is a place holder and use prama values 
		PreparedStatement myStmt = null;
		
		try{
			// get db connection
			myConn = dataSource.getConnection();
			
			// create sql for insert
			String sql = "insert into student "
					   + "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			// set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			// execute sql insert
			myStmt.execute();
			
		}
		finally{
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public Student getStudent(String theStudentId) throws Exception{
		
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentID;
		
		try{
			// convert student id to int
			studentID = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to get selected student
			String sql = "select * from student where id=?";
			
			// create prepare statement 
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentID);
			
			// execute statement
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			if (myRs.next()){
				// "first_name" is actual column name from database
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				// use the studentid during construction
				theStudent = new Student(studentID, firstName, lastName, email);
			}
			else{
				throw new Exception("Could not find student id: " + studentID);
			}
			
			return theStudent;
		}
		finally{
			// close
			close(myConn, myStmt, myRs);
		}
		
	}

	public void updateStudent(Student theStudent) throws Exception{
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try{
		// get db connection
		myConn = dataSource.getConnection();
		
		// create SQL update statement
		// important message:
		// "white space" in L181 and L182
		String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
		
		// prepare statement
		myStmt = myConn.prepareStatement(sql);
		
		// set params
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
				
		// execute SQL statement
		myStmt.execute();
		}
		finally{
			
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentId) throws Exception{
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try{
			// convert student id to int
			int studentID = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create SQL to delete student
			String sql = "delete from student where id=?";
			
			// prepare statement(?)
			myStmt = myConn.prepareStatement(sql);
			
			// set params(only 1 param)
			myStmt.setInt(1, studentID);
			
			// execute SQL statement
			myStmt.execute();		
		}
		finally{
			// clean up JDBC code
			close(myConn, myStmt, null);
		}
	}
}














