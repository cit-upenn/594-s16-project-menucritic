

/**
 * This class is the main class for FileParser
 * @author meredithmargulies
 *
 */
public class FileParserTester {

	public static void main(String[] args) throws Exception{

		FileParser fp = new FileParser();
		fp.parseFile();
		fp.cleanData("BetterRestaurantRestults.json");
	}

}
