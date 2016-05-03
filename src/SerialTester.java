import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SerialTester {
	public static void main(String [] args)
	   {
	      MenuVectorMap mvp = null;
	      try
	      {
	         FileInputStream fis = new FileInputStream("smallTest.ser");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         mvp = (MenuVectorMap) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(IOException ioe)
	      {
	         ioe.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Class not found");
	         c.printStackTrace();
	         return;
	      }
	      System.out.println("Deserialized MenuVectorMap..");
	      // Display content using Iterator
	      for(MenuAttributeVector mav: mvp.vectorMap.values()){
	    	  System.out.println(mav.id);
	    	  System.out.println(mav);
	      }
	    
	   }
}

