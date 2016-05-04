
/**
 * This is a constructor for a RestaurantData object
 * @author meredithmargulies
 *
 */
public class RestaurantData {
	private double latitude;
	private double longitude;
	private double rating;
	private String address;
	private boolean chain;
	private int number;
	
	/**
	 * This is a constructor for the RestaurantData object
	 * @param latitude the latitude of the restaurant
	 * @param longitude the longitude of the restaurant
	 * @param rating the rating of the restaurant
	 * @param address the address of the restaurant
	 */
	public RestaurantData(double latitude, double longitude, double rating, String address) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.rating = rating;
		chain = false;
		number = 1;
	}
	
	/**
	 * a getter for the address
	 * @return the address of the restaurant
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * a getter for the number of restaurants with the same name
	 * @return the number of restaurants with the same name
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * a setter for the number of restaurants with the same name
	 * @param number the number of restaurants with the same name
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * a getter for the latitude
	 * @return the latitude of the restaurant
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * A setter for the latitude
	 * @param latitude the latitude of the restaurant
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * a getter for the longitude
	 * @return the longitude of the restaurant
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * a setter for the longitude
	 * @param the longitude of the restaurant
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * a getter for the rating
	 * @return the rating of the restaurant
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * a setter for the rating
	 * @param rating the rating of the restaurant
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	/**
	 * a getter for if the restaurant is a chain restaurant
	 * @return if the restaurant is a chain restaurant
	 */
	public boolean isChain() {
		return chain;
	}

	/**
	 * a getter for if the restaurant is a chain restaurant
	 * @param if the restaurant is a chain restaurant
	 */
	public void setChain(boolean chain) {
		this.chain = chain;
	}
	
	
}
