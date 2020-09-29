package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.entity.Country;
import ecommercejava.cms.icommyjava.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryServices {

    @Autowired
    CountryRepository countryRepository;

    public String getCountryById(String id){
        if(id==null || id.isEmpty())
                  return "";

        Country country =  countryRepository.getOne(Integer.parseInt(id));
        return country !=null? country.getCountry() :"";
    }


}
