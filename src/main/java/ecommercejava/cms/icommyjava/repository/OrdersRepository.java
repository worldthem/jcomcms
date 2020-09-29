package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Orders findFirstBySessionuserAndStatus(String sessionuser, String status);
    Orders findFirstBySessionuserAndOrdersid(String sessionuser, Integer ordersid);

    Page<Orders> findByStatusNotOrderByOrdersidDesc(String status, Pageable pageable);

    Page<Orders> findByStatusContainingOrShippingContainingOrSecretnrContainingOrderByOrdersidDesc(String status, String txt, String secret, Pageable pageable);

    Orders getOne(Integer ordersid);
    Page<Orders> findByUseridOrderByOrdersidDesc(Integer userid, Pageable pageable);

    Orders findFirstBySessionuser(String sessionuser );
    Orders findFirstBySecretnr(String secretnr );
    Orders findFirstBySecretnrAndUserid(String secretnr, Integer uid );
    Orders  findByUseridAndOrdersid(Integer userid, Integer ordersid);
}