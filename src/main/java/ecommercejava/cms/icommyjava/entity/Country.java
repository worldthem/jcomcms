package ecommercejava.cms.icommyjava.entity;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Country {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(length = 200)
    private String country;

    @Column(length = 15)
    private String code;
 
    @Column(length = 5)
    private Integer store;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float price;

    @Column(length = 200)
    private String shipping;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float price1;

    @Column(length = 200)
    private String shipping1;
 
    @Column(columnDefinition="Decimal(10,2)")
    private Float price2;

    @Column(length = 200)
    private String shipping2;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
 
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
 
    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }
 
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
 
    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }
 
    public Float getPrice1() {
        return price1;
    }

    public void setPrice1(Float price1) {
        this.price1 = price1;
    }
 
    public String getShipping1() {
        return shipping1;
    }

    public void setShipping1(String shipping1) {
        this.shipping1 = shipping1;
    }
 
    public Float getPrice2() {
        return price2;
    }

    public void setPrice2(Float price2) {
        this.price2 = price2;
    }
 
    public String getShipping2() {
        return shipping2;
    }

    public void setShipping2(String shipping2) {
        this.shipping2 = shipping2;
    }

}
