package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.GameData;
import model.GameDataTX;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DBUnitTests {

    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
    }

    @BeforeEach
    public void beforeEach() throws DataAccessException {
        authDAO.deleteAllAuths();
        gameDAO.deleteAllGames();
        userDAO.deleteAllUsers();
    }




    //AuthDAO Tests
    @Test
    @DisplayName("Good Create Auth")
    public void goodCreateAuth() throws DataAccessException {
        authDAO.createAuth("token", "username");
        authDAO.createAuth("token1", "username1");

        Assertions.assertEquals("username1", authDAO.getAuth("token1").username());
        Assertions.assertEquals("username", authDAO.getAuth("token").username());
    }

    @Test
    @DisplayName("Bad Create Auth")
    public void badCreateAuth() throws DataAccessException {
        authDAO.createAuth("token", "username");

        assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth("token", "username");
        });

    }

    @Test
    @DisplayName("Good Get Auth")
    public void goodGetAuth() throws DataAccessException {
        authDAO.createAuth("token", "username");

        Assertions.assertEquals("username", authDAO.getAuth("token").username());
    }

    @Test
    @DisplayName("Bad Get Auth")
    public void badGetAuth() throws DataAccessException {
        assertNull(authDAO.getAuth("token"));
    }

    @Test
    @DisplayName("Good Delete Auth")
    public void goodDeleteAuth() throws DataAccessException {
        authDAO.createAuth("token", "username");
        authDAO.deleteAuth("token");

        assertNull(authDAO.getAuth("token"));
    }

    @Test
    @DisplayName("Bad Delete Auth")
    public void badDeleteAuth() throws DataAccessException {
        fail();
    }

    @Test
    @DisplayName("Delete All Auths")
    public void deleteAllAuths() throws DataAccessException {
        authDAO.createAuth("token", "username");
        authDAO.createAuth("token1", "username1");
        authDAO.createAuth("token2", "username2");

        authDAO.deleteAllAuths();

        assertNull(authDAO.getAuth("token"));
        assertNull(authDAO.getAuth("token1"));
        assertNull(authDAO.getAuth("token2"));
    }




    //GameDAO Tests
    @Test
    @DisplayName("Good Create Game")
    public void goodCreateGame() throws DataAccessException {
        int gameID = gameDAO.createGame("name");

        Assertions.assertEquals("name", gameDAO.getGame(gameID).gameName());

        ChessGame startingGame = new ChessGame();

        Assertions.assertEquals(startingGame, gameDAO.getGame(gameID).game());
    }

    @Test
    @DisplayName("Bad Create Game")
    public void badCreateGame() throws DataAccessException {
        fail();
    }

    @Test
    @DisplayName("Good Get Game")
    public void goodGetGame() throws DataAccessException {
        int gameID = gameDAO.createGame("name");

        Assertions.assertEquals("name", gameDAO.getGame(gameID).gameName());
    }

    @Test
    @DisplayName("Bad Get Game")
    public void badGetGame() throws DataAccessException {
        Assertions.assertNull(gameDAO.getGame(100));
    }

    @Test
    @DisplayName("Good Get All Games")
    public void goodGetAllGames() throws DataAccessException {
        gameDAO.createGame("name");
        gameDAO.createGame("name1");
        gameDAO.createGame("name2");

        Collection<String> validNames = List.of("name", "name1", "name2");

        Collection<GameDataTX> allGames = gameDAO.getAllGames();
        assertEquals(validNames.size(), allGames.size());

        for (GameDataTX game : allGames) {
            assertTrue(validNames.contains(game.gameName()));
        }
    }

    @Test
    @DisplayName("Bad Get All Games")
    public void badGetAllGames() throws DataAccessException {
        Collection<GameDataTX> allGames = gameDAO.getAllGames();

        Assertions.assertTrue(allGames.isEmpty());
    }

    @Test
    @DisplayName("Good Update Game")
    public void goodUpdateGame() throws DataAccessException, InvalidMoveException {
        int gameID = gameDAO.createGame("name");
        ChessGame movePieceGame = new ChessGame();
        movePieceGame.makeMove(new ChessMove(new ChessPosition(2,1), new ChessPosition(4, 1), null));
        GameData updatedGame = new GameData(gameID, "whiteJoined", "blackJoined", "updatedName", movePieceGame);
        gameDAO.updateGame(updatedGame);

        Assertions.assertEquals("whiteJoined", gameDAO.getGame(gameID).whiteUsername());
        Assertions.assertEquals("blackJoined", gameDAO.getGame(gameID).blackUsername());
        Assertions.assertEquals("updatedName", gameDAO.getGame(gameID).gameName());
        Assertions.assertEquals(movePieceGame, gameDAO.getGame(gameID).game());
    }

    @Test
    @DisplayName("Bad Update Game")
    public void badUpdateGame() throws DataAccessException {
        fail();
    }

    @Test
    @DisplayName("Delete All Games")
    public void deleteAllGames() throws DataAccessException {
        int gameID1 = gameDAO.createGame("name");
        int gameID2 = gameDAO.createGame("name1");
        int gameID3 = gameDAO.createGame("name2");

        gameDAO.deleteAllGames();

        assertNull(gameDAO.getGame(gameID1));
        assertNull(gameDAO.getGame(gameID2));
        assertNull(gameDAO.getGame(gameID3));
    }




    //GameDAO Tests
    @Test
    @DisplayName("Good Create User")
    public void goodCreateUser() throws DataAccessException {
        fail();
    }

    @Test
    @DisplayName("Bad Create User")
    public void badCreateUser() throws DataAccessException {
        fail();
    }

    @Test
    @DisplayName("Good Get User")
    public void goodGetUser() throws DataAccessException {
        fail();
    }

    @Test
    @DisplayName("Bad Get User")
    public void badGetUser() throws DataAccessException {
        fail();
    }

    @Test
    @DisplayName("Delete All Users")
    public void deleteAllUsers() throws DataAccessException {
        fail();
    }
}
