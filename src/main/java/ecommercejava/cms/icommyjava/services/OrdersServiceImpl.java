package ecommercejava.cms.icommyjava.services;

import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.entity.CuponApplied;
import ecommercejava.cms.icommyjava.entity.Orders;
import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.entity.Users;
import ecommercejava.cms.icommyjava.helper.AuthenticationSystem;
import ecommercejava.cms.icommyjava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService{

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CuponAppliedRepository cuponAppliedRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    CountryServices countryServices;

    @Override
    public Page<Orders> findByUseridOrderByOrdersidDesc(Integer userid, Pageable pageable) {
        return ordersRepository.findByUseridOrderByOrdersidDesc(userid,  pageable);
    }

    @Override
    public Page<Orders> findByStatusContainingOrShippingContainingOrSecretnrContainingOrderByOrdersidDesc(String status, String txt, String secret, Pageable pageable) {
        return ordersRepository.findByStatusContainingOrShippingContainingOrSecretnrContainingOrderByOrdersidDesc( status,  txt,  secret, pageable);
    }

    @Override
    public Page<Orders> findByStatusNotOrderByOrdersidDesc(String status, Pageable pageable) {
        return ordersRepository.findByStatusNotOrderByOrdersidDesc(status, pageable);
    }

    @Override
    public Orders findFirstBySessionuserAndStatus(String sessionuser, String status) {
        return ordersRepository.findFirstBySessionuserAndStatus(sessionuser,status) ;
    }

    @Override
    public Orders findFirstBySessionuserAndOrdersid(String sessionuser, Integer ordersid) {
        return ordersRepository.findFirstBySessionuserAndOrdersid(sessionuser,ordersid);
    }

    @Override
    public Orders getOne(Integer ordersid) {
        return ordersRepository.getOne(ordersid);
    }

    @Override
    public Orders findFirstBySessionuser(String sessionuser) {
        return ordersRepository.findFirstBySessionuser(sessionuser);
    }


    @Override
    public Orders findFirstBySecretnr(String secretnr, Model model, String getBy) {
        String forseLogin = Helpers.getConfig().getMinOptions().getForseCheckoutLogin();
        Integer uid = AuthenticationSystem.currentUserId();

       Orders order =  getBy.contains("admin")? ordersRepository.getOne(Integer.parseInt(secretnr)) :
                (forseLogin !=null ? ordersRepository.findFirstBySecretnrAndUserid(secretnr , uid) :  ordersRepository.findFirstBySecretnr(secretnr ));

       if(order !=null){

           List<String> paramsIn= new ArrayList<>();
           paramsIn.add("billing");
           paramsIn.add("shipping");
           List<Settings> settings = settingsRepository.findByParamInOrderByIdDesc(paramsIn );
           model.addAttribute("fieldsData", settings);
           model.addAttribute("countryServices", countryServices);

             CuponApplied cupon =  cuponAppliedRepository.findByOrdersid(order.getOrdersid());
             Users user = order.getUserid() > 0 ? usersRepository.getOne(order.getUserid()) : null;
             model.addAttribute("cupon", (cupon !=null? cupon.getCuponmodule()  : null));
             model.addAttribute("user", user);

         }
        return order;
    }

    @Override
    public Orders save(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    public Orders findByUseridAndOrdersid(Integer userid, Integer ordersid) {
        return ordersRepository.findByUseridAndOrdersid(userid, ordersid);
    }

    @Override
    public void deleteById(Integer id) {
         ordersRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return ordersRepository.count();
    }
}
