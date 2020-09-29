package ecommercejava.cms.icommyjava.dto.Config;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.helper.Language;

public class CatJson {
    private Integer id;
    private Integer parent;
    private Integer tip;
    private String type;
    private String title;
    private String cpu;
    private String cpufull;

    public Integer getParent() {
        return parent == null ? 0 :parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getTip() {
        return tip==null ? 0 : tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }

    public String getTitle() {
        return  Helpers.splitLang(title);
    }

    public String getTitleByLang(String lang) {
        return Language.splitLangByLang(title, lang) ;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getCpufull() {
        return cpufull==null? "" : cpufull;
    }

    public void setCpufull(String cpufull) {
        this.cpufull = cpufull;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
