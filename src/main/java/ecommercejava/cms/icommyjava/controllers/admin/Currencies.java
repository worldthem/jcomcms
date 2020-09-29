package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.dto.Config.Currency;
import ecommercejava.cms.icommyjava.helper.AdminHelpers;
import ecommercejava.cms.icommyjava.helper.HelpersJson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller // This means that this class is a Controller
public class Currencies extends MainController {



    @RequestMapping(value ="/admin/money-currencies", method = RequestMethod.GET)
    public ModelAndView showattribute(Model model, HttpServletRequest request) {

        model.addAttribute("title",  " Currencies");

        model.addAttribute("fields", AdminHelpers.checkoutFields("currencies"));
        model.addAttribute("fieldsConfig", HelpersJson.getConfig().getCurrencies());

        return view("admin::pages/currencies") ;
    }


    @RequestMapping(value ="/admin/money-currencies-save", method = RequestMethod.POST)
    public String store(HttpServletRequest request) {


        LinkedHashMap<String, String> fields =  AdminHelpers.checkoutFields("currencies");

        LinkedHashMap<String, Currency> map = new LinkedHashMap<>();

        for(Map.Entry<String, String> entry: fields.entrySet()) {

            if(request.getParameter(entry.getKey()+"[enable]")!=null) {
                Currency currency = new Currency();
                currency.setName(entry.getKey());
                currency.setCode(request.getParameter(entry.getKey()+"[code]"));
                currency.setMain(request.getParameter(entry.getKey()+"[main]"));
                currency.setType(request.getParameter(entry.getKey()+"[type]"));
                currency.setRate(request.getParameter(entry.getKey()+"[rate]"));
                map.put(entry.getKey(), currency);
            }
        }
        Config config = HelpersJson.getConfig();
        config.setCurrencies(map);
        config.save();
        //HelpersFile.updateConfig(config);

        return  "redirect:" + request.getHeader("Referer");
    }

}
