
package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.*;
import dataaccess.*;
import exception.ResponseException;
import model.GameData;
import model.request.GetGameRequest;
import model.request.UpdateGameRequest;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ClearService;
import service.GameService;
import service.UserService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    protected UserDAO userDAO;
    protected AuthDAO authDAO;
    protected GameDAO gameDAO;

    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    private void init() {
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();

            userService = new UserService(userDAO, authDAO, gameDAO);
            gameService = new GameService(userDAO, authDAO, gameDAO);
            clearService = new ClearService(userDAO, authDAO, gameDAO);
        } catch (DataAccessException e) {
            System.err.println("Failed to initialize DAOs: " + e.getMessage());
            throw new RuntimeException("Server failed to initialize due to database error.", e);
        }
    }

    private final ConnectionManager connections = new ConnectionManager();
    private Session session;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        init();
        this.session = session;
        Gson gson = new Gson();
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();

        UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(json.get("commandType").getAsString());
        switch (type) {
            case CONNECT -> {
                ConnectCommand connectCommand = gson.fromJson(json, ConnectCommand.class);
                connect(connectCommand);
            }
            case MAKE_MOVE -> {
                MoveCommand moveCommand = gson.fromJson(json, MoveCommand.class);
                makeMove(moveCommand);
            }
            case LEAVE -> {
                LeaveCommand leaveCommand = gson.fromJson(json, LeaveCommand.class);
                leave(leaveCommand);
            }
            case RESIGN -> {
                ResignCommand resignCommand = gson.fromJson(json, ResignCommand.class);
                resign(resignCommand);
            }
        }
    }

    private void connect(ConnectCommand c) throws IOException {
        try {
            if (gameService.getGame(new GetGameRequest(c.getAuthToken(), c.getGameID())).gameData == null) {
                connections.respond(c.getAuthToken(), c.getGameID(), new ErrorMessage("Error: Invalid game ID"), session);
                return;
            }
        } catch (ResponseException e) {
            connections.respond(c.getAuthToken(), c.getGameID(), new ErrorMessage("Error: unauthorized"), session);
            return;
        }

        connections.add(c.getAuthToken(), c.getGameID(), session);


        String message;
        if (c.isObserver()) {
            message = String.format("%s has joined as an observer.", c.getUsername());
        } else if (c.isWhite()) {
            message = String.format("%s has joined as the white player.", c.getUsername());
        } else {
            message = String.format("%s has joined as the black player.", c.getUsername());
        }
        var gameMessage = new LoadGameMessage("gameData");
        connections.respond(c.getAuthToken(), c.getGameID(), gameMessage, session);
        var notification = new NotificationMessage(message, false, null);
        connections.broadcast(c.getAuthToken(), c.getGameID(), notification);
    }

    private void leave(LeaveCommand c) throws IOException {
        connections.remove(c.getAuthToken());
        try {
            GameData gameData = gameService.getGame(new GetGameRequest(c.getAuthToken(), c.getGameID())).gameData;
            GameData updatedGame;
            if (authDAO.getAuth(c.getAuthToken()).username().equals(gameData.whiteUsername())) {
                updatedGame = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else if (authDAO.getAuth(c.getAuthToken()).username().equals(gameData.blackUsername())){
                updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            } else {
                var notification = new NotificationMessage(authDAO.getAuth(c.getAuthToken()).username() + " has stopped observing.", false, null);
                connections.broadcast(c.getAuthToken(), c.getGameID(), notification);
                return;
            }

            gameService.updateGame(new UpdateGameRequest(c.getAuthToken(), updatedGame));

        } catch (ResponseException | DataAccessException e) {
            throw new IOException();
        }
        String message;
        if (c.isObserver()) {
            message = String.format("%s has stopped observing.", c.getUsername());
        } else {
            message = String.format("%s has left the game.", c.getUsername());
        }
        var notification = new NotificationMessage(message, false, null);
        connections.broadcast(c.getAuthToken(), c.getGameID(), notification);
    }


    private void makeMove(MoveCommand c) throws IOException {
        var message = String.format("%s made the move %s.", c.getUsername(), c.getMoveString());

        ChessGame.TeamColor teamColor;
        try {
            GameData gameData = gameService.getGame(new GetGameRequest(c.getAuthToken(), c.getGameID())).gameData;
            if (authDAO.getAuth(c.getAuthToken()).username().equals(gameData.whiteUsername())) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else if (authDAO.getAuth(c.getAuthToken()).username().equals(gameData.blackUsername())) {
                teamColor = ChessGame.TeamColor.BLACK;
            } else {
                connections.respond(c.getAuthToken(), c.getGameID(), new ErrorMessage("Error: observer cannot make moves"), session);
                return;
            }

            if (gameData.game().isGameOver()) {
                connections.respond(c.getAuthToken(), c.getGameID(), new ErrorMessage("Error: the game is over"), session);
                return;
            }
            if (gameData.game().getTeamTurn() != teamColor) {
                connections.respond(c.getAuthToken(), c.getGameID(), new ErrorMessage("Error: not your turn"), session);
                return;
            }
            gameData.game().makeMove(c.getMove());

            gameService.updateGame(new UpdateGameRequest(c.getAuthToken(), gameData));
        } catch (InvalidMoveException | ResponseException | DataAccessException e) {
            connections.respond(c.getAuthToken(), c.getGameID(), new ErrorMessage("Error: invalid move"), session);
            return;
        }
        var loadGameMessage = new LoadGameMessage("gameData");
        connections.respond(c.getAuthToken(), c.getGameID(), loadGameMessage, session);
        connections.broadcast(c.getAuthToken(), c.getGameID(), loadGameMessage);

        var notification = new NotificationMessage(message, true, c.getMoveString());
        connections.broadcast(c.getAuthToken(), c.getGameID(), notification);

    }

    private void resign(ResignCommand c) throws IOException {
        try {
            GameData gameData = gameService.getGame(new GetGameRequest(c.getAuthToken(), c.getGameID())).gameData;
            if (gameData.game().isGameOver()) {
                var errorMessage = new ErrorMessage("Error: the game is already over.");
                connections.respond(c.getAuthToken(), c.getGameID(), errorMessage, session);
                return;
            }
            gameData.game().setGameOver(true);
            gameService.updateGame(new UpdateGameRequest(c.getAuthToken(), gameData));

            if (authDAO.getAuth(c.getAuthToken()).username().equals(gameData.blackUsername()) || authDAO.getAuth(c.getAuthToken()).username().equals(gameData.whiteUsername())) {
                var notification = new NotificationMessage(authDAO.getAuth(c.getAuthToken()).username() + " resigned. The game is over", false, null);
                connections.respond(c.getAuthToken(), c.getGameID(), notification, session);
                connections.broadcast(c.getAuthToken(), c.getGameID(), notification);
            } else {
                var errorMessage = new ErrorMessage("Error: you are observing, you cannot resign.");
                connections.respond(c.getAuthToken(), c.getGameID(), errorMessage, session);
            }


        } catch (ResponseException | DataAccessException e) {
            throw new IOException();
        }
    }

}


