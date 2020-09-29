package ecommercejava.cms.icommyjava.dto.Attributes;


import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.helper.Language;

import java.util.Map;

public class AttributesVar {
    private Lang name;
    private String type;
    private String box;
    private String size;
    private String cl;
    private Map<String, Lang> sugestion;

    public AttributesVar() {
    }

    public Lang getName() {
        return name;
    }

    public String getNameTranslated() {
        return name.get(Language.currentLang());
    }
    public void setName(Lang name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public Map<String, Lang> getSugestion() {
        return sugestion;
    }
    public String getSugestionByKey(String key) {
        return sugestion.containsKey(key)? sugestion.get(key).get(Language.currentLang()):"";
    }
    public void setSugestion(Map<String, Lang> sugestion) {
        this.sugestion = sugestion;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }
}
