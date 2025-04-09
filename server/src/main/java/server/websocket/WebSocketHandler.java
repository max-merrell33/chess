
package server.websocket;

import com.google.gson.*;
import dataaccess.*;
import exception.ResponseException;
import model.request.GetGameRequest;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import service.ClearService;
import service.GameService;
import service.UserService;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
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
//            case RESIGN -> resign();
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

        var loadGameMessage = new LoadGameMessage("gameData");
        connections.respond(c.getAuthToken(), c.getGameID(), loadGameMessage, session);
        connections.broadcast(c.getAuthToken(), c.getGameID(), loadGameMessage);

        var notification = new NotificationMessage(message, true, c.getMoveString());
        connections.broadcast(c.getAuthToken(), c.getGameID(), notification);

    }
}
