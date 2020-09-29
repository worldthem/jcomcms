package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.Orders;
import ecommercejava.cms.icommyjava.helper.AuthenticationSystem;
import ecommercejava.cms.icommyjava.services.OrdersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
//@RequestMapping(path={"/orders-page"}) // This means URL's start with /demo (after Application path)
public class OrdersController extends MainController{

    @Autowired
    OrdersServiceImpl ordersService;



   // @Autowired
    //private AuthenticationSystem authenticationSystem;

    @RequestMapping(value ="/orders-list", method = RequestMethod.GET)
    public ModelAndView orders(HttpServletRequest request, Authentication authentication, Model model ){
        int currentUserId = AuthenticationSystem.currentUserId();
        System.out.println();
        if(currentUserId > 0){
            Page<Orders> orders= ordersService.findByUseridOrderByOrdersidDesc(currentUserId,  pagination(request, ordersService.count(), model, 40));
            model.addAttribute("rows", orders);
         }
        model.addAttribute("title", Helpers.translate("Orders List"));

        model.addAttribute("typepage", "list");

      return view("theme::vieworder");
    }


    @RequestMapping(value ="/orders-page/{orderid}", method = RequestMethod.GET)
    public ModelAndView order(@PathVariable String orderid, HttpServletRequest request, HttpServletResponse response, Model model ){


         Orders order = ordersService.findFirstBySecretnr(orderid, model,"");

        model.addAttribute("title", Helpers.translate("Order"));
        model.addAttribute("row", order);
        model.addAttribute("typepage", "single");
        model.addAttribute("uid", AuthenticationSystem.currentUserId());
        model.addAttribute("tmpuid", getCartUserId(request, response));

        return view("theme::vieworder");
    }



}
