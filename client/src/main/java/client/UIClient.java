package client;

import server.ServerFacade;

public abstract class UIClient {
    protected final ServerFacade server;
    protected String serverUrl;
    protected String authToken;
    protected String username;
    protected int gameID;
    protected boolean playerIsWhite;

    public UIClient(String serverUrl, String authToken, String username, int gameID, boolean playerIsWhite) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
        this.playerIsWhite = playerIsWhite;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public boolean playerIsWhite() {
        return playerIsWhite;
    }

    public int getGameID() {
        return gameID;
    }

    public abstract String eval(String input);
    public abstract String help();

}
