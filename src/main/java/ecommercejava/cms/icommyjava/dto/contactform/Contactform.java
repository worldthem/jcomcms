package ecommercejava.cms.icommyjava.dto.contactform;

import java.util.LinkedHashMap;

public class Contactform {
    private String subject;
    private String to;
    private String submit;
    private String message;
    private LinkedHashMap<String, FieldsForm> fields;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LinkedHashMap<String, FieldsForm> getFields() {
        return fields;
    }

    public void setFields(LinkedHashMap<String, FieldsForm> fields) {
        this.fields = fields;
    }
}
