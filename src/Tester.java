
public class Tester {
	/**
	 * Main to test that the bigram generator works
	 * @param args
     */
	public static void main(String[] args) {
		long start = System.nanoTime();
		BigramGenerator bg = new BigramGenerator();
		long end = System.nanoTime();
		
		System.out.println("time taken to create bigram generator "+ (end-start)/(Math.pow(10,9)));
		long start1 = System.nanoTime();
		bg.generateBigrams("hello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fishhello cats grapefruits cables talented yellow red fish ");
		long end1 = System.nanoTime();
		System.out.println("time taken to create bigrams "+ (end1-start1)/(Math.pow(10,9)));
		
	}
}
