package ecommercejava.cms.icommyjava.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookies {
    /**
     * Get value from cookie by name
     * @param name
     * @return
     */


    public static String get(String name, HttpServletRequest request){
         Cookie[] cookies = request.getCookies();
        if(cookies !=null) {
            String data ="";

            for (Cookie cookie : cookies) {
                //display only the cookie with the name 'name'
                if (cookie.getName().contains(name)) {
                    data =cookie.getValue();
                    break;
                }
            }
            return data;
        }
        return "";
    }

    public static void set(String name, String val, HttpServletResponse response){
        Cookie cookie = new Cookie(name, val);
        cookie.setMaxAge(200 * 24 * 60 * 60);
        cookie.setPath("/");
        //add cookie to response
        response.addCookie(cookie);
    }
}
