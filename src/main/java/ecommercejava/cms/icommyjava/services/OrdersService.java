package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public interface OrdersService  {
    Orders findFirstBySessionuserAndStatus(String sessionuser, String status);
    Orders findFirstBySessionuserAndOrdersid(String sessionuser, Integer ordersid);

    Page<Orders> findByStatusNotOrderByOrdersidDesc(String status, Pageable pageable);

    Page<Orders> findByStatusContainingOrShippingContainingOrSecretnrContainingOrderByOrdersidDesc(String status, String txt, String secret, Pageable pageable);

    Orders getOne(Integer ordersid);
    Page<Orders> findByUseridOrderByOrdersidDesc(Integer userid, Pageable pageable);

    Orders findFirstBySessionuser(String sessionuser );
    Orders findFirstBySecretnr(String secretnr , Model model, String getB);
    Orders save(Orders orders);
    void deleteById(Integer id);
    Long count();
    Orders findByUseridAndOrdersid(Integer userid, Integer ordersid);

}
