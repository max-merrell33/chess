package client;

import server.ServerFacade;

public abstract class UIClient {
    protected final ServerFacade server;
    protected String serverUrl;
    protected String authToken;
    protected String username;
    protected int gameID;

    public UIClient(String serverUrl, String authToken, String username, int gameID) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }

    public abstract String eval(String input);
    public abstract String help();

}
