package model.request;

public class JoinRequest extends AuthenticatedRequest {
    public String playerColor;
    public int gameID;

    public JoinRequest(String authToken, String playerColor, int gameID) {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
}
