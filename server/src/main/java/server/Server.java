package server;

import handler.ClearHandler;
import handler.LoginHandler;
import handler.RegisterHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final ClearService clearService = new ClearService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> RegisterHandler.registerHandler(req, res, userService));
        Spark.post("/session", (req, res) -> LoginHandler.loginHandler(req, res, userService));
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
