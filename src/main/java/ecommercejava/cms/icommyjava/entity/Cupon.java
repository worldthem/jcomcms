package ecommercejava.cms.icommyjava.entity;


import ecommercejava.cms.icommyjava.Helpers;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // This tells Hibernate to make a table out of this class
public class Cupon {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(length = 100)
    private String cupon;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float amount;

    @Column(length = 10)
    private String type;

    private Integer used;

    @Column(length = 20)
    private String publish;

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

    public String getCupon() {
        return cupon;
    }

    public void setCupon(String cupon) {
        this.cupon = cupon;
    }
 
    public Float getAmount() {
        return amount;
    }

    public String getDiscount() {
        return type.contains("fix")? "-"+ Helpers.price(amount,null): "-"+Float.toString(amount)+"%";
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
 
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
 
    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
 
    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
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


}
