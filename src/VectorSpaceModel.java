
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class implements the Vector-Space model.
 * It takes a corpus and creates the tf-idf vectors for each document.
 * @author swapneel
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
	public TreeMap<String, TreeMap<String, Double>> tfIdfWeights;
	
	/**
	 * The constructor.
	 * It will take a corpus of documents.
	 * Using the corpus, it will generate tf-idf vectors for each document.
	 * @param corpus the corpus of documents
	 */
	public VectorSpaceModel(Corpus corpus) {
		this.corpus = corpus;
		tfIdfWeights = new TreeMap<String, TreeMap<String, Double>>();
		
		createTfIdfWeights();
	}

	/**
	 * This creates the tf-idf vectors.
	 */
	private void createTfIdfWeights() {
		System.out.println("Creating the tf-idf weight vectors");
		Set<String> terms = corpus.getInvertedIndex().keySet();
		
		for (Document document : corpus.getDocuments()) {
			TreeMap<String, Double> weights = new TreeMap<String, Double>();
			
			for (String term : terms) {
//				System.out.print("***" + term +"***");
				double tf = document.getTermFrequency(term);
//				System.out.print(" tf: "+ tf);
				double idf = corpus.getInverseDocumentFrequency(term);
//				System.out.println(" idf: " + idf);
				
				double weight = tf * idf;
				
				weights.put(term, weight);
			}
			tfIdfWeights.put(document.getMenuId(), weights);
			System.out.println();
		}
	}
	
	/**
	 * This method will return the magnitude of a vector.
	 * @param document the document whose magnitude is calculated.
	 * @return the magnitude
	 */
	private double getMagnitude(Document document) {
		double magnitude = 0;
		TreeMap<String, Double> weights = tfIdfWeights.get(document);
		
		for (double weight : weights.values()) {
			magnitude += weight * weight;
		}
		
		return Math.sqrt(magnitude);
	}
	
	/**
	 * This will take two documents and return the dot product.
	 * @param d1 Document 1
	 * @param d2 Document 2
	 * @return the dot product of the documents
	 */
	private double getDotProduct(Document d1, Document d2) {
		double product = 0;
		TreeMap<String, Double> weights1 = tfIdfWeights.get(d1);
		TreeMap<String, Double> weights2 = tfIdfWeights.get(d2);
		
		for (String term : weights1.keySet()) {
			product += weights1.get(term) * weights2.get(term);
		}
		
		return product;
	}
	
	/**
	 * This will return the cosine similarity of two documents.
	 * This will range from 0 (not similar) to 1 (very similar).
	 * @param d1 Document 1
	 * @param d2 Document 2
	 * @return the cosine similarity
	 */
	public double cosineSimilarity(Document d1, Document d2) {
		return getDotProduct(d1, d2) / (getMagnitude(d1) * getMagnitude(d2));
	}
}