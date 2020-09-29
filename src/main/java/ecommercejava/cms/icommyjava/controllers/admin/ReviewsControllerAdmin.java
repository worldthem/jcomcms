package ecommercejava.cms.icommyjava.controllers.admin;


import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Comments;
import ecommercejava.cms.icommyjava.services.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ReviewsControllerAdmin extends MainController {

    @Autowired
    CommentsService commentsService;

    @GetMapping("/admin/comments")
    public ModelAndView show (Model model, HttpServletRequest request,
                              @RequestParam(value ="search", required = false) String search,
                              @RequestParam(value ="userid", required = false) String userid,
                              @RequestParam(value ="status", required = false) String status,
                              Authentication authentication) {

        Page<Comments> comments = null;
        if(search !=null){
            comments = commentsService.search(search, pagination(request, commentsService.count(), model, 40));
        }else if(userid !=null){
            comments = commentsService.getBy("userid", userid, pagination(request, commentsService.count(), model, 40));
        }else if(status !=null){
            comments = commentsService.getBy("status", status, pagination(request, commentsService.count(), model, 40));
        }else{
            comments = commentsService.findAll(pagination(request, commentsService.count(), model, 40));
        }


        model.addAttribute("title", "View Comments");

        model.addAttribute("rows", comments);

        if(authentication !=null){
            System.out.println("user data:"+authentication.getName());
        }


        return view("admin::pages/comments") ;

    }


    /**
     *  bulk update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/comments-delete") // Map ONLY POST Requests

    public String bulk (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            commentsService.delete(Integer.parseInt(id));
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }


    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/comments-bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam(value = "bulkid", required = false) List<String> bulkid) {
        String action = request.getParameter("action");
        String search = request.getParameter("s");

        if(!search.isEmpty() && action.isEmpty())
            return "redirect:/admin/comments?search="+search  ;

        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        commentsService.delete(Integer.parseInt(idnr));
                    }
                }
                if (action.contains("1") || action.contains("2")) {
                    for (String idnr : bulkid) {
                        Comments comments = commentsService.getOne(Integer.parseInt(idnr));
                        comments.setStatus(Integer.parseInt(action));
                        commentsService.save(comments);
                    }
                }
            }
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }
}
