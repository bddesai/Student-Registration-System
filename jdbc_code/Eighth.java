package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Bhavin
 * This class executes the a query which helps the student
 * to drop a certain class 
 */
public class Eighth {

	String sid,classid;
	
	/**
	 * @param insid
	 * @param inclassid
	 */
	public Eighth(String insid, String inclassid){
		this.sid=insid;
		this.classid=inclassid;
	}
	
	/**
	 * @throws SQLException
	 */
	public void drop_class() throws SQLException {
	    
		 Connection conn = null;
		 CallableStatement cs = null;   
		try
	    {
	        //Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
	        conn = ds.getConnection("username", "password");

	        //Prepare to call stored procedure:
	        cs = conn.prepareCall("{call drop_class(?,?) }");  //CHANGES IN HERE
	        
	        //set all the in parameters  			CHANGE IN HERE
	        cs.setString(1, sid);  	//sid
	        cs.setString(2, classid);	//classid
	        
	        // execute and retrieve the result set
	        cs.executeUpdate();
	        
	        System.out.println("The Student dropped the class successfully.");
	        
	        //close the result set, statement, and the connection
	        cs.close();
	        conn.close();
	        
	   } 
	   catch (SQLException ex) { 
		   	
		// get the error messages based on requirements
		    String message = ex.getMessage();
			String token[] = message.split(" ");

			if(token[1].equalsIgnoreCase("SID"))
				System.out.println("The SID is invalid");
			else if(token[1].equalsIgnoreCase("CLASSID"))
				System.out.println("The CLASSID is invalid");
			else if(token[1].equalsIgnoreCase("drop"))
				System.out.println("The drop is not permitted because another class uses it as a prerequisite");
			else if(token[1].equalsIgnoreCase("student"))
				System.out.println("This student is not enrolled in this or any of the classes");
			else if(token[1].equalsIgnoreCase("class"))
				System.out.println("The class has now no students");
			else 
				System.out.println("Some Connection Error! Please debug");
			
			
		  // System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());
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



