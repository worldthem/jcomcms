package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Categories;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoriesRepository extends CrudRepository<Categories, Integer> {
     Categories findFirstByCpu(String cpu);
     List<Categories> findByTypeContaining(String type, Pageable pageable);
     Categories getOne(Integer id);
     long count();
}