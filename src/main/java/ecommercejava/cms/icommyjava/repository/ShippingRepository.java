package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Shipping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
   long count();
   Shipping getOne(Integer id);
   Page<Shipping> findByCountry(Integer country, Pageable pageable);
   List<Shipping> findByShowhide(Integer showhide );
}