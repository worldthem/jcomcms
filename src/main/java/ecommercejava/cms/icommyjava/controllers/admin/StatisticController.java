package ecommercejava.cms.icommyjava.controllers.admin;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.controllers.MainController;
import ecommercejava.cms.icommyjava.entity.Lang;
import ecommercejava.cms.icommyjava.entity.Visits;
import ecommercejava.cms.icommyjava.helper.HelpersFile;
import ecommercejava.cms.icommyjava.helper.HelpersJson;
import ecommercejava.cms.icommyjava.repository.VisitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class StatisticController extends MainController {

   @Autowired
   private VisitsRepository visitsRepository;



   @GetMapping(path="/admin/statistic")
   public ModelAndView show(HttpServletRequest request, Model model){

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("dd");
            DateTimeFormatter dtfMonth = DateTimeFormatter.ofPattern("MM");
            LocalDate now = LocalDate.now();
            LocalDate dateMinus = now.minusDays(30);
            String d = request.getParameter("d");
            List<Visits> getStatistic = d != null? visitsRepository.findByDateBetween(LocalDate.parse(d), LocalDate.parse(d)):
                                                   visitsRepository.findByDateBetween(dateMinus, now);

            Map<String, String[]> dates = new LinkedHashMap<>();
            Map<String, Long> source = new LinkedHashMap<>();
            Map<String, Integer> pageVisit = new LinkedHashMap<>();

       try{
            if(getStatistic != null){
                for(Visits visit: getStatistic){
                    String datedb = dtf.format(visit.getDate());
                    String [] parm2 = {dtfDay.format(visit.getDate()), dtfMonth.format(visit.getDate()), "1", Long.toString(visit.getTimespend())};
                     if(dates.containsKey(datedb)){
                         parm2[2] = Integer.toString(Integer.parseInt(dates.get(datedb)[2]) +1);
                         parm2[3] = Long.toString(Long.parseLong(dates.get(datedb)[3]) + visit.getTimespend());
                         dates.put(datedb, parm2);
                    }else{
                        dates.put(datedb, parm2);
                    }
                     // For sources
                     String souc = visit.getUrl().isEmpty() ? "Direct": visit.getUrl();
                    if(source.containsKey(souc)){
                        source.put(souc, source.get(souc) + visit.getTimespend());
                    }else{
                        source.put(souc, visit.getTimespend());
                    }

                    // For pageVisit
                    // All the pages users access it saved in db in json format in fields - pagevisit
                    List<String> pagesvisiting =  HelpersJson.isJSONArrValid(visit.getPagevisit());

                    if(pagesvisiting !=null) {
                       for (String pag : pagesvisiting) {
                           if (pageVisit.containsKey(pag)) {
                               pageVisit.put(pag, pageVisit.get(pag) + 1);
                           } else {
                               pageVisit.put(pag, 1);
                           }
                       }
                    }

                }
            }
       }catch (Exception e){
            System.out.println("errr data"+e);
        }



       /*
            CityResponse response = dbReader.city(ipAddress);

            String countryName = response.getCountry().getName();
            String cityName = response.getCity().getName();
            String postal = response.getPostal().getCode();
            String state = response.getLeastSpecificSubdivision().getName();
            */
       Map<String, Long> countries = new LinkedHashMap<>();
       String dbLocation = HelpersFile.pathToFile("content/config/GeoLite2-Country.mmdb");

           File database = new File(dbLocation);
       DatabaseReader dbReader=null;
       try {
            dbReader = new DatabaseReader.Builder(database).build();
       }catch (Exception e){}

           if(getStatistic != null){
               for(Visits visit: getStatistic){
                   String country =  visit.getIpvisit() ;
                   try{
                         InetAddress ipAddress = InetAddress.getByName(visit.getIpvisit());
                         country =dbReader !=null?  dbReader.country(ipAddress).getCountry().getName() : country ;
                   }catch (Exception e){ System.out.println("Contries err:"+e); }

                       if (countries.containsKey(country)) {
                           countries.put(country, countries.get(country) + visit.getTimespend());
                       } else {
                           countries.put(country, visit.getTimespend());
                       }

                }
           }

       model.addAttribute("rowDate",dates);
       model.addAttribute("rowCountries",countries);
       model.addAttribute("rowSource", source);
       model.addAttribute("rowPageVisit",pageVisit);

       return view("admin::pages/statistic");
   }

}
