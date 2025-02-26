package model.request;

public class CreateRequest extends Request {
    public String authToken;
    public String gameName;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
