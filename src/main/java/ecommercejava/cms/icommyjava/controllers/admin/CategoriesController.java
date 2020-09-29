package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Categories;
import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.helper.Language;
import ecommercejava.cms.icommyjava.repository.CategoriesRepository;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller // This means that this class is a Controller
public class CategoriesController extends MainController {

    @Autowired // This means to get the bean called ProductRepository
    private CategoriesRepository categoriesRepository;

    @Autowired // This means to get the bean called ProductRepository
    private SettingsRepository settingsRepository;

    /**
     *  Show the list of categories
     * @param type
     * @param model
     * @param request
     * @return
     */
     @RequestMapping(value ="/admin/categories/list/**/{type}", method = RequestMethod.GET)
    public ModelAndView categories(@PathVariable String type, Model model, HttpServletRequest request) {
        //String name, Model model
        model.addAttribute("title", type +" categories");
        model.addAttribute("type", type);
        List<Categories> categories = categoriesRepository.findByTypeContaining(type, pagination(request, categoriesRepository.count(), model, 40));


        if(categories !=null){
             model.addAttribute("categorieslist", categories);
        }

        return view("admin::pages/categories") ;
    }

    /**
     * Show edit page
     * @param type
     * @param id
     * @param model
     * @return
     */

     @RequestMapping(value ="/admin/categories/add-edit/{type}", method = RequestMethod.GET)
    public ModelAndView categoriesaddedit(@PathVariable String type, @RequestParam String id, Model model ) {
           Categories category = categoriesRepository.getOne(Integer.parseInt(id));
           model.addAttribute("type", type);
        try{
            model.addAttribute("title", category.getTitle());
            model.addAttribute("page", category);
           JsonElement dataJson = Helpers.getConfigValByKey("_categories_structure_");

         }catch (Exception e){
            model.addAttribute("page", new Categories() );
            model.addAttribute("title", "Add new Category");
        }

        return view("admin::pages/addedit_categories") ;
    }

    @PostMapping(path = "/admin/update-categories-multiple", consumes = "application/x-www-form-urlencoded")
    public @ResponseBody
    String storeSingle (HttpServletRequest request, Model model, RedirectAttributes redirectAttributes ) {
         Lang lang = new Lang();
         String cpu = "";
         int i=0;
         for (String l : Language.listLang()){
             lang.set(l, request.getParameter("title_"+l));
             if(i==0)
                  cpu =request.getParameter("title_"+l);

             i++;
         }
         String parent= request.getParameter("parent");

        //request
         Categories categories = new Categories();
         categories.setFullTitle((new Gson()).toJson(lang));
         categories.setCpu(Helpers.slug(cpu));
         categories.setType(request.getParameter("type"));
         categories.setParent(parent !=null && !parent.isEmpty()? Integer.parseInt(parent):0);

         categoriesRepository.save(categories);

         Helpers.generateCatStructure((List<Categories>) categoriesRepository.findAll());

           String catid= request.getParameter("productID");
                  catid = catid != null ? catid.replaceAll("--","\""):"";
        System.out.println("ddddddddd:"+catid);
        System.out.println("ddddddddd:"+Helpers.catStructure(request.getParameter("type"), "checkbox", catid));
         return Helpers.catStructure(request.getParameter("type"), "checkbox", catid);
      }



    /**
     *  get data from post and put in database, and after redirect back
     * @param request
     * @param model
     * @return
     */


    @PostMapping(path = "/admin/update-categories", consumes = "application/x-www-form-urlencoded")
    public String store (HttpServletRequest request, Model model, RedirectAttributes redirectAttributes ) {
        Integer id = request.getParameter("catid").isEmpty() ? 0 : Integer.parseInt(request.getParameter("catid"));

        Categories cat = new Gson().fromJson(Helpers.convertRequestt(request.getParameterMap()), Categories.class);


        // here we call setter what do some sett like generate slug, and put the title in translate like {"en":"title en", "it":"title it"}
        try{
            Categories n = categoriesRepository.getOne(id);
            cat.setter(n, request.getParameter("lang"));
        }catch (Exception e){
            cat.setter(new Categories(), request.getParameter("lang"));
        }

        categoriesRepository.save(cat);

        // will generate cpu for categories and save them in json format in config.json
         Helpers.generateCatStructure((List<Categories>) categoriesRepository.findAll());

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
       return  "redirect:" + request.getHeader("Referer");

    }

    /**
     *  Update or delete bulk
     * @param request
     * @param id
     * @return
     */
     @PostMapping(path="/admin/categories/update-bulk") // Map ONLY POST Requests
    public String updateBulk (HttpServletRequest request, @RequestParam("id") List<String> id) {
       // Integer id = request.getParameter("id").isEmpty() ? 0 : Integer.parseInt(request.getParameter("id"));
      String action = request.getParameter("action");
        try{
           if(id.size()>0 && action.contains("del")){
               //if(action.contains("del")){
                   for (String idnr : id) {
                       categoriesRepository.deleteById(Integer.parseInt(idnr));
                    }
               // will generate cpu for categories and save them in json format in config.json

               //}
           }
           // Categories n = categoriesRepository.getOne(id);
            //n.setType(request.getParameter("type"));
            //categoriesRepository.save(n);
        }catch (Exception e){System.out.println("errrr:"+e);}
        try{
            Helpers.generateCatStructure((List<Categories>) categoriesRepository.findAll());
        }catch (Exception en){}
        return "redirect:" + request.getHeader("Referer");
    }


    /**
     *  Delete or hide category by id
     * @param request
     * @param id
     * @param action
     * @return
     */
     @GetMapping(path="/admin/categories/delete-hide") // Map ONLY POST Requests
    public String delete_hide (HttpServletRequest request, @RequestParam String id,
                               @RequestParam(name="action", required=false, defaultValue="") String action) {
        // Integer id = request.getParameter("id").isEmpty() ? 0 : Integer.parseInt(request.getParameter("id"));

        try{
            if(action == null || action.isEmpty()) {
                 categoriesRepository.deleteById(Integer.parseInt(id));
                 // will generate cpu for categories and save them in json format in config.json
                 Helpers.generateCatStructure((List<Categories>) categoriesRepository.findAll());
            }else {
                 Categories n = categoriesRepository.getOne(Integer.parseInt(id));
                 n.setTip(Integer.parseInt(action));
                 categoriesRepository.save(n);
            }

        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}
