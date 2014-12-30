package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;

/**
 * @author Bhavin
 * This class is for showing all the prerequisites of the courses
 */
public class Fifth{
	
	String deptcode;
	int courseno;
	
	/**
	 * @param dept_code
	 * @param course_no
	 */
	public Fifth(String dept_code, String course_no){
		this.deptcode=dept_code;
		this.courseno=Integer.parseInt(course_no);
	}
	
	/**
	 * @throws SQLException
	 */
	public void execute_fifth() throws SQLException{
		Connection conn = null;
		CallableStatement cs =null;
		try
		{
		
		  //Connection to Oracle server
		  OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
		  ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
		  conn = ds.getConnection("username", "password");
		
		  //Prepare to call stored procedure:
		  cs = conn.prepareCall("{ call prereq(?,?,?)}");
		  
		  //set all the in parameters  			CHANGE IN HERE
		  cs.setString(1, deptcode);  	//dept_code
		  cs.setInt(2, courseno);  	//course#
		  
		  //register the out parameter (the first parameter)
		  cs.registerOutParameter(3, OracleTypes.CURSOR);
		  
		  // execute and retrieve the result set
		  cs.executeUpdate();
		  ResultSet rs = (ResultSet)cs.getObject(3);
		  

		  // print the results
		  while (rs.next()) {
		      System.out.println(rs.getString(1) + "\t" + rs.getInt(2));
		  }
		
		  //close the result set, statement, and the connection
		  cs.close();
		  conn.close();
		  
		  
		} 
		catch (SQLException ex) { 
			// get the error messages based on requirements
			String message = ex.getMessage();
			String token[] = message.split(" ");
			if(token[1].equals("There"))
				System.out.println("There are no prerequisites for the given course");
			else if(token[1].equals("This"))
				System.out.println("This course is not valid");
			else
				System.out.println("Invalid Entry");
			
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
