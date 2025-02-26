package service;

import dataaccess.*;
import model.result.ClearResult;

public class ClearService extends Service {

    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public ClearResult clear() throws DataAccessException {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuths();
        gameDAO.deleteAllGames();

        return new ClearResult();
    }
}
