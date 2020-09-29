package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.mail.MailService;
import ecommercejava.cms.icommyjava.mail.Maildto;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EmailAdmin extends MainController {
    @Autowired
    SettingsRepository settingsRepository;

    @Autowired
    MailService mailService;

    @Autowired
    private ConfigurableEnvironment environment;

    @ModelAttribute("email")
    public Maildto maildto() {
        return new Maildto();
    }

    @RequestMapping(value ="/admin/email-settings", method = RequestMethod.GET)
    public ModelAndView show(Model model, HttpServletRequest request) {
        Settings settings = settingsRepository.findFirstByParam("email_settings");

        Maildto maildto =  settings ==null ? new Maildto() : new Gson().fromJson(settings.getValue1(), Maildto.class);
        System.out.println("propppp:"+environment.getProperty("spring.mail.properties.mail.smtp.auth"));
        //environment.setValueSeparator();

        model.addAttribute("row", maildto);
        model.addAttribute("title", "E-mail settings");
        return view("admin::pages/email") ;
    }

    @RequestMapping(value ="/admin/email-settings", method = RequestMethod.POST)
    public String store(@ModelAttribute("email") Maildto maildto, HttpServletRequest request) {
        Settings settings = settingsRepository.findFirstByParam("email_settings");
        settings = settings==null? new Settings(): settings;
        settings.setValue1(new Gson().toJson(maildto));
        settings.setParam("email_settings");
        settingsRepository.save(settings);

        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value ="/admin/mail/send-test", method = RequestMethod.POST)
    public String test(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String to = request.getParameter("to");
        if(!to.isEmpty()) {
           redirectAttributes.addFlashAttribute("response", mailService.sendmail(to, "test mail", request.getParameter("body")));
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
