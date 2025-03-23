package model.request;

public class GetGameRequest extends AuthenticatedRequest {
    public int gameID;

    public GetGameRequest(String authToken, int gameID) {
        this.authToken = authToken;
        this.gameID = gameID;
    }
}
