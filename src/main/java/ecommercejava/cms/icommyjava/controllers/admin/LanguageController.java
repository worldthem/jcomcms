package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.Language;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
public class LanguageController extends MainController {

    @GetMapping("/admin/general-settings/language")
    public ModelAndView simpleHtml(Model model, @RequestParam(value ="lang", required = false) String lang, HttpServletRequest request ) {
        //String name, Model model
        model.addAttribute("title", "Languages");
        model.addAttribute("languagelist", HelpersFile.getConfigXmlData("config", "languages", "content/config/lang.xml") );
        model.addAttribute("configlang", Helpers.listLang() );
        model.addAttribute("adminlang", Helpers.getConfig().getAdminlang());

        model.addAttribute("langtranslate", lang );
        if(lang !=null  ) {
            model.addAttribute("englishjson", Language.engLang !=null? Language.engLang : Helpers.getEnLang());
            model.addAttribute("totranslatejson", Helpers.getLang(lang));
        }

        return view("admin::pages/language") ;
    }

    @GetMapping("/admin/general-settings/language/operation")
    public String operation(@RequestParam(value ="lang") String lang, @RequestParam(value ="action") String action, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Config config = Helpers.getConfig();

        List<String> listlang= config.getLanguages();
        listlang.remove(lang);
         if(action.contains("defaul")) {
            listlang.add(0, lang);
         }
         config.setLanguages(listlang);
         config.save();

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/admin/general-settings/language/set")
    public String setlang(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String lang = request.getParameter("language");
        String adminLang = request.getParameter("adminlanguage");
        Config config = Helpers.getConfig();
       if(!lang.isEmpty()) {
           List<String> listlang= config.getLanguages();
           listlang.remove(lang);
           listlang.add(0, lang);
           config.setLanguages(listlang);
        }

        config.setAdminlang(adminLang);
        config.save();

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/admin/general-settings/language/store")
    public String store(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String lang = request.getParameter("lang");

        Map<String, String[]> langTranslated = request.getParameterMap();
        Map<String,String> saveLang = new LinkedHashMap<>();
        for(Map.Entry<String, String[]> entry:langTranslated.entrySet()){
            if(entry.getKey().contains("langtranslate")){
                System.out.println("key is"+entry.getKey());
                String key1 = Helpers.splitInputKey(entry.getKey(), 0);

                if(!key1.isEmpty())
                    saveLang.put(key1,entry.getValue()[0]);
             }
        }

        try {
            Helpers.updateFile(new Gson().toJson(saveLang), "content/lang/"+lang+".json");
        }catch (Exception e){}

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }

}
