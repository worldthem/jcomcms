package ecommercejava.cms.icommyjava.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Config.Config;
import ecommercejava.cms.icommyjava.dto.Config.MainOptions;
import ecommercejava.cms.icommyjava.dto.UserRegistrationDto;
import ecommercejava.cms.icommyjava.entity.*;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.repository.CartRepository;
import ecommercejava.cms.icommyjava.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class InstallController extends MainController{

  @Autowired
  private UserService userService;

  @Autowired
  private PagesServices pagesServices;

  @Autowired
  private ProductService productService;

  @Autowired
  SettingsService settingsService;

  @Autowired
  CategoriesServices categoriesServices;

  @Autowired
  GalleryServices galleryServices;

   // @GetMapping("/instalationstest")
   // public @ResponseBody
  private Integer newinstall() {
        Map<String,String> connectionsCatPr = new HashMap<>();
        connectionsCatPr.put("long-sweater-with-parrot","clothing");
        connectionsCatPr.put("faux-suede-shoes-with-bow-detailed-heel-green-uk3-eu36","clothing");
        connectionsCatPr.put("original-samsung-galaxy--16gb-ram","phones");
        connectionsCatPr.put("156-inch-ultra-slim-laptop-16gb-ram-512gb-ssd","laptops");
        connectionsCatPr.put("jumper-ezbook-x1-laptop-windows-10","laptops");
        connectionsCatPr.put("antivirus-dt12","software");
        connectionsCatPr.put("it-is-a-long-established","new-idea");

        Map<String,String> connectionsGalleryPr = new HashMap<>();
        connectionsGalleryPr.put("C8sOAnRUefeS0F3VZ0YcXKfjS.jpg","long-sweater-with-parrot");
        connectionsGalleryPr.put("aOvk8am2TrztJD2lvv8hi8XxA.jpg","long-sweater-with-parrot");
        connectionsGalleryPr.put("fCCem21yCcIeH77RYFRG8nICT.jpg","faux-suede-shoes-with-bow-detailed-heel-green-uk3-eu36");
        connectionsGalleryPr.put("0eIklLdMKoGR5YFhNvjYp8BVC.jpg","faux-suede-shoes-with-bow-detailed-heel-green-uk3-eu36");
        connectionsGalleryPr.put("09B7vM7ZFdXNlO5JZJYInyvMy.jpg","original-samsung-galaxy--16gb-ram");
        connectionsGalleryPr.put("rGlCytu0Nh99kHugz9aeAChpL.jpg","original-samsung-galaxy--16gb-ram");
        connectionsGalleryPr.put("sL4tLHPZJeNuXPkBfECl1zBDS.jpg","156-inch-ultra-slim-laptop-16gb-ram-512gb-ssd");
        connectionsGalleryPr.put("NsuTGJpMxVUb2LVJnRRIdOpU5.jpg","156-inch-ultra-slim-laptop-16gb-ram-512gb-ssd");
        connectionsGalleryPr.put("mDmjdNjLUXfxKc9LFypXBKSyu.jpg","jumper-ezbook-x1-laptop-windows-10");
        connectionsGalleryPr.put("9hDnkY5ikAjHvRz1pvfUnxTZ6.zip","antivirus-dt12");

         String[] shCode ={"[menulogged idin=id1 idnot=id2]","[menu id=id1]","[menu id=id1]"};
         String contactFrom = "[form id=id1]";
         Integer idHome = 0;
        // Settings
        try{
            String json =HelpersFile.readFile("content/config/install/settings.json");
            Type type = new TypeToken<List<Settings>>(){}.getType();
            List<Settings> pages=  (new Gson()).fromJson(json, type);
            for (Settings page : pages){
                Settings settings =  settingsService.save(page);
               if(settings.getValue2() !=null) {
                   if (settings.getValue2().contains("topMenuLogin"))
                       shCode[0] = shCode[0].replace("id1", settings.getId().toString());

                   if (settings.getValue2().contains("topMenuNOTLogin"))
                       shCode[0] = shCode[0].replace("id2", settings.getId().toString());

                   if (settings.getValue2().contains("main"))
                       shCode[1] = shCode[1].replace("id1", settings.getId().toString());

                   if (settings.getValue2().contains("footer"))
                       shCode[2] = shCode[2].replace("id1", settings.getId().toString());
               }

               if(settings.getParam().contains("_contact_forms_"))
                   contactFrom = contactFrom.replace("id1", settings.getId().toString());

            }
        }catch (Exception e){
            System.out.println("Error:"+e);
        }

        try{
            List<String> d = new ArrayList();
            d.add("_header_");
            d.add("_footer_");
            List<Settings> set = settingsService.findByParamInOrderByIdDesc(d);
             for (Settings setSingle : set){
                 String content = setSingle.getValue1();
                 content = content.replace("[menulogged idin=94 idnot=2]", shCode[0]);
                 content = content.replace("[menu id=95]", shCode[1]);
                 content = content.replace("[menu id=96]", shCode[2]);
                 setSingle.setValue1(content);
                 settingsService.save(setSingle);
             }
        }catch (Exception e){}


        // Pages
        try{
            String json =HelpersFile.readFile("content/config/install/pages.json");
             Type type = new TypeToken<List<Page>>(){}.getType();
              List<Page> pages =  (new Gson()).fromJson(json, type);
             for (Page page : pages){

                 if(page.getCpu().contains("contacts"))
                       page.setFullText(page.getFullText().replace("[form id=75]", contactFrom));

                 Page page1 =  pagesServices.save(page);
                 if(page1.getCpu().compareTo("home") ==0)
                     idHome =page1.getPageid();
              }
        }catch (Exception e){
            System.out.println("Error:"+e);
        }

        // Categoies
        try{
            String json =HelpersFile.readFile("content/config/install/categories.json");
            Type type = new TypeToken<List<Categories>>(){}.getType();
            List<Categories> pages=  (new Gson()).fromJson(json, type);
            for (Categories page : pages){
                Categories cat =  categoriesServices.save(page);
                connectionsCatPr.forEach((k,v)->{ if(cat.getCpu().contains(v)){ connectionsCatPr.put(k,"[\""+cat.getCatid()+"\"]");}});
            }
        }catch (Exception e){
            System.out.println("Error:"+e);
        }

        // Products
        try{
            String json =HelpersFile.readFile("content/config/install/products.json");
            Type type = new TypeToken<List<Product>>(){}.getType();
            List<Product> pages=  (new Gson()).fromJson(json, type);
            for (Product page : pages){
                if(connectionsCatPr.containsKey(page.getCpu()))
                   page.setCatid(connectionsCatPr.get(page.getCpu()));
                   Product pr=productService.save(page);

                   connectionsGalleryPr.forEach((k,v)->{ if(pr.getCpu().contains(v)){ connectionsGalleryPr.put(k, pr.getProductid().toString());}});
            }
        }catch (Exception e){
            System.out.println("Error:"+e);
        }

        // Gallery
        try{
            String json =HelpersFile.readFile("content/config/install/gallery.json");
            Type type = new TypeToken<List<Gallery>>(){}.getType();
            List<Gallery> pages=  (new Gson()).fromJson(json, type);
            for (Gallery page : pages){
                if(connectionsGalleryPr.containsKey(page.getDirectory()))
                    page.setProductid(Integer.parseInt(connectionsGalleryPr.get(page.getDirectory())));

                 galleryServices.save(page);
            }
        }catch (Exception e){
            System.out.println("Error Gallery:"+e);
        }



    return idHome;
    }

@PostMapping("/new-instalations")
public ModelAndView install(HttpServletRequest request, Model model){
      long uCount = userService.count();
      long sCount = settingsService.count();

       if(uCount>0 || sCount>0){
           return view("front::auth/login");
       }

    UserRegistrationDto registrationDto = new UserRegistrationDto(request.getParameter("fname"),
                                                                  request.getParameter("lname"),
                                                                  request.getParameter("email"),
                                                                  request.getParameter("password"),
                                                          "ROLE_ADMIN"  );

    try {
        userService.save(registrationDto);
    }catch (Exception e){  System.out.println("Error insert user is:"+e);  }


    String democontent = request.getParameter("democontent");
    Integer homePage = 0;
    if(democontent !=null){
        homePage = newinstall();
      }
    String websitename = request.getParameter("websitename");

    Config config = Helpers.getConfig();
    MainOptions mainOptions = config.getMinOptions();
    mainOptions.setSite_title(websitename);
    mainOptions.setPageHome(homePage.toString());
    config.setMinOptions(mainOptions);
    config.save();
    model.addAttribute("title","One more new website");

    getUrlContents("http://jcomcms.com/json/new?url="+ ServletUriComponentsBuilder.fromCurrentServletMapping().toUriString());

   return view("front::start/finish");
}
  private  String getUrlContents(String theUrl)  {
        StringBuilder content = new StringBuilder();
        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
             String line;
           // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)  {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch(Exception e) { e.printStackTrace(); }

        return content.toString();
    }


}
