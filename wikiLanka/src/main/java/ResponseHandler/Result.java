/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseHandler;

import java.util.List;

/**
 *
 * @author Bhash
 */
public class Result {
    
    private String formatted_address;
    private Geometry geometry;
    private List<Event> events;
    private String icon;
    private String id;
    private String name;
    private double rating;
    private String vicinity;
    private String refference;
    private List<String> types;
    
    
    

   

    /**
     * @return the formatted_address
     */
    public String getFormatted_address() {
        return formatted_address ;
    }

    /**
     * @param formatted_address the formatted_address to set
     */
    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
    /**
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
 
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * @return the refference
     */
    public String getRefference() {
        return refference;
    }

    /**
     * @param refference the refference to set
     */
    public void setRefference(String refference) {
        this.refference = refference;
    }

    public List<Event> getEvents() {
        return events;
    }

    /**
     * @return the vicinity
     */
    public String getVicinity() {
        return vicinity;
    }

    /**
     * @param vicinity the vicinity to set
     */
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
    
   

   

   
    
    


    
    
    
    
    
}
