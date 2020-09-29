package ecommercejava.cms.icommyjava.services;

import ecommercejava.cms.icommyjava.entity.Categories;
import ecommercejava.cms.icommyjava.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServices {
    @Autowired
    CategoriesRepository categoriesRepository;

   public Categories save(Categories categories){
        return categoriesRepository.save(categories);
    }

    public Iterable<Categories> findAll(){
       return categoriesRepository.findAll();
    }

    public void delete(Integer id){
       categoriesRepository.deleteById(id);
   }
}
