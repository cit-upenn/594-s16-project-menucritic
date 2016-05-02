import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class MenuAnalyzer extends Analyzer{
	
	/**
	 * the part of speech model as a stream
	 */
	InputStream modelIn;
	/**
	 * the model to be created from the input stream
	 */
	POSModel model;
	/**
	 * a tagger object initialized with a model
	 */
	POSTaggerME tagger;
	/**
	 * stop words used for filtering
	 */
	CharArraySet stopWords; 
	
	/**
	 * Constructor, will initialize the stopwords and the model
	 */
	public MenuAnalyzer(){
		stopWords = StandardAnalyzer.STOP_WORDS_SET;
		initModel();
		tagger = new POSTaggerME(model);
	}
	
	/**
	 * initialize the model
	 */
	private void initModel() {
		try {
			  modelIn = new FileInputStream("en-pos-maxent.bin");
			  model = new POSModel(modelIn);
			}
			catch (IOException e) {
			 System.out.println("Could not find appropriate binary file (en-pos-maxent.bin) ");
			  e.printStackTrace();
			}
			finally {
			  if (modelIn != null) {
			    try {
			      modelIn.close();
			    }
			    catch (IOException e) {
			    }
			  }
			}
		
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		WhitespaceTokenizer source = new WhitespaceTokenizer();              
	    TokenStream filter = new LowerCaseFilter(source);                
	    filter = new StopFilter(filter, stopWords);                  
	    return new TokenStreamComponents(source, filter);
	} 

	
	/**
	 * method to filter input and generate POS tags.
	 * 
	 * @param text the text to generate tags for
	 * @return A Tuple : first: the filtered string
	 * 					 second: a HashMap of strings mapped to their POS tags
	 */
	public MyTuple<String, HashMap<String,String>> generateTags(String text){
		
		HashMap<String, String> wordsTags = new HashMap<String, String>();
		ArrayList<String> filteredWords = new ArrayList<String>();
		
		StringReader sr = new StringReader(text);
		TokenStream ts = this.tokenStream("", sr);
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		StringBuilder sb = new StringBuilder();
		
		try{
			ts.reset();
			 while (ts.incrementToken()) {
				 String toAdd = term.toString().replaceAll("[^A-za-z-']", "");
				 if(toAdd != ""){//if the word to add is valid, add it to list of filtered words and append it to the string builder
//					 System.out.println("***"+toAdd);
					 filteredWords.add(toAdd);
					 sb.append(toAdd + " ");
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
		
		String [] words = filteredWords.toArray(new String [filteredWords.size()]);
		String [] tags = tagger.tag(words);
		//populate the tagMap with the tags of the filtered words. 
		for(int i = 0; i< tags.length; i++){
			wordsTags.put(words[i], tags[i]);
		}
		
		MyTuple<String, HashMap<String, String>> retVal = new MyTuple<>(sb.toString(), wordsTags);
		
		return retVal;
	}
	
}
