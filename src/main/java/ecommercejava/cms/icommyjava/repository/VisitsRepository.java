package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitsRepository extends JpaRepository<Visits, Integer> {
 Visits findFirstByIpvisitAndDate(String ip, LocalDate date);
 List<Visits> findByDateBetweenOrderByIdAsc(LocalDate date1, LocalDate date2);
 List<Visits> findByDateBetween(LocalDate date1, LocalDate date2);
}