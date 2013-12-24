/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Udara
 */
public class Path_Finder {
   Connection connection = null;
   Statement stm = null;
   Random random = new Random();
   String from = "";
   String to = "";
    HashMap<String, Double> guess = new HashMap<String,Double>();
   private int ifrom,ito;
   private int[] chovers = {27, 5, 7, 31, 28, 29, 2, 32, 37, 10, 41, 35, 34, 100, 90, 101, 53, 252, 45, 282, 284, 231, 235, 207, 204, 281}; // change over points
   Path_Finder(String method , Connection con){           
       
       if(method.equals("bus")){
           try{
               connection = con;
               stm = connection.createStatement();
               getPlaceID();
           }catch(Exception e){
               e.printStackTrace();
           }
           if(connection != null){
               System.out.println("connected to the database successfully");
           }
       }
   }

    Path_Finder(String from,String to,String method , Connection con) {
        
        getPlaceID();
        
        if(method.equals("bus")){
           try{
               connection = con;
               stm = connection.createStatement();
               //getPlaceID();
           }catch(Exception e){
               e.printStackTrace();
           }
           if(connection != null){
               System.out.println("connected to the database successfully");
               this.from = placePredict(from);//(from.substring(0,1).toUpperCase() + from.substring(1)).trim();
               this.to = placePredict(to);//(to.substring(0,1).toUpperCase() + to.substring(1)).trim();
               getPlaceID();
           }
       }
        
    }
   
    String placePredict(String name){
        String out = null;
        if(connection != null){
           try {
               
               String query = "SELECT name FROM place where name like \'"+name.substring(0,1).toUpperCase()+name.substring(1)+"\'";
               ResultSet rs = stm.executeQuery(query);
               if(rs.next()){
                   out = rs.getString(1);
               }else{
                   query = "SELECT name FROM place where name like \'"+name.substring(0,1).toUpperCase()+"%\'";
                   
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
                   guess.put(pos.get(maxIndex), max);
                   System.out.println(name+" ->"+pos.get(maxIndex)+" -> "+max);
                   return pos.get(maxIndex);
               }
               rs.close();
           } catch (SQLException ex) {
               Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
           }    
       }
        return out;
    }
   int getFromId(){
       return ifrom;
   }
   int getToId(){
       return ito;
   }
   void setDestination(String dest){
       to = (dest.substring(0,1).toUpperCase() + dest.substring(1)).trim();
   }
   void setDeparture(String dep){
       from = (dep.substring(0,1).toUpperCase() + dep.substring(1)).trim();
   }
   void setLocation(String dep,String dest){
       to = (dest.substring(0,1).toUpperCase() + dest.substring(1)).trim();
       from = (dep.substring(0,1).toUpperCase() + dep.substring(1)).trim();
   }
   void killStatement(){
        try{ stm.close(); }catch(Exception e){};
    }
   void getPlaceID(){
       if(connection != null){
           try {
               
               String query = "SELECT pid FROM place where name=\'"+from+"\'";
               ResultSet rs = stm.executeQuery(query);
               if(rs.next()){
                   ifrom = rs.getInt("pid");
               }
               query = "SELECT pid FROM place where name=\'"+to+"\'";
               rs = stm.executeQuery(query);
               if(rs.next()){
                   ito = rs.getInt("pid");
               }
               rs.close();
           } catch (SQLException ex) {
               Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
   }
   String getPlaceName(int pid){
       String out = null;
        try {
               
               String query = "SELECT name FROM place where pid="+pid;
               ResultSet rs = stm.executeQuery(query);
               if(rs.next()){
                   out = rs.getString(1);
               }
               rs.close();
           } catch (SQLException ex) {
               Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
           }
        return out;
   }
   String getBusRoute(int busid){
       ResultSet rs;
       try {
            String query = "SELECT * from bus WHERE busid="+busid;
            rs = stm.executeQuery(query);
            String temp;
            if(rs.next()){
                temp = rs.getString(2);
            //    System.out.println("Route no is "+temp);
                return temp;
            }
            
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
            
        }
       return null;
   }
   String getBusFrom(int busid){
       ResultSet rs;
       try {
            String query = "SELECT * from bus WHERE busid="+busid;
            rs = stm.executeQuery(query);
            String temp;
            if(rs.next()){
                temp = rs.getString(3);
        //        System.out.println("From "+temp);
                return temp;
            }
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
   }
   String getBusTo(int busid){
       ResultSet rs;
       try {
            String query = "SELECT * from bus WHERE busid="+busid;
            rs = stm.executeQuery(query);
            String temp;
            if(rs.next()){
                temp = rs.getString(4);
      //          System.out.println("To "+temp);
                return temp;
            }
            
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
   }
   
   String showPath(){
       String out = "";
       if(!guess.isEmpty()){
           if(guess.size()>1){
               Iterator<String> temp = guess.keySet().iterator();
               String f = temp.next(),t = temp.next();
               out += "I guess u mean from "+f+"("+(int)Math.ceil(guess.get(f)*100)+"%)"+" to "+t+"("+(int)Math.ceil(guess.get(t)*100)+"%)"+".";
           }else{
                String t = guess.keySet().iterator().next();
                out += "I guess u mean "+t+"("+(int)Math.ceil(guess.get(t)*100)+"%).";
           }
       }
       String one = level1(ifrom, ito); 
       if(one != null)  return (out+one);
       String[] two = level2(ifrom,ito);
       String[] three = level3(ifrom,ito);
       if(Double.parseDouble(two[0]) <= Double.parseDouble(three[0])){
           return (out+two[1]);
       }else{
           return (out+three[1]);
       }
   }
   
   
   ResultSet findLink(int ito,int ifrom){
       ResultSet rs;
   //   System.out.println(ifrom+"  "+from);
   //    System.out.println(ito+"   " +to);
        try {
            String query = "SELECT * FROM stop AS s1 WHERE s1.pid="+ito+" AND s1.bid IN ( SELECT s2.bid FROM stop AS s2 WHERE s2.pid="+ifrom+" AND s2.stopNo < s1.stopNo )";
            rs = stm.executeQuery(query);
         //   int temp = 0;
            if(rs.next()){
        //        temp = rs.getInt("bid");
         //       System.out.println("the place id is "+temp);
                return rs;
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return null;
   }
   String level1(int ifrom,int ito){
        ResultSet rs = findLink(ito,ifrom);
        if(rs != null){
            System.out.println("Match found!");
            String[] answers = {"You can use ","Please take "};
            int busid = 0;
           try {
               busid = rs.getInt(1);
               rs.close();
           } catch (SQLException ex) {
               Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
           }
           
            String ans = answers[random.nextInt(answers.length)]+getBusRoute(busid)+" "+getBusFrom(busid)+"-"+getBusTo(busid);
            
            return ans ;
        }else{
            System.out.println("No direct method");
        }
            
        return null;
   }
   String[] level2(int ifrom,int ito){
       int min_halts = 99999;
       int nstops, nstops2,halt2,halt1,halt3,halt4;
       int bid1 = 0,bid2 = 0,change = 0,mstop1,mstop2,t1,t2;
       double min_dis = 100000000;
       String name1, name2, name3;
       ResultSet busid1 = null,busid2 = null;
       for(int value : chovers){
           busid1 = findLink(ifrom,value);
           busid2 = findLink(value,ito);
           if(busid1 != null){
               if(busid2 != null){
                   
                   try {
                       t1 = findLink(ifrom,value).getInt(1);
                       halt2 = findLink(ifrom,value).getInt(3);
                       halt1 = haltNo(t1, ifrom);
                       nstops = Math.abs(halt2);

                       
                       t2 = findLink(value, ito).getInt(1);
                       halt4 = findLink(value, ito).getInt(3);
                       halt3 = haltNo(t2,value);
                       nstops2 = Math.abs(halt4);
                       
                       double fr[] = getCoordinates(ifrom);
                       double ch[] = getCoordinates(value);
                       double to[] = getCoordinates(ito);
                       
                       double distance = Math.sqrt(Math.pow(fr[0]-ch[0], 2)+Math.pow(fr[1]-ch[1],2))+Math.sqrt(Math.pow(ch[0]-to[0], 2)+Math.pow(ch[1]-to[1],2));
                       if(distance < min_dis/*(nstops+nstops2) < min_halts*/){
                           bid1 = t1;
                           bid2 = t2;
                           change = value;
                           mstop1 = nstops;
                           mstop2 = nstops2;
                           min_dis = distance;
                           min_halts = nstops+nstops2;//System.out.println(t1+" "+t2+ "  "+value);
                       }
                   } catch (SQLException ex ) {
                       Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   
               }
           }
       }
       String ans = null;
       if(min_halts != 99999){
     //      System.out.println(min_dis);
           String[] answers = {"You can use ","Please take "};
           ans = answers[random.nextInt(answers.length)]
                  +getBusRoute(bid1)+" to go to "+getPlaceName(change)
                   +" and take the bus "+getBusRoute(bid2)+" then you can get to "+getPlaceName(ito);
       }
       String[] out = {Double.toString(min_dis),ans};
       try{busid1.close(); busid2.close();}catch(Exception e){};
       return out;
   }
   String[] level3(int ifrom,int ito){
       int min_halts = 99999;
       int nstops, nstops2,halt2,halt1,halt3,halt4;
       int bid1 = 0,bid2 = 0,bid3 = 0,change = 0,change2 = 0,mstop1,mstop2,t1,t2,t3;
       double min_dis = 100000000;
       String name1, name2, name3;
       ResultSet busid1 = null,busid2 = null;
       for(int value2 : chovers){ 
           if(findLink(ifrom,value2) != null){
            try{
                t3 = findLink(ifrom,value2).getInt(1);
            }catch(SQLException ex){
                continue;
            }
            for(int value : chovers){
                if(value2 == value) continue;
                busid1 = findLink(value2,value);
                busid2 = findLink(value,ito);
                if(busid1 != null){
                    if(busid2 != null){

                        try {
                            t1 = findLink(value2,value).getInt(1);
                            halt2 = findLink(value2,value).getInt(3);
                            halt1 = haltNo(t1, value2);
                            nstops = Math.abs(halt2);


                            t2 = findLink(value, ito).getInt(1);
                            halt4 = findLink(value, ito).getInt(3);
                            halt3 = haltNo(t2,value);
                            nstops2 = Math.abs(halt4);

                            double fr[] = getCoordinates(ifrom);
                            double ch2[]= getCoordinates(value2);
                            double ch[] = getCoordinates(value);
                            double to[] = getCoordinates(ito);

                            double distance = Math.sqrt(Math.pow(fr[0]-ch2[0], 2)+Math.pow(fr[1]-ch2[1],2))+
                                    Math.sqrt(Math.pow(ch2[0]-ch[0], 2)+Math.pow(ch2[1]-ch[1],2))+
                                         Math.sqrt(Math.pow(ch[0]-to[0],2)+Math.pow(ch[1]-to[1],2));
                            if(distance < min_dis/*(nstops+nstops2) < min_halts*/){
                                bid1 = t1;
                                bid2 = t2;
                                bid3 = t3;
                                change = value;
                                change2 = value2;
                                mstop1 = nstops;
                                mstop2 = nstops2;
                                min_dis = distance;
                                min_halts = nstops+nstops2;//System.out.println(t1+" "+t2+ "  "+value);
                            }
                        } catch (SQLException ex ) {
                            Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                    }
                }
           }
       }
       String ans = null;
       if(min_halts < 99999){
       //    System.out.println(min_dis);
           String[] answers = {"You can use ","Please take "};
           ans = answers[random.nextInt(answers.length)]+"  "+getBusRoute(bid3)+" and go to "+getPlaceName(change2) 
                   +" ant take the bus "+getBusRoute(bid1)+" to go to "+getPlaceName(change)
                   +" and take the bus "+getBusRoute(bid2)+" then you can get to "+getPlaceName(ito);
       }
       String[] out = {Double.toString(min_dis),ans};
       try{ busid1.close(); busid2.close(); }catch(Exception e){};
       return out;
   }
   int haltNo(int bid,int pid){
        int halt = -1;
        try {
            ResultSet rs;
           
             String sql_query = "SELECT * FROM stop AS s WHERE s.pid="+pid+" AND s.bid="+bid;
             rs = stm.executeQuery(sql_query);
             if(rs.next()){
                 halt = rs.getInt(3);
             }
     /*	if(($halt = mysql_query ($sql_query, $link)) != false && (mysql_num_rows($halt)) > 0)
             {
                     $haltdet = mysql_fetch_array($halt);
                     return $haltdet[2];
             }
             else
             {
                     return false;
             }*/
             rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
        }
      //  System.out.println("------->"+halt);
        return halt;
    }
   double[] getCoordinates(int pid){
       double[] cd = new double[2];
       String q = "SELECT loc FROM place WHERE pid="+pid;
       
        try {
            ResultSet rs = stm.executeQuery(q);
            while(rs.next()){
                String co = rs.getString(1);
             //   System.out.println(co);
                Scanner sc = new Scanner(co).useDelimiter(",|\\|");
                cd[0] = Double.parseDouble(sc.next());
                cd[1] = Double.parseDouble(sc.next());
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       return cd;
   }
}
