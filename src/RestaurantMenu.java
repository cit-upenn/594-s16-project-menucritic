

import java.util.HashMap;

/**
 * This is the class Restaurant Menu, representing a menu
 * @author meredithmargulies
 *
 */
public class RestaurantMenu {
	//instance variables
	private int ID;
	private boolean chain;
	private double rating;
	private HashMap<String, String> menuAndDescriptions;
	private String name;
	
	/**
	 * This is a constructor for the Restaurant Menu Object
	 * @param ID the ID of the restaurant
	 * @param isChain true if the restaurant is a chain restaurant
	 * @param rating the rating of the restaurant
	 * @param menuAndDescriptions the items on the menu and their descriptions
	 * @param name the name of the restaurant
	 */
	public RestaurantMenu(int ID, boolean isChain, double rating, HashMap<String, String> menuAndDescriptions, String name) {
		this.ID = ID;
		this.chain = isChain;
		this.rating = rating;
		this.menuAndDescriptions = menuAndDescriptions;
		this.name = name;
	}
	
	/**
	 * A getter for the ID
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * A setter for the ID
	 * @param iD the ID
	 */
	public void setID(int iD) {
		ID = iD;
	}
	
	/**
	 * A getter for chain
	 * @return whether the restaurant is a chain
	 */
	public boolean isChain() {
		return chain;
	}

	/**
	 * A setter for chain
	 * @param chain if the restaurant is a chain
	 */
	public void setChain(boolean chain) {
		this.chain = chain;
	}
	
	/**
	 * A getter for the rating
	 * @return the rating of the restaurant
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * A setter for the rating
	 * @param rating the rating of the restaurant
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * A getter for the Menu
	 * @return a  Hashmap of items and their corresponding descriptions
	 */
	public HashMap<String, String> getMenuAndDescriptions() {
		return menuAndDescriptions;
	}
	
	/**
	 * A setter for the menu
	 * @param menuAndDescriptions the menu you want to use
	 */
	public void setMenuAndDescriptions(HashMap<String, String> menuAndDescriptions) {
		this.menuAndDescriptions = menuAndDescriptions;
	}
	
	/**
	 * A getter for the name of the restaurant
	 * @return the name of the restaurant
	 */
	public String getName() {
		return name;
	}

	/**
	 * A setter for the name
	 * @param name the name of the restaurant
	 */
	public void setName(String name) {
		this.name = name;
	}

}
