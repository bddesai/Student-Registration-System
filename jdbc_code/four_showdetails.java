package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;



/**
 * @author Bhavin
 * This class is for showing details based on sid 
 */
public class four_showdetails {

	String sid;
	
	/**
	 * @param insid
	 */
	public four_showdetails(String insid){
		this.sid=insid;
	}

	public void show_details() throws SQLException {
	Connection conn = null;
	CallableStatement cs =null;
	try
	{
	
	  //Connection to Oracle server
	  OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
	  ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
	  conn = ds.getConnection("username", "password");
	
	  //Prepare to call stored procedure:
	  cs = conn.prepareCall("{ call show_details(?,?)}");
	  
	  //set all the in parameters  			CHANGE IN HERE
	  cs.setString(1, sid);  	//sid
	  
	  //register the out parameter (the first parameter)
	  cs.registerOutParameter(2, OracleTypes.CURSOR);
	  
	  // execute and retrieve the result set
	  cs.executeUpdate();
	  ResultSet rs = (ResultSet)cs.getObject(2);
	  
	  // print the results
	  while (rs.next()) {
	      System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t" +  rs.getString(4));
	  }
	
	  //close the result set, statement, and the connection
	  cs.close();
	  conn.close();
	  
	  
	} 
	catch (SQLException ex) { 
		// get the error messages based on requirements
		String message = ex.getMessage();
		String token[] = message.split(" ");
		if(token[2].equals("sid"))
			System.out.println("The SID is invalid");
		else if(token[2].equals("student"))
			System.out.println("The student has not taken any course");
		
		//System.out.println ("\n*** SQLException caught ***\n" + token[2]);
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