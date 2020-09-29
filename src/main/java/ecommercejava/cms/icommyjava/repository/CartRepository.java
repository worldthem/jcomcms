package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserid(String userid);
    List<Cart> findByUseridAndTypecart(String userid, Integer typecart);
    List<Cart> findByUseridAndAndOrdersid(String userid, Integer ordersid);
    List<Cart> findByUseridAndTypecartAndOrdersid(String userid, Integer typecart, Integer ordersid);
    Cart findByUseridAndCartid(String userid, Integer cartid);
    Cart findByUseridAndProduct_ProductidAndOptionsidAndTypecart(String userid,Integer productid, String options_id, Integer tycart);
    Cart getOne(Integer id);
    Cart findByOrdersidAndCartid(Integer ordersid, Integer cartid);
}