package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameDataTX;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private int nextID = 1;
    private final HashMap<Integer, GameData> allGameData = new HashMap<>();

    //createGame
    public int createGame(String gameName) throws DataAccessException {
        int newGameID = nextID++;
        GameData newGame = new GameData(newGameID, null, null, gameName, new ChessGame());
        allGameData.put(newGameID, newGame);
        return newGameID;
    }

    public GameData getGame(int gameId) throws DataAccessException {
        return allGameData.get(gameId);
    }

    //listGames
    public Collection<GameDataTX> getAllGames() throws DataAccessException {
        return allGameData.values().stream()
                .map(GameDataTX::new)
                .toList();
    }

    //updateGame
    public void updateGame(GameData gameData) throws DataAccessException {
        allGameData.put(gameData.gameID(), gameData);
    }

    //deleteAllGames
    public void deleteAllGames() throws DataAccessException {
        allGameData.clear();
    }
}
