package ecommercejava.cms.icommyjava.paymentmethods.paypal;

public class PaypalPayerInfo {
    private String firstName;
    private String lastName;
    private String email;

    public PaypalPayerInfo(String firstName, String lastName, String email) {
        this.firstName = firstName == null ? "":firstName;
        this.lastName = lastName == null ? "":lastName;
        this.email = email == null ? "":email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }


}
