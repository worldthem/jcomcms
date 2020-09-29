package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Gallery;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GalleryRepository extends CrudRepository<Gallery, Integer> {
    List<Gallery> findByProductid(Integer id);
    Gallery findByPageid(Integer id);
    Gallery getOne(Integer id);
}