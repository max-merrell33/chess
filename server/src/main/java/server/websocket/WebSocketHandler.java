
package server.websocket;

import com.google.gson.*;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.SQLGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        Gson gson = new Gson();
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();

        UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(json.get("commandType").getAsString());
        switch (type) {
            case CONNECT -> {
                ConnectCommand connectCommand = gson.fromJson(json, ConnectCommand.class);
                connect(connectCommand, session);
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

    private void connect(ConnectCommand c, Session session) throws IOException {
        connections.add(c.getAuthToken(), c.getGameID(), session);
        String message;
        if (c.isObserver()) {
            message = String.format("%s has joined as an observer.", c.getUsername());
        } else if (c.isWhite()) {
            message = String.format("%s has joined as the white player.", c.getUsername());
        } else {
            message = String.format("%s has joined as the black player.", c.getUsername());
        }
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


    private void makeMove(MoveCommand c) throws IOException, DataAccessException {
        var message = String.format("%s made the move %s.", c.getUsername(), c.getMove());

        var notification = new NotificationMessage(message, true, c.getMove());
        connections.broadcast(c.getAuthToken(), c.getGameID(), notification);

    }
}
