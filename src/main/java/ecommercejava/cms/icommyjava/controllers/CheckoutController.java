package ecommercejava.cms.icommyjava.controllers;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Ordersdto;
import ecommercejava.cms.icommyjava.dto.UserRegistrationDto;
import ecommercejava.cms.icommyjava.dto.billingshipping.FieldsJson;
import ecommercejava.cms.icommyjava.entity.*;
import ecommercejava.cms.icommyjava.helper.*;
import ecommercejava.cms.icommyjava.mail.MailService;
import ecommercejava.cms.icommyjava.paymentmethods.Payme;
import ecommercejava.cms.icommyjava.repository.*;
import ecommercejava.cms.icommyjava.services.CheckoutService;
import ecommercejava.cms.icommyjava.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CheckoutController extends MainController{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    PageRepository pageRepository;

    @Autowired
    List<Payme> paymentTypes;

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    private UserService userService;

    @Autowired
    MailService mailService;

    // Instantiate our encoder
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired // This means to get the bean called ProductRepository
    private SettingsRepository settingsRepository;


    private Float wight(String tmpUserID){
        Float total = 0.0f;
        List<Cart> carts = cartRepository.findByUseridAndTypecart(tmpUserID,2);
        if(carts !=null) {
            for(Cart cart : carts) {
                total= total+cart.getWight();
            }
         }
       return total;
    }


    private String getShippingMethod(Integer countryId, Float total, Float weight, HttpServletRequest request){
         List<Shipping> shippingMethods = shippingRepository.findByShowhide(2);
         ViewHelpersRequest view = new ViewHelpersRequest(request);
        if(shippingMethods !=null && shippingMethods.size()>0){
            String data="";
            String checked="";
           for (Shipping shipping : shippingMethods) {

               if (countryId == shipping.getCountry() && weight <= shipping.getWeight()) {

                   Float priceDelivery =  shipping.getFree_delivery() >= total ?
                                    (shipping.getType_shipping() == 2?  shipping.getPrice() : (total * shipping.getPrice()) /100) :  0.0f  ;

                   data = data+"<li>\n" +
                               "   <label>\n" +
                               "    <input type=\"radio\" "+(checked.isEmpty()? "checked":"")+" autocomplete=\"off\" required onclick=\"shipping_calculate('" + view.price(priceDelivery, null) + "', '" + view.price((total + priceDelivery), null) + "');\" value=\"" + shipping.getId() + "\" name=\"deliverymethod\" class=\"cl_deliverymethod\">\n" +
                                shipping.getShipping_name() +
                               "    </label>\n" +
                               "   <span>" + view.price(priceDelivery, null) + "</span>\n" +
                               "</li>";
                   checked="ddd";
               }
           }
            return data;
         }else {
            return "notshipping";
        }


    }

    @RequestMapping(value ="/checkout/step1", method = RequestMethod.GET)
    public ModelAndView single(HttpServletRequest request, HttpServletResponse response, Model model ){

            String tmpUserID = getCartUserId(request, response);
            Float weight = wight(tmpUserID);

            Float totalcart = checkoutService.cartTotal(tmpUserID,null);

            model.addAttribute("totalcart", totalcart);
            model.addAttribute("wight", weight);

            Orders orders = checkoutService.findFirstBySessionuserOrderByOrdersidDesc(tmpUserID);

            Map<String, String> lastAddress = orders !=null? HelpersJson.convertJsonToMap(orders.getShipping()):new HashMap<>();
            model.addAttribute("lastaddress",  lastAddress);
            String countryid= lastAddress.containsKey("shipping_country")? lastAddress.get("shipping_country") :
                     (lastAddress.containsKey("country")?lastAddress.get("country") : "");

            model.addAttribute("shippingMethods", getShippingMethod((countryid.isEmpty()? 0:Integer.parseInt(countryid)), totalcart, weight, request));

            model.addAttribute("title", (new ViewHelper()).l("Checkout"));

            Settings billing = settingsRepository.findFirstByParam("billing");
            Settings shipping = settingsRepository.findFirstByParam("shipping");
            model.addAttribute("billingFields", AdminHelpers.checkoutFieldsDb(billing != null ? billing.getValue1() : ""));
            model.addAttribute("shippingFields", AdminHelpers.checkoutFieldsDb(shipping != null ? shipping.getValue1() : ""));
            model.addAttribute("titleBilling", billing != null ? Helpers.splitLang(billing.getValue()) : "");
            model.addAttribute("titleShipping", shipping != null ? Helpers.splitLang(shipping.getValue()) : "");
            model.addAttribute("currentEmail", AuthenticationSystem.userEmail());
            model.addAttribute("forseLogin",Helpers.getConfig().getMinOptions().getForseCheckoutLogin());

            if(HelpersJson.getConfig().getMinOptions().getCheckoutTermandcondition() > 0) {
                 Page page = pageRepository.getOne(HelpersJson.getConfig().getMinOptions().getCheckoutTermandcondition());
                 if(page !=null){
                     model.addAttribute("page", page);
                 }
             }

            Iterable<Country> countries = countryRepository.findAll();
            model.addAttribute("countries", countries);


        Settings settings = settingsRepository.findFirstByParam("paymenMethods");
        settings = settings  == null ? new Settings() : settings;
        model.addAttribute("paymentSettings", settings.getValue1List());
        model.addAttribute("payment", paymentTypes);


        return view("theme::checkout") ;
    }

    @RequestMapping(value ="/checkout/get-shipping", method = RequestMethod.POST)
    public String getShipping(HttpServletRequest request, Model model ){
        Integer countryId = request.getParameter("id") !=null?  Integer.parseInt(request.getParameter("id")): 0;
        Float total= request.getParameter("total") !=null?  Float.parseFloat(request.getParameter("total")): 0.0f;
        Float weight = request.getParameter("kg") !=null?  Float.parseFloat(request.getParameter("kg")): 0.0f;
        String answer =getShippingMethod(countryId, total, weight,  request);
        model.addAttribute("content", answer.contains("notshipping") ? "" : answer);
        return viewpatNohtml("cms::content");
    }




    @RequestMapping(value ="/checkout/step2", method = RequestMethod.POST)
    public String step2(HttpServletRequest request, HttpServletResponse response, Model model ){
        Settings billing = settingsRepository.findFirstByParam("billing");
        Settings shipping = settingsRepository.findFirstByParam("shipping");
        Map<String, String> data= new HashMap<>();

       if(billing != null){
            LinkedHashMap<String, FieldsJson> bill = AdminHelpers.checkoutFieldsDb(billing != null ? billing.getValue1() : "");
              if(bill.size()>0){
               for(Map.Entry<String, FieldsJson> entry: bill.entrySet()) {
                   if(!entry.getKey().contains("password")){
                     data.put(entry.getKey(), request.getParameter(entry.getKey()));
                   }
               }
            }
         }

      String needShipp = request.getParameter("needshipaddress");
        if(shipping != null && needShipp !=null){
            LinkedHashMap<String, FieldsJson> shipp = AdminHelpers.checkoutFieldsDb(shipping != null ? shipping.getValue1() : "");
            if(shipp.size()>0){
                for(Map.Entry<String, FieldsJson> entry: shipp.entrySet()) {
                    data.put("shipping_"+entry.getKey(), "shipping_"+request.getParameter(entry.getKey()));
                }
            }
        }

        String email = request.getParameter("email") == null ? "" : request.getParameter("email");
        String pass = request.getParameter("password");
        if(pass !=null && !pass.isEmpty() && !AuthenticationSystem.isLogged() && !email.isEmpty()){
            try {
                Users user = userService.findFirstByEmail(email);
              if(user == null){
                  UserRegistrationDto registrationDto = new UserRegistrationDto(
                          request.getParameter("name"), request.getParameter("lastname"),
                          email,  pass, "ROLE_USER"
                    );
                 userService.save(registrationDto);
                 request.login(registrationDto.getEmail(), registrationDto.getPassword());
              }else{
                  if (encoder.matches(pass,  user.getPassword())){
                      request.login(email, pass);
                  }
              }
            }catch (Exception e){
                System.out.println("Error insert user is:"+e);
            }
         }


        String paymentMethod = request.getParameter("payment");
        String tmpUserID = getCartUserId(request, response);

        float totalcart = checkoutService.cartTotal(tmpUserID, request.getParameter("deliverymethod"));

        Ordersdto ordersdto = new Ordersdto(tmpUserID,
                0,
                "start",
                Helpers.randomString(50),
                request.getParameter("deliverymethod"),
                 "",
                (new Gson()).toJson(data), Float.toString(totalcart),paymentMethod);


               Orders orders = checkoutService.updateOrder(ordersdto);
        System.out.println("ordersId 1:"+orders.getOrdersid());
                List<Cart> cartList =cartRepository.findByUseridAndTypecart(tmpUserID, 2 );
                for(Cart cart : cartList){
                     cart.setOrdersid(orders.getOrdersid());
                     cartRepository.save(cart);
                }


            String status = "pending";
        for (Payme payme: paymentTypes) {
            if(payme.getId().contains(paymentMethod)){
                  status =payme.makePayment(request, totalcart, orders.getOrdersid());
                  break;
            }
         }

         if(!status.contains("redirect")) {

            ordersdto.setStatus(status);
            ordersdto.setOrdersId(orders.getOrdersid());
            ordersdto.setSecretnr(orders.getSecretnr());
            checkoutService.updateLastStep( ordersdto);

        }


         return status.contains("redirect")? status :  "redirect:"+Helpers.baseurl()+"orders-page/"+ orders.getSecretnr();

    }

}
