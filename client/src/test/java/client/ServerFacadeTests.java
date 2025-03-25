package client;

import exception.ResponseException;
import model.request.*;
import model.result.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void beforeEach() throws ResponseException {
        facade.clear();
    }

    @Test
    @DisplayName("Good Register")
    public void goodRegister() throws ResponseException {
        facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));

        LoginResult res = facade.loginUser(new LoginRequest("username", "password"));
        Assertions.assertEquals("username", res.username);
    }

    @Test
    @DisplayName("Bad Register")
    public void badRegister() throws ResponseException {
        facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));
        });
        Assertions.assertEquals(403, exception.statusCode());
    }

    @Test
    @DisplayName("Good Login")
    public void goodLogin() throws ResponseException {
        facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult res = facade.loginUser(new LoginRequest("username", "password"));

        Assertions.assertEquals("username", res.username);
    }

    @Test
    @DisplayName("Bad Login")
    public void badLogin() throws ResponseException {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.loginUser(new LoginRequest("username", "password"));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Good Logout")
    public void goodLogout() throws ResponseException {
        RegisterResult res = facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));
        facade.logoutUser(new LogoutRequest(res.authToken));

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.listGames(new ListRequest(res.authToken));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Bad Logout")
    public void badLogout() throws ResponseException {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.logoutUser(new LogoutRequest("not_an_auth_token"));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Good List")
    public void goodList() throws ResponseException {
        RegisterResult regRes = facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));
        facade.createGame(new CreateRequest(regRes.authToken, "game1"));
        facade.createGame(new CreateRequest(regRes.authToken, "game2"));

        ListResult listRes = facade.listGames(new ListRequest(regRes.authToken));

        Assertions.assertEquals(2, listRes.games.size());
    }

    @Test
    @DisplayName("Bad List")
    public void badList() throws ResponseException {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.listGames(new ListRequest("not_an_auth_token"));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Good Join")
    public void goodJoin() throws ResponseException {
        RegisterResult regRes = facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));
        CreateResult createRes = facade.createGame(new CreateRequest(regRes.authToken, "game1"));

        facade.joinGame(new JoinRequest(regRes.authToken, "WHITE", createRes.gameID));

        GetGameResult gameRes = facade.getGame(new GetGameRequest(regRes.authToken, createRes.gameID));

        Assertions.assertEquals("username", gameRes.gameData.whiteUsername());
    }

    @Test
    @DisplayName("Bad Join")
    public void badJoin() throws ResponseException {
        RegisterResult regRes1 = facade.registerUser(new RegisterRequest("username1", "password", "email@email.com"));
        RegisterResult regRes2 = facade.registerUser(new RegisterRequest("username2", "password", "email@email.com"));

        CreateResult createRes = facade.createGame(new CreateRequest(regRes1.authToken, "game1"));

        facade.joinGame(new JoinRequest(regRes1.authToken, "WHITE", createRes.gameID));

        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.joinGame(new JoinRequest(regRes2.authToken, "WHITE", createRes.gameID));
        });
        Assertions.assertEquals(403, exception.statusCode());

    }

    @Test
    @DisplayName("Good Get Game")
    public void goodGetGame() throws ResponseException {
        RegisterResult res = facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));

        CreateResult createRes = facade.createGame(new CreateRequest(res.authToken, "game1"));

        GetGameResult gameRes = facade.getGame(new GetGameRequest(res.authToken, createRes.gameID));

        Assertions.assertEquals("game1", gameRes.gameData.gameName());
    }

    @Test
    @DisplayName("Bad Get Game")
    public void badGetGame() throws ResponseException {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.getGame(new GetGameRequest("not_an_auth_token", 1));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

    @Test
    @DisplayName("Good Create")
    public void goodCreate() throws ResponseException {
        RegisterResult res = facade.registerUser(new RegisterRequest("username", "password", "email@email.com"));

        CreateResult createRes = facade.createGame(new CreateRequest(res.authToken, "game1"));

        GetGameResult gameRes = facade.getGame(new GetGameRequest(res.authToken, createRes.gameID));

        Assertions.assertEquals("game1", gameRes.gameData.gameName());
    }

    @Test
    @DisplayName("Bad Create")
    public void badCreate() throws ResponseException {
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            facade.createGame(new CreateRequest("not_an_auth_token", "aGameName"));
        });
        Assertions.assertEquals(401, exception.statusCode());
    }

}
