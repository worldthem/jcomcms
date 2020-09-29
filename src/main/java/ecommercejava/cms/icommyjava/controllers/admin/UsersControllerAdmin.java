package ecommercejava.cms.icommyjava.controllers.admin;

import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.dto.UserRegistrationDto;
import ecommercejava.cms.icommyjava.entity.Users;
import ecommercejava.cms.icommyjava.repository.UsersRepository;
import ecommercejava.cms.icommyjava.services.UserService;
import ecommercejava.cms.icommyjava.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UsersControllerAdmin extends MainController {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    public UsersControllerAdmin(UserService userService) {
        super();
        this.userService = userService;
    }


    @ModelAttribute("users")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }


    @RequestMapping(value ="/admin/users", method = RequestMethod.GET)
    public ModelAndView show (Model model, HttpServletRequest request, @RequestParam(value ="search", required = false) String search) {
       Page<Users> users =search !=null ? usersRepository.findByEmailContainingOrFirstNameContainingOrLastNameContaining(search,search,search, pagination(request, usersRepository.count(), model, 20)):
                    usersRepository.findAll(pagination(request, usersRepository.count(), model, 20));


        //String name, Model model
        model.addAttribute("title",  "Users");
        model.addAttribute("search",  search);
        model.addAttribute("rows", users);
        //List<Categories> categories = categoriesRepository.findByTypeContaining(type, pagination(request, categoriesRepository.count(), model, 40));
        return view("admin::pages/users") ;
    }


    @RequestMapping(value ="admin/users/single", method = RequestMethod.GET)
    public ModelAndView single (@RequestParam(value ="id") String id, Model model, HttpServletRequest request) {
        Users user =  usersRepository.getOne(Integer.parseInt(id));
        //String name, Model model
        model.addAttribute("title",  "Edit Users");

        model.addAttribute("row", user);
        //List<Categories> categories = categoriesRepository.findByTypeContaining(type, pagination(request, categoriesRepository.count(), model, 40));
        return view("admin::pages/userSingle") ;
    }


    /**
     *  get data from post and put in database, and after redirect back this use for add and update new user
     * @param request
     * @return
     */

    @PostMapping(path="/admin/users/store") // Map ONLY POST Requests

    public String store (@ModelAttribute("users") UserRegistrationDto registrationDto, HttpServletRequest request,
                         RedirectAttributes redirectAttributes, BindingResult bindingResult ) {

        String id = request.getParameter("id");
        System.out.println("update here:"+id);
        String err="";
        if(id==null) {
            err = userValidator.validatedata(registrationDto, bindingResult);
        }

        if (err.isEmpty()) {
          try {
              if(id==null) {
                  userService.save(registrationDto);
              }else{
                  userService.update(registrationDto, Integer.parseInt(id));
              }
            } catch (Exception e) {
                System.out.println("Error insert user is:" + e);
            }
        }

        redirectAttributes.addFlashAttribute("message",(err.isEmpty() ? "Successfully updated!": err));
        return  "redirect:" + request.getHeader("Referer");
    }



    /**
     *  delete
     * @param request
     * @return
     */

    @GetMapping(path="/admin/users/destroy") // Map ONLY POST Requests

    public String delete (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            usersRepository.deleteById(Integer.parseInt(id));
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }






    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/users/destroy-bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam(value = "bulkid", required = false) List<String> bulkid) {
        String action = request.getParameter("action");
        String search = request.getParameter("s");

        if(!search.isEmpty() && action.isEmpty())
            return "redirect:/admin/users?search="+search  ;

        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        usersRepository.deleteById(Integer.parseInt(idnr));
                    }
                }
            }
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

}
