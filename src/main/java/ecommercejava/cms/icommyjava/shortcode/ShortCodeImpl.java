package ecommercejava.cms.icommyjava.shortcode;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Attributes.Attributes;
import ecommercejava.cms.icommyjava.dto.Attributes.AttributesVar;
import ecommercejava.cms.icommyjava.dto.Config.CatJson;
import ecommercejava.cms.icommyjava.dto.Config.Currency;
import ecommercejava.cms.icommyjava.dto.Config.Menu;
import ecommercejava.cms.icommyjava.dto.contactform.Contactform;
import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.entity.Product;
import ecommercejava.cms.icommyjava.entity.Settings;

import ecommercejava.cms.icommyjava.helper.*;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import ecommercejava.cms.icommyjava.services.ProductService;
import ecommercejava.cms.icommyjava.services.SettingsService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
public class ShortCodeImpl extends MainController implements ShortCode{

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    @Autowired
    private ProductService productService;

    @Autowired
    private SettingsService settingsService;

    /**
     * This method will be called each time when found a short code
     * @param code
     * @param map
     * @return
     */
    public String shortCodes(String code, Map<String,String> map){
       String data = null;
        switch (code) {
            case "search":
                data = search(map);
                break;
            case "productfromcategory":
                data = productfromcategory(map);
                break;
            case "products":
                data = products(map);
                break;
            case "categorieslist":
                data = categorieslist(map);
                break;
            case "subcategories":
                data = subcategories(map);
                break;
            case "attributes":
                data = attributes(map);
                break;
            case "menu":
                data = menu(map);
                break;
            case "menulogged":
                data = menulogged(map);
                break;
            case "cart":
                data = cart(map);
                break;
            case "currency":
                data = currency();
                break;
            case "language":
                data = language(map);
                break;
            case "pricesort":
                data = pricesort();
                break;
            case "subscribe":
                data = subscribe();
                break;
            case "form":
                data = contactform(map);
                break;
            case "button":
                data = button(map);
                break;
         }
         return  data;
    }





    /**
     *  Will compile html and return data in string format
     * @param templateModel
     * @param htmlpath
     * @return = html compiled
     */
    public String compileHtml(Map<String, Object> templateModel, String htmlpath){
        try{
            Template freemarkerTemplate = freemarkerConfigurer.createConfiguration()
                    .getTemplate(Helpers.view(htmlpath, "view"));
            templateModel.put("view", new ViewHelper());
            templateModel.put("baseurl",Helpers.baseurl());
            return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
        }catch (Exception e){System.out.println("Err template:"+e);  }
        return "";
    }



    private String contactform(Map<String,String> variables){

        if( variables.containsKey("id")) {
            String id = variables.get("id");
            Settings settings = settingsService.getOne(Integer.parseInt(id));
           if(settings !=null) {
               Map<String, Object> templateModel = new HashMap<>();
               try {
                   templateModel.put("form", (new Gson()).fromJson(settings.getValue1(), Contactform.class));
                   templateModel.put("id", id);
                   return compileHtml(templateModel, "cms::default/contactForm");
               }catch (Exception e){ }
           }

         }
         return "";
      }


    /**
     * will show the subscribe form
     * Short code use [subscribe]
     * @return
     */
    private String subscribe(){
         Map<String, Object> templateModel = new HashMap<>();
         return  compileHtml(templateModel, "front::default/subscribe");
    }


    /**
     * Atributes short code // [attributes id=field1 catid=37]
     * @param variables
     * @return
     */

    private String attributes(Map<String,String> variables){
        String id = variables.containsKey("id")? variables.get("id"): "";
        String catid = variables.containsKey("catid")? variables.get("catid"): "";

        String url = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();

       if(!catid.isEmpty()) {
           Map<String, CatJson> cats = new HashMap<>(Helpers.getConfig().getCategories());
           if (cats.containsKey(catid)) {
               if (!url.contains(cats.get(catid).getCpu())) {
                   return "";
               }
           }
       }

        Attributes attributes = Helpers.getAttrSettings();

                   String data ="";

                for(Map.Entry<String, AttributesVar> entry :  attributes.getFields().entrySet()) {
                    String newid= id.isEmpty() ? entry.getKey() : id;

                    if(newid.contains(entry.getKey())) {
                        data = data + "<div class=\"sidebarBlock\"> <div class=\"titleVariation\">" + entry.getValue().getNameTranslated() + "</div> <div class=\"variationsList\">";

                        for (Map.Entry<String, Lang> entry2 : entry.getValue().getSugestion().entrySet()) {
                            data = data + "<label> <input type=\"checkbox\"" + (url.contains(entry2.getKey()) ? "checked" : "") +
                                    "                     value=\"\" onclick=\"optionsShowProducts(this,'" + entry2.getKey() + "','" + entry.getKey() + "');\"/> " + entry2.getValue().get(Language.currentLang()) + "</label>";
                        }

                        data = data + "</div> </div>";
                    }
                }



        return data;
    }


    /**
     *  Add to cart Short code, it allow you to add produc to cart by using product ID
     *  [cart title=Add to cart id=21 redirect=cart]
     *  redirect=cart  - it's mean after add to cart will redirect to cart Page
     *  redirect=checkout  - it's mean after add to cart will redirect to checkout Page
     */

    private String  cart(Map<String,String> variables) {
        String id = variables.containsKey("id")? variables.get("id"): "";
        String title = variables.containsKey("title")? variables.get("title"): "";
        String url = Helpers.baseurl();
        String redirect = variables.containsKey("redirect")? (variables.get("redirect").contains("cart")? "&redirect=/cart/get" : "&redirect=/checkout/step1") : "";
        String random = "cl"+Helpers.randomString(10);
        return  "<a href=\"#\" class=\"btn btn-big\" onclick=\"simplePost('"+url+"cart/add', '.load_content_smallcart', 'action=AddToCart&id="+id+"&qtu=1&max_qtu=200&oneQty=yes"+redirect+"&type=smallCart', '', '."+random+"'); return false;\">\n" +
                "  <span class=\""+random+"\">"+title+"</span>\n" +
                "</a>";
    }

    /**
     * Get last products by default will take last 8
     * [products count=10]
     * @param variables
     * @return
     */
     private String products(Map<String,String> variables){
         int count = variables.containsKey("count")? Integer.parseInt(variables.get("count")): 8;
         Pageable pageable = PageRequest.of(0, count);
         Page<Product> products = productService.findAll(pageable);
         String view = Helpers.viewExist("theme::layouts/product")  ? "theme::layouts/product" :  "front::product/product";
         Map<String, Object> templateModel = new HashMap<>();
         templateModel.put("products",  products);
         templateModel.put("htmlpath", Helpers.view(view, "view"));
         return  compileHtml(templateModel, "front::product/product-shortcode");
     }

    /**
     * get last products from category by default will take last 8
     * [productfromcategory idcat=12 count=8]
     * @param variables
     * @return
     */
    private String productfromcategory(Map<String,String> variables){
        String id = variables.containsKey("idcat")? variables.get("idcat"): "";
        int count = variables.containsKey("count")? Integer.parseInt(variables.get("count")): 8;

        Pageable pageable = PageRequest.of(0, count);
        Page<Product> products = productService.getByCatid(id, pageable);
        String view = Helpers.viewExist("theme::layouts/product")  ? "theme::layouts/product" :  "front::product/product";
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("products",  products);
        templateModel.put("htmlpath", Helpers.view(view, "view"));


        return  compileHtml(templateModel, "front::product/product-shortcode");
      }

    /**
     * get the currencies switch btn
     * @return
     */
    protected String currency(){
        Map<String, Object> templateModel = new HashMap<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, Currency> currencies = Helpers.getConfig().getCurrencies();
         String cookie = Cookies.get("currency", request);
         String [] sign = {cookie};
        if(currencies.size()>0 && cookie.isEmpty()){
            currencies.forEach((k, v) ->{ if(v.getMain()){sign[0]=k;}  });
        }


        templateModel.put("seted", sign[0]);
        templateModel.put("curency",   currencies);
        return  compileHtml(templateModel, "front::default/currency-switch-button");
    }

    /**
     * Menu Short code
     * [menu id=2 class=topMenu align=center]
     */
    protected String menu(Map<String,String> variables){
        String id = variables.containsKey("id") ? variables.get("id"): "0" ;
        String className = variables.containsKey("class") ? variables.get("class"): "" ;
        String align = variables.containsKey("align") ? " center-text": "" ;

        Settings settings = settingsService.getOne(Integer.parseInt(id));
        String rand = Helpers.randomString(10);
        return settings ==null ? "":
                        "<div class=\"container_menu_icon\" onclick=\"menu_show_is(this,'.cl"+rand+" ."+className+"')\"><div class=\"bar1\"></div><div class=\"bar2\"></div><div class=\"bar3\"></div> </div>" +
                        "<ul class=\"ulmenu "+className+align+" cl"+rand+" \">"+settings.getValue1Lang()+"</ul>"  ;
    }

    protected String menulogged(Map<String,String> variables){
        String  idin= variables.containsKey("idin") ? variables.get("idin"): "" ;
        String  idnot= variables.containsKey("idnot") ? variables.get("idnot"): "" ;
        Map<String,String> vids = new HashMap<>();
        if(AuthenticationSystem.isLogged()){
              vids.put("id",idin);
             return !idin.isEmpty()?  menu(vids):"";
        }else{
              vids.put("id",idnot);
            return !idnot.isEmpty()?  menu(vids):"";
        }
     }


    /**
     * this will show the list of categories or subcategories
     * @param variables
     * @return
     */
    protected String subcategories(Map<String,String> variables){
       String url= ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
       String mainCat= "";
        int parrentId=0;
       try {
           String[] split = url.split("/");
           Map<String, CatJson> map = Helpers.getConfig().getCategories();
           for (Map.Entry<String, CatJson> entry : map.entrySet()) {
               if (entry.getValue().getCpu().contains(split[split.length-1])  ) {
                   mainCat = entry.getKey();
                   parrentId= entry.getValue().getParent();
                   break;
               }
           }

           boolean haveChild =false;
           if(!mainCat.isEmpty()){
               int catId = Integer.parseInt(mainCat);
               for (Map.Entry<String, CatJson> entry : map.entrySet()) {
                     if(entry.getValue().getParent()==catId){
                         haveChild =true; break;
                     }
                 }
           }
           mainCat = haveChild? mainCat : Integer.toString(parrentId);

       }catch (Exception e){  }

       if(!mainCat.isEmpty())
           variables.put("maincatid", mainCat);

        return categorieslist(variables);
}

    /**
     * will show only the list of main categoreis
     * @param variables
     * @return
     */
    protected String categorieslist(Map<String,String> variables ){
       Map<String, CatJson> map = Helpers.getConfig().getCategories();
       String type = variables.containsKey("type") ? variables.get("type"): "product" ;
       String title= !variables.containsKey("maincatid") ? Helpers.translate("Categories") :
                      (map.containsKey(variables.get("maincatid")) ? map.get(variables.get("maincatid")).getTitle() : "" );

       String baseUrl = Helpers.baseurl();
       String url=type.contains("product")? baseUrl+"cat/" :baseUrl+"blog/" ;

 String data= "<div class=\"sidebar-categories sidebarBlock\">" +
                "<h2 class=\"catTitleLeft\">"+title+"</h2>" +
                 "<ul class=\"categoriesList\">";

                             for( Map.Entry<String, CatJson> entry: map.entrySet()){
                                 CatJson val = entry.getValue();
                                 int parrentCat =  !variables.containsKey("maincatid") ? 0 : Integer.parseInt(variables.get("maincatid"));
                                 if(val.getType().contains(type) && parrentCat == val.getParent()){
                                     data = data + "<li><a href=\""+url+val.getCpufull()+"\">"+val.getTitle()+"</a></li>";
                                  }
                             }
   data =data + "</ul>" +
             " </div>";

        return data ;
}

    /**
     * will show the form with price minim and price maxim
     * @return
     */
    public String pricesort(){
    Map<String, Object> templateModel = new HashMap<>();
    //templateModel.put("view",  new ViewHelper());
     return  compileHtml(templateModel, "front::product/pricesort");
  }

    /**
     * will show the search form
     * @param map
     * @return
     */
    public String search(Map map){
        String title = map.containsKey("title")? (String) map.get("title") : "";
        String type  = map.containsKey("type") ? (String) map.get("type") : "products";
        String url = type == "posts" ?   "/search/posts" : "/cat/search";
        String cat = map.containsKey("cat")? (String) map.get("cat") : "";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String search = request.getParameter("search") != null ? request.getParameter("search") : "";
        String catid = request.getParameter("catid") != null ? request.getParameter("catid") : "";

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("url", url);
        templateModel.put("search", search);
        templateModel.put("catid", search);
        templateModel.put("title", title);

        return  compileHtml(templateModel, "front::default/searchForm");

    }

    /**
     * Simple nice Button
     *  [button url=# title=SHOP NOW color=black]
     */

    public String button(Map map) {
        String title = map.containsKey("title")? (String) map.get("title") : "";
        String color  = map.containsKey("color") ? (String) map.get("color") : "white";
        String url = map.containsKey("url") ? (String) map.get("url") : "#";
        String style = !color.contains("white") ?  "style=\"color:"+color+"; border-color:"+color+";" : "";

        return "<a class=\"niceButton\" "+style+" href=\""+url+"\">"+title+"</a>";
    }

    /**
     * will show the language switch
     * @param map
     * @return
     */
    private String language(Map map){
        String type = map.containsKey("type")? (String) map.get("type") : "icon";
        String data ="";
        List<String> arrayLang = Helpers.listLang();

           if(arrayLang.size()>1) {
               data ="<div class=\"lang-switch\">";
               String baseurl = Helpers.baseurl();
               Map<String,String> langListmap = new HashMap<>();
               if(type.contains("full")){
                     langListmap = HelpersFile.getConfigXmlData("config", "languages", "content/config/lang.xml");
               }

               for (int i = 0; i < arrayLang.size(); i++) {

                  data = data+ "<a href = \""+baseurl+"set-lang/"+arrayLang.get(i)+"\" >\n" +
                                "<img src=\""+baseurl+"content/views/assets/langicon/"+arrayLang.get(i).toUpperCase()+".png\"/>";
                    if(type.contains("short")){
                        data = data+" "+arrayLang.get(i).toUpperCase();
                    }else if(type.contains("full")){
                        data = data+" "+(langListmap.containsKey(arrayLang.get(i)) ? langListmap.get(arrayLang.get(i)) : arrayLang.get(i));
                    }

                  data = data+"</a>";
               }


               data =data + "</div>";
           }
        return  data;
    }


}
