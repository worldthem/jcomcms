package ecommercejava.cms.icommyjava.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ecommercejava.cms.icommyjava.Helpers;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity // This tells Hibernate to make a table out of this class
public class Settings {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(length = 150)
    private String param;

    @Column(columnDefinition = "LONGTEXT") //LONGTEXT
    private String value;
 
    @Column(columnDefinition = "LONGTEXT")
    private String value1;

    @Column(length = 255)
    private String value2;

    @Column(length = 3)
    private String autoload;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
 
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
 
    public String getValue1() {
        return value1;
    }

    public String getValue1Lang() {
        return Helpers.splitLang(value1);
    }
    public Lang getValue1AllLang() {
        try{
            Gson gson = new Gson();
            return gson.fromJson(value1, (Type) Lang.class);
        }catch (Exception e){
            return new Lang();
        }
    }

    public void setValue1Lang(String value, String lang) {
        this.value1 = Helpers.saveLang(value, value1, lang);
    }

    public Map<String, String> getValue1Map() {
       try {
           Type type = new TypeToken<Map<String, String>>(){}.getType();
           return value1==null ? new HashMap<>() : (new Gson()).fromJson(value1, type);
       }catch (Exception e){}

       return new HashMap<>();
    }

    public List<String> getValue1List() {
        try {
            Type type = new TypeToken<List<String>>(){}.getType();
            return value1==null ? new ArrayList<>() : (new Gson()).fromJson(value1, type);
        }catch (Exception e){}

        return new ArrayList<>();
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }
 
    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
 
    public String getAutoload() {
        return autoload;
    }

    public void setAutoload(String autoload) {
        this.autoload = autoload;
    }

}
