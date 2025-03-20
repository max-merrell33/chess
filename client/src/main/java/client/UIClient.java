package client;

import server.ServerFacade;

public abstract class UIClient {
    protected final ServerFacade server;
    protected String serverUrl;

    public UIClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public abstract String eval(String input);
    public abstract String help();

}
