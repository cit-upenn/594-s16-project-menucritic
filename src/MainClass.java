import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

public class MainClass {
	private MenuAnalyzer ma = new MenuAnalyzer();
	private MenuVectorMap mvp;
	
	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.start();
	}
	public MainClass(){
		ma = new MenuAnalyzer();
		mvp = new MenuVectorMap();
		
	}
	private void start() {
		//for each menu
			//new menuDataObject mdo(id, rating, chain)
			//for each item
				//updates counts, description will append to stringbuilder
				//ma.getTags(itemText)
				//ma.getTags(descText)
				//mdo.addItemMap(Tagmap)
				//mdo.addDescriptionMap(descMap)
				//mdo.addDescription(descText)
			
			//mvp.add(mdo) //updates MAV for this restaurant
				////-calls mdo.calculateTotals
		
		//mvp.generateTFIDFVecs();
		String id = "11";
		Double chain = 0.0;
		Double rating = 4.5;
		TreeMap<String, String> testMenu = new TreeMap<>();
		testMenu.put("Shrimp Scampi", "This pesto pesto pesto pesto pesto pesto accidentally delicious lemon jumbo shrimp is carefully sauteed in a white wine butter sauce");
		testMenu.put("Seared Scallops", "lemon pine nut juice reduction, micro greens, cooked tasty cream sauce");
		testMenu.put("Filet Mignon", "Seared prime cut cooked nicely medium-rare");
		testMenu.put("Duck ala King", "A scrupmtious pan-seared duck breast lathered in a Chambord balsamic reduction");
		MenuDataObject mdo = new MenuDataObject(id, rating, chain);
		String id2 = "12";
		Double chai2 = 0.0;
		Double rating2 = 4.5;
		TreeMap<String, String> testMenu2 = new TreeMap<>();
		testMenu2.put("Angel Hair with Pesto", "Fresh made pesto pesto tradtional, italian, angel hair, tossed in a lemon basil parmesan pine nut pesto");
		MenuDataObject mdo2 = new MenuDataObject(id2, rating2, chai2);
		
		String id3 = "13";
		Double chai3 = 0.0;
		Double rating3 = 4.5;
		TreeMap<String, String> testMenu3 = new TreeMap<>();
		testMenu3.put("Burger", "A Simple burger with lettuce and cheese");
		MenuDataObject mdo3 = new MenuDataObject(id3, rating3, chai3);
		
		long start = System.nanoTime();
		MenuAnalyzer ma = new  MenuAnalyzer();
		long end = System.nanoTime();
		System.out.println("Time taken to create menuAnalyzer: "+ (end - start)/Math.pow(10,9) );
		MenuVectorMap mvp = new MenuVectorMap();
		
		long start1 = System.nanoTime();
		for(String s: testMenu.keySet()){
			MyTuple<String, HashMap<String,String>> tupItem = ma.generateTags(s);
			mdo.addItemMap(tupItem.second);
			MyTuple<String, HashMap<String,String>> tupDesc = ma.generateTags(testMenu.get(s));
			
			mdo.addDescriptionMap(tupDesc.second);
			mdo.addDescription(tupDesc.first);
		}
//		mdo.calculateTotals();
		
		for(String s: testMenu2.keySet()){
			MyTuple<String, HashMap<String,String>> tupItem = ma.generateTags(s);
			mdo2.addItemMap(tupItem.second);
			MyTuple<String, HashMap<String,String>> tupDesc = ma.generateTags(testMenu2.get(s));
			mdo2.addDescriptionMap(tupDesc.second);
			mdo2.addDescription(tupDesc.first);
		}
//		mdo2.calculateTotals();
		
		for(String s: testMenu3.keySet()){
			MyTuple<String, HashMap<String,String>> tupItem = ma.generateTags(s);
			mdo3.addItemMap(tupItem.second);
			MyTuple<String, HashMap<String,String>> tupDesc = ma.generateTags(testMenu3.get(s));
			mdo3.addDescriptionMap(tupDesc.second);
			mdo3.addDescription(tupDesc.first);
		}
//		mdo3.calculateTotals();
		long end1 = System.nanoTime();
		System.out.println("Time taken to create all mdos: "+ (end1-start1)/Math.pow(10,9));
		
		long start2 = System.nanoTime();
		mvp.add(mdo);
		mvp.add(mdo2);
		mvp.add(mdo3);
		long end2 = System.nanoTime();
		System.out.println("Time taken to add mdos to mvp: "+ (end2-start2)/Math.pow(10,9));
//		System.out.println("mdo: " + mdo.allDescString.toString());
//		System.out.println("mdo2: " + mdo2.allDescString.toString());
//		System.out.println("mdo3: " + mdo3.allDescString.toString());
		
		long start3 = System.nanoTime();
		Document doc = new Document(mdo.allDescString.toString(), id);
		Document doc2 = new Document(mdo2.allDescString.toString(), id2);
		Document doc3 = new Document(mdo3.allDescString.toString(), id3);
		long end3 = System.nanoTime();
		System.out.println("Time taken to create documents "+ (end3-start3)/Math.pow(10,9));
		
		ArrayList<Document> docs = new ArrayList<>();
		docs.add(doc);
		docs.add(doc2);
		docs.add(doc3);
		long start4 = System.nanoTime();
		Corpus corp = new Corpus(docs);
		long end4 = System.nanoTime();
		System.out.println("Time taken to create corpus: "+ (end4-start4)/Math.pow(10,9));
		
		BigramGenerator bg = new BigramGenerator();
		
		Document doc11 = new Document(bg.generateBigrams(mdo.allDescString.toString()), id);
		Document doc21 = new Document(bg.generateBigrams(mdo2.allDescString.toString()), id2);
		Document doc31 = new Document(bg.generateBigrams(mdo3.allDescString.toString()), id3);
		ArrayList<Document> docs2 = new ArrayList<>();
		docs2.add(doc11);
		docs2.add(doc21);
		docs2.add(doc31);
		
		Corpus corp2 = new Corpus (docs2);
		System.out.println("got here");
		VectorSpaceModel vsm2 = new VectorSpaceModel(corp2);
		HashMap<String, TreeMap<String, Double>> vsmMap2 = vsm2.tfIdfWeights;
		for(String menuId: mvp.vectorMap.keySet()){
			
			//need to ensure iteration is the same each time. whereas .values() does not ensure
			//any order - use tree map or linked hashmap - orders keys everytime, but not values, thats
			//fine though i think
//			System.out.println("Mav id: "+ mav.id);
			Vector<Double> vec = new Vector<>();
			vec.addAll(vsmMap2.get(menuId).values());
			System.out.println(vec.size());
//			System.out.println("28: "+ vec.get(29));
			System.out.println(vec);
			mvp.vectorMap.get(menuId).setTFIDFWordsVec(vec);
			
		}
		
		
		
		long start5 = System.nanoTime();
		VectorSpaceModel vsm = new VectorSpaceModel(corp);
		long end5 = System.nanoTime();
		System.out.println("Time taken to create VectorSpaceModel "+ (end5-start5)/Math.pow(10,9));
		
		System.out.println("total time including menuAnalyzer creation"+ (end5-start)/Math.pow(10,9));
		
		HashMap<String, TreeMap<String, Double>> vsmMap = vsm.tfIdfWeights;
		//
//		System.out.println(mvp.vectorMap.values().size());
		for(String menuId: mvp.vectorMap.keySet()){
			
			//need to ensure iteration is the same each time. whereas .values() does not ensure
			//any order - use tree map or linked hashmap - orders keys everytime, but not values, thats
			//fine though i think
//			System.out.println("Mav id: "+ mav.id);
			Vector<Double> vec = new Vector<>();
			vec.addAll(vsmMap.get(menuId).values());
			System.out.println(vec.size());
//			System.out.println("28: "+ vec.get(29));
			System.out.println(vec);
			mvp.vectorMap.get(menuId).setTFIDFWordsVec(vec);
			
		}
	}
	
}
