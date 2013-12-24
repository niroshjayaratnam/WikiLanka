/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseHandler;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bhash
 */
public class FinalResponse {
    
    private List<String> html_attributions;
    private List <Result> results;
    private String status;
    private String next_page_token;
    
    

    @Override
    public String toString() {
        return getHtml_attributions()  + "\n" + getResults() + "\n" + getStatus() + "\n" + getNext_page_token();
    }    

    /**
     * @return the html_attributions
     */
    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    /**
     * @return the results
     */
    public List <Result> getResults() {
        return results;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the next_page_token
     */
    public String getNext_page_token() {
        return next_page_token;
    }
}
