package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.entity.Comments;
import ecommercejava.cms.icommyjava.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {
    @Autowired
    CommentsRepository commentsRepository;

    @Autowired
    UsersService usersService;

    public Page<Comments> findAll(Pageable pageable){
        Page<Comments> contacts = commentsRepository.findAllByOrderByCommentsidDesc(pageable);
        return contacts;
    }

    public Page<Comments> search(String search, Pageable pageable){
        return commentsRepository.findByCommentContainingOrCommentauthorContainingOrCommentauthoremailContaining(search,search,search,pageable);
    }

     public Page<Comments> getBy(String by, String find, Pageable pageable){
         Page<Comments> comments = null;
        if(by.contains("status")){
            comments =  commentsRepository.findByStatus(Integer.parseInt(find),pageable);
        }else if(by.contains("userid")){
            comments =  commentsRepository.findByUser(usersService.getOne(Integer.parseInt(find)), pageable);
        }

        return comments;
    }

    public Comments save(Comments comments){
        return commentsRepository.save(comments);
    }
    public Comments getOne(Integer id){
        return commentsRepository.getOne(id);
    }

    public void delete(Integer id){
         commentsRepository.deleteById(id);
    }

    public long count(){
        return commentsRepository.count();
    }

}
