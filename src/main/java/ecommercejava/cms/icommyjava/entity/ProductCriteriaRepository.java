package ecommercejava.cms.icommyjava.entity;


import ecommercejava.cms.icommyjava.helper.HelpersJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ProductCriteriaRepository {
    @Autowired
    EntityManager em;
    Float maxPrice = 1000000000000.0f;

     // https://www.baeldung.com/jpa-and-or-criteria-predicates
    public Page<Product> search(Map<String, String[]> map, Pageable page) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        Root<Product> productRoot = cq.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesAttr = new ArrayList<>();

         String search = map.containsKey("search")? map.get("search")[0]: null;
         String min = map.containsKey("pricemin")? map.get("pricemin")[0]: null;
         String max = map.containsKey("pricemax")? map.get("pricemax")[0]: null;
         String sort = map.containsKey("sort")? map.get("sort")[0]: "";
         String catid =map.containsKey("catid")? map.get("catid")[0]: "0";

        Float minim =min == null || min.isEmpty()? 0.0f : Float.parseFloat(min);
        Float maxim = max == null || max.isEmpty()? 0.0f : Float.parseFloat(max);
              maxim = minim>0 && maxim==0 ? maxPrice : maxim;

         if(search !=null){
             Predicate prTitle = cb.like(productRoot.get("title"), "%" + search + "%");
             Predicate prText = cb.like(productRoot.get("text"), "%" + search + "%");
             Predicate prDescription = cb.like(productRoot.get("description"), "%" + search + "%");
             //Predicate prSearch = cb.or(prTitle , prText, prDescription);
             predicates.add(cb.or(prTitle , prText, prDescription));
         }else{
            List<Predicate> predicatesCat = new ArrayList<>();
             for (String s : map.get("catid")) {
                 predicatesCat.add(cb.like(productRoot.get("catid"), "%" + s + "%"));
             }
             predicates.add(cb.or(predicatesCat.toArray(new Predicate[0])));
          }

        if(minim > 0 || maxim > 0){
            //Predicate priceBetwen = cb.between(productRoot.get("price"), minim, maxim);
            predicates.add(cb.between(productRoot.get("price"), minim, maxim));
        }

        map.forEach((k,v)->{ if(HelpersJson.getAttrSettings().getFields().containsKey(k)){
                                     if(v[0].contains("-")){
                                         String[] split = v[0].split("-");
                                         List<Predicate> predicateSame = new ArrayList<>();
                                           for(String val: split) {
                                               predicateSame.add(cb.like(productRoot.get("attr"), "%" + k +"\": \""+ val + "%"));
                                            }
                                         //Predicate predicateSameImpl = cb.or(predicateSame.toArray(new Predicate[0]));
                                         predicatesAttr.add(cb.or(predicateSame.toArray(new Predicate[0])));
                                     }else{
                                         predicatesAttr.add(cb.like(productRoot.get("attr"), "%" + k +"\": \""+ v[0] + "%"));
                                     }

                                }
                            });
        /*
        if(list !=null){
           for (String query: list){
               predicates.add(cb.like(productRoot.get("attr"), "%" + query + "%"));
           }
         }

         */
        Predicate predicateFinal = cb.and(predicates.toArray(new Predicate[0]));

       if(predicatesAttr.size()>0) {
             Predicate predicateForAttr = cb.and(predicatesAttr.toArray(new Predicate[0]));
             Predicate predicateForInit = cb.and(predicates.toArray(new Predicate[0]));
             predicateFinal =  cb.and(predicateForAttr,predicateForInit);
        }



        //Predicate finalPredicate = cb.and(predicateForAttr);
        cq.where(predicateFinal);
        if(sort.isEmpty()) {
            cq.orderBy(cb.desc(productRoot.get("productid")));
        }else{
            if(sort.contains("desc")){
                 cq.orderBy(cb.desc(productRoot.get("price")));
            }else{
                cq.orderBy(cb.asc(productRoot.get("price")));
            }
        }

        TypedQuery<Product> query = em.createQuery(cq);
        int totalRows = query.getResultList().size();
        query.setFirstResult(page.getPageNumber() * page.getPageSize());
        query.setMaxResults(page.getPageSize());

        /*
        if (authorName != null) {
            predicates.add(cb.equal(book.get("author"), authorName));
        }
        if (title != null) {
            predicates.add(cb.like(book.get("title"), "%" + title + "%"));
        }

         */
        //cq.where(predicates.toArray(new Predicate[0]));



        return new PageImpl<Product>(query.getResultList(), page, totalRows); //em.createQuery(cq).setFirstResult(1).setMaxResults(10).getResultList();
    }
}
