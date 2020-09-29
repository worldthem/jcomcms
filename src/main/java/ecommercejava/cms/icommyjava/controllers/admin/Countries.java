package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Country;
import ecommercejava.cms.icommyjava.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller // This means that this class is a Controller
public class Countries extends MainController {

    @Autowired // This means to get the bean called ProductRepository
    private CountryRepository countryRepository;


    @RequestMapping(value ="/admin/checkout-countries", method = RequestMethod.GET)
    public ModelAndView show (Model model, HttpServletRequest request) {

        Iterable<Country> contries =  countryRepository.findAll( );
        //String name, Model model
        model.addAttribute("title",  "Countries");

        model.addAttribute("rows", contries);
        //List<Categories> categories = categoriesRepository.findByTypeContaining(type, pagination(request, categoriesRepository.count(), model, 40));
        return view("admin::pages/countries") ;
    }

    /**
     *  get data from post and put in database, and after redirect back this use for add and update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/checkout-countries/store") // Map ONLY POST Requests

    public  String store (HttpServletRequest request, RedirectAttributes redirectAttributes ) {
        Country country = new Gson().fromJson(Helpers.convertRequestt(request.getParameterMap()), Country.class);
        countryRepository.save(country);
        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return request.getParameter("returndata") != null ? viewpatNohtml("admin::layouts/ok" ) : "redirect:" + request.getHeader("Referer");
    }



    /**
     *  bulk update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/checkout-countries/delete") // Map ONLY POST Requests

    public String delete (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            countryRepository.deleteById(Integer.parseInt(id));
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/checkout-countries/bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam("bulkid") List<String> bulkid) {
        String action = request.getParameter("action");
        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        countryRepository.deleteById(Integer.parseInt(idnr));
                    }
                }
            }
       }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}
