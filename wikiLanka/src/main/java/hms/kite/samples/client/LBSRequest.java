/*
 *   (C) Copyright 1996-${year} hSenid Software International (Pvt) Limited.
 *   All Rights Reserved.
 *
 *   These materials are unpublished, proprietary, confidential source code of
 *   hSenid Software International (Pvt) Limited and constitute a TRADE SECRET
 *   of hSenid Software International (Pvt) Limited.
 *
 *   hSenid Software International (Pvt) Limited retains all title to and intellectual
 *   property rights in these materials.
 *
 */
package hms.kite.samples.client;

import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.lbs.LbsRequestSender;
import hms.kite.samples.api.lbs.LbsRequestMessage;
import java.awt.Point;

import java.util.Properties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LBSRequest {

    private static final Logger LOGGER = Logger.getLogger(LBSRequest.class.getName());
    
    private Properties properties = new Properties();
    private final static String PROPERTY_NAME = "lbs-sample.properties";

    public double[] sendRequest(String msisdn) {
    	double[] point = new double[2];
    	
        try {
            final LbsRequestSender requestSender = new LbsRequestSender(new URL(getPropertyValue("url")));

            final LbsRequestMessage lbsRequestMessage = new LbsRequestMessage();
            lbsRequestMessage.setApplicationId("APP_003783");
            lbsRequestMessage.setPassword("38ed2499466f579b169e728c86f7e315");
            lbsRequestMessage.setSubscriberId(msisdn);
            lbsRequestMessage.setServiceType(LbsRequestMessage.ServiceType.IMMEDIATE);
            lbsRequestMessage.setResponseTime(LbsRequestMessage.ResponseTime.NO_DELAY);
            lbsRequestMessage.setFreshness(LbsRequestMessage.Freshness.HIGH);
            lbsRequestMessage.setHorizontalAccuracy("1000");
            
            try {
                point = captureResponse(requestSender, lbsRequestMessage);
            } catch(Exception e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e){
            LOGGER.log(Level.ALL, e.getMessage());
        }
        finally {
        	return point;
        }
    }

    private double[] captureResponse(LbsRequestSender requestSender, LbsRequestMessage lbsRequestMessage) {
    	double[] location = new double[2];
    	
        try{
            String response = requestSender.sendLbsRequest(lbsRequestMessage).toString();

            int first = response.indexOf("latitude='") + 10;
            String value = response.substring(first, response.indexOf("'", first + 1));
            System.out.println(value);
            location[0] = Double.parseDouble(value);
            
            first = response.indexOf("longitude='") + 11;
            value = response.substring(first, response.indexOf("'", first + 1));
            System.out.println(value);
            location[1] = Double.parseDouble(value);
            
            return location;
            
        } catch (SdpException e) {
            e.printStackTrace();
        } catch(NumberFormatException nfe) {
        	nfe.printStackTrace();
        }
        finally {
        	return location;
        }
    }

    private String getPropertyValue(String key) {
        return getText(key);
    }
    
    private void loadProperties(){
        String path = System.getProperty("user.dir");
        String[] workingDir = path.split("stand-alone");

        try {
            InputStream in = new FileInputStream("lbs-sample.properties");
            properties.load(in);
            in.close();
        } catch (Exception e) {
            LOGGER.info(PROPERTY_NAME + " unable to load.");
            throw new IllegalStateException(e.toString());
        }
    }

    public String getText(String key){
        if(properties == null || properties.isEmpty())
        	loadProperties();
        
        final String value = properties.getProperty(key);
        if(value == null || value.isEmpty())
            return null;
        else
            return value;
    }
}
