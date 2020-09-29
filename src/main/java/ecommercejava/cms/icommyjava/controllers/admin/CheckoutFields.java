package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.billingshipping.FieldsJson;
import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.AdminHelpers;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller // This means that this class is a Controller
public class CheckoutFields extends MainController {


    @Autowired // This means to get the bean called ProductRepository
    private SettingsRepository settingsRepository;

    @RequestMapping(value ="/admin/checkout-fields/{fieldsType}", method = RequestMethod.GET)
     public ModelAndView showattribute(@PathVariable String fieldsType, Model model, HttpServletRequest request) {

        Settings fieldsDb = settingsRepository.findFirstByParam(fieldsType);

        model.addAttribute("title", fieldsType.substring(0, 1).toUpperCase() + fieldsType.substring(1)  +" Fields");

        model.addAttribute("fieldsType", fieldsType);
        model.addAttribute("titleBox", fieldsDb !=null? new Gson().fromJson(fieldsDb.getValue(), Lang.class):null);

        model.addAttribute("fields", AdminHelpers.checkoutFields(fieldsType));
        model.addAttribute("fieldsdb",  AdminHelpers.checkoutFieldsDb( fieldsDb !=null? fieldsDb.getValue1():""));

        return view("admin::pages/checkoutfields") ;
    }

    @RequestMapping(value ="/admin/checkout-fields-save", method = RequestMethod.POST)
    public String store(HttpServletRequest request) {
         String fieldsType = request.getParameter("fieldsType");

         LinkedHashMap<String, String> fields =  AdminHelpers.checkoutFields(fieldsType);

         LinkedHashMap<String, FieldsJson> map = new LinkedHashMap<>();

        for(Map.Entry<String, String> entry: fields.entrySet()) {

            if(request.getParameter(entry.getKey()+"[enable]")!=null) {
                FieldsJson fieldsJson = new FieldsJson();
                Lang title = new Lang();
                for (int i = 0; i < Helpers.listLang().size(); i++) {
                    title.set(Helpers.listLang().get(i), request.getParameter(entry.getKey()+"[title][" + Helpers.listLang().get(i) + "]"));
                }
                fieldsJson.setTitle(title);
                fieldsJson.setShowTitle(request.getParameter(entry.getKey()+"[showTitle]"));
                fieldsJson.setRequired(request.getParameter(entry.getKey()+"[required]"));
                fieldsJson.setBoxLength(request.getParameter(entry.getKey()+"[boxLength]"));
                fieldsJson.setBoxType(request.getParameter(entry.getKey()+"[boxType]"));
                map.put(entry.getKey(), fieldsJson);
             }
         }

        Lang title = new Lang();
        for (int i = 0; i < Helpers.listLang().size(); i++) {
            title.set(Helpers.listLang().get(i), request.getParameter( "title[" + Helpers.listLang().get(i) + "]"));
        }


        Settings fieldsDb = settingsRepository.findFirstByParam(fieldsType)==null? new Settings(): settingsRepository.findFirstByParam(fieldsType);


        fieldsDb.setParam(fieldsType);
        fieldsDb.setValue(new Gson().toJson(title));
        fieldsDb.setValue1(new Gson().toJson(map));
        settingsRepository.save(fieldsDb);


        return  "redirect:" + request.getHeader("Referer");
    }


}
