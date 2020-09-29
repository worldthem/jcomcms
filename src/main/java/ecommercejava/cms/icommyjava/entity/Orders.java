package ecommercejava.cms.icommyjava.entity;


import ecommercejava.cms.icommyjava.helper.HelpersJson;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity // This tells Hibernate to make a table out of this class
public class Orders {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer ordersid;
    private Integer userid;

    @Column(length = 70)
    private String sessionuser;

    @Column(length = 100)
    private String secretnr;


    @Column(length = 100)
    private String paymenttoken;
 
    @Column(columnDefinition = "TEXT")
    private String shipping;
 
    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(length = 100)
    private String status;

    @Column(length = 150)
    private String message;

    @Column(length = 150)
    private String totalpayd;


    private Integer shippingid;


    @Column(length = 150)
    private String paymentmethod;


    @UpdateTimestamp
    private LocalDateTime updated_at;

    @CreationTimestamp
    @Column(updatable = false) //, name = "createdat"
    private LocalDateTime created_at;




    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="ordersid")//, insertable=false, updatable=false
    private List <Cart> cart;

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shippingid", insertable=false, updatable=false)
    private Shipping shippingtable;

    public Shipping getShipingTable() {
        try {
            if(getShippingid()>0){
                String shipn = shippingtable.getShipping_name();
                return shippingtable;
            }
            return null;
        }catch (Exception e){
            return null;
        }
     }


    public String getSessionuser() {
        return sessionuser;
    }

    public void setSessionuser(String sessionuser) {
        this.sessionuser = sessionuser;
    }
 
    public String getSecretnr() {
        return secretnr ==null? "":secretnr;
    }

    public void setSecretnr(String secretnr) {
        this.secretnr = secretnr;
    }

    public String getPaymenttoken() {
        return paymenttoken ==null? "": paymenttoken;
    }

    public void setPaymenttoken(String paymenttoken) {
        this.paymenttoken = paymenttoken;
    }


    public String getShipping() {
        return shipping == null ? "" : shipping;
    }

    public Map<String,String> getShippingMap() {
          return HelpersJson.convertJsonToMap(shipping);
     }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }
 
    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
 
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
 
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Integer getOrdersid() {
        return ordersid;
    }

    public void setOrdersid(Integer ordersid) {
        this.ordersid = ordersid;
    }

    public Integer getUserid() {
        return userid == null? 0 : userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Float getTotalpayd() {
        return totalpayd != null ? Float.parseFloat(totalpayd ) : 0.0f;
    }

    public void setTotalpayd(String totalpayd) {
        this.totalpayd = totalpayd;
    }

    public Integer getShippingid() {
        return shippingid == null ? 0 : shippingid;
    }


    public void setShippingid(Integer shippingid) {
        this.shippingid = shippingid;
    }

    public String getPaymentmethod() {
        return paymentmethod ==null? "": paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

}
