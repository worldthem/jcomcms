package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.UserRegistrationDto;
import ecommercejava.cms.icommyjava.entity.Users;
import ecommercejava.cms.icommyjava.helper.AuthenticationSystem;
import ecommercejava.cms.icommyjava.helper.ViewHelper;
import ecommercejava.cms.icommyjava.mail.MailService;
import ecommercejava.cms.icommyjava.repository.UsersRepository;
import ecommercejava.cms.icommyjava.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller // This means that this class is a Controller
//@RequestMapping(path="/users") // This means URL's start with /demo (after Application path)
public class UserController extends MainController {
    @Autowired // This means to get the bean called userRepository
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    MailService mailService;

    @ModelAttribute("users")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }


    // Instantiate our encoder
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    //users.setPassword(encoder.encode(users.getPassword()));
    //usersRepository.save(user);
    @GetMapping("/login")
    public ModelAndView loogin(Model model) {
        model.addAttribute("title", "Java ecommerce cms");
        return view("front::auth/login");
    }



    // Function to handle the login process
    @PostMapping(path="/signin") // Map ONLY POST Requests
    public @ResponseBody
    String login (@ModelAttribute("SpringWeb") Users users, Model model, HttpServletRequest request) {
        ViewHelper viewHelper= new ViewHelper();
        Users existingUser = usersRepository.findByEmailIgnoreCase(users.getEmail());
        if (existingUser != null) {
            // Use encoder.matches to compare raw password with encrypted password

            if (encoder.matches(users.getPassword(), existingUser.getPassword())){
                // Successfully logged in
                try {
                    request.login(users.getEmail(), users.getPassword());
                    String typelogin =request.getParameter("typelogin");
                    return typelogin!=null ? (typelogin.contains("admin")? "redirect::/admin": "reload") : "reload" ;
                } catch (Exception e) {
                    return viewHelper.l("Bad Credentials");
                }

                //return "redirect:/admin";
            } else {
                // Wrong password
                return viewHelper.l("Incorrect password. Try again.");
            }
        } else {
                return viewHelper.l("The ccount with the email does not exist!");
           }

    }

    // Function to handle the login process
    @PostMapping(path="/reset-password") // Map ONLY POST Requests
    public @ResponseBody
    String resetpassword (@ModelAttribute("SpringWeb")Users users) {
        ViewHelper viewHelper= new ViewHelper();
        Users existingUser = usersRepository.findByEmailIgnoreCase(users.getEmail());
        if (existingUser != null) {
            String random = Helpers.randomString(50);
            existingUser.setResetToken(random);
            existingUser.setResetTime(LocalDateTime.now());
            usersRepository.save(existingUser);

            Map<String,String> map = new HashMap<>();
            map.put("reseturl",  Helpers.baseurl()+"reset-password?email="+existingUser.getEmail()+"&resetToken="+random);

            mailService.sendSystemMail("reset-password", existingUser.getEmail(), map);

            return viewHelper.l("Check your email.");
        } else {
            return viewHelper.l("The ccount with the email does not exist!");
        }

    }


    // Function to handle the login process
    // /users/reset-password?email=emil@mm.gm&resetToken=123
    @GetMapping(path="/reset-password") // Map ONLY POST Requests
    public ModelAndView resetpassword_view (@ModelAttribute("SpringWeb")Users users, Model model) {
        ViewHelper viewHelper= new ViewHelper();
        Users existingUser = usersRepository.findByEmailIgnoreCase(users.getEmail());
        if (existingUser != null) {
            if (users.getResetToken().compareTo(existingUser.getResetToken())==0 && users.getResetToken().length()>48) {
                model.addAttribute("token",  existingUser.getResetToken());
                model.addAttribute("email",  existingUser.getEmail());
            }else {
               model.addAttribute("message",  Helpers.translate("Incorect url."));

            }
        } else {
            model.addAttribute("message",  Helpers.translate("The account with the email does not exist!"));

        }
        model.addAttribute("title",  Helpers.translate("Reset password."));
        return view("theme::newpassword") ;
    }

    // Function to handle the login process
    @PostMapping(path="/set-password") // Map ONLY POST Requests
    public @ResponseBody
    String setpassword (@ModelAttribute("SpringWeb")Users users, HttpServletRequest request) {
        Users existingUser = usersRepository.findByEmailIgnoreCase(users.getEmail());
        if (existingUser != null) {
            if (users.getResetToken().compareTo(existingUser.getResetToken())==0 && users.getResetToken().length()>48) {

                existingUser.setResetToken("");
                existingUser.setPassword(encoder.encode(users.getPassword()));

                usersRepository.save(existingUser);

                try {
                    //request.login(existingUser.getEmail(), users.getPassword());
                    return Helpers.translate("Your password has been reseted, try to login");
                } catch (Exception e) {
                    return Helpers.translate("Bad Credentials");
                }

                //return "New password seted.";
            }else{
                return Helpers.translate("Incorect url.");
            }

        } else {
            return Helpers.translate("The ccount with the email does not exist!");
        }

    }

    @GetMapping(path="/signup/my-account") // Map ONLY POST Requests
    public ModelAndView editMyAccount (Model model, HttpServletRequest request) {
         int uid= AuthenticationSystem.currentUserId();
          Users user=null;
         if(uid>0){
           user = userService.getOne(uid);
         }

        model.addAttribute("title", Helpers.translate("My account"));
        model.addAttribute("row", user);
        return view("theme::myaccount");
    }

    @PostMapping(path="/signup/update")
    public String update (@ModelAttribute("SpringWeb")Users users, HttpServletRequest request) {
        int uid= AuthenticationSystem.currentUserId();
        Users user= new Users();
        if(uid>0){
            user = userService.getOne(uid);

            user.setFirstName(request.getParameter("firstName"));
            user.setLastName(request.getParameter("lastName"));

            String setPass= request.getParameter("creataccount");
            String pass = request.getParameter("password");

            if(setPass !=null && !pass.isEmpty() && pass != null) {
                user.setPassword(encoder.encode(pass));
            }

            usersRepository.save(user);
       }

       return "redirect:" + request.getHeader("Referer");
    }

}