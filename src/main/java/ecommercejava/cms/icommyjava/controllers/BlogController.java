package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.entity.Categories;
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

@Controller
public class BlogController extends MainController {
    @Autowired
    PagesServices pagesServices;
    @Autowired
    DoShortCode doShortCode;


    @RequestMapping(value ={"/blog","blog/**/{cpu}"}, method = RequestMethod.GET)
    public ModelAndView list(@PathVariable(required = false) String cpu, Model model, HttpServletRequest request) {
        Categories categories= null;
        if(cpu !=null){
              categories = pagesServices.getCat(cpu);
         }

        org.springframework.data.domain.Page<Page> rows = cpu !=null ?
                   (categories !=null? pagesServices.getPostsFromCategory(Integer.toString(categories.getCatid()), pagination(request, pagesServices.count(), model, 20)) : null) :
                    pagesServices.findByType("posts", pagination(request, pagesServices.count(), model, 20));

         model.addAttribute("title", categories !=null ? categories.getTitle() : "Blog");
         model.addAttribute("rows",rows);
        return view("theme::posts");
    }

    @RequestMapping(value ="/blog-single/{cpu}", method = RequestMethod.GET)
    public ModelAndView single(@PathVariable String cpu, Model model, HttpServletRequest request) {

        Page row = pagesServices.getpost(cpu);

        if(row!=null) {
            model.addAttribute("title", row.getTitle());
        }

        model.addAttribute("row",row);

        return view("theme::posts");
    }
}
