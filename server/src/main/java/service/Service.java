package service;

import dataaccess.*;
import model.AuthData;

public class Service {
    protected final UserDAO userDAO;
    protected final AuthDAO authDAO;
    protected final GameDAO gameDAO;

    public Service(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }
}
