package ecommercejava.cms.icommyjava.entity;

import javax.persistence.*;


@Entity
public class Gallery implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @Column(length = 255)
    private String sesion_id;

    @Column(name = "productid", nullable=false)
    private Integer productid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="productid", insertable=false, updatable=false)
    private Product product;


    private Integer pageid;
    private Integer userid;
 
    @Column(columnDefinition = "TEXT")
    private String directory;

    @Column(length = 512)
    private String title;
 
    @Column(length = 5)
    private Integer sort;
 
    @Column(length = 20)
    private String typefile ;
 
    @Column(length = 5)
    private Integer importnr;


   public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getSesion_id() {
        return sesion_id;
    }

    public void setSesion_id(String sesion_id) {
        this.sesion_id = sesion_id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
 
    public String getDirectory() {
        return directory == null ? "" :directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

 
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
 

 
    public Integer getImportnr() {
        return importnr;
    }

    public void setImportnr(Integer importnr) {
        this.importnr = importnr;
    }



    public Integer getPageid() {
        return pageid;
    }

    public void setPageid(Integer pageid) {
        this.pageid = pageid;
    }

    public String getTypefile() {
        return typefile == null ? "" : typefile;
    }

    public void setTypefile(String typefile) {
        this.typefile = typefile;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getTitle() {
        return title==null ? "": title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

/*
    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

 */
}
