package ecommercejava.cms.icommyjava.helper;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Attributes.Attributes;
import ecommercejava.cms.icommyjava.dto.Attributes.AttributesVar;
import ecommercejava.cms.icommyjava.dto.Attributes.ProductVariations;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.dto.Config.Currency;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class HelpersJson extends  HelpersFile{

    public static Config conficData=null;

    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public static String jsonGetRequest(String urlQueryString) {
        String json = null;
        try {
            URL url = new URL(urlQueryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            json = streamToString(inStream); // input stream to string
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }


  /**
     * Check if json string is valid if yes than return will return an JsonArray, will convert to json if not will return null
     * @param json
     * @return
     */

    public static List<String> isJSONArrValid(String json) {
        try{
            Type type = new TypeToken<List<String>>(){}.getType();
            return  (new Gson()).fromJson(json, type);
         }catch (Exception e){
            return null;
          }
    }

    /**
     * Check if json string is valid if yes than return will return an JsonObject, will convert to json if not will return null
     * @param json
     * @return
     */

    public static Map<String,String>convertJsonToMap(String json) {
        json = json==null? "":json;
        try {
            Type type = new TypeToken<Map<String,String>>(){}.getType();
            return !json.isEmpty() ? (new Gson()).fromJson(json, type) : new HashMap<>() ;
        } catch (Throwable ignored) {
            return new HashMap<>();
        }
    }

    /**
     * Check if json string is valid if yes than return will return an JsonObject, will convert to json if not will return null
     * @param json
     * @return
     */

    public static JsonObject isJSONValid(String json) {
        try {
            //JSONArray gson = new JSONArray(json);
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            return  jsonObject;
        } catch (Throwable ignored) {
            return null;
        }
    }


   public static JsonElement isJson(String json){

       try {
           JsonElement jsonObject = new Gson().fromJson(json, JsonObject.class);
           return  jsonObject;
       } catch (Throwable ignored) { }

       try {
           JsonElement jsonObject = new Gson().fromJson(json, JsonArray.class);
           return  jsonObject;
       } catch (Throwable ignored) { }

       return null;
   }

    /**
     * Get data from config file
     * @return
     */

    public static String stringConfig(){
         // if(conficData ==null){
           //   conficData = readFile("content/config/config.json");
        //  }
          return   readFile("content/config/config.json");
    }

    /**
     * get attributes from json file, deiced tu use json file because table row mysql may not have enough space for it
     * @return
     */
    public static String stringAttr(){
        return readFile("content/config/attr.json");
    }



    public static Config getConfig(){
        if(conficData ==null){
              String data = stringConfig();
              conficData = data.isEmpty()? new Config() : new Gson().fromJson(data, Config.class);
            }
         return conficData;
    }

    public static Currency getMainCurrency(){
       Map<String, Currency> currencies = getConfig().getCurrencies();

       for(Map.Entry<String, Currency> entry: currencies.entrySet()) {
            if( entry.getValue().getMain()){
                return entry.getValue();
            }
       }

        Currency currency = new Currency();
        currency.setRate("1");
        currency.setName("USD");
        currency.setType("left");
       return currency;
    }
    /**
     * get json variable vrom config
     * @param key
     * @return
     */
    public static JsonElement getConfigValByKey(String key){
        return  returnAttr(stringConfig(), key);
    }

    /**
     * get json variable vrom config
     * @param key
     * @return
     */
    public static String getStringConfigValByKey(String key){
        return  returnStringAttr(stringConfig(), key);
    }

    /**
     * get json variable vrom config
     * @param key1
     *  @param key2
     * @return
     */
    public static JsonElement getConfigValByKeyKey(String key1, String key2){
        try {
            JsonElement data1 = returnAttr(stringConfig(), key1);
            JsonElement data2 = returnAttr(clearJson(data1.toString()), key2);
            return data2;
        }catch (Exception e){ }
        return  null;
    }




    public static AttributesVar attributesVar(JsonElement type){
       try {
            return new Gson().fromJson(type.toString(), AttributesVar.class);
       }catch (Exception e){
           return  new AttributesVar();
       }
    }

    public static Attributes getAttrSettings(){
        try {
             Attributes attributes = new Gson().fromJson(Helpers.stringAttr(), Attributes.class);
             return attributes ;
        }catch (Exception e){
             return new Attributes();
         }
    }



    /**
     *  will return the key of sugestion
     * @param attr
     * @param field
     * @return
     */
    public static String getKeyFromProductAttr (Map<String, ProductVariations> attr, String field ){

       try {
            if(attr!=null){
                for (Map.Entry<String, ProductVariations> entry : attr.entrySet()) {
                    if(entry.getValue().get(field) != null) {
                        return entry.getValue().get(field);
                        //if(jsattributes.get(field).getSugestion().containsKey(entry.getValue().get(field))) {
                          //  return  jsattributes.get(field).getSugestion().get(entry.getValue().get(field)).get(lang);
                        //}
                    }
                }
            }
        }catch (Exception e){  }

        return "";
    }



    /**
     * this will check if sugestion key is in attr product, this is for product specifictaions like select and checkbox
     * @param attr
     * @param field
     * @param keySugestion
    * @return true or false
     */
   public static boolean checkIfIsInAttrProduct(Map<String, ProductVariations> attr, String field, String keySugestion ){
        try {
            if(attr!=null){
                for (Map.Entry<String, ProductVariations> entry : attr.entrySet()) {
                    if(entry.getValue().get(field) != null) {
                         if(entry.getValue().get(field).contains(keySugestion)) {
                            return true;
                         }
                    }
                }
            }
         }catch (Exception e){  }

        return false;
    }



    /**
     *  will return json element by key
     * @param val
     * @param keyName
     * @return
     */

    public static String returnJsonElement (JsonElement val,   String keyName){
        if(val == null)
            return "";

        JsonElement val2 = val.getAsJsonObject().get(keyName) ;
        return val2 !=null ? clearJson(val2.toString()) : "";
    }



    /**
     * convert from array value came with " and \ so we need to clear them and get clear string
     * @param jsonString
     * @return
     */
    public static String clearJson(String jsonString){
        if(jsonString == null)
            return "";

        String json =  jsonString.startsWith("\"") ? jsonString.substring(1, jsonString.length() - 1) : jsonString;
        json = json.replaceAll("\\\\r","");
        json = json.replaceAll("\\\\n","");
        json = json.replaceAll("\\\\","");
        return json;
    }


    public static JsonElement returnAttr(String attr, String type){
        JsonObject json =  isJSONValid(attr);

        if(json !=null) {
            return json.get(type);
        }

        return null;
    }

    public static String returnStringAttr(String attr, String type){
        JsonObject json =  isJSONValid(attr);

        if(json !=null) {
            return json.get(type) !=null? clearJson(json.get(type).toString()) : "";
        }

        return "";
    }

    public static String jsonElemString(JsonElement var, String key){
        if(var !=null) {
            if(var.getAsJsonObject().get(key) != null){
                return clearJson(var.getAsJsonObject().get(key).toString());
            }
        }
        return "";
    }

    public static Integer jsonElemInt(Map<String, String> var, String key){
        if(var !=null) {
            if(var.containsKey(key)){
                return var.get(key).isEmpty() ? 0 :Integer.parseInt(var.get(key)) ;
            }
        }
        return 0;
    }

    public static float jsonElemFloat(Map<String, String> var, String key){
        if(var !=null) {
            if(var.containsKey(key)){
                return var.get(key).isEmpty() ? Float.parseFloat("0") : Float.parseFloat(var.get(key)) ;
            }
        }
        return Float.parseFloat("0");
    }


    public static String convertRequestt(Map<String, String[]> mapRequest ){

       Map<String, String> map =  new LinkedHashMap<>();
        for(Map.Entry<String, String[]> entry: mapRequest.entrySet()){
            String val = entry.getValue()[0].isEmpty() ? null : entry.getValue()[0];
            String key = entry.getKey();
            if(entry.getKey().contains("catset")){
                try {
                    val = new Gson().toJson(entry.getValue());
                    key = "catid";
                }catch (Exception e){}
             }

            map.put(key, val);

        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(map);
    }

    public static Map<String, String> convertRequestToMap(Map<String, String[]> mapRequest ){

        Map<String, String> map =  new LinkedHashMap<>();
        for(Map.Entry<String, String[]> entry: mapRequest.entrySet()){
            String val = entry.getValue()[0].isEmpty() ? null : entry.getValue()[0];
            String key = entry.getKey();
            if(entry.getKey().contains("catset")){
                try {
                    val = new Gson().toJson(entry.getValue());
                    key = "catid";
                }catch (Exception e){}
            }

            map.put(key, val);

        }

        return map;
    }

    public static Map<String,List<String>> readyBlocks(String json){
        JsonObject jsonObject = Helpers.isJSONValid(json);
        Map<String,List<String>> map = new HashMap<>();

        if(jsonObject!=null){
            try {
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

                    List<String> list = new ArrayList<>();
                    list.add(Helpers.clearJson(entry.getValue().getAsJsonArray().get(0).toString()));
                    list.add(Helpers.clearJson(entry.getValue().getAsJsonArray().get(1).toString()));
                    list.add(Helpers.clearJson(entry.getValue().getAsJsonArray().get(2).toString()));
                    try {
                        list.add(Helpers.clearJson(entry.getValue().getAsJsonArray().get(3).toString()));
                    } catch (Exception e) {
                        list.add("");
                    }

                    map.put(entry.getKey(), list);
                }
            }catch (Exception e){}
        }

        return map;
    }

}
