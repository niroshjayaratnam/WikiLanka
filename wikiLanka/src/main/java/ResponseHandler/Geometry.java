/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseHandler;

/**
 *
 * @author Bhash
 */
public class Geometry {
    
    private Location location;  
    
    
    
    @Override
    public String toString() {
        return getLocation().getLat() +":" + getLocation().getLng() ;
    }

    /**
     * @return the location
     */
    public Location  getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    
    
}
