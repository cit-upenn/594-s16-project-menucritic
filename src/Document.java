
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * This class represents one document.
 * It will keep track of the term frequencies.
 * @author swapneel
 *
 */
public class Document implements Comparable<Document> {
	
	/**
	 * A hashmap for term frequencies.
	 * Maps a term to the number of times this terms appears in this document. 
	 */
	private HashMap<String, Integer> termFrequency;
	
	/**
	 * The name of the file to read.
	 */
	private String menuString;
	private String menuId;
	
	/**
	 * The constructor.
	 * It takes in the name of a file to read.
	 * It will read the file and pre-process it.
	 * @param filename the name of the file
	 */
	public Document(String menuString, String menuId) {
		this.menuString = menuString;
		termFrequency = new HashMap<String, Integer>();
		this.menuId = menuId;
		
		readFileAndPreProcess();
	}
	
	public Document(ArrayList<String> bigramList, String menuId){
		termFrequency = new HashMap<String, Integer>();
		readBigramListAndPreProcess(bigramList);
		this.menuId = menuId;
	}
	
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
	 * This method will read in the file and do some pre-processing.
	 * The following things are done in pre-processing:
	 * Every word is converted to lower case.
	 * Every character that is not a letter or a digit is removed.
	 * We don't do any stemming.
	 * Once the pre-processing is done, we create and update the 
	 */
	private void readFileAndPreProcess() {
			Scanner in = new Scanner(menuString);
//			System.out.println("Reading file: " + filename + " and preprocessing");
			
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
		return menuString.compareTo(other.getMenuString());
	}

	/**
	 * @return the filename
	 */
	private String getMenuString() {
		return menuString;
	}
	
	/**
	 * This method is used for pretty-printing a Document object.
	 * @return the filename
	 */
	public String toString() {
		return menuString;
	}

	public String getMenuId() {
		return menuId;
	}

}