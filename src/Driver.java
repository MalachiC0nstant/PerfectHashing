package hash341;
import java.io.*;
public class Driver {

	public static void main(String[] args) {
		
		String filename = "C:\\Users\\semad\\eclipse-workspace\\PerfectHashing\\src\\US_Cities_LL.txt";
		CityTable citytable = new CityTable(filename, 16000);
		citytable.writeToFile("US_Cities_LL.ser");
	}
}
