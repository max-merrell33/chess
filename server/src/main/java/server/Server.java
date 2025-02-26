package server;

import dataaccess.*;
import handler.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    protected final UserDAO userDAO = new MemoryUserDAO();
    protected final AuthDAO authDAO = new MemoryAuthDAO();
    protected final GameDAO gameDAO = new MemoryGameDAO();

    private final UserService userService = new UserService(userDAO, authDAO, gameDAO);
    private final GameService gameService = new GameService(userDAO, authDAO, gameDAO);
    private final ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> RegisterHandler.registerHandler(req, res, userService));
        Spark.post("/session", (req, res) -> LoginHandler.loginHandler(req, res, userService));
        Spark.delete("/session", (req, res) -> LogoutHandler.logoutHandler(req, res, userService));

        Spark.get("/game",(req, res) -> ListHandler.listHandler(req, res, gameService));
        Spark.post("/game",(req, res) -> CreateHandler.createHandler(req, res, gameService));
        Spark.put("/game",(req, res) -> JoinHandler.joinHandler(req, res, gameService));

        Spark.delete("/db", (req, res) -> ClearHandler.clearHandler(req, res, clearService));

        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
