import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

public class MyTester {
	public static void main(String[] args) {
		double itemAdjStat = 12;
		String str = "JJRB";
		
		itemAdjStat += str.substring(0,2).equals("JJ") ? 1 : 0;
		System.out.println(itemAdjStat);
		
		
		String id = "11";
		Double chain = 0.0;
		Double rating = 4.5;
		TreeMap<String, String> testMenu = new TreeMap<>();
		testMenu.put("Shrimp Scampi", "This accidentally delicious jumbo shrimp is carefully sauteed in a white wine butter sauce");
		testMenu.put("Seared Scallops", "lemon juice reduction, micro greens, cooked tasty cream sauce");
		testMenu.put("Filet Mignon", "Seared prime cut cooked nicely medium-rare");
		testMenu.put("Duck ala King", "A scrupmtious pan-seared duck breast lathered in a Chambord balsamic reduction");
		MenuDataObject mdo = new MenuDataObject(id, rating, chain);
		MenuAnalyzer ma = new  MenuAnalyzer();
		
		MenuVectorMap mvp = new MenuVectorMap();
		for(String s: testMenu.keySet()){
			mdo.addItemMap(ma.generateTags(s));
			mdo.addDescriptionMap(ma.generateTags(testMenu.get(s)));
			mdo.addDescription(testMenu.get(s));
		}
		mdo.calculateTotals();
		mvp.add(mdo);
		
		System.out.println(mvp.vectorMap.get(id).toString());
		
		String wholeMenu = mdo.allDescString.toString();
		System.out.println("Whole Menu: "+ wholeMenu);
//		
//		Analyzer az = new Analyzer() {
//			
//			@Override
//			protected TokenStreamComponents createComponents(String arg0) {
//				WhitespaceTokenizer source = new WhitespaceTokenizer();
//				ShingleFilter sf = new ShingleFilter(source);
//			    return new TokenStreamComponents(source, sf);
//			}
//		};
//		ShingleAnalyzerWrapper saw = new ShingleAnalyzerWrapper(az);
//		
//		
//		
//		StringReader sr = new StringReader(wholeMenu);
//		TokenStream ts = az.tokenStream("", sr);
//		TokenStream tt = saw.tokenStream("",sr );
//		CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
//		Set<String> bigrams = new HashSet<String>();
//		try{
//			ts.reset();
//			 while (ts.incrementToken()) {
//				 System.out.println(term.toString());
//		       }
//		     
//		       ts.end();
//		} catch(IOException ef){
//			ef.printStackTrace();
//		} finally{
//			try {
//				ts.close();
//			} catch (IOException ef) {
//				// TODO Auto-generated catch block
//				ef.printStackTrace();
//			}
//		}
		
		Document doc = new Document(wholeMenu, id);
		
		
	}
}
