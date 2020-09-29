package ecommercejava.cms.icommyjava.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.Lang;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
public class Language extends  HelpersJson{

 public static List<String> engLang = null;

    /**
     * will translate by string for multilanguage
     * @param text
     * @return
     */


    public static String getTranslateByLang(String text, String lang)  {
        ObjectMapper objectMapper = new ObjectMapper();
        //myList.stream().anyMatch(str -> str.trim().equals("B"))
        if(engLang ==null){
            try{
                  engLang = getEnLang();
             }catch (Exception e){ }
          }else {
            String newTxt = text.toLowerCase().trim();
            if(!engLang.stream().anyMatch(str -> str.trim().equals(newTxt)) && !newTxt.isEmpty()){
                engLang.add(newTxt);
             }
        }

        if(lang.contains("en") || lang.isEmpty())
            return text;
        //create ObjectMapper instance

        String value = text;
        String newTxt = text.trim().toLowerCase();
        try{
            //convert json file to map
            Map<String, String> map = objectMapper.readValue(readFile("content/lang/"+lang+".json"), Map.class);
            value  = map.containsKey(newTxt) ? map.get(newTxt) : text;
        }catch (Exception e){
            //System.out.println( "Error read file: " + e);
        }

        return value;
   }



    /**
     * Get lang file
     * @return
     */

    public static Map<String, String> getLang(String name){
        if(name == null || name.contains("en"))
            return null;

        try{
            String langFiele = readFile("content/lang/"+name+".json");
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            return  (new Gson()).fromJson(langFiele, type);
        }catch (Exception e){return null;}

     }

    /**
     * Get lang file
     * @return
     */

    public static List<String> getEnLang(){
        String langFiele = readFile("content/lang/en.json");
        Type type = new TypeToken<List<String>>(){}.getType();
        return  (new Gson()).fromJson(langFiele, type);
     }

    public static String splitLang(String json){
        String lang = currentLang();

        try{
            Gson gson = new Gson();
            Lang language = gson.fromJson(json, Lang.class);
            return language.get(lang);
        }catch (Exception e){
            return json;
        }
    }

    public static String splitLangByLang(String json, String lang){
         try{
            Gson gson = new Gson();
            Lang language = gson.fromJson(json, Lang.class);
            return language.get(lang);
        }catch (Exception e){
            return json;
        }
    }

    public static String getLangDataByLang(Lang ittem, String Lang){
       if(ittem == null){
            return "";
        }else{
            Lang lang = ittem;
            return lang.get(Lang);
        }
    }

    public static String getLangData(Lang ittem){
        String currentLang = currentLang();
       if(ittem == null){
            return "";
        }else{
            Lang lang = ittem;
            return lang.get(currentLang);
        }
    }

    public static Lang saveSimple(String text, Lang existing, String lang ){
        Lang mlang = existing == null ? new Lang(): existing;
        mlang.set(lang, text==null? "": text );
        return mlang;
    }

    public static String saveLang(String text, String existin, String lang ){
          lang = lang.isEmpty() ? "en" : lang;
          Lang lanCheck = new Lang();
          lanCheck.set(lang, "nothing");
          lang = lanCheck.get(lang).isEmpty() ? "en" : lang;

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        try{
            Lang language = gson.fromJson(existin, Lang.class);
            language.set(lang, text);
            return gson.toJson(language);

        }catch (Exception e){
            System.out.println("eeeerrr:"+e);
            try{
                Lang language = new Lang();
                language.set(lang, text);
                return gson.toJson(language);
            }catch (Exception dd){
                System.out.println("eeeerrr2:"+dd);
                return text;
            }
        }
    }



    public static JsonElement getConfigVar(String key, String secondKey){
        String data  = stringConfig();
        JsonElement element = returnAttr(data, key);
        if(element !=null) {
            if (secondKey != null) {
                return element.getAsJsonObject().get(secondKey);
            }
        }
        return element;
    }




    public static String getLangFromUrl(){

        //ServletUriComponentsBuilder.fromCurrentContextPath() // return http://localhost:8999
        //ServletUriComponentsBuilder.fromCurrentServletMapping().toUriString() // return the domain name  http://localhost:8999

        //ServletUriComponentsBuilder.fromCurrentRequest().toUriString() // return full url  http://localhost:8999/cat/become-milionare/laptops?id=dsg
        // add on the end .toUriString()
        String lang="";
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        try {
            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUriString(uri).build().getQueryParams();

            lang = parameters.get("wlang").get(0);
        }catch (Exception e){}
        lang = lang == null || lang.isEmpty() ? HelpersJson.getConfig().getLanguages().get(0) : lang;

       // if(uri.contains("/admin")) {

       // }else{
            //lang = getCookie("language").isEmpty()? HelpersJson.getConfig().getLanguages().get(0) : getCookie("language");
        //}

        return lang;
    }

    public static List<String> listLang(){
      return getConfig().getLanguages();
    }


    public static String adminLanguage(){

        List<String> listlang= getConfig().getLanguages();
        String baseurl = getConfig().getMinOptions().getBaseurl();

        String ll= "<div  class=\"col-md-12\">"+
                    "<p class=\"lang_menu\">";

         String currentLang = getLangFromUrl();

           String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();

           for(int i=0; i<listlang.size(); i++){
              String urifinal = !uri.contains("wlang") ? (uri.contains("?") ? uri+"&wlang="+listlang.get(i) : uri+"?wlang="+listlang.get(i)):
                                                                 uri.replace("wlang="+currentLang,"wlang="+listlang.get(i));

                String line =  i == 0 ? "" : "|";
                String className = currentLang.contains(listlang.get(i)) || ( currentLang.isEmpty() &&  i == 0 ) ? "activLang":"";
                ll=  ll+ line+ "<a  href=\""+urifinal+"\" class=\""+className+"\"> <img src=\""+baseurl+"content/views/assets/langicon/"+listlang.get(i).toUpperCase()+".png\"/> "+listlang.get(i).toUpperCase()+"</a>";
            }

             ll=  ll + "  <a href=\""+baseurl+"admin/general-settings/language\" target=\"_blank\"><i class=\"fa fa-pencil\"></i></a>   \n" +
                    "                    </p>\n" +
                    "             </div>\n" +
                    "             <div class=\"clear\"> </div>";


        return ll;
    }


    public static String currentLang(){
        List<String> listlang= Helpers.listLang();
        String currentLang = getLangFromUrl();
        return currentLang.isEmpty() ? listlang.get(0) : currentLang;
     }

    public static String currentLangForUrl(){
        List<String> listlang= Helpers.listLang();
        String currentLang = getLangFromUrl();
        return currentLang.isEmpty()? "": (listlang.get(0).contains(currentLang)? "" : "/"+currentLang );
    }

}
