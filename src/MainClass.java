import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class MainClass {
	private MenuVectorMap mvp;
	final private int NUM_THREADS = 5;

	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.startThreaded();
	}
	public MainClass(){
		mvp = new MenuVectorMap();

	}

	private void startThreaded() {
		/* create collection of threads to be executed */
		Collection<Callable<MyTuple<Document,Document>>> tasks = new ArrayList<>();
		DataInterface di = new DataInterface("cleanData.json");
		ArrayList<RestaurantMenu> allMenus = di.getAllMenus();
		
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(NUM_THREADS);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(30 * 1000);
        
        MenuAnalyzerFactory maf = new MenuAnalyzerFactory();
        GenericObjectPool<MenuAnalyzer> maPool = new GenericObjectPool<MenuAnalyzer>(maf, config);
		
		for (RestaurantMenu menu : allMenus) { /* loop to parse data */
			int id = menu.getID();
			double rating = menu.getRating();
			HashMap <String, String> menuContents = menu.getMenuAndDescriptions();
			double chain = menu.isChain()? 1.0 : 0.0;
			tasks.add(new MenuDataObject(id, rating, menuContents, chain, maPool , mvp));
		}

        /* execute each item rating prediction thread and store in 'results' */
		List<Future<MyTuple<Document, Document>>> results = null;
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
		try {
			results = executor.invokeAll(tasks);
		} catch (Exception ignore) {
			throw new RuntimeException();
		} finally {
			executor.shutdown();
		}
		System.out.println("Handed out all tasks");
		/* populate return values of thread pool into two ArrayList */
		ArrayList<Document> monograms = new ArrayList<>();
		ArrayList<Document> bigrams = new ArrayList<>();
		//first Document in tuple is for monograms, second is for bigrams
		int i = 0; 
		for (Future<MyTuple<Document, Document>> f : results) {
			try {
				System.out.println("adding mongrams and bigrams: " + i++);
				monograms.add(f.get().first);
				bigrams.add(f.get().second);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("All Documents added");
		VectorSpaceModel monogramsVSM = new VectorSpaceModel(new Corpus(monograms));
		VectorSpaceModel bigramsVSM = new VectorSpaceModel(new Corpus(bigrams));
		System.out.println("Corpi and Vector Space Models created");
		HashMap<Integer, TreeMap<String, Double>> monoVSMMap = monogramsVSM.tfIdfWeights;
		HashMap<Integer, TreeMap<String, Double>> biVSMMap = bigramsVSM.tfIdfWeights;
		Map<Integer, MenuAttributeVector> menusAttributesMap = mvp.vectorMap;
		int j = 0; 
		for (Integer menuId: menusAttributesMap.keySet()){
			Vector<Double> vecMono = new Vector<>();
			Vector<Double> vecBi = new Vector<>();
			if (!monoVSMMap.containsKey(menuId) || !biVSMMap.containsKey(menuId)) {
				throw new IllegalStateException("Menu id did not exist");
			}
			vecMono.addAll(monoVSMMap.get(menuId).values());
			vecBi.addAll(biVSMMap.get(menuId).values());
			menusAttributesMap.get(menuId).setTFIDFWordsVec(vecMono);
			menusAttributesMap.get(menuId).setTFIDFBigramsVec(vecBi);
			System.out.println("Finished iteration "+ j++);
		}
//		 for(MenuAttributeVector mav : mvp.vectorMap.values()){
//			 System.out.println(mav);
//			}
		PrintWriter writer = null;
		try {
			
			//change the output name/file extenstion as needed
			 writer = new PrintWriter("vec_output.txt");
			 System.out.println("PrintWriter created");
			 //change or comment ou
			 MenuAttributeVector.AttributeOptions [] options = {
					 //couldn't find out how to import enums...
					 MenuAttributeVector.AttributeOptions.WL_ITEM,
					 MenuAttributeVector.AttributeOptions.ADJ_ITEM,
					 MenuAttributeVector.AttributeOptions.ADV_ITEM,
					 MenuAttributeVector.AttributeOptions.NW_ITEM,
					 MenuAttributeVector.AttributeOptions.WL_DESC,
					 MenuAttributeVector.AttributeOptions.ADJ_DESC,
					 MenuAttributeVector.AttributeOptions.ADV_DESC,
					 MenuAttributeVector.AttributeOptions.NW_DESC,
//					 MenuAttributeVector.AttributeOptions.CHAIN,
//					 MenuAttributeVector.AttributeOptions.RATING,
					 MenuAttributeVector.AttributeOptions.TFIDF_WORD,
					 MenuAttributeVector.AttributeOptions.TFIDF_BIGRAM
					 
			 };
			 System.out.println("Options Set");
			 for(MenuAttributeVector mav : mvp.vectorMap.values()){
				 System.out.println("writing vector "+ mav.id);
					writer.println(mav.getVectorString(options));
				}
			 System.out.println("All Vectors written");
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (writer != null){
				writer.close();
			}
		}
		
		
//		try	{
//			FileOutputStream fos = new FileOutputStream("smallTest.ser");
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//			oos.writeObject(mvp);
//			oos.close();
//			fos.close();
//			System.out.printf("Serialized HashMap data is saved in MenuVectorMap2.ser");
//		} catch(IOException ioe) {
//			ioe.printStackTrace();
//		}

	}


//	private void start() {
//		//for each menu
//		//new menuDataObject mdo(id, rating, chain)
//		//for each item
//		//updates counts, description will append to stringbuilder
//		//ma.getTags(itemText)
//		//ma.getTags(descText)
//		//mdo.addItemMap(Tagmap)
//		//mdo.addDescriptionMap(descMap)
//		//mdo.addDescription(descText)
//
//		//mvp.add(mdo) //updates MAV for this restaurant
//		////-calls mdo.calculateTotals
//
//		//mvp.generateTFIDFVecs();
//		String id = "11";
//		Double chain = 0.0;
//		Double rating = 4.5;
//		TreeMap<String, String> testMenu = new TreeMap<>();
//		testMenu.put("Shrimp Scampi", "This pesto pesto pesto pesto pesto pesto accidentally delicious lemon jumbo shrimp is carefully sauteed in a white wine butter sauce");
//		testMenu.put("Seared Scallops", "lemon pine nut juice reduction, micro greens, cooked tasty cream sauce");
//		testMenu.put("Filet Mignon", "Crème brûlée cut cooked nicely medium-rare");
//		testMenu.put("Duck ala King", "A scrupmtious pan-seared duck breast lathered in a Chambord balsamic reduction");
//		MenuDataObject mdo = new MenuDataObject(id, rating, chain);
//		String id2 = "12";
//		Double chai2 = 0.0;
//		Double rating2 = 4.5;
//		TreeMap<String, String> testMenu2 = new TreeMap<>();
//		testMenu2.put("Angel Hair with Pesto", "Fresh made pesto pesto tradtional, italian, angel hair, tossed in a lemon basil parmesan pine nut pesto");
//		MenuDataObject mdo2 = new MenuDataObject(id2, rating2, chai2);
//
//		String id3 = "13";
//		Double chai3 = 0.0;
//		Double rating3 = 4.5;
//		TreeMap<String, String> testMenu3 = new TreeMap<>();
//		testMenu3.put("Burger", "A Simple burger with lettuce and cheese");
//		MenuDataObject mdo3 = new MenuDataObject(id3, rating3, chai3);
//
//		long start = System.nanoTime();
//		MenuAnalyzer ma = new  MenuAnalyzer();
//		long end = System.nanoTime();
//		System.out.println("Time taken to create menuAnalyzer: "+ (end - start)/Math.pow(10,9) );
//		MenuVectorMap mvp = new MenuVectorMap();
//
//		long start1 = System.nanoTime();
//		for(String s: testMenu.keySet()){
//			MyTuple<String, HashMap<String,String>> tupItem = ma.generateTags(s);
//			mdo.addItemMap(tupItem.second);
//			MyTuple<String, HashMap<String,String>> tupDesc = ma.generateTags(testMenu.get(s));
//
//			mdo.addDescriptionMap(tupDesc.second);
//			mdo.addDescription(tupDesc.first);
//		}
////		mdo.calculateTotals();
//
//		for(String s: testMenu2.keySet()){
//			MyTuple<String, HashMap<String,String>> tupItem = ma.generateTags(s);
//			mdo2.addItemMap(tupItem.second);
//			MyTuple<String, HashMap<String,String>> tupDesc = ma.generateTags(testMenu2.get(s));
//			mdo2.addDescriptionMap(tupDesc.second);
//			mdo2.addDescription(tupDesc.first);
//		}
////		mdo2.calculateTotals();
//
//		for(String s: testMenu3.keySet()){
//			MyTuple<String, HashMap<String,String>> tupItem = ma.generateTags(s);
//			mdo3.addItemMap(tupItem.second);
//			MyTuple<String, HashMap<String,String>> tupDesc = ma.generateTags(testMenu3.get(s));
//			mdo3.addDescriptionMap(tupDesc.second);
//			mdo3.addDescription(tupDesc.first);
//		}
////		mdo3.calculateTotals();
//		long end1 = System.nanoTime();
//		System.out.println("Time taken to create all mdos: "+ (end1-start1)/Math.pow(10,9));
//
//		long start2 = System.nanoTime();
//		mvp.add(mdo);
//		mvp.add(mdo2);
//		mvp.add(mdo3);
//		long end2 = System.nanoTime();
//		System.out.println("Time taken to add mdos to mvp: "+ (end2-start2)/Math.pow(10,9));
////		System.out.println("mdo: " + mdo.allDescString.toString());
////		System.out.println("mdo2: " + mdo2.allDescString.toString());
////		System.out.println("mdo3: " + mdo3.allDescString.toString());
//
//		long start3 = System.nanoTime();
//		Document doc = new Document(mdo.allDescString.toString(), id);
//		Document doc2 = new Document(mdo2.allDescString.toString(), id2);
//		Document doc3 = new Document(mdo3.allDescString.toString(), id3);
//		long end3 = System.nanoTime();
//		System.out.println("Time taken to create documents "+ (end3-start3)/Math.pow(10,9));
//
//		ArrayList<Document> docs = new ArrayList<>();
//		docs.add(doc);
//		docs.add(doc2);
//		docs.add(doc3);
//		long start4 = System.nanoTime();
//		Corpus corp = new Corpus(docs);
//		long end4 = System.nanoTime();
//		System.out.println("Time taken to create corpus: "+ (end4-start4)/Math.pow(10,9));
//
//		BigramGenerator bg = new BigramGenerator();
//
//		Document doc11 = new Document(bg.generateBigrams(mdo.allDescString.toString()), id);
//		Document doc21 = new Document(bg.generateBigrams(mdo2.allDescString.toString()), id2);
//		Document doc31 = new Document(bg.generateBigrams(mdo3.allDescString.toString()), id3);
//		ArrayList<Document> docs2 = new ArrayList<>();
//		docs2.add(doc11);
//		docs2.add(doc21);
//		docs2.add(doc31);
//
//		Corpus corp2 = new Corpus (docs2);
//		System.out.println("got here");
//		VectorSpaceModel vsm2 = new VectorSpaceModel(corp2);
//		HashMap<String, TreeMap<String, Double>> vsmMap2 = vsm2.tfIdfWeights;
//		for(String menuId: mvp.vectorMap.keySet()){
//
//			//need to ensure iteration is the same each time. whereas .values() does not ensure
//			//any order - use tree map or linked hashmap - orders keys everytime, but not values, thats
//			//fine though i think
////			System.out.println("Mav id: "+ mav.id);
//			Vector<Double> vec = new Vector<>();
//			vec.addAll(vsmMap2.get(menuId).values());
//			System.out.println(vec.size());
////			System.out.println("28: "+ vec.get(29));
//			System.out.println(vec);
//			mvp.vectorMap.get(menuId).setTFIDFBigramsVec(vec);
//
//		}
//
//
//
//		long start5 = System.nanoTime();
//		VectorSpaceModel vsm = new VectorSpaceModel(corp);
//		long end5 = System.nanoTime();
//		System.out.println("Time taken to create VectorSpaceModel "+ (end5-start5)/Math.pow(10,9));
//
//		System.out.println("total time including menuAnalyzer creation"+ (end5-start)/Math.pow(10,9));
//
//		HashMap<String, TreeMap<String, Double>> vsmMap = vsm.tfIdfWeights;
//		//
////		System.out.println(mvp.vectorMap.values().size());
//		for(String menuId: mvp.vectorMap.keySet()){
//
//			//need to ensure iteration is the same each time. whereas .values() does not ensure
//			//any order - use tree map or linked hashmap - orders keys everytime, but not values, thats
//			//fine though i think
////			System.out.println("Mav id: "+ mav.id);
//			Vector<Double> vec = new Vector<>();
//			vec.addAll(vsmMap.get(menuId).values());
//			System.out.println(vec.size());
////			System.out.println("28: "+ vec.get(29));
//			System.out.println(vec);
//			mvp.vectorMap.get(menuId).setTFIDFWordsVec(vec);
//
//		}
//
//	}

}
