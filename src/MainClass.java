import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainClass {
	private MenuVectorMap mvp;
	final private int NUM_THREADS = 5;

	private final String outputFileName = "excel_output.txt";

	public MainClass(){
		mvp = new MenuVectorMap();
	}

	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.startThreaded();
	}

	/**
	 * Start multi-threaded computation of word metrics
	 * (bigram analysis is commented out because we found that it was not helpful in analyzing the
	 * data)
	 */
	public void startThreaded() {
		/* create collection of threads to be executed */
		Collection<Callable<MyTuple<Document,Document>>> tasks = new ArrayList<>();
		DataInterface di = new DataInterface("FinalData.json");
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
//		ArrayList<Document> bigrams = new ArrayList<>();
//		first Document in tuple is for monograms, second is for bigrams
		int i = 0;
		for (Future<MyTuple<Document, Document>> f : results) {
			try {
				System.out.println("adding mongrams and bigrams: " + i++);
				monograms.add(f.get().first);
//				bigrams.add(f.get().second);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("All Documents added");
		Corpus corpMono = new Corpus(monograms);
//		Corpus corpBigr = new Corpus(bigrams);
		System.out.println("Corpi created");
		VectorSpaceModel monogramsVSM = new VectorSpaceModel(corpMono);
//		VectorSpaceModel bigramsVSM = new VectorSpaceModel(corpBigr);
		System.out.println("Vector Space Models created");
		HashMap<Integer, TreeMap<String, Double>> monoVSMMap = monogramsVSM.tfIdfWeights;
//		HashMap<Integer, TreeMap<String, Double>> biVSMMap = bigramsVSM.tfIdfWeights;
		Map<Integer, MenuAttributeVector> menusAttributesMap = mvp.vectorMap;
		int j = 0;
		for (Integer menuId: menusAttributesMap.keySet()){
			Vector<Double> vecMono = new Vector<>();
//			Vector<Double> vecBi = new Vector<>();
			if (!monoVSMMap.containsKey(menuId) ){//|| !biVSMMap.containsKey(menuId)) {
				throw new IllegalStateException("Menu id did not exist");
			}
			vecMono.addAll(monoVSMMap.get(menuId).values());
//			vecBi.addAll(biVSMMap.get(menuId).values());
			menusAttributesMap.get(menuId).setTFIDFWordsVec(vecMono);
//			menusAttributesMap.get(menuId).setTFIDFBigramsVec(vecBi);
			System.out.println("Finished iteration "+ j++);
		}
		outputFile(outputFileName);
	}

	/**
	 * Write all of the MenuAttributeVectors out to a file to be used in analysis
	 * @param outputFile
	 */
	private void outputFile(String outputFile) {
		PrintWriter writer = null;
		try {
			//change the output name/file extenstion as needed
			writer = new PrintWriter(outputFile);
			System.out.println("PrintWriter created");
			//change or comment out undesired attributes
			MenuAttributeVector.AttributeOptions [] options = {
					//couldn't find out how to import enums more elegantly...
					MenuAttributeVector.AttributeOptions.WL_ITEM,
					MenuAttributeVector.AttributeOptions.ADJ_ITEM,
					MenuAttributeVector.AttributeOptions.ADV_ITEM,
					MenuAttributeVector.AttributeOptions.NW_ITEM,
					MenuAttributeVector.AttributeOptions.WL_DESC,
					MenuAttributeVector.AttributeOptions.ADJ_DESC,
					MenuAttributeVector.AttributeOptions.ADV_DESC,
					MenuAttributeVector.AttributeOptions.NW_DESC,
			};
			System.out.println("Options Set");
			for(MenuAttributeVector mav : mvp.vectorMap.values()){
				writer.println(mav.getVectorString(options));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (writer != null){
				writer.close();
			}
		}

	}
}
