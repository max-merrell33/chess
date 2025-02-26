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
        LogoutRequest logoutReq = new LogoutRequest(req.headers("authorization"));
        LogoutResult logoutRes = userService.logout(logoutReq);
        return new Gson().toJson(logoutRes);
    }
}
