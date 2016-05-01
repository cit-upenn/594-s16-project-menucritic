import java.util.List;import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;



public class VectorTester {

	public static void main(String[] args) {
		MenuAttributeVector mav = new MenuAttributeVector();
		Vector<Double> vec = new Vector<>();
		vec.add(2.2);
		vec.add(4.4);
		vec.add(5.5);
		StringBuilder sb = new StringBuilder();
		for(Double d: vec){
			sb.append(Double.toString(d)+" ");
		}
		System.out.println(sb);
		System.out.println(mav);
		
		Vector<Double> vec2 = new Vector<>();
		vec2.add(9.9);
		vec2.add(83.2);
		vec2.add(7.7);
		
		mav.setTFIDFWordsVec(vec); //2.2,4.4,5.5
		mav.setTFIDFBigramsVec(vec2); //9.9,83.2,7.7
		System.out.println(mav);
		final MenuAttributeVector.AttributesOptions a = null;
		MenuAttributeVector.AttributesOptions [] options = {a.RATING,a.CHAIN,a.ADV_ITEM,a.ADV_DESC,a.NW_DESC,a.ADJ_DESC, a.ADJ_ITEM,a.TFIDF_BIGRAM};
		System.out.println(mav.getVectorString(options));
//		Vector<Double> myVec = mav.getVector();
//		mav.zeroOutTFIDFWords(myVec);
//		System.out.println(mav);
//		
		
//		 /**
//	     * Natural term frequencies for a set of documents
//	     *
//	     * @param documents sequence of documents, each of which is a bag of terms
//	     * @param <TERM>    term type
//	     * @return sequence of map of terms to their term frequencies
//	     */
//	    public static <TERM> Iterable<Map<TERM, Double>> tfs(Iterable<Collection<TERM>> documents) {
//	        return tfs(documents, TfType.NATURAL);
//	    }
		
		String b = "Hello this is a a a a test of the emergency the alert alert system";
		String c = "I was on alert while taking the test";
		String d = "Hello the system here is oppressive. I have an emergency";
		String e = "The issue here is that I don't like the oppressive emergency system alerts";
		String e2 = "issue oppressive emergency system alerts";
		String f = "I also take issue with the oppressive system";
		String g = "The test I just took was very emergency oppressive";
		
		
		ArrayList<Integer> ns = new ArrayList<>();
		ns.add(2);
		
		ArrayList<String> docs = new ArrayList<>();
		docs.add(b);
		docs.add(c);
		docs.add(d);
		docs.add(e);
		docs.add(f);
		docs.add(g);
		
//		ArrayList<ArrayList<String>> result = ngramDocumentTerms(new MyWhitespaceTokenizer(), 2, docs);
//		
//		for(ArrayList<String> al: result){
//			for(String s: al){
//				System.out.println(s);
//			}
//		}
		
		Analyzer az = new Analyzer() {
			
			@Override
			protected TokenStreamComponents createComponents(String arg0) {
				WhitespaceTokenizer source = new WhitespaceTokenizer();
				TokenStream filter = new LowerCaseFilter(source);  
				filter = new StopFilter(filter, StandardAnalyzer.STOP_WORDS_SET);
				ShingleFilter sf = new ShingleFilter(filter);
				sf.setFillerToken("");
				filter = sf;
			    return new TokenStreamComponents(source, filter);
			}
		};
		ShingleAnalyzerWrapper saw = new ShingleAnalyzerWrapper(az);
		
		
		
		StringReader sr = new StringReader(e2);
		TokenStream ts = az.tokenStream("", sr);
		TokenStream tt = saw.tokenStream("",sr );
		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		Set<String> bigrams = new HashSet<String>();
		try{
			ts.reset();
			 while (ts.incrementToken()) {
				 System.out.println(term.toString());
		       }
		     
		       ts.end();
		} catch(IOException ef){
			ef.printStackTrace();
		} finally{
			try {
				ts.close();
			} catch (IOException ef) {
				// TODO Auto-generated catch block
				ef.printStackTrace();
			}
		}
		
		
	    
//		Iterable<ArrayList<String>> docs = new ArrayList<ArrayList<String>>();
//		
//		docs.add(b);
//		docs.add(c);
//		docs.add(d);
//		docs.add(e);
//		docs.add(f);
//		docs.add(g);
//		
//		ArrayList<Map<String, Double>> map = TfIdf.tfs(docs, TfIdf.TfType.LOGARITHM);
//		for(String s: map.keySet()){
//			System.out.println("String: "+ s + " tf: "+ map.get(s));
//		}
//		
//		ArrayList<String> test = new ArrayList<String>();
//		for(String z: b.split(" ")){
//			test.add(z);
//		}
//		
//		Map<String, Double> map2 = TfIdf.tf(test, TfIdf.TfType.LOGARITHM);
//		for(String s: map2.keySet()){
//			System.out.println("String: "+ s + " tf: "+ map2.get(s));
//		}
		
		
	}
	
	

}
