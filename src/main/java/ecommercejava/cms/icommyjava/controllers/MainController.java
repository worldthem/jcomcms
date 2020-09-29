/**
 * The MIT License (MIT)
 * Copyright (c) Corneli Frunze
 */
package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Config.Menu;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.*;
import ecommercejava.cms.icommyjava.services.SettingsService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController extends Admin {

    @Autowired
    private SettingsService settingsRepository;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    private Map<String, String> settings;
    public static HashMap<String, String> allSettins = null;

/*
    private Cookie[] cookies =  null;
    @GetMapping("/")
    public void readCookie(HttpServletRequest request) {
        //get all cookies
        cookies = request.getCookies();
        System.out.println("cookie are this");
    }

    public String getCookie(String name){
        if(cookies !=null) {
            String data ="";
            for (Cookie cookie : cookies) {
                //display only the cookie with the name 'website'
                if (cookie.getName().equals( name)) {
                    data =cookie.getValue();
                    break;
                }
            }
            return data;
        }
        return "";
    }

 */

  protected String getCartUserId(HttpServletRequest request, HttpServletResponse response){
    String cookie = Cookies.get("useridtmp", request);
    String useridtmp = cookie.isEmpty()? Helpers.randomString(40) : cookie;

    if(cookie.isEmpty())
        Cookies.set("useridtmp", useridtmp, response);

    return useridtmp;

}


   public Map prepareData(){
      HashMap<String, String> map = new HashMap<String, String>();


       String configThemePath= "content/views/themes/"+Helpers.getConfig().getTheme()+"/config/config.xml";

       if(Helpers.fileExist(configThemePath)){
           Map<String, String> ConfigData =  HelpersFile.getConfigXmlData("config","hooks", configThemePath);
           for(Map.Entry<String, String> entry2: ConfigData.entrySet()){
               map.put(entry2.getKey()+"_hook", entry2.getValue());
           }
       }

       List<String> list = new ArrayList<>();
       list.add("_header_");  list.add("_footer_"); list.add("_shop_sidebar_");  list.add("_footerjs_");


       List<Settings> settings = settingsRepository.findByParamInOrderByIdDesc(list);

        for (Settings row:settings){
            if(row.getParam().contains("_footerjs_")){
                map.put(row.getParam(), row.getValue1());
            }else {
                map.put(row.getParam(), row.getValue1Lang());
                map.put(row.getParam() + "css", row.getValue());
            }
        }

       //Menu
        List<Settings> settingsMenu = settingsRepository.findByParam("website_menu");
        if(settingsMenu !=null) {
            for (Settings rowMenu : settingsMenu) {
                if (rowMenu.getValue2() != null) {
                    map.put(rowMenu.getValue2(), rowMenu.getValue1Lang());
                }
                map.put("menuId" + rowMenu.getId(), rowMenu.getValue1Lang());
            }
        }


     //Website URL
       String url = HelpersJson.getConfig().getMinOptions().getBaseurl();
       map.put("baseurl", url);

       map.put("themeassets", url+"content/views/themes/"+ HelpersJson.getConfig().getTheme()+"/assets");

       String themePath= "/themes/"+ HelpersJson.getConfig().getTheme();
       map.put("themepath",  themePath);

       map.put("productpath", Helpers.fileExist("content/views"+themePath+"/layouts/product.html") ?
                                                  themePath+"/layouts/product.html" :"/cms/standart/product/product.html" );

       map.put("adminassets", url+"content/views/assets/admin");
       map.put("frontassets", url+"content/views/assets/front");
       map.put("assets", url+"content/views/assets");
       map.put("adminmenu", adminmenu("full", url));
       map.put("adminmenusubmenu", adminmenu("submenu", url));

       return map;
    }

   private ModelAndView setvarView(){
       ModelAndView mav = new ModelAndView();
       Map <String, String> map = prepareData();

       for (Map.Entry<String, String> entry : map.entrySet()) {
         mav.addObject(entry.getKey(), entry.getValue());
       }
      return mav;
   }

   public Pageable pagination(HttpServletRequest request, Long total, Model model, Integer perpage){
       int page = 0; //default page number is 0 (yes it is weird)
       int size = perpage; //default page size is 10

       if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
           page = Integer.parseInt(request.getParameter("page")) - 1;
       }

       if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
           size = Integer.parseInt(request.getParameter("size"));
       }

       float devide = ((float)total) / ((float)size);
       double dDouble = devide>0? Math.ceil(devide ) : 0;

       long totalpages = (long) dDouble;

        model.addAttribute("totalPages", totalpages);
       model.addAttribute("totalittems", total);
       model.addAttribute("pagenumber", page );

       model.addAttribute("urlpagination",  request.getRequestURI() );


       return PageRequest.of(page, size);

   }


    public ModelAndView view(String viewName ){
        ModelAndView mav = setvarView();
        String viewDefault = Helpers.view(viewName, "default");
               viewDefault =!AuthenticationSystem.isLogged() &&
                             Helpers.getConfig().getMinOptions().getWebsiteonmaintenance() !=null  &&
                             viewName.contains("theme::") ? "/cms/standart/default/maintenance": viewDefault;


        try {
            String path = Helpers.view(viewName, "view");
            mav.addObject("viewname", path);
            //mav.addObject("view", new ViewHelper());
            mav.setViewName(viewDefault);

            return mav;
        }catch (Exception e){
            mav.addObject("viewname", "");
            mav.setViewName(viewDefault);
            return mav;
        }
    }


    public ModelAndView viewpart(String viewName){
        String path = Helpers.view(viewName, "nohtml");
        ModelAndView mav = setvarView();
        mav.addObject("viewname", path);
        mav.setViewName(path);
        return mav;
    }

    public String viewpatNohtml(String viewName){
          return Helpers.view(viewName, "nohtml");
    }

    public String splitLang(String json){
       return Helpers.splitLang(json);
   }

    /**
     *  Will compile html and return data in string format
     * @param templateModel
     * @param htmlpath
     * @return = html compiled
     */
    public String templateHtml(Map<String, Object> templateModel, String htmlpath){
        try{
            Template freemarkerTemplate = freemarkerConfigurer.createConfiguration()
                    .getTemplate(Helpers.view(htmlpath, "view"));
            templateModel.put("view", new ViewHelper());
            templateModel.put("baseurl",Helpers.baseurl());
             return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        }catch (Exception e){System.out.println("Err:"+e);  }
        return "";
    }

}
