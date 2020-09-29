package ecommercejava.cms.icommyjava.entity;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Wishlist {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(length = 50)
    private String tmpsesionid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idproduct")
    private Product product;

    @Column(length = 50)
    private String types;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTmpsesionid() {
        return tmpsesionid;
    }

    public void setTmpsesionid(String tmpsesionid) {
        this.tmpsesionid = tmpsesionid;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }


}
