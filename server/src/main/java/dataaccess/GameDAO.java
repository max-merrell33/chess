package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    //createGame
    public void createGame(GameData gameData) throws DataAccessException;

    //listGames
    public Collection<GameData> getAllGames() throws DataAccessException;

    //updateGame
    public void updateGame(GameData gameData) throws DataAccessException;

    //deleteGame
    public void deleteGame(int gameId) throws DataAccessException;

    //deleteAllGames
    public void deleteAllGames() throws DataAccessException;
}
