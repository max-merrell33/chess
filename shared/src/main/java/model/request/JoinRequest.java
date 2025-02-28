package model.request;

public class JoinRequest extends Request {
    public String authToken;
    public String playerColor;
    public int gameID;

    public JoinRequest(String authToken, String playerColor, int gameID) {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
