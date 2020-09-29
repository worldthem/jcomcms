package ecommercejava.cms.icommyjava.config;



import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.helper.Cookies;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * in this controler we do language remember if you delete it than multiple language will not work
 * here we delete en from url and add it to the end the url will be like ?
 * http://localhost:8999/en/product/seven-steps
 *  we remove this part "en/" and add to the end wlang=en
 *  on the end we will have
 *  http://localhost:8999/product/seven-steps?wlang=en
 *
 *  when you have a controler like
 *  @RequestMapping(path={"/page"}) you dont have to add locale like @RequestMapping(path={"/{locale}/page"})
 *
 */


@Component
public class PathVariableLocaleFilter extends OncePerRequestFilter {

    private static final String LOCALE_ATTRIBUTE_NAME = "wlang";
   /// private static final Logger LOG = ;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String url = StringUtils.defaultString(request.getRequestURI().substring(request.getContextPath().length()));
        String[] variables = url.split("/");
        Boolean redirect = false;
        if(url.contains("/admin") || Helpers.getConfig().getLanguages().size() == 1 || url.contains("/set-lang")) {
            //filterChain.doFilter(request, response);

        }else{

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String mainLang = Helpers.getConfig().getLanguages().get(0);
            String CookieLang = Cookies.get("sitelangdata", request);
            CookieLang = CookieLang.isEmpty() ? mainLang: CookieLang;

            //String currentUrlLang = variables.length > 1 && isLocale(variables[1]) ? variables[1]: "";
            String querisURL = request.getQueryString() == null? "": "?"+request.getQueryString();

            if (variables.length > 1 && isLocale(variables[1])) { // in url is language like "it" or "en"
                if(!CookieLang.contains(variables[1])){ // if lang from url is not equal with lang from cookies than we add new lang to url and redirect it
                    String newUrls = StringUtils.removeStart(url, '/' + variables[1]); // remove the lang from url
                    newUrls = mainLang.contains(CookieLang)? newUrls+querisURL : "/"+CookieLang+newUrls+querisURL;
                    httpResponse.sendRedirect(newUrls);
                    redirect = true;
                }
            }else{ // the url not contain any lang
                if(!CookieLang.contains(mainLang)){ // we check if lang from cookies is different from main lang than we redirect if not go next
                    httpResponse.sendRedirect("/"+CookieLang+url+querisURL);
                    redirect = true;
                }
            }


        }



      if(!redirect) { // check if was any url we skip this part if not than we go to next

          if (variables.length > 1 && isLocale(variables[1])) { // we check if lang is in url than we add the end of url wlang=en
              //LOG.debug("Found locale {}", variables[1]);

              String newUrl = StringUtils.removeStart(url, '/' + variables[1]); // remove lang from url

              //request.setAttribute(LOCALE_ATTRIBUTE_NAME, variables[1]);
              newUrl = request.getQueryString() != null ? newUrl + "?" + request.getQueryString() + "&" + LOCALE_ATTRIBUTE_NAME + "=" + variables[1] :
                      newUrl + "?" + LOCALE_ATTRIBUTE_NAME + "=" + variables[1]; // add  to the end of url  wlang=seted language

                RequestDispatcher dispatcher = request.getRequestDispatcher(newUrl);
                dispatcher.forward(request, response);

          } else {
              filterChain.doFilter(request, response);
          }
      }
    }

    private boolean isLocale(String locale) {
        //validate the string here against an accepted list of locales or whatever
        try {
            LocaleUtils.toLocale(locale);
            return true;
        } catch (IllegalArgumentException e) {
            //LOG.trace("Variable \'{}\' is not a Locale", locale);
        }
        return false;
    }



}
