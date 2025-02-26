package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    public Collection<GameData> getAllGames() throws DataAccessException {
        return List.of();
    }

    public void updateGame(GameData gameData) throws DataAccessException {

    }

    public void deleteGame(int gameId) throws DataAccessException {

    }

    public void deleteAllGames() throws DataAccessException {

    }
}
