package client;

import server.ServerFacade;

public abstract class UIClient {
    protected final ServerFacade server;
    protected String serverUrl;
    protected String authToken;
    protected String username;
    protected int gameID;
    protected boolean playerIsWhite;
    protected boolean playerIsObserver;


    public UIClient(String serverUrl, String authToken, String username, int gameID, boolean playerIsWhite, boolean playerIsObserver) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
        this.playerIsWhite = playerIsWhite;
        this.playerIsObserver = playerIsObserver;
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

    public boolean playerIsObserver() { return playerIsObserver; }

    public int getGameID() {
        return gameID;
    }

    public abstract String eval(String input);
    public abstract String help();

}
