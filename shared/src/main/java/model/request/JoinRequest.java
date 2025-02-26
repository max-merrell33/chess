package model.request;

public class JoinRequest extends Request {
    public String authToken;
    public String playerColor;
    public int gameID;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
