/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package placeapi;


import RequestHandler.RequestGenerator;
import RequestHandler.UriGenerator;
import ResponseHandler.FinalResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Bhash
 */
public class PlaceApi {

    /**
     * @param args the command line arguments
     */
     
    
   public String[] nearByFive(double lat, double lng,int radius, String type) throws URISyntaxException, MalformedURLException, IOException{
       
       RequestGenerator rg = new RequestGenerator();
       UriGenerator ug = new UriGenerator();
       URI uri = ug.nearbyPlaceSearch(lat, lng, radius,type);
      
       Gson gson = new GsonBuilder().create();
       System.out.println("No problem out to here");
       FinalResponse fr = rg.searchRequest(uri);
       System.out.println("I think we got thing right");
      String[] address = new String[5];
       int i =0;
           if(fr != null && "OK".equals(fr.getStatus())){
               while(i<5 &&  fr.getResults() != null && fr.getResults().size()>i   ){
                   System.out.println(fr.getResults().get(i).getVicinity());
                    address[i] =fr.getResults().get(i).getName() +" " + fr.getResults().get(i).getVicinity();
                    i++;                   
               }
               System.out.println(i);
               return address;
           }else{
               if(fr != null){
                  System.out.println(fr.getStatus()); 
               }else{
                   System.out.println("Error occured");
               }
               
               return  address;
           }      
   }
   
    public String[] textSearchFive(double lat, double lng,int radius, String query) throws URISyntaxException, MalformedURLException, IOException{
       
       RequestGenerator rg = new RequestGenerator();
       UriGenerator ug = new UriGenerator();
       URI uri = ug.textSearch(lat, lng, radius,query);
      
       Gson gson = new GsonBuilder().create();    
       FinalResponse fr = rg.searchRequest(uri);
      String[] address = new String[5];
       int i =0;
           if(fr != null && "OK".equals(fr.getStatus())){
               while(i<5 &&  fr.getResults() != null && fr.getResults().size()>i   ){
               //    System.out.println(fr.getResults().get(i).getVicinity());
                    address[i] =fr.getResults().get(i).getName() +" " + fr.getResults().get(i).getFormatted_address();
                    i++;                   
               }
               System.out.println(i);
               return address;
           }else{
               if(fr != null){
                  System.out.println(fr.getStatus()); 
               }else{
                   System.out.println("Error occured");
               }
               
               return  address;
           }      
   }
}
