package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Subscribers;
import ecommercejava.cms.icommyjava.repository.SubscribersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SubscribersControllerAdmin extends MainController {
    @Autowired
    SubscribersRepository subscribersRepository;

    @RequestMapping(value ="/admin/subscribers", method = RequestMethod.GET)
    public ModelAndView show (Model model, HttpServletRequest request, @RequestParam(value ="search", required = false) String search) {
        //Page<Subscribers> subscribers =  subscribersRepository.findAll(pagination(request, subscribersRepository.count(), model, 20));
        Page<Subscribers> subscribers =search !=null ? subscribersRepository.findByEmailContaining(search,  pagination(request, subscribersRepository.count(), model, 20)):
                subscribersRepository.findAll(pagination(request, subscribersRepository.count(), model, 20));
        //String name, Model model
        model.addAttribute("title",  "Subscribers");
        model.addAttribute("search",  search);
        model.addAttribute("rows", subscribers );
        //List<Categories> categories = categoriesRepository.findByTypeContaining(type, pagination(request, categoriesRepository.count(), model, 40));
        return view("admin::pages/subscribe") ;
    }


    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/subscribe/destroy-bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam(value = "bulkid", required = false) List<String> bulkid) {
        String action = request.getParameter("action");
        String search = request.getParameter("s");

        if(!search.isEmpty() && action.isEmpty())
            return "redirect:/admin/subscribers?search="+search  ;



        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        subscribersRepository.deleteById(Integer.parseInt(idnr));
                    }
                }
            }
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }
}
