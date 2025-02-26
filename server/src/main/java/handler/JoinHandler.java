package handler;

import com.google.gson.Gson;
import model.request.JoinRequest;
import model.result.JoinResult;
import server.ResponseException;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinHandler {
    public static Object joinHandler(Request req, Response res, GameService gameService) throws ResponseException {
        JoinRequest joinReq = new Gson().fromJson(req.body(), JoinRequest.class);
        joinReq.setAuthToken(req.headers("Authorization"));
        JoinResult joinRes = gameService.joinGame(joinReq);
        return new Gson().toJson(joinRes);

    }
}
