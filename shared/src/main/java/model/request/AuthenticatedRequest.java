package model.request;

public class AuthenticatedRequest extends Request {
    public String authToken;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
