

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataInterface {
	String fileName;
	
	public DataInterface(String file) {
		fileName = file;
	}
	
	public ArrayList<RestaurantMenu> getAllMenus() {
		ArrayList<RestaurantMenu> allMenus = new ArrayList<RestaurantMenu>();
		JSONParser parser = new JSONParser();
		try {
			JSONArray a = (JSONArray) parser.parse(new FileReader(fileName));
			for(Object o : a) {
				JSONObject restaurant = (JSONObject) o;
				int ID = (int) restaurant.get("ID");
				boolean chain = (boolean) restaurant.get("Chain");
				double rating = (double) restaurant.get("Rating");
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
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allMenus;
		
	}
	
	

}
