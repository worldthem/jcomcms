package ecommercejava.cms.icommyjava.paymentmethods.paypal;

public class PaypalOrderDetail {
    private String productName;
    private float subtotal;
    private float total;
    private Integer orderId;
    public PaypalOrderDetail(String productName, Float total, Integer orderId) {
        this.productName = productName;
        this.subtotal = total;
        this.total = total;
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getSubtotal() {
        return String.format("%.2f", subtotal);
    }

    public String getTotal() {
        return String.format("%.2f", total);
    }
    public String getOrderId() {
        return Integer.toString(orderId);
    }
}
