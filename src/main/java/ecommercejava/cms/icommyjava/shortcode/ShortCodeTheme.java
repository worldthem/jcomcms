package ecommercejava.cms.icommyjava.shortcode;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ShortCodeTheme implements ShortCode{

    /**
     * this is an example if you want to create your own shortcodes, its very easy just follow the code bellow, it also support db connections, enjoy it :)
     * @param code
     * @param map
     * @return
     */
    public String shortCodes(String code, Map<String,String> map){
        String data = null;
        switch (code) {
            case "testShortcode":
                data = testShortcode(map);
                break;
            case "testShortcode2":
                data = testShortcode2(map);
                break;

        }
        return  data;
    }

    public String testShortcode(Map<String, String> map){
        return " test code";
    }

    public String testShortcode2(Map<String, String> map){
        return " test code2";
    }
}
