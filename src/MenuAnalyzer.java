import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class MenuAnalyzer extends Analyzer{
	InputStream modelIn;
	POSModel model;
	POSTaggerME tagger;// = new POSTaggerME(model);
	CharArraySet stopWords; 
	

	
	public MenuAnalyzer(){
		stopWords = StandardAnalyzer.STOP_WORDS_SET;
		init();
		tagger = new POSTaggerME(model);
//		sb = new StringBuilder();
		//s
	}
	
	private void init() {
		try {
			  modelIn = new FileInputStream("en-pos-maxent.bin");
			  model = new POSModel(modelIn);
			}
			catch (IOException e) {
			  // Model loading failed, handle the error
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
//	    filter = new StemFilter(filter);
	    return new TokenStreamComponents(source, filter);
	} 
//	

	public MyTuple<String, HashMap<String,String>> generateTags(String text){
//		StringBuilder sb = new StringBuilder();
		HashMap<String, String> wordsTags = new HashMap<String, String>();
		ArrayList<String> filteredWords = new ArrayList<String>();
		StringReader sr = new StringReader(text);
		TokenStream ts = this.tokenStream("", sr);
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		try{
			ts.reset();
			 while (ts.incrementToken()) {
				 String toAdd = term.toString().replaceAll("[^A-za-z-']", "");
				 if(toAdd != ""){
					 filteredWords.add(toAdd);
				 }
				 //update word length count etc.
//		         System.out.println(term.toString());
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
//	System.out.println(sb.toString());
		String [] words = filteredWords.toArray(new String [filteredWords.size()]);
		String [] tags = tagger.tag(words);
		StringBuilder sb = new StringBuilder();
		//from original text can calculate various attributes - average number of words, average word length, #adjectives
		//from here, can count the number of various tags, 
		
		for(int i = 0; i< tags.length; i++){
			sb.append(words[i]+ " ");
			wordsTags.put(words[i], tags[i]);
//			System.out.println(words[i] + " : " + tags[i]); //" : "+ probs[i]);
			
//			sb.append(sent[i] + tags[i]+ " ");
		}
		MyTuple<String, HashMap<String, String>> retVal = new MyTuple<>(sb.toString(), wordsTags);
		return retVal;
	}
	
}
