package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.request.*;
import model.result.*;
import exception.ResponseException;

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
                throw new ResponseException(400, "bad request, game name cannot be blank");
            }
            int newGameID = gameDAO.createGame(req.gameName);

            return new CreateResult(newGameID);
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public GetGameResult getGame(GetGameRequest req) throws ResponseException {
        try {
            if (authDAO.getAuth(req.authToken) == null) {
                throw new ResponseException(401, "unauthorized");
            }
            if (req.gameID == 0) {
                throw new ResponseException(400, "bad request, game ID cannot be blank");
            }
            GameData game = gameDAO.getGame(req.gameID);

            return new GetGameResult(game);
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public UpdateGameResult updateGame(UpdateGameRequest req) throws ResponseException {
        try {
            if (authDAO.getAuth(req.authToken) == null) {
                throw new ResponseException(401, "unauthorized");
            }
            if (req.gameData.gameID() == 0) {
                throw new ResponseException(400, "bad request");
            }
            GameData game = gameDAO.getGame(req.gameData.gameID());

            gameDAO.updateGame(new GameData(game.gameID(), req.gameData.whiteUsername(),
                    req.gameData.blackUsername(), game.gameName(), req.gameData.game()));

            return new UpdateGameResult();
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
            GameData updatedGame;

            if (oldGame == null) {
                throw new ResponseException(400, "bad request");
            }

            if (isWhite) {
                if (oldGame.whiteUsername() != null) {
                    throw new ResponseException(403, "already taken");
                }
                updatedGame = new GameData(oldGame.gameID(), authData.username(), oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
            } else {
                if (oldGame.blackUsername() != null) {
                    throw new ResponseException(403, "already taken");
                }
                updatedGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), authData.username(), oldGame.gameName(), oldGame.game());
            }

            gameDAO.updateGame(updatedGame);

            return new JoinResult();

        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
