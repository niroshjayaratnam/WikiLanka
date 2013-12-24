/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeSerializer;

import ResponseHandler.Geometry;
import ResponseHandler.Result;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;


/**
 *
 * @author Bhash
 */
public class ResultSerializer implements JsonDeserializer<Result>{
    
    

    @Override
    public Result deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            Result rst = new Result();
            
            Gson gson = new Gson();
            JsonObject jsonObject = je.getAsJsonObject();
            rst.setFormatted_address(jsonObject.get("formatted_address").getAsString());            
            
            Geometry geo   = gson.fromJson(je,Geometry.class ); //not sure
            rst.setGeometry(geo);            
            
            rst.setIcon(jsonObject.get("icon").getAsString());
            rst.setId(jsonObject.get("id").getAsString());
            rst.setName(jsonObject.get("name").getAsString());
            rst.setRating(jsonObject.get("rating").getAsDouble());
            rst.setRefference(jsonObject.get("refference").getAsString());
            rst.setVicinity(jsonObject.get("vicinity").getAsString());
            
            /*Types tp = gson.fromJson(je, Types.class);
            rst.setTypes(tp);*/
            
         
            return rst;
    
    }
    
}
