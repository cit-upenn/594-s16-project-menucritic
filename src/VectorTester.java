
import java.util.Vector;



public class VectorTester {

	public static void main(String[] args) {
		  
		  Vector<Double> tfidfWordVec = new Vector<>();
		  Vector<Double> tfidfBigramVec = new Vector<>();
	
		  Double [] vals = {1.11,2.22,3.33,4.44,5.55,6.66,7.77,8.88,1.0,5.0};
		  String id = "1234";
		MenuAttributeVector mav = new MenuAttributeVector(vals,id);
		
		tfidfWordVec.add(2.2);
		tfidfWordVec.add(4.4);
		tfidfWordVec.add(5.5);
		mav.setTFIDFWordsVec(tfidfWordVec);
		
		tfidfBigramVec.add(9.9);
		tfidfBigramVec.add(83.2);
		tfidfBigramVec.add(7.7);
		mav.setTFIDFBigramsVec(tfidfBigramVec);
		
		System.out.println(mav);
		
		final MenuAttributeVector.AttributeOptions a = null;
		MenuAttributeVector.AttributeOptions [] options = {a.RATING,a.CHAIN,a.ADV_ITEM,a.ADV_DESC,a.NW_DESC,a.ADJ_DESC, a.ADJ_ITEM,a.TFIDF_BIGRAM};
		System.out.println(mav.getVectorString(options));
		System.out.println(mav.getVector().toString());

		
		
	}
	
	

}
