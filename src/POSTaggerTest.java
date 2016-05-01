import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.HyphenatedWordsFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

public class POSTaggerTest {
		public static void main(String[] args) throws IOException {
			InputStream modelIn = null;
			POSModel model = null;
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
			
			
		POSTaggerME tagger = new POSTaggerME(model);
		
		String test = "A scrupmtious pan-seared duck breast lathered in a Chambord balsamic reduction";
		String [] sent = test.split(" ");
		String tags[] = tagger.tag(sent);
		double probs[] = tagger.probs();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i< tags.length; i++){
			System.out.println(sent[i] + " : " + tags[i]); //" : "+ probs[i]);
			
			sb.append(sent[i] + tags[i]+ " ");
		}
		System.out.println(sb.toString());
		System.out.println();
		Sequence topSequences[] = tagger.topKSequences(sent);
//		for(int i = 0; i< topSequences.length; i++){
//			System.out.println(topSequences[i].getOutcomes());
//			double proobs [] = topSequences[i].getProbs();
////			for(int j = 0; j < proobs.length; j++ ){
////				System.out.println(proobs[i]);
//			}
			
		
		
		
//		
		
	class MyAnalyzer extends Analyzer{

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		// TODO Auto-generated method stub
		TokenStreamComponents tsc = new TokenStreamComponents(new WhitespaceTokenizer());
		return new TokenStreamComponents(new WhitespaceTokenizer());
		
	}
			
		}
	
	MyAnalyzer az = new MyAnalyzer();
	
	
	TokenStream tokenStream = az.tokenStream("",new StringReader(test));
	HyphenatedWordsFilter hwf = new HyphenatedWordsFilter(tokenStream);
	CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
//	tokenStream.reset();
//	while(hwf.incrementToken()){
//		System.out.println(term.toString());
//	}
//	tokenStream.reset();
//	term = tokenStream.addAttribute(CharTermAttribute.class);
	 try {
	       tokenStream.reset();
	     
	       // print all tokens until stream is exhausted
	       while (tokenStream.incrementToken()) {
	         System.out.println(term.toString());
	       }
	     
	       tokenStream.end();
	     } finally {
	       tokenStream.close();
	     }
	   }
	 
}
