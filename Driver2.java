package hash341;
import java.util.Scanner;

public class Driver2 {

	public static void main(String[] args) {
		try {
			CityTable citytable = CityTable.readFromFile("US_Cities_LL.ser");
			
			Scanner myObj = new Scanner(System.in);  // Create a Scanner object
			while(true) {
				System.out.println("Enter City, State (or 'quit'): ");
			
				String cname = myObj.nextLine();  // Read user input
				if(cname.equals("quit")) {
					break;
				}
			
				City city = citytable.find(cname);
				if(city== null ) { 
					System.out.println("Could not find '" + cname + "'");
				}
				else {
					System.out.println("Found "+ cname + " ("+ city.longitude 
							+ ", " + city.latitude + ")");
					
					System.out.println("http://www.google.com/maps?z=10&q="+ city.longitude 
							+ "," + city.latitude);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
