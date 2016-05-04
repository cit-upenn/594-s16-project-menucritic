
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * This class represents one document.
 * It will keep track of the term frequencies.
 * @author swapneel and gabe colton
 *
 */
public class Document implements Comparable<Document> {
	
	/**
	 * A hashmap for term frequencies.
	 * Maps a term to the number of times this terms appears in this document. 
	 */
	private HashMap<String, Integer> termFrequency;
	
	/**
	 * The full menu (all item descriptions, not items) in the form a string
	 */
	private String menuString;
	
	/**
	 * A unique id for this menu/restaurant
	 */
	private int menuId;
	
	
	/**
	 * A constructor for tf of single words
	 * @param menuString the full menu as a string
	 * @param menuId the id of this menu
	 */
	public Document(String menuString, int menuId) {
		this.menuString = menuString;
		termFrequency = new HashMap<String, Integer>();
		this.menuId = menuId;
		
		readFileAndPreProcess();
	}
	
	/**
	 * A constructor for tf of bigrams
	 * @param bigramList a list of all bigrams appearing in the document
	 * @param menuId the id of the menu
	 */
	public Document(ArrayList<String> bigramList, int menuId){
		termFrequency = new HashMap<String, Integer>();
		this.menuId = menuId;
		readBigramListAndPreProcess(bigramList);
		
	}
	
	/**
	 * populates the termFrequency instance variable with frequencies of the bigrams
	 * @param bigramList all bigrams appearing in the document
	 */
	private void readBigramListAndPreProcess(ArrayList<String> bigramList) {
		for(String bigram: bigramList){
				if (termFrequency.containsKey(bigram)) {
					int oldCount = termFrequency.get(bigram);
					termFrequency.put(bigram, ++oldCount);
				} else {
					termFrequency.put(bigram, 1);
				}
		}
	}

	/**
	 * This method will read in the full menu string
	 * At this point, all menus have been pre-processed to
	 * remove stop words, get rid of all non alphabetic characters (except - and ')
	 * Every word is converted to lower case.
	 * We update the term frequency map
	 */
	private void readFileAndPreProcess() {
			Scanner in = new Scanner(menuString);
			
			while (in.hasNext()) {
				String nextWord = in.next();
				
				if (!(nextWord.equalsIgnoreCase(""))) {
					if (termFrequency.containsKey(nextWord)) {
						int oldCount = termFrequency.get(nextWord);
						termFrequency.put(nextWord, ++oldCount);
					} else {
						termFrequency.put(nextWord, 1);
					}
				}
			}
			in.close();
	}
	
	/**
	 * This method will return the term frequency for a given word.
	 * If this document doesn't contain the word, it will return 0
	 * @param word The word to look for
	 * @return the term frequency for this word in this document
	 */
	public double getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		} else {
			return 0;
		}
	}
	
	/**
	 * This method will return a set of all the terms which occur in this document.
	 * @return a set of all terms in this document
	 */
	public Set<String> getTermList() {
		return termFrequency.keySet();
	}

	@Override
	/**
	 * The overriden method from the Comparable interface.
	 */
	public int compareTo(Document other) {
		return Integer.valueOf(menuId).compareTo(other.menuId);
	}
	
	/**
	 * This method is used for printing the entire menu string
	 * @return the filename
	 */
	public String toString() {
		return menuString;
	}

	/** getter for the menu id 
	 * @return the menuId
	 */
	public int getMenuId() {
		return menuId;
	}

}