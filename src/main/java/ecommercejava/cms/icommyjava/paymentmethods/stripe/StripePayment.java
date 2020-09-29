package ecommercejava.cms.icommyjava.paymentmethods.stripe;


import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Ordersdto;
import ecommercejava.cms.icommyjava.entity.Orders;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.Cookies;
import ecommercejava.cms.icommyjava.paymentmethods.Payme;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import ecommercejava.cms.icommyjava.services.CheckoutService;
import ecommercejava.cms.icommyjava.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StripePayment extends MainController implements Payme {

    @Autowired
    SettingsRepository settingsRepository;

    @Autowired
    CheckoutService checkoutService;

    /**
     * default id enter name wthout space [Az09]
     * @return
     */
    public String getId(){
        return "stripePayment";
    }

    /**
     * the data in databasetable settings by param getId() in json format
     * @return
     */
    private StripeModule getSettings(){
        Settings settings = settingsRepository.findFirstByParam(getId());

        try{
            if(settings != null) {
                String val = settings.getValue1();
                return val.isEmpty()? new StripeModule() :  (new Gson()).fromJson(val, (Type) StripeModule.class);
            }
        }catch (Exception e){ }
        return new StripeModule();
    }

    /**
     * get title of payment
     * @return
     */
    public String getTitle(){
        return getSettings().getTitle().isEmpty() ? "Check payment": getSettings().getTitle();
    }

    /**
     * get description
     * @return
     */
    public String getDescription(){
        return getSettings().getDescription().isEmpty()? "Check payment you can pay bu check for you online staff" : getSettings().getDescription();
    }

    /**
     * this part will show the in the payment list on checkout page
     * @return
     */
    public String frontEnd(){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("row", getSettings());
        templateModel.put("idPayment", getId());
        return templateHtml(templateModel, "payment::stripepayment/frontend");
    }

    /**
     * this part will show the admin options
     * @return
     */
    public String admin(){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("row", getSettings());
        return templateHtml(templateModel, "payment::stripepayment/admin");
    }

    /**
     * store all the information from the admin config put in json and save in db settings
     * @param request
     * @return
     */
    public String store(MultipartHttpServletRequest request){
        String image = request.getParameter("image");
        MultipartFile file = request.getFile("file");

        // upload main image
        if(file != null){
            if(!file.getOriginalFilename().isEmpty()){
                image = (new StorageService()).uploadSingleImg(file, "content/views/payment/stripepayment/img");
            }
        }

        StripeModule moduleData =  getSettings();
        moduleData.setLang(request.getParameter("lang"));
        moduleData.setTitle(request.getParameter("title"));
        moduleData.setDescription(request.getParameter("description"));
        moduleData.setPublicKey(request.getParameter("publickey"));
        moduleData.setSecretKey(request.getParameter("secretkey"));
        moduleData.setNoimage(request.getParameter("noimage"));
        moduleData.setImage(image);

        Settings settings = settingsRepository.findFirstByParam(getId());
        if(settings  == null){
            settings = new Settings();
        }
        settings.setParam(getId());
        settings.setValue1((new Gson()).toJson(moduleData));
        settingsRepository.save(settings);

        return "";
    }


    /**
     * when costomers press checkout page
     * @return  = "pending" , "onhold", "success", "canceled", "failed", "redirect:https://url.com/url/data"
     */
    public String makePayment(HttpServletRequest request, Float totalcart, Integer orderId){

        String key= getSettings().getSecretKey();

        if(key!=null) {
               Orders order = checkoutService.getOne(orderId);
               if(order !=null) {
                   try {
                       PaymentIntent paymentIntent = PaymentIntent.retrieve( order.getPaymenttoken());
                       return paymentIntent.getStatus().contains("succeeded") ? "success" : "failed";
                   } catch (Exception e) { }
               }
        }

       return "pending";
     }

    /**
     * when costomers press checkout page
     * @return
     */

    @GetMapping(path = "/payment-stripe/stripe-get-secret")
    public @ResponseBody
    Map<String, String> preparePayment(HttpServletRequest request){
        String tmpUserID = Cookies.get("useridtmp", request);

        float totalcart = checkoutService.cartTotal(tmpUserID, request.getParameter("d"));

        String key= getSettings().getSecretKey();
        Map<String, String> map = new HashMap();
        if(key!=null) {

            Ordersdto ordersdto = new Ordersdto(tmpUserID,
                                         0,
                                          "start",
                                                Helpers.randomString(50),
                                                request.getParameter("d"), "","", Float.toString(totalcart), getId());

            Orders order = checkoutService.updateOrder(ordersdto);

            Stripe.apiKey = key;
            long total =  (long)totalcart * 100;


            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(total)
                            .setCurrency(Helpers.getMainCurrency().getName())
                            .addPaymentMethodType("card")
                            .setDescription("Order ID: "+order.getOrdersid()+" ; from: "+Helpers.baseurl() )
                            .build();
            try{
                PaymentIntent paymentIntent = PaymentIntent.create(params);
                map.put("client_secret", paymentIntent.getClientSecret());
                map.put("paymentid", paymentIntent.getId());

                // put payment id for future data statistic
                order.setPaymenttoken(paymentIntent.getId());
                checkoutService.save(order);

               return  map;
            }catch (Exception e){  System.out.println("payment err:" +e); }

        }


        return new HashMap<>();
    }
}
