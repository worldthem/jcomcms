package ecommercejava.cms.icommyjava.helper;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Attributes.Attributes;
import ecommercejava.cms.icommyjava.dto.Attributes.AttributesVar;
import ecommercejava.cms.icommyjava.dto.Config.CatJson;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.dto.billingshipping.FieldsJson;
import ecommercejava.cms.icommyjava.entity.Categories;
import ecommercejava.cms.icommyjava.entity.Gallery;
import ecommercejava.cms.icommyjava.entity.Lang;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AdminHelpers {




    public static LinkedHashMap getSubmenu(String Path, String Name){

       if(!Helpers.fileExist(Path))
                          return null;

        try {
            NodeList nList = HelpersFile.readXml(Path, Name);
            //new TreeMap
            LinkedHashMap<String, LinkedHashMap<String,String>> topMenu = new LinkedHashMap<>();

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    //System.out.println("Menu nr : " + eElement.getAttribute("nr"));
                    //System.out.println("Length : " + eElement.getElementsByTagName("menu").getLength());

                   NodeList nodelist = eElement.getElementsByTagName("menu");
                    LinkedHashMap<String, String> submenu = new LinkedHashMap<>();
                    for (int j = 0; j < nodelist.getLength(); j++) {
                        Element element1 = (Element) nodelist.item(j);
                        Node nodmenu= nodelist.item(j);

                        if(nodmenu !=null) {
                            //System.out.println("Url: " + element1.getAttribute("url") + " ---  Title: " + nodmenu.getTextContent());
                            submenu.put(element1.getAttribute("url"), nodmenu.getTextContent());
                        }

                    }
                    String idMenu = eElement.getAttribute("nr");
                    if(submenu ==null || submenu.isEmpty()){
                        submenu.put("action", "delete");
                    }
                    topMenu.put(idMenu, submenu );
                }
            }
            return topMenu;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


public static Map<String, LinkedHashMap<String,String>> mergeMap(Map<String, LinkedHashMap<String,String>> to, Map<String, LinkedHashMap<String,String>> from){
        if(from !=null) {
            for (Map.Entry<String, LinkedHashMap<String,String>> entry : from.entrySet()) {
               if(to.containsKey(entry.getKey())){

                     if(entry.getValue().get("action") != null  ){
                        to.remove(entry.getKey());
                     }else{
                        LinkedHashMap<String, String> newMap =new LinkedHashMap<>(entry.getValue());
                        LinkedHashMap<String, String> newMap2 =new LinkedHashMap<>(to.get(entry.getKey()));
                        LinkedHashMap<String, String> newMap3 =new LinkedHashMap<>();
                        newMap3.putAll(newMap2);
                        newMap3.putAll(newMap);
                        to.put(entry.getKey(), newMap3);
                     }

               }else{
                   to.put(entry.getKey(), entry.getValue());
               }

             }
        }
     return to;
}


public static String firstElemMap(Map<String,String> map, String val){
        if(map == null)
            return "";

        Map.Entry<String,String> entry = map.entrySet().iterator().next();
       return  val.contains("key")? entry.getKey():  entry.getValue();
}

 public static LinkedHashMap removeFirstElemMap(LinkedHashMap<String,String> map ){
         if(map == null)
               return null;

        Map.Entry<String,String> entry = map.entrySet().iterator().next();
        map.remove(entry.getKey());

        return map;
    }


    public static String adminmenu(String type){
         return "";
    }



    public static CatJson setCatJson(Categories category, String cpu){
            CatJson catJson = new CatJson();
            catJson.setId(category.getCatid());
            catJson.setCpufull(cpu);
            catJson.setCpu(category.getCpu());
            catJson.setTitle(category.getTitleClean());
            catJson.setType(category.getType());
            catJson.setTip(category.getTip());
            catJson.setParent(category.getParent());
       return catJson;
    }



   public static void generateCatStructure(List<Categories> categories){
        Map<String, CatJson> catStructure = new LinkedHashMap<>();
         try{

           for(Categories category : categories){
               String cpu="";
               String cpu2="";
               String cpu3="";
               String cpu4="";
               String cpu5="";

              if(category.getParent().compareTo(0)==0) {
                   cpu = cpu.isEmpty() ? category.getCpu() : cpu + "/" + category.getCpu();
                   catStructure.put(Integer.toString(category.getCatid()), setCatJson(category, cpu));

                   for (Categories category2 : categories) {
                        if (category2.getParent().compareTo(category.getCatid())==0) {
                           cpu2 = cpu + "/" + category2.getCpu();
                           catStructure.put(Integer.toString(category2.getCatid()), setCatJson(category2, cpu2));

                           for (Categories category3 : categories) {
                               if (category3.getParent().compareTo(category2.getCatid())==0) {
                                   cpu3 = cpu2 + "/" + category3.getCpu();
                                   catStructure.put(Integer.toString(category3.getCatid()), setCatJson(category3, cpu3));

                                   for (Categories category4 : categories) {
                                       if (category4.getParent().compareTo(category3.getCatid())==0) {
                                           cpu4 = cpu3 + "/" + category4.getCpu();
                                           catStructure.put(Integer.toString(category4.getCatid()), setCatJson(category4, cpu4));

                                           for (Categories category5 : categories) {
                                               if (category5.getParent().compareTo(category4.getCatid())==0) {
                                                   cpu5 = cpu4 + "/" + category5.getCpu();
                                                   catStructure.put(Integer.toString(category5.getCatid()), setCatJson(category5, cpu5));
                                                }
                                           }

                                       }
                                   }

                               }
                           }


                       }
                   }
               }

           }

             Config config = Helpers.getConfig();
             config.setCategories(catStructure);
             config.save();

          }catch (Exception n){ System.out.println("errrStruc"+n);}

    }


    public static String checkIsIn(String json, String id, String show){
        String checked ="";
        json = json == null ? "" : json;
        if(json.contains("[")) {
            List<String> jsonArray = Helpers.isJSONArrValid(json);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (jsonArray.get(i).contains(id)) {
                        checked = show;
                        break;
                    }
                }
             }
        }else{
            if (json.contains(id)) {
                checked = show;
            }
        }
        return checked;
    }

    public static String catcheckbox(CatJson catJson , String selected){
        try{
            String[] cpufull = catJson.getCpufull().split("/");
            String spac = "0";
            if(cpufull.length==2) {
                spac = "7px";
            }else if(cpufull.length==3){
                spac = "12px";
            }else if(cpufull.length==4){
                spac = "17px";
            }
             return "<label class=\"product_cat_single\">\n" +
                     "   <input style=\"margin-left:"+spac+";\" name=\"catset\" "+checkIsIn(selected, Integer.toString(catJson.getId()), "checked='yes'")+" type=\"checkbox\" value=\""+catJson.getId()+"\"> "+catJson.getTitle()+" \n" +
                     "</label>";
         }catch (Exception e){ /*System.out.println("dddddddd" +e);*/   return ""; }
    }

    public static String catselect(CatJson catJson , String selected ){
        try{
            String[] cpufull = catJson.getCpufull().split("/");
            String spac = "";
            if(cpufull.length==2) {
                spac = "&nbsp;&nbsp;";
            }else if(cpufull.length==3){
                spac = "&nbsp;&nbsp;&nbsp;&nbsp;";
            }else if(cpufull.length==4){
                spac = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            }

         return "<option value=\""+catJson.getId()+"\" "+checkIsIn(selected, Integer.toString(catJson.getId()), "selected='yes'")+">"+spac+" "+catJson.getTitle()+"</option>"  ;
         }catch (Exception e){ /*System.out.println("dddddddd" +e);*/    return ""; }

    }
    public static String tableAdmin(CatJson catJson){
       try{
           String[] cpufull = catJson.getCpufull().split("/");
           String spac = "";
           if(cpufull.length==2) {
               spac = "<span style=\"margin-left:15px;\">&gt;</span>";
           }else if(cpufull.length==3){
               spac = "<span style=\"margin-left:20px;\">&gt;</span>";
           }else if(cpufull.length==4){
               spac = "<span style=\"margin-left:25px;\">&gt;</span>";
           }

        return  "<tr>\n" +
                "                 <td>\n" +
                "                     <input type=\"checkbox\" name=\"id\" class=\"checkboxeach\"   value=\""+catJson.getId()+"\">\n" +
                "                 </td>\n" +
                "                 <td>"+catJson.getId()+"</td>\n" +
                "                 <td>\n" +
                "                     <a href=\"/"+catJson.getCpufull()+"\" target=\"_blank\" >"+spac+" "+catJson.getTitle()+"</a>\n" +
                "                 </td>\n" +
                "                 <td>"+catJson.getCpu()+"</td>\n" +
                "                 <td>\n" +
                "                     <a href=\"/admin/categories/add-edit/"+catJson.getType()+"?id="+catJson.getId()+"\" class=\"fa_edit\"></a> |\n" +
                "                     <a href=\"admin/categories/delete-hide?id="+catJson.getId()+"&action="+(catJson.getTip()==2? "1":"2")+"\" title=\"Unpublish\" class=\""+(catJson.getTip()==2? "fa_unpublish":"fa_publish")+"\" ></a>\n" +
                "                 </td>\n" +
                "                 <td>\n" +
                "                     <a href=\"/admin/categories/delete-hide?id="+catJson.getId()+"\" class=\"fa_delete\" onclick=\"return confirm('You are sure?') ? true : false;\"></a>\n" +
                "                 </td>\n" +
                "             </tr>";
        }catch (Exception e){ System.out.println("dddddddd" +e);   return ""; }

    }

    public static String typeReturn(String key, CatJson jsonElement, String type, String selected ){
        if(type.contains("admintable")){

            return tableAdmin(jsonElement );
        }else if(type.contains("select")){
            return catselect(jsonElement, selected );
        }else if(type.contains("checkbox")){
            return catcheckbox(jsonElement, selected);
        }
        return "";
    }


    public static String  ReturnLoop(Map<String, CatJson> categories , String type, String viewtype, int idParent,  String selected ){
        String dataReturn = "";
        int j=0;

        for (Map.Entry<String, CatJson> entry : categories.entrySet()) {

            int parrent = entry.getValue().getParent();
            if(parrent == idParent && entry.getValue().getType().contains(type)){
                 dataReturn = dataReturn + typeReturn(entry.getKey(), entry.getValue(), viewtype, selected );
                 dataReturn = dataReturn + ReturnLoop(  categories, type, viewtype, entry.getValue().getId(), selected );
              }

        }
        return dataReturn;
    }

    public static String catStructure(String type, String viewtype,  String selected){
        Map<String, CatJson> categories =  HelpersJson.getConfig().getCategories();
        String dataReturn = "";

        if(categories !=null){
             dataReturn =ReturnLoop(categories, type, viewtype,0, selected );
          }

        return dataReturn;
    }


    public static String splitInputKey(String inputKey, int key){
        Matcher m = Pattern.compile("\\[([^]]+)\\]").matcher(inputKey);
        List<String> list = new ArrayList<String>();
        while(m.find()) {
            list.add(m.group(1));
        }
        try{
           return list.size()>0 ? list.get(key):"";
        }catch (Exception e){
            return "";
        }

    }

    public static Map<String, Map<String, String>> counvertDoubleTomap(Map<String, String[]> map, String inputName1, String inputName2){
        Map<String, Map<String, String>> newmap = new LinkedHashMap<>();

        for(Map.Entry<String, String[]> entry :map.entrySet()){
            if(entry.getKey().contains(inputName1) || entry.getKey().contains(inputName2)) {
                String inputType = entry.getKey().contains(inputName1) ? inputName1 : inputName2 ;

                if(inputType.contains("specification")){
                    String key1 = splitInputKey(entry.getKey(),0) ;
                     for(int j=0; j<entry.getValue().length;j++) {
                       if(!entry.getValue()[j].isEmpty()) {
                           Map<String, String> map3 = new LinkedHashMap<>();
                           map3.put(key1, entry.getValue()[j]);
                           map3.put("type", inputType);
                           newmap.put(Helpers.randomString(4), map3);
                       }
                     }
                 }else{

                    String key1 = splitInputKey(entry.getKey(),0);
                    String key2 = splitInputKey(entry.getKey(),1);

                    if(newmap.containsKey(key1)){
                        Map<String, String> map3 = new LinkedHashMap<>(newmap.get(key1));
                        map3.put(key2, entry.getValue()[0]);
                        map3.put("type", inputType);
                        newmap.put(key1, map3);
                    }else{
                        Map<String, String> map2 = new LinkedHashMap<>();
                        map2.put(key2, entry.getValue()[0]);
                        newmap.put(key1, map2);
                    }
                }


            }
        }



        Map<String, Map<String, String>> newmap2 = new LinkedHashMap<>();
        Attributes attributes = Helpers.getAttrSettings();
        if(attributes.getFields().size()>0) {
            for (Map.Entry<String, Map<String, String>> entry : newmap.entrySet()) {
                for (Map.Entry<String, String> entry2 : entry.getValue().entrySet()) {
                     if(attributes.getFields().containsKey(entry2.getKey())){
                         Map<String, Lang> sugestion = attributes.getFields().get(entry2.getKey()).getSugestion();
                         String keyRet= Helpers.randomString(4);
                         sugestion.forEach((k, v)->{
                             //if(entry2.getValue().contains(v)){}
                         });
                     }
                }
            }
        }

       return newmap;
    }

    public static Map<String, Map<String, String>> counvertSingle(Map<String, String[]> map, String is){
        Map<String, Map<String, String>> newmap = new LinkedHashMap<>();

        for(Map.Entry<String, String[]> entry :map.entrySet()){
            if(entry.getKey().contains(is)) {

                String key1 = splitInputKey(entry.getKey(), 0);
                String key2 = splitInputKey(entry.getKey(),1);

                if(newmap.containsKey(key1)){
                    Map<String, String> map3 = new LinkedHashMap<>(newmap.get(key1));
                    map3.put(key2, entry.getValue()[0]);
                    newmap.put(key1, map3);
                }else{
                    Map<String, String> map2 = new LinkedHashMap<>();
                    map2.put(key2, entry.getValue()[0]);
                    newmap.put(key1, map2);
                }
            }
        }
        return newmap;
    }

  public static String adminGallery2(String json, String id){
      List<String> js = Helpers.isJSONArrValid(json);
      String returnData = "";
      if (js != null) {
          String baseurl = HelpersJson.getConfig().getMinOptions().getBaseurl();
          for (int i = 0; i < js.size(); i++) {
              String img = js.get(i) != null ? js.get(i)  : "";
              if (js.get(i) != null) {
                  returnData = returnData + "<div class=\"gallery_product\">"
                                          + "<a class=\"delete_image\" href=\"#\" onclick=\"access_url('"+baseurl+"admin/gallery/remove-image/"+id+"/"+img+"/gallery','.show_result_gallery', '.response_gallery', 'none'); return false;\">x</a>"
                                          + "<a href=\"#\"><img src=\""+baseurl+"content/imgproduct/thumb_"+img+"\" ></a>"
                                          + "</div>";
              }
          }
      }
     return returnData+"<div class='clear'></div>";
  }
    public static String adminFiles2(String json, String id){
        JsonObject js = Helpers.isJSONValid(json);

        String returnData = "";
        if (js != null) {
            String baseurl = HelpersJson.getConfig().getMinOptions().getBaseurl();
            for (Map.Entry<String, JsonElement> entry : js.entrySet()) {
                String title = Helpers.returnJsonElement(entry.getValue().getAsJsonObject(), "title");
                String file = Helpers.returnJsonElement(entry.getValue().getAsJsonObject(), "file");

                returnData = returnData + "<div class=\"col-md-5\">"+title+"</div>"
                                        +  "<div class=\"col-md-5\"><a href=\""+baseurl+"content/files/"+file+"\">"+file+"</a></div>"
                                        +  "<div class=\"col-md-2\"><a class=\"fa_delete\" href=\"#\" onclick=\"access_url('"+baseurl+"admin/gallery/remove-image/"+id+"/"+entry.getKey()+"/file','.listOfFiles', '.load_iacon', 'none'); return false;\"></a></div>"
                                        +  "<div class=\"height10px\"></div>";

            }
        }
        return returnData+"<div class='clear'></div>";
    }


    public static String adminGallery(List<Gallery> galleries, String type){
        String baseurl = HelpersJson.getConfig().getMinOptions().getBaseurl();

        String returnData = "";
        if (galleries != null) {
           for(int i=0;i<galleries.size();i++) {
               if (galleries.get(i).getTypefile().contains(type)) {
                   if (type.contains("file")) {
                       returnData = returnData + "<div class=\"col-md-5\">" + galleries.get(i).getTitle() + "</div>"
                               + "<div class=\"col-md-5\"><a href=\"" + baseurl + "content/files/" + galleries.get(i).getDirectory() + "\">" + galleries.get(i).getDirectory() + "</a></div>"
                               + "<div class=\"col-md-2\"><a class=\"fa_delete\" href=\"#\" onclick=\"access_url('" + baseurl + "admin/gallery/remove-image/" + galleries.get(i).getId() + "/" + galleries.get(i).getProductid() + "/file','.listOfFiles', '.load_iacon', 'none'); return false;\"></a></div>"
                               + "<div class=\"height10px\"></div>";
                   } else {
                       returnData = returnData + "<div class=\"gallery_product\">"
                               + "<a class=\"delete_image\" href=\"#\" onclick=\"access_url('" + baseurl + "admin/gallery/remove-image/" + galleries.get(i).getId() +"/" + galleries.get(i).getProductid() + "/gallery','.show_result_gallery', '.response_gallery', 'none'); return false;\">x</a>"
                               + "<a href=\"#\"><img src=\"" + baseurl + "content/imgproduct/thumb_" + galleries.get(i).getDirectory() + "\" ></a>"
                               + "</div>";
                   }
               }
           }
         }
        return returnData+"<div class='clear'></div>";
    }


    public static String attrNewKey(){
        Attributes attributes = Helpers.getAttrSettings();
        String keyNew = "field15";
        for (int i = 1; i < 46 ; i++) {
            if(!attributes.getFields().containsKey("field"+i)){
                keyNew =  "field"+i;
                break;
            }
        }
        System.out.println("keys"+ keyNew);
        return keyNew ;
    }

  public static LinkedHashMap<String, String> checkoutFields(String field){
       String json = HelpersFile.readFile("content/config/biling_shipping/"+field+".json");
       if(json.isEmpty())
           return null;

      Type type = new TypeToken<LinkedHashMap<String, String>>(){}.getType();
       return  (new Gson()).fromJson(json, type);
  }

  public static LinkedHashMap<String, FieldsJson> checkoutFieldsDb(String json){

           if(json.isEmpty() || json == null)
                    return new LinkedHashMap<String, FieldsJson>(){};

             try{
                 Type type = new TypeToken<LinkedHashMap<String, FieldsJson>>(){}.getType();
                 return  (new Gson()).fromJson(json, type);
             }catch (Exception e){
                 return new LinkedHashMap<String, FieldsJson>(){};
             }

    }


}
