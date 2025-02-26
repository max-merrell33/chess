package service;

import dataaccess.*;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.ClearResult;
import model.result.LoginResult;
import model.result.RegisterResult;

import java.util.UUID;

public class UserService {
    // register, login, logout
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        RegisterResult res = new RegisterResult();
        if (userDAO.getUser(req.username) != null) {
            // throw an error idk
        }

        userDAO.createUser(new UserData(req.username, req.password, req.email));
        var newAuthData = authDAO.createAuth(UUID.randomUUID().toString(), req.username);

        res.authToken = newAuthData.authToken();
        res.username = req.username;

        return res;
    }

    public LoginResult login(LoginRequest req) throws DataAccessException {

    }

}
