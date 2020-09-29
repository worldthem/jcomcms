package ecommercejava.cms.icommyjava.dto.billingshipping;

public class CheckoutFields {
    private String enable;
    private String name;
    private String show_title;
    private String required;
    private String mode;

    public String[] getEnable() {

        return enable.contains("|")? enable.split("|") : null;
    }

    public String[] getName() {
        return name.contains("|")? name.split("|") : null;
    }

    public String[] getShow_title() {
        return show_title.contains("|")? show_title.split("|") : null;
    }

    public String[] getRequired() {
        return required.contains("|")? required.split("|") : null;
    }

    public String[] getMode() {
        return mode.contains("|")? mode.split("|") : null;
    }






}
