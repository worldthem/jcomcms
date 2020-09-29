package ecommercejava.cms.icommyjava.paymentmethods.paypal;


import com.google.gson.Gson;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Ordersdto;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.Cookies;
import ecommercejava.cms.icommyjava.paymentmethods.Payme;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import ecommercejava.cms.icommyjava.services.CheckoutService;
import ecommercejava.cms.icommyjava.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaypalPayment extends MainController implements Payme {
    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    PaypalService service;

    @Autowired
    CheckoutService checkoutService;


    private static final long serialVersionUID = 1L;
    /**
     * default id enter name wthout space [Az09]
     * @return
     */
    public String getId(){
        return "paypalPayment";
    }

    /**
     * the data in databasetable settings by param getId() in json format
     * @return
     */
    public PaypalModule getSettings(){
        Settings settings = settingsRepository.findFirstByParam(getId());

        try{
            if(settings != null) {
                String val = settings.getValue1();
                return val.isEmpty()? new PaypalModule() :  (new Gson()).fromJson(val, (Type) PaypalModule.class);
            }
        }catch (Exception e){ }
        return new PaypalModule();
    }

    /**
     * get title of payment
     * @return
     */
    public String getTitle(){
        return getSettings().getTitle();
    }

    /**
     * get description
     * @return
     */
    public String getDescription(){
        return getSettings().getDescription();
    }

    /**
     * this part will show the in the payment list on checkout page
     * @return
     */
    public String frontEnd(){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("row", getSettings());
        templateModel.put("idPayment", getId());
        return templateHtml(templateModel, "payment::paypalpayment/frontend");
    }

    /**
     * this part will show the admin options
     * @return
     */
    public String admin(){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("row", getSettings());
        return templateHtml(templateModel, "payment::paypalpayment/admin");
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
                image = (new StorageService()).uploadSingleImg(file, "content/views/payment/paypalpayment/img");
            }
        }

        PaypalModule moduleData =  getSettings();
        moduleData.setLang(request.getParameter("lang"));
        moduleData.setTitle(request.getParameter("title"));
        moduleData.setDescription(request.getParameter("description"));
        moduleData.setNoimage(request.getParameter("noimage"));
        moduleData.setPublicKey(request.getParameter("publickey"));
        moduleData.setSecretKey(request.getParameter("secretkey"));
        moduleData.setMode(request.getParameter("mode"));
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
     * @return  = "pending" , "onhold", "success", "canceled", "redirect:https://url.com/url/data"
     */

    //https://www.codejava.net/coding/how-to-integrate-paypal-payment-into-java-web-application
    public String makePayment(HttpServletRequest request, Float totalcart, Integer orderId){
        try {

            PaypalOrderDetail orderDetail = new PaypalOrderDetail("Order Id:" + orderId, totalcart, orderId);
            PaypalPayerInfo payerInfo = new PaypalPayerInfo(request.getParameter("name"),request.getParameter("lastname"), request.getParameter("email"));

            PaypalService paymentServices = new PaypalService();
            PaypalModule paypalModule = getSettings();
            paymentServices.setAuthotrisation(paypalModule.getPublicKey(), paypalModule.getSecretKey(), paypalModule.getMode());
            String approvalLink = paymentServices.authorizePayment(orderDetail, payerInfo );
            return  "redirect:"+approvalLink;

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }

        return "onhold";
    }

    @GetMapping("/paypal-cancel-payment")
    public String canseledPayment(HttpServletRequest request){

        return "redirect:"+ Helpers.baseurl()+"checkout/step1";
    }

    @GetMapping("/paypal-success-payment")
    public String finishPayment(HttpServletRequest request){
        String totalcart = "0";
        String status = "onhold";
        int orderid = 0;
        String paymentId = request.getParameter("paymentId");
        try {

            String payerId = request.getParameter("PayerID");
            PaypalService paymentServices = new PaypalService();

            PaypalModule paypalModule = getSettings();
            paymentServices.setAuthotrisation(paypalModule.getPublicKey(), paypalModule.getSecretKey(), paypalModule.getMode());

            Payment payment = paymentServices.executePayment(paymentId, payerId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);

            //https://developer.paypal.com/docs/paypal-plus/germany/integrate/execute-payment/#sample-response
            //System.out.println("data info " + transaction.getAmount().getTotal());
              //System.out.println("data info state " + payment.getState());
              //System.out.println("data info intent " + payment.getIntent());

              orderid = transaction.getCustom()!=null ? Integer.parseInt(transaction.getCustom()): orderid;

              totalcart =transaction.getAmount().getTotal();
              status = "success";

          } catch (PayPalRESTException e) {   System.out.println("errorpayemnt " + e);  }

        String tmpUserID = Cookies.get("useridtmp", request);

        Ordersdto ordersdto = new Ordersdto(tmpUserID,
                                            orderid,
                                            status,
                                            Helpers.randomString(50),
                                            "",
                                            paymentId,
                                            "", totalcart, getId());


        return   "redirect:"+Helpers.baseurl()+"orders-page/"+checkoutService.updateLastStep(ordersdto);

    }

}
