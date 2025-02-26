package handler;

import com.google.gson.Gson;
import model.request.LogoutRequest;
import model.result.LogoutResult;
import server.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    public static Object logoutHandler(Request req, Response res, UserService userService) throws ResponseException {
        System.out.println("body: " + req.body() + " headers: " + req.headers() + " params: " + req.params() + " map: " + req.queryMap());

        LogoutRequest logoutReq = new LogoutRequest(req.headers("Authorization"));
        LogoutResult logoutRes = userService.logout(logoutReq);
        return new Gson().toJson(logoutRes);
    }
}
