package ecommercejava.cms.icommyjava.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // This tells Hibernate to make a table out of this class
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer cartid;
    @Column(length = 5)
    private Integer typecart;

    @Column(length = 40)
    private String userid;

    @Column(length = 5)
    private Integer qty;
 
    @Column(length = 5)
    private Integer downloaded;
    
    @Column(length = 30)
    private String optionsid;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float price  = new Float(0.0 );
 
    private LocalDateTime date;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

    private Integer ordersid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productid")//, insertable=false, updatable=false
    private Product product;

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }


    public Integer getTypecart() {
        return typecart;
    }

    public void setTypecart(Integer typecart) {
        this.typecart = typecart;
    }

 
    public Integer getQty() {
        return qty == null? 0:qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
 
    public Integer getDownloaded() {
        return downloaded == null? 0: downloaded;
    }

    public void setDownloaded(Integer downloaded) {
        this.downloaded = downloaded;
    }
 
    public String getOptionsid() {
        return optionsid;
    }

    public void setOptionsid(String optionsid) {
        this.optionsid = optionsid;
    }

 
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
 
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public Integer getCartid() {
        return cartid;
    }

    public void setCartid(Integer cartid) {
        this.cartid = cartid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getOrdersid() {
        return ordersid;
    }

    public void setOrdersid(Integer ordersid) {
        this.ordersid = ordersid;
    }

    public Float getTotalRow(){
        Float price = product.getPriceFull();
         if( optionsid != null){
             Float priceOption = product.getAttrMapModule().containsKey(optionsid)?  Float.parseFloat(product.getAttrMapModule().get(optionsid).get("price")) : price;
             price = priceOption > 0 ? priceOption : price;
         }
         return qty*price;
    }
    public Float getWight(){
        Float wight = product.getWeight();
        if( optionsid != null){
            Float wightOption = product.getAttrMapModule().containsKey(optionsid)?  Float.parseFloat(product.getAttrMapModule().get(optionsid).get("weight")) : wight;
            wight = wightOption > 0 ? wightOption : wight;
        }
        return qty*wight;
    }

}
