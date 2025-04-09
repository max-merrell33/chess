package websocket.commands;

public class ConnectCommand extends UserGameCommand {

    private final boolean isWhite;
    private final boolean isObserver;

    public ConnectCommand(String authToken, Integer gameID, String username, boolean isObserver, boolean isWhite) {
        super(CommandType.CONNECT, authToken, gameID, username);
        this.isObserver = isObserver;
        this.isWhite = isWhite;
    }

    public boolean isObserver() {
        return isObserver;
    }

    public boolean isWhite() {
        return isWhite;
    }

}
