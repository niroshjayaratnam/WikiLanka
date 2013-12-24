/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import hms.kite.samples.api.sms.messages.MoSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsReq;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Udara
 */
public class Memory_Module {
    Connection connection;
    Statement stm;
    private String id="unknown",name;
    private boolean auth = false;
    private String msg;
    Memory_Module(MoSmsReq sms , Connection con) {
        this.id = sms.getSourceAddress();
        
        try {
            connection = con;
            stm = connection.createStatement();
            if(connection != null){
                System.out.println("connected to the database successfully");
                String query = "SELECT name FROM tourists WHERE id=\'"+this.id+"\'";
                ResultSet rs = stm.executeQuery(query);
                if(rs.next()){
                    name = rs.getString(1);
                    auth = true;
                }else{
                    auth = regUser(sms);
                }
                rs.close();
                stm.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(Memory_Module.class.getName()).log(Level.SEVERE, null, ex);
        }
        auth = true;
    }
    String getID(){
        return id;
    }
    String getName(){
        return name;
    }
    Connection getConnection(){
        return connection;
    }
    boolean authorized(){
        return true;
    }
    boolean regUser(MoSmsReq msg){
        if(connection != null){
            if(name == null){
                String id = msg.getSourceAddress();
                try{
                    String name = "anonymus";
                    this.name = name;
                    String querry = "INSERT INTO tourists (id,name) VALUES (\'"+id+"\',\'"+name+"\')";
                    stm.executeUpdate(querry);
                    return true;
                }catch(Exception e){
                    return false;
                }
            }
        }
        return false;
    }
    String refineMsg(String content){
        Scanner sc = new Scanner(content);
        ArrayList<String> out = new ArrayList<String>();
        if(connection != null){
            String refine;
            try{
                String querry;
                while(sc.hasNext()){
                    refine = sc.next();
                    querry = "SELECT word FROM sms where term like \'"+refine+"\'";
                    ResultSet rs = stm.executeQuery(querry);
                    if(rs.next()){
                        out.add(rs.getString(1));
                    }else{
                        out.add(refine);
                    }
                }
            }catch(Exception e){
                return content;
            }
            refine = "";
            for(int i=0;i<out.size();i++){
                refine += out.get(i)+" ";
            }
            return refine.trim();
        }
        return content;
    }
    String updateName(String name){
        Scanner sc = new Scanner(name);
        if(connection != null){
                try{
                    String nn = sc.next();
                    String querry = "UPDATE tourists SET name = \'"+nn.toLowerCase()+"\' where id = \'"+id+"\'";
                    stm.executeUpdate(querry);
                    String[] answers = {"Hi, "+nn+". It's good to know your name. I'm Mega Mind",
                        "My name is Mega Mind. I'll remember your name, "+nn,
                        "I'm Mega Mind. It's nice to know you, "+nn};
                    Random rnd = new Random();
                    return answers[rnd.nextInt(answers.length)];
                }catch(Exception e){
                    
                }
        }
        String[] answers = {"I'm sorry I don't get your name.",
                        "I don't think I'll remember your name.",
                        "Sorry friend I can't remember your name now." };
                    Random rnd = new Random(answers.length);
                    return answers[rnd.nextInt()];
    }
}
