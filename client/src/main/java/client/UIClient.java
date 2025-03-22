package client;

import server.ServerFacade;

public abstract class UIClient {
    protected final ServerFacade server;
    protected String serverUrl;
    protected String authToken;
    protected String username;

    public UIClient(String serverUrl, String authToken, String username) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public abstract String eval(String input);
    public abstract String help();

}
