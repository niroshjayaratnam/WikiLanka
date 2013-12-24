/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeSerializer;

import ResponseHandler.Geometry;
import ResponseHandler.Location;
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
public class GeometryDeserializer implements JsonDeserializer<Geometry>{

    @Override
    public Geometry deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        
        Gson gson = new Gson();
        Geometry geometry = new Geometry();
        JsonObject jsonObject = je.getAsJsonObject();
        
        Location location = gson.fromJson(je, Location.class);
        geometry.setLocation(location);
        
        
        
        return geometry;
    }
    
}
