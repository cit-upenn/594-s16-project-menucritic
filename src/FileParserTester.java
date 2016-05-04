

import java.io.File;
import java.util.Scanner;
/**
 * This class is the main class for FileParser
 * @author meredithmargulies
 *
 */
public class FileParserTester {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		FileParser fp = new FileParser();
		fp.parseFile();
		fp.cleanData("BetterRestaurantRestults.json");
	}

}
