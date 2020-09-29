package ecommercejava.cms.icommyjava.helper;


import ecommercejava.cms.icommyjava.entity.CustomUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSystem {

    public static boolean isLogged() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null != authentication && !("anonymousUser").equals(authentication.getName());
    }
    public static String userEmail(){
        if(isLogged()){
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getName() ;
        }
         return null;
    }


    public static Integer currentUserId(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        int id = 0;
        CustomUser user;
        if(isLogged()){
            user = (CustomUser) securityContext.getAuthentication().getPrincipal();
            id = user.getId();
         }
       return id;
    }


}
