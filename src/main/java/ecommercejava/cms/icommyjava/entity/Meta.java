package ecommercejava.cms.icommyjava.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // This tells Hibernate to make a table out of this class
public class Meta {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer metaid;
    @Column(length = 150)
    private String param;

    @Column(length = 255)
    private String value;

    @Column(length = 255)
    private String value2;
 
    @Column(columnDefinition = "TEXT")
    private String text;
 
    private LocalDateTime created_at;
 
    private LocalDateTime updated_at;


 
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
 
    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
 
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
 
    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
 
    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getMetaid() {
        return metaid;
    }

    public void setMetaid(Integer metaid) {
        this.metaid = metaid;
    }
}
