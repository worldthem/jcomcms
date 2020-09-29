package ecommercejava.cms.icommyjava.entity;


import ecommercejava.cms.icommyjava.Helpers;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity // This tells Hibernate to make a table out of this class
public class Categories implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer catid;
    @Column(length = 30)
    private String type;

    @Column(length = 30)
    private String post_type;

    private Integer parent= new Integer(0);
 
    @Column(columnDefinition = "TEXT")
    private String title;
 
    @Column(columnDefinition = "TEXT")
    private String metad;
 
    @Column(columnDefinition = "TEXT")
    private String metak;

    @Column(length = 80)
    private String image;
 
    @Column(columnDefinition = "TEXT")
    private String cpu;

    @Column(length = 255)
    private String url;
 
    @Column(length = 5)
    private Integer tip= new Integer(0);
 
    @Column(length = 5)
    private Integer sort;
 
    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(length = 20)
    private String date;
 
    @Column(length = 5)
    private Integer param;

    @Transient
    private String lang;


   public Integer getCatid() {
        return catid;
    }

    public void setCatid(Integer catid) {
        this.catid = catid;
    }
 
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
 
    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }
 
    public Integer getParent() {
        return   parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
 
    public String getTitle() {
        return Helpers.splitLang(title);
    }


    public String getTitleClean() {
        return  title ;
    }
    public void setFullTitle(String title) {
        this.title = title;
    }
    public void setTitle(String title) {
        this.title = Helpers.saveLang(title, this.title, this.lang);
    }
 
    public String getMetad() {
        return Helpers.splitLang(metad);
    }

    public void setMetad(String metad) {
        this.metad =Helpers.saveLang(metad, this.metad, this.lang) ;
    }
 
    public String getMetak() {
        return Helpers.splitLang(metak);
    }

    public void setMetak(String metak) {
        this.metak =  Helpers.saveLang(metak, this.metak, this.lang) ;
    }
    public String getText() {
        return Helpers.splitLang(text);
    }

    public void setText(String text) {
        this.text = Helpers.saveLang(text, this.text, this.lang) ;
    }

 
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
 
    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
           this.cpu = StringUtils.isEmpty(cpu)  ?  Helpers.slug(this.title) : Helpers.slug(cpu);
    }
 
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
 
    public Integer getTip() {
        return tip ;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }
 
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
 
    public Integer getParam() {
        return param;
    }

    public void setParam(Integer param) {
        this.param = param;
    }
 
    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    public String getFlullTitle() { return title; }
    public String getFlullText() { return text;}
    public String getFlullMetak() { return metak; }
    public String getFlullMetad() {  return metad; }

    public void setter( Categories entity, String lang){
        setLang(lang);
        setCpu(this.cpu);
        this.title =  Helpers.saveLang(this.title, entity.getFlullTitle(), lang);
        this.metad=  Helpers.saveLang(this.metad, entity.getFlullMetad(), lang);
        this.metak=  Helpers.saveLang(this.metak, entity.getFlullMetak(), lang);
        this.text=  Helpers.saveLang(this.text, entity.getFlullText(), lang);
     }

}
