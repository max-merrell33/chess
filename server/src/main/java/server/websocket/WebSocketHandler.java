
package server.websocket;

import com.google.gson.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        Gson gson = new Gson();
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();

        UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(json.get("commandType").getAsString());
        switch (type) {
            case CONNECT -> {
                ConnectCommand connectCommand = gson.fromJson(json, ConnectCommand.class);
                connect(connectCommand.getAuthToken(), connectCommand.getUsername(), session, connectCommand.isObserver(), connectCommand.isWhite());
            }
//            case MAKE_MOVE -> makeMove();
            case LEAVE -> {
                LeaveCommand leaveCommand = gson.fromJson(json, LeaveCommand.class);
                leave(leaveCommand.getAuthToken(), leaveCommand.getUsername(), leaveCommand.isObserver());
            }
//            case RESIGN -> resign();
        }
    }

    private void connect(String authToken, String username, Session session, boolean isObserver, boolean isWhite) throws IOException {
        connections.add(authToken, session);
        String message;
        if (isObserver) {
            message = String.format("%s has joined as an observer.", username);
        } else if (isWhite) {
            message = String.format("%s has joined as the white player.", username);
        } else {
            message = String.format("%s has joined as the black player.", username);
        }
        var notification = new NotificationMessage(message);
        connections.broadcast(authToken, notification);
    }

    private void leave(String authToken, String username, boolean isObserver) throws IOException {
        connections.remove(authToken);
        String message;
        if (isObserver) {
            message = String.format("%s has stopped observing.", username);
        } else {
            message = String.format("%s has left the game.", username);
        }
        var notification = new NotificationMessage(message);
        connections.broadcast(authToken, notification);
    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}
