import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * A class that represents a Vector of all possible attributes of a menu
 * @author gabecolton
 *
 */
public class MenuAttributeVector implements Serializable{
	
	/**
	 * the serial ID for this object
	 */
	private static final long serialVersionUID = 8704267055052033776L;

	/**
	 * A 1d vector holding the value of the average word length for each item
	 */
	private Vector<Double> wl_item;
	/**
	 * A 1d vector holding the value of the average number of adjectives for each item
	 */
	private Vector<Double> adj_item;
	/**
	 * A 1d vector holding the value of the average number of adverbs for each item
	 */
	private Vector<Double> adv_item;
	/**
	 * A 1d vector holding the value of the average number of words in the item
	 */
	private Vector<Double> nw_item;
	/**
	 * A 1d vector holding the value of the average word length for each description
	 */
	private Vector<Double> wl_desc;
	/**
	 * A 1d vector holding the value of the average number of adjectives for each description
	 */
	private Vector<Double> adj_desc;
	/**
	 * A 1d vector holding the value of the average number of adverbs for each description
	 */
	private Vector<Double> adv_desc;
	/**
	 * A 1d vector holding the value of the average number of words in the description
	 */
	private Vector<Double> nw_desc;
	
	
	/**
	 * a 1d vector holding the value that is 1.0 if the menu is from a chain, 0.0 if not
	 */
	private Vector<Double> chain;
	/**
	 * The yelp rating of this restaurant
	 */
	private Vector<Double> rating;
	
	/**
	 * The tfidf vector for the words in this menu
	 */
	private Vector<Double> tfidfWordVec;
	/**
	 * The tfidf vector for the bigrams in this menu
	 */
	private Vector<Double> tfidfBigramVec;
	
	/**
	 * An ArrayList of all of the base attributes (not including tfidfword and tfidfbigram)
	 */
	private ArrayList<Vector<Double>> allAttrs;
	
	/**
	 * Enum types allow user to request a Vector including any and all of the following attributes. 
	 * For Item and Descrition, respectively: 
	 * WL: average word length
	 * ADJ: adjectives per word
	 * NW: number words
	 * 
	 * CHAIN: Chain restaurant or not
	 * RATING: The Yelp rating of the menu
	 * TFIDF_WORD: a tfidf vector for all words seen
	 * TFIDF_BIGRAM: a tfidf vector for all bigrams seen
	 * @author gabecolton
	 *
	 */
	public enum AttributeOptions {
		WL_ITEM, ADJ_ITEM, ADV_ITEM, NW_ITEM, WL_DESC, ADJ_DESC, ADV_DESC, NW_DESC, CHAIN, RATING, TFIDF_WORD, TFIDF_BIGRAM
	}
	
	/**
	 * used when a user wants the whole vector
	 */
	public static AttributeOptions [] allOptions = {
			AttributeOptions.WL_ITEM,
			 AttributeOptions.ADJ_ITEM,
			 AttributeOptions.ADV_ITEM,
			 AttributeOptions.NW_ITEM,
			 AttributeOptions.WL_DESC,
			 AttributeOptions.ADJ_DESC,
			 AttributeOptions.ADV_DESC,
			 AttributeOptions.NW_DESC,
			 AttributeOptions.CHAIN,
//			 AttributeOptions.RATING,
			 AttributeOptions.TFIDF_WORD,
			 AttributeOptions.TFIDF_BIGRAM};
	
	/**
	 * The id of this restaurant
	 */
	public int id;
	
	/**
	 * The number of attributes this menu has
	 */
	private int attributeNum;
	
	
	public MenuAttributeVector(Double [] atts, int id){
		
		  allAttrs = new ArrayList<>(); 
		  attributeNum = AttributeOptions.values().length-2;
		  wl_item = new Vector<>();
		  adj_item = new Vector<>();
		  adv_item = new Vector<>();
		  nw_item = new Vector<>();
		  wl_desc = new Vector<>();
		  adj_desc = new Vector<>();
		  adv_desc = new Vector<>();
		  nw_desc = new Vector<>();
		  chain = new Vector<>();
		  rating = new Vector<>();
		 
		  allAttrs.add(wl_item);
		  allAttrs.add(adj_item);
		  allAttrs.add(adv_item);
		  allAttrs.add(nw_item);
		  allAttrs.add(wl_desc);
		  allAttrs.add(adj_desc);
		  allAttrs.add(adv_desc);
		  allAttrs.add(nw_desc);
		  allAttrs.add(chain);
		  allAttrs.add(rating);
		  
		  tfidfWordVec = new Vector<>();
		  tfidfBigramVec = new Vector<>();
	  
		this.id = id;
		attributeNum = AttributeOptions.values().length-2;
		if(attributeNum != atts.length){
			throw new IllegalStateException("must add all attributes");
		}
		for(int i = 0; i < attributeNum; i++){
			allAttrs.get(i).add(atts[i]);
		}
	}
	
	/**
	 * Gets the full, populated vector
	 * @return
	 */
	public Vector<Double> getVector(){
		Vector<Double> retVal = new Vector<>();
		for(Vector<Double> v: allAttrs){
			retVal.addAll(v);
		}
		retVal.addAll(tfidfWordVec);
		retVal.addAll(tfidfBigramVec);
		
		return retVal;
	}
	
	/**
	 * Returns a vector with the options not selected zeroed out
	 * @param options
	 * @return
	 */
	public Vector<Double> getVector(AttributeOptions[] options){
		Vector<Double> retVal = new Vector<>();
		for(AttributeOptions ao : AttributeOptions.values()){
			if(Arrays.asList(options).contains(ao)){
				if(ao.equals(AttributeOptions.TFIDF_WORD)){
					retVal.addAll(tfidfWordVec);
				} else if(ao.equals(AttributeOptions.TFIDF_BIGRAM)){
					retVal.addAll(tfidfBigramVec);
				}else {
					retVal.addAll(allAttrs.get(ao.ordinal()));
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Returns a string representation of the Vector with the options not selected zeroed out
	 * of format rating, attr1 attr2 attr3...
	 * @param options
	 * @return
	 */
	public String getVectorString(AttributeOptions[] options){
		Vector<Double> tempVec = getVector(options);
		StringBuilder sb = new StringBuilder();
<<<<<<< HEAD
//		sb.append(Integer.toString(id)+",");
		sb.append(Double.toString(rating.get(0))).append(",");
=======
		sb.append(Integer.toString(id)).append(",");
>>>>>>> 7a1973e77e6b41c44fa7de13494c488353b652fa
		for(Double d: tempVec){
			sb.append(d).append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/**
	 * 
	 * Sets this MenuAttributeVector's tfidfWordVec to the parameter
	 * @param tfidfWords
	 */
	public void setTFIDFWordsVec(Vector<Double> tfidfWords){
		tfidfWordVec = tfidfWords; 
		  
	}
	
	/**
	 * Sets this MenuAttributeVector's tfidfBigramVec to the parameter
	 * @param tfidfBigrams
	 */
	public void setTFIDFBigramsVec(Vector<Double> tfidfBigrams){
		tfidfBigramVec = tfidfBigrams;
	}

	
	/*
	 * Gets the full vector as a string
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return getVectorString(allOptions);
	}
	
	
	
}
