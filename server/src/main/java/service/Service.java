package service;

import dataaccess.*;
import model.AuthData;

public class Service {
    protected final UserDAO userDAO = new MemoryUserDAO();
    protected final AuthDAO authDAO = new MemoryAuthDAO();
    protected final GameDAO gameDAO = new MemoryGameDAO();

}
