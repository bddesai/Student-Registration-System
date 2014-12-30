package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Bhavin
 * This class is constructed to execute Query number 6 to show  
 * all students in particular class 
 */
public class sixth {

	 String classid;
	
	/**
	 * @param inclassid
	 */
	public sixth(String inclassid){
		this.classid = inclassid;
	}
	
	/**
	 * @throws SQLException
	 * establish the connection and fetch records
	 */
	public void show_studinclass() throws SQLException {
		Connection conn = null;
		CallableStatement cs =null;
		try
		{
		
		  //Connection to Oracle server
		  OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
		  ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
		  conn = ds.getConnection("username", "password");
		
		  //Prepare to call stored procedure:
		  cs = conn.prepareCall("{ call show_studinclass(?,?)}");
		  
		  //set all the in parameters  			
		  cs.setString(1, classid);  	//classid
		  
		  //register the out parameter (the first parameter)  
		  cs.registerOutParameter(2, OracleTypes.CURSOR);	// the system_refcursor to obtain the data
		  
		  // execute and retrieve the result set
		  cs.executeUpdate();
		  ResultSet rs = (ResultSet)cs.getObject(2);
		  
		  // print the results
		  while (rs.next()) {
		      System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) );
		  }
		
		  //close the result set, statement, and the connection
		  cs.close();
		  conn.close();
		  
		  
		} 
		catch (SQLException ex) { 
			// get the error messages based on requirements
			String message = ex.getMessage();
			String token[] = message.split(" ");
			if(token[2].equals("class"))
				System.out.println("The CLASSID is invalid");
			else if(token[2].equals("student"))
				System.out.println("NO student is enrolled in class");
			
		}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
		finally {
			//close connection
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