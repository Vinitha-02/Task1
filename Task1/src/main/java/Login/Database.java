package Login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
public class Database {
	static Logger log = Logger.getLogger(Database.class.getName());
	static int employeeId;
	static String departmentID;
	public static boolean validate(String employeeName, String employeePass) 
	
	{
		boolean status = false;  
      try{  
	     Class.forName("com.mysql.jdbc.Driver");
	     Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/organisation","root","12345");
	     log.info("Driver is connected");
		 PreparedStatement ps= con.prepareStatement("SELECT * FROM organisation.employeedetails  where employeeName=? and employeePass=?");
	     ps.setString(1,employeeName);  
	     ps.setString(2,employeePass); 
	     ResultSet rs=ps.executeQuery(); 
         if(rs.next()){
	      status= true;
}else {
	log.warn("Please check the EmployeeName and employeePassword");}
    employeeId= rs.getInt("employeeId");
    departmentID=rs.getString("departmentID");
    ps.close(); 
	con.close();
	}catch(Exception e)
    {e.printStackTrace();}
      
	return status;
}public int getEmployeeId() {
	    return employeeId;
	  }
	public String getDepartmentID() {
	    return departmentID;
	  }
}



