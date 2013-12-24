package hms.kite.samples.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Udara
 */
public class Drug_Detector {
	private static final int SIDE_EFFECTS = 0, DRUG_USAGE = 1;
	
    String name;
    Connection connection;
    Statement stm;
    boolean iknow = false;
    HashMap<String, Double> guess = new HashMap<String,Double>();
	
    public Drug_Detector(String name , Connection con) {
        this.name = name.toLowerCase().trim().replaceAll(" | and ", "_");
        try {
            connection = con;
            stm = connection.createStatement();
            if(connection != null){
                System.out.println("connected to the database successfully");
                String query = "SELECT name FROM drug WHERE name LIKE \'"+this.name+"\'";
                ResultSet rs = stm.executeQuery(query);
                if(rs.next()){
                    iknow = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
    private String[] parsePage() throws IOException {
		String address = "http://www.medindia.net/doctors/drug_information/"+name+"_print.htm";
		URL oracle = new URL(address);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine, usage = "", effects = "";
		int t=0;
		String w;
		boolean se = true;
		
		String[] output = new String[2];
		
		try {
			while ((inputLine = in.readLine()) != null && !inputLine.contains("Why it is"));

			while (inputLine != null) {
				usage += inputLine;
				if(inputLine.contains("How it should"))
					break;
				inputLine = in.readLine();
			}
			usage = usage.substring(usage.indexOf("Why it is"), usage.indexOf("How it should"));
			
			//&nbsp;, &<other characters>
			usage = usage.replaceAll("&[#\\w]*;", "");
			//tags with leading/trailing white space
			usage = usage.replaceAll("\\s*<[^>]+>\\s*", "\n");
			//extra newlines
			usage = usage.replaceAll("\n{3,}", "\n\n");
			//extra newlines followed by headings
			usage = usage.replace(":\n\n", ":\n");
			usage = usage.trim();
			
			if(usage.length() < 1)
				throw new IOException();
		}
		catch(RuntimeException e) {
			usage = "Sorry, no usage details were found.";
		}

		try {
			while ((inputLine = in.readLine()) != null && !inputLine.contains("Side Effects"));

			while (inputLine != null && !inputLine.contains("<form")) {
				effects += inputLine;
				inputLine = in.readLine();
			}
			
			//dots at ends of phrases
			effects = effects.replaceAll("\\x2e\\s*<", "<");
			//&nbsp;, &<other characters>
			effects = effects.replaceAll("&[#\\w]*;", "");
			//tags with leading/trailing white space
			effects = effects.replaceAll("\\s*<[^>]+>\\s*", "\n");
			//dashes out of place
			effects = effects.replaceAll("\n\\x2d\\s*", "\n-");
			//extra newlines
			effects = effects.replaceAll("\n{3,}", "\n\n");
			//extra newlines followed by headings
			effects = effects.replace(":\n\n", ":\n");
			effects = effects.replace("Side Effects :\n", "");
			
			effects = effects.trim();
			if(effects.length() < 1)
				throw new IOException();
			else
				effects = "Side effects of " + name + ":\n\n" + effects;
		}
		catch(RuntimeException e) {
			effects = "Sorry, no side effect details were found.";
		}

		in.close();
		
		output[0] = usage;
		output[1] = effects;
		return output;
	}
    
	public String getDrugUsage() {
        String usage = "";
        System.out.println("-->"+name);
        if(iknow){
            String query = "SELECT drug_usage FROM drug WHERE name like \'" + name + "\'";
            try {
                ResultSet rs = stm.executeQuery(query);
                if(rs.next()){
                    return rs.getString(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }  
        }else{
            try {
				String[] details = parsePage();
				usage = details[0];
				String effects = details[1];
				
				String learn = "INSERT INTO drug (name,drug_usage,side_effects) VALUES (\'" + name + "\',\'" + usage.substring(0,Math.min(418, usage.length())) + "\',\'" + effects.substring(0,Math.min(418, effects.length())) +"\')";
				try {
					stm.executeUpdate(learn);
					iknow = true;
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
            }catch(IOException e){
                String predict = drugPredict(name);
				
				if(predict != null) {
					usage += "I guess you mean "+predict+" ("+(int) Math.ceil(guess.get(predict)*100)+"%)\n";
					String query = "SELECT drug_usage FROM drug WHERE name=\'"+predict+"\'";
					try {
						ResultSet rs = stm.executeQuery(query);
						if(rs.next()){
							usage += rs.getString(1);
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
				else {
					return "sorry";
				}
            }
        }
        return usage;
	}
	
	public String getSideEffects(){
        String effects = "";
        System.out.println("-->"+name);
        if(iknow){
            String query = "SELECT side_effects FROM drug WHERE name like \'"+name+"\'";
            try {
                ResultSet rs = stm.executeQuery(query);
                if(rs.next()){
                    return rs.getString(1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }  
        }else{
            try {
				String[] details = parsePage();
				effects = details[1];
				String usage = details[0];
				
				String learn = "INSERT INTO drug (name,drug_usage,side_effects) VALUES (\'" + name + "\',\'" + usage.substring(0,Math.min(418, usage.length())) + "\',\'" + effects.substring(0,Math.min(418, effects.length())) +"\')";
				try {
					stm.executeUpdate(learn);
					iknow = true;
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
            }catch(IOException e){
                String predict = drugPredict(name);
				
				if(predict != null) {
					effects += "I guess you mean "+predict+" ("+(int) Math.ceil(guess.get(predict)*100)+"%)\n";
					String query = "SELECT side_effects FROM drug WHERE name=\'"+predict+"\'";
					try {
						ResultSet rs = stm.executeQuery(query);
						if(rs.next()){
							effects += rs.getString(1);
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
				else {
					return "sorry";
				}
            }
        }
        return effects;
    }
    
    String drugPredict(String name){
        String out = null;
        if(connection != null){
           try {
                   String query = "SELECT name FROM drug where name like \'"+name.substring(0,1).toUpperCase()+"%\'";
                   
                   ArrayList<String> pos = new ArrayList<String>();
                   ResultSet rs = stm.executeQuery(query);
                   while(rs.next()){
                       pos.add(rs.getString(1));  
                   }
				   
				   //quit if no matches found
				   if(pos.size() < 1)
						return null;
				
                   int maxIndex=0,factorial = 0;
                   double t = 0,ta = 0,max = 0;
				   
                   for(int i=0;i<pos.size();i++){
                       t = 0;
                       factorial = 0;
                       
                        for(int k=0;k<name.length();k++){
                               for(int kk=k+1;kk<name.length();kk++){
                                    String temp=pos.get(i);
                                    if(temp.contains(name.subSequence(k,kk))){
                                        t += 1;
                                    }
                                    factorial++;
                               }
                       }
                        ta = t/factorial;
                       if(ta > max){
                           max = ta;
                           maxIndex = i;
                       }
                   }
                   guess.put(pos.get(maxIndex), max);
                   System.out.println(name+" ->"+pos.get(maxIndex)+" -> "+max);
                   return pos.get(maxIndex);
               
           } catch (SQLException ex) {
               //Logger.get//Logger(Path_Finder.class.getName()).log(Level.SEVERE, null, ex);
           }    
       }
        return out;
    }
}
