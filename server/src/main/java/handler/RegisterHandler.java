package handler;

import com.google.gson.Gson;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import server.ResponseException;
import service.UserService;
import spark.*;

public class RegisterHandler {

    public static Object registerHandler(Request req, Response res, UserService userService) throws ResponseException {
        RegisterRequest regReq = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult regRes = userService.register(regReq);
        return new Gson().toJson(regRes);

    }
}
