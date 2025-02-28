package model.request;

public class CreateRequest extends Request {
    public String authToken;
    public String gameName;

    public CreateRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
