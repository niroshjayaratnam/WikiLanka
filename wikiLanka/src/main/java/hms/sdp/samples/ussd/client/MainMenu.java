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
 *   @auther emil
 */
package hms.sdp.samples.ussd.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.StatusCodes;
import hms.kite.samples.api.sms.messages.MoSmsReq;
import hms.kite.samples.api.ussd.MoUssdListener;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdResp;
import hms.kite.samples.client.GeocodeResult;
import hms.kite.samples.client.GeocoderResult;
import hms.kite.samples.client.LBSRequest;
import hms.kite.samples.client.SimpleClient;
import hms.kite.samples.client.Wiki;
import hms.sdp.samples.ussd.utils.PropertyLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import placeapi.PlaceApi;
        
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class MainMenu implements MoUssdListener {

    private final static Logger LOGGER = Logger.getLogger(MainMenu.class
            .getName());

    //hardcoded values
    private static final String EXIT_SERVICE_CODE = "000";
    private static final String BACK_SERVICE_CODE = "999";
    private static final String INIT_SERVICE_CODE = "779";
//    private static final String REQUEST_SENDER_SERVICE = "http://localhost:7000/ussd/send";
    private static final String REQUEST_SENDER_SERVICE = "http://api.dialog.lk:8080/ussd/send";
    private static final String PROPERTY_KEY_PREFIX = "menu.level.";
    private static final String USSD_OPERATION_MT_CONT="mt-cont";
    private static final String USSD_OPERATION_MT_FIN="mt-fin";
//social plugin
    private static final String PROTECTED_TWITTER_URL = "https://api.twitter.com/1.1/";
    //menu state saving for back button
    private List<Byte> menuState = new ArrayList<Byte>();
    //service to send the request
    private UssdRequestSender ussdMtSender;
    //JDBC pool
    private DataSource datasource;
    //Hashmap of OAuth request tokens
    private ConcurrentHashMap<String,String> tokenRequests = new ConcurrentHashMap<String, String> ();

    @Override
    public void init() {
        //create and initialize service
        try {
            ussdMtSender = new UssdRequestSender(new URL(REQUEST_SENDER_SERVICE));
        } catch (MalformedURLException e) {
            LOGGER.log(Level.INFO, "Unexpected error occurred", e);
        }
        //this code is added by me to init the jdbc pool
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3306/MegaMind");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("udaras");
        p.setPassword("ud@r@s");
        p.setJmxEnabled(true);
          p.setTestWhileIdle(false);
          p.setTestOnBorrow(true);
          p.setValidationQuery("SELECT 1");
          p.setTestOnReturn(false);
          p.setValidationInterval(30000);
          p.setTimeBetweenEvictionRunsMillis(30000);
          p.setMaxActive(100);
          p.setInitialSize(10);
          p.setMaxWait(10000);
          p.setRemoveAbandonedTimeout(60);
          p.setMinEvictableIdleTimeMillis(30000);
          p.setMinIdle(10);
          p.setLogAbandoned(true);
          p.setRemoveAbandoned(true);
          p.setJdbcInterceptors(
            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
          datasource = new DataSource();
          datasource.setPoolProperties(p);
    }

    /**
     * Receive requests
     * @param moUssdReq
     */
    @Override
    public void onReceivedUssd(MoUssdReq moUssdReq) {
        LBSRequest lbs = new LBSRequest();
        double loc[] = lbs.sendRequest(moUssdReq.getSourceAddress());
        Connection con = null;
        LOGGER.info("Message recieved from "+moUssdReq.getSourceAddress());
        try{
            con = datasource.getConnection();
            Statement st = con.createStatement();
            String first = "SELECT name from tourists where id =\'"+moUssdReq.getSourceAddress()+"\'";
            ResultSet rs = st.executeQuery(first);
            if(!rs.next()){
                st.executeUpdate("INSERT INTO tourists(id,name) VALUES(\'"+moUssdReq.getSourceAddress()
                        +"\',\'anonymous\')");
            }
            String querry = "UPDATE tourists SET location=\'"+loc[0]+","+loc[1]+"\' where id=\'"+moUssdReq.getSourceAddress()+"\'";
            st.executeUpdate(querry);
            st.close();
        }catch(SQLException e){
            LOGGER.info("Couldn't update the user location.");
        }finally{
            if(con != null) try{con.close();}catch(Exception e){}
        }
        try {
            //start processing request
            processRequest(moUssdReq);
        } catch (SdpException e) {
            LOGGER.log(Level.INFO, "Unexpected error occurred", e);
        }
    }
    

    /**
     * Build the response based on the requested service code
     * @param moUssdReq
     */
    private void processRequest(MoUssdReq moUssdReq) throws SdpException {

        //exit request - session destroy
        if(moUssdReq.getMessage().equals(EXIT_SERVICE_CODE)){
            terminateSession(moUssdReq);
            return;//completed work and return
        }

        //back button handling
        if (moUssdReq.getMessage().equals(BACK_SERVICE_CODE)) {
            backButtonHandle(moUssdReq);
            return;//completed work and return
        }
        
        //text request. can be a question or a tweet
        if(moUssdReq.getMessage().contains("[a-zA-Z]")){
            String msg = moUssdReq.getMessage();
        }

        //get current service code
        byte serviceCode;
        if (moUssdReq.getMessage().equals(INIT_SERVICE_CODE)) {
            serviceCode=0;
            clearMenuState();
        }else{
            serviceCode=getServiceCode(moUssdReq);
        }
        //create request to display user
        final MtUssdReq request = createRequest(moUssdReq, buildNextMenuContent(serviceCode , moUssdReq),USSD_OPERATION_MT_CONT);
        sendRequest(request);
        //record menu state
        menuState.add(serviceCode);
    }

    /**
     * Build request object
     * @param moUssdReq     - Receive request object
     * @param menuContent   - menu to display next
     * @param ussdOperation - operation
     * @return MtUssdReq    - filled request object
     */
    private MtUssdReq createRequest(MoUssdReq moUssdReq, String menuContent, String ussdOperation) {
        final MtUssdReq request = new MtUssdReq();
        request.setApplicationId(moUssdReq.getApplicationId());
        request.setEncoding(moUssdReq.getEncoding());
        request.setMessage(menuContent);
        request.setPassword("38ed2499466f579b169e728c86f7e315");
        request.setSessionId(moUssdReq.getSessionId());
        request.setUssdOperation(ussdOperation);
        request.setVersion(moUssdReq.getVersion());
        request.setDestinationAddress(moUssdReq.getSourceAddress());
        return request;
    }

    /**
     * load a property from ussdmenu.properties
     * @param key
     * @return value
     */
    private String getText(byte key , MoUssdReq moUssdReq) {
        String out = PropertyLoader.getInstance().getText(PROPERTY_KEY_PREFIX + key);
        int len=0;
        System.out.println(out.length());
        while(len < out.length()-4){
            if(out.charAt(len) == '>' && out.charAt(len+1) == '>' && out.charAt(len+2) == '>'){
                String var = out.substring(len+3,len+8);
                if(var.equals("near1")){
                    String places[] = getNearAttraction(moUssdReq);
                    out = out.replace(">>>near1",places[0]);
                    out = out.replace(">>>near2",places[1]);
                    out = out.replace(">>>near3",places[2]);
                    out = out.replace(">>>near4",places[3]);
                    out = out.replace(">>>near5",places[4]);
                }else if(var.equals("tweet")){
                    Connection con = null;
                    Statement stm;
                    ResultSet rs;
                    try {
                        con = datasource.getConnection();
                        stm = con.createStatement();
                        String querry = "SELECT twitterkey,twittersecret,screenName FROM tokens WHERE id=\'"+moUssdReq.getSourceAddress()+"\'";
                        rs = stm.executeQuery(querry);
                        if(rs.next()){
                            String screenName = rs.getString(3);
                            tokenRequests.put(moUssdReq.getSourceAddress(), rs.getString(1)+" "+rs.getString(2));
                            out = out.replace(">>>tweet", "Hi! "+screenName+"\nTweet what's going on at where you are. Reply with your tweet.");
                        }else{
                            out = out.replace(">>>tweet","\nYou haven't allowed access for CARAVAN in your twitter account\n1.)Request a PIN number from twitter\n2.)I already have a PIN\n999.)Back");
                        }
                        rs.close();
                        stm.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }finally{
                        if(con != null) try{con.close();}catch(Exception e){};
                    }
                }else if(var.equals("tauth")){
                    OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
                                    .apiKey("nqhFLkm4QnfPfQZrsWSuw")
                                    .apiSecret("ddZlQgMBuVYwEfbW34PeLF9uIibW4YF8brv96iwkc")
                                    .callback("oob")
                                    .build();
                            
                    Token requestToken = service.getRequestToken();
                    String longUrl = service.getAuthorizationUrl(requestToken);
                    String shortUrl = null;
                    String message = null;
                    //lets shorten this longUrl using google url shortner
                    HttpClient httpclient = HttpClientBuilder.create().build();
                    HttpPost post = new HttpPost("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyBh9YE-z9Rj6eeBrHhSVzSmgMGLb168ivo");
                    post.setHeader("Content-Type","application/json");
                    try {
                        post.setEntity(new StringEntity("{\"longUrl\":\""+longUrl+"\"}"));
                        HttpResponse response = httpclient.execute(post);
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        StringBuilder result = new StringBuilder();
                        String line = "";
                        while ((line = rd.readLine()) != null) {
                            result.append(line);
                        }
                        JsonParser parser = new JsonParser();
                        JsonObject json = (JsonObject) parser.parse(result.toString());
                        shortUrl = json.get("id").getAsString();
                    } catch (Exception ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    tokenRequests.put(moUssdReq.getSourceAddress(), requestToken.toString());
                if(shortUrl == null){
                    message =  "Sorry =( Error occured. Please try to ask for another Twitter request!";
                }else{
                    message = "CARAVAN has asked permissions from twitter to allow post your tweets. You'll have to allow access to CARAVAN twitter application for this. Please goto this URL and allow access to the application.\n "+shortUrl+" \n"
                      + "After you allow access twitter will give you a PIN number. Please Enter that PIN in USSD menu";
                }
                        MoSmsReq sms = new MoSmsReq();
                        sms.setApplicationId("APP_003783");
                        sms.setSourceAddress(moUssdReq.getSourceAddress());
                        sms.setMessage(message);
                        SimpleClient sc = new SimpleClient();
                        try{
                            sc.initialize(datasource.getConnection());
                            sc.sendReply(sms);
                        }catch(SQLException se){
                            out = "Please try again later";
                        }
                        out = out.replace(">>>tauth", "");
                       // out += service.getAuthorizationUrl(requestToken);
                       // tokenRequests.put(moUssdReq.getSourceAddress(),requestToken.toString());
                }else if(var.equals("tstat")){
                    Connection con = null;
                    Statement stm;
                    ResultSet rs;
                    try {
                        con = datasource.getConnection();
                        stm = con.createStatement();
                        String querry = "SELECT screenName FROM tokens WHERE id=\'"+moUssdReq.getSourceAddress()+"\'";
                        rs = stm.executeQuery(querry);
                        if(rs.next()){
                            out = out.replace(">>>tstat", rs.getString(1));
                        }
                        rs.close();
                        stm.close();
                    } catch (SQLException ex) {
                        out = out.replace(">>>tstat", "friend");
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }finally{
                        if(con != null) try{con.close();}catch(Exception e){};
                    }
                }else if(var.equals("place")){
                    PlaceApi pa = new PlaceApi();
                    LBSRequest lbs = new LBSRequest();
                    double[] point = lbs.sendRequest(moUssdReq.getSourceAddress());
                    String type;
                    int s = Integer.parseInt(moUssdReq.getMessage());
                    switch(s){
                        case 1:
                            type = "banks"; break;
                        case 2:
                            type = "cafes"; break;
                        case 3:
                            type = "grocery of supermarket"; break;
                        case 4:
                            type = "hospitals"; break;
                        case 5:
                            type = "police"; break;
                        case 6:
                            type = "restaurant"; break;
                        case 7:
                            type = "shopping_mall"; break;
                        default:
                            type = "";
                    }
                    String add[] = new String[5];
                    try {
                        add = pa.nearByFive(point[0], point[1], 10000, type);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String details = "";
                    for(s = 0;s<5 ; s++){
                        if(add[s] == null)  break;
                        else details += (s+1) +"). "+ add[s]+"\n";
                    }
                    if(s==0) details = "I found no results.. =(";
                    out = out.replace(">>>place",details );
                }else if(var.equals("mega2")){
                    SimpleClient simpleClient = new SimpleClient();
                    try {
                        simpleClient.initialize(datasource.getConnection());
                        MoSmsReq sms = new MoSmsReq();
                        sms.setApplicationId("APP_003783");
                        sms.setSourceAddress(moUssdReq.getSourceAddress());
                        Wiki wiki = new Wiki();
                        String message = wiki.getWiki(moUssdReq.getMessage());
                        if(message != null){
                            sms.setMessage(message);
                        }else{
                            sms.setMessage("Error Occured :( Please try again and search using only the search term.");
                        }
                        SimpleClient sc = new SimpleClient();
                        try{
                            sc.initialize(datasource.getConnection());
                            sc.sendReply(sms);
                            out = out.replace(">>>mega2", "You will shortly receive a SMS with the answer :)");
                        }catch(SQLException se){
                            out = out.replace(">>>mega2","Please try again later");
                        }         
                    } catch (SQLException ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(var.equals("geoco")){
                    try {
                        String content,inputLine;
                        LBSRequest loc = new LBSRequest();
                        double[] d = loc.sendRequest(moUssdReq.getSourceAddress());
                        URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+d[0]+","+d[1]+"&sensor=false");
                        URLConnection yd = url.openConnection();
                        BufferedReader b = new BufferedReader(new InputStreamReader(yd.getInputStream()));
                        content = "";
                        while((inputLine = b.readLine()) != null){
                            content += inputLine;
                        }
                        Gson gson = new Gson();
                        GeocodeResult geo = gson.fromJson(content, GeocodeResult.class);
                        
                        out = out.replace(">>>geoco",geo.getResults().get(0).getFormattedAddress() );
                    } catch (Exception ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            len++;
        }
        return out;
    }
    String[] getNearAttraction(MoUssdReq moUssedReq){
        Connection con = null;
 //       Comparator<Attraction> compa = (Comparator<Attraction>) new AttractionComparator();
        LBSRequest lbs= new LBSRequest();
        LinkedList<Attraction> att = new LinkedList<Attraction>();
        try{
            con = datasource.getConnection();
            Statement st = con.createStatement();
            System.out.println(moUssedReq.getSourceAddress());
            ResultSet re = st.executeQuery("SELECT location FROM tourists WHERE id=\'"+moUssedReq.getSourceAddress()+"\'");
            double lot=0,lat=0;
            while(re.next()){
                String location = re.getString(1);
                int i=0;
                while(location.charAt(i) != ',')    i++;
                lat = Double.parseDouble(location.substring(0,i));
                lot = Double.parseDouble(location.substring(i+1));
                System.out.println(lot+"----"+lat);
            }
            re.close();
            ResultSet rs;
            short cState = menuState.get(menuState.size()-1);
            if(cState == 22){
                short cc = 0;
                try{
                    cc = Short.parseShort(moUssedReq.getMessage());
                }catch(NumberFormatException e){
                    cc = 0;
                }
                if(cc == 1){
                    rs = st.executeQuery("SELECT * FROM attractions WHERE type=\'Beach Holidays\'");
                }else if(cc == 2){
                    rs = st.executeQuery("SELECT * FROM attractions WHERE type=\'Discover the past\'");
                }else if(cc == 3){
                    rs = st.executeQuery("SELECT * FROM attractions WHERE type=\'Pilgrimage\'");
                }else if(cc == 4){
                    rs = st.executeQuery("SELECT * FROM attractions WHERE type=\'Wild Safari\'");
                }else{
                    rs = st.executeQuery("SELECT * FROM attractions WHERE type=\'Nature Trails / Attractions / Activity\'");
                }      
            }else{
                rs = st.executeQuery("SELECT * FROM attractions");
            }
            while(rs.next()){
                Attraction a = new Attraction(Integer.parseInt(rs.getString(1))
                        , rs.getString(2), Double.parseDouble(rs.getString(3))
                        , Double.parseDouble(rs.getString(4)), 0, rs.getString(5));
                a.setDistance(Math.pow(((a.getLongitude()-lot)*(a.getLongitude()-lot)+(a.getLatitude()-lat)*(a.getLatitude()-lat)),0.5));
                att.add(a);
            }
            rs.close();
            st.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
          //  return "Error Occured!";
            //nothing to do here
        }finally{
            if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
        Collections.sort(att);
   /*     for(Attraction at : att){
            System.out.println(at.getName()+" -> "+at.getDistance());
        }*/
        String[] places = new String[5];
        places[0] = att.get(0).getName();
        places[1] = att.get(1).getName();
        places[2] = att.get(2).getName();
        places[3] = att.get(3).getName();
        places[4] = att.get(4).getName();
        return places;
    }
    /**
     * Request sender
     * @param request
     * @return MtUssdResp
     */
    private MtUssdResp sendRequest(MtUssdReq request) throws SdpException {
        //sending request to service
        MtUssdResp response = null;
        try {
            response = ussdMtSender.sendUssdRequest(request);
        } catch (SdpException e) {
            LOGGER.log(Level.INFO, "Unable to send request", e);
            throw e;
        }

        //response status
        String statusCode = response.getStatusCode();
        String statusDetails = response.getStatusDetail();
        if (StatusCodes.SuccessK.equals(statusCode)) {
            LOGGER.info("MT USSD message successfully sent");
        } else {
            LOGGER.info("MT USSD message sending failed with status code ["
                    + statusCode + "] " + statusDetails);
        }
        return response;
    }

    /**
     * Clear history list
     */
    private void clearMenuState() {
        LOGGER.info("clear history List");
        menuState.clear();
    }

    /**
     * Terminate session
     * @param moUssdReq
     * @throws SdpException
     */
    private void terminateSession(MoUssdReq moUssdReq) throws SdpException {
        final MtUssdReq request = createRequest(moUssdReq, "", USSD_OPERATION_MT_FIN);
        sendRequest(request);
    }

    /**
     * Handlling back button with menu state
     * @param moUssdReq
     * @throws SdpException
     */
    private void backButtonHandle(MoUssdReq moUssdReq) throws SdpException {
        byte lastMenuVisited = 0;

        //remove last menu when back
        if(menuState.size()>0){
            menuState.remove(menuState.size() - 1);
            lastMenuVisited = menuState.get(menuState.size() - 1);
        }

        //create request and send
        final MtUssdReq request = createRequest(moUssdReq, buildBackMenuContent(lastMenuVisited,moUssdReq), USSD_OPERATION_MT_CONT);
        sendRequest(request);

        //clear menu status
        if(lastMenuVisited==0){
            clearMenuState();
            //add 0 to menu state ,finally its in main menu
            menuState.add((byte)0);
        }

    }

    /**
     * Create service code to navigate through menu and for property loading
     * @param moUssdReq
     * @return serviceCode
     */
    private byte getServiceCode(MoUssdReq moUssdReq){
        byte serviceCode=0;
        try {
            serviceCode=Byte.parseByte(moUssdReq.getMessage());
        } catch (NumberFormatException e) {
           // System.out.println("we entered here : "+menuState.get(menuState.size()-1));
            if(menuState.get(menuState.size()-1) == (byte) 37){
                Verifier verifier = new Verifier(moUssdReq.getMessage());
                OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
                                .apiKey("nqhFLkm4QnfPfQZrsWSuw")
                                .apiSecret("ddZlQgMBuVYwEfbW34PeLF9uIibW4YF8brv96iwkc")
                                .callback("oob")
                                .build();

                if(tokenRequests.containsKey(moUssdReq.getSourceAddress())){
               //     System.out.println(tokenRequests.get(moUssdReq.getSourceAddress()));
                    String tok = tokenRequests.get(moUssdReq.getSourceAddress()).substring(6);
                    int i=0;
                    while(tok.charAt(i) != ' '/* || tok.charAt(i+1) != ',' || tok.charAt(i+2) != ' '*/) i++;
                    String tokkey = tok.substring(0,i);
                    String tokSecret = tok.substring(i+3,tok.length()-1);
               //     System.out.println(tokkey+"<<----------");
               //     System.out.println(tokSecret+"<<--------");
                    tokenRequests.remove(moUssdReq.getSourceAddress());
                    
                    Token reqestToken = new Token(tokkey, tokSecret);
                    try{
                        Token accessToken = service.getAccessToken(reqestToken, new Verifier(moUssdReq.getMessage()));
                        
                        tok = accessToken.toString().substring(6);
                    //    System.out.println("this is the token : "+tok);
                        i=0;
                        while(tok.charAt(i) != ' ' /*|| tok.charAt(i-1) != ',' || tok.charAt(i-2) != ' '*/) i++;
                        tokkey = tok.substring(0,i);
                        tokSecret = tok.substring(i+3,tok.length()-1);
                        
                        OAuthRequest request = new OAuthRequest(Verb.GET,PROTECTED_TWITTER_URL+"account/settings.json");
                        service.signRequest(accessToken, request); 
                        Response response = request.send();
                        System.out.println(response.getBody());
                        String result = response.getBody();
                        int k;
                        for(k=0;k<result.length();k++){
                            if(result.charAt(k) == '{') break;
                        }
                        result = result.substring(k);
                        JsonParser parser = new JsonParser();
                        JsonObject json = (JsonObject) parser.parse(result.toString());
                        String screenName = json.get("screen_name").getAsString();
                        boolean geoEnable = json.get("geo_enabled").getAsBoolean();
                        {
                            Connection con = datasource.getConnection();
                            Statement stm = con.createStatement();
                            String querry = "INSERT INTO tokens(id,twitterkey,twittersecret,screenName) VALUES (\'"+moUssdReq.getSourceAddress()
                                    +"\',\'"+tokkey+"\',\'"+tokSecret+"\',\'"+screenName+"\')";
                            stm.executeUpdate(querry);
                            stm.close();
                            con.close();
                        }
                        serviceCode = (byte) 38;
                    }catch(Exception fail){
                        fail.printStackTrace();
                        serviceCode = (byte) 39;
                    }
                    
                }
            }else if(menuState.get(menuState.size()-1) == (byte) 31){
                OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
                                .apiKey("nqhFLkm4QnfPfQZrsWSuw")
                                .apiSecret("ddZlQgMBuVYwEfbW34PeLF9uIibW4YF8brv96iwkc")
                                .callback("oob")
                                .build();
                int i=0;
                String data = tokenRequests.get(moUssdReq.getSourceAddress());
                while(data.charAt(i) != ' ') i++;
                String tokkey = data.substring(0,i);
                String toksecret = data.substring(i+1);
                System.out.println(tokkey+" -- "+toksecret);
                tokenRequests.remove(moUssdReq.getSourceAddress());
                Token access = new Token(tokkey,toksecret);
                OAuthRequest request = new OAuthRequest(Verb.POST,PROTECTED_TWITTER_URL+"statuses/update.json");
                request.addBodyParameter("status", moUssdReq.getMessage());
                service.signRequest(access, request);
                Response response = request.send();
                System.out.println(response.getBody());
                if(response.getBody().contains("errors")){
                    serviceCode = (byte) 32;
                }else{
                    serviceCode = (byte) 33;
                }
            }else if(menuState.get(menuState.size()-1) == (byte) 5){
                serviceCode = (byte) 51;
            }
            return serviceCode;
        }
        int reformat;
        //create service codes for child menus based on the main menu codes
        if (menuState.size() > 0 && menuState.get(menuState.size() - 1) != 0) {
            
            String generatedChildServiceCode = "" + menuState.get(menuState.size() - 1) + "" + serviceCode;
            reformat = Integer.parseInt(generatedChildServiceCode);
            if(reformat > 127){
                //this is the reformatting hack
                if(reformat == 311) generatedChildServiceCode = "35";
                else if(reformat == 312) generatedChildServiceCode = "37";
                else if(reformat == 313) generatedChildServiceCode = "36";
                else if(reformat == 351) generatedChildServiceCode = "37";
            //    else if(reformat == 391) generatedChildServiceCode = "36";
                else if(reformat > 220 && reformat <226) generatedChildServiceCode = "24";
            }
            serviceCode = Byte.parseByte(generatedChildServiceCode);
        }
        System.out.println(serviceCode);
        return serviceCode;
    }

    /**
     * Build next menu based on the service code
     * @param selection
     * @return menuContent
     */
    private String buildNextMenuContent(byte selection , MoUssdReq moUssdReq){
        String menuContent;
        try {
            //build menu contents
            menuContent = getText(selection,moUssdReq);
        } catch(MissingResourceException e) {
            //back to main menu
            System.out.println("Resource is missiong : "+selection );
            menuContent = getText((byte)0 , moUssdReq);
        }
        
        
        
        return menuContent;
    }

    /**
     * Build back menu based on the service code
     * @param selection
     * @return menuContent
     */
    private String buildBackMenuContent(byte selection , MoUssdReq moUssdReq){
        String menuContent;
        try {
            //build menu contents
            menuContent = getText(selection,moUssdReq);

        } catch(MissingResourceException e) {
            //back to main menu
            menuContent = getText((byte)0,moUssdReq);
        }
        return menuContent;
    }

}