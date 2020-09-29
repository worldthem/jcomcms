package ecommercejava.cms.icommyjava.controllers;


import ecommercejava.cms.icommyjava.Helpers;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController extends MainController  implements ErrorController {

    @RequestMapping("/errors")
    public ModelAndView handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("title", "error");

        String error="";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                 error="404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                error="500";
            }
        }


        model.addAttribute("title", status);
        model.addAttribute("errortype", error);
        String originalUri ="";
        try {
            originalUri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI).toString();
        }catch (Exception e){}
            originalUri = originalUri != null? originalUri : "";

        String view =  Helpers.viewExist("theme::404") ? "theme::404" : "standart::404";
                view = originalUri.contains("admin")? "admin::404" : view;
        return  view(view);
    }

    /**
     * @deprecated
     */
    @Override
    public String getErrorPath() {
        return null;
    }

}