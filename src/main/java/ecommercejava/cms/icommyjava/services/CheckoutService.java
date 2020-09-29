package ecommercejava.cms.icommyjava.services;

 
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Ordersdto;
import ecommercejava.cms.icommyjava.entity.Cart;
import ecommercejava.cms.icommyjava.entity.CuponApplied;
import ecommercejava.cms.icommyjava.entity.Orders;
import ecommercejava.cms.icommyjava.entity.Shipping;
import ecommercejava.cms.icommyjava.mail.MailService;
import ecommercejava.cms.icommyjava.repository.CartRepository;
import ecommercejava.cms.icommyjava.repository.CuponAppliedRepository;
import ecommercejava.cms.icommyjava.repository.OrdersRepository;
import ecommercejava.cms.icommyjava.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CuponAppliedRepository cuponAppliedRepository;

    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    MailService mailService;

    /**
     * Get cart total
     * @param tmpUserID
     * @param shippingId
     * @return
     */

    public Float cartTotal(String tmpUserID, String shippingId){

        Float total = 0.0f;
        List<Cart> carts = cartRepository.findByUseridAndTypecart(tmpUserID,2);
        if(carts !=null) {

            for(Cart cart : carts) {
                total= total+cart.getTotalRow();
            }

            try {
                CuponApplied cupon_applied = cuponAppliedRepository.findByUseridAndOrdersid(tmpUserID, null);
                Float discount = cupon_applied.getCuponmodule().getType().contains("percent") ? ((total * cupon_applied.getCuponmodule().getAmount()) / 100) : cupon_applied.getCuponmodule().getAmount();
                total = total - discount;
            } catch (Exception e) {  }
        }

        if(shippingId !=null) {
            int shippId = shippingId.isEmpty() ? 0 : Integer.parseInt(shippingId) ;
            if(shippId > 0) {
                Shipping shipping = shippingRepository.getOne(shippId);
                if (shipping != null) {
                    total = shipping.getFree_delivery() <= total ? total : total + shipping.getPrice();
                }
            }
        }

       return  total;

     }

    /**
     * Update row in table orders
     * @param ordersdto
     * @return
     */
     public Orders updateOrder(Ordersdto ordersdto){
         Orders orders =ordersdto.getOrdersId() > 0 ? ordersRepository.getOne(ordersdto.getOrdersId()) :
                                                      ordersRepository.findFirstBySessionuserAndStatus(ordersdto.getTmpUserID(), ordersdto.getStatus());

         orders = orders != null? orders : new Orders();
         String secret = orders != null? (orders.getSecretnr().isEmpty()? Helpers.randomString(50) : orders.getSecretnr()) : Helpers.randomString(50) ;
         String shipping  = orders != null? (orders.getShipping().isEmpty()? ordersdto.getShipping() : orders.getShipping()) : ordersdto.getShipping();
         String paymentToken = orders != null? (orders.getPaymenttoken().isEmpty()? ordersdto.getPaymenttoken() : orders.getPaymenttoken()) : ordersdto.getPaymenttoken();
         String paymentMethod = orders != null? (orders.getPaymentmethod().isEmpty()? ordersdto.getPaymentmethod() : orders.getPaymentmethod()) : ordersdto.getPaymentmethod();

         orders.setSessionuser(ordersdto.getTmpUserID());
         orders.setStatus(ordersdto.getStatus());
         orders.setSecretnr(secret);
         orders.setShippingid(ordersdto.getShippingid());
         orders.setShipping(shipping);
         orders.setPaymenttoken(paymentToken);
         orders.setPaymentmethod(paymentMethod);
         orders.setUserid(ordersdto.getUserid());
         Orders ordersSaved =ordersRepository.save(orders);

         return ordersSaved;
     }

    /**
     * get oneline form orders
     * @param orderId
     * @return
     */
    public Orders getOne(Integer orderId){ return ordersRepository.getOne(orderId); }

    /**
     * get last order from table orders
     * @param tmpUserID
     * @return
     */
    public Orders findFirstBySessionuserOrderByOrdersidDesc(String tmpUserID){ return ordersRepository.findFirstBySessionuser(tmpUserID); }

    public Orders save(Orders orders){ return ordersRepository.save(orders); }

    /**
     * this will be the last step of update
     * @param ordersdto
     * @return
     */
    public String updateLastStep(Ordersdto ordersdto){

        Orders orders =ordersdto.getOrdersId() > 0 ? ordersRepository.findFirstBySessionuserAndOrdersid(ordersdto.getTmpUserID(), ordersdto.getOrdersId()) :
                ordersRepository.findFirstBySessionuserAndStatus(ordersdto.getTmpUserID(), "start");


        String secret = orders != null? (orders.getSecretnr().isEmpty()? Helpers.randomString(50) : orders.getSecretnr()) : Helpers.randomString(50) ;

        if(orders != null){
            orders.setStatus(ordersdto.getStatus());
            orders.setSecretnr(secret);
            orders.setTotalpayd(ordersdto.getTotalpayd());
            Orders ordersSaved = ordersRepository.save(orders);

            List<Cart> cartList =cartRepository.findByUseridAndTypecartAndOrdersid(ordersdto.getTmpUserID(), 2, ordersSaved.getOrdersid());
            for(Cart cart : cartList){
                cart.setTypecart(4);
                cart.setPrice(cart.getTotalRow());
                cartRepository.save(cart);
            }
            orders.setCart(cartList);
            mailService.emailOrders(orders.getStatus(), orders);
        }

       try{
           CuponApplied cupon_applied = cuponAppliedRepository.findByUseridAndOrdersid(ordersdto.getTmpUserID(), null);
           if(cupon_applied !=null){
               cupon_applied.setOrdersid(orders.getOrdersid());
               cuponAppliedRepository.save(cupon_applied);
           }
       }catch (Exception e){  }

        return secret ;
    }
}
