package ecommercejava.cms.icommyjava.controllers.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.contactform.Contactform;
import ecommercejava.cms.icommyjava.dto.contactform.FieldsForm;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ContactFormControllerAdmin extends MainController {
    @Autowired
    SettingsRepository settingsRepository;


    @ModelAttribute("form")
    public Contactform contactform() {
        return new Contactform();
    }


    @GetMapping("/admin/contact-form")
    public ModelAndView show (Model model, HttpServletRequest request, @RequestParam(value = "id", required = false) String id ) {

        model.addAttribute("title", "Contact forms");
        model.addAttribute("id", id);

        List<Settings> settings = settingsRepository.findByParamOrderByIdDesc("_contact_forms_");
        model.addAttribute("rows", settings);
        return view("admin::pages/contactforms") ;

    }

    @GetMapping("/admin/contact-form-single")
    public ModelAndView single (Model model, HttpServletRequest request, @RequestParam(value = "id") String id ) {
        Settings settings = settingsRepository.getOne(Integer.parseInt(id));

        model.addAttribute("title", "Contact forms");
        model.addAttribute("id", id);
        model.addAttribute("row", settings);
        model.addAttribute("field", new FieldsForm());
        try {
            model.addAttribute("form", (new Gson()).fromJson(settings.getValue1(), Contactform.class));
        }catch (Exception e){
            model.addAttribute("form", new Contactform());
         }
        return view("admin::pages/contactforms") ;

    }

    /**
     * Save
     * @param request
     * @param redirectAttributes
     * @return
     */

    @PostMapping("/admin/contact-form-store")
    public String store(@ModelAttribute("form" ) Contactform contactform, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Settings settings = new Settings();
        try {
              settings = settingsRepository.getOne(Integer.parseInt(request.getParameter("id")));
              String vv = settings.getValue();
        }catch (Exception e){ }


         Gson gson = new GsonBuilder().disableHtmlEscaping().create();

          if(contactform !=null) {
            settings.setValue1(gson.toJson(contactform));
          }

         settings.setValue( request.getParameter("value"));
         settings.setParam("_contact_forms_");
         settingsRepository.save(settings);

        return "redirect:" + request.getHeader("Referer");
    }

    /**
     *  bulk update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/contact-form-distroy") // Map ONLY POST Requests

    public String delete (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            settingsRepository.deleteById(Integer.parseInt(id));
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}
