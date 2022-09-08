package hash341;

import java.io.Serializable;

public class City implements Serializable {
	public String name;
	public float longitude;
	public float latitude;
	
	public City() {}
	
	public City(String city_name, float city_longitude, float city_latitude) {
		name = city_name;
		longitude = city_longitude;
		latitude = city_latitude;
	}
}
