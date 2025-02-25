package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private int nextId = 1;
    private final HashMap<Integer, GameData> allGameData = new HashMap<>();
    //createGame
    public void createGame(GameData gameData) throws DataAccessException {
        allGameData.put(nextId++, gameData);
    }

    //listGames
    public Collection<GameData> getAllGames() throws DataAccessException {
        return allGameData.values();
    }

    //updateGame
    public void updateGame(GameData gameData) throws DataAccessException {
        allGameData.put(gameData.gameID(), gameData);
    }

    //deleteGame
    public void deleteGame(int gameId) throws DataAccessException {
        allGameData.remove(gameId);
    }

    //deleteAllGames
    public void deleteAllGames() throws DataAccessException {
        allGameData.clear();
    }
}
