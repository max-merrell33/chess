package dataaccess;

import model.GameData;
import model.GameDataTX;

import java.util.Collection;

public interface GameDAO {
    //createGame
    public int createGame(String gameName) throws DataAccessException;

    //listGames
    public Collection<GameDataTX> getAllGames() throws DataAccessException;

    //updateGame
    public void updateGame(GameData gameData) throws DataAccessException;

    //deleteGame
    public void deleteGame(int gameId) throws DataAccessException;

    //deleteAllGames
    public void deleteAllGames() throws DataAccessException;
}
