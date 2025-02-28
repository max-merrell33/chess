package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.request.CreateRequest;
import model.request.JoinRequest;
import model.request.ListRequest;
import model.result.CreateResult;
import model.result.JoinResult;
import model.result.ListResult;
import server.ResponseException;

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
            if (req.gameName == null || req.gameName.isBlank()) {
                throw new ResponseException(400, "bad request");
            }
            int newGameID = gameDAO.createGame(req.gameName);

            return new CreateResult(newGameID);
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public JoinResult joinGame(JoinRequest req) throws ResponseException {
        try {
            AuthData authData = authDAO.getAuth(req.authToken);
            if (authData == null) {
                throw new ResponseException(401, "unauthorized");
            }
            if (req.playerColor == null || req.playerColor.isBlank()) {
                throw new ResponseException(400, "bad request");
            }
            if (!req.playerColor.equals("WHITE") && !req.playerColor.equals("BLACK")) {
                throw new ResponseException(400, "bad request");
            }

            boolean isWhite = req.playerColor.equals("WHITE");
            GameData oldGame = gameDAO.getGame(req.gameID);

            if (oldGame == null) {
                throw new ResponseException(400, "bad request");
            }

            if (isWhite) {
                if (oldGame.whiteUsername() != null) {
                    throw new ResponseException(403, "already taken");
                }
            } else {
                if (oldGame.blackUsername() != null) {
                    throw new ResponseException(403, "already taken");
                }
            }

            gameDAO.updateGame(req.gameID, authData.username(), isWhite);

            return new JoinResult();

        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
