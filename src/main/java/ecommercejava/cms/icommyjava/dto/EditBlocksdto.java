package ecommercejava.cms.icommyjava.dto;


import ecommercejava.cms.icommyjava.Helpers;

public class EditBlocksdto {
    private String style;
    private String css;
    private String content;

    public EditBlocksdto(String style, String css, String content) {
        this.style = style;
        this.css = css;
        this.content = content;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getContent() {
       return content != null? content.contains("col-md")? content :"<div class=\"col-md-12 inside_grid\" id=\"id"+ Helpers.randomString(5)+"\">"+content+"</div>":"";
     }

    public String getText() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
