/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Udara
 */
public class WeatherForcast {
    String start;
    String end;
    double co[] = new double[2];
    String address;
    String respond = "";
    DecimalFormat numberFormat = new DecimalFormat("#.000000");
  
    WeatherForcast(double[] co){
        this.co[0] = co[0];
        this.co[1] = co[1];
        co[0] = Math.round(co[0]*1000000.0)/1000000.0;
        co[1] = Math.round(co[1]*1000000.0)/1000000.0;
        address = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="+co[0]+"&lon="+co[1]+"&cnt=10&mode=xml";
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
    String forcast(String start,String end){
        String msg = "";
        readData();
        try{
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
       //     File file=new File("output.xml");
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(address);
            HttpResponse response = client.execute(get);
            Reader reader = new InputStreamReader(response.getEntity().getContent(),"UTF-8");
            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");
            Document doc = docBuilder.parse(address);

            NodeList list = doc.getElementsByTagName("forecast");
            int totalPods = list.getLength();
      //      System.out.println("Total no of pod : " + totalPods);
      //      msg = String.format("Total no of pod : " + totalPods + System.lineSeparator());
            if(totalPods == 0){
                
                msg = "No forcast avaliable :( ";
                return msg;
            }
            NodeList listOfPods = list.item(0).getChildNodes();
            System.out.println("forecast found : "+totalPods);
            if(start == null){
                DateTime dt = new DateTime(DateTimeZone.forID("Asia/Colombo"));
                String year = dt.year().getAsString();
                int m = Integer.parseInt(dt.monthOfYear().getAsText());
                int d = Integer.parseInt(dt.dayOfMonth().getAsText());
                start = year+"-"+m/10+""+m%10+"-"+d/10+""+d%10;
                
            }
   L1:      for(int s=0; s<listOfPods.getLength(); s++){
                
                Node firstPersonNode = listOfPods.item(s);
                if(firstPersonNode.getNodeName() == "time"){
                    boolean found = false;
                    NamedNodeMap attr = firstPersonNode.getAttributes();
                    
                    if(attr.item(0).getTextContent().equals(start)){
                            //System.out.println(attr.item(0).getTextContent()+"  "+start);
                            NodeList content = firstPersonNode.getChildNodes();
                            found = true;
                            String tag;

                            for(int d=0;d<content.getLength();d++){
                                for(int f=0;f<content.getLength();f++){
                                    tag = content.item(f).getNodeName();
                                    //System.out.println(tag);
                                    if(tag.equals("symbol")){
                                       msg = "Status : "+content.item(f).getAttributes().getNamedItem("name").getTextContent()+" "; 
                                    }else if(tag.equals("windDirection")){
                                        msg += "Wind Direction : "+content.item(f).getAttributes().getNamedItem("deg").getTextContent()+" "; 
                                    }else if(tag.equals("windSpeed")){
                                        msg += "Wind Speed : "+content.item(f).getAttributes().getNamedItem("mps").getTextContent()+"Miles/s.   ";
                                    }else if(tag.equals("temperature")){
                                        double kelvin = Double.parseDouble(content.item(f).getAttributes().getNamedItem("day").getTextContent());
                                            msg += "Temprature : "+Math.round(kelvin -273.3) +"  ";
                                    }else if(tag.equals("clouds")){
                                        msg += "Sky : "+content.item(f).getAttributes().getNamedItem("value").getTextContent()+"    ";
                                    }

                             
                                 
                        /*        if("plaintext".equals(subContent.item(f).getNodeName())){
                                //    System.out.println(subContent.item(f).getTextContent());
                                    s1 = subContent.item(f).getTextContent();
                                    s2 = new String(s1.getBytes("ascii"),"ascii");
                                    s2.replaceAll("  ", "");
                                    msg += s2+System.lineSeparator();
                                    
                                }*/
                            }
                        }
                        if(attr.item(0).getTextContent().equals(end)){
                                 found = false;
                        } 
                    }
                }
            }
        }catch(Exception e){
           // System.out.println(e.getMessage());
            msg = e.getMessage();
        }
        return msg;
    }
}
