package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Country;
import ecommercejava.cms.icommyjava.entity.Orders;
import ecommercejava.cms.icommyjava.mail.MailService;
import ecommercejava.cms.icommyjava.repository.CountryRepository;
import ecommercejava.cms.icommyjava.services.OrdersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrdersControllerAdmin extends MainController {

    @Autowired
    OrdersServiceImpl ordersService;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    MailService mailService;

    @GetMapping("/admin")
    public ModelAndView show (Model model, HttpServletRequest request,
                              @RequestParam(value ="search", required = false) String search,
                              @RequestParam(value ="userid", required = false) String userid) {
        Page<Orders> orders = null;

      if(userid != null) {
           orders =ordersService.findByUseridOrderByOrdersidDesc(Integer.parseInt(userid), pagination(request, ordersService.count(), model, 40));
      }else {
            orders = search != null ?
                    ordersService.findByStatusContainingOrShippingContainingOrSecretnrContainingOrderByOrdersidDesc(search, search, search, pagination(request, ordersService.count(), model, 40)) :
                    ordersService.findByStatusNotOrderByOrdersidDesc("start", pagination(request, ordersService.count(), model, 40));
      }

        Iterable<Country> countries = countryRepository.findAll();

        model.addAttribute("title", "View orders");
        model.addAttribute("rows", orders);
        model.addAttribute("countries", countries);
        return view("admin::pages/orders") ;
    }

    @RequestMapping(value ="/admin/vieworder", method = RequestMethod.GET)
    public ModelAndView single (Model model, HttpServletRequest request, @RequestParam(value ="id", required = false) String orderid ) {

        Orders order = ordersService.findFirstBySecretnr(orderid, model,"admin");

        model.addAttribute("title", Helpers.translate("Order"));
        model.addAttribute("row", order);
        return view("admin::pages/vieworder") ;
    }

    @RequestMapping(value ="/admin/order-change-status", method = RequestMethod.GET)
    public String changeStatus (HttpServletRequest request, @RequestParam(value ="id") String orderid , @RequestParam(value ="status") String status) {
         Orders orders = ordersService.getOne(Integer.parseInt(orderid));
         orders.setStatus(status);
         ordersService.save(orders);


         mailService.emailOrders(status, orders);


        return "redirect:" + request.getHeader("Referer");
    }

    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/orders-bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam(value = "bulkid", required = false) List<String> bulkid) {
        String action = request.getParameter("action");
        String search = request.getParameter("s");

        if(!search.isEmpty() && action.isEmpty())
            return "redirect:/admin?search="+search  ;

        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        ordersService.deleteById(Integer.parseInt(idnr));
                    }
                }else if(!action.contains("del") && !action.isEmpty()){

                    for (String idnr : bulkid) {
                        Orders orders = ordersService.getOne(Integer.parseInt(idnr));
                             orders.setStatus(action);
                        ordersService.save(orders);
                    }
                }
            }
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}
