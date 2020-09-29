package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.dto.UserRegistrationDto;
import ecommercejava.cms.icommyjava.entity.Users;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    Users save(UserRegistrationDto registrationDto);
    Users update(UserRegistrationDto registrationDto, Integer id);
    Users findFirstByEmail(String email);
    Users getOne(Integer id);
    long count();
}
