package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import model.request.CreateRequest;
import model.request.ListRequest;
import model.result.CreateResult;
import model.result.ListResult;
import server.ResponseException;

import java.util.Collection;

public class GameService extends Service {
    // list, create, join
    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        super(userDAO, authDAO, gameDAO);
    }

    public ListResult listGames(ListRequest req) throws ResponseException {
        try {
            if (authDAO.getAuth(req.authToken) == null) {
                throw new ResponseException(401, "unauthorized");
            }

            return new ListResult(gameDAO.getAllGames());
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public CreateResult createGame(CreateRequest req) throws ResponseException {
        try {
            if (authDAO.getAuth(req.authToken) == null) {
                throw new ResponseException(401, "unauthorized");
            }
            if (req.gameName == null) {
                throw new ResponseException(400, "bad request");
            }
            int newGameID = gameDAO.createGame(req.gameName);

            return new CreateResult(newGameID);
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
