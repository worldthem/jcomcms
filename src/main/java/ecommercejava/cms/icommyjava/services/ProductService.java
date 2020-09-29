package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.entity.Categories;
import ecommercejava.cms.icommyjava.entity.Product;
import ecommercejava.cms.icommyjava.entity.ProductCriteriaRepository;
import ecommercejava.cms.icommyjava.entity.Wishlist;
import ecommercejava.cms.icommyjava.repository.CategoriesRepository;
import ecommercejava.cms.icommyjava.repository.ProductRepository;
import ecommercejava.cms.icommyjava.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCriteriaRepository productCriteriaRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

    Float maxPrice = 1000000000000.0f;

    public Product findByProductid(Integer id){
      return productRepository.findByProductid(id);
  }
    public Product getByCpu(String cpu) {
     return productRepository.findFirstByCpu(cpu) ;
    }
    public Product getOne(Integer id) {
        return productRepository.getOne(id);
    }
    public Page<Product> searchByTitle(String title, Pageable pageable) {
        return productRepository.findByTitleContaining(title, pageable) ;
    }


    public List<Product> findByCatidList(String catid, Pageable pageable) {
        return productRepository.findByCatidContainingOrderByProductidAsc(catid, pageable);
    }

    public Page<Product> findByUsersid(Integer uid, Pageable pageable) {
        return productRepository.findByUsers_useridOrderByProductidDesc(uid, pageable);
    }
    public Page<Product> search(String title, String descr, String text, String attr, Pageable pageable) {
           return productRepository.findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingOrderByProductidDesc(title,descr,text,attr,pageable);
     }

    public Page<Product> findByCatid(String catid, Pageable pageable ) {
         return productRepository.findByCatidContainingOrderByProductidDesc(catid, pageable);
     }

     public Page<Product> findByCatid(String catid, Pageable pageable, String pricesort, String min, String max) {
         Float minim =min == null || min.isEmpty()? 0.0f : Float.parseFloat(min);
         Float maxim = max == null || max.isEmpty()? 0.0f : Float.parseFloat(max);
         maxim = minim>0 && maxim==0 ? maxPrice : maxim;

        if(pricesort.isEmpty() ){
            if(minim > 0 || maxim > 0){
                return productRepository.findByCatidContainingAndPriceBetweenOrderByProductidDesc(catid,minim, maxim, pageable);
            }else{ // we may go with between but when you will have many records in the table this will influence on speed so decide to go with second if
                return productRepository.findByCatidContainingOrderByProductidDesc(catid, pageable);
            }
        }else{
            if(minim > 0 || maxim > 0){
                return pricesort.contains("desc") ?
                        productRepository.findByCatidContainingAndPriceBetweenOrderByPriceDesc(catid,minim, maxim, pageable) :
                        productRepository.findByCatidContainingAndPriceBetweenOrderByPriceAsc(catid,minim, maxim, pageable);
            }else{ // we may go with between but when you will have many records in the table this will influence on speed so decide to go with second if
                return pricesort.contains("desc") ?
                        productRepository.findByCatidContainingOrderByPriceDesc(catid, pageable) :
                        productRepository.findByCatidContainingOrderByPriceAsc(catid,  pageable);
            }
        }

    }

    public Page<Product> getByCatid(String catid, Pageable pageable ) {
        return  productRepository.findByCatidContainingOrderByPriceDesc(catid, pageable);
    }



    public Page<Product> search(String title, String descr, String text, String attr, Pageable pageable, String pricesort, String min, String max ) {
        Float minim =min == null || min.isEmpty()? 0.0f : Float.parseFloat(min);
        Float maxim = max == null || max.isEmpty()? 0.0f : Float.parseFloat(max);
              maxim = minim>0 && maxim==0 ? maxPrice : maxim;

        if(pricesort.isEmpty() ){
             if(minim > 0 || maxim > 0){
                return productRepository.findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingAndPriceBetweenOrderByProductidDesc(title,descr,text,attr, minim, maxim, pageable);
             }else{ // we may go with between but when you will have many records in the table this will influence on speed so decide to go with second if
                return productRepository.findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingOrderByProductidDesc(title,descr,text,attr, pageable);
            }

        }else{
            if(minim>0 || maxim > 0){
                return pricesort.contains("desc") ?
                        productRepository.findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingAndPriceBetweenOrderByPriceDesc(title,descr,text,attr,minim, maxim,pageable) :
                        productRepository.findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingAndPriceBetweenOrderByPriceAsc(title,descr,text,attr,minim, maxim,pageable) ;

            }else{ // we may go with between but when you will have many records in the table this will influence on speed so decide to go with second if
                return pricesort.contains("desc") ?
                        productRepository.findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingOrderByPriceDesc(title,descr,text,attr,pageable) :
                        productRepository.findByTitleContainingOrDescriptionContainingOrTextContainingOrAttrContainingOrderByPriceAsc(title,descr,text,attr,pageable) ;
            }



        }
    }


    //Page<Product> findAll(Pageable pageable);
    public long count() {
        return productRepository.count();
    }

    public Page<Product> findAll(Pageable pageable){
      return productRepository.findAllByOrderByProductidDesc(pageable);
    }

    public Page<Product> getAll(Pageable pageable){
      return productRepository.findAll(pageable);
    }

    public Product save(Product product){
      return productRepository.save(product);
    }

    public void delete(String id){
        productRepository.deleteById(Integer.parseInt(id));
    }
    public  Product getOne(String id){
        return productRepository.getOne(Integer.parseInt(id));
    }

    public Page<Product> getProducts(Map<String, String[]> map, Pageable page){
          return productCriteriaRepository.search(map, page);
    }

    public Page<Wishlist> wishlist(String uid, Pageable pageable){
        return wishlistRepository.findFirstByTmpsesionid(uid, pageable);
    }
    public long wishlistCount(){
        return wishlistRepository.count();
    }

    public Categories getCat(String cpu){
        return categoriesRepository.findFirstByCpu(cpu);
    }

}
