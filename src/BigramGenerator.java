import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class BigramGenerator {
	
	Analyzer a; 
	
	/**
	 * Constructor, initializes the anonymous analyzer to tokenize by white space and then
	 * create Bigrams
	 */
	public BigramGenerator(){
	
		 a = new Analyzer() {
			
			@Override
			protected TokenStreamComponents createComponents(String arg0) {
				WhitespaceTokenizer source = new WhitespaceTokenizer();   
				TokenStream filter = new ShingleFilter(source);
				return new TokenStreamComponents(source, filter);
			}
		};
	}
	
	
	/**
	 * generates and returns Bigrams from the text
	 * @param text the text from which to create bigrams
	 * @return an ArrayList of all Bigrams found in the text
	 */
	public ArrayList<String> generateBigrams(String text){
		StringReader sr = new StringReader(text);
		TokenStream ts = a.tokenStream("", sr);
		ArrayList <String> retVal = new ArrayList<>();
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		try{
			ts.reset();
			 while (ts.incrementToken()) {
				 String toAdd = term.toString();
				 if(toAdd.contains(" ")){//is a bigram
					 retVal.add(toAdd);
				 }
		       }
		       ts.end();
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			try {
				ts.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retVal;
	}
	
	
}
