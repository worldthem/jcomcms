package ecommercejava.cms.icommyjava.dto;


import ecommercejava.cms.icommyjava.helper.AuthenticationSystem;

public class Ordersdto {

    private Integer userid;
    private Integer ordersId;
    private String tmpUserID;
    private String status;
    private String secretnr;
    private String shippingid;
    private String paymenttoken;
    private String shipping;
    private String totalpayd;
    private String paymentmethod;

    public Ordersdto(String tmpUserID, Integer ordersId, String status, String secretnr, String shippingid, String paymenttoken, String shipping,String totalpayd, String paymentmethod ) {
        this.ordersId= ordersId;
        this.tmpUserID = tmpUserID;
        this.status = status;
        this.secretnr = secretnr;
        this.shippingid = shippingid;
        this.paymenttoken = paymenttoken;
        this.shipping = shipping;
        this.totalpayd=totalpayd;
        this.paymentmethod= paymentmethod;
        this.userid = AuthenticationSystem.currentUserId();
    }


    public Integer getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Integer ordersId) {
        this.ordersId = ordersId;
    }

    public String getTmpUserID() {
        return tmpUserID;
    }

    public String getStatus() {
        return status;
    }

    public String getSecretnr() {
        return secretnr;
    }

    public Integer getShippingid() {
        return shippingid != null? (!shippingid.isEmpty()? Integer.parseInt(shippingid) : 0) : 0;
    }

    public String getPaymenttoken() {
        return paymenttoken;
    }


    public void setTmpUserID(String tmpUserID) {
        this.tmpUserID = tmpUserID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSecretnr(String secretnr) {
        this.secretnr = secretnr;
    }

    public void setShippingid(String shippingid) {
        this.shippingid = shippingid;
    }

    public void setPaymenttoken(String paymenttoken) {
        this.paymenttoken = paymenttoken;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getTotalpayd() {
        return totalpayd;
    }

    public void setTotalpayd(String totalpayd) {
        this.totalpayd = totalpayd;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public Integer getUserid() {
        return AuthenticationSystem.currentUserId();
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
