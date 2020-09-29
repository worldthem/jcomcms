package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.Page;
import ecommercejava.cms.icommyjava.services.DoShortCode;
import ecommercejava.cms.icommyjava.services.PagesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Controller // This means that this class is a Controller
//@RequestMapping(path="/") // This means URL's start with /demo (after Application path)


@RequestMapping(path={"/page"}) // This means URL's start with /demo (after Application path)
public class Pages  extends  MainController{
    @Autowired
    PagesServices pagesServices;
    @Autowired
    DoShortCode doShortCode;

    @RequestMapping(value ="/{cpu}", method = RequestMethod.GET)
    public ModelAndView index(@PathVariable String cpu, HttpServletRequest request, Model model ) {

        String view = "theme::pages";
            Page page = pagesServices.getPagesByCpu(cpu);
            if(page!=null){
                model.addAttribute("title", page.getTitle());
                model.addAttribute("css", Helpers.generateCss(page.getStyle(), page.getCss()));
                model.addAttribute("content", doShortCode.doshortcodes(page.getText()));
            }else{
                 view = "theme::404";
                 model.addAttribute("title", "404");
            }

         return view(view) ;
    }

}
