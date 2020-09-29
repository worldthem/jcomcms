package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Users findByEmailIgnoreCase(String email);
    Page<Users> findByEmailContainingOrFirstNameContainingOrLastNameContaining(String email, String first, String last, Pageable pageable);
    Page<Users> findAll(Pageable pageable);
    Page<Users> findAllByOrderByUseridDesc(Pageable pageable);
    Users findByEmail(String email);
    Users getOne(Integer id);
    long count();
}