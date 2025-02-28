package dataaccess;

import model.GameData;
import model.GameDataTX;

import java.util.Collection;

public interface GameDAO {
    //createGame
    int createGame(String gameName) throws DataAccessException;

    //getGame
    GameData getGame(int gameId) throws DataAccessException;

    //listGames
    Collection<GameDataTX> getAllGames() throws DataAccessException;

    //updateGame
    void updateGame(int gameId, String username, boolean isWhite) throws DataAccessException;

    //deleteAllGames
    void deleteAllGames() throws DataAccessException;
}
