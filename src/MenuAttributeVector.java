import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

public class MenuAttributeVector implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8704267055052033776L;


	/**
	 * Enum types allow user to request a Vector including any and all of the following attributes. 
	 * Attributes not requested will be included in the vector as zeros. 
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
	public enum AttributesOptions{
		WL_ITEM, ADJ_ITEM, ADV_ITEM, NW_ITEM, WL_DESC, ADJ_DESC, ADV_DESC, NW_DESC, CHAIN, RATING, TFIDF_WORD, TFIDF_BIGRAM
	}
	
	public String id;
	private Vector<Double> vec; 
	private int tfidfWordsLen;
	private int tfidfBigramsLen;
	private int tfidfBigramsStartIndex;
	private int attributeNum;
	
	
	/**
	 * default constructor, Initializes the vector with the first 7 indices initially set to -1
	 */
	public MenuAttributeVector(){
		vec = new Vector<>();
		attributeNum = AttributesOptions.values().length-2;
		for(int i = 0; i < attributeNum; i++){
			vec.add(-1.);
		}
	}
	public MenuAttributeVector(Double [] atts, String id){
		this.id = id;
		vec = new Vector<>();
		attributeNum = AttributesOptions.values().length-2;
		if(attributeNum != atts.length){
			throw new IllegalStateException("must add all attributes");
		}
		for(int i = 0; i < attributeNum; i++){
			vec.add(atts[i]);
		}
	}
	
	/**
	 * Gets the full, populated vector
	 * @return
	 */
	public Vector<Double> getVector(){
		return vec;
	}
	
	/**
	 * Returns a vector with the options not selected zeroed out
	 * @param options
	 * @return
	 */
	public Vector<Double> getVector(AttributesOptions[] options){
		Vector<Double> retVal = (Vector<Double>) vec.clone();
		for(AttributesOptions ao : AttributesOptions.values()){
			if(!Arrays.asList(options).contains(ao)){
//				System.out.println("Not to be include: " + ao.toString() + " at index : "+ ao.ordinal());
				if(ao.equals(AttributesOptions.TFIDF_WORD)){
					zeroOutTFIDFWords(retVal);
				} else if(ao.equals(AttributesOptions.TFIDF_BIGRAM)){
					zeroOutTFIDFBigrams(retVal);
				} else {
					retVal.set(ao.ordinal(), 0.0);
				}
				
			}
		}
		return retVal;
	}
	
	/**
	 * Returns a string representation of the Vector with the options not selected zeroed out
	 * @param options
	 * @return
	 */
	public String getVectorString(AttributesOptions[] options){
		Vector<Double> tempVec = getVector(options);
		StringBuilder sb = new StringBuilder();
		for(Double d: tempVec){
			sb.append(d+ ",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/**
	 * zeros out all tfidfWords for the vector
	 * @param vector
	 */
	private void zeroOutTFIDFWords(Vector<Double> vector) {
		for(int i = 0; i < tfidfWordsLen; i++){
			vector.set(attributeNum+i, 0.0);
		}
		
	}

	/**
	 * zeros out all tfidfBigrams for the vector
	 * @param vector
	 */
	private void zeroOutTFIDFBigrams(Vector<Double> vector) {
		for(int i = 0; i < tfidfBigramsLen; i++){
			vector.set(tfidfBigramsStartIndex+i, 0.0);
		}
		
	}
	
	
	
	/**
	 * CALL BEFORE setTFIDFBigrams
	 * Appends the tfidfWords Vector to this MenuAttributeVector
	 * Also sets the tfidfWordsLen instance variable; 
	 * @param tfidfWords
	 */
	public void setTFIDFWordsVec(Vector<Double> tfidfWords){
		vec.addAll(tfidfWords);
		tfidfWordsLen = tfidfWords.size();
	}
	
	/**
	 * CALL AFTER setTFIDFWordsVec
	 * Appends the tfidfBigrams Vector to this MenuAttributeVector
	 * Also sets the tfidfBigramsLen and tfidfBigramsStartIndex instance variables;
	 * @param tfidfBigrams
	 */
	public void setTFIDFBigramsVec(Vector<Double> tfidfBigrams){
		vec.addAll(tfidfBigrams);
		tfidfBigramsLen = tfidfBigrams.size();
		tfidfBigramsStartIndex = attributeNum+tfidfWordsLen;
	}

	
	/*
	 * Gets the full vector as a string
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Double d: vec){
			sb.append(d+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	
	
}
