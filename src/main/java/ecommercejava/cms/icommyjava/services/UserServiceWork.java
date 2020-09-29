package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.entity.Users;
import ecommercejava.cms.icommyjava.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceWork {
    @Autowired
    UsersRepository usersRepository;

    public Users findByEmailIgnoreCase(String email) {
        return usersRepository.findByEmailIgnoreCase(email);
    }
}
