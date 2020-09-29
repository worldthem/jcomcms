package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.CuponApplied;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuponAppliedRepository extends JpaRepository<CuponApplied, Integer> {

    CuponApplied findByUseridAndOrdersid(String userid, Integer Ordersid);
    CuponApplied findByOrdersid(Integer Ordersid);
    //Cupon_applied findByUseridAndCuponidAndOrdersid(Integer cuponid, String userid, Integer Ordersid);

}