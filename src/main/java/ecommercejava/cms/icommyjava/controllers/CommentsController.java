package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.entity.Comments;
import ecommercejava.cms.icommyjava.entity.Product;
import ecommercejava.cms.icommyjava.repository.CommentsRepository;
import ecommercejava.cms.icommyjava.repository.ProductRepository;
import ecommercejava.cms.icommyjava.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller // This means that this class is a Controller
public class CommentsController extends  MainController{
    @Autowired // This means to get the bean called ProductRepository
    CommentsRepository commentsRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    ProductRepository productRepository;

    @RequestMapping(value ="/comments/store", method = RequestMethod.POST)
    public @ResponseBody
    String store(HttpServletRequest request, Authentication authentication) {
         Product product = productRepository.getOne(Integer.parseInt(request.getParameter("id_post")));

         Comments comments = new Comments();
         comments.setProduct(product);
         comments.setStars(Integer.parseInt(request.getParameter("stars")));
         comments.setCommentAuthor(request.getParameter("comment_author"));
         comments.setCommentAuthorEmail(request.getParameter("comment_author_email"));
         comments.setComment(request.getParameter("comment"));
         comments.setCommentAthorIp(request.getRemoteAddr());
         comments.setStatus(1);
         if(authentication !=null){

         }

         commentsRepository.save(comments);
        return "<span class=\"fa_ok\"></span>";
      }


}
