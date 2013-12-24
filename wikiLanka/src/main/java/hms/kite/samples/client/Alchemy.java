/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

/**
 *
 * @author Udara
 */
import com.alchemyapi.api.AlchemyAPI;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import java.io.*;
import java.text.Normalizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

class KeywordTest {
    public String getKeyWords(String input) throws IOException, SAXException,
            ParserConfigurationException, XPathExpressionException {
        // Create an AlchemyAPI object.
        AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromString("0c58de6169120da20c85fd8f8ff88a2fe8fef830");
/*
        // Extract topic keywords for a web URL.
        Document doc = alchemyObj.URLGetRankedKeywords("http://www.techcrunch.com/");
        System.out.println(getStringFromDocument(doc));
*/
        // Extract topic keywords for a text string.
        Document doc = alchemyObj.TextGetRankedKeywords(input);
        String raw = getStringFromDocument(doc);
        System.out.println(raw);
        int start = raw.indexOf("<text>");
        if(start < 0)   //error!
            return null;

		//offset = "<description">.length() = 13
        raw = raw.substring(start + 6);
        int end = raw.indexOf("</text>");
        
        raw = raw.substring(0, end).trim();
        raw = Normalizer.normalize(raw, Normalizer.Form.NFD);
        raw = raw.replaceAll("[^\\p{ASCII}]", "");
        raw = raw.replaceAll("&amp;", "&");
        raw = raw.replaceAll("&lt;", "<");
        raw = raw.replaceAll("&gt;", ">");
        raw = raw.replaceAll("&quot;", "\"");
        
        return raw;
/*
        // Load a HTML document to analyze.
        String htmlDoc = getFileContents("data/example.html");

        // Extract topic keywords for a HTML document.
        doc = alchemyObj.HTMLGetRankedKeywords(htmlDoc, "http://www.test.com/");
        System.out.println(getStringFromDocument(doc));
        */
    }

    // utility function
    private static String getFileContents(String filename)
        throws IOException, FileNotFoundException
    {
        File file = new File(filename);
        StringBuilder contents = new StringBuilder();

        BufferedReader input = new BufferedReader(new FileReader(file));

        try {
            String line = null;

            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } finally {
            input.close();
        }

        return contents.toString();
    }

    // utility method
    private static String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            return writer.toString();
        } catch (TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
