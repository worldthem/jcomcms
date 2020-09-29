package ecommercejava.cms.icommyjava.entity;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

@Entity // This tells Hibernate to make a table out of this class
public class Shipping {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Integer type_shipping;

    @Column(length = 255)
    private String shipping_name;
 
    @Column(length = 5)
    private Integer store;

    private Integer country;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float weight;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float price;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float free_delivery;

    @Column(length = 5)
    private Integer showhide;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    public Integer getType_shipping() {
        return type_shipping == null ? 2 : type_shipping;
    }

    public void setType_shipping(Integer type_shipping) {
        this.type_shipping = type_shipping;
    }
 
    public String getShipping_name() {
        return Helpers.splitLang(shipping_name) ;
    }
    public Lang getShipping_nameFull() {
        return shipping_name !=null? new Gson().fromJson(shipping_name, Lang.class) : new Lang();
    }

    public void setShipping_name(HttpServletRequest request) {
          Lang title = new Lang();
          for (int i = 0; i < Helpers.listLang().size(); i++) {
            String langName = Helpers.listLang().get(i);
            title.set(langName, request.getParameter("shipping_name["+langName+"]"));
          }
          this.shipping_name = new Gson().toJson(title);
    }
 
    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }
 
    public Integer getCountry() {
        return country == null ? 0 : country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }
 
    public Float getWeight() {
        return weight==null? 0.0f: weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
 
    public Float getPrice() {
        return price==null? 0.0f: price;
    }
    public String getStringPrice() {
        return Float.toString(price);
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getFree_delivery() {return free_delivery==null? 0.0f:free_delivery; }

    public String getStringFree_delivery() {return Float.toString(free_delivery); }

    public void setFree_delivery(Float free_delivery) {
        this.free_delivery = free_delivery;
    }
 
    public Integer getShowhide() {
        return showhide== null? 1 : showhide;
    }

    public void setShowhide(Integer showhide) {
        this.showhide = showhide;
    }

}
