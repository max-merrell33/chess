package model;

public record GameDataTX(int gameID, String whiteUsername, String blackUsername, String gameName ) {
    public GameDataTX(GameData gameData) {
        this(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName());
    }
}
