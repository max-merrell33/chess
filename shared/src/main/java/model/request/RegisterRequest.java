package model.request;

public class RegisterRequest extends Request {
    public String username;
    public String password;
    public String email;

    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
