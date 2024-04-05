import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.ConnectionFactory;

public class Lab4 {
	public static void main(String[] args) {
		UIStart();
	}
	
	public static void displaySchedule(String StartLocationName, String DestinationName, String Date) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			
			final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Trip T INNER JOIN TripOffering O ON T.TripNumber=O.TripNumber WHERE T.StartLocationName = ? AND T.DestinationName = ? AND O.Date = TO_DATE(?, 'YYYY-MM-DD')");
			
			stmt.setString(1, StartLocationName);
			stmt.setString(2, DestinationName);	
			stmt.setString(3, Date);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
		        System.out.printf("TripNumber: %s, StartLocationName: %s, DestinationName: %s, Date: %s, ScheduledStartTime: %s, SecheduledArrivalTime: %s, DriverName: %s, BusID: %d\n", 
		        		rs.getString("TripNumber"), rs.getString("StartLocationName"), rs.getString("DestinationName"), rs.getString("Date"), rs.getString("ScheduledStartTime"), 
		        		rs.getString("SecheduledArrivalTime"), rs.getString("DriverName"), rs.getInt("BusID"));

		    }
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void deleteTripOffering(String TripNumber, String Date, String ScheduledStartTime) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("DELETE FROM TripOffering T WHERE T.TripNumber = ? AND T.Date = TO_DATE(?, 'YYYY-MM-DD') AND T.ScheduledStartTime = ?");
			
			stmt.setString(1, TripNumber);
			stmt.setString(2, Date);	
			stmt.setString(3, ScheduledStartTime);
			
			stmt.executeUpdate();
			
			connection.commit();
			
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	// returns True if insert is successful
	public static boolean addTripOffering(String TripNumber, String Date, String ScheduledStartTime, String SecheduledArrivalTime, String DriverName, int BusID) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("INSERT INTO TripOffering(TripNumber, Date, ScheduledStartTime, SecheduledArrivalTime, "
					+ "DriverName, BusID) VALUES(?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?,?)");
			
			stmt.setString(1, TripNumber);
			stmt.setString(2, Date);	
			stmt.setString(3, ScheduledStartTime);
			stmt.setString(4, SecheduledArrivalTime);
			stmt.setString(5, DriverName);
			stmt.setInt(6, BusID);
			
			stmt.executeUpdate();
			
			connection.commit();
			
			return true;
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	// returns True if update is successful
	public static boolean updateTripDriver(String driverName, String TripNumber, String Date, String ScheduledStartTime) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("UPDATE TripOffering SET DriverName = ? WHERE TripOffering.TripNumber = ? AND TripOffering.Date = TO_DATE(?, 'YYYY-MM-DD') AND TripOffering.ScheduledStartTime = ?");
			
			stmt.setString(1, driverName);
			stmt.setString(2, TripNumber);
			stmt.setString(3, Date);	
			stmt.setString(4, ScheduledStartTime);
			
			stmt.executeUpdate();
			
			connection.commit();
			
			return true;
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	// returns True if update is successful
	public static boolean updateTripBus(int BusID, String TripNumber, String Date, String ScheduledStartTime) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("UPDATE TripOffering SET BusID = ? WHERE TripOffering.TripNumber = ? AND TripOffering.Date = TO_DATE(?, 'YYYY-MM-DD') AND TripOffering.ScheduledStartTime = ?");
			
			stmt.setInt(1, BusID);
			stmt.setString(2, TripNumber);
			stmt.setString(3, Date);	
			stmt.setString(4, ScheduledStartTime);
			
			stmt.executeUpdate();
			
			connection.commit();
			
			return true;
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static void displayTripStopInfo(String TripNumber) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			
			final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM TripStopInfo T INNER JOIN Stop S ON T.StopNumber=S.StopNumber WHERE T.TripNumber = ? ORDER BY T.StopNumber ASC");
			
			stmt.setString(1, TripNumber);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
		        System.out.printf("TripNumber: %s, StopNumber: %s, StopAddress: %s, SequenceNumber: %s, DrivingTime: %s\n", 
		        		rs.getString("TripNumber"), rs.getString("StopNumber"), rs.getString("StopAddress"), rs.getString("SequenceNumber"), rs.getString("DrivingTime"));
		        
		    }
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public static void displayWeeklySchedule(String DriverName, String Date) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			
			final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM TripOffering T WHERE T.DriverName = ? AND T.Date >= TO_DATE(?, 'YYYY-MM-DD') - 3 AND T.Date <= TO_DATE(?, 'YYYY-MM-DD') + 3");
			
			stmt.setString(1, DriverName);
			stmt.setString(2, Date);
			stmt.setString(3, Date);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
		        System.out.printf("TripNumber: %s, Date: %s, ScheduledStartTime: %s, SecheduledArrivalTime: %s, DriverName: %s, BusID: %s\n", 
		        		rs.getString("TripNumber"), rs.getString("Date"), rs.getString("ScheduledStartTime"), rs.getString("SecheduledArrivalTime"), rs.getString("DriverName"), rs.getString("BusID"));
		        
		    }
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	// return True if insert is successful
	public static boolean addDriver(String DriverName, String DriverTelephoneNumber) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("INSERT INTO Driver(DriverName, DriverTelephoneNumber) VALUES(?,?)");
			
			stmt.setString(1, DriverName);
			stmt.setString(2, DriverTelephoneNumber);	
			
			stmt.executeUpdate();
			
			connection.commit();
			
			return true;
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	// return True if insert is successful
	public static boolean addBus(int BusID, String model, String year) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("INSERT INTO Bus(BusID, Model, Year) VALUES(?,?,?)");
			
			stmt.setInt(1, BusID);
			stmt.setString(2, model);	
			stmt.setString(3, year);
			
			stmt.executeUpdate();
			
			connection.commit();
			
			return true;
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public static void deleteBus(int BusID) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("DELETE FROM Bus B WHERE B.BusID = ?");
			
			stmt.setInt(1, BusID);
			
			stmt.executeUpdate();
			
			connection.commit();
			
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	// return True if insert is successful
	public static boolean insertActualStopTripInfo(String TripNumber, String Date, String ScheduledStartTime, int StopNumber,
			String SecheduledArrivalTime, String ActualStartTime, String ActualArrivalTime, int NumberOfPassengerIn,
			int NumberOfPassengerOut) {
		try {
			Connection connection = ConnectionFactory.getConnection();
			connection.setAutoCommit(false);
			
			final PreparedStatement stmt = connection.prepareStatement("INSERT INTO ActualTripStopInfo(TripNumber, Date, ScheduledStartTime, StopNumber, "
					+ "SecheduledArrivalTime, ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, NumberOfPassengerOut) VALUES(?,TO_DATE(?, 'YYYY-MM-DD'),?,?,?,?,?,?,?)");
			
			stmt.setString(1, TripNumber);
			stmt.setString(2, Date);	
			stmt.setString(3, ScheduledStartTime);
			stmt.setInt(4, StopNumber);
			stmt.setString(5, SecheduledArrivalTime);	
			stmt.setString(6, ActualStartTime);
			stmt.setString(7, ActualArrivalTime);
			stmt.setInt(8, NumberOfPassengerIn);
			stmt.setInt(9, NumberOfPassengerOut);
			
			stmt.executeUpdate();
			
			connection.commit();
			
			return true;
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	
	public static void UIStart() {
		Scanner input = new Scanner(System.in);
		String choice;
		
		while (true) {
			try {
				System.out.println("Please select an option");
				System.out.println("1. Display Schedule");
				System.out.println("2. Edit Schedule");
				System.out.println("3. Display Stops");
				System.out.println("4. Display Driver Weekly Schedule");
				System.out.println("5. Add Driver");
				System.out.println("6. Add Bus");
				System.out.println("7. Delete Bus");
				System.out.println("8. Record Actual Trip Stop Info");
				System.out.println("0. Quit");
				
				choice = input.nextLine();
				
				if (choice.equals("1")) {
					choice1(input);
					continue;
				} else if (choice.equals("2")) {
					choice2(input);
					continue;
				} else if (choice.equals("3")) {
					choice3(input);
					continue;
				} else if (choice.equals("4")) {
					choice4(input);
					continue;
				} else if (choice.equals("5")) {
					choice5(input);
					continue;
				} else if (choice.equals("6")) {
					choice6(input);
					continue;
				} else if (choice.equals("7")) {
					choice7(input);
					continue;
				} else if (choice.equals("8")) {
					choice8(input);
					continue;
				} else if (choice.equals("0")) {
					break;
				} else {
					System.out.println("Please enter an valid option");
				}
			} catch(Exception e) {
				System.out.println("Invalid Input, please retry");
			}
			
		}
	}
	
	public static void choice1(Scanner input) {
		String paramList;
		String[] param;
		System.out.println("Enter StartLocationName, DestinationName, and Date (separated by commas and no spaces)");
		paramList = input.nextLine();
		param = paramList.split(",");
		
		System.out.println();
		System.out.printf("Listing all trips with StartLocationName: %s, DestinationName: %s, Date:%s \n", param[0], param[1], param[2]);
		System.out.println("---------------------------------------------------------------------------------");
		displaySchedule(param[0], param[1], param[2]);
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Press ENTER to return to main menu");
		input.nextLine();
	}
	
	public static void choice2(Scanner input) {
		
		System.out.println("What do you want to edit?");
		System.out.println("1. Delete Trip Offering");
		System.out.println("2. Add Trip Offering");
		System.out.println("3. Change Driver");
		System.out.println("4. Change Bus");
		System.out.println("");
		String choice = input.nextLine();
		
		String paramList;
		String[] param;
		
		if (choice.equals("1")) {
			System.out.println("Enter TripNumber, Date, and ScheduledStartTime (separated by commas and no spaces)");
			paramList = input.nextLine();
			param = paramList.split(",");
			
			System.out.println();
			System.out.printf("Deleting trip offering with TripNumber: %s, Date: %s, ScheduledStartTime: %s \n", param[0], param[1], param[2]);
			deleteTripOffering(param[0], param[1], param[2]);
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("Press ENTER to return to main menu");
			input.nextLine();
			
		} else if (choice.equals("2")) {
			while (true) {
				System.out.println("Enter TripNumber, Date, ScheduledStartTime, SecheduledArrivalTime, DriverName, and BusID (separated by commas and no spaces)");
				paramList = input.nextLine();
				param = paramList.split(",");
				
				System.out.println();
				System.out.printf("Adding trip offering with TripNumber: %s, Date: %s, ScheduledStartTime: %s, SecheduledArrivalTime: %s, DriverName: %s, BusID: %s \n", param[0], param[1], param[2], param[3], param[4], param[5]);
				boolean result = addTripOffering(param[0], param[1], param[2], param[3], param[4], Integer.parseInt(param[5]));
				
				// Check if insert was successful
				if (result) {
					System.out.println("Sucessfully added trip offering");
				} else {
					System.out.println("Insert failed! Check that all variables exists");
				}
				
				System.out.println("---------------------------------------------------------------------------------");
				
				System.out.println("Type 1 to add another trip offering or Press ENTER to return to main menu");
				String repeat = input.nextLine();
				if (!repeat.equals("1")) {
					break;
				}
			}
		} else if (choice.equals("3")) {
			System.out.println("Enter TripNumber, Date, and ScheduledStartTime (separated by commas and no spaces)");
			paramList = input.nextLine();
			param = paramList.split(",");
			
			System.out.println("Enter New Driver Name");
			String driverName = input.nextLine();
			
			System.out.println();
			System.out.printf("Changing driver name to %s on trip offering with TripNumber: %s, Date: %s, ScheduledStartTime: %s \n", driverName, param[0], param[1], param[2]);
			boolean result = updateTripDriver(driverName, param[0], param[1], param[2]);
			
			if (result) {
				System.out.println("Successfully changed driver name");
			} else {
				System.out.println("Update failed! Check that driver exists");
			}
			
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("Press ENTER to return to main menu");
			input.nextLine();
			
		} else if (choice.equals("4")) {
			System.out.println("Enter TripNumber, Date, and ScheduledStartTime (separated by commas and no spaces)");
			paramList = input.nextLine();
			param = paramList.split(",");
			
			System.out.println("Enter New Bus ID");
			int busID = input.nextInt();
			input.nextLine();
			
			System.out.println();
			System.out.printf("Changing driver name to %d on trip offering with TripNumber: %s, Date: %s, ScheduledStartTime: %s \n", busID, param[0], param[1], param[2]);
			boolean result = updateTripBus(busID, param[0], param[1], param[2]);
			
			if (result) {
				System.out.println("Successfully changed busID");
			} else {
				System.out.println("Update failed! Check that bus exists");
			}
			
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("Press ENTER to return to main menu");
			input.nextLine();
			
		} else {
			System.out.println("No option selected");
			System.out.println("Returning to main menu");
			
			// wait 2 seconds
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				System.out.println();
				e.printStackTrace();
			}
		}
	}
	
	public static void choice3(Scanner input) {
		System.out.println("Enter TripNumber");
		String TripNum = input.nextLine();
		
		System.out.println();
		System.out.printf("Listing trip stop info of TripNumber:%s \n", TripNum);
		System.out.println("---------------------------------------------------------------------------------");
		displayTripStopInfo(TripNum);
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Press ENTER to return to main menu");
		input.nextLine();
	}
	
	public static void choice4(Scanner input) {
		String paramList;
		String[] param;
		System.out.println("Enter DriverName and Date (separated by commas and no spaces)");
		paramList = input.nextLine();
		param = paramList.split(",");
		
		System.out.println();
		System.out.printf("Listing weekly schedule of %s on %s \n", param[0], param[1]);
		System.out.println("---------------------------------------------------------------------------------");
		displayWeeklySchedule(param[0], param[1]);
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Press ENTER to return to main menu");
		input.nextLine();
	}
	
	public static void choice5(Scanner input) {
		String paramList;
		String[] param;
		System.out.println("Enter DriverName and DriverTelephoneNumber (separated by commas and no spaces)");
		paramList = input.nextLine();
		param = paramList.split(",");
		
		System.out.println();
		System.out.printf("Adding Driver %s with DriverTelephoneNumber %s \n", param[0], param[1]);

		boolean result = addDriver(param[0], param[1]);

		if (result) {
			System.out.println("Successfully added driver");
		} else {
			System.out.println("Insert failed! That driver already exists");
		}
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Press ENTER to return to main menu");
		input.nextLine();
	}
	
	public static void choice6(Scanner input) {
		String paramList;
		String[] param;
		System.out.println("Enter BusID, model, and year (separated by commas and no spaces)");
		paramList = input.nextLine();
		param = paramList.split(",");
		
		System.out.println();
		System.out.printf("Adding Bus with BusID: %s, model: %s, year: %s \n", param[0], param[1], param[2]);
		
		boolean result = addBus(Integer.parseInt(param[0]), param[1], param[2]);
		
		if (result) {
			System.out.println("Successfully added bus");
		} else {
			System.out.println("Insert failed! That bus already exists");
		}
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Press ENTER to return to main menu");
		input.nextLine();
	}
	
	public static void choice7(Scanner input) {
		System.out.println("Enter BusID");
		int busID = input.nextInt();
		input.nextLine();
		
		System.out.println();
		System.out.printf("Removing Bus with BusID: %d \n", busID);
		
		deleteBus(busID);
		
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Press ENTER to return to main menu");
		input.nextLine();
	}
	
	public static void choice8(Scanner input) {
		String paramList;
		String[] param;
		System.out.println("Enter TripNumber, Date, ScheduledStartTime, StopNumber, "
				+ "SecheduledArrivalTime, ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, "
				+ "NumberOfPassengerOut (separated by commas and no spaces)");
		paramList = input.nextLine();
		param = paramList.split(",");
		
		System.out.println();
		System.out.printf("Adding StopTripInfo for TripNumber: %s, Date: %s, ScheduledStartTime: %s, StopNumber: %s, "
				+ "SecheduledArrivalTime: %s, ActualStartTime: %s, ActualArrivalTime: %s, "
				+ "NumberOfPassengerIn: %s, NumberOfPassengerOut: %s \n", param[0], param[1], param[2], param[3], param[4], param[5], param[6], param[7], param[8]);

		boolean result = insertActualStopTripInfo(param[0], param[1], param[2], Integer.parseInt(param[3]), param[4], param[5], 
				param[6], Integer.parseInt(param[7]), Integer.parseInt(param[8]));

		if (result) {
			System.out.println("Successfully added StopTripInfo");
		} else {
			System.out.println("Insert failed! Check that all variables exists");
		}
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Press ENTER to return to main menu");
		input.nextLine();
	}
	
}
