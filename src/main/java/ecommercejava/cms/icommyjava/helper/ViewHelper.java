package ecommercejava.cms.icommyjava.helper;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Attributes.ProductVariations;
import ecommercejava.cms.icommyjava.dto.Config.CatJson;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.dto.Config.MainOptions;
import ecommercejava.cms.icommyjava.dto.billingshipping.FieldsJson;
import ecommercejava.cms.icommyjava.entity.*;
import ecommercejava.cms.icommyjava.services.DoShortCode;
import ecommercejava.cms.icommyjava.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ViewHelper {

    @Autowired
    DoShortCode doShortCode;


    public String breadcrumbs(String title){
        try {

            String url = ServletUriComponentsBuilder.fromCurrentRequest().toUriString().replace("http://","").replace("https://","");

            if(url.contains("\\?")){
                String[] splitRew = url.split("\\?");
                url= splitRew[0];
            }

            String[] split = url.split("/");
            Map<String, CatJson> map = Helpers.getConfig().getCategories();
            String baseurl = Helpers.baseurl();
            String data = "<ul class=\"breadcrumbNav\"><li><a href=\""+baseurl+"\">"+Helpers.translate("Home")+"</a></li>";

             boolean yesAddtitle = false;
            for(int i=0; i < split.length;i++){
                if(i>1){
                    for(Map.Entry<String, CatJson> entry: map.entrySet()){
                         if(entry.getValue().getCpu().compareTo(split[i])==0){
                             data = data+ "<li><a href=\""+baseurl+"cat/"+entry.getValue().getCpufull()+"\">"+entry.getValue().getTitle()+"</a></li>";
                             if(entry.getValue().getTitle().compareTo(title) ==0)
                                 yesAddtitle = true;
                         }
                    }
                }
             }
            data =  title !=null && !yesAddtitle ? data+  "<li><a href=\"#\">"+title+"</a></li>":data;

            data = data+ "</ul>";
            return data;
       }catch (Exception e){
        }
         return "";
    }


    /**
     * Translate simple words
      * @param text
     * @return
     */
    public String l(String text) {
       return  Helpers.translate(text);
    }

    /**
     * Translate simple words
     * @param text
     * @return
     */
    public String lang(String text, String lang) {
        return Language.getTranslateByLang(text, lang);
    }
    /**
     * return base url
     * @return
     */

    public String baseurl(){
      return Helpers.getConfig().getMinOptions().getBaseurl();
    }

    /**
     * Convert to shortCode
      * @param text
     * @return
     */
    public String shortcode(String text){
       //DoShortCode newShort = new DoShortCode();
       return doShortCode.doshortcodes(text);
    }


    /**
     * EditBlock will split the css and content in database they are  css~~~~~page content
     * @param conent
     * @param returnTyep
     * @return
     */
    public String editableBlocks(String conent, String returnTyep){
        String page = conent;
        String csstype = "";

        try {
            if (conent.contains("~~~~~")) {
                String[] split = conent.split("~~~~~");
                page = split[1];
                csstype = split[0];
            }
        }catch (Exception e){}

        return returnTyep == "content" ? page : Helpers.generateCss(csstype,"");

      }


    /**
     * will generate the product url
     * @param cpu
     * @param cat
     * @return
     */
    public String prurl(String cpu, String cat ) {
        return Helpers.productUrl(cpu, cat );
    }

    /**
     *  will generate add to cart btn
     * @param attr
     * @param classcss
     * @param prid
     * @param qtu
     * @return
     */
    public String addToCartBtn(Map<String, ProductVariations> attr, String classcss, Integer prid, Integer qtu) {
        return Helpers.addToCartBtn(attr, classcss, prid, qtu);
    }

    public String viewProductOnclick(Integer prid ) {
        return " onclick=\"modaljs(this,'show','ajax');return false;\" data-modal=\"#mini-modal\" data-url=\""+Helpers.baseurl()+"product/url/"+prid+"?type=mini\" ";
    }

    /**
     * will generate path to html by theme::login (themes/activetheme/auth/login.html)  or admin::product and return full path like
     * @param text
     * @return
     */
    public String htmlPath(String text) {
        return Helpers.view(text,"view");
    }

    /**
     * return the path to image product
     * @param image
     * @param type
     * @return
     */
    public String imgproduct(String image, String type) {
        return Helpers.imageProduct(image, type);
    }


    /**
     * will return the full product gallery html
     * @param galleries
     * @param mainimg
     * @return
     */

    public String gallery(List<Gallery> galleries, String mainimg){
        return Helpers.getGallery(galleries, mainimg);
    }

    /**
     * Get rating of product comments
     * @param comments
     * @return
     */
    public String countStars(List<Comments> comments){  return Helpers.startsQtu(comments);}


    /**
     * will return admin menu
     * @param type
     * @return
     */
    public String adminMenu(String type ){
        return Helpers.adminmenu(type);
    }

    /**
     * will return categories list in format you need for exam
     * @param type
     * @param viewtype = chekbox | select
     * @param selected = json with selected categories like ["21","22"] where 21 or 22 is cat id
     * @return
     */
    public String catsructure(String type, String viewtype, String selected){
        return Helpers.catStructure(type, viewtype, selected);
    }

    /**
     * will generate random string
     * @param nr= integer nr like 10 or 3
     * @return  REgrgSV4342vSDFBst454
     */
    public String rand(int nr){
        return Helpers.randomString(nr);
    }

    /**
     * Will return the html with swich languages inadmin
     * @return
     */
    public String adminSwitchLang(){
        return Helpers.adminLanguage();
    }

    /**
     * will return current lang
      * @return en OR it OR gb
     */
    public String currentLang(){
        return Helpers.currentLang();
    }

    /**
     * will clear json it came in format like "text"
      * @param txt
     * @return your string
     */
    public String clearJson(String txt){
        return Helpers.clearJson(txt);
    }

    /**
     * will return the lang in List array format
     * @return
     */
    public List<String> listLang(){
        return Helpers.listLang();
    }

    /**
     * will return the lang in List array format
     * @return
     */
    public String comaLang(){
        return String.join(",", Helpers.listLang()) ;
    }

    /**
     * will check if user is autentificated
     * @return true | false
     */
    public boolean isAuthenticated(){
        return AuthenticationSystem.isLogged();
    }

    /**
     * return role
     * @param roles
     * @return
     */
    public String returnRole(Collection<Role> roles){
        String ret="Subscriber";
        if(roles !=null){
            for (Role role : roles){
               if(role.getName().contains("ROLE_ADMIN")){
                   ret="Admin";
                   break;
               }

               if(role.getName().contains("ROLE_EDITOR")){
                    ret="Editor";
                    break;
                }

                if(role.getName().contains("ROLE_CONTRIBUTOR")){
                    ret="Contributor";
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * this will return all the data from content/config/config.json in fromat config
     * you can access any parametr by view.getConfig().getMinOptions().getSite_title();
     * @return  view.getConfig()
     */
    public Config getConfig(){
        return HelpersJson.getConfig();
    }

    /**
     * this will return the mainopttions from the json file
     *  use
     * @return view.mainOptions()
     */
    public MainOptions mainOptions(){
        return getConfig().getMinOptions();
    }

    /**
     * Will return the logo in string format
     * @return
     */
    public String logo(){
      return "<a href='"+mainOptions().getBaseurl()+"'>" +
             "<img class='logo loading' src='"+mainOptions().getBaseurl()+"content/images/"+mainOptions().getLogo()+"'></a>";
    }

    /**
     * Will return the path to favicon
     * @return
     */
    public String favicon(){
        return mainOptions().getBaseurl()+"content/images/"+mainOptions().getFavicon();
    }


    public String menu(String id){
        return mainOptions().getBaseurl()+"content/images/"+mainOptions().getFavicon();
    }



    public String adminGallery(List<Gallery> galleries, String type){
        return galleries == null ? "": Helpers.adminGallery(galleries, type);
    }



    /**
     *  will return the key of sugestion
     * @param attr
     * @param field
     * @return
     */
    public static String getKeyFromProductAttr (Map<String, ProductVariations> attr, String field ){
        return Helpers.getKeyFromProductAttr (attr,field);
    }


    public boolean checkIfIsInAttrProduct(Map<String, ProductVariations> attr, String field, String keySugestion ){
        return Helpers.checkIfIsInAttrProduct(attr, field, keySugestion );
    }

    public String getSugestionById(Map<String, Lang> sugestion, String keySugestion, String lang){
         return keySugestion !=null && sugestion !=null ? (sugestion.containsKey(keySugestion) ? sugestion.get(keySugestion).get(lang): "") : "";
    }

    public String getOptionTitle(Map<String, Map<String, String>> attr, String idOption){
        return CartCheckout.getOptionTitle(attr, idOption);
    }

    public Float calcDiscount(Cupon cupon, Float total, String type){
        Float discount = cupon.getType().contains("percent") ? ((total * cupon.getAmount()) / 100) : cupon.getAmount();

        return type.contains("total") ? total - discount : discount;

    }

    /**
     * convert the price in $20.20 and will return the sale price if is set
     * @param price
     * @param sale_price
     * @return
     */
    public String price(Float price, Float sale_price ){
        return Helpers.price(price, sale_price);
    }

    /**
     * will return the right translate all the languages are set in json format like  {"it":"Title it","en":"Title en"}
     * @param json
     * @return
     */
    public String splitLang(String json){
        return Helpers.splitLang(json);
    }

    /**
     * will convert json format to map
     * @param json
     * @return
     */
    public LinkedHashMap<String, FieldsJson> checkoutFields(String json){
        return  AdminHelpers.checkoutFieldsDb( json );
    }

}
