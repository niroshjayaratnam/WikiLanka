/*
 *   (C) Copyright 1996-2012 hSenid Software International (Pvt) Limited.
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
package hms.sdp.samples.ussd;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.logging.Logger;

public class MainApp {

    private final static Logger LOGGER = Logger.getLogger(MainApp.class.getName());

    public static void main(String[] args) throws Exception {
        Server server = new Server(5555);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("../lib/sdp-ussd-company.war");
        server.setHandler(webapp);
        LOGGER.info("Application starting ...");
        server.start();
        server.join();
    }
}
