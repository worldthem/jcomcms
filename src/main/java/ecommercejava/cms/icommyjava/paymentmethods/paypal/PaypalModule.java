package ecommercejava.cms.icommyjava.paymentmethods.paypal;


import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.helper.Language;

public class PaypalModule {
    public String getTitle() {
        return title== null? "Pay by card.": Language.getLangData(title);
    }

    public void setTitle(String title) {
        this.title =   Language.saveSimple(title, this.title, lang );
      }

    public String getDescription() {
        return description == null ? "You can pay with your credit or debit card.": Language.getLangData(description);
    }

    public void setDescription(String description) {
        this.description=  Language.saveSimple(description, this.description, lang );
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getNoimage() {
        return noimage == null ? false : true;
    }

    public void setNoimage(String noimage) {
        this.noimage = noimage;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMode() {
        return mode==null? "sandbox": mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private String noimage;
    private String image;
    private Lang title;
    private  Lang description;
    private String lang;


    private String publicKey;
    private String secretKey;
    private String mode;


}