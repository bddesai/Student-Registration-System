package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Bhavin
 *  This class executes the seventh query 
 */
public class seventh {

	String sid, classid;
	
	// get parameters from driver code
	public seventh(String insid, String inclassid){
		this.sid=insid;
		this.classid =inclassid;
	}
	
	/**
	 * @throws SQLException
	 * connect to database and retrieve records 
	 */
	public void enroll_student() throws SQLException{
		Connection conn = null;
		CallableStatement cs = null;   

		try {
			//Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
	        conn = ds.getConnection("username", "password");

	        //Prepare to call stored procedure:
	        cs = conn.prepareCall("{call stud_enroll(?,?) }"); 
	        
	        //set all the in parameters  			
	        cs.setString(1, sid);  	//sid
	        cs.setString(2, classid);  	//classid
	        
	        // execute and insert the records
	        cs.executeUpdate();
	        
	        System.out.println("\nThe Student has successfully registered.");
	        

	        //close the result set, statement, and the connection
	        cs.close();
	        conn.close();
		}  
		catch (SQLException ex) {
			
			
			// get the error messages based on requirements
			String message = ex.getMessage();
			String token[] = message.split(" ");
			if(token[2].equalsIgnoreCase("sid"))
				System.out.println("The SID is invalid");
			else if(token[2].equalsIgnoreCase("classid"))
				System.out.println("The CLASSID is invalid");
			else if(token[2].equalsIgnoreCase("class"))
				System.out.println("The class is closed");
			else if(token[2].equalsIgnoreCase("student"))
				System.out.println("The student is already in this class");
			else if(token[2].equalsIgnoreCase("Overloaded"))
				System.out.println("You are Overloaded");
			else if(token[2].equalsIgnoreCase("enroll"))
				System.out.println("Cannot enroll in more than 4 classes in the same year and same semester");
		}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
		finally {
			// close the connection
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
