package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Integer> {
        Country getOne(Integer id);

}