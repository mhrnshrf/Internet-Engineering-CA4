import java.io.*;
import java.util.*;
import com.sun.net.httpserver.*;
import java.util.zip.*;

public class ZipHandler implements HttpHandler {
	public void handle(HttpExchange t) throws IOException {

		ZipInputStream in = new ZipInputStream(t.getRequestBody());
		ZipEntry entry = new ZipEntry("");
		while((entry = in.getNextEntry())!=null){
			
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(""+entry.getName()));
			int word = 0;
            while ((word = in.read()) != -1){
            	out.write(word);
			}
			// out.flush();
			out.close();
		}



        String s = "successful reconfiguration";
        byte[] result = s.getBytes();
        
        t.sendResponseHeaders(200, result.length);
        Headers headers = t.getResponseHeaders();
        headers.add("Date", Calendar.getInstance().getTime().toString());
        headers.add("Content-Type", "text/html");
        OutputStream os = t.getResponseBody();
        os.write(result);
        os.close();
    }


}