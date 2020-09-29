package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The MIT License (MIT)
 *Copyright (c) Corneli Frunze
 */
public class Admin {

     @Autowired
     private Configuration configuration;

     private boolean checkMap(Map<String, String> map , String uri){
         boolean yes = false;
         for (Map.Entry<String, String> entry : map.entrySet()) {
             String menu =entry.getKey();
             if(menu.contains("?")){
                String[] split =menu.split("\\?");
                 menu = split[0];
              }
             if(menu.equals(uri)){
                  yes = true;
                  break;
              }
         }
         return yes;
     }


    public String adminmenu(String type, String baseurl){
        LinkedHashMap<String, LinkedHashMap<String,String>> topMenu = Helpers.getSubmenu("content/config/adminconfig.xml", "menuittem");
        LinkedHashMap<String, LinkedHashMap<String,String>> topTheme = Helpers.getSubmenu("content/views/themes/"+Helpers.getConfig().getTheme()+"/config/config.xml", "menuittem");
        Map<String, LinkedHashMap<String,String>> finalmenu =new TreeMap<>();
        try {
              finalmenu =   new TreeMap<>(topTheme ==null ? topMenu : Helpers.mergeMap(topMenu, topTheme));
            //Map<String, LinkedHashMap<String,String>> finalmenu =
        }catch (Exception e){
            System.out.println("Menu  errro"+e);
        }


        // this is for submenu it show only
        String submenu=  "";

        if(finalmenu !=null && finalmenu.size()>0) {
            String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
            String domain = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
            uri = uri.replace(domain,"").substring(1);
            //System.out.println("dddddddddddd:"+uri);
          for (Map.Entry<String, LinkedHashMap<String,String>> entry : finalmenu.entrySet()) {
                //System.out.println("entryK:" + entry.getValue());
                 if(entry.getValue() !=null ){
                     if(checkMap(entry.getValue() , uri) ) {
                         for (Map.Entry<String, String> entry2 : entry.getValue().entrySet()) {
                             if(!entry2.getKey().contains("#") && !entry2.getValue().contains("hidden"))
                                submenu = submenu + "<li> <a href=\"" + baseurl + entry2.getKey() + "\">" + entry2.getValue() + "</a> </li>";
                          }
                     }
                }
             }
          }


        try{
            Template template =configuration.getTemplate(Helpers.view("admin::layouts/adminmenu","view"));
            Map<String, Object> model = new Hashtable<>();
            //model.put("view", new ViewHelper());

            model.put("checkMap", this);
            model.put("menu", finalmenu);
            model.put("baseurl", baseurl);
            return type.contains("full")? FreeMarkerTemplateUtils.processTemplateIntoString(template,  model): submenu;
            /*
            Context context = new Context();
            context.setVariable("menu", finalmenu);
            context.setVariable("baseurl", baseurl);
            return type.contains("full")? templateEngine.process("cms/admin/layouts/adminmenu", context): submenu;

             */

        }catch (Exception e){System.out.println("Errdd:"+e);  }



        return "";
    }


    public String firstElemMap(Map<String,String> map, String val){
        if(map == null)
            return "";

        Map.Entry<String,String> entry = map.entrySet().iterator().next();
        return  val.contains("key")? entry.getKey():  entry.getValue();
    }




}
