import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class MenuVectorMap implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4485176336505072973L;
	/**
	 * String is the restaurant ID, key is the MenuAttribute vector for the restaurant
	 */
	public ConcurrentHashMap<String, MenuAttributeVector> vectorMap;
	private ConcurrentHashMap<String, MenuDataObject> dataMap;
	
	public MenuVectorMap (){
		vectorMap = new ConcurrentHashMap<>();
		dataMap= new ConcurrentHashMap<>();
	}
	
	/**
	 * Updates the restaurant in the map based on the MenuAnalyzer's data, 
	 * store the ma for access
	 * @param mdo
	 */
	public void add(MenuDataObject mdo){
		//WL_ITEM, ADJ_ITEM, ADV_ITEM, NW_ITEM, WL_DESC, ADJ_DESC, ADV_DESC, NW_DESC, CHAIN, RATING, TFIDF_WORD, TFIDF_BIGRAM
		mdo.calculateTotals();
		Double [] attributes = {mdo.wl_item, mdo.adj_item, mdo.adv_item, mdo.nw_item,
								mdo.wl_desc, mdo.adj_desc, mdo.adv_desc, mdo.nw_desc, 
								mdo.chain, mdo.rating};
		vectorMap.putIfAbsent(mdo.id, new MenuAttributeVector(attributes, mdo.id));
		dataMap.putIfAbsent(mdo.id, mdo);
		
	}
	
	/**
	 * calculates TFIDF Word and Bigram vectors for all menus
	 */
	private void generateTFIDFVecs(){
			//create two Documents for each Menu, 
			//one that is it's full menu string for word tfidf
			// one which is an ArrayList of Bigrams, created using a ShingleFilter
		//create two Corpuses, 
			//one for the word menus, 
			//one for the bigram menus
		//create two VectorSpaceModels, one for each corpus
		//get tfidf vectors for each 
		
	}
}
