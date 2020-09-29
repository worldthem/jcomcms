package ecommercejava.cms.icommyjava.helper;



import ecommercejava.cms.icommyjava.Helpers;
import ecommercejava.cms.icommyjava.dto.Config.Currency;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ViewHelpersRequest {
    HttpServletRequest request;

    public ViewHelpersRequest(HttpServletRequest request){
        this.request = request;
    }

    /**
     * convert the price in $20.20 and will return the sale price if is set
     * @param price
     * @param sale_price
     * @return
     */
    public String price(Float price, Float sale_price ){
        String currencyPosition = "nospace";
        String currencyCode ="$";
        Float rate = 1.0f;
        String mainCurrency = "USD";

        Map<String, Currency> currencies = Helpers.getConfig().getCurrencies();
        if(currencies.size()>0){
            String cookie =Cookies.get("currency", request);
               for (Map.Entry<String, Currency> entry : currencies.entrySet()) {
                    if (entry.getValue().getMain()) {
                        currencyPosition = entry.getValue().getType();
                        currencyCode = entry.getValue().getCode();
                        rate = Float.parseFloat(entry.getValue().getRate());
                        mainCurrency = entry.getKey();
                        break;
                    }
                }

            if(!cookie.isEmpty() && currencies.containsKey(cookie) && !mainCurrency.contains(cookie)){
                currencyPosition = currencies.get(cookie).getType();
                currencyCode = currencies.get(cookie).getCode();
                rate = Float.parseFloat(currencies.get(cookie).getRate());
              }
          }

        price  = price != null ? price * rate : 0;
        sale_price = sale_price !=null ? sale_price * rate : 0;

        String price_return = "";
        String p = "<span>"+  Helpers.formatDecimal(price)+"</span>";
        String sp = "<span>"+  Helpers.formatDecimal(sale_price)+"</span>";

        if(currencyPosition == "left with space"){
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+currencyCode+" "+p+"</del> "+currencyCode+" "+sp : currencyCode+" "+p;
        }else if(currencyPosition == "right with space"){
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+p+" "+currencyCode+"</del> "+sp+" "+currencyCode : p+" "+currencyCode;
        }else if(currencyPosition == "right"){
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+p+currencyCode+"</del> "+sp+currencyCode : p+currencyCode;
        }else{
            price_return  =  sale_price > 0 ? "<del class='pricesale'>"+currencyCode+  p +"</del> "+currencyCode + sp : currencyCode + p;
        }

        return price_return;
    }


}
