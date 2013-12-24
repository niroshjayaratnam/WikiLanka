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
public class AttractionComparator implements Comparator<Attraction> {

    @Override
    public int compare(Attraction o1, Attraction o2) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if(o1.getDistance() > o2.getDistance()){
            return 1;
        }else if(o1.getDistance() < o2.getDistance()){
            return -1;
        }else{
            return 0;
        }
    }
    
}
