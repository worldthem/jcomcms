package ecommercejava.cms.icommyjava.dto.Config;

import java.util.List;

public class MainOptions {
    private  String media_crop;
    private  String media_width = "";
    private  String media_height= "";
    private  String websiteonmaintenance;
    private  String developmentmode;
    private  String admin_email;
    private  String baseurl;
    private  String site_title;
    private  String metad;
    private  String metak;
    private  String pageHome;
    private  String checkoutTermandcondition;
    private  String logo;
    private  String favicon;
    private  String adminlang;
    private List<String> languages ;
    private String forseCheckoutLogin;

    public String getMedia_crop() {
        return media_crop;
    }

    public void setMedia_crop(String media_crop) {
        this.media_crop = media_crop;
    }

    public String getMedia_width() {
        return media_width;
    }

    public void setMedia_width(String media_width) {
        this.media_width = media_width;
    }

    public String getMedia_height() {
        return media_height;
    }

    public void setMedia_height(String media_height) {
        this.media_height = media_height;
    }

    public String getWebsiteonmaintenance() {
        return websiteonmaintenance;
    }

    public void setWebsiteonmaintenance(String websiteonmaintenance) {
        this.websiteonmaintenance = websiteonmaintenance;
    }

    public String getDevelopmentmode() {
        return developmentmode;
    }

    public void setDevelopmentmode(String developmentmode) {
        this.developmentmode = developmentmode;
    }
    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getBaseurl() {
        return baseurl==null ? "/": baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getSite_title() {
        return site_title;
    }

    public void setSite_title(String site_title) {
        this.site_title = site_title;
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

    public Integer getPageHome() {
        return pageHome== null?   0:Integer.parseInt(pageHome) ;
    }

    public void setPageHome(String pageHome) {
        this.pageHome = pageHome;
    }
    public Integer getCheckoutTermandcondition() {
        return checkoutTermandcondition == null? 0:Integer.parseInt(checkoutTermandcondition);
    }

    public void setCheckoutTermandcondition(String checkoutTermandcondition) {
        this.checkoutTermandcondition = checkoutTermandcondition;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getForseCheckoutLogin() {
        return forseCheckoutLogin;
    }

    public void setForseCheckoutLogin(String forseCheckoutLogin) {
        this.forseCheckoutLogin = forseCheckoutLogin;
    }

}
