package ecommercejava.cms.icommyjava.controllers;


import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.*;
import ecommercejava.cms.icommyjava.helper.AuthenticationSystem;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.HelpersJson;
import ecommercejava.cms.icommyjava.repository.CartRepository;
import ecommercejava.cms.icommyjava.repository.VisitsRepository;
import ecommercejava.cms.icommyjava.repository.WishlistRepository;

import ecommercejava.cms.icommyjava.services.OrdersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
public class SetterController extends MainController{
    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    OrdersServiceImpl ordersService;

    @Autowired
    VisitsRepository visitsRepository;

    private final int ARBITARY_SIZE = 1048;

   private Map<String, String> extension = new HashMap<String, String>() {{
        put("aac","audio/aac");
        put("avi","video/x-msvideo");
        put("csv","text/csv");
        put("doc","application/msword");
        put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        put("eot","application/vnd.ms-fontobject");
        put("gif","image/gif");
        put("html","text/html");
        put("jpg","image/jpeg");
        put("jpeg","image/jpeg");
        put("mp3","audio/mpeg");
        put("mpeg","video/mpeg");
        put("oga","audio/ogg");
        put("png","image/png");
        put("pdf","application/pdf");
        put("svg","image/svg+xml");
        put("txt","text/plain");
        put("xls","application/vnd.ms-excel");
        put("xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        put("zip","application/zip");
        put("3gp","video/3gpp");
        put("7z","application/x-7z-compressed");
        put("oct","APPLICATION/OCTET-STREAM");
    }};

    @GetMapping("/set-lang/{lang}")
    public String setLang(@PathVariable String lang, HttpServletRequest request, HttpServletResponse response) {

        if(lang !=null) {
            Cookie cookie = new Cookie("sitelangdata", lang);
            cookie.setMaxAge(200 * 24 * 60 * 60);
            cookie.setPath("/");
            //add cookie to response
            response.addCookie(cookie);
        }

        return  "redirect:" + (lang !=null? request.getHeader("Referer"): Helpers.baseurl());
    }

    @GetMapping("/set-currency/{currency}")
    public String setCurrency(@PathVariable String currency, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("currency", currency);
        cookie.setMaxAge(200 * 24 * 60 * 60);
        cookie.setPath("/");
        //add cookie to response
        response.addCookie(cookie);
        return  "redirect:" + request.getHeader("Referer");
    }




    @GetMapping("/add-to-wishlist/{productid}")
    public @ResponseBody
    String addtowishlist(@PathVariable String productid, HttpServletRequest request, HttpServletResponse response, Model model) {
       String uid= getCartUserId(request, response);
        Wishlist wishlist = wishlistRepository.findFirstByTmpsesionidAndProduct_productid(uid, Integer.parseInt(productid));
        if(wishlist==null){

            Wishlist w = new Wishlist();
            w.setTmpsesionid(uid);
            w.getProduct().setProductid(Integer.parseInt(productid));
        }
         return  "ok";
    }

    @GetMapping("/download-user-file/{ordersid}/{cartid}/{fileid}")
     public String download(@PathVariable String ordersid, @PathVariable String cartid, @PathVariable String fileid, HttpServletRequest req, HttpServletResponse resp) {
           int currentUserId = AuthenticationSystem.currentUserId();
            Orders orders = ordersService.findByUseridAndOrdersid(currentUserId, Integer.parseInt(ordersid));

            if(orders == null)
                return  "redirect:" + req.getHeader("Referer");

            Cart cart = cartRepository.findByOrdersidAndCartid(orders.getOrdersid(), Integer.parseInt(cartid));

              if(cart == null)
                return  "redirect:" + req.getHeader("Referer");

                 List<Gallery> gallery = cart.getProduct().getGallery();


                    if(gallery == null)
                      return  "redirect:" + req.getHeader("Referer");


                     String[] redirectBack = {"yes"};
                     gallery.forEach((v)->{
                          if(v.getId().equals(Integer.parseInt(fileid))) {
                              String fileName = v.getDirectory();
                              String extens = "oct";
                              if(fileName.contains(".")) {
                                  String[] split = fileName.split("\\.");
                                  extens = split[split.length-1];
                              }

                              try {
                                  PrintWriter out = resp.getWriter();
                                  if(extension.containsKey(extens))
                                    resp.setContentType(extension.get(extens));

                                    String savedName = v.getTitle().replaceAll(" ","_").replaceAll("'","");
                                    resp.setHeader("Content-disposition", "attachment; filename="+savedName+"."+extens);
                                    redirectBack[0]="not";

                                    cart.setDownloaded(cart.getDownloaded()+1);
                                    cartRepository.save(cart);

                                  FileInputStream fileInputStream = new FileInputStream(HelpersFile.pathToFile("content/files/"+fileName));
                                  int i;
                                  while ((i = fileInputStream.read()) != -1) {
                                      out.write(i);
                                  }
                                  fileInputStream.close();
                                  out.close();


                              } catch (Exception e) {
                              }
                          }
                       });

             return redirectBack[0].contains("yes")? "redirect:" + req.getHeader("Referer") : null;
        }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @GetMapping("/analitycs-set")
    public @ResponseBody
    String analitycs( HttpServletRequest request, HttpServletResponse response, Model model) {
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("referer");
        String increesTime = request.getParameter("updatetime");
     /*
        System.out.println("userAgent:"+userAgent+";referer:"+referer+";previos:"+request.getParameter("refer")+";ip:"+getClientIP(request));

        //host
        Enumeration ed = request.getHeaderNames();
        while (ed.hasMoreElements()) {
            String name = (String)ed.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " = " + value);
        }

      */

        if(   userAgent.contains("google") ||
              userAgent.contains("baidu") ||
              userAgent.contains("bot")  ){
            return  "";
        }

       try{

           Visits visits = visitsRepository.findFirstByIpvisitAndDate(getClientIP(request), LocalDate.now());
             if(visits !=null){
                 List<String> pagesvisit =  HelpersJson.isJSONArrValid(visits.getPagevisit());
                 String[] split = referer.split(request.getHeader("host"));
                 if(increesTime == null) {
                     if (!pagesvisit.contains(split[1])) {
                         pagesvisit.add(split[1]);
                         visits.setPagevisit((new Gson()).toJson(pagesvisit));
                         visitsRepository.save(visits);
                     }
                 }else{
                     visits.setTimespend(visits.getTimespend() + 20);
                     visitsRepository.save(visits);
                 }
             }else{
                 Visits vis = new Visits();
                 vis.setDate(LocalDate.now());
                 vis.setIpvisit(getClientIP(request));
                 String[] split = referer.split(request.getHeader("host"));
                 vis.setPagevisit("[\""+split[1]+"\"]");
                 vis.setUrl(request.getParameter("refer"));
                 vis.setUser_agent(userAgent);
                 visitsRepository.save(vis);
             }


        }catch (Exception e){}


        return "ok";
    }


}
