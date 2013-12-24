/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author udara
 */
public class MovieSearch {
    private String respond = "";
    private String title = "";
    private String address;
    Movie mov;
    public MovieSearch(String title) {
        this.title = title;
        address = genURL(title);
        readData();
        int i=0;
        while(respond.charAt(i) != '{'){
            i++;
        }
        respond = respond.substring(i);
        Gson gson = new Gson();
        Rotten rot = gson.fromJson(respond,Rotten.class);
        mov = rot.getMovies();
        System.out.println("Title --> "+mov.title);
    }
    
    String getInfo(){
        String output = "";
        if(mov == null){
            output += "Sorry! I didn't find that movie.";
        }else{
            output += "Title : "+mov.title+"\n  ";
            output += "Year : "+mov.year+"\n  ";
            output += (mov.release_dates.containsKey("theater"))? "Release : "+mov.release_dates.get("theater") +"\n  " 
                    : "";
            output += (mov.ratings.containsKey("audience_score"))?"Score : "+mov.ratings.get("audience_score")+"\n  "
                    : "";
            output += "Age rating : "+mov.mpaa_rating+"\n  ";
        }
        return output;
    }
    
    void readData(){
        try{
            URL oracle = new URL(address);
            URLConnection yc = oracle.openConnection();
             BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
      //      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.xml")));
            while ((inputLine = in.readLine()) != null) {
                respond += inputLine + "\n";
            }
            in.close();
        //    out.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println();
    }
    static String genURL(String input){
        String output = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=wqg7swt7zamcex6zqs7buqvg&q=";
        for(int s=0;s<input.length();s++){
            char c = input.charAt(s);
            if(c>47 && c<58)        output += c ;      //numeric numbers
            else if(c>64 && c<91)   output += c ;       //upper case letters
            else if(c>96 && c<123)  output += c ;       //lower case letters
            else if(c>31 && c<127)  output += "%"+Integer.toHexString(c);
        }
        output += "&page_limit=1";   //entering my appID
  //      System.out.println(output);
        return output;
    }
}
class Rotten{
    int total;
    Movie[] movies;

    public Rotten() {
    }
    Movie getMovies(){
        if(total == 0)  return null;
        else{
            return movies[0];
        }
    }
}
class Movie{
    String id;
    String title;
    Integer year;
    String mpaa_rating;
    String runtime;
    HashMap<String, String> release_dates;
    HashMap<String, String> ratings;
    String synopsis;

    public Movie() {
    }
    
}
