package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.LogoutResult;
import model.result.RegisterResult;
import server.ResponseException;

import java.util.UUID;

public class UserService extends Service {
    // register, login, logout
    public UserService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        try {
            RegisterResult res = new RegisterResult();

            if (req.username == null || req.password == null || req.email == null) {
                throw new ResponseException(400, "bad request");
            }

            if (req.username.isBlank() || req.password.isBlank() || req.email.isBlank()) {
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

            if (user == null || !user.password().equals(req.password)) {
                throw new ResponseException(401, "unauthorized");
            }

            AuthData newAuthData = authDAO.createAuth(UUID.randomUUID().toString(), req.username);

            res.authToken = newAuthData.authToken();
            res.username = req.username;

            return res;
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public LogoutResult logout(LogoutRequest req) throws ResponseException {
        try {
            if (authDAO.getAuth(req.authToken) == null) {
                throw new ResponseException(401, "unauthorized");
            }

            authDAO.deleteAuth(req.authToken);
            return new LogoutResult();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}








