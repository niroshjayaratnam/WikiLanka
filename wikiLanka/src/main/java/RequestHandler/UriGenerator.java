/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RequestHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.utils.URIBuilder;
/**
 *
 * @author Bhash
 */
public class UriGenerator {
    
      
    
    private static final String key = "AIzaSyDgSpnKKVbY96decVrMcrrWWp721zVvxWU";
    
    //text search required
    private String sensor = "false";
    
    private URI nearByUri;
    private URI textSearchUri;
    

    public UriGenerator() {
          try {
              nearByUri  = new URIBuilder()
                .setScheme("https")
                .setHost("maps.googleapis.com")
                 .setPath("/maps/api/place/nearbysearch/json")
                 .setParameter("key",key).setParameter("sensor", sensor). build();
              textSearchUri =new URIBuilder()
                .setScheme("https")
                .setHost("maps.googleapis.com")
                .setPath("/maps/api/place/textsearch/json")
                 .setParameter("key",key).setParameter("sensor", sensor)
                 .build();
          } catch (URISyntaxException ex) {
              Logger.getLogger(UriGenerator.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          
    } 
    
    public URI nearbyPlaceSearch(double lat, double lng ,double radius  ) throws URISyntaxException{
         URI tempUri = new URIBuilder(nearByUri)
        .setParameter("location" , String.valueOf(lat)+ "," + String.valueOf(lng) )        
        .setParameter("radius",String.valueOf(radius) )
        .build();       
          System.out.println(tempUri);
      
        return tempUri;
       
        
    }
    public URI nearbyPlaceSearch(double lat, double lng ,double radius ,String keyword ) throws URISyntaxException{ 
        
          URI tempUri = new URIBuilder(nearByUri)
        .setParameter("keyword", keyword)
       .setParameter("location" , String.valueOf(lat)+ "," + String.valueOf(lng) )
        .setParameter("radius", String.valueOf(radius)) 
        .build();       
          System.out.println(tempUri);
      
        return tempUri;
        
    }
    
    public URI textSearch(String query) throws URISyntaxException{
        
         URI tempUri = new URIBuilder(textSearchUri)
        .setParameter("query", query)
        .build();
       
          System.out.println(tempUri);
      
        return tempUri;
        
    }
    
        public URI textSearch(double lat, double lng ,double radius ,String query ) throws URISyntaxException{        
          URI tempUri = new URIBuilder(textSearchUri)
        .setParameter("query", query)
        .setParameter("lat", String.valueOf(lat))
        .setParameter("lng", String.valueOf(lng))
        .setParameter("radius", String.valueOf(radius)) 
        .build();       
          System.out.println(tempUri);
      
        return tempUri;
    }
        
     public URI textSearch(String query, String type) throws IOException, URISyntaxException{
         
       URI tempUri = new URIBuilder(textSearchUri)
        .setParameter("query", query)
        .setParameter("type", type)       
        .build();
       System.out.println(tempUri);
      return tempUri;

        
    }
        
        
    
    
    
}
