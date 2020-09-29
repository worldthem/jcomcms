package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.paymentmethods.Payme;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class PaymentAdmin extends MainController {

    @Autowired
    List<Payme> paymentTypes;

    @Autowired
    SettingsRepository settingsRepository;

   @RequestMapping(value ="/admin/checkout-payment", method = RequestMethod.GET)
    public ModelAndView show(Model model, HttpServletRequest request) {
       Settings settings = settingsRepository.findFirstByParam("paymenMethods");
       settings = settings  == null ? new Settings() : settings;

       List<String> list = settings.getValue1List();

       model.addAttribute("listActive", list);
        model.addAttribute("title", "Payments");
        model.addAttribute("rows", paymentTypes);
        model.addAttribute("page", "list");
       return view("admin::pages/payment");
    }

    @RequestMapping(value ="admin/checkout-payment/single", method = RequestMethod.GET)
    public ModelAndView single(@RequestParam(value ="payment") String payment, Model model, HttpServletRequest request) {
        for(Payme p:paymentTypes){
            if(p.getId().contains(payment)) {
                model.addAttribute("title", p.getTitle());
                model.addAttribute("row", p);
                model.addAttribute("content", p.admin());
                break;
             }
          }
        Settings settings = settingsRepository.findFirstByParam("paymenMethods");
        settings = settings  == null ? new Settings() : settings;

        List<String> list = settings.getValue1List();
        model.addAttribute("enable", !list.contains(payment) ? false: true);

      return view("admin::pages/payment");
    }

    @RequestMapping(value ="/admin/checkout-payment/store", method = RequestMethod.POST)
    public String store(@RequestParam(value ="payment") String payment, HttpServletRequest request, MultipartHttpServletRequest requestMultipart) {

       for(Payme p:paymentTypes){
            if(p.getId().contains(payment)) {
                p.store(requestMultipart);
                break;
            }
        }

        Settings settings = settingsRepository.findFirstByParam("paymenMethods");
        settings = settings  == null ? new Settings() : settings;

        List<String> list = settings.getValue1List();

        if(request.getParameter("enable") !=null){
            if(!list.contains(payment)){ list.add(payment); }
        }else{
            if(list.contains(payment)){ list.remove(payment);  }
        }


        settings.setParam("paymenMethods");
        settings.setValue1((new Gson()).toJson(list));
        settingsRepository.save(settings);

        return  "redirect:" + request.getHeader("Referer");
    }


    @RequestMapping(value ="/admin/checkout-payment/onoff", method = RequestMethod.GET)
    public String onoff(@RequestParam(value ="payment") String payment, @RequestParam(value ="onoff") String onoff, HttpServletRequest request ) {

        Settings settings = settingsRepository.findFirstByParam("paymenMethods");
        settings = settings  == null ? new Settings() : settings;

        List<String> list = settings.getValue1List();

        if(onoff.contains("on")){
            if(!list.contains(payment)){ list.add(payment); }
        }else{
            if(list.contains(payment)){ list.remove(payment);  }
        }

        settings.setParam("paymenMethods");
        settings.setValue1((new Gson()).toJson(list));
        settingsRepository.save(settings);

        return  "redirect:" + request.getHeader("Referer");
    }



}
