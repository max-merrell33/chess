package service;

import dataaccess.*;
import model.result.ClearResult;

public class ClearService {

    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    public ClearResult clear() throws DataAccessException {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuths();
        gameDAO.deleteAllGames();

        return new ClearResult();
    }
}
