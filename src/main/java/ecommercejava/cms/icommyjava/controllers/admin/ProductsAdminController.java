package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Attributes.Attributes;
import ecommercejava.cms.icommyjava.dto.Attributes.AttributesVar;
import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.entity.Product;
import ecommercejava.cms.icommyjava.entity.Users;
import ecommercejava.cms.icommyjava.helper.AdminHelpers;
import ecommercejava.cms.icommyjava.helper.AuthenticationSystem;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import ecommercejava.cms.icommyjava.services.GalleryServices;
import ecommercejava.cms.icommyjava.services.ProductService;
import ecommercejava.cms.icommyjava.services.StorageService;
import ecommercejava.cms.icommyjava.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
//@RequestMapping(path="") // This means URL's start with /demo (after Application path)

public class ProductsAdminController extends MainControllerAdmin {
    @Autowired // This means to get the bean called ProductRepository
    private ProductService productService;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    GalleryServices galleryService;

    @Autowired
    StorageService storageService;

    @Autowired
    UserService userService;


    @RequestMapping(value ="/admin/view-products", method = RequestMethod.GET)
    public ModelAndView showproductlist(Model model, HttpServletRequest request,
                                        @RequestParam(value ="userid", required=false ) String userid,
                                        @RequestParam(value ="catid", required=false ) String catid,
                                        @RequestParam(value ="search", required=false ) String search

                                         ) {
        Page<Product> productResponse = null;
        if(userid !=null){
            productResponse =
                    productService.findByUsersid(Integer.parseInt(userid), pagination(request, productService.count(), model, 40));
        }else if(catid !=null){
            productResponse = productService.findByCatid(catid, pagination(request, productService.count(), model, 40));
        }else if(search !=null){
            productResponse = productService.search(search, search, search, search, pagination(request, productService.count(), model, 40));
        }else {
            productResponse = productService.findAll(pagination(request, productService.count(), model, 40));   //pagination(request, productService.count(), model, 20)
        }

        model.addAttribute("title", "Products");

        if(productResponse !=null) {
            model.addAttribute("products", productResponse);
          }

        return view("admin::pages/productlist") ;
    }

    @RequestMapping(value ="/admin/products/add-edit", method = RequestMethod.GET)
    public ModelAndView product_add_edit(@RequestParam(value ="id", required=false, defaultValue = "0") String id, Model model  ) {
        Product product = productService.getOne(Integer.parseInt(id));


        Attributes attributes = Helpers.getAttrSettings();
        String jsSugestion = " ";
        if(attributes.getFields().size()>0) {
            for (Map.Entry<String, AttributesVar> entry : attributes.getFields().entrySet()) {
                jsSugestion = jsSugestion + " var "+ entry.getKey() +" = "+ new Gson().toJson(entry.getValue().getSugestion())+"; ";
             }
        }

       jsSugestion = jsSugestion +"";

        model.addAttribute("jsattributes", jsSugestion);
        model.addAttribute("attributes", attributes.getFields());
        model.addAttribute("categorieslist", Helpers.getConfigXmlData("categorieslink","product",""));

        try{
            model.addAttribute("title", product.getTitle());
            model.addAttribute("page", product);
            model.addAttribute("attr", product.getAttrMapModule());
            model.addAttribute("productid", product.getProductid());
            model.addAttribute("prid", product.getProductid());

        }catch (Exception e){
            model.addAttribute("page", new Product() );
            model.addAttribute("title", "Add new Product");
             int randomInt = (int)(999999.0 * Math.random());
            model.addAttribute("temporarid", randomInt);
            model.addAttribute("prid", randomInt);
         }

          return view("admin::pages/product") ;
    }


    /**
     *  get data from post and put in database, and after redirect back
     * @param request
     * @return
     */

    @PostMapping(path="/admin/update-product") // Map ONLY POST Requests

    public  String store (HttpServletRequest request, RedirectAttributes redirectAttributes,
                          @RequestParam(value ="imgmain", required=false, defaultValue = "null") MultipartFile imgmain) {

            Product pr = new Gson().fromJson(Helpers.convertRequestt(request.getParameterMap()), Product.class);

            // upload main image
            if(!imgmain.getOriginalFilename().isEmpty()){
                pr.setImage(storageService.uploadSingleImg(imgmain, ""));
             }

           // get product id
            Integer id = StringUtils.isEmpty(request.getParameter("productid")) ? 0 : Integer.parseInt(request.getParameter("productid"));

            try{
                // here we call setter what do some sett like generate slug, and put the title in translate like {"en":"title en", "it":"title it"}
                Product n = productService.getOne(id);
                pr.setter(n, request.getParameter("lang"));
             }catch (Exception e){
                pr.setter(new Product(), request.getParameter("lang"));
                Users users = userService.getOne(AuthenticationSystem.currentUserId());
                if(users !=null)
                   pr.setUsers(users);
             }
          //
            Map<String, Map<String, String>> map = Helpers.counvertDoubleTomap(request.getParameterMap(), "variation", "specification");
            pr.setAttr(new Gson().toJson(map));

            //Map<String, Map<String, String>> map2 = Helpers.counvertDoubleTomap(request.getParameterMap(), "specification");
         //System.out.println("mappp"+map2.toString() );

           Product productSave =  productService.save(pr);
           updateSugestion(request);

             // update gallery
             String temporarid = request.getParameter("temporarid");
              if(temporarid != null){
                   temporarid = temporarid.replace(",","").replace(".","");
                   galleryService.updateGalleryByProductId(productSave.getProductid(), Integer.parseInt(temporarid));
               }

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");

    }

    private void updateSugestion(HttpServletRequest request){

        Map<String,String[]> map = request.getParameterMap();
        Attributes attributes = Helpers.getAttrSettings();
         boolean wasSomethnig = false;
        String evitDouble = "sdrgerdefregge";

        for(Map.Entry<String, String[]> entry: map.entrySet()) {
            if (entry.getKey().contains("sugestion") && !entry.getKey().contains("[]")){

                String fieldKey = AdminHelpers.splitInputKey(entry.getKey(),0);
                String SugestionKey = AdminHelpers.splitInputKey(entry.getKey(),1);

                if(attributes.getFields().containsKey(fieldKey) && !fieldKey.isEmpty() && !SugestionKey.isEmpty() && !evitDouble.contains("sugestion["+fieldKey+"]["+SugestionKey+"]")) {
                    wasSomethnig =true;
                    try{
                        AttributesVar attributesVar = attributes.getFields().get(fieldKey);
                        Map<String, Lang> sugestion =  attributesVar.getSugestion();
                        sugestion.remove(SugestionKey);

                        Lang title = new Lang();
                        for (int i = 0; i < Helpers.listLang().size(); i++) {
                            String lang = Helpers.listLang().get(i);
                            title.set(lang, request.getParameter("sugestion["+fieldKey+"]["+SugestionKey+"]["+lang+"]"));
                        }
                        sugestion.put(SugestionKey, title);

                        attributesVar.setSugestion(sugestion);
                        attributes.getFields().put(fieldKey, attributesVar);

                    }catch (Exception e){System.out.println("tiltle error:"+e); }

                    evitDouble = "sugestion["+fieldKey+"]["+SugestionKey+"]";
                }
            }
        }

          if(wasSomethnig)
                attributes.save();

    }



    /**
     *  Update or delete bulk
     * @param request
     * @param id
     * @return
     */
    @PostMapping(path="/admin/products-bulk") // Map ONLY POST Requests
    public String updateBulk (HttpServletRequest request, @RequestParam(value = "id", required = false) List<String> id) {
        // Integer id = request.getParameter("id").isEmpty() ? 0 : Integer.parseInt(request.getParameter("id"));
        String action = request.getParameter("action");
        String category_id = request.getParameter("category_id");
        String search = request.getParameter("s");
        if(!search.isEmpty() && action.isEmpty())
            return "redirect:/admin/view-products?search="+search;

        try{
            if(id.size()>0 && action.contains("del")){
                for (String idnr : id) {
                    productService.delete(idnr);
                }
             }else if(id.size()>0 && (action.contains("1") || action.contains("2"))){
                for (String idnr : id) {
                    Product product = productService.getOne(idnr) ;
                    product.setHide(Integer.parseInt(action));
                     productService.save(product);
                }
            }else if(id.size()>0 && action.contains("move")){
                for (String idnr : id) {
                    Product product = productService.getOne(idnr) ;
                     product.setCatid("[\""+category_id+"\"]");
                    productService.save(product);
                }
            }

        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }


    /**
     *  delete
     * @param request
     * @return
     */

    @GetMapping(path="/admin/products-delete-hide") // Map ONLY POST Requests

    public String delete (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            productService.delete(id);
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}