package ecommercejava.cms.icommyjava.paymentmethods.check;

import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.paymentmethods.Payme;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import ecommercejava.cms.icommyjava.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CheckPayment extends MainController implements Payme {
    @Autowired
    SettingsRepository settingsRepository;

    /**
     * default id enter name wthout space [Az09]
     * @return
     */
    public String getId(){
        return "checkPayment";
    }

    /**
     * the data in databasetable settings by param getId() in json format
     * @return
     */
    private CheckModule getSettings(){
        Settings settings = settingsRepository.findFirstByParam(getId());

        try{
            if(settings != null) {
                String val = settings.getValue1();
                return val.isEmpty()? new CheckModule() :  (new Gson()).fromJson(val, (Type) CheckModule.class);
            }
        }catch (Exception e){ }
        return new CheckModule();
    }

    /**
     * get title of payment
     * @return
     */
    public String getTitle(){
        return getSettings().getTitle();
    }

    /**
     * get description
     * @return
     */
    public String getDescription(){
        return getSettings().getDescription();
    }

    /**
     * this part will show the in the payment list on checkout page
     * @return
     */
    public String frontEnd(){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("row", getSettings());
        templateModel.put("idPayment", getId());
        return templateHtml(templateModel, "payment::checkpayment/frontend");
    }

    /**
     * this part will show the admin options
     * @return
     */
    public String admin(){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("row", getSettings());
        return templateHtml(templateModel, "payment::checkpayment/admin");
     }

    /**
     * store all the information from the admin config put in json and save in db settings
     * @param request
     * @return
     */

    public String store(MultipartHttpServletRequest request){
        String image = request.getParameter("image");
        MultipartFile file = request.getFile("file");

        // upload main image
        if(file != null){
            if(!file.getOriginalFilename().isEmpty()){
                image = (new StorageService()).uploadSingleImg(file, "content/views/payment/checkpayment/img");
            }
        }

        CheckModule moduleData =  getSettings();
        moduleData.setLang(request.getParameter("lang"));
        moduleData.setTitle(request.getParameter("title"));
        moduleData.setDescription(request.getParameter("description"));
        moduleData.setInstruction(request.getParameter("instruction"));
        moduleData.setNoimage(request.getParameter("noimage"));
        moduleData.setImage(image);

        Settings settings = settingsRepository.findFirstByParam(getId());
        if(settings  == null){
            settings = new Settings();
        }
        settings.setParam(getId());
        settings.setValue1((new Gson()).toJson(moduleData));
        settingsRepository.save(settings);

        return "";
    }

    /**
     * when costomers press checkout page
     * @return  = "pending" , "onhold", "success", "canceled", "redirect:https://url.com/url/data"
     */
    public String makePayment(HttpServletRequest request, Float totalcart, Integer orderId){
        return "pending";
    }
}
