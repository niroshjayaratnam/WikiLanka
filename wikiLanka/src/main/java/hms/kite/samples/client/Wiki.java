/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import com.alchemyapi.api.AlchemyAPI;
import com.alchemyapi.api.AlchemyAPI_KeywordParams;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;

/**
 * @author Janaka
 */
public class Wiki {
    private final String base = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?";
    
    public String getWiki(String keyword) {
        String link = base + "QueryString=" + keyword + "&MaxHits=1";

        URL url = null;
        String raw = "";
        
        
        try {
            url = new URL(link);
            raw = getPage(url);
        }
        catch(MalformedURLException mfue) {
            System.out.println("Invalid URL");
            return null;
        }
        catch(IOException ioe) {
            System.out.println("Connection Error");
            return null;
        }

        int start = raw.indexOf("<Description>");
        if(start < 0)   //error!
            return null;

		//offset = "<description">.length() = 13
        raw = raw.substring(start + 13);
        int end = raw.indexOf("</Description>");
        
        raw = raw.substring(0, end).trim();
        raw = Normalizer.normalize(raw, Normalizer.Form.NFD);
        raw = raw.replaceAll("[^\\p{ASCII}]", "");
        raw = raw.replaceAll("&amp;", "&");
        raw = raw.replaceAll("&lt;", "<");
        raw = raw.replaceAll("&gt;", ">");
        raw = raw.replaceAll("&quot;", "\"");
        
        return raw;
    }
    
    private String getPage(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        //error if not in 200-300 range
        if(responseCode < 200 || responseCode > 299)
                throw new IOException("Connection Error " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}

