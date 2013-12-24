/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.util.HashMap;
import org.eclipse.jetty.util.ajax.JSONObjectConvertor;
//import org.json.JSONObject;

/**
 *
 * @author Udara
 */
public class AI {
    String sucess;
    String errorType;
    String errorMessage;
 //   JsonElement message;
    Gson gson = new Gson();
    HashMap<String, String> message;
   /* static class checking{
        String chatBotName;
        String chatBotID;
        String message;
        String emotion; 
    }
    String getMessage(){
        try{
            checking M = gson.fromJson(message, checking.class);
            return M.message;
        }catch(JsonParseException e){
            return "Error";
        }
    }*/
    
}
