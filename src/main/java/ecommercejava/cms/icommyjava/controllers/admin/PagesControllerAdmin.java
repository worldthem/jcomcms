package ecommercejava.cms.icommyjava.controllers.admin;

import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Page;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.helper.HelpersJson;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import ecommercejava.cms.icommyjava.services.PagesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PagesControllerAdmin extends MainController {

    @Autowired
    PagesServices pagesServices;

    @Autowired
    SettingsRepository settingsRepository;


    @GetMapping("/admin/page/{type}")
    public ModelAndView show (Model model, HttpServletRequest request, @PathVariable String type, @RequestParam(value ="search", required = false) String search ) {
        org.springframework.data.domain.Page<Page> pages = search !=null ?
                                 pagesServices.search(search, pagination(request, pagesServices.count(), model, 40)) :
                                 pagesServices.findByType(type, pagination(request, pagesServices.count(), model, 40));

        model.addAttribute("title", "View "+type);
        model.addAttribute("type", type);
        model.addAttribute("rows", pages);

        return view("admin::pages/page") ;

    }

    @GetMapping("/admin/page-single/{type}")
    public ModelAndView single (Model model, HttpServletRequest request,  @PathVariable String type) {
         String id = request.getParameter("id") !=null ? request.getParameter("id") : "0";
         Page page = pagesServices.getOne(Integer.parseInt(id));
        try{
            model.addAttribute("title","Edit "+ page.getTitle());
            model.addAttribute("pageid", page.getPageid());
            model.addAttribute("row", page);
        }catch (Exception e){
            model.addAttribute("row", new Page());
        }
         model.addAttribute("categorieslist", Helpers.getConfigXmlData("categorieslink","posts",""));
         model.addAttribute("type", type);

        Settings settings = settingsRepository.findFirstByParam("_page_builder_");
        try {
            model.addAttribute("array", HelpersJson.readyBlocks(settings.getValue1()));
        }catch (Exception e){
            model.addAttribute("array", new HashMap<>());
         }


         return view("admin::pages/page-add-edit") ;
     }


    @PostMapping("/admin/pages-store")
    public String store(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Page pr = new Gson().fromJson(Helpers.convertRequestt(request.getParameterMap()), Page.class);
         boolean newPage = false;
        try{
            // here we call setter what do some sett like generate slug, and put the title in translate like {"en":"title en", "it":"title it"}
            Page n = pagesServices.getOne(Integer.parseInt(request.getParameter("pageid")));
            pr.setter(n, request.getParameter("lang"));
        }catch (Exception e){
              newPage = true;
             pr.setter(new Page(), request.getParameter("lang"));
        }
         Page page =  pagesServices.save(pr);

         redirectAttributes.addFlashAttribute("message","Successfully updated!");

         return newPage? "redirect:/admin/page-single/"+page.getType()+"?id="+page.getPageid() :  "redirect:" + request.getHeader("Referer");
    }

    /**
     *  Enable disable
     * @param request
     * @return
     */

    @GetMapping(path="/admin/page-enable") // Map ONLY POST Requests

    public String enableDisable (@RequestParam(value ="id") String id, @RequestParam(value ="enable") String enable, HttpServletRequest request ) {

        try{
            Page page = pagesServices.getOne(Integer.parseInt(id));
            if (page !=null) {
                page.setEnable(enable.contains("disable")? null: "enable");
                pagesServices.save(page);
            }
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }


    /**
     *  delete
     * @param request
     * @return
     */

    @GetMapping(path="/admin/page-destroy") // Map ONLY POST Requests

    public String delete (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            pagesServices.delete(id);
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }


    @PostMapping("/admin/page-check-block")
    public @ResponseBody
    String checkApi(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String json = "";
        try {
             json =HelpersJson.jsonGetRequest("https://ecommercecms.org/json/bulderblocks");
            Settings settings = settingsRepository.findFirstByParam("_page_builder_");
            settings= settings== null? new Settings() : settings;
            settings.setValue1(json);
            settings.setParam("_page_builder_");
            settingsRepository.save(settings);

        }catch (Exception e){ }

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("array", HelpersJson.readyBlocks(json));
        return templateHtml(templateModel, "admin::layouts/ready-blocks");
     }


    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/page-bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam(value = "bulkid", required = false) List<String> bulkid) {
        String action = request.getParameter("action");
        String search = request.getParameter("s");
        String type = request.getParameter("type");
        if(!search.isEmpty() && action.isEmpty())
            return "redirect:/admin/page/"+type+"?search="+search;

        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        pagesServices.delete(idnr);
                    }
                }
            }
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }
    /**
     *  bulk update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/page-dublicate") // Map ONLY POST Requests

    public String dublicate (HttpServletRequest request, RedirectAttributes redirectAttributes ,
                             @RequestParam(value = "id") String id,
                             @RequestParam(value = "from") String from,
                             @RequestParam(value = "to") String to ) {

       try{
            Page page = pagesServices.getOne(Integer.parseInt(id));
            page.setLang(to);
            page.setText(page.getText(from));
            pagesServices.save(page);
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}
