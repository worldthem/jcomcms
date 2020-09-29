package ecommercejava.cms.icommyjava.services;


import ecommercejava.cms.icommyjava.shortcode.ShortCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DoShortCode {

    @Autowired
    List<ShortCode> shortCodes;

    /**
     * will parce the text and will get the content between [],
     * and will call  shortCodes(shortCodeName, data) from shortCodes
     * exmample short code [menu id=44]
     * shortCode.shortCodes("menu", Map {"id":"44"});
     * @param text
     * @return
     */
    public String doshortcodes(String text){
        if(text == null)
            return "";

        String newtxt = text;
        Matcher m = Pattern.compile("\\[([^]]+)\\]").matcher(newtxt);
        if(m == null)
            return text;

        while(m.find()) {
            HashMap<String, String> data = new HashMap<>();
            String shortCodeName = m.group(1);
            String lastKey ="";
            if(m.group(1).contains("=")){
                String[] split = m.group(1).split(" ");
                shortCodeName = split[0];
                for(int i=1;i<split.length;i++){
                    if( split[i].contains("=")) {
                        String[] split2 = split[i].split("=");
                        data.put(split2[0], split2[1]);
                        lastKey = split2[0];
                    }else{
                        String lastKVal = data.get(lastKey);
                        data.put(lastKey, lastKVal + " " + split[i]);
                    }
                }

            }

           try {
               for(ShortCode shortCode : shortCodes){
                   newtxt = newtxt.replace("["+m.group(1)+"]", shortCode.shortCodes(shortCodeName, data));
                   String txt = shortCode.shortCodes(shortCodeName, data);
                   //Map<String, String> mp = new HashMap<>(shortCode.shortCodes(data));
                    if(!txt.isEmpty()){
                        newtxt = newtxt.replace("["+m.group(1)+"]", txt);
                        //newtxt = newtxt.replace("["+m.group(1)+"]", mp.get(shortCodeName));
                    }
               }
           }catch (Exception e){}
        }

        return newtxt;
    }
}
