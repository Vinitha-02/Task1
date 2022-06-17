package Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class LoginEmployee extends HttpServlet {
	static Logger log = Logger.getLogger(LoginEmployee.class.getName());
protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		res.setContentType("application/json");
		 String  employeeName=  req.getParameter("employeeName");
		 String  employeePass=  req.getParameter("employeePass");
		
		 PrintWriter out= res.getWriter();
		 Database d= new Database();
	     Connection c=null;
	     PreparedStatement ps=null;
	     Statement st=null;
	     ResultSet r=null;
	     
		 try {
			 if(d.validate(employeeName, employeePass)) {	 
				 log.info("Successfully logged in");
				
				 HttpSession session=req.getSession(); 
				
				 
				 
	             Class.forName("com.mysql.jdbc.Driver");
	             c=DriverManager.getConnection("jdbc:mysql://localhost:3306/organisation","root","12345");
	          String   departmentID =	d.getDepartmentID();
	             if(departmentID.equals("A1")) {
	            	 session.setAttribute("departmentID",departmentID); 
                 st= c.createStatement();
                 String sql= "select employeedetails.employeeId,employeedetails.employeeName,employeedetails.employeePass,employeedetails.employeeAge,departmentdetails.departmentID,departmentdetails.departmentName,departmentdetails.Salary from employeedetails,departmentdetails where employeedetails.departmentID=departmentdetails.departmentID";

		         r = st.executeQuery(sql);
                 while(r.next()) {
                	 JSONObject record= new JSONObject();
             		 
            		 JSONArray data= new JSONArray();
            		
				        record.put("employeeId", r.getInt("employeeId"));
		                record.put("employeeName", r.getString("employeeName"));
		                record.put("employeeAge", r.getString("employeeAge"));
		                record.put("employeePass", r.getString("employeePass"));
		                record.put("departmentID", r.getString("departmentID"));
		                record.put("departmentName", r.getString("departmentName"));
		                record.put("Salary", r.getInt("Salary"));
		                data.add(record);
		                
		                out.println(data);
		              
		  }
                 log.info("Successfully fectched the data");  
                 }else{
                	 
                	
				     ps= c.prepareStatement("select employeedetails.employeeId,employeedetails.employeeName,employeedetails.employeePass,employeedetails.employeeAge,departmentdetails.departmentID,departmentdetails.departmentName,departmentdetails.Salary from employeedetails,departmentdetails where employeedetails.employeeName=? and departmentdetails.departmentID=?;");   
		             ps.setString(1,employeeName);
		             ps.setString(2, departmentID);
		              ResultSet rs = ps.executeQuery();
		             
		              while (rs.next()) {
		              JSONObject record= new JSONObject();

		         	  JSONArray data= new JSONArray();
		         		
			          
				      record.put("employeeId", rs.getInt("employeeId"));
			          record.put("employeeName", rs.getString("employeeName"));
			          record.put("employeeAge", rs.getString("employeeAge"));
			          record.put("employeePass", rs.getString("employeePass"));
			          record.put("departmentID", rs.getString("departmentID"));  
		              record.put("departmentName", rs.getString("departmentName"));
		              record.put("Salary", rs.getInt("Salary"));
		              data.add(record);
			          out.println(data);
			        
			          log.info("successfully fetched the data");
		         }
			}
	            RequestDispatcher rd = req.getRequestDispatcher("Update");
	 			rd.include(req, res);
			}else {
				log.warn("please check the EmployeeName and EmployeePassword");
			}
			}
		 catch(Exception e) {
			
			e.printStackTrace();
		}
		
		
	}}



