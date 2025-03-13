package service;

import dataaccess.*;
import model.result.ClearResult;
import exception.ResponseException;

public class ClearService extends Service {

    public ClearService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public ClearResult clear() throws ResponseException {
        try {
            userDAO.deleteAllUsers();
            authDAO.deleteAllAuths();
            gameDAO.deleteAllGames();

            return new ClearResult();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
