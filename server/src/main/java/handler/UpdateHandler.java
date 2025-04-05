package handler;

import com.google.gson.Gson;
import exception.ResponseException;
import model.request.UpdateGameRequest;
import model.result.UpdateGameResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class UpdateHandler {
    public static Object updateHandler(Request req, Response res, GameService gameService) throws ResponseException {
        UpdateGameRequest updateReq = new Gson().fromJson(req.body(), UpdateGameRequest.class);
        updateReq.setAuthToken(req.headers("Authorization"));
        UpdateGameResult updateRes = gameService.updateGame(updateReq);
        return new Gson().toJson(updateRes);

    }
}
