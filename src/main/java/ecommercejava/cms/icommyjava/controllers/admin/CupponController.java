package ecommercejava.cms.icommyjava.controllers.admin;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Cupon;
import ecommercejava.cms.icommyjava.repository.CuponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CupponController extends MainController {

    @Autowired
    CuponRepository cuponRepository;

    @RequestMapping(value ="/admin/cupon", method = RequestMethod.GET)
    public ModelAndView show(Model model, HttpServletRequest request) {
        model.addAttribute("title", "Cuppon");
        Iterable<Cupon> rows = cuponRepository.findAll();
        model.addAttribute("rows", rows);
        return view("admin::pages/cupon") ;
    }


    /**
     *  get data from post and put in database, and after redirect back this use for add and update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/cupon/store") // Map ONLY POST Requests

    public  String store (HttpServletRequest request, RedirectAttributes redirectAttributes ) {
        try {
            Cupon cupon = new Gson().fromJson(Helpers.convertRequestt(request.getParameterMap()), Cupon.class);
            cuponRepository.save(cupon);
        }catch (Exception e){
            System.out.println(e);
        }

        redirectAttributes.addFlashAttribute("message","Successfully updated!");
        return request.getParameter("returndata") != null ? viewpatNohtml("admin::layouts/ok" ) : "redirect:" + request.getHeader("Referer");
    }


    /**
     *  bulk update
     * @param request
     * @return
     */

    @GetMapping(path="/admin/cupon/delete") // Map ONLY POST Requests

    public String delete (@RequestParam(value ="id") String id, HttpServletRequest request ) {

        try{
            cuponRepository.deleteById(Integer.parseInt(id));
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }

    /**
     *  bulk update
     * @param request
     * @return
     */

    @PostMapping(path="/admin/cupon/bulk") // Map ONLY POST Requests

    public String bulk (HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam("bulkid") List<String> bulkid) {
        String action = request.getParameter("action");
        try{
            if(bulkid.size()>0 ) {
                if (action.contains("del")) {
                    for (String idnr : bulkid) {
                        cuponRepository.deleteById(Integer.parseInt(idnr));
                    }
                }
            }
        }catch (Exception e){System.out.println("errrr:"+e);}
        return "redirect:" + request.getHeader("Referer");
    }
}
