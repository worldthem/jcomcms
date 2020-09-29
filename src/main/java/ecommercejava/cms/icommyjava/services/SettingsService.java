package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.entity.Settings;
import ecommercejava.cms.icommyjava.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {
    @Autowired
    SettingsRepository settingsRepository;


    public Settings findByParamAndValue2(String param, String val2){
        return  settingsRepository.findByParamAndValue2( param,  val2);
    }
    public Settings findFirstByParam(String param ){
        return  settingsRepository.findFirstByParam( param );
    }
    public Settings save(Settings setting){
        return  settingsRepository.save(setting);
    }
    public List<Settings> findByParamInOrderByIdDesc(List<String> list){
        return  settingsRepository.findByParamInOrderByIdDesc(list);
    }
    public List<Settings> findByParam(String param){
        return settingsRepository.findByParamOrderByIdDesc(param);
    }

    public List<Settings> findAll(){
        return  settingsRepository.findAll();
    }
    public Settings getOne(Integer id){
        return  settingsRepository.getOne(id);
    }
    public void delete(Integer id){
         settingsRepository.deleteById(id);
    }
    public long count(){
        return settingsRepository.count();
    }

}
