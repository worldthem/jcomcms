package ecommercejava.cms.icommyjava.paymentmethods.paypal;


import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import ecommercejava.cms.icommyjava.Helpers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalService {
    private String clientID;
    private String clientSecret;
    private String mode;

    public void setAuthotrisation(String clientID, String clientSecret, String mode) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.mode = mode;
    }


    public String authorizePayment(PaypalOrderDetail orderDetail, PaypalPayerInfo payerInfo )
            throws PayPalRESTException {

        Payer payer = getPayerInformation(payerInfo);

        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(orderDetail);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);

        // check if email is set than we set payer if not not set
        if(!payerInfo.getEmail().isEmpty())
          requestPayment.setPayer(payer);

        requestPayment.setIntent("sale"); // authorize

        APIContext apiContext = new APIContext(clientID, clientSecret, mode);

        Payment approvedPayment = requestPayment.create(apiContext);

        return getApprovalLink(approvedPayment);

    }

    private Payer getPayerInformation(PaypalPayerInfo paypalpayerInfo) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal"); //

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(paypalpayerInfo.getFirstName())
                 .setLastName(paypalpayerInfo.getLastName())
                 .setEmail(paypalpayerInfo.getEmail());
        //.setBillingAddress("dddddddddwe");

        payer.setPayerInfo(payerInfo);

        return payer;
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(Helpers.baseurl()+"paypal-cancel-payment");
        redirectUrls.setReturnUrl(Helpers.baseurl()+"paypal-success-payment");

        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(PaypalOrderDetail orderDetail) {
        //details.setTax(orderDetail.getTax());
        //details.setShipping(orderDetail.getShipping());
        String currency = Helpers.getMainCurrency().getName();
        Details details = new Details();
        //details.setShipping(orderDetail.getShipping());
        details.setSubtotal(orderDetail.getSubtotal());
        //details.setTax(orderDetail.getTax());

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(orderDetail.getTotal());
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getProductName());
        transaction.setCustom(orderDetail.getOrderId());
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setCurrency(currency);
        item.setName(orderDetail.getProductName());
        item.setPrice(orderDetail.getSubtotal());
        //item.setTax(orderDetail.getTax());
        item.setQuantity("1");

        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }

    public Payment executePayment(String paymentId, String payerId )
            throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(clientID, clientSecret, mode);

        return payment.execute(apiContext, paymentExecution);
    }

}
