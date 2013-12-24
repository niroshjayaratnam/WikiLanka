package hms.kite.samples.client;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import opennlp.tools.parser.Parse;
import org.eclipse.jetty.util.ajax.JSONDateConvertor;


/**
 *
 * @author Udara
 */
public class MindObj {
    int relevance[] = new int[7];
    private int wolframalpha = 0,dictionary = 1,medical = 2,drug = 3 , personality = 4 , busRoute = 5 , wikipedia = 6;
    private String msg = "";
    private String reply = "";
    private String interest = "";
    private String ansFormat = "";
    private Memory_Module memory;
    String[] location;
    double[] coordinate = new double[2];
//    Parse parse;
    String nouns[] = null;
    String nounPhrases[];
    
    String category = "";
    String action = "";
    Entities entities;
    //List<HashMap<String,Date>> dateRange;
    
    
      //  private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

    
    Gson gson = new Gson();
    void setMsg(String m){
        msg = m;
    }
    String getMsg(){
        return msg;
    }
    String getStartPossition(){
        if(!action.equals("NAVIGATION_DIRECTIONS") && !action.equals("TRANSIT_NEXT_BUS")
                && !action.equals("TRANSIT_SCHEDULE")) {
            return null;
        }
        String start = entities.departing;
        return start;
    }
    String getEndPossition(){
        if(!action.equals("NAVIGATION_DIRECTIONS") && !action.equals("TRANSIT_NEXT_BUS")
                && !action.equals("TRANSIT_SCHEDULE")) {
            return null;
        }
        String end = entities.destination;
        return end;
    }
    String getTravelMethod(){
        if(!action.equals("NAVIGATION_DIRECTIONS")) {
            return null;
        }
        String method = entities.transitType;
        if(method != null){
            return method;
        }else return "bus";
    }
    String getStartDate(){
        if(action.equals("WEATHER_STATUS")){
            return entities.dateRange.get(0).get("start")  ;
        }       
        return null;
    }
    String getEndDate(){
        if(!action.equals("WEATHER_STATUS")){
            return null;
        }
        String end = entities.dateRange.get(0).get("end");
        return end;
    }
    String trainTime(){
        String time = entities.timeRange.get(0).get("start")  ;
        if(time.contains("AM")){
            return time.substring(0,8);
        }else if(time.contains("PM")){
            int h = Integer.parseInt(time.substring(0,2));
            h = (h+12)%24;
            String temp = h/10+""+h%10+time.substring(2,8);
            return temp;
        }
        return null;
    }

    void setReply(String in) {
        reply = in;
    }
    void appendReply(String in){
        reply += " "+in.trim();
    }
    String getReply(){
        return reply;
    }
    void setInterest(String in){
        interest = in;
    }
    String getInterest(){
        return interest;
    }
    void formatAnswer(String in){
        ansFormat = in;
    }
    void setLocation(double latitude,double longitude){
        coordinate[0] = latitude;
        coordinate[1] = longitude;
    }
    double[] getLocation(){
        String town = entities.location;
        double co[] = new double[2];
        if(town != null){
            try {
                
                URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+town+"&sensor=false");
                            URLConnection yd = url.openConnection();
                            BufferedReader b = new BufferedReader(new InputStreamReader(yd.getInputStream()));
                            String content = "";
                            String inputLine;
                            while((inputLine = b.readLine()) != null){
                                content += inputLine;
                            }
                            GeocodeResult geo = gson.fromJson(content, GeocodeResult.class);
                co[0] = geo.getResults().get(0).getGeometry().getLocation().getLat().doubleValue();
                co[1] = geo.getResults().get(0).getGeometry().getLocation().getLng().doubleValue();
                System.out.println("Place -> "+co[0]+" - "+co[1]);
            } catch (IOException ex) {
                Logger.getLogger(MindObj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            LBSRequest loc = new LBSRequest();
            double[] d = loc.sendRequest(memory.getID());
            co[0] = d[0];
            co[1] = d[1];
        }
        return co;
    }
    String getTitle(){
        if(action.equals("ENTERTAINMENT_MOVIE")){
            return entities.title;
        }else{
            return null;
        }
    }
 /*   void setDataRange (List<HashMap<String,Date>> dateRange){
        this.dateRange = dateRange;
    }
    List<HashMap<String,Date>> getDuration(){
        return dateRange;
    }*/
    Memory_Module getMemory(){
        return memory;
    }
    void setMemory(Memory_Module m){
        memory = m;
    }
}
class Entities{
    String departing;
    String destination;
    String transitType;
    List<HashMap<String, String>> dateRange;
    List<HashMap<String, String>> timeRange;
    String location;
    String time;
    String title;
  /*  void setStart(Date start) throws JsonParseException{
        this.start = start;
    }
    Date getStart(){
        return start;
    }
    void setEnd(Date end) throws JsonParseException{
        this.end = end;
    }
    Date getEnd(){
        return end;
    }*/
}
