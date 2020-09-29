package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.dto.UserRegistrationDto;
import ecommercejava.cms.icommyjava.mail.MailService;
import ecommercejava.cms.icommyjava.services.UserService;
import ecommercejava.cms.icommyjava.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    MailService mailService;

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }


    @ModelAttribute("users")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public
    @ResponseBody
    String registerUserAccount(@ModelAttribute("users") UserRegistrationDto registrationDto, BindingResult bindingResult, Model model, HttpServletRequest request) {

        String err= userValidator.validatedata(registrationDto, bindingResult);
        if (!err.isEmpty()) {
                return err;
          }

      try {
          registrationDto.setUserRole("ROLE_USER");
          userService.save(registrationDto);
      }catch (Exception e){
          System.out.println("Error insert user is:"+e);
      }

        try {
            request.login(registrationDto.getEmail(), registrationDto.getPassword());
        } catch (Exception e) { }

        Map<String,String> map = new HashMap<>();
        map.put("email", registrationDto.getEmail());
        map.put("password", registrationDto.getPassword());

        mailService.sendSystemMail("new-account", registrationDto.getEmail(), map);

        String typelogin =request.getParameter("typelogin");
        return typelogin!=null ? (typelogin.contains("admin")? "redirect::/admin": "reload") : "reload" ;
   }
}