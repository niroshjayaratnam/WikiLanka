/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RequestHandler;

import ResponseHandler.FinalResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.net.www.http.HttpClient;

/**
 *
 * @author Bhash
 */
public class RequestGenerator {    
 
    String keyword;
    
   public FinalResponse searchRequest(URI uri) throws MalformedURLException, IOException, URISyntaxException{
   
   CloseableHttpClient httpclient = HttpClients.createDefault();
   HttpGet httpget = new HttpGet(uri);
   final FinalResponse fr;
      // JsonObject json = new JsonObject();
   

ResponseHandler<FinalResponse> rh = new ResponseHandler<FinalResponse>() {

            

       @Override
       public FinalResponse handleResponse(HttpResponse hr) throws ClientProtocolException, IOException {
        
       HttpEntity entity = hr.getEntity();
        Gson gson = new GsonBuilder().create();
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        Reader reader = new InputStreamReader(entity.getContent(), charset);
        
        return gson.fromJson(reader, FinalResponse.class);
         }
    };
        return httpclient.execute(httpget,rh);
  
    
    
    
    
    
    
   }
}