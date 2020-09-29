package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
         Role findFirstByName(String name);
}