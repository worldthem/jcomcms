package ecommercejava.cms.icommyjava.dto;

import java.time.LocalDateTime;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
public class ProductSave {
    private String SKU;
    private Integer qtu;
    private Integer store;
    private String title;
    private String cpu;
    private String metad;
    private String metak;
    private String description;
    private String text;
    private Float weight;
    private String stock;
    private Float price;
    private Float sale_price;
    private String attr;
    private String optionsdata;
    private String image;
    private Integer hide;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;

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

    public String getMetad() {
        return metad;
    }

    public void setMetad(String metad) {
        this.metad = metad;
    }

    public String getMetak() {
        return metak;
    }

    public void setMetak(String metak) {
        this.metak = metak;
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
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



}
