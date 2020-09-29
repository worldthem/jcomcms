package ecommercejava.cms.icommyjava.shortcode;

import java.util.Map;

public interface ShortCode {
    String shortCodes(String code, Map<String,String> map);
}
