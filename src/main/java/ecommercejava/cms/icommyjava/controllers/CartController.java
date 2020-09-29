package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.Cart;
import ecommercejava.cms.icommyjava.entity.Cupon;
import ecommercejava.cms.icommyjava.entity.CuponApplied;
import ecommercejava.cms.icommyjava.entity.Product;
import ecommercejava.cms.icommyjava.helper.ViewHelper;
import ecommercejava.cms.icommyjava.repository.CartRepository;
import ecommercejava.cms.icommyjava.repository.CuponAppliedRepository;
import ecommercejava.cms.icommyjava.repository.CuponRepository;
import ecommercejava.cms.icommyjava.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class CartController extends  MainController{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CuponAppliedRepository cuponAppliedRepository;

    @Autowired
    CuponRepository cuponRepository;

    @Autowired
    ProductRepository productRepository;


    @RequestMapping(value ="/cart/get", method = RequestMethod.GET)
    public ModelAndView single(HttpServletRequest request, HttpServletResponse response, Model model ){

        String tmpUserID = getCartUserId(request, response);

        List<Cart> cart = cartRepository.findByUseridAndTypecart(tmpUserID, 2);
        model.addAttribute("rows", cart);

        model.addAttribute("title", (new ViewHelper()).l("Cart"));
        try {
            CuponApplied cupon_applied = cuponAppliedRepository.findByUseridAndOrdersid(tmpUserID, null);
            model.addAttribute("cupon", cupon_applied.getCuponmodule());
        }catch (Exception e){}


        return view("theme::cart") ;
    }

    /**
     * this method will return the cart so when update or delete ittem from cart we actualy return all the cart
     * @param model
     * @param tmpUserID
     * @return
     */
    private String getCart(Model model, String tmpUserID){
        List<Cart> cart = cartRepository.findByUseridAndTypecart(tmpUserID, 2);
        model.addAttribute("rows", cart);
        model.addAttribute("baseurl", Helpers.baseurl());

        try {
            CuponApplied cupon_applied = cuponAppliedRepository.findByUseridAndOrdersid(tmpUserID, null);
            model.addAttribute("cupon", cupon_applied.getCuponmodule());
          }catch (Exception e){ }

        String view= Helpers.viewExist("theme::layouts/cartittem")? "theme::layouts/cartittem": "cms::cart/cartittem";
        return viewpatNohtml(view) ;
    }

    /**
     * will apply the cupon
     * @param request
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value ="/cart/cupon-apply", method = RequestMethod.POST)
    public String cupponApply(HttpServletRequest request, Model model, HttpServletResponse response){
        String tmpUserID = getCartUserId(request, response);
        String cupponName = request.getParameter("cuppon");

        if(cupponName.isEmpty() || cupponName!=null){
            try{
                Cupon cupon= cuponRepository.findByCupon(cupponName);
                if(cupon !=null){
                    CuponApplied cupon_applied = cuponAppliedRepository.findByUseridAndOrdersid( tmpUserID, null);

                    if(cupon_applied == null){
                                 cupon_applied= new CuponApplied();
                    }

                    cupon_applied.setUserid(tmpUserID);
                    cupon_applied.setCuponmodule(cupon);
                    cuponAppliedRepository.save(cupon_applied);
                }
            }catch (Exception e){}


        }
      return getCart(model, tmpUserID);
    }


    @RequestMapping(value ="/cart/operations", method = RequestMethod.POST)
    public String operations(@RequestParam(value ="action") String action, @RequestParam(value ="id") String id, @RequestParam(value ="val") String val, HttpServletRequest request, Model model, HttpServletResponse response){

        String tmpUserID = getCartUserId(request, response);
        Cart cart = cartRepository.getOne(Integer.parseInt(id));
        if(cart !=null) {
           try{
                if (action.contains("increment") ) {
                     cart.setQty(cart.getQty()+Integer.parseInt(val));
                    cartRepository.save(cart);
                }else if(action.contains("decrement")){
                    int nr = cart.getQty()<= Integer.parseInt(val)? 1 : (cart.getQty()-Integer.parseInt(val));
                    cart.setQty(nr);
                    cartRepository.save(cart);
                }else if(action.contains("update")){
                    int nr = Integer.parseInt(val) == 0 ? 1 : Integer.parseInt(val);
                    cart.setQty(nr);
                    cartRepository.save(cart);
                }else if(action.contains("del")){
                    cartRepository.deleteById(cart.getCartid());
                }
             }catch (Exception e){}
         }

       return getCart(model, tmpUserID);

    }



    @RequestMapping(value ="/cart/add", method = RequestMethod.POST)
    public String add(HttpServletRequest request, Model model, HttpServletResponse response){
        String tmpUserID = getCartUserId(request, response);
        int pId = Integer.parseInt(request.getParameter("id"));
        int qtu = Integer.parseInt(request.getParameter("qtu"));
        String options = request.getParameter("options") == null? "" : request.getParameter("options");

        Cart cart =cartRepository.findByUseridAndProduct_ProductidAndOptionsidAndTypecart(tmpUserID, pId, options, 2);
        Product product = productRepository.getOne(pId);

        if(product !=null) {
            if (cart == null) {
                cart = new Cart();
            }
             int newQtu = cart == null ? qtu : cart.getQty() + qtu;
            cart.setUserid(tmpUserID);
            cart.setProduct(product);
            cart.setQty(product.getQtu()<=newQtu ? product.getQtu() : newQtu);
            cart.setOptionsid(options);
            cart.setTypecart(2);
            cartRepository.save(cart);
        }

        List<Cart> carts = cartRepository.findByUseridAndTypecart(tmpUserID, 2);
        model.addAttribute("rows", carts);
        model.addAttribute("baseurl", Helpers.baseurl());

        String view= Helpers.viewExist("theme::layouts/small_cartittem")? "theme::layouts/small_cartittem": "cms::small_cartittem";
        return viewpatNohtml(view) ;

    }


}
