import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;


public class MenuVectorMap implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4485176336505072973L;
	/**
	 * Integer is the restaurant ID, key is the MenuAttribute vector for the restaurant
	 */

	public ConcurrentHashMap<Integer, MenuAttributeVector> vectorMap;

	public MenuVectorMap (){
		vectorMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * Updates the restaurant in the map based on the MenuAnalyzer's data, 
	 * store the ma for access
	 * @param mdo
	 */
	public void add(MenuDataObject mdo){
		mdo.calculateTotals();
		Double [] attributes = {mdo.wl_item, mdo.adj_item, mdo.adv_item, mdo.nw_item,
								mdo.wl_desc, mdo.adj_desc, mdo.adv_desc, mdo.nw_desc, 
								mdo.chain, mdo.rating};
		vectorMap.putIfAbsent(mdo.id, new MenuAttributeVector(attributes, mdo.id));

	}

}
