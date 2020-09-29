package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
       Wishlist  findFirstByTmpsesionidAndProduct_productid(String uid, Integer id );
       Page<Wishlist> findFirstByTmpsesionid(String uid, Pageable pageable);
       long count();
}