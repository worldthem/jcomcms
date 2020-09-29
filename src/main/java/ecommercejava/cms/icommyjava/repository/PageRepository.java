package ecommercejava.cms.icommyjava.repository;


import ecommercejava.cms.icommyjava.entity.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


import java.util.List;

public interface PageRepository extends CrudRepository<Page, Integer> {
          Page findFirstByCpuAndType(String cpu, String type);

          List<Page> findByType(String type);
          org.springframework.data.domain.Page<Page> findByType(String type, Pageable pageable);
          org.springframework.data.domain.Page<Page> findByTypeAndCatidContains(String type, String catid, Pageable pageable);

          Page save(Page page);
          Page findFirstByDirection(String direction);
          Page getOne(Integer id);
          long count();
          org.springframework.data.domain.Page<Page> findByTitleContainingOrTextContaining(String txt1, String txt2,   Pageable pageable);
}