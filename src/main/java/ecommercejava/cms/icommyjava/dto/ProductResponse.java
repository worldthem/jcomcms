package ecommercejava.cms.icommyjava.dto;

import java.time.LocalDateTime;

public class ProductResponse {
    private Integer productid;
    private Integer userid;
    private String catid;
    private String SKU;
    private Integer qtu;
    private Integer store;
    private String title;
    private String cpu;
    private String meta;
    private String description;
    private String text;
    private Float weight;
    private Float price;
    private Float sale_price;
    private String attr;
    private String optionsdata;
    private Integer hide;
    private LocalDateTime created_at;
    private String directory;

    public ProductResponse(Integer productid, Integer userid, String catid, String SKU, Integer qtu, Integer store, String title, String cpu, String meta, String description,
                           String text, Float weight, Float price, Float sale_price, String attr, String optionsdata, Integer hide, LocalDateTime created_at,
                           String directory){

        this.productid = productid;
        this.userid=userid;
        this.catid=catid;
        this.SKU=SKU;
        this.qtu=qtu;
        this.store=store;
        this.title=title;
        this.cpu=cpu;
        this.meta=meta;
        this.description=description;
        this.text=text;
        this.weight=weight;
        this.price=price;
        this.sale_price=sale_price;
        this.attr=attr;
        this.optionsdata=optionsdata;
        this.hide=hide;
        this.created_at=created_at;
        this.directory=directory;
    }

   // public ProductResponse(){}

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public Integer getQtu() {
        return qtu;
    }

    public void setQtu(Integer qtu) {
        this.qtu = qtu;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getSale_price() {
        return sale_price;
    }

    public void setSale_price(Float sale_price) {
        this.sale_price = sale_price;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getOptionsdata() {
        return optionsdata;
    }

    public void setOptionsdata(String optionsdata) {
        this.optionsdata = optionsdata;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }
}
