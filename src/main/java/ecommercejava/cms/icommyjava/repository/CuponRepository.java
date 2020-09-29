package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuponRepository extends JpaRepository<Cupon, Integer> {
     Cupon findByCupon(String cupon);
}