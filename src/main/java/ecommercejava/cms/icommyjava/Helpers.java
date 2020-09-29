package ecommercejava.cms.icommyjava;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ecommercejava.cms.icommyjava.dto.Attributes.ProductVariations;
import ecommercejava.cms.icommyjava.dto.Config.CatJson;
import ecommercejava.cms.icommyjava.dto.Styledto;
import ecommercejava.cms.icommyjava.entity.Comments;
import ecommercejava.cms.icommyjava.entity.Gallery;
import ecommercejava.cms.icommyjava.helper.Cookies;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.HelpersJson;
import ecommercejava.cms.icommyjava.helper.Language;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ecommercejava.cms.icommyjava.dto.Config.Currency;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class Helpers extends Language {


public static String baseurl(){
    return HelpersJson.getConfig().getMinOptions().getBaseurl();
}


public static String translate(String text){
      String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
      String lang = Language.getLangFromUrl();

      if(uri.contains("/admin")) {
          lang = getConfig().getAdminlang();
      }

      return  Language.getTranslateByLang(text, lang);
  }


  public static String view(String viewName, String type){
        String path="/cms/standart/"+viewName+".html";
        String viewDefault = "/cms/standart/default";
        if(viewName.contains("::")) {
            String[] split = viewName.split("::");
            if(split[0].contains("admin")){
                path="/cms/admin/"+split[1]+".html";
                viewDefault = "/cms/admin/default";
            }else if(split[0].contains("theme")){
                path="/themes/"+  Helpers.getConfig().getTheme()+"/"+split[1]+".html";
                viewDefault = "/themes/"+  Helpers.getConfig().getTheme()+"/default";
            }else if(split[0].contains("payment")){
                path="/payment/"+split[1]+".html";
            }else{
                path="/cms/standart/"+split[1]+".html";
                viewDefault = "/cms/standart/default";
            }
        }
       return type.contains("default") ? viewDefault : type.contains("nohtml") ? path.replace(".html",""): path;
    }

    public static boolean viewExist(String view){
       String path = "content/views"+view(view, "html");
        File file = new File(HelpersFile.pathToFile(path));
        if (!file.exists()) { //&& !file.mkdirs()
            return false;
        }
        return true;
    }


    /**
     * will generate random string from numbers and letters
     * @param nr
     * @return
     */

    public static String randomString(int nr){
        String[] arr = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","r","s","t","u","v","x","y","z","A","B","C","D","E","F", "G","H","I","J","K","L","M","N","O","P","R","S","T","U","V","X","Y","Z","1","2","3","4","5","6","7","8","9","0"};
        String newRand = "";
        for(int i = 0; i <nr;  i++){
            int rnd = new Random().nextInt(arr.length);
            newRand = newRand + arr[rnd];
        }
        return newRand;
    }




    private static String checkEmpty(String val3 ,  String before, String after){
         return val3 !=null ? (!val3.isEmpty() ?  before + val3 + after :"") : "";
    }

    private static String checkDefault(String val3, String defaultData ){
        return val3 !=null ? (!val3.isEmpty() ?  val3 :defaultData) :defaultData;
    }


   public static String generateCss(String style, String css){
        Map<String, Styledto> map = new HashMap<>();
        try{
             if(!style.isEmpty()) {
                 Type type = new TypeToken<Map<String, Styledto>>() {}.getType();
                 map = (new Gson()).fromJson(style, type);
             }
         }catch (Exception e){ }


       String content = "\n<!-- \n";
        if(map.size()>0){
                 for (Map.Entry<String, Styledto> entry : map.entrySet()) {
                        String bg_type = entry.getValue().getBg_type() !=null ? entry.getValue().getBg_type() : "";

                        content =content +"#"+entry.getKey()+", #"+entry.getKey()+" a , #"+entry.getKey()+" p, #"+entry.getKey()+" button { "+ checkEmpty(entry.getValue().getFont_color(), "color:", ";")+"  }"
                              +"#"+entry.getKey()+"{"
                              +checkEmpty(entry.getValue().getBg_image(), "background-image: url(\"", "\");")
                              +(bg_type == "fixed" ? "background-attachment:fixed;" :"")
                              +"padding:" +checkDefault(entry.getValue().getPaddingt(), "20") +"px "+checkDefault(entry.getValue().getPaddingr(),  "15") +"px "
                                          +checkDefault(entry.getValue().getPaddingb(),  "20") +"px "+checkDefault(entry.getValue().getPaddingl(),  "15") +"px; "
                              +"}"
                              +"#"+entry.getKey()+":before {"
                              +"display:block;"+checkEmpty(entry.getValue().getBg_color(), "background:", ";")+checkEmpty(entry.getValue().getBg_opacity(), "opacity:", ";")
                              +"}"
                              +css;
                       }

        }else {
            content =content +css;
         }

      return content + "\n -->\n";
   }


    public static String getCatFromProduct(String catid){
        String getcatCpu = "";
               if(!catid.isEmpty()) {
                   JsonElement json = getConfigValByKey("_categories_structure_");
                   if (json != null) {
                       try {
                           JsonElement val2 = json.getAsJsonObject().get(clearJson(catid));
                           if(val2 != null){
                               getcatCpu = "<a href=\""+returnJsonElement(val2, "cpufull")+"\">"+returnJsonElement(val2, "")+"</a>";
                           }
                           getcatCpu = val2 != null ? returnJsonElement(val2, "cpufull") : "no-cat";

                       } catch (Exception e) {
                       }
                   }
               }
        return getcatCpu;
    }






    public static String productUrl(String cpu, String cat){
         String url = "product/";
         if(cat == null)
             return url+ "no-cat" + "/"+cpu;

         List<String> jsoncat = isJSONArrValid(cat);
         String catid = "";
         if(jsoncat !=null){
             catid = jsoncat.get(0);
          }else{
             catid = cat.replaceAll("\\D+","");
         }

         CatJson allCategories = getConfig().getCategories().containsKey(catid)?  getConfig().getCategories().get(catid): new CatJson();
        return url+ allCategories.getCpufull() + "/"+cpu;
    }

    public static String formatDecimal(float number) {
        float epsilon = 0.004f; // 4 tenths of a cent
        if (Math.abs(Math.round(number) - number) < epsilon) {
            return String.format("%10.0f", number); // sdb
        } else {
            return String.format("%10.2f", number); // dj_segfault
        }
    }





  public static Boolean checkVariations(String attr){
      JsonObject json = isJSONValid(attr);
      if(json !=null){
          JsonElement element = json.get("variation");
          if(element !=null)
              return true;
      }
      return false;
  }

  public static String addToCartBtn(Map<String, ProductVariations> attr, String classcss, Integer prid, Integer qtu ){
      qtu = qtu !=null ? qtu : 0;
      String returnData = "";
      String productId= Integer.toString(prid);
      String Qtu2 = Integer.toString(qtu);
      String[] check = {"not"};

      attr.forEach((k,v)->{ if(v.get("type") !=null) { if(v.get("type").contains("var")){ check[0] = "yes";} } } );

      String url =  baseurl();

      if(check[0].contains("yes")){
          returnData = " onclick=\"modaljs(this,'show','ajax');return false;\" data-modal=\"#mini-modal\" data-url=\""+url+"product/url/"+productId+"?type=mini\" ";
      }else{
          returnData = " onclick=\"simplePost('"+url+"cart/add', '.load_content_smallcart', 'action=AddToCart&id="+productId+"&qtu=1&max_qtu="+Qtu2+"&type=smallCart','', '.add_class"+productId+"'); return false;\"";
      }

       return " <a href=\"#\" class=\""+classcss+"\" "+returnData+">\n" +
              "    <i class=\"fa fa-shopping-cart\"></i>\n" +
              "    <span class=\"add_class"+productId+"\"> </span>\n" +
              " </a>";
  }




  public static Float getMedia(List<Comments> comments, String type ){
      int media= 0, star1 = 0, star2 = 0, star3 = 0, star4=0, star5 = 0, totalCommnents = 0;

      if(comments !=null){
          for (Comments comment : comments) {
              totalCommnents ++;
              if(comment.getStars() == 1){
                  star1 ++;
              }else if(comment.getStars() == 2){
                  star2 ++;
              }else if(comment.getStars() == 3){
                  star3 ++;
              }else if(comment.getStars() == 4){
                  star4 ++;
              }else if(comment.getStars() == 5){
                  star5 ++;
              }

          }
      }else{
          System.out.println("Comments: null ");
      }

      media= star1 +  (star2 * 2) + (star3 * 3) + (star4 * 4) + (star5 * 5);

      return Float.valueOf(totalCommnents > 0 ? (type == "total" ? totalCommnents : media/totalCommnents)  : 0);
  }


    /**
     * Get starsof product
     * @param comments
     * @return
     */
   public static String startsQtu(List<Comments> comments){
      float getMedia = getMedia(comments, "media");
      return getMedia > 0 ? "<span class=\"stars"+Math.round(getMedia)+"\"></span>":"";
    }



    /**
     * Return image product if image is not set return noimage
     * @param image
     * @param type
     * @return
     */

    public static String imageProduct(String image, String type){
        if(image == null || image.isEmpty())
            return type=="thumb"? "content/imgproduct/thumb_noimage.jpg" : "content/imgproduct/noimage.jpg";

        return type=="thumb"? "content/imgproduct/"+image : "content/imgproduct/"+image;
    }

    /**
     * get products gallery
     * @param galleries
     * @param mainimg
     * @return
     */
  public static String getGallery(List<Gallery> galleries, String mainimg){
      String baseurl = HelpersJson.getConfig().getMinOptions().getBaseurl();
      String galleryReturn = "";
      String mainImg= "";

      if (galleries != null) {
          mainImg = mainimg == null || mainimg.isEmpty() ? "" :
                  "<a data-fancybox=\"gallery\" class=\"gallery_product\" href=\"" + baseurl + imageProduct(mainimg, "full") + "\"  onclick=\"return replace_image_with(this);\">\n" +
                      "<img src=\"" + baseurl + imageProduct(mainimg, "thumb") + "\" alt=\"\">\n" +
                  "</a>";

          for (int i = 0; i < galleries.size(); i++) {
              if (galleries.get(i).getTypefile().contains("gallery")) {
                  galleryReturn = galleryReturn +
                              "<a data-fancybox=\"gallery\" class=\"gallery_product\" href=\"" + baseurl + imageProduct(galleries.get(i).getDirectory(), "full") + "\"  onclick=\"return replace_image_with(this);\">\n" +
                              "     <img src=\"" + baseurl + imageProduct(galleries.get(i).getDirectory(), "thumb") + "\" alt=\"\">\n" +
                              "</a>";
                  }
              }

          }

      return !galleryReturn.isEmpty()?  mainImg+galleryReturn : "";
   }

    /**
     * will generate url from string
     * @param input : The best 25 $ title in town
     * @return : The-25-best-title-in-town
     */
    public static String slug(String input){
          Pattern NONLATIN = Pattern.compile("[^\\w-]");
          Pattern WHITESPACE = Pattern.compile("[\\s]");
          Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGESDHASHES.matcher(slug).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }


   public static String prepareMenu(String menu){

       menu = menu.replaceAll("<a href=\"#\" class=\"fa_delete fa_small delete_ittem\"></a>","")
                  .replaceAll("<a href=\"#\" class=\"fa_edit fa_small\"></a>","")
                  .replaceAll("<a href=\"#\" class=\"fa_move move_bdn\"></a>","")
                  .replaceAll("<a href=\"#\" class=\"fa_move move_bdn ui-sortable-handle\"></a>","")
                  .replaceAll("class=\"simple_link\"","")
                  .replaceAll("style=\"opacity: 1;\"","")
                  .replaceAll(" ui-sortable-handle","")
                  .replaceAll("<ul>","<ul class=\"sub-menu\">");


       String currentLang = Language.currentLangForUrl();
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();

       menu = menu.replaceAll("href=\"/","href=\""+currentLang+"/")
                  .replaceAll("href=\""+url,"href=\""+url+currentLang+"/");

       String getCurrentUrl = currentUrl();

       if(getCurrentUrl.equals("/")){
            menu = menu.replaceAll("\"/\"","\""+getCurrentUrl+"\" class=\"active-menu\"")
                       .replaceAll("\""+url+"\"", getCurrentUrl+"\" class=\"active-menu\"");
        }else{
            menu = menu.replaceAll(getCurrentUrl+"\"",  getCurrentUrl+"\" class=\"active-menu\"") ;

       }

     return menu;
   }

public static String currentUrl(){
    //ServletUriComponentsBuilder.fromCurrentContextPath().toUriString() // return the domain name   http://localhost:8999
    //ServletUriComponentsBuilder.fromCurrentServletMapping().toUriString() // return the domain name
    //ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString()  // return full url with http //http://localhost:8999/cat/become-milionare/be-milionare-child
    //ServletUriComponentsBuilder.fromCurrentRequest().toUriString() // return full url  http://localhost:8999/cat/become-milionare/be-milionare-child?q=dsdf&d=dss

    String domainName= ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    String urlPat= ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
          urlPat = urlPat.replace(domainName,"");
    return urlPat;
}


    /**
     * convert the price in $20.20 and will return the sale price if is set
     * @param price
     * @param sale_price
     * @return
     */
    public static String price(Float price, Float sale_price ){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String currencyPosition = "nospace";
        String currencyCode ="$";
        Float rate = 1.0f;
        String mainCurrency = "USD";

        Map<String,  Currency> currencies =  HelpersJson.getConfig().getCurrencies();
        if(currencies.size()>0){
            String cookie = Cookies.get("currency", request);
            for (Map.Entry<String, Currency> entry : currencies.entrySet()) {
                if (entry.getValue().getMain()) {
                    currencyPosition = entry.getValue().getType();
                    currencyCode = entry.getValue().getCode();
                    rate = Float.parseFloat(entry.getValue().getRate());
                    mainCurrency = entry.getKey();
                    break;
                }
            }

            if(!cookie.isEmpty() && currencies.containsKey(cookie) && !mainCurrency.contains(cookie)){
                currencyPosition = currencies.get(cookie).getType();
                currencyCode = currencies.get(cookie).getCode();
                rate = Float.parseFloat(currencies.get(cookie).getRate());
            }
        }

        price  = price != null ? price * rate : 0;
        sale_price = sale_price !=null ? sale_price * rate : 0;

        String price_return = "";
        String p = "<span>"+  formatDecimal(price)+"</span>";
        String sp = "<span>"+  formatDecimal(sale_price)+"</span>";

        if(currencyPosition == "left with space"){
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+currencyCode+" "+p+"</del> "+currencyCode+" "+sp : currencyCode+" "+p;
        }else if(currencyPosition == "right with space"){
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+p+" "+currencyCode+"</del> "+sp+" "+currencyCode : p+" "+currencyCode;
        }else if(currencyPosition == "right"){
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+p+currencyCode+"</del> "+sp+currencyCode : p+currencyCode;
        }else{
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+currencyCode+  p +"</del> "+currencyCode + sp : currencyCode + p;
        }

        return price_return;
    }

    /**
     * check if is ajax request
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWithHeader);
    }

    public static String html2text(String html) {
        return html.replaceAll("\\<.*?>","");
    }

    public static String excerpt(String txt, int nrwords){
        txt = html2text(txt);
        String[] split= txt.split(" ");
        String newtxt = "";
        int i=0;
        for (String word:split){
            i++;
            newtxt = newtxt+ " "+word;
            if(i == nrwords) break;
        }
        return newtxt;
    }

}
