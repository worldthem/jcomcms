package ecommercejava.cms.icommyjava.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Visits {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(columnDefinition = "LONGTEXT")
    private String pagevisit;

    @Column(length = 20)
    private String ipvisit;


    private LocalDate date;

    @Column(length = 300)
    private String url;

    @Column(length = 800)
    private String user_agent;

    @Column(length = 20)
    private String browser_language;

    private Long timespend;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getPagevisit() {
        return pagevisit;
    }

    public void setPagevisit(String pagevisit) {
        this.pagevisit = pagevisit;
    }
 
    public String getIpvisit() {
        return ipvisit;
    }

    public void setIpvisit(String ipvisit) {
        this.ipvisit = ipvisit;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUrl() {
        return url != null? url : "";
    }

    public void setUrl(String url) {
        this.url = url;
    }
 
    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }
 
    public String getBrowser_language() {
        return browser_language;
    }

    public void setBrowser_language(String browser_language) {
        this.browser_language = browser_language;
    }

    public Long getTimespend() {
        return timespend == null? 0 : timespend;
    }

    public void setTimespend(Long timespend) {
        this.timespend = timespend;
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

}
