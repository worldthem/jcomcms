package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Config.CatJson;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.dto.Config.Menu;
import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.entity.Page;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.Language;
import ecommercejava.cms.icommyjava.repository.PageRepository;
import ecommercejava.cms.icommyjava.services.SettingsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;

@Controller // This means that this class is a Controller
public class MenuController extends MainControllerAdmin{
    @Autowired
    PageRepository pageRepository;

    @Autowired
   private SettingsService settingsService;

    private String param = "website_menu";

    @GetMapping("/admin/menu")
    public ModelAndView index(Model model, @RequestParam(value ="lang", required = false) String lang ) {
        model.addAttribute("title", "Menu");
        List<Settings> menuList = settingsService.findByParam(param);
        model.addAttribute("rows",  menuList);
        return view("admin::pages/menu") ;
    }

    @GetMapping("/admin/menu/single")
    public ModelAndView single(Model model, @RequestParam(value ="id" ) String id ) {
        model.addAttribute("title", "Menu");
        model.addAttribute("key", id);
        model.addAttribute("row", settingsService.getOne(Integer.parseInt(id)) );

        model.addAttribute("customLink", HelpersFile.getConfigXmlData("menulink", "customlink", ""));
        model.addAttribute("pages", pageRepository.findByType("pages"));
        model.addAttribute("categories",  Helpers.getConfig().getCategories());
        model.addAttribute("productCategories",   HelpersFile.getConfigXmlData("config", "categorieslink", ""));
       return view("admin::pages/menu-single") ;
    }


    @PostMapping("/admin/menu/store")
    public String store(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String id = request.getParameter("id");
        String lang = request.getParameter("lang");
        String munuIttem = request.getParameter("munuIttem");

        Settings settings =id != null?  settingsService.getOne(Integer.parseInt(id)): new Settings();

        if(settings == null && id != null)
               settings =new Settings();

        settings.setValue(request.getParameter("title"));
        settings.setValue2(request.getParameter("position"));
        settings.setValue1Lang(munuIttem, lang);
        settings.setParam(param);
        settingsService.save(settings);

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return  "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/admin/menu/dublicate")
    public String dublicate(HttpServletRequest request, RedirectAttributes redirectAttributes) {
         String id = request.getParameter("id");
         String from = request.getParameter("from");
         String to = request.getParameter("to");
         Settings settings = settingsService.getOne(Integer.parseInt(id));

         Config config= Helpers.getConfig();
         Map<String, CatJson> categories = config.getCategories();

        if(settings !=null){
             Lang men = settings.getValue1AllLang();
             String menuStringGet = men.get(from);

             Document document;
             try {
                 //Get Document object after parsing the html from given url.
                 document = Jsoup.parse(menuStringGet);

                 //Get links from document object.
                 Elements links = document.select("a[href]");
                 Map<String, CatJson> getCat= config.getCategories();
                 //Iterate links and print link attributes.
                 for (Element link : links) {
                     if(link.attr("href" ).contains("page/")){
                         String[] cpu = link.attr("href" ).split("page/");
                         Page page =pageRepository.findFirstByCpuAndType(cpu[1], "pages");

                         if(page !=null)
                           menuStringGet = menuStringGet.replace(link.text(), page.getTitleByLang(to));


                     }else if(link.attr("href" ).contains("cat/") || link.attr("href" ).contains("categories/")){
                         String[] cpu = link.attr("href" ).split("/");

                         String cpuCat = cpu[cpu.length-1].isEmpty() ? cpu[cpu.length-2] : cpu[cpu.length-1];
                         //System.out.println("length:"+cpu.length+" ; url:"+cpuCat);

                         for (Map.Entry<String, CatJson> cat : categories.entrySet()){
                             if(cat.getValue().getCpu().contains(cpuCat)){
                                 menuStringGet = menuStringGet.replace(link.text(), cat.getValue().getTitleByLang(to));
                             }
                         }

                      }else{
                         menuStringGet = menuStringGet.replace(link.text(),  Language.getTranslateByLang(link.text(), to));
                     }

                  }

             } catch (Exception e) {
                 System.out.println("errrrrooorr"+e);
             }

             settings.setValue1Lang(menuStringGet,to);
             settingsService.save(settings);
         }

         return  "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/admin/menu/destroy")
    public String distroy(HttpServletRequest request, @RequestParam(value ="id" ) String id ) {
        try{
            settingsService.delete(Integer.parseInt(id));
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}
