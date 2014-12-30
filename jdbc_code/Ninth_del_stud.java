package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Bhavin
 * This class is used to delete a student from the class.
 * Throw appropriate exceptions 
 */
public class Ninth_del_stud {

	String sid;
	
	/**
	 * @param insid
	 */
	public Ninth_del_stud(String insid){
		this.sid=insid;
	}
		
	/**
	 * @throws SQLException
	 */
	public void delete_student() throws SQLException {
		Connection conn = null;
		CallableStatement cs =null;
		
		try
	    {
	        //Connection to Oracle server
	        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
	        conn = ds.getConnection("username", "password");

	        //Prepare to call stored procedure:
	        cs = conn.prepareCall("{call del_stud(?) }");  
	        
	        //set all the in parameters  			
	        cs.setString(1, sid);  	//sid
	        
	        // execute and retrieve the result set
	        cs.executeUpdate();
	        
	        System.out.println("Records successfully deleted");
	        
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
	   }
	   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
		finally {
			// close connection
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



