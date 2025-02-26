package handler;

import com.google.gson.GsonBuilder;
import model.request.ListRequest;
import model.result.ListResult;
import server.ResponseException;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListHandler {
    public static Object listHandler(Request req, Response res, GameService gameService) throws ResponseException {
        ListRequest listReq = new ListRequest(req.headers("Authorization"));
        ListResult listRes = gameService.listGames(listReq);
        return new GsonBuilder()
                .serializeNulls() // Ensures null values are included
                .create()
                .toJson(listRes);
    }
}
