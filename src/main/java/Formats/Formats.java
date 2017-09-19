/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author barueri
 */
public class Formats {
    
    public Formats(){}
    
    //Cria string json genérica com valor default value
    public static String JsonFormat(String tuple) throws IOException{
        
        JSONObject json = new JSONObject();
        json.put("value", tuple);
        
        return json.toString();
    }
    
    //Cria string json com tag e valor vindos de parâmetro
    public static String JsonFormat(String tag, String tuple) throws IOException{
        
        JSONObject json = new JSONObject();
        json.put(tag, tuple);
        
        return json.toString();
    }
    
    //Cria string json com lista de valores vindos de parâmetro (por default enumerados de 0 até tam-1)
    public static String JsonFormat(ArrayList<String> values) throws IOException{
        
        JSONObject json = new JSONObject();
        int i =0;
        for(String value : values){
            json.put(Integer.toString(i), value.toString());
            i++;
        }
        
        return json.toString();
    }
    
    //Cria string json com lista de tags e lista de valores vindos de parâmetro
    public static String JsonFormat(ArrayList<String> tags, ArrayList<String> values) throws IOException{
        
        JSONObject json = new JSONObject();
        if(values.size() == tags.size()){
            
            for(int i=0; i<values.size(); i++){
                json.put(tags.get(i), values.get(i));
            }
            
        }
        
        return json.toString();
    }
    
    //Adiciona em uma string json um valor associado a uma tag vindos por parâmetro
    public static String addInJsonFormat(String pastJson, String tag, String value) throws IOException{
        
        JSONObject json = new JSONObject(pastJson);
        json.put(tag, value);
        
        return json.toString();
    }
    
    //Adiciona em um json um valor associado a uma tag vindos por parâmetro e retorna a string do json 
    public static String addInJsonFormat(JSONObject json, String tag, String value) throws IOException{
        
        json.put(tag, value);
        
        return json.toString();
    }
    
    //Adiciona em uma string json uma lista de valores associada a uma tag vindos por parâmetro retornando a string do json
    public static String addListInJsonFormat(String pastJson, String tag, ArrayList<String> values) throws IOException{
        
        JSONObject json = new JSONObject(pastJson);
        json.put(tag, values);
        
        return json.toString();
    }
    
    //Adiciona em um json uma lista de valores associada a uma tag vindos por parâmetro retornando a string do json
    public static String addListInJsonFormat(JSONObject json, String tag, ArrayList<String> values) throws IOException{
        
        json.put(tag, values);
        
        return json.toString();
    }
    
    //Retorna de uma string json uma lista de valores associadas a uma tag vindos por parâmetro
    public static ArrayList<String> returnArrayFromJSON(String json, String tag) throws IOException{
        
        JSONObject jsonObj1 = new JSONObject(json);
        JSONArray jArray = jsonObj1.getJSONArray(tag);
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i=0; i<jArray.length();i++){
            arrayList.add((String)jArray.get(i));
        }

        return arrayList;
    }
    
    //Retorna de uma json uma lista de valores associadas a uma tag vindos por parâmetro
    public static ArrayList<String> returnArrayFromJSON(JSONObject json, String tag) throws IOException{
        
        JSONArray jArray = json.getJSONArray(tag);
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i=0; i<jArray.length();i++){
            arrayList.add((String)jArray.get(i));
        }

        return arrayList;
    }
}