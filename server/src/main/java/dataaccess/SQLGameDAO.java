package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameDataTX;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

    UtilsDB utilsDB = new UtilsDB();

    public SQLGameDAO() throws DataAccessException {
        utilsDB.configureDatabase();
    }

    public int createGame(String gameName) throws DataAccessException {
        ChessGame newChessGame = new ChessGame();
        var json = new Gson().toJson(newChessGame);
        var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        return utilsDB.executeUpdate(statement, gameName, json);
    }

    public GameData getGame(int gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public Collection<GameDataTX> getAllGames() throws DataAccessException {
        var result = new ArrayList<GameDataTX>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGameTX(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        String statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";

        utilsDB.executeUpdate(statement,
                gameData.whiteUsername(),
                gameData.blackUsername(),
                new Gson().toJson(gameData.game()),
                gameData.gameID()
        );
    }

    public void deleteAllGames() throws DataAccessException {
        var statement = "TRUNCATE games";
        utilsDB.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");

        var json = rs.getString("game");
        var game = new Gson().fromJson(json, ChessGame.class);

        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    private GameDataTX readGameTX(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");

        return new GameDataTX(gameID, whiteUsername, blackUsername, gameName);
    }
}
