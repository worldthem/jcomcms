package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.dto.UserRegistrationDto;
import ecommercejava.cms.icommyjava.entity.CustomUser;
import ecommercejava.cms.icommyjava.entity.Role;
import ecommercejava.cms.icommyjava.entity.Users;
import ecommercejava.cms.icommyjava.repository.RoleRepository;
import ecommercejava.cms.icommyjava.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private RoleRepository roleRepository;

    private UsersRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }



    @Override
    public Users save(UserRegistrationDto registrationDto) {

        Role role = roleRepository.findFirstByName(registrationDto.getUserRole());
        if(role==null){
            role = new Role(registrationDto.getUserRole());
        }

        Users user = new Users(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(role));

        return userRepository.save(user);
    }

    @Override
    public Users update(UserRegistrationDto registrationDto, Integer Id) {

        Users user = userRepository.getOne(Id);
        Role role = roleRepository.findFirstByName(registrationDto.getUserRole());
        if(role==null){
           role = new Role(registrationDto.getUserRole());
        }

        Users users = new Users(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                user.getPassword(), Arrays.asList(role));
                users.setUserid(Id);

            String pass = registrationDto.getPassword();
            if(pass !=null){
                if(!pass.isEmpty()){
                    users.setPassword(passwordEncoder.encode(pass));
                }
            }

         return userRepository.save(users);
    }

    @Override
    public Users findFirstByEmail(String username) {
        Users user = userRepository.findByEmail(username);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(username);
        //System.out.println("user dataaa:" +user.getEmail()+user.getPassword());
        ///System.out.println("user dataassss:" +user.getRoles());

        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }


        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new CustomUser(
                user.getEmail(),
                user.getPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                mapRolesToAuthorities(user.getRoles()),
                user.getUserid());
       // return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public Users getOne(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public long count(){
        return userRepository.count();
    }
}
