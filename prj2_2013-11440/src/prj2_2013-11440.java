import java.sql.*;
import java.io.*;

class prj2 {

	static String doubleLine = "============================================================";
	static String singleLine = "---------------------------------------------------------------------------";
	
	public static void main(String[] args)
	{	
		String serverName = "147.46.15.238";
		String dbName = "DB-2013-11440";
		String userName = "DB-2013-11440";
		String password = "DB-2013-11440"; 
		String url = "jdbc:mariadb://" + serverName + "/" + dbName;
		String initMsg = "1. print all universities\n2. print all students\n3. insert a new university\n4. remove a university\n5. insert a new student\n6. remove a student\n7. make an application\n8. print all students who applied for a university\n9. print all universities a student applied for\n10. print expected successful applicants of a university\n11. print universities expected to accept a student\n12. Exit\n" + doubleLine;
		Connection conn;
		
		System.out.println(doubleLine);
		while (true)
		{
			
			try {
				conn = DriverManager.getConnection(url, userName, password);
				
				System.out.println(initMsg);
				System.out.print("Select yout action: ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				int input = Integer.parseInt(br.readLine());
				
				switch(input) {
				case 1:
					printAllUniv(conn);
					break;
				case 2:
					printAllStud(conn);
					break;
				case 3:
					insertNewUniv(conn);
					break;
				case 4:
					removeUniversity(conn);
					break;
				case 5:
					insertNewStud(conn);
					break;
				case 6:
					removeStudent(conn);
					break;
				case 7:
					makeApplication(conn);
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 11:
					break;
				case 12:
					System.out.println("Bye!");
					System.exit(0);
				default:
					System.out.println("Invalid action.");
				}
					
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* 1. print all universities */
	static void printAllUniv(Connection conn) {
		try {
			int id, capacity;
			String name, group;
			float weight, applied;
			
			String selectSql = "SELECT * FROM university";
			PreparedStatement selectStmt = conn.prepareStatement(selectSql);
			ResultSet selectRs = selectStmt.executeQuery();
			
			if (!selectRs.first()) {
				/* SQL result is empty */
			} else {
				System.out.println(singleLine);
				System.out.println("id\tname\t\t\tcapacity\tgroup\tweight\tapplied");
				System.out.println(singleLine);
				do {
					id = selectRs.getInt("id");
					name = selectRs.getString("name");
					capacity = selectRs.getInt("capacity");
					group = selectRs.getString("group");
					weight = selectRs.getFloat("weight");
					applied = selectRs.getFloat("applied");
					
					System.out.println(id+"\t"+name+"\t\t\t"+capacity+"\t\t"+group+"\t"+weight+"\t"+applied);					
				} while (selectRs.next());
				System.out.println(singleLine);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(doubleLine);
	}
	
	/* 2. print all students */
	static void printAllStud(Connection conn) {
		try {
			int id, csat_score, school_score;
			String name;
			
			String selectSql = "SELECT * FROM student";
			PreparedStatement selectStmt = conn.prepareStatement(selectSql);
			ResultSet selectRs = selectStmt.executeQuery();
			
			if (!selectRs.first()) {
				/* SQL result is empty */
			} else {
				System.out.println(singleLine);
				System.out.println("id\t\t\tname\t\t\tcsat_score\tschool_score");
				System.out.println(singleLine);
				do {
					id = selectRs.getInt("id");
					name = selectRs.getString("name");
					csat_score = selectRs.getInt("csat_score");
					school_score = selectRs.getInt("school_score");
					
					System.out.println(id+"\t\t\t"+name+"\t\t\t"+csat_score+"\t\t"+school_score);					
				} while (selectRs.next());
				System.out.println(singleLine);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(doubleLine);
	}

	/* 3. insert a new university */
	static void insertNewUniv(Connection conn) {
		try {			
			String insertSql = "INSERT INTO university VALUES(?, ?, ?, ?, ?, ?)";
			/* 1:id, 2:name, 3:capacity, 4:group, 5:weight, 6:applied */
			PreparedStatement insertStmt = conn.prepareStatement(insertSql);

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("University name: ");
			String name = br.readLine();
			if (name.length() > 128) {} /* truncate */
			insertStmt.setString(2, name);
			
			System.out.print("University capacity: ");
			int capacity = Integer.parseInt(br.readLine());
			if (capacity < 1) {
				System.out.println("Capacity should be over 0.");
				System.out.println(doubleLine);
				return;
			}
			insertStmt.setInt(3, capacity);
			
			System.out.print("University group: ");
			String group = br.readLine();
			if (group.compareTo("A") != 0 && group.compareTo("B") != 0 && group.compareTo("C") != 0) {
				System.out.println("Group should be 'A', 'B', or 'C'.");
				System.out.println(doubleLine);
				return;
			}
			insertStmt.setString(4, group);
			
			System.out.print("Weight of high school records: ");
			float weight = Float.parseFloat(br.readLine());
			if (weight < 0.0) {
				System.out.println("Weight of high school records cannot be negative.");
				System.out.println(doubleLine);
				return;
			}
			insertStmt.setFloat(5, weight);
			
			/* applied is initialized to 0 */
			insertStmt.setInt(6, 0);
			
			/* get next id */
			String getIdSql = "SELECT max(id) FROM university";
			PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
			ResultSet getIdRs = getIdStmt.executeQuery();
			int id = 1;
			if (getIdRs.first())
				id = getIdRs.getInt("max(id)") + 1;
			insertStmt.setInt(1, id);
			
			insertStmt.executeUpdate();
			System.out.println("A university is successfully inserted.");
			System.out.println(doubleLine);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 4. remove a university */
	static void removeUniversity(Connection conn) {
		try {
			System.out.println("A university is successfully deleted.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 5. insert a new student */
	static void insertNewStud(Connection conn) {
		try {			
			String insertSql = "INSERT INTO student VALUES(?, ?, ?, ?)";
			/* 1:id, 2:name, 3:csat_score, 4:school_score */
			PreparedStatement insertStmt = conn.prepareStatement(insertSql);

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Student name: ");
			String name = br.readLine();
			insertStmt.setString(2, name);
			
			System.out.print("CSAT score: ");
			int csat_score = Integer.parseInt(br.readLine());
			if (csat_score > 400 || csat_score < 0) {
				System.out.println("CSAT score should be between 0 and 400.");
				System.out.println(doubleLine);
				return;
			}
			insertStmt.setInt(3, csat_score);
			
			System.out.print("High school record score: ");
			int school_score = Integer.parseInt(br.readLine());
			if (school_score > 100 || csat_score < 0) {
				System.out.println("High school records score should be between 0 and 100.");
				System.out.println(doubleLine);
				return;
			}
			insertStmt.setInt(4, school_score);
			
			/* get next id */
			String getIdSql = "SELECT max(id) FROM student";
			PreparedStatement getIdStmt = conn.prepareStatement(getIdSql);
			ResultSet getIdRs = getIdStmt.executeQuery();
			int id = 1;
			if (getIdRs.first())
				id = getIdRs.getInt("max(id)") + 1;
			insertStmt.setInt(1, id);
			
			insertStmt.executeUpdate();
			System.out.println("A student is successfully inserted.");
			System.out.println(doubleLine);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 6. remove a student */
	static void removeStudent(Connection conn) {
		try {
			System.out.println("A student is successfully deleted.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 7. make an application */
	static void makeApplication(Connection conn) {
		try {
			String insertSql = "INSERT INTO apply VALUES(?, ?, ?, ?)";
			/* 1:stud_id, 2:univ_id, 3:group, 4:weight */
			PreparedStatement insertStmt = conn.prepareStatement(insertSql);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Student Id: ");
			int stud_id = Integer.parseInt(br.readLine());
			/* check stud_id is valid data */
			String checkSql = "SELECT * FROM student WHERE id = " + stud_id;
			PreparedStatement checkStmt = conn.prepareStatement(checkSql);
			ResultSet checkRs = checkStmt.executeQuery();
			if (!checkRs.first()) {
				System.out.println("Student " + stud_id + " doesn't exist.");
				System.out.println(doubleLine);
				return;
			}
			insertStmt.setInt(1, stud_id);
			
			System.out.println("University Id: ");
			int univ_id = Integer.parseInt(br.readLine());
			insertStmt.setInt(2, univ_id);
			
			/* check univ_id is valid data and get group & weight info in university table */
			String getInfoSql = "SELECT * FROM university WHERE id = " + univ_id;
			PreparedStatement getInfoStmt = conn.prepareStatement(getInfoSql);
			ResultSet getInfoRs = getInfoStmt.executeQuery();
			
			/* check univ_id is valid data */
			if (!getInfoRs.first()) {
				System.out.println("University " + univ_id + "doesn't exist.");
				System.out.println(doubleLine);
				return;
			} else {
				String group = getInfoRs.getString("group");
				float weight = getInfoRs.getFloat("weight");
				
				/* check application is one university per group */
				checkSql = "SELECT * FROM apply WHERE stud_id = " + stud_id;
				checkStmt = conn.prepareStatement(checkSql);
				checkRs = checkStmt.executeQuery();
				if (checkRs.first()) {
					do {
						String compareGroup = checkRs.getString("group");
						if (group.compareTo(compareGroup) == 0) {
							System.out.println("A student can apply up to one university per group.");
							System.out.println(doubleLine);
							return;
						}						
					} while (checkRs.next());
				}
				
				insertStmt.setString(3, group);
				insertStmt.setFloat(4, weight);
				insertStmt.executeUpdate();
				System.out.println("Successfully made an application");
				System.out.println(doubleLine);
			}
			
			/* update 'applied' in university table */
			updateApplied(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void updateApplied(Connection conn) {
		
	}
}