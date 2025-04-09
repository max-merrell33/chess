package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(String authToken, int gameID, Session session) {
        var connection = new Connection(authToken, session);
        connections.putIfAbsent(gameID, new ConcurrentHashMap<>());
        connections.get(gameID).put(authToken, connection);
    }

    public void remove(String authToken) {
        for (var gameMap : connections.values()) {
            gameMap.remove(authToken);
        }
    }

    public void broadcast(String excludeAuthToken, int gameID, ServerMessage serverMessage) throws IOException {
        var gameConnections = connections.get(gameID);
        if (gameConnections == null) return;

        var removeList = new ArrayList<String>();

        for (var entry : gameConnections.entrySet()) {
            String token = entry.getKey();
            Connection c = entry.getValue();
            if (c.session.isOpen()) {
                if (!token.equals(excludeAuthToken)) {
                    c.send(serverMessage.toString());
                }
            } else {
                removeList.add(token);
            }
        }

        for (String token : removeList) {
            gameConnections.remove(token);
        }

        if (gameConnections.isEmpty()) {
            connections.remove(gameID);
        }
    }
}