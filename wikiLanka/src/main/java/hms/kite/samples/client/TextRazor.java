/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hms.kite.samples.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/**
 *
 * @author udara
 */
public class TextRazor {
    
    final static int wolframalpha = 0,dictionary = 1,medical = 2, drug = 3 , personality = 4 , busRoute = 5 , wikipedia = 6;
    
    String time;
    Response response;
    ArrayList<String> words = new ArrayList<String>();
    Gson gson = new Gson();
    public TextRazor(){
    }
    Boolean findRelations(MindObj mo){
        System.out.println("--- "+mo.getMsg());
        Scanner sc = new Scanner(mo.getMsg().trim());
        while(sc.hasNext()){     
            words.add(sc.next());
        }

        if(response != null){
           Properties temp[] = response.properties;
           if(temp != null){
                for(int j=0;j<temp.length;j++){
                    int t[] = temp[j].wordPositions;
                    for(int k=0;k<t.length;k++){
                        if(words.get(t[k]).contains("meaning")){
                            int s[] = temp[j].propertyPositions;
                            String out = "";
                            for(int m=0;m<s.length;m++){
                                out += words.get(s[m]) + " ";
                            }
                            out = out.trim();
                            System.out.println("meaning->  "+ out);
                            mo.setInterest(out);
                            mo.relevance[dictionary] += 300;
                            mo.relevance[wolframalpha] += 150;
                            return true;
                        }
                        if(words.get(t[k]).contains("effect")){
                            int s[] = temp[j].propertyPositions;
                            String out = "";
                            for(int m=0;m<s.length;m++){
                                out += words.get(s[m]) + " ";
                            }
                            out = out.trim();
                                
                            
                            if(out.equalsIgnoreCase("side")){
                                mo.relevance[drug] += 250;
                                continue;
                            }else if(k>0 && words.get(t[k-1]).contains("side")){
                                   mo.relevance[drug] += 300;
                            }
                            System.out.println("Effect->  "+ out);
                            mo.setInterest("(s) "+out);
                            mo.relevance[wolframalpha] += 120;
                            return true;
                        }
                        if(words.get(t[k]).contains("drug")){
                            int s[] = temp[j].propertyPositions;
                            String out = "";
                            for(int m=0;m<s.length;m++){
                                out += words.get(s[m]) + " ";
                            }
                            out = out.trim();
                            System.out.println("Drug->  "+ out);
                            mo.setInterest("(u) "+out);
                            mo.relevance[drug] += 300;
                            mo.relevance[wolframalpha] += 120;
                            return true;
                        }
                    }
                }
            }
        }
        if(mo.getMsg().contains("who") || mo.getMsg().contains("what")){
                                                  
                KeywordTest key = new KeywordTest();
                try{
                    String person = key.getKeyWords(mo.getMsg());
                    mo.setInterest(person);
                    if(words.size() < 5){   
                        mo.relevance[wikipedia] += 300;
                        mo.relevance[wolframalpha] += 280;
                    }else{
                        mo.relevance[wikipedia] += 280;
                        mo.relevance[wolframalpha] += 300;
                    }
                }catch(Exception e){
                                
                }
                return true;
        }
        return false;
    }
    Boolean findPronouns(MindObj mo){
        if(response != null){
           Sentence temp[] = response.sentences;
           if(temp != null){
                for(int j=0;j<temp.length;j++){
                    Word t[] = temp[j].words;
                    for(int k=0;k<t.length;k++){
                        if(t[k].partOfSpeech.equals("PRP") || t[k].partOfSpeech.equals("PRP$")){
                            mo.relevance[personality] += 200;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
class Response{
    String language;
    boolean languageIsReliable;
    Properties []properties;
    Sentence []sentences;

    public Response() {
    } 
}
class Properties{
    int id;
    int wordPositions[];
    int propertyPositions[];
    double time;

    public Properties() {
    }   
}
class Sentence{
    int possition;
    Word words[];
    public Sentence() {
    }   
}

class Word{
    int possition;
    int startingPos;
    int endingPos;
    String stem;
    String lemma;
    String token;
    String partOfSpeech;
    public Word() {
    }
}