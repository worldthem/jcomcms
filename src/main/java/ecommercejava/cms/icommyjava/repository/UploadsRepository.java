package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Uploads;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadsRepository extends CrudRepository<Uploads, Integer> {

}