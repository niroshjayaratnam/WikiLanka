/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TrainTimetable {

    String nextTrain(String from, String to, String timer , Connection con) throws Exception {
        TrainSchedule sc = null;
        // String[] price = new String[3];
        String time;
        if(timer == null){
        Date day = Calendar.getInstance().getTime();
        String s = day.toString();
        String[] details = s.split(" ");
        time = details[3];} else{
            time = timer;
        }
        //System.out.println(day);

        String start = readDB(from , con);
        String des = readDB(to , con);

        if (start != null && des != null) {
            ArrayList<TrainSchedule> schedules = new ArrayList<TrainSchedule>();
            String url = generateURL(start, des);



            Document doc = Jsoup.connect(url).get();
            Elements es = doc.getElementsByTag("tr");


            try {
                for (int i = 10; i < es.size() - 2; i++) {
                    //Element e = es.get(i);
                    //System.out.println(e);
                    TrainSchedule ts = new TrainSchedule(es.get(i).child(1).text(), es.get(i + 1).child(1).text(), es.get(i + 2).child(1).text(), es.get(i + 3).child(1).text(), es.get(i + 4).child(1).text(), es.get(i + 5).child(1).text(), es.get(i + 6).child(1).text(), es.get(i + 7).child(1).text());
                    schedules.add(ts);
                    //System.out.println(es.get(i).child(0).text());
                }
            } catch (Exception err) {
                for (int i = 6; i < es.size() - 10; i++) {

                    Element e = es.get(i);
                    TrainSchedule ts = new TrainSchedule(e.child(0).text(), e.child(1).text(), e.child(2).text(), e.child(3).text(), e.child(4).text(), e.child(5).text(), e.child(6).text(), e.child(7).text());
                    schedules.add(ts);

                }
            }


            /*for (int j = es.size() - 8; j < es.size() - 5; j++) {
             String tmp = es.get(j).child(1).text();
             price[j - es.size() + 8] = tmp.substring(0, tmp.indexOf("R") + 1);
             }*/

            for (int i = 0; i < schedules.size(); i++) {
                //schedules.get(i).show();
                //System.out.println(time);
                int check = time.compareTo(schedules.get(i).arrival);

                if (check < 0) {
                    sc = schedules.get(i);
                    //schedules.get(i).show();
                    break;
                }



            }
            String response;
            if (sc != null) {
                String[] sms = new String[3];
                //sms[0] = "Train arrives " +from + " at "+ sc.arrival +" & depature at "+sc.departure+".It will reach "+to+" at "+sc.reaching +".Train is a "+sc.Type.toLowerCase() +" available "+ sc.availability.toLowerCase();
                sms[1] = "Train arriving " + from + " at " + sc.arrival + " will depart at " + sc.departure + ".it will reach " + to + " by " + sc.reaching + " the " + sc.Type.toLowerCase() + " train is available on " + sc.availability.toLowerCase();
                //sms[2] = "I found this.Next train's" + System.lineSeparator() + "Arrival: " + sc.arrival + System.lineSeparator() + "Depature: " + sc.departure + System.lineSeparator() + "Availability: " + sc.availability + System.lineSeparator() + "Type: " + sc.Type + System.lineSeparator() + "Ticket price: 1st class " + price[0] + " 2nd class " + price[1] + " 3rd class " + price[2];

                //System.out.println(sms[1]);
                response = sms[1];
            } else {
                response = " Sorry I couldn't find any trains !!";
                //System.out.println(response);
            }
            return response;
        } else {
            return "Input error";
        }
    }

    private String generateURL(String from, String to) {
        String URL = "http://slr.malindaprasad.com/index.php?from=" + from + "&to=" + to;
        return URL;
    }

    private static String readDB(String s , Connection con) {

        try {

            Connection conn = con;
            Statement st = conn.createStatement();
            String search = "SELECT * FROM stations where name like '" + s + "'";
            ResultSet res = st.executeQuery(search);
            //  System.out.println(res.);
            if (res.next()) {
                String name = res.getString("name");
                String code = res.getString("code");
                //System.out.println(name + "\t" + code);
                return code;
            } else {
                //System.out.println("No such place");
                search = "SELECT * FROM stations where name like '" + placePredict(s) + "'";
                res = st.executeQuery(search);
            //  System.out.println(res.);
                if (res.next()) {
                String name = res.getString("name");
                String code = res.getString("code");
                //System.out.println(name + "\t" + code);
                return code;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static String placePredict(String name){
        String out = null;
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MegaMind","udaras","ud@r@s");
        } catch (Exception ex) {
            Logger.getLogger(TrainTimetable.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(connection != null){
           try {
               Statement stm = connection.createStatement();
               String query = "SELECT name FROM stations where name like \'"+name+"\'";
               ResultSet rs = stm.executeQuery(query);
               if(rs.next()){
                   out = rs.getString(1);
               }else{
                   query = "SELECT name FROM stations where name like \'"+name.substring(0,1).toUpperCase()+"%\'";
                   
                   ArrayList<String> pos = new ArrayList<String>();
                   rs = stm.executeQuery(query);
                   while(rs.next()){
                       pos.add(rs.getString(1));  
                   }
                   int maxIndex=0,factorial = 0;
                   double t = 0,ta = 0,max = 0;
                   for(int i=0;i<pos.size();i++){
                       t = 0;
                       factorial = 0;
                       
                        for(int k=0;k<name.length();k++){
                               for(int kk=k+1;kk<name.length();kk++){
                                    String temp=pos.get(i);
                                    if(temp.contains(name.subSequence(k,kk))){
                                        t += 1;
                                    }
                                    factorial++;
                               }
                       }
                        ta = t/factorial;
                       if(ta > max){
                           max = ta;
                           maxIndex = i;
                       }
                   }
                //   guess.put(pos.get(maxIndex), max);
                   System.out.println(name+" ->"+pos.get(maxIndex)+" -> "+max);
                   return pos.get(maxIndex);
               }
               
           } catch (Exception ex) {
               Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
           }    
       }
        return out;
    }
}
class TrainSchedule {

    String arrival;
    String departure;
    String reaching;
    String starting;
    String runsTo;
    String availability;
    String Type;
    String trainNo;

    public TrainSchedule(String arrival, String departure, String reaching, String starting, String runsTo, String availability, String Type, String trainNo) {
        this.arrival = arrival;
        this.departure = departure;
        this.reaching = reaching;
        this.starting = starting;
        this.runsTo = runsTo;
        this.availability = availability;
        this.Type = Type;
        this.trainNo = trainNo;
    }

    public void show() {
        System.out.println("Arrival: " + arrival);
        System.out.println("Depature: " + departure);
        System.out.println("Reach: " + reaching);
        //System.out.println("Starting: "+starting);
        //System.out.println("Runs to: "+runsTo);
        System.out.println("Availability: " + availability);
        System.out.println("Type: " + Type);
        //System.out.println("Train No: "+trainNo);
        System.out.println("");
    }
}
