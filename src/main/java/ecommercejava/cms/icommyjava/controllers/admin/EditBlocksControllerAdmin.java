package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.EditBlocksdto;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.HelpersJson;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Controller
public class EditBlocksControllerAdmin extends MainController {
    @Autowired
    SettingsRepository settingsRepository;


    @GetMapping("/admin/editable-blocks")
    public ModelAndView single (Model model, HttpServletRequest request,
                                @RequestParam(value = "type") String type,
                                @RequestParam(value = "editor", required = false) String editor  ) {


        model.addAttribute("title", "Edit " + type.replaceAll("_",""));
        model.addAttribute("type", type);

        Settings settingsEditor = settingsRepository.findFirstByParam("_page_builder_");
        try {
            model.addAttribute("array", HelpersJson.readyBlocks(settingsEditor .getValue1()));
        }catch (Exception e){
            model.addAttribute("array", new HashMap<>());
        }

        Settings settings = settingsRepository.findFirstByParam(type);
        settings = settings==null ? new Settings(): settings;

        EditBlocksdto editBlocksdto =new EditBlocksdto("", "", Helpers.splitLang(settings.getValue1()));

        String val= settings.getValue();
               val= val==null?"":val;
        if(val.contains("~~~~~~")){
            String [] split = val.split("~~~~~~");
           try{
               if(split.length>0)
                   editBlocksdto.setStyle(split[0]);
           }catch (Exception e){}

            try {
                editBlocksdto.setCss(split[1]);
            }catch (Exception e){}

        }else{
            editBlocksdto.setStyle(val);
        }

        model.addAttribute("row", editBlocksdto);
        model.addAttribute("editor", editor);

        return view("admin::pages/editableblcoks") ;

    }

    /**
     * Save
     * @param request
     * @param redirectAttributes
     * @return
     */

    @PostMapping("/admin/edit-block-store")
    public String store(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        Settings settings = settingsRepository.findFirstByParam(request.getParameter("type"));
        settings = settings==null ? new Settings(): settings;

        settings.setValue(request.getParameter("style")+"~~~~~~"+request.getParameter("css"));
        settings.setValue1(Helpers.saveLang(request.getParameter("text"), settings.getValue1(), request.getParameter("lang"))  );
        settings.setParam(request.getParameter("type") );
        settingsRepository.save(settings);

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return "redirect:" + request.getHeader("Referer");
    }


    /**
     *  bulk update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/edit-block-dublicate") // Map ONLY POST Requests

    public String dublicate (HttpServletRequest request, RedirectAttributes redirectAttributes ,
                             @RequestParam(value = "type") String type,
                             @RequestParam(value = "from") String from,
                             @RequestParam(value = "to") String to ) {

        try{
            Settings settings = settingsRepository.findFirstByParam(type);
            settings = settings==null ? new Settings(): settings;

            String text=  Helpers.splitLangByLang( settings.getValue1(),  from);

            settings.setValue1(Helpers.saveLang(text, settings.getValue1(), to)  );
            settingsRepository.save(settings);
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }
}
