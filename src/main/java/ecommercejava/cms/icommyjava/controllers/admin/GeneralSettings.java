package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.dto.Config.MainOptions;
import ecommercejava.cms.icommyjava.entity.Page;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.HelpersJson;
import ecommercejava.cms.icommyjava.repository.PageRepository;
import ecommercejava.cms.icommyjava.services.SettingsService;
import ecommercejava.cms.icommyjava.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller // This means that this class is a Controller
public class GeneralSettings extends MainController {

    @Autowired // This means to get the bean called ProductRepository
    private PageRepository pageRepository;

    @Autowired
    private SettingsService settingsRepository;

    @RequestMapping(value ="/admin/general-settings", method = RequestMethod.GET)
    public ModelAndView show(Model model, HttpServletRequest request) {
        model.addAttribute("title", "General settings");
        model.addAttribute("row", HelpersJson.getConfig().getMinOptions());
        model.addAttribute("row", HelpersJson.getConfig().getMinOptions());
        Settings settings =settingsRepository.findFirstByParam("_footerjs_");
        model.addAttribute("_footerjs_", settings !=null? settings.getValue1():"");

        List<Page> pages = pageRepository.findByType("pages");
        model.addAttribute("pages", pages);
        return view("admin::pages/generalSettings") ;
    }


    /**
     *  get data from post and put in database, and after redirect back this use for add and update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/general-settings/store") // Map ONLY POST Requests

    public  String store (HttpServletRequest request, RedirectAttributes redirectAttributes,
                          @RequestParam(value ="logofile", required=false, defaultValue = "null") MultipartFile logofile,
                          @RequestParam(value ="faviconfile", required=false, defaultValue = "null") MultipartFile faviconfile) {


         Config config = HelpersJson.getConfig();

         MainOptions mainOptions = new Gson().fromJson(Helpers.convertRequestt(request.getParameterMap()), MainOptions.class);

        // upload logo
        if(!logofile.getOriginalFilename().isEmpty()){
            mainOptions.setLogo(new StorageService().uploadSingleImg(logofile, "content/images"));
        }

        // upload favicon
        if(!faviconfile.getOriginalFilename().isEmpty()){
            mainOptions.setFavicon(new StorageService().uploadSingleImg(faviconfile, "content/images"));
        }


      if(mainOptions.getDevelopmentmode() !=null) {
          String style = HelpersFile.readFile("content/views/assets/front/css/style.css");
          style = style.replaceAll("\\s{2,}", "").replaceAll("\\.\\./", "/content/views/assets/front/");
          HelpersFile.updateFile(style,"content/views/assets/front/css/style.html");

          String pageBuilderDefault = HelpersFile.readFile("content/views/assets/front/css/pageBuilderDefault.css");
          pageBuilderDefault = pageBuilderDefault.replaceAll("\\s{2,}", "").replaceAll("\\.\\./", "/content/views/assets/front/");
          HelpersFile.updateFile(pageBuilderDefault,"content/views/assets/front/css/pageBuilderDefault.html");
       }

         config.setMinOptions(mainOptions);
         config.save();


        Settings settings = settingsRepository.findFirstByParam("_footerjs_");

         try{
             settings.setValue1(request.getParameter("_footerjs_"));
             settings.setParam("_footerjs_");
             settingsRepository.save(settings);
         }catch (Exception e){
             Settings settings1 = new Settings();
             settings1.setValue1(request.getParameter("_footerjs_"));
             settingsRepository.save(settings1);
         }

         redirectAttributes.addFlashAttribute("message","Successfully updated!");
         return  "redirect:" + request.getHeader("Referer");
    }


}
