package ecommercejava.cms.icommyjava.helper;



import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Attributes.Attributes;
import ecommercejava.cms.icommyjava.dto.Attributes.AttributesVar;

import java.util.Map;

public class CartCheckout {

    /**
     * get from product attr option
     * @param attr
     * @param idOption
     * @return Size:20; Color:Red;
     */
    public static String getOptionTitle(Map<String, Map<String, String>> attr, String idOption){
        Attributes attributes = Helpers.getAttrSettings();
        String retData="";
        if(attr.size()>0 && !idOption.isEmpty()){
            if(attr.containsKey(idOption)) {
                Map<String, String> option = attr.get(idOption);
                for(Map.Entry<String, String> entry: option.entrySet()) {

                    if(attributes.getFields().containsKey(entry.getKey())) {
                        retData = retData + attributes.getFields().get(entry.getKey()).getNameTranslated()+" : "+ attributes.getFields().get(entry.getKey()).getSugestionByKey(entry.getValue())+"; ";
                    }
                }
            }

        }

        return retData;
    }




}
