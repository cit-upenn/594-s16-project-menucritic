
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class implements the Vector-Space model.
 * It takes a corpus and creates the tf-idf vectors for each document.
 * @author swapneel and gabecolton
 *
 */
public class VectorSpaceModel {
	
	/**
	 * The corpus of documents.
	 */
	private Corpus corpus;
	
	/**
	 * The tf-idf weight vectors.
	 * The hashmap maps a menu id to another hashmap.
	 * The second hashmap maps a term to its tf-idf weight for this document.
	 */
	public HashMap<Integer, TreeMap<String, Double>> tfIdfWeights;
	
	/**
	 * The constructor.
	 * It will take a corpus of documents.
	 * Using the corpus, it will generate tf-idf vectors for each document.
	 * @param corpus the corpus of documents
	 */
	public VectorSpaceModel(Corpus corpus) {
		this.corpus = corpus;
		tfIdfWeights = new HashMap<Integer, TreeMap<String, Double>>();
		
		createTfIdfWeights();
	}

	/**
	 * This creates the tf-idf vectors.
	 */
	private void createTfIdfWeights() {
		System.out.println("Creating the tf-idf weight vectors");
		
		Set<String> terms = corpus.getInvertedIndex().keySet();
		System.out.println("Number of terms: "+terms.size());
		int i = 0; 
		for (Document document : corpus.getDocuments()) {
			TreeMap<String, Double> weights = new TreeMap<String, Double>();
			System.out.println("Done with document "+ i++);
			for (String term : terms) {
				double tf = document.getTermFrequency(term);
				double idf = corpus.getInverseDocumentFrequency(term);
				double weight = tf * idf;
				weights.put(term, weight);
			}
			tfIdfWeights.put(document.getMenuId(), weights);
		}
	}

}