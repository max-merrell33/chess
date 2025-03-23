package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.request.GetGameRequest;
import model.request.ListRequest;
import model.result.GetGameResult;
import model.result.ListResult;
import exception.ResponseException;
import org.eclipse.jetty.http.HttpParser;
import service.GameService;
import spark.Request;
import spark.Response;

public class GetGameHandler {
    public static Object getGameHandler(Request req, Response res, GameService gameService) throws ResponseException {
        GetGameRequest getGameReq = new GetGameRequest(req.headers("Authorization"), Integer.parseInt(req.params(":gameID")));
        GetGameResult getGameRes = gameService.getGame(getGameReq);
        return new Gson().toJson(getGameRes);

    }
}
