package service;

import dataaccess.*;
import model.GameDataTX;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.*;
import server.ResponseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceUnitTests {

    private static UserService userService;
    private static GameService gameService;
    private static ClearService clearService;

    RegisterRequest testUserReg = new RegisterRequest("username", "password", "email@gmail.com");
    String testAuthToken;

    @BeforeAll
    public static void init() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO, gameDAO);
        gameService = new GameService(userDAO, authDAO, gameDAO);
        clearService = new ClearService(userDAO, authDAO, gameDAO);
    }

    @BeforeEach
    public void beforeEach() throws ResponseException {
        clearService.clear();

        //Register one user and save the auth token
        RegisterResult res = userService.register(testUserReg);
        testAuthToken = res.authToken;

    }

    @Test
    @DisplayName("Good Clear")
    public void goodClear() {

    }

    @Test
    @DisplayName("Good Register")
    public void goodRegister() throws ResponseException {
        RegisterRequest req = new RegisterRequest("test", "password", "email@gmail.com");
        RegisterResult res = userService.register(req);

        Assertions.assertNotNull(res, "Register result is null");
        Assertions.assertEquals(req.username, res.username, "Register result username is not equal to the username in the request");
        Assertions.assertNotNull(res.authToken, "Register result authToken is null");
        Assertions.assertFalse(res.authToken.isBlank(), "Register result authToken is empty");

    }

    @Test
    @DisplayName("Register with Blank Username")
    public void badRegisterBlankUsername() {
        RegisterRequest req = new RegisterRequest("", "password", "email@gmail.com");

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userService.register(req);
        });
        Assertions.assertEquals(400, exception.statusCode());

    }

    @Test
    @DisplayName("Register with Blank Username")
    public void badRegisterBlankPassword() {
        RegisterRequest req = new RegisterRequest("username", "", "email@gmail.com");

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userService.register(req);
        });
        Assertions.assertEquals(400, exception.statusCode());

    }

    @Test
    @DisplayName("Register with Blank Email")
    public void badRegisterBlankEmail() {
        RegisterRequest req = new RegisterRequest("username", "password", "");

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userService.register(req);
        });
        Assertions.assertEquals(400, exception.statusCode());

    }

    @Test
    @DisplayName("Register Same User Twice")
    public void badRegisterTwice() {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userService.register(testUserReg);
        });
        Assertions.assertEquals(403, exception.statusCode());

    }

    @Test
    @DisplayName("Good Login")
    public void goodLogin() throws ResponseException {
        userService.logout(new LogoutRequest(testAuthToken));

        LoginRequest req = new LoginRequest("username", "password");
        LoginResult res = userService.login(req);

        Assertions.assertNotNull(res, "Login result is null");
        Assertions.assertEquals(res.username, req.username, "Login result username is not equal to the username in the request");
        Assertions.assertNotNull(res.authToken, "Login result authToken is null");
        Assertions.assertFalse(res.authToken.isBlank(), "Register result authToken is empty");

    }

    @Test
    @DisplayName("Bad Login wrong credentials")
    public void badLogin() {
        LoginRequest req = new LoginRequest("username", "passw0rd");
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userService.login(req);
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Good Logout")
    public void goodLogout() throws ResponseException {
        LogoutResult res = userService.logout(new LogoutRequest(testAuthToken));

        // Assert that after logging out, a request with the old token returns 401 - Unauthorized
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            gameService.listGames(new ListRequest(testAuthToken));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Bad Logout")
    public void badLogout() {
        // Assert that a logout request with an invalid token will return 401 - Unauthorized
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userService.logout(new LogoutRequest("notARealToken"));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Good List Games")
    public void goodListGames() throws ResponseException {
        gameService.createGame(new CreateRequest(testAuthToken, "Game1"));
        gameService.createGame(new CreateRequest(testAuthToken, "Game2"));

        ListResult res = gameService.listGames(new ListRequest(testAuthToken));

        Assertions.assertEquals(2, res.games.size(), "List games does not return the correct amount of games");

        List<GameDataTX> gamesList = new ArrayList<>(res.games);
        List<String> gameNames = List.of(gamesList.get(0).gameName(), gamesList.get(1).gameName());

        Assertions.assertTrue(gameNames.contains("Game1"));
        Assertions.assertTrue(gameNames.contains("Game2"));
    }

    @Test
    @DisplayName("Bad List Games Bad Auth")
    public void badListGames() {
        // Assert that a call with invalid Auth will throw 401 - Unauthorized
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            gameService.listGames(new ListRequest("notARealToken"));
        });
        Assertions.assertEquals(401, exception.statusCode());

    }

    @Test
    @DisplayName("Good Create Game")
    public void goodCreateGame() throws ResponseException {
        CreateResult res = gameService.createGame(new CreateRequest(testAuthToken, "Game1"));

        Assertions.assertNotNull(res, "Create result is null");
        Assertions.assertNotEquals(0, res.gameID, "Create result gameID is 0");

        Collection<GameDataTX> gameList = gameService.listGames(new ListRequest(testAuthToken)).games;

        Assertions.assertEquals(1, gameList.size(), "Game was not created successfully");
        Assertions.assertEquals("Game1", gameList.iterator().next().gameName(), "Game was created but with an incorrect name");

    }

    @Test
    @DisplayName("Bad Create Game Bad Auth")
    public void badCreateGameBadAuth() {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            gameService.createGame(new CreateRequest("notARealToken", "Game1"));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Bad Create Game Blank Name")
    public void badCreateGameBlankName() {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            gameService.createGame(new CreateRequest(testAuthToken, ""));
        });
        Assertions.assertEquals(400, exception.statusCode());

    }

    @Test
    @DisplayName("Good Join Game White")
    public void goodJoinGameWhite() throws ResponseException {
        int gameID = gameService.createGame(new CreateRequest(testAuthToken, "Game1")).gameID;

        gameService.joinGame(new JoinRequest(testAuthToken, "WHITE", gameID));

        Collection<GameDataTX> gameData = gameService.listGames(new ListRequest(testAuthToken)).games;
        GameDataTX gameDataObj = gameData.iterator().next();

        Assertions.assertEquals(gameID, gameDataObj.gameID());
        Assertions.assertEquals("username", gameDataObj.whiteUsername(), "White username was not updated");

    }

    @Test
    @DisplayName("Good Join Game Black")
    public void goodJoinGameBlack() throws ResponseException {
        int gameID = gameService.createGame(new CreateRequest(testAuthToken, "Game1")).gameID;

        gameService.joinGame(new JoinRequest(testAuthToken, "BLACK", gameID));

        Collection<GameDataTX> gameData = gameService.listGames(new ListRequest(testAuthToken)).games;
        GameDataTX gameDataObj = gameData.iterator().next();

        Assertions.assertEquals(gameID, gameDataObj.gameID());
        Assertions.assertEquals("username", gameDataObj.blackUsername(), "Black username was not updated");

    }

    @Test
    @DisplayName("Bad Join Game Bad Auth")
    public void badJoinGameBadAuth() throws ResponseException {
        int gameID = gameService.createGame(new CreateRequest(testAuthToken, "Game1")).gameID;

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            gameService.joinGame(new JoinRequest("notARealToken", "BLACK", gameID));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Bad Join Game Bad Color")
    public void badJoinGameBadColor() throws ResponseException {
        int gameID = gameService.createGame(new CreateRequest(testAuthToken, "Game1")).gameID;

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            gameService.joinGame(new JoinRequest(testAuthToken, "NOTACOLOR", gameID));
        });
        Assertions.assertEquals(400, exception.statusCode());
    }

    @Test
    @DisplayName("Bad Join Game Empty ID")
    public void badJoinGameEmptyID() throws ResponseException {
        gameService.createGame(new CreateRequest(testAuthToken, "Game1"));

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            gameService.joinGame(new JoinRequest(testAuthToken, "BLACK", 0));
        });
        Assertions.assertEquals(400, exception.statusCode());
    }

}
