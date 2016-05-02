import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MenuVectorMap implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4485176336505072973L;
	/**
	 * Integer is the restaurant ID, key is the MenuAttribute vector for the restaurant
	 */
	public Map<Integer, MenuAttributeVector> vectorMap;
//	private Map<String, MenuDataObject> dataMap;
	
	public MenuVectorMap (){
		vectorMap = new HashMap<>();
//		dataMap= new HashMap<>();
	}
	
	/**
	 * Updates the restaurant in the map based on the MenuAnalyzer's data, 
	 * store the ma for access
	 * @param ma
	 */
	public void add(MenuDataObject mdo){
		//WL_ITEM, ADJ_ITEM, ADV_ITEM, NW_ITEM, WL_DESC, ADJ_DESC, ADV_DESC, NW_DESC, CHAIN, RATING, TFIDF_WORD, TFIDF_BIGRAM
		mdo.calculateTotals();
		Double [] attributes = {mdo.wl_item, mdo.adj_item, mdo.adv_item, mdo.nw_item,
								mdo.wl_desc, mdo.adj_desc, mdo.adv_desc, mdo.nw_desc, 
								mdo.chain, mdo.rating};
		vectorMap.putIfAbsent(mdo.id, new MenuAttributeVector(attributes, mdo.id));
//		dataMap.putIfAbsent(mdo.id, mdo);
		
	}

}
