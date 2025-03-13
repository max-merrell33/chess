package handler;

import com.google.gson.Gson;
import model.request.LoginRequest;
import model.result.LoginResult;
import exception.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    public static Object loginHandler(Request req, Response res, UserService userService) throws ResponseException {
        LoginRequest loginReq = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult loginRes = userService.login(loginReq);
        return new Gson().toJson(loginRes);
    }
}
