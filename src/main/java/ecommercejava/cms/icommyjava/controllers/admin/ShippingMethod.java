package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.Country;
import ecommercejava.cms.icommyjava.entity.Shipping;
import ecommercejava.cms.icommyjava.repository.CountryRepository;
import ecommercejava.cms.icommyjava.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller // This means that this class is a Controller
public class ShippingMethod extends MainControllerAdmin{

    @Autowired // This means to get the bean called ProductRepository
    private ShippingRepository shippingRepository;

    @Autowired // This means to get the bean called ProductRepository
    private CountryRepository countryRepository;


    @RequestMapping(value ="/admin/checkout-shipping", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam(value ="country", required=false, defaultValue = "0") String country, Model model, HttpServletRequest request) {
            int countryId = Integer.parseInt(country);
            Page<Shipping> shippings = countryId>0?  shippingRepository.findByCountry(countryId, pagination(request, shippingRepository.count(), model, 40)):
                                                shippingRepository.findAll(pagination(request, shippingRepository.count(), model, 40));

         //   Page<Shipping> shippings =  shippingRepository.findAll(pagination(request, shippingRepository.count(), model, 40));


        //String name, Model model
        model.addAttribute("title",  "Shipping methods");

        model.addAttribute("rows",  shippings);



        Iterable<Country> contries =  countryRepository.findAll();
        model.addAttribute("countries", contries);

       return view("admin::pages/shipping") ;
    }


    /**
     *  get data from post and put in database, and after redirect back this use for add and update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/checkout-shipping/store") // Map ONLY POST Requests

    public  String store (HttpServletRequest request, RedirectAttributes redirectAttributes ) {
        Shipping shipping = new Gson().fromJson(Helpers.convertRequestt(request.getParameterMap()), Shipping.class);
        shipping.setShipping_name(request);
        shippingRepository.save(shipping);

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }


    /**
     * show single page for edit
     * @param id
     * @param model
     * @param request
     * @return
     */

    @RequestMapping(value ="/admin/checkout-shipping/update", method = RequestMethod.GET)
    public ModelAndView singleShipping(@RequestParam(value ="id") String id, Model model, HttpServletRequest request) {

        Shipping shipping =  shippingRepository.getOne(Integer.parseInt(id));
        //String name, Model model
        model.addAttribute("title",  "Shipping methods");

        model.addAttribute("row",  shipping);
        Iterable<Country> contries =  countryRepository.findAll( );
        model.addAttribute("countries", contries);
        //List<Categories> categories = categoriesRepository.findByTypeContaining(type, pagination(request, categoriesRepository.count(), model, 40));
        return view("admin::pages/shippingEdit") ;
    }


    /**
     *  bulk update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/checkout-shipping/delete") // Map ONLY POST Requests

    public String delete (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            shippingRepository.deleteById(Integer.parseInt(id));
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/checkout-shipping/bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam("bulkid") List<String> bulkid) {
        String action = request.getParameter("action");
        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        shippingRepository.deleteById(Integer.parseInt(idnr));
                    }
                 } else if ( action.contains("3") ||  action.contains("2")) {
                    for (String idnr : bulkid) {
                        Shipping shipping =  shippingRepository.getOne(Integer.parseInt(idnr));
                        shipping.setShowhide(Integer.parseInt(action));
                        shippingRepository.save(shipping);
                    }
                }
            }

        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }



}
