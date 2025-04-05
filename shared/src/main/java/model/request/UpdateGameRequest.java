package model.request;

import model.GameData;

public class UpdateGameRequest extends AuthenticatedRequest {
    public GameData gameData;

    public UpdateGameRequest(String authToken, GameData gameData) {
        this.authToken = authToken;
        this.gameData = gameData;
    }
}
