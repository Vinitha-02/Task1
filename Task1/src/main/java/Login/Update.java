package Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class Update extends HttpServlet {
	static Logger log = Logger.getLogger(Update.class.getName());
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Database d=new Database();
	int employeeId=	d.getEmployeeId();
		
		res.setContentType("application/json");
		JSONObject record= new JSONObject();
		String  employeeAge= req.getParameter("employeeAge");
		String  employeePass= req.getParameter("employeePass");
	   
		
        PreparedStatement ps=null;
	    ResultSet rs=null;
	    PrintWriter out= res.getWriter();
	    HttpSession session = req.getSession(false);
	
		Connection c=null;
		if(session  != null)
		{
		
		String departmentID= (String) session.getAttribute("departmentID");
		
			
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
			c=DriverManager.getConnection("jdbc:mysql://localhost:3306/organisation","root","12345");
			String sql="UPDATE employeedetails SET employeeAge =?, employeePass=? WHERE employeeId =?";
			ps= c.prepareStatement(sql);
			
			log.info("Details are updated Successfully");
		    ps.setString(1,employeeAge);
		    ps.setString(2,employeePass);
			ps.setInt(3, employeeId);
	        ps.executeUpdate(); 
	 
	       out.println("Details are updated sucessfully");
	       ps= c.prepareStatement("select employeedetails.employeeId,employeedetails.employeeName,employeedetails.employeePass,employeedetails.employeeAge,departmentdetails.departmentID,departmentdetails.departmentName,departmentdetails.Salary from employeedetails,departmentdetails where employeedetails.employeeId=? and departmentdetails.departmentID=?;");   
	     
	       ps.setInt(1,employeeId);
           ps.setString(2,departmentID);
         
           rs = ps.executeQuery();
		
		    while (rs.next()) {
		         JSONArray data= new JSONArray();
			      record= new JSONObject();
				 record.put("employeeId", rs.getInt("employeeId"));
			     record.put("employeeName", rs.getString("employeeName"));
			     record.put("employeeAge", rs.getString("employeeAge"));
			     record.put("employeePass", rs.getString("employeePass"));
			     record.put("departmentID", rs.getString("departmentID"));
			     record.put("departmentName", rs.getString("departmentName"));
	            record.put("Salary", rs.getInt("Salary"));
               
			     data.add(record);
			     out.println(data);
			     log.info("Successfully fetched the data");
		         }
			}
		 catch(Exception e) {
	
			e.printStackTrace();
		}
    }

}}
