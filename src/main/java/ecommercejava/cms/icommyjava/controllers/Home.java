package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.Page;
import ecommercejava.cms.icommyjava.entity.Product;
import ecommercejava.cms.icommyjava.services.DoShortCode;
import ecommercejava.cms.icommyjava.services.PagesServices;
import ecommercejava.cms.icommyjava.services.ProductService;
import ecommercejava.cms.icommyjava.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


//@RequestMapping(path="/") // This means URL's start with /demo (after Application path)
@Controller
public class Home extends MainController  {

    @Autowired // This means to get the bean called userRepository
    private PagesServices pagesServices;

    @Autowired
    private ProductService productService;

    @Autowired
    DoShortCode doShortCode;

    @Autowired
    UsersService usersService;


     @GetMapping("/")
    //@RequestMapping(value={"/"}, method = RequestMethod.GET)
    public ModelAndView index(Model model, HttpServletRequest request) {
            int settingsPageHome  = Helpers.getConfig().getMinOptions().getPageHome();

            Long countU = usersService.count();

            if (countU==0){
                 return view("frontend::start/admin");
             }


        if(settingsPageHome >0){
                 Page page = pagesServices.getOne(settingsPageHome);

               try{
                   model.addAttribute("title", page.getTitle());
                   model.addAttribute("css", Helpers.generateCss(page.getStyle(), page.getCss()));
                   model.addAttribute("content", doShortCode.doshortcodes(page.getText()));
                   model.addAttribute("metad", page.getMetad());
                   model.addAttribute("metak", page.getMetak());
               }catch (Exception e){}

            }else{
            org.springframework.data.domain.Page<Product> products = productService.findAll(pagination(request, productService.count(), model, 30));
             model.addAttribute("title", Helpers.getConfig().getMinOptions().getSite_title());
             model.addAttribute("metad", Helpers.getConfig().getMinOptions().getMetad());
             model.addAttribute("metak", Helpers.getConfig().getMinOptions().getMetak());
             model.addAttribute("products", products);
        }


         String view= settingsPageHome>0 ? "theme::pages" : "theme::home";

         return view(view);
    }

}
