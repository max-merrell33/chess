package dataaccess;

import model.GameData;
import model.GameDataTX;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    public Collection<GameDataTX> getAllGames() throws DataAccessException {
        return List.of();
    }

    public void updateGame(int gameId, String username, boolean isWhite) throws DataAccessException {

    }

    public void deleteGame(int gameId) throws DataAccessException {

    }

    public void deleteAllGames() throws DataAccessException {

    }
}
