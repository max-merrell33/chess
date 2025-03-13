package handler;

import com.google.gson.Gson;
import model.request.CreateRequest;
import model.result.CreateResult;
import exception.ResponseException;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateHandler {
    public static Object createHandler(Request req, Response res, GameService gameService) throws ResponseException {
        CreateRequest createReq = new Gson().fromJson(req.body(), CreateRequest.class);
        createReq.setAuthToken(req.headers("Authorization"));
        CreateResult createRes = gameService.createGame(createReq);
        return new Gson().toJson(createRes);

    }
}
