package server;

import dataaccess.*;
import handler.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    protected UserDAO userDAO;
    protected AuthDAO authDAO;
    protected GameDAO gameDAO;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    private void init() {
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();

            userService = new UserService(userDAO, authDAO, gameDAO);
            gameService = new GameService(userDAO, authDAO, gameDAO);
            clearService = new ClearService(userDAO, authDAO, gameDAO);
        } catch (DataAccessException e) {
            System.err.println("Failed to initialize DAOs: " + e.getMessage());
            throw new RuntimeException("Server failed to initialize due to database error.", e);
        }

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        init();

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
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
