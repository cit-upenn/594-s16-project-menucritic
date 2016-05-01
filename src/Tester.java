import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Tester {
			public static void main(String[] args) throws IOException {
				String urlParameters  = "text="+args[0];
				byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
				int    postDataLength = postData.length;
				String request        = "http://text-processing.com/api/tag/";
				URL    url            = new URL( request );
				HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
				conn.setDoOutput( true );
				conn.setInstanceFollowRedirects( false );
				conn.setRequestMethod( "POST" );
//				conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
				conn.setRequestProperty( "charset", "utf-8");
				conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
				conn.setUseCaches( false );
				try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
					   wr.write( postData );
					   
					}
				BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                        conn.getInputStream()));
				 String decodedString;
			        while ((decodedString = in.readLine()) != null) {
			            System.out.println(decodedString);
			        }
			        in.close();
				
				
			}
}
