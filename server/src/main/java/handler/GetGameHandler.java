package handler;

import com.google.gson.GsonBuilder;
import model.request.GetGameRequest;
import model.result.GetGameResult;
import exception.ResponseException;
import service.GameService;
import spark.Request;
import spark.Response;

public class GetGameHandler {
    public static Object getGameHandler(Request req, Response res, GameService gameService) throws ResponseException {
        GetGameRequest getGameReq = new GetGameRequest(req.headers("Authorization"), Integer.parseInt(req.params(":gameID")));
        GetGameResult getGameRes = gameService.getGame(getGameReq);
        return new GsonBuilder()
                .serializeNulls() // Ensures null values are included
                .create()
                .toJson(getGameRes);

    }
}
