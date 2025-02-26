package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;
import server.ResponseException;

import java.util.UUID;

public class UserService {
    // register, login, logout
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        try {
            RegisterResult res = new RegisterResult();

            if (req.username == null || req.password == null || req.email == null) {
                throw new ResponseException(400, "bad request");
            }

            if (userDAO.getUser(req.username) != null) {
                throw new ResponseException(403, "already taken");
            }

            userDAO.createUser(new UserData(req.username, req.password, req.email));
            AuthData newAuthData = authDAO.createAuth(UUID.randomUUID().toString(), req.username);

            res.authToken = newAuthData.authToken();
            res.username = req.username;

            return res;
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        try {
            LoginResult res = new LoginResult();

            UserData user = userDAO.getUser(req.username);
            if (user != null && user.password().equals(req.password)) {
                AuthData newAuthData = authDAO.createAuth(UUID.randomUUID().toString(), req.username);

                res.authToken = newAuthData.authToken();
                res.username = req.username;
            } else {
                throw new ResponseException(401, "unauthorized");
            }

            return res;
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
