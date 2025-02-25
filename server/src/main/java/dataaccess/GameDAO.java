package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    //createGame
    public void createGame(GameData gameData);

    //listGames
    public Collection<GameData> getAllGames();

    //updateGame
    public void updateGame(GameData gameData);

    //deleteGame
    public void deleteGame(int gameId);

    //deleteAllGames
    public void deleteAllGames();
}
