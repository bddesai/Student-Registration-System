package jdbc_trial;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Bhavin
 * This is the main driver code where you will get all the user input 
 *  and feed all parameters to the class and call approriate methods.
 *  Throw exceptions appropriately
 */
public class Driver {

	private static Scanner reader;

	/**
	 * @param args
	 * Main function 
	 */
	public static void main(String[] args) {
		
		
			
		while(true){
			try {
				System.out.println("\n--> Enter [1..10] for running your desired query");
				System.out.println("    2. Display   3. Add Student   4. Show Details    5. Show Prerequisites");
				System.out.println("    6. Show Student Enrollment    7. Enroll Student  8. Drop Class   9. Delete Student\n");
				
				System.out.print("Input Number :");
				reader = new Scanner(System.in);
				int query_no = -1;
				try{
				query_no = reader.nextInt();
				}catch(InputMismatchException e){e.getMessage();}
				
				// validate your input 
				if(query_no <1 && query_no>10){
					System.out.println("\nError: Invalid selection\n");
					System.exit(0);
				}
				
				switch(query_no){
				
				case 1:	// Query one: The sequence 
					System.out.println("The sequence is generated through script.");
					break;
				case 2: // Query 2
					System.out.print("Enter table name:");
					Scanner tableinput = new Scanner(System.in);
					String tablename = tableinput.next();
					Query2_display data = new Query2_display(tablename);
					try {
						data.show_data();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 3: // Query 3 : add student
					System.out.print("\nEnter Student Details (Comma Seperated):");
					Scanner stud = new Scanner(System.in);
					String stud_details = stud.next();
					add_student as = new add_student(stud_details);
					try {
						as.add_stud();
					} catch (SQLException e1) {
						System.out.println(e1.getMessage());
					}
					break;
				case 4:  // Query 4
					System.out.print("\nEnter SID: ");
					Scanner sid = new Scanner(System.in);
					String stud_id = sid.next();
					
					// pass parameters and call the method
					four_showdetails fsd = new four_showdetails(stud_id);
					try {
						fsd.show_details();
					} catch (SQLException e1) {
						System.out.println(e1.getMessage());
					}
					break;
				case 5: // Query 5
					System.out.print("\nEnter Dept Code: ");
					Scanner five= new Scanner(System.in);
					String deptcode= five.next();
					System.out.print("\nEnter Course Number: ");
					String courseno = five.next();
					
					// pass parameters and call the method
					Fifth f = new Fifth(deptcode, courseno);
					try {
						f.execute_fifth();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
					
					break;
				case 6: //Query 6
					System.out.print("\nEnter CLASSID: ");
					Scanner cid = new Scanner(System.in);
					String classid = cid.next();
					
					// pass parameters and call the method
					sixth six = new sixth(classid);
					try {
						six.show_studinclass();
					} catch (SQLException e1) {
						System.out.println(e1.getMessage());
					}
					break;
				case 7: // Query 7: enroll students
					// take inputs
					System.out.print("\nEnter SID: ");
					Scanner seven= new Scanner(System.in);
					String sid7= seven.next();
					System.out.print("\nEnter CLASSID: ");
					String classid7 = seven.next();
					
					// pass parameters and call the method
					seventh svn = new seventh(sid7, classid7);
					try {
						svn.enroll_student();
					} catch (SQLException e2) {
						System.out.println(e2.getMessage());
					}
					
					break;
				case 8: // Query 8
					System.out.print("\nEnter SID: ");
					Scanner eight= new Scanner(System.in);
					String sid8= eight.next();
					System.out.print("\nEnter CLASSID: ");
					String classid8 = eight.next();
					
					// pass parameters and call the method
					Eighth	ei = new Eighth(sid8, classid8);
					try {
						ei.drop_class();
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 9: // Query 9 
					System.out.print("\nEnter SID to delete: ");
					Scanner ssid = new Scanner(System.in);
					String sstud_id = ssid.next();
					
					// pass parameters and call the method
					Ninth_del_stud nds = new Ninth_del_stud(sstud_id);
					try {
						nds.delete_student();
					} catch (SQLException e1) {
						System.out.println(e1.getMessage());
					}
					break;
				case 10: System.out.println("The triggers are generated through script.");
				break;
				default: System.out.println("\nWRONG INPUT. Please check again.\n");
					break;
				
				}  //end switch
			} catch (Exception e) {
				System.out.println("\nYour entry caused an exception \n");
			}
		}//end while
		
			
	}
}
