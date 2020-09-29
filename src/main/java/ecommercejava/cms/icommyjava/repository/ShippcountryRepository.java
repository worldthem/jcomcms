package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Shippcountry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippcountryRepository extends CrudRepository<Shippcountry, Integer> {

}