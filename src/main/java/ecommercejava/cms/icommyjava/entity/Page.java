package ecommercejava.cms.icommyjava.entity;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.helper.Language;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // This tells Hibernate to make a table out of this class
public class Page {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer pageid;
    private Integer sort;

    @Column(length = 150)
    private String catid;

    @Column(length = 20)
    private String type;

    @Column(length = 300)
    private String cpu;

    @Column(length = 300)
    private String title;

    @Column(length = 512)
    private String metad;

    @Column(length = 512)
    private String metak;

    @Column(length = 512)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String style;

    @Column(columnDefinition = "TEXT")
    private String css;

    @Column(columnDefinition = "TEXT")
    private String options;
 
    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Column(length = 20)
    private String direction;

    @Column(length = 10)
    private String enable;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

    @Transient
    private String lang;

 
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

 
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
 
    public String getCpu() {
        return cpu==null ? "": cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = StringUtils.isEmpty(cpu)  ?  Helpers.slug(this.title) : Helpers.slug(cpu);
    }
 
    public String getTitle() {
          return Helpers.splitLang(title);
    }
    public String getTitleByLang(String lang) {
        return  Language.splitLangByLang(title, lang);
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

    public String getShortText(Integer nrWords) {
        String txt = Helpers.splitLang(text);
        return Helpers.excerpt(txt, nrWords) ;
    }

    public String getText(String lang) {
        return  Helpers.splitLangByLang(text, lang) ;
    }

    public String getContent() {
        String txt= text != null ? Helpers.splitLang(text): "";
        return txt.contains("col-md")? txt :"<div class=\"col-md-12 inside_grid\" id=\"id"+Helpers.randomString(5)+"\">"+txt+"</div>";
    }

    public void setText(String text) {
        this.text = Helpers.saveLang(text, this.text, this.lang) ;
    }

    public void setFullText(String text) {
        this.text = text ;
    }

    public String getFullText() {
        return text;
    }


    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
 
    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Integer getPageid() {
        return pageid;
    }

    public void setPageid(Integer pageid) {
        this.pageid = pageid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCss() {
        return css;
    }


    public void setCss(String css) {
        this.css = css;
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

    public void setter(Page entity, String lang){
        setLang(lang);
        setCpu(this.cpu);
        this.title =  Helpers.saveLang(this.title, entity.getFlullTitle(), lang);
        this.metad=  Helpers.saveLang(this.metad, entity.getFlullMetad(), lang);
        this.metak=  Helpers.saveLang(this.metak, entity.getFlullMetak(), lang);
        this.text=  Helpers.saveLang(this.text, entity.getFlullText(), lang);
    }
}
