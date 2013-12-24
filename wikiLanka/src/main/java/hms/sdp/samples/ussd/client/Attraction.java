/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.sdp.samples.ussd.client;

import java.util.Comparator;

/**
 *
 * @author Udara
 */
public class Attraction implements Comparable<Attraction> {
    private int id;
    private String name;
    private double longitude;
    private double latitude;
    private double distance;
    private String type;

    public Attraction(int id, String name, double latitude, double longitude, double distance, String type) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
        this.type = type;
    }
    
    int getID(){ return id; }
    String getName(){ return name;  }
    Double getLongitude(){ return longitude;  }
    Double getLatitude(){ return latitude;  }
    Double getDistance(){ return distance;  }
    String getType(){ return type;  }
    
    void setDistance(double d){ distance = d;   }

    @Override
    public int compareTo(Attraction o) {
       // throw new UnsupportedOperationException("Not supported yet.");
       if(this.getDistance() > o.getDistance())   return 1;
       else if(this.getDistance() < o.getDistance())  return -1;
       else return 0;
    }
}