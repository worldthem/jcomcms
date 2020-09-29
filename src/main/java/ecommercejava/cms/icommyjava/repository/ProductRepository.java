package ecommercejava.cms.icommyjava.repository;



import ecommercejava.cms.icommyjava.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

     Product findByProductid(Integer id);
     Product findFirstByCpu(String cpu);
     Product getOne(Integer id);
     Page<Product> findByTitleContaining(String title, Pageable pageable);
     Page<Product> findByCatidContainingOrderByProductidDesc(String catid, Pageable pageable);
     Page<Product> findAllByOrderByProductidDesc(Pageable pageable);

     Page<Product> findByCatidContainingAndPriceBetweenOrderByProductidDesc(String catid, Float pricemin, Float pricemax, Pageable pageable);

     Page<Product> findByCatidContainingOrderByPriceDesc(String catid, Pageable pageable);
     Page<Product> findByCatidContainingOrderByPriceAsc(String catid, Pageable pageable);

     Page<Product> findByCatidContainingAndPriceBetweenOrderByPriceDesc(String catid, Float pricemin, Float pricemax, Pageable pageable);
     Page<Product> findByCatidContainingAndPriceBetweenOrderByPriceAsc(String catid, Float pricemin, Float pricemax, Pageable pageable);

     List<Product> findByCatidContainingOrderByProductidAsc(String catid, Pageable pageable);

     Page<Product> findByUsers_useridOrderByProductidDesc(Integer uid, Pageable pageable);

     Page<Product> findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingOrderByProductidDesc(String title, String descr, String text, String attr, Pageable pageable);

     Page<Product> findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingAndPriceBetweenOrderByProductidDesc(String title, String descr, String text, String attr, Float pricemin, Float pricemax, Pageable pageable);

     Page<Product> findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingAndPriceBetweenOrderByPriceDesc(String title, String descr, String text, String attr, Float pricemin, Float pricemax, Pageable pageable);
     Page<Product> findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingAndPriceBetweenOrderByPriceAsc(String title, String descr, String text, String attr, Float pricemin, Float pricemax, Pageable pageable);

     Page<Product> findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingOrderByPriceDesc(String title, String descr, String text, String attr, Pageable pageable);
     Page<Product> findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingOrderByPriceAsc(String title, String descr, String text, String attr, Pageable pageable);

     //Page<Product> findAll(Pageable pageable);
     long count();
     //Page<Product> findAll(Pageable pageable);


/*
    @Query("SELECT new cms.ecommercecms.org.dto.ProductResponse(p.productid, p.userid, p.catid,  p.SKU, p.qtu, p.store," +
                " p.title, p.cpu, p.meta, p.description,p.text, p.weight, p.price, p.sale_price, " +
                "p.attr, p.optionsdata, p.hide, p.created_at, g.directory) FROM Product p LEFT JOIN p.galleries g")
    public List<ProductResponse> getJoinInformation();

    @Query("SELECT new cms.ecommercecms.org.dto.ProductResponse(p.productid, p.userid, p.catid,  p.SKU, p.qtu, p.store," +
            " p.title, p.cpu, p.meta, p.description,p.text, p.weight, p.price, p.sale_price, " +
            "p.attr, p.optionsdata, p.hide, p.created_at, g.directory) FROM Product p LEFT JOIN p.galleries g WHERE ")
    public List<ProductResponse> getSingle(String cpu);

    @Query("select new com.demo.entities.ProducResponse(p.id, p.name, p.price, p.category.id, p.category.name) from Product p, Category c where p.category = c")
    public List<ProducResponse> join();


@Query("SELECT new cms.ecommercecms.org.dto.ProductResponse(p.productid, p.userid, p.catid,  p.SKU, p.qtu, p.store," +
        " p.title, p.cpu, p.meta, p.description,p.text, p.weight, p.price, p.sale_price, " +
        "p.attr, p.optionsdata, p.hide, p.created_at, g.directory) FROM Product p LEFT JOIN p.galleries g")
 */


}