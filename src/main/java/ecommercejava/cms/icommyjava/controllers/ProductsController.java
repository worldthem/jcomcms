package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Attributes.Attributes;
import ecommercejava.cms.icommyjava.dto.Attributes.AttributesVar;
import ecommercejava.cms.icommyjava.dto.Config.CatJson;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.entity.Categories;
import ecommercejava.cms.icommyjava.entity.Page;
import ecommercejava.cms.icommyjava.entity.Product;
import ecommercejava.cms.icommyjava.helper.Cookies;
import ecommercejava.cms.icommyjava.helper.ViewHelper;
import ecommercejava.cms.icommyjava.helper.ViewHelpersRequest;
import ecommercejava.cms.icommyjava.services.PagesServices;
import ecommercejava.cms.icommyjava.services.ProductService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller // This means that this class is a Controller
//@RequestMapping(path="") // This means URL's start with /demo (after Application path)

public class ProductsController extends  MainController{
    @Autowired // This means to get the bean called ProductRepository
    private ProductService productService;

    @Autowired
    PagesServices pagesServices;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    @RequestMapping(value ="/products/sort/{sort}", method = RequestMethod.GET)
    public String sortprice(@PathVariable String sort, HttpServletRequest request, HttpServletResponse response){
        (new Cookies()).set("pricesort", sort, response);
        return "redirect:" + request.getHeader("Referer");
     }

    //@GetMapping("/get-single")
    @RequestMapping(value ="/cat/**/{cpu}", method = RequestMethod.GET)
    public ModelAndView showproductfromcategory(@PathVariable String cpu, Model model, HttpServletRequest request,
                                                @RequestParam(name="type", required=false ) String type ) {

        String priceSort = (new Cookies()).get("pricesort", request);

        Categories categories = productService.getCat(cpu);
        if(categories ==null&& !cpu.contains("search"))
            return view("theme::404") ;

        Integer catid = categories ==null? 0 : categories.getCatid();
        Map<String, CatJson> cat = Helpers.getConfig().getCategories();
        String catIds = Integer.toString(catid);
        if(cat != null){
            for(Map.Entry<String, CatJson> entry: cat.entrySet()){
                if(catid == entry.getValue().getParent())
                            catIds = catIds+"-"+entry.getKey();
            }
          }

        Map<String, String[]> req = new HashMap<>(request.getParameterMap());
        String[] catIdArr = catIds.split("-"); //{Integer.toString(catid)};
        String[] sortPrice = {priceSort};
        req.put("catid",catIdArr);
        req.put("sort",sortPrice);

        org.springframework.data.domain.Page<Product> productResponse = productService.getProducts(req, pagination(request, productService.count(), model, 20));

        if(productResponse !=null) {
            model.addAttribute("products", productResponse);
            model.addAttribute("title", categories==null ? Helpers.translate("Search"): splitLang(categories.getTitle()));
        }

       return Helpers.isAjax(request)? viewpart("theme::productlistAjax") : view("theme::productlist") ;
    }

    /**
     * this is use in case you use No sql db like mangodb or maria db because they not support Criteria
     * @param cpu
     * @param model
     * @param request
     * @param search
     * @param pricemin
     * @param pricemax
     * @param sort
     * @return
     */
   // @RequestMapping(value ="/cat/**/{cpu}", method = RequestMethod.GET)
    public ModelAndView showproductfromcategoryMango(@PathVariable String cpu, Model model, HttpServletRequest request,
                                                     @RequestParam(name="s", required=false ) String search,
                                                     @RequestParam(name="pricemin", required=false ) String pricemin,
                                                     @RequestParam(name="pricemax", required=false ) String pricemax,
                                                     @RequestParam(name="sort", required=false ) String sort
    ) {



        String priceSort = (new Cookies()).get("pricesort", request);

        Categories categories = productService.getCat(cpu);
        if(categories ==null)
            return view("theme::404") ;

        Integer catid =  categories.getCatid();


       org.springframework.data.domain.Page<Product> productResponse  ;
        if(search!=null){
            productResponse = productService.search(search,search,search,search, pagination(request, productService.count(), model, 20), priceSort, pricemin, pricemax);
        }else {
           productResponse = productService.findByCatid(Integer.toString(catid), pagination(request, productService.count(), model, 20), priceSort, pricemin, pricemax );
        }



        if(productResponse !=null) {
            model.addAttribute("products", productResponse);
            model.addAttribute("title", splitLang(categories.getTitle()));
          }
      return view("theme::productlist") ;
    }



    //@GetMapping("/get-single")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    //@Secured("ROLE_ADMIN")
    @RequestMapping(value ="/product/**/{cpu}", method = RequestMethod.GET)
    public ModelAndView single(HttpServletRequest request, @PathVariable String cpu, Model model, @RequestParam(name="type", required=false, defaultValue="full") String type) {

        // product can be accesible also by id
        String regex = "\\d+";
        Product product = cpu.matches(regex) ? productService.getOne(Integer. valueOf(cpu)):
                                               productService.getByCpu(cpu);

        String view = type.contains("full") ? "theme::singleproduct" :
                        (Helpers.viewExist("theme::layouts/minisingle")  ? "theme::layouts/minisingle" :  "frontend::product/minisingle");

       try{
            model.addAttribute("title",  product.getTitle());
            model.addAttribute("description",  product.getDescription());
            model.addAttribute("text",  product.getText());

            model.addAttribute("product", product);
            model.addAttribute("priceOtions", priceOptions(product, request));

            //getTabs
            model.addAttribute("tabs", getTabs(product));

            // get cat of products
            String cat = product.getCatid();
            List<String> isJson = Helpers.isJSONArrValid(cat);
            if (isJson != null) { // in case is json than we take the first, the product could be in 2-3 categories
                cat = isJson.get(0) != null? isJson.get(0) : "0";
            }

            // random products for related products
            Long qty = productService.count();
            int rand1 = (int) (Math.random() * qty);
            int rand2 = (int) (Math.random() * qty);
            rand2 = rand1 == rand2 ? (int) (Math.random() * qty) : rand2;
            //System.out.println("rand: "+idx + "---"+qty);

            Pageable pageable1 = PageRequest.of(rand1, 2);
            Pageable pageable2 = PageRequest.of(rand2, 2);
             List<Product> products1 = productService.findByCatidList(cat, pageable1);
             List<Product> products2 = productService.findByCatidList(cat, pageable2);
            //Merge two list list
            List<Product> products = Stream.of(products1, products2).flatMap(Collection::stream).collect(Collectors.toList());

            model.addAttribute("products", products);

       }catch(Exception en){
          // System.out.println("eeeee:"+en);
             view = "theme::404";
        }

        // we need to get single product when press on add to cart and product have variations than we show a popup to users or for quick show
        return type.contains("full") ? view(view) : viewpart(view);
    }


    /**
     * Generate Product single page tabs like descriptions, reviews,...
     * @param product
     * @return
     */
    @ResponseBody
    private String getTabs(Product product){
        List<Page> pages = pagesServices.findByType("tabs");

        String title = "";
        String content = "";
        int i = 0;
        if(pages !=null){
            for (Page page : pages){
               if(page.getEnable()!=null){
                   i++;

                 title = title + "<li class=\"tab_buttons  Tab"+page.getPageid()+(i==1 ? " active":"")+" \">\n" +
                               "    <a href=\"#Tab"+page.getPageid()+"\" class=\"nav-link\" onclick=\"return setTab('Tab"+page.getPageid()+"');\" >"+splitLang(page.getTitle())+"</a>\n" +
                                 "</li> ";

                  content  =content + "<div class=\"tab-pane"+(i==1 ? "":" display_none")+"\" id=\"Tab"+page.getPageid()+"\">\n" +
                          tabContent(page.getText(), product) +
                                       " </div>";
               }
            }
        }else{
            System.out.println("no tabs");
        }


        try{

            if(!title.isEmpty()) {
                Template freemarkerTemplate = freemarkerConfigurer.createConfiguration()
                        .getTemplate("cms/standart/product/tabs.html");
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("titletabs", title);
                templateModel.put("contenttabs", content);
                return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
            }

         }catch (Exception e){ }
        return "";
    }



    private String tabContent(String text, Product product){

         if(text.contains("[descriptions]"))
                   return "<div class=\"col-md-12\">"+ text.replace("[descriptions]", splitLang(product.getText()))+"</div>";

         if(text.contains("[specifications]")){
             // we get json and put it in map
             Map<String, Map<String, String>> map = product.getAttrMapMap();

             if(map.size()>0){ //check if map size
                   // get all atributes from attr.json
                 Attributes attributes = Helpers.getAttrSettings();
                   String returnSpec =  "";
                  // create a tmp map to put data because one specification can have multiple value this is for checkbox specifications
                   LinkedHashMap<String, String> checkV= new LinkedHashMap<>();

                  for(Map.Entry<String, Map<String, String>> entry: map.entrySet()) {
                       if(entry.getValue().get("type").contains("specification")){ //we need only specifications because variations and specifications are in same json

                           entry.getValue().forEach((key, value) -> {
                               if(attributes.getFields().containsKey(key)) {
                                   if(checkV.containsKey(attributes.getFields().get(key).getNameTranslated())){
                                       checkV.put(attributes.getFields().get(key).getNameTranslated(),  checkV.get(attributes.getFields().get(key).getNameTranslated())+", "+attributes.getFields().get(key).getSugestionByKey(value));
                                   }else{
                                       checkV.put(attributes.getFields().get(key).getNameTranslated(), attributes.getFields().get(key).getSugestionByKey(value));
                                   }

                               }
                           });
                         }
                  }


                  try{
                     if(product.getComments() !=null) {
                         Template freemarkerTemplate = freemarkerConfigurer.createConfiguration()
                                 .getTemplate("cms/standart/product/specifications.html");
                         Map<String, Object> templateModel = new HashMap<>();
                         templateModel.put("rows", checkV);
                         return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
                     }

                 }catch (Exception e){System.out.println("Err:"+e);  }

              }
             return "<div class=\"col-md-12\">"+ text.replace("[specifications]", "")+"</div>";
            }

        if(text.contains("[reviews]")){
            try{
                if(product.getComments() !=null) {
                    Template freemarkerTemplate = freemarkerConfigurer.createConfiguration()
                            .getTemplate("cms/standart/product/reviews.html");
                    Map<String, Object> templateModel = new HashMap<>();
                    templateModel.put("comments", product.getComments());
                    templateModel.put("product", product);
                    templateModel.put("view", new ViewHelper());
                    templateModel.put("nrcomments", Math.round(Helpers.getMedia(product.getComments(), "total")));
                    templateModel.put("reting", Helpers.getMedia(product.getComments(), "rating"));
                    templateModel.put("starreting", Helpers.startsQtu(product.getComments()));

                    return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
                }

               }catch (Exception e){System.out.println("Err:"+e);  }

         }

        return "<div class=\"col-md-12\">"+ splitLang(text) +"</div>";
      }

    /**
     * will show variations for product like select "color:red; size:25"
     * @param product
     * @return
     */
      public String priceOptions(Product product, HttpServletRequest request) {
          Map<String, Map<String, String>> map = product.getAttrMapMap();
          Attributes attributes = Helpers.getAttrSettings();
          String returnData = "";
          ViewHelpersRequest viewRequest = new ViewHelpersRequest(request);
          if (map.size() > 0 && attributes.getFields().size() > 0) { //check if map size
              // get all atributes from attr.json

              for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
                  if (entry.getValue().get("type").contains("variation")) {
                      String varName = "";
                      for (Map.Entry<String, String> entry2 : entry.getValue().entrySet()) {
                          if (attributes.getFields().containsKey(entry2.getKey())) {
                              String valueAttr = attributes.getFields().get(entry2.getKey()).getSugestionByKey(entry2.getValue());
                              if(!valueAttr.isEmpty())
                                 varName = varName + attributes.getFields().get(entry2.getKey()).getNameTranslated() + ":" + attributes.getFields().get(entry2.getKey()).getSugestionByKey(entry2.getValue()) + "; ";
                          }
                      }

                      returnData = returnData + "<option value=\"" + entry.getKey() + "\" " + (Helpers.jsonElemInt(entry.getValue(), "qtu") > 0 ? "" : "disabled") + "  " +
                              "price=\"" + (Helpers.jsonElemFloat(entry.getValue(), "price") > 0 ? viewRequest.price(Helpers.jsonElemFloat(entry.getValue(), "price"), 0.0f) : "0") + "\">\n" +
                              varName +
                              "</option> ";

                  }
              }
          }

          return returnData.isEmpty()? "": "<select name=\"options\" class=\"select_options_product\" required=\"\" onchange=\"update_price(this);\">"+returnData+ "</select>";
      }


    @RequestMapping(value ="/wishlist", method = RequestMethod.GET)
    public ModelAndView wishlist(HttpServletRequest request, HttpServletResponse response, Model model ) {
            model.addAttribute("rows", productService.wishlist(getCartUserId(request, response), pagination(request, productService.wishlistCount(), model, 20)));
            model.addAttribute("title","Wishlist");
          return  view("theme::productlist") ;
    }
 }
