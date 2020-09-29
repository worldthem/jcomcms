package ecommercejava.cms.icommyjava.entity;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Usermeta {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Integer userid;

    @Column(length = 255)
    private String meta_key;
 
    @Column(columnDefinition = "TEXT")
    private String meta_value;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeta_key() {
        return meta_key;
    }

    public void setMeta_key(String meta_key) {
        this.meta_key = meta_key;
    }
 
    public String getMeta_value() {
        return meta_value;
    }

    public void setMeta_value(String meta_value) {
        this.meta_value = meta_value;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
