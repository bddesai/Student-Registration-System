package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Bhavin
 * This class helps you to add students based on inputs provided 
 * from the driver code through user input
 */
public class add_student {

	String stud_details;
	
	/**
	 * @param details
	 */
	public add_student(String details){
		this.stud_details=details;
	}
	
	
    /**
    * @throws SQLException
    */
	public void add_stud() throws SQLException {
	   Connection conn = null;
	   CallableStatement cs = null;
	   
	try
    {
    	
    	String data[] = stud_details.split(",");
    	if(data.length!=6){
    			System.out.println("Invalid number of arguments");
    	}

        //Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
        conn = ds.getConnection("username", "password");

        //Prepare to call stored procedure:
        cs = conn.prepareCall("{call add_stud(?,?,?,?,?,?) }"); 
        
        //set all the in parameters  			
        cs.setString(1, data[0]);  	//sid
        cs.setString(2, data[1]);  	//firstname
        cs.setString(3, data[2]);  	//lastname
        cs.setString(4, data[3]);  	//status
        cs.setDouble(5, Double.parseDouble(data[4]));	//gpa
        cs.setString(6, data[5]); 	//email
        
        // execute and retrieve the result set
        cs.executeUpdate();
        
        System.out.println("\n1 Student row inserted successfully");
        

        //close the result set, statement, and the connection
        cs.close();
        conn.close();
        
        
   } 
   catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
    finally {
    	// close the cconnection
		if(cs!=null || conn!=null){
			try{
				cs.close();
				conn.close();
			}
			catch(SQLException ex){System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		}
	}
  }
} 



