package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Subscribers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscribers, Integer> {
    Page<Subscribers> findByEmailContaining(String search, Pageable pageable);
    Page<Subscribers> findAll(Pageable pageable);
    long count();

}
