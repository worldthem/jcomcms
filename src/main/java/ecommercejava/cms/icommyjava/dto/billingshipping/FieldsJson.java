package ecommercejava.cms.icommyjava.dto.billingshipping;


import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.helper.Language;

public class FieldsJson {
    private Lang title;
    private String showTitle;
    private String required;
    private String boxLength;
    private String boxType;

    public Lang getTitle() {
        return title;
    }

    public String getTitleTranslated() {
        return  Language.getLangData(title);
    }

    public void setTitle(Lang title) {
        this.title = title;
    }

    public boolean getShowTitle() {
        return showTitle==null? false : true;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    public boolean getRequired() {
        return required==null? false : true;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getBoxLength() {
        return boxLength;
    }

    public void setBoxLength(String boxLength) {
        this.boxLength = boxLength;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }
}
