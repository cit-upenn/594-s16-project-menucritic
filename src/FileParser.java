import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The file parser is used to query the api and do data parsing
 * @author meredithmargulies
 *
 */
public class FileParser {
	//instance variables
	JSONArray allData;
	int count = 0;
	int callCount = 0;
	HashMap<String, RestaurantData> ratings;
	HashMap<Integer, String> index;
	HashSet<String> chains;

	/**
	 * This constructor creates a FileParser object
	 * @throws IOException
	 * @throws ParseException
	 */
	public FileParser() throws IOException, ParseException {
		ratings = new HashMap<String, RestaurantData>();
		allData = new JSONArray();
		index = new HashMap<Integer, String>();
		chains = new HashSet<String>();
	}

	public void cleanData(String myFile) {
		JSONParser parser = new JSONParser();
		JSONArray betterChains = new JSONArray();
		JSONArray finalRez = new JSONArray();
		count = 1;
		try {
			JSONArray a = (JSONArray) parser.parse(new FileReader(myFile));
			for(Object o : a) {
				JSONObject restaurant = (JSONObject) o;
				String name = (String) restaurant.get("Name");
				if(chains.contains(name)) {
					restaurant.put("Chain", true);
				} else {
					chains.add(name);
				}
				betterChains.add(restaurant);

			}
			chains.clear();
			for(int i = betterChains.size()-1; i >= 0;i-- ) {
				JSONObject restaurant = (JSONObject) betterChains.get(i);
				String name = (String) restaurant.get("Name");
				if(!chains.contains(name)) {
					chains.add(name);
					restaurant.put("ID", count);
					finalRez.add(restaurant);
					count++;
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try(FileWriter file = new FileWriter("/Users/meredithmargulies/Desktop/FinalData.json")) {
			file.write(finalRez.toJSONString());
			System.out.println("Created Results");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void parseFile() {
		JSONParser parser = new JSONParser();
		JSONArray a;
		try {
			a = (JSONArray) parser.parse(new FileReader("output.json"));

			System.out.println(a.get(0));
			int count = 0;
			for (Object o : a)
			{

				JSONObject person = (JSONObject) o;
				//if()
				double latitude = 0;
				if(person.get("latitude").getClass() == Long.class) {
					Long lat = (Long) person.get("latitude");
					latitude = lat.doubleValue();

				} else {
					latitude= (double) person.get("latitude");
				}
				double longitude = 0;
				if(person.get("longitude").getClass() == Long.class) {
					Long lon = (Long) person.get("longitude");
					longitude = lon.doubleValue();

				} else {
					longitude = (double) person.get("longitude");
				}
				String address = (String) person.get("full_address");
				String name = (String) person.get("name");
				double rating = 0;
				if(person.get("stars").getClass() == Long.class) {
					Long rat = (Long) person.get("stars");
					rating = rat.doubleValue();
				} else {
					rating = (double) person.get("stars");
				}
				String[] first = address.split("\n");
				address = first[0];
				if(!ratings.containsKey(name)) {
					ratings.put(name, new RestaurantData(latitude, longitude, rating, address));
					index.put(count, name);
				}
				count++;

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int j = 0; j < index.size(); j++) {
			String i = index.get(j);
			if(callCount < 500) {
				RestaurantData rd = ratings.get(i);
				if(rd != null) {
					try {
						processARestaurant(rd.getLatitude(), rd.getLongitude(), rd.getAddress(), rd.getRating(), rd.isChain());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				callCount++;
			} else if(callCount == 500) {
				System.out.println(i);
				RestaurantData rd = ratings.get(i);
				if(rd!= null) {
					System.out.println(rd.getAddress());
				}
				break;
			} else {
				break;
			}
		}
		try(FileWriter file = new FileWriter("/Users/meredithmargulies/Desktop/nextresults.json")) {
			file.write(allData.toJSONString());
			System.out.println("Created Results");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method creates a JSON object and adds it to the JSONArray
	 * @param name the name of the restaurant
	 * @param rating the rating of the restaurant
	 * @param items the items and descriptions of the menu
	 * @param chain whether or not the restaurant is a chain restaurant
	 */
	@SuppressWarnings("unchecked")
	public void createJSON(String name, double rating, HashMap<String, String> items, boolean chain) {
		JSONObject restaurant = new JSONObject();
		restaurant.put("Name", name);
		restaurant.put("Rating", rating);
		restaurant.put("Chain", chain);
		JSONArray allItems = new JSONArray();
		Set<String> eachItem = items.keySet();
		for(String i: eachItem) {
			JSONObject anItem = new JSONObject();
			anItem.put("Item", i);
			anItem.put("Description",items.get(i));
			allItems.add(anItem);
		}
		restaurant.put("Menu", allItems);
		allData.add(restaurant);

	}

	/**
	 * A method to parse the API output
	 * @param line the line to parse
	 * @param rating the rating of the restaurant
	 * @param chain whether or not the restaurant is a chain restaurant
	 */
	private void parseLine(String line, double rating, boolean chain) {
		HashMap<String, String> itemsAndDesc = new HashMap<String, String>();
		JSONParser parser = new JSONParser();
		JSONObject a = new JSONObject();
		try {
			a = (JSONObject) parser.parse(line);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String status = (String) a.get("status");
		if(!status.equals("error")) {
			try {
				JSONArray array = (JSONArray) a.get("venues");
				JSONObject rest = (JSONObject) array.get(0);
				JSONArray menus = (JSONArray) rest.get("menus");
				String restaurantName = (String) rest.get("name");
				if(restaurantName.contains("Spa") || (count == 1 && restaurantName.equals("McDonald's")) || restaurantName.contains("Salon") || restaurantName.contains("Hair") || restaurantName.contains("Laundry")  ) {
					return;
				}
				if(restaurantName.equals("McDonald's")) {
					count++;
				}
				//System.out.println(count);
				System.out.println(restaurantName);
				System.out.println(rating);
				for(int i = 0; i < menus.size(); i++) {
					//System.out.println(menus.size());
					JSONObject menu = (JSONObject) menus.get(i);
					JSONArray subsections = (JSONArray) menu.get("sections");
					for(int j=0; j < subsections.size(); j++) {
						JSONObject sub = (JSONObject) subsections.get(j);
						JSONArray subby = (JSONArray) sub.get("subsections");
						for(int l = 0; l < subby.size(); l++) {
							JSONObject it = (JSONObject) subby.get(l);
							JSONArray items = (JSONArray) it.get("contents");
							//System.out.println("try");
							//System.out.println(items.size());
							for(int k =0; k < items.size(); k++) {
								//System.out.println("try");
								JSONObject item = (JSONObject) items.get(k);
								//System.out.println(item);
								String name = (String) item.get("name");
								//System.out.println("try");
								String description = (String) item.get("description");
								if(name!=null && description!= null) {
									itemsAndDesc.put(name, description);
								}
							}
						}
					}
				}
				createJSON(restaurantName, rating, itemsAndDesc, chain);
			} catch(Exception e) {
				return;
			}

		}

	}

	/**
	 * This method queries the api for the specified restaurant
	 * @param lat the latitude of the restaurant
	 * @param lon the longitude of the restaurant
	 * @param address the address of the restaurant
	 * @param rating the rating of the restaurant
	 * @param chain whether or not the restaurant is a chain restaurant
	 */
	private void processARestaurant(double lat, double lon, String address, double rating, boolean chain) {
		//HashSet<String> chains = new HashSet<String>();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://api.locu.com/v2/venue/search");

		Map<String, Object> config = new HashMap<String, Object>();
		config.put(JsonGenerator.PRETTY_PRINTING, Boolean.valueOf(true));
		JsonBuilderFactory factory = Json.createBuilderFactory(config);
		JsonObject value = factory.createObjectBuilder()
				//number 2
				.add("api_key", "a5fdc405f821db8dadb56d1553f457a168de0cbc")
				.add("fields", factory.createArrayBuilder()
						.add("name")
						.add("location")
						.add("contact")
						.add("menus"))
				.add("venue_queries", factory.createArrayBuilder()
						.add(factory.createObjectBuilder()
								.add("location", factory.createObjectBuilder()
										.add("geo", factory.createObjectBuilder()
												.add("$in_lat_lng_radius", factory.createArrayBuilder()
														.add(lat)
														.add(lon)
														.add(100)))))
						.add(factory.createObjectBuilder()
								.add("location", factory.createObjectBuilder()
										.add("address1", address))))
				.build();

		try {
			httpPost.setEntity(new StringEntity(value.toString()));
			CloseableHttpResponse response2 = httpclient.execute(httpPost);

			HttpEntity entity2 = response2.getEntity();
			String inputLine ;
			BufferedReader br = new BufferedReader(new InputStreamReader(entity2.getContent()));
			try {
				inputLine = br.readLine();
				parseLine(inputLine, rating, chain);


				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			EntityUtils.consume(entity2);
			response2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
