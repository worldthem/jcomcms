package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.entity.Users;
import ecommercejava.cms.icommyjava.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    public Page<Users> findAll(Pageable pageable){
        Page<Users> Users = usersRepository.findAllByOrderByUseridDesc(pageable);
        return Users;
    }

    public Users findByEmail(String email){
       return  usersRepository.findByEmail(email);
    }

    public Users getOne(Integer id){
       return  usersRepository.getOne(id);
    }
     public long count(){
        return usersRepository.count();
     }

    public Users save(Users users){
        return  usersRepository.save(users);
    }
}
