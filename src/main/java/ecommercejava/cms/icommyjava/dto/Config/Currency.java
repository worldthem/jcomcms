package ecommercejava.cms.icommyjava.dto.Config;

public class Currency {
   private String name;
    private String code;
    private String main;
    private String type;
    private String rate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getMain() {
        return main==null? false : true;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
