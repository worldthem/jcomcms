package ecommercejava.cms.icommyjava.controllers.admin;

import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.Attributes.Attributes;
import ecommercejava.cms.icommyjava.dto.Attributes.AttributesVar;
import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.helper.AdminHelpers;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.ViewHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
public class AttributesController extends MainController {


    @RequestMapping(value ="/admin/attributes/{attribute}", method = RequestMethod.GET)

    public ModelAndView showattribute(@PathVariable String attribute, Model model, HttpServletRequest request) {

        model.addAttribute("title", "Attributes");
        model.addAttribute("attr", attribute);
        model.addAttribute("attributes", Helpers.getAttrSettings().getFields());

        return view("admin::pages/attributes") ;
    }

    /**
     * this part we use in many methods so decide to split
     * this will set new attributes and udate the file
     * @param existingAttr
     */
    private void updateAttrFile(Map<String, AttributesVar> existingAttr){
        Attributes attributes = new Attributes();
        attributes.setFields(existingAttr);

        // update attr.json  file
        HelpersFile.updateFile(new Gson().toJson(attributes), "content/config/attr.json");
    }


    @PostMapping(path = "/admin/attributes-save", consumes = "application/x-www-form-urlencoded")
    public String store(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){

        // get all attributes
        Attributes attributes = Helpers.getAttrSettings();

        for(Map.Entry<String, String[]> entry: request.getParameterMap().entrySet()) {
              if(entry.getKey().contains("key[")) {

                  String fieldKey = AdminHelpers.splitInputKey(entry.getKey(), 0);
                  System.out.println("keiis"+fieldKey);
                  // set title, because title become in multilanguage
                  Lang title = new Lang();
                  for (int i = 0; i < Helpers.listLang().size(); i++) {
                      title.set(Helpers.listLang().get(i), request.getParameter("title[" + fieldKey + "][" + Helpers.listLang().get(i) + "]"));
                  }


                  //check if key is in attribute than we get the right attribute to update it, if not we set new one -(new AttributesVar())
                  AttributesVar attributesVar = !fieldKey.contains("new") && attributes.getFields().containsKey(fieldKey) ? attributes.getFields().get(fieldKey) : new AttributesVar();

                  attributesVar.setName(title);
                  attributesVar.setBox(request.getParameter("box[" + fieldKey + "]"));
                  attributesVar.setType(request.getParameter("type[" + fieldKey + "]"));

                   String keyattr =  !fieldKey.contains("new") ? fieldKey : Helpers.attrNewKey();

                   if(!keyattr.isEmpty())
                       attributes.getFields().put(keyattr, attributesVar);
              }
          }
        attributes.save();


        return  "redirect:" + request.getHeader("Referer");
    }


    @RequestMapping(value ="/admin/attributes-delete/{fieldKey}", method = RequestMethod.GET)

    public String delete(@PathVariable String fieldKey, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        Attributes attributes = Helpers.getAttrSettings();
        if(!fieldKey.isEmpty() && attributes.getFields().containsKey(fieldKey) ){
            attributes.getFields().remove(fieldKey);
            attributes.save();
        }

        return "redirect:" + request.getHeader("Referer");
    }


    private String getSugestionPage(String key, Model model  ){
        Attributes attributes = Helpers.getAttrSettings();
        Map<String, Lang> sugestion = attributes.getFields().containsKey(key)? attributes.getFields().get(key).getSugestion() : new LinkedHashMap<>();
        model.addAttribute("view", new ViewHelper());
        model.addAttribute("sugestion", sugestion);
        model.addAttribute("fieldKey", key);
        model.addAttribute("baseurl", Helpers.baseurl());

        return "/cms/admin/layouts/sugestion";
    }

    @PostMapping(path = "/admin/attributes-sugestion" )
    public String showSugestion(@RequestParam(value ="key")  String key, Model model , HttpServletRequest request) {
        return getSugestionPage(key, model );
    }


    @PostMapping(path = "/admin/attributes-sugestion/update", consumes = "application/x-www-form-urlencoded")
    public String storeSugestion (HttpServletRequest request, Model model ) {
        String fieldKey = request.getParameter("fieldKey");
        String id = request.getParameter("id");
               id =  id.contains("new")? Helpers.randomString(4) : id;
        // get all attributes
        Attributes attributes = Helpers.getAttrSettings();


        if(attributes.getFields().containsKey(fieldKey)) {
            AttributesVar attributesVar = attributes.getFields().get(fieldKey);
            Map<String, Lang> sugestion = attributesVar.getSugestion() ==null ? new LinkedHashMap<>() : attributesVar.getSugestion();


              List<String> listk = new ArrayList<>();
                for (int i = 0; i <400 ; i++) {
                    listk.add(Helpers.randomString(5));
                }

               for (int i = 0; i < Helpers.listLang().size(); i++) {
                  String lang = Helpers.listLang().get(i);

                  String[] split = request.getParameter("title["+lang +"]").split("\\r?\\n");

                  for (int j = 0; j <split.length ; j++) {
                      String keyIs = listk.get(j);
                      Lang title = sugestion.containsKey(keyIs)? sugestion.get(keyIs) : new Lang();
                      title.set(lang, split[j]);
                      sugestion.put(keyIs, title);
                  }

               }

               attributesVar.setSugestion(sugestion);
               attributes.getFields().put(fieldKey, attributesVar);
               attributes.save();
        }
        return getSugestionPage(fieldKey, model );
    }


    @PostMapping(path = "/admin/attributes-sugestion/update-single" )
    public @ResponseBody
    String updateSingle (HttpServletRequest request, Model model) {
        String fieldKey = request.getParameter("fieldKeySingle");
        String key = request.getParameter("id");

        Attributes attributes = Helpers.getAttrSettings();

        if(attributes.getFields().containsKey(fieldKey)) {
            try{
                AttributesVar attributesVar = attributes.getFields().get(fieldKey);
                Map<String, Lang> sugestion =  attributesVar.getSugestion();
                sugestion.remove(key);
                Lang title = new Lang();
                for (int i = 0; i < Helpers.listLang().size(); i++) {
                    String lang = Helpers.listLang().get(i);
                    title.set(lang, request.getParameter("title["+lang+"]"));
                }
                sugestion.put(key, title);
                attributesVar.setSugestion(sugestion);
                attributes.getFields().put(fieldKey, attributesVar);
                attributes.save();
                //updateAttrFile(existingAttr);
            }catch (Exception e){System.out.println("tiltle error:"+e); }

        }
        return "ok";
    }


    @PostMapping(path = "/admin/attributes-sugestion/bulk" )
    public String bulkOptions (HttpServletRequest request, Model model ) {
        String fieldKey = request.getParameter("fieldKey");
        String action = request.getParameter("action");

        Attributes attributes = Helpers.getAttrSettings();

        if(attributes.getFields().containsKey(fieldKey) && action.contains("del")) {
           try{
               AttributesVar attributesVar = attributes.getFields().get(fieldKey);
               Map<String, Lang> sugestion =  attributesVar.getSugestion();
               Map <String, String[]> mapParam = request.getParameterMap();

                   if(mapParam.containsKey("rowid")){
                       for (int i = 0; i <mapParam.get("rowid").length ; i++) {
                           sugestion.remove(mapParam.get("rowid")[i]);
                       }

                       attributesVar.setSugestion(sugestion);
                       attributes.getFields().put(fieldKey, attributesVar);
                       attributes.save();
                     }
             }catch (Exception e){}

        }

        return getSugestionPage(fieldKey, model );
    }

    @PostMapping(path = "/admin/attributes-sugestion/remove" )

    public String remouveSugestion (HttpServletRequest request, @RequestParam(value ="fieldKey") String fieldKey, @RequestParam(value ="key") String key, Model model ) {

        Attributes attributes = Helpers.getAttrSettings();

        if(attributes.getFields().containsKey(fieldKey)) {
            try{
                AttributesVar attributesVar = attributes.getFields().get(fieldKey);
                Map<String, Lang> sugestion =  attributesVar.getSugestion();
                sugestion.remove(key);
                attributesVar.setSugestion(sugestion);
                attributes.getFields().put(fieldKey, attributesVar);
                attributes.save();
            }catch (Exception e){}

        }
        return getSugestionPage(fieldKey, model );
    }


}
