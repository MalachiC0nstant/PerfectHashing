package hash341;

import java.io.*;
import java.util.*;
import java.lang.Integer;
public class CityTable {
	
	private class Slot implements Serializable {
		int count;
		City city;
		Hash24 hash24;
		ArrayList<City> sec_table; // count ^ 2
		
		private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
			stream.writeInt(count);
		    stream.writeObject(city);
		    stream.writeObject(hash24);
		    stream.writeObject(sec_table);
		}
		
	    private void readObject(java.io.ObjectInputStream stream)
	        throws IOException, ClassNotFoundException {
	        	count = stream.readInt();
	        	city = (City) stream.readObject();
	        	hash24 =  (Hash24)stream.readObject();
	        	sec_table =  (ArrayList<City>)stream.readObject();
	    }		

		void init() {
			hash24 = new Hash24();				
			sec_table = new ArrayList<City>(count*count);
			for (int i = 0; i < (count*count); i++)
				sec_table.add(new City("", 0.f, 0.f));			
		}
		
		int add(City new_city) {
			int attempts = 0;
			if (count == 1) {
				city = new_city;
			} else { // > 1
				if (sec_table == null) {
					this.init();
					attempts++;		
				}
				int index = hash24.hash(new_city.name) % (count * count);
				if (sec_table.get(index).name.isEmpty()) {
					sec_table.set(index, new_city);
				} else {
					ArrayList<City> sec_table_saved = sec_table;
					
					while (true) {
						attempts++;
						this.init();
						boolean flag = true;						
						for (City saved_city: sec_table_saved) {
							if (saved_city.name.isEmpty())
								continue;
							int index2 = hash24.hash(saved_city.name) % (count * count);
							if (sec_table.get(index2).name.isEmpty()) {
								sec_table.set(index2, saved_city);
							} else {
								flag = false;
								break;
							}
						}
						if (flag) {
							int index3 = hash24.hash(new_city.name) % (count * count);
							if (sec_table.get(index3).name.isEmpty()) {
								attempts++;
								sec_table.set(index3, new_city);
								break;
							}
						}
					}
				}
			}
			return attempts;
		}
	}
	
	
	
	Hash24 hash24;
	ArrayList<Slot> table;
	
	CityTable() {}
	
	CityTable(String fname, int tsize){
		hash24 = new Hash24();
		System.out.println("Primary hash table hash function:");
		hash24.dump();
		
		Scanner infile = null;
		try
		{
			infile = new Scanner(new FileReader(fname));
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File not found");
			e.printStackTrace(); // prints error(s)
			System.exit(0); // Exits entire program
		}
		
		ArrayList<City> cities = new ArrayList<City>();
		while(infile.hasNextLine())
		{
			String name = infile.nextLine();
			String line2 = infile.nextLine();
			
			StringTokenizer tokenizer = new StringTokenizer(line2);

			// You should know what you are reading in

			float longitude = Float.parseFloat(tokenizer.nextToken());
			float latitude = Float.parseFloat(tokenizer.nextToken());
			
			cities.add(new City(name, longitude, latitude));
		}

		infile.close( );
		
		table = new ArrayList<Slot>();
		for (int i = 0; i < tsize; i++)
			table.add(new Slot());
		
		for (City city: cities) {
			int index = hash24.hash(city.name) % tsize;
			table.get(index).count += 1;
		}
		
		int max_collisions = 0;
		int stat[] = new int [25];
		for (Slot slot: table) {
			stat[slot.count] += 1;
			if(slot.count > max_collisions) {
				max_collisions = slot.count;
			}
		}
		
		System.out.println("Primary hash table statistics: ");
		System.out.println("	Number of cities: " + cities.size());
		System.out.println("	Table size: " + table.size());
		System.out.println("	Max collisions: " + max_collisions);

		for(int i = 0 ; i < 25; i++) {
			System.out.println("	# of primary slots with " + i + " cities = " + stat[i]);
		}
		System.out.println();
		System.out.println();
		System.out.println();
		int statistics[] = new int [21];
		City temp = cities.get(0);
		int most_collisions = 0;
		for(City city: cities) {
			//hash to primary table first
			int index = hash24.hash(city.name) % tsize;
			int attempts = table.get(index).add(city);
			if(attempts > most_collisions) {
				most_collisions = attempts;
				temp = city;
			}
			statistics[attempts]++;
		}
		int index3 = hash24.hash(temp.name)% table.size();
		Slot slot2 = table.get(index3);
		System.out.println(" *** Cities in the slot with most collisions ***" );
		for(City city : slot2.sec_table) {
			if(city.latitude == 0.f && city.longitude == 0.f) {
				continue;
			}
			System.out.println("  "+city.name+" (" + city.latitude +", "+ city.longitude+ ")");
		}
		System.out.println();
		float sum = 0;
		System.out.println(" Secondary hash table statistics:" );
		for(int i = 1; i <= 20; i++) {
			sum+=statistics[i];
			System.out.println("	# of secondary hash tables trying " + i + " hash functions = " + statistics[i]);
		}
		System.out.println();
		System.out.println(" Number of secondary hash tables with more than 1 item = "+ sum);
		float average = sum/statistics[1];
		System.out.println(" Average # of hash functions tried = "+ average);
	}
	
	public void writeToFile(String fName) {
		try {
		     FileOutputStream out = new FileOutputStream(fName);
	         ObjectOutputStream oout = new ObjectOutputStream(out);
	
	         // write something in the file
	         oout.writeObject(hash24);
	         oout.writeObject(table);
	
	         // close the stream
	         oout.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static CityTable readFromFile(String fName){
		CityTable city_table;
		try {
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fName));

	        // read and print what we wrote before
			city_table = new CityTable();
	        city_table.hash24 = (Hash24) ois.readObject();
	        city_table.table = (ArrayList<Slot>) ois.readObject();
			ois.close();
			
		} catch (Exception ex) {
			throw new RuntimeException();
		}
		return city_table;
	}
	
	public City find(String cname) {

		int index1 = hash24.hash(cname) % table.size();
		Slot slot = table.get(index1);
		
		if(slot.count == 1) {
			if (slot.city.name.equals(cname))
				return slot.city;
		} 
		else if(slot.count > 1) {
			int index2 = slot.hash24.hash(cname) % (slot.sec_table.size()); 
			City city = slot.sec_table.get(index2);
			if (city.name.equals(cname)) {
				return city;
			}
		}
		return null;
	}
}
