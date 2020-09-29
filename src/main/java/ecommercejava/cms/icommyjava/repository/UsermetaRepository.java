package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Usermeta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermetaRepository extends CrudRepository<Usermeta, Integer> {

}