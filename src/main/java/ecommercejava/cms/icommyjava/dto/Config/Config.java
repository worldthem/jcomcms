package ecommercejava.cms.icommyjava.dto.Config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.HelpersJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private MainOptions mainOptions;
    private Map<String, Currency> currencies;
    private Map<String, CatJson> categories;
    private List<String> languages ;
    private  String adminlang;
    private  Map<String, Menu> menu;
    private  String theme;
    private Map<String,String> generalSetting;

    
    public MainOptions getMinOptions() {
        return mainOptions==null? new MainOptions() : mainOptions;
    }

    public void setMinOptions(MainOptions mainOptions) {
        this.mainOptions = mainOptions;
    }
   
    public Map<String, Currency> getCurrencies() {
        return currencies == null ? new HashMap<String, Currency>() :currencies;
    }

    public void setCurrencies(Map<String, Currency> currencies) {
        this.currencies = currencies;
    }

    public Map<String, CatJson> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, CatJson> categories) {
        this.categories = categories;
    }

    public String getAdminlang() {
        return adminlang==null ? "en": adminlang;
    }

    public void setAdminlang(String adminlang) {
        this.adminlang = adminlang;
    }

    public List<String> getLanguages() {
        if(languages == null){
            List<String> newLang =new ArrayList<>();
            newLang.add("en");
            return newLang;
        }
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
    

    public String getGeneralSetting(String option) {
        return generalSetting.containsKey(option)? generalSetting.get(option) : "";
    }

    public void setGeneralSetting(Map<String, String> mainOptions) {
        this.generalSetting = mainOptions;
    }
    public void setParamGeneralSetting(String key, String value) {
        this.generalSetting.put(key, value);
    }


    public Map<String, Menu> getMenu() {
        return menu;
    }

    public void setMenu(Map<String, Menu> menu) {
        this.menu = menu;
    }

    
    public void save(){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String data =gson.toJson(this);

        HelpersFile.updateFile(data, "content/config/config.json");
        HelpersJson.conficData = null;
    }

    public String getTheme() {
        return theme == null? "standart": theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }


}
