package model.request;

public class LogoutRequest extends Request {
    public String authToken;

    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }
}
