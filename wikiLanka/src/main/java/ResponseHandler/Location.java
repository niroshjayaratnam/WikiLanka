/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseHandler;

/**
 *
 * @author Bhash
 */
public class Location {
    
    private double lat;
    private double lng;

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }
    
    
    
}
