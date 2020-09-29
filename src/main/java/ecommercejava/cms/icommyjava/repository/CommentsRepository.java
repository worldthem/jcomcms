package ecommercejava.cms.icommyjava.repository;

import ecommercejava.cms.icommyjava.entity.Comments;
import ecommercejava.cms.icommyjava.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentsRepository extends JpaRepository<Comments, Integer> {
    //Page<Comments> findByOrderByIdDesc(Pageable pageable);
    Page<Comments> findAll(Pageable pageable);
    Page<Comments> findAllByOrderByCommentsidDesc(Pageable pageable);
    Page<Comments> findByCommentContainingOrCommentauthorContainingOrCommentauthoremailContaining(String txt1, String txt2, String txt3, Pageable pageable);
    Page<Comments> findByUser(Users users, Pageable pageable);
    Page<Comments> findByStatus(Integer s, Pageable pageable);
    long count();
}