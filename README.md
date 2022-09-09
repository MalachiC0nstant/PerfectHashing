# PerfectHashing
Perfect hashing is hashing without collisions. If you hash n items into a table of size n^2 with a randomly chosen hash function then your probability of not having any collisions is greater than 50%. Assuming you have a collision, you pick another hash function and repeat. The expected number of of attempts you need to achieve 0 collisions is less than 2. However, a table size of n^2 is expensive memory wise so the primary hash table I use, is size n. In order for me to solve the collisions, I created a secondary hash table in a slot of the primary table. The secondary table will be size of t^2, where t is the number of collisions that occur at the primary slot. Given my schema, the expected number of slots used by the seconadary hash table is less than 2n.

How do you search for an item in this hash table? First you hash the item to find its slot in the primary table. If that slot is not empty then you find the slot in the secondary table, and if that slot is also non-empty, you compare the two items. A match means you found the item otherwise the item is not in the hash table.

The perfect hashing schema is applied to a file from geonames.org containing approximately 16,000 city names in the United States. The file also contains each city's longtitude and latitude. An example would look like:   
Abington, MA   
42.10482 -70.94532 

The first program (can be ran using Driver.java) will read the text file provided (an example is US_Cities_LL.txt) and construct a hash table. It will then print out statistics and store the hash table object into a file named US_Cities_LL.ser. 
 
The statistics will look like the following:

 Primary hash table hash function: *** Hash24 dump ***   
prime1 = 16890581  
prime2 = 17027399  
random_a = 12863497  
random_b = 12811586  
random_c = 105054  
Primary hash table statistics:   
	Number of cities: 15937  
	Table size: 16000  
	Max collisions: 6  
	# of primary slots with 0 cities = 5946  
	# of primary slots with 1 cities = 5829  
	# of primary slots with 2 cities = 2923  
	# of primary slots with 3 cities = 1004  
	# of primary slots with 4 cities = 248  
	# of primary slots with 5 cities = 42  
	# of primary slots with 6 cities = 8  
	# of primary slots with 7 cities = 0  
	# of primary slots with 8 cities = 0  
	# of primary slots with 9 cities = 0  
	# of primary slots with 10 cities = 0  
	# of primary slots with 11 cities = 0  
	# of primary slots with 12 cities = 0  
	# of primary slots with 13 cities = 0  
	# of primary slots with 14 cities = 0  
	# of primary slots with 15 cities = 0  
	# of primary slots with 16 cities = 0  
	# of primary slots with 17 cities = 0  
	# of primary slots with 18 cities = 0  
	# of primary slots with 19 cities = 0   
	# of primary slots with 20 cities = 0  
	# of primary slots with 21 cities = 0  
	# of primary slots with 22 cities = 0  
	# of primary slots with 23 cities = 0  
	# of primary slots with 24 cities = 0  



 *** Cities in the slot with most collisions ***  
  Cricket, NC (-81.19398, 36.17152)   
  Marbury, AL (-86.47109, 32.70124)  
  Eagle River, MI (-88.29566, 47.41381)  

 Secondary hash table statistics:  
	# of secondary hash tables trying 1 hash functions = 4225  
	# of secondary hash tables trying 2 hash functions = 897  
	# of secondary hash tables trying 3 hash functions = 211  
	# of secondary hash tables trying 4 hash functions = 62  
	# of secondary hash tables trying 5 hash functions = 11  
	# of secondary hash tables trying 6 hash functions = 2  
	# of secondary hash tables trying 7 hash functions = 1  
	# of secondary hash tables trying 8 hash functions = 0  
	# of secondary hash tables trying 9 hash functions = 0  
	# of secondary hash tables trying 10 hash functions = 0  
	# of secondary hash tables trying 11 hash functions = 0  
	# of secondary hash tables trying 12 hash functions = 0  
	# of secondary hash tables trying 13 hash functions = 0  
	# of secondary hash tables trying 14 hash functions = 0  
	# of secondary hash tables trying 15 hash functions = 0  
	# of secondary hash tables trying 16 hash functions = 0  
	# of secondary hash tables trying 17 hash functions = 0  
	# of secondary hash tables trying 18 hash functions = 0  
	# of secondary hash tables trying 19 hash functions = 0  
	# of secondary hash tables trying 20 hash functions = 0  

 Number of secondary hash tables with more than 1 item = 5409.0  
 Average # of hash functions tried = 1.2802367  
 
The second program (Driver2.java), reads the hash table object from the file US_Cities_LL.ser and promotes the user to type in a city name and a state (e.g, Houston, Texas). So the user will see:  
Enter City, State (or 'quit'):   
Where he could type a city and its state:  
Enter City, State (or 'quit'):  
Abbeville, AL  
The following output will generated:    
Found Abbeville, AL (31.57184, -85.25049)
http://www.google.com/maps?z=10&q=31.57184,-85.25049
 
