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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hms.kite.samples.api.EncodingType;
import hms.kite.samples.api.StatusCodes;
import hms.kite.samples.api.sms.MoSmsListener;
import hms.kite.samples.api.sms.SmsRequestSender;
import hms.kite.samples.api.sms.messages.MoSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsResp;
import hms.sdp.samples.ussd.client.MainMenu;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class SimpleClient implements MoSmsListener {

    private final static Logger LOGGER = Logger.getLogger(SimpleClient.class.getName());
    private DataSource datasource;
    private Connection connection;
  //  private String SenderURL = "http://localhost:7000/sms/send";
    private String SenderURL = "http://api.dialog.lk:8080/sms/send";
    @Override
    public void init() {

    }
    public void initialize(Connection d){
        //datasource = d;
        connection = d;
    }
    @Override
    public void onReceivedSms(MoSmsReq moSmsReq) {
        try {
            LOGGER.info("Sms Received for generate request : " + moSmsReq);
  //          SmsRequestSender smsMtSender = new SmsRequestSender(new URL(SenderURL));

//            MtSmsReq mtSmsReq;
//            mtSmsReq = createSubmitMultipleSms(moSmsReq);
//            mtSmsReq = createSimpleMtSms(moSmsReq);

  /*          mtSmsReq.setApplicationId("APP_003783");
            mtSmsReq.setPassword("38ed2499466f579b169e728c86f7e315");
            mtSmsReq.setSourceAddress("Caravan");// default sender address or aliases
            mtSmsReq.setVersion(moSmsReq.getVersion());
//            mtSmsReq.setEncoding("0");
//            mtSmsReq.setChargingAmount("5");

            String deliveryReq = moSmsReq.getDeliveryStatusRequest();
            if (deliveryReq != null) {
                if (deliveryReq.equals("1")) {
                    mtSmsReq.setDeliveryStatusRequest("1");
                }
            } else {
                mtSmsReq.setDeliveryStatusRequest("0");
            }

            MtSmsResp mtSmsResp = smsMtSender.sendSmsRequest(mtSmsReq);
            String statusCode = mtSmsResp.getStatusCode();
            String statusDetails = mtSmsResp.getStatusDetail();
            if (StatusCodes.SuccessK.equals(statusCode)) {
                LOGGER.info("MT SMS message successfully sent");
            } else {
                LOGGER.info("MT SMS message sending failed with status code [" + statusCode + "] "+statusDetails);
            }
*/

        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Unexpected error occurred", e);
        }
    }
    public void sendReply(MoSmsReq moSmsReq){
        try {
            SmsRequestSender smsMtSender = new SmsRequestSender(new URL(SenderURL));

            MtSmsReq mtSmsReq;
//            mtSmsReq = createSubmitMultipleSms(moSmsReq);
            mtSmsReq = createSimpleMtSms(moSmsReq);

            mtSmsReq.setApplicationId("APP_003783");
            mtSmsReq.setPassword("38ed2499466f579b169e728c86f7e315");
            mtSmsReq.setSourceAddress("Caravan");// default sender address or aliases
            mtSmsReq.setVersion(moSmsReq.getVersion());
//            mtSmsReq.setEncoding("0");
//            mtSmsReq.setChargingAmount("5");

            String deliveryReq = moSmsReq.getDeliveryStatusRequest();
            if (deliveryReq != null) {
                if (deliveryReq.equals("1")) {
                    mtSmsReq.setDeliveryStatusRequest("1");
                }
            } else {
                mtSmsReq.setDeliveryStatusRequest("0");
            }

            MtSmsResp mtSmsResp = smsMtSender.sendSmsRequest(mtSmsReq);
            String statusCode = mtSmsResp.getStatusCode();
            String statusDetails = mtSmsResp.getStatusDetail();
            if (StatusCodes.SuccessK.equals(statusCode)) {
                LOGGER.info("MT SMS message successfully sent");
            } else {
                LOGGER.info("MT SMS message sending failed with status code [" + statusCode + "] "+statusDetails);
            }


        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Unexpected error occurred", e);
        }
    }

    private MtSmsReq createSimpleMtSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = new MtSmsReq();

        mtSmsReq.setMessage(moSmsReq.getMessage());
        List<String> addressList = new ArrayList<String>();
        addressList.add(moSmsReq.getSourceAddress());
        mtSmsReq.setDestinationAddresses(addressList);

        return mtSmsReq;
    }

    private MtSmsReq createSubmitMultipleSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = new MtSmsReq();

        mtSmsReq.setMessage("This message will receive to multiple users");
        List<String> addressList = new ArrayList<String>();

        addressList.add("tel:123456789");
        addressList.add("tel:456789123");

        mtSmsReq.setDestinationAddresses(addressList);

        return mtSmsReq;
    }

    private MtSmsReq createBinarySm(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = new MtSmsReq();
        mtSmsReq.setMessage(
                "3000000002010000481c010000000000001c000000000007e000720000000000" +
                        "3c1001c10000000001e010fe03e00000001f000bff8ff8000000f00007ffdffc" +
                        "00000080000fedffdc000000602a0f7fefce0000001fd41e7ff7ee0000000060" +
                        "1cfff7ee00000007801cfff3fe00000004001cfffbfe00000003051cfffbfe00" +
                        "07f800fa9cfffbfe007800000c1c7ffffe00000000300e7ffffc0000000021ce" +
                        "7ffffc00001ffe1e373ffff80007e000000fbffff8001800000003dffff00000" +
                        "00000001efffe0000000000000ffffc00000000fff007fff80000007f000003f" +
                        "ff000000380000001ffe0000000000000007fc0000000000000001f800000000" +
                        "00000000600000");
        mtSmsReq.setEncoding(EncodingType.BINARY.getCode());
        mtSmsReq.setBinaryHeader("060504158a0000");

        List<String> addressList = new ArrayList<String>();
        addressList.add(moSmsReq.getSourceAddress());
        mtSmsReq.setDestinationAddresses(addressList);

        return mtSmsReq;
    }

    private MtSmsReq createFlashSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = new MtSmsReq();

        mtSmsReq.setMessage("This is a flash SM");

        List<String> addressList = new ArrayList<String>();
        addressList.add(moSmsReq.getSourceAddress());
        mtSmsReq.setDestinationAddresses(addressList);

        mtSmsReq.setEncoding(EncodingType.FLASH.getCode());

        return mtSmsReq;
    }
    
    private void processSMS(MoSmsReq moSmsReq){
        
    }
    
    private String twitterOauthURL(String sourceId){
      /*  OAuthService service = new ServiceBuilder().provider(TwitterApi.class)
                                    .apiKey("nqhFLkm4QnfPfQZrsWSuw")
                                    .apiSecret("ddZlQgMBuVYwEfbW34PeLF9uIibW4YF8brv96iwkc")
                                    .callback("oob")
                                    .build();
                            
        Token requestToken = service.getRequestToken();
        String longUrl = service.getAuthorizationUrl(requestToken);
                    //lets shorten this longUrl using google url shortner
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyBh9YE-z9Rj6eeBrHhSVzSmgMGLb168ivo");
        post.setHeader("Content-Type","application/json");
        String shortURL = null;
        System.out.println("started oauth");
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
            shortURL = json.get("id").getAsString();
            System.out.println("got the short url");
      } catch (Exception ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
      }
        Connection con = null;
        Statement stm;
        try{
            con = connection;
            stm = con.createStatement();
            String tok = requestToken.toString().substring(6);
                int i=0;
                    while(tok.charAt(i) != ' '/* || tok.charAt(i+1) != ',' || tok.charAt(i+2) != ' ') i++;
                String tokkey = tok.substring(0,i);
                String tokSecret = tok.substring(i+3,tok.length()-1);
            String querry = "SELECT id FROM tokens WHERE id="+sourceId;
            ResultSet rs = stm.executeQuery(querry);
            System.out.println("entered the database");
            if(rs.next()){
                querry = "UPDATE tokens SET twitterkey=\'"+tokkey+"\',twittersecret=\'"+tokSecret+"\' WHERE id=\'"+sourceId+"\'";
                stm.executeUpdate(querry);
                rs.close();
                stm.close();
            }else{
                querry = "INSERT INTO tokens (id,twitterkey,twittersecret) VALUES(\'"+sourceId+"\',\'"+tokkey+"\',\'"+tokSecret+"\')";
                stm.executeUpdate(querry);
                stm.close();
            }
        }catch(Exception e){
            return "Sorry =( CARAVAN is a little crowded now.Please try again later.";
        }finally{   
            if(con != null){ try{ con.close(); }catch(Exception e){} }
        }
      if(shortURL == null){
          return "Sorry =( Error occured. Please try to ask for another Twitter request!";
      }else{
          return "CARAVAN has asked permissions from twitter to allow post your tweets. You'll have to allow access to CARAVAN twitter application for this. Please goto this URL and allow access to the application.\n "+shortURL+" \n"
                  + "After you allow access twitter will give you a PIN number. Please copy that PIN number and send a message to 77100 by typing cvn twitter <YOUR_PIN_HERE> .( i.e cvn twitter 12345678 )";
      }*/
        return sourceId;
    }
}