

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The class DataInterface is used to act as an interface between the data and vector generation
 * @author meredithmargulies
 *
 */
public class DataInterface {
	String fileName;
	
	/**
	 * This is a constructor for the DataInterface
	 * @param file the JSON file name to extract menus from
	 */
	public DataInterface(String file) {
		fileName = file;
	}
	
	/**
	 * This method creates all of the menus from the JSON file 
	 * @return an ArrayList of Restaurant Menus
	 */
	public ArrayList<RestaurantMenu> getAllMenus() {
		ArrayList<RestaurantMenu> allMenus = new ArrayList<RestaurantMenu>();
		JSONParser parser = new JSONParser();
		try {
			JSONArray a = (JSONArray) parser.parse(new FileReader(fileName));
			for(Object o : a) {
				JSONObject restaurant = (JSONObject) o;
				int ID;
				if(restaurant.get("ID").getClass() == Long.class) {
					Long lon = (Long) restaurant.get("ID");
					ID = lon.intValue();
				} else {
					ID = (int) restaurant.get("ID");
				}
				System.out.println("Restaurant id: "+ID);
				boolean chain = (boolean) restaurant.get("Chain");
				double rating;
				if(restaurant.get("Rating").getClass() == Long.class) {
					Long lon = (Long) restaurant.get("Rating");
					rating = lon.doubleValue();
				} else {
					rating = (double) restaurant.get("Rating");
				}
				JSONArray Menu = (JSONArray) restaurant.get("Menu");
				HashMap<String, String> mapMenu = new HashMap<String,String>();
				for(Object i : Menu) {
					JSONObject item = (JSONObject) i;
					String itemName = (String) item.get("Item");
					String description = (String) item.get("Description");
					mapMenu.put(itemName, description);
					
				}
				String name = (String) restaurant.get("Name");
				allMenus.add(new RestaurantMenu(ID, chain, rating, mapMenu, name));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allMenus;
		
	}
	
	

}
