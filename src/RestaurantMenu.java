

import java.util.HashMap;

public class RestaurantMenu {
	
	private int ID;
	private boolean chain;
	private double rating;
	private HashMap<String, String> menuAndDescriptions;
	private String name;
	
	public RestaurantMenu(int ID, boolean isChain, double rating, HashMap<String, String> menuAndDescriptions, String name) {
		this.ID = ID;
		this.chain = isChain;
		this.rating = rating;
		this.menuAndDescriptions = menuAndDescriptions;
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isChain() {
		return chain;
	}

	public void setChain(boolean chain) {
		this.chain = chain;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public HashMap<String, String> getMenuAndDescriptions() {
		return menuAndDescriptions;
	}

	public void setMenuAndDescriptions(HashMap<String, String> menuAndDescriptions) {
		this.menuAndDescriptions = menuAndDescriptions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
