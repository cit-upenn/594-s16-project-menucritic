import java.util.concurrent.ConcurrentHashMap;


/**
 * A DataStructure to hold all of the Restaurants and their MenuAttribueVectors
 * @author gabecolton
 *
 */
public class MenuVectorMap {
	
	/**
	 * The Hashmap all of the threads will be writing to as they generate 
	 * MenuAttributeVectors for each menu
	 */
	public ConcurrentHashMap<Integer, MenuAttributeVector> vectorMap;

	/**
	 * Constructor, initializes the ConcurrentHashMap
	 */
	public MenuVectorMap (){
		vectorMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * Updates the restaurant's MenuAttributeVector
	 * in the ConcurrentHashMap based on the MenuDataObject's data, 
	 * @param mdo contains data to populate the MenuAttributeVector
	 */
	public void add(MenuDataObject mdo){
		mdo.calculateTotals();
		Double [] attributes = {mdo.wl_item, mdo.adj_item, mdo.adv_item, mdo.nw_item,
								mdo.wl_desc, mdo.adj_desc, mdo.adv_desc, mdo.nw_desc, 
								mdo.chain, mdo.rating};
		vectorMap.putIfAbsent(mdo.id, new MenuAttributeVector(attributes, mdo.id));

	}

}
