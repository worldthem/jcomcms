package ecommercejava.cms.icommyjava.mail;
 
import com.google.gson.Gson;
import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.billingshipping.FieldsJson;
import ecommercejava.cms.icommyjava.entity.*;
import ecommercejava.cms.icommyjava.helper.AdminHelpers;
import ecommercejava.cms.icommyjava.repository.CuponAppliedRepository;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import ecommercejava.cms.icommyjava.services.CountryServices;
import ecommercejava.cms.icommyjava.services.OrdersService;
import ecommercejava.cms.icommyjava.services.PagesServices;
import ecommercejava.cms.icommyjava.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

@Service
public class MailService {
    @Autowired
    SettingsRepository settingsRepository;

    // @Autowired
    // private JavaMailSender javaMailSender;

    @Autowired
    PagesServices pagesServices;

    @Autowired
    CountryServices countryServices;

    @Autowired
    CuponAppliedRepository cuponAppliedRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    OrdersService ordersService;


   public String sendSystemMail(String direction, String to, Map<String,String> map){

       Page page = pagesServices.getByDirection(direction);

       if(page!=null){
           if(page.getEnable() !=null){
            String subject= page.getTitle();
            String body = page.getText();
              for(Map.Entry<String, String> entry:  map.entrySet()) {
                  body = body.replaceAll("\\["+entry.getKey()+"\\]", entry.getValue());
               }
                sendmail( to, subject, body);

           }
       }

     return "";
   }



    public String sendmail(String to, String subject, String body)   {
        Settings settings = settingsRepository.findFirstByParam("email_settings");

        if(settings ==null)
            return "Mail not set!";

        Maildto maildto = new Gson().fromJson(settings.getValue1(), Maildto.class);
        String user = maildto.getUsername();
        String pass= maildto.getPassword();
        String host= maildto.getHost();
        String port= maildto.getPort();

        if(user.isEmpty() || pass.isEmpty() || host.isEmpty() || port.isEmpty())
            return "Plaes go to "+ Helpers.baseurl()+"admin/email-settings and fill all the fields!";

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", maildto.getHost());
            props.put("mail.smtp.port", maildto.getPort());

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(maildto.getUsername(), maildto.getPassword());
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(maildto.getFromemail(), false));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");
            msg.setSentDate(new Date());

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart attachPart = new MimeBodyPart();


            msg.setContent(multipart);
            Transport.send(msg);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
    }



    public String emailOrders(String status , Orders orders){

        Map<String,String>  ordersShipping = orders.getShippingMap();
        List<String> paramsIn= new ArrayList<>();
        paramsIn.add("billing");
        paramsIn.add("shipping");

        List<Settings> settings = settingsRepository.findByParamInOrderByIdDesc(paramsIn );

        CuponApplied cupon =  cuponAppliedRepository.findByOrdersid(orders.getOrdersid());

        Users user = orders.getUserid() !=null? usersService.getOne(orders.getUserid()) : null;
              user = user==null? new Users() : user;

        String shippingTxt ="";


       if(settings.size()>0) {
         shippingTxt = shippingTxt + "<table style=\"width: 100%; max-width:800px;min-width:320px\" cellspacing=\"0\">\n" +
                   "                   <tbody><tr>";
         for (Settings row : settings) {
             LinkedHashMap<String, FieldsJson> fields =  AdminHelpers.checkoutFieldsDb( row.getValue1() );
             String delimiter = row.getParam().contains("shipping")? "shipping_" : "" ;

             if(fields.size()>0){

                  String[] showData ={"false"};
                  fields.forEach((k,v)->{ if(orders.getShippingMap().containsKey(delimiter+k)){ showData[0]="true"; } });

                      if (showData[0].contains("true")) {
                          shippingTxt = shippingTxt + "<td style=\"text-align: left; border:1px solid #e4e4e4; padding:15px; vertical-align: top;\" >\n" +
                                  "                       <div style=\"font-style:italic;font-size: 17px; font-weight: bold; margin-bottom:10px;\"> " + Helpers.splitLang(row.getValue()) + "</div> ";

                          for (Map.Entry<String, FieldsJson> entry : fields.entrySet()) {
                              String value = orders.getShippingMap().containsKey(delimiter + entry.getKey()) ? orders.getShippingMap().get(delimiter + entry.getKey()) : "";

                              if (!value.isEmpty())
                                  shippingTxt = shippingTxt + "<p>" + entry.getValue().getTitleTranslated() + ":" + (entry.getKey().contains("country") ? countryServices.getCountryById(value) : value) + "</p>";

                          }
                          shippingTxt = shippingTxt + "</td>";
                      }
             }
         }

         shippingTxt = shippingTxt +"</tr> </tbody> </table>";

       }

        String products="<table style=\"width: 100%; max-width:800px;min-width:320px;border:1px solid #e4e4e4;\" cellspacing=\"0\"><tbody>";
               String baseurl = Helpers.baseurl();
             for(Cart cart : orders.getCart()){
                 products=products+"<tr>\n" +
                         "                    <td style=\"text-align: left; border-bottom:1px solid #e4e4e4; padding:15px;border-spacing: 0px;\">\n" +
                         "                        <a href=\""+baseurl + cart.getProduct().getUrl()+"\"><b> "+cart.getProduct().getTitle()+"</b> </a>\n" +
                         (cart.getOptionsid() !=null ? "<br /> <i> "+cart.getProduct().getAttrNames(cart.getOptionsid())+"</i>": "" )+
                         "                   </td>\n" +
                         "                    <td style=\"text-align: left; border-bottom:1px solid #e4e4e4; padding:15px;border-spacing: 0px;\">\n" +
                         "                      "+cart.getQty()+" <b>x</b>  "+ Helpers.price(cart.getPrice(),null)+" \n" +
                         "                    </td>\n" +
                         "  </tr>";
             }

        if(cupon !=null) {
            products = products + "<tr>\n" +
                    "                     <td style=\"text-align: right;padding:5px 15px;font-size: 15px;\"><b>" + Helpers.translate("Cupon") + "(" + cupon.getCuponmodule().getCupon() + "):</b> </td>\n" +
                    "                     <td style=\"text-align: left;padding:5px 15px;font-size: 15px;\"><b>" + cupon.getCuponmodule().getDiscount() + "</b></td>\n" +
                    "                 </tr>";
           }


        if(orders.getShipingTable() !=null) {
            products = products + "<tr>\n" +
                    "                <td style=\"text-align: right;padding:5px 15px;font-size: 15px;\"><b>" + Helpers.translate("Shipping") + "(" + orders.getShipingTable().getShipping_name() + "):</b> </td>\n" +
                    "                <td style=\"text-align: left;padding:5px 15px;font-size: 15px;\"><b>" + Helpers.price(orders.getShipingTable().getPrice(), null) + "</b></td>\n" +
                    "          </tr>";
           }

        products=products+"<tr>\n" +
                "                <td style=\"text-align: right;padding:5px 15px;font-size: 15px;\"><b>"+ Helpers.translate("Total payd")+":</b> </td>\n" +
                "                <td style=\"text-align: left;padding:5px 15px;font-size: 15px;\"><b>"+ Helpers.price(orders.getTotalpayd(),null)+"</b></td>\n" +
                "          </tr>" ;

        products=products+"</tbody></table>";


        String email = orders.getShippingMap().containsKey("email")? orders.getShippingMap().get("email") : user.getEmail();




        Map<String,String> map = new HashMap<>();
        map.put("order_number",  Integer.toString(orders.getOrdersid()));
        map.put("products",  products);
        map.put("billing_shiping",  shippingTxt);
        map.put("order_amount",  Float.toString(orders.getTotalpayd()));
        map.put("first_name",  orders.getShippingMap().containsKey("name")? orders.getShippingMap().get("name") : user.getFirstName());
        map.put("last_name",  orders.getShippingMap().containsKey("lastname")? orders.getShippingMap().get("lastname") : user.getLastName());

        return sendSystemMail(status, email, map);
    }

    /*
     public String sendMail2(String to, String title, String body){
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(to);

            helper.setSubject(title);
            helper.setText(body, true);
            //helper.addAttachment("invoice.pdf", new ClassPathResource("invoice.pdf"));
            javaMailSender.send(msg);
         return "Message sent successful";
        }catch (Exception e){
            //System.out.println("Mail error:"+e);
        return e.getMessage();
        }
    }
     */

}
