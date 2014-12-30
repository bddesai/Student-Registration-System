package jdbc_trial;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.pool.OracleDataSource;


/**
 * @author Bhavin
 * This is the 2nd query which will display all the records of the selected table 
 */
public class Query2_display {

	String input;
	Query2_display(String tablename){
		this.input=tablename;
	}
	
   public void show_data() throws SQLException {
	
    Connection conn = null;
    CallableStatement cs = null;   
	try
    {
        //Connection to Oracle server
        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111");
        conn = ds.getConnection("username", "password");
        cs = null;
        
        //Prepare to call stored procedure based on input
        switch(input){
    	case "students": 
    		cs = conn.prepareCall("{ call display.show_students(?)}");
    		break;
    	case "courses": 
    		cs = conn.prepareCall("{ call display.show_courses(?)}");
    		break;
    	case "prerequisites": 
    		cs = conn.prepareCall("{ call display.show_prerequisites(?)}");
    		break;
    	case "classes": 
    		cs = conn.prepareCall("{ call display.show_classes(?)}");
    		break;
    	case "enrollments": 
    		cs = conn.prepareCall("{ call display.show_enrollments(?)}");
    		break;
    	case "logs": 
    		cs = conn.prepareCall("{ call display.show_logs(?)}");
    		break;
    	default:
    		System.out.println("Invalid selection of a table");
        }
        
        // register the out parameter (the first parameter)
        cs.registerOutParameter(1, OracleTypes.CURSOR);
        
        // execute and retrieve the result set
        cs.executeUpdate();
        ResultSet rs = (ResultSet)cs.getObject(1);
        
        // print the results according to the column index of the selected table 
        while (rs.next()) {
        	
        	switch(input){
	        	case "students" :
	        		System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) +"\t"+ rs.getString(4) + "\t" + rs.getDouble(5) + "\t" + rs.getString(6));
	        		break;
	        	case "courses" :
	        		System.out.println(rs.getString(1) + "\t" + rs.getInt(2) + "\t" + rs.getString(3));
	        		break;
	        	case "prerequisites" :
	        		System.out.println(rs.getString(1) + "\t" + rs.getInt(2) + "\t" + rs.getString(3) +"\t"+ rs.getInt(4));
	        		break;
	        	case "classes" :
	        		System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getInt(3)+ "\t" + rs.getInt(4) + "\t" + rs.getInt(5) + "\t" + rs.getString(6)  
	        				+ "\t" + rs.getInt(7) + "\t" + rs.getInt(8));
	        		break;
	        	case "enrollments" :
	        		System.out.println(rs.getString(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3));
	        		break;
	        	case "logs" :
	        		System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getTime(3) + "\t" +rs.getString(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6));
	        		break;
	        	default:
	        		System.out.println("Invalid number of columns in a table");
        	}
        } //end while

        //close the result set, statement, and the connection
        cs.close();
        conn.close();
        
        System.out.println("\nRecords of '"+input+"' table displayed successfully\n");
   } 
   catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	 finally {
		 	// clode the connection
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



