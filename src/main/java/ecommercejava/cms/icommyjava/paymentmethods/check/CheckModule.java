package ecommercejava.cms.icommyjava.paymentmethods.check;


import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.helper.Language;

public class CheckModule {
    public String getTitle() {
        return title== null? "Check payments": Language.getLangData(title);
    }

    public void setTitle(String title) {
        this.title =  Language.saveSimple(title, this.title, lang );
      }

    public String getDescription() {
        return description== null? "Please send a check to Store Name, Store Street, Store Town, Store State / County, Store Postcode." : Language.getLangData(description);
    }

    public void setDescription(String description) {
        this.description= Language.saveSimple(description, this.description, lang );
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstruction() {
        return Language.getLangData(instruction);
    }

    public void setInstruction(String instruction) {
        this.instruction= Language.saveSimple(instruction, this.instruction, lang);
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

    private String noimage;
    private String image;
    private Lang instruction;
    private Lang title;
    private  Lang description;
    private String lang;
}
