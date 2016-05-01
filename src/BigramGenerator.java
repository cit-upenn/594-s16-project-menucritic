import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class BigramGenerator {
	
	Analyzer a; 
	
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
//					 System.out.println(toAdd);
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
