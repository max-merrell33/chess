package websocket.commands;

import static websocket.commands.UserGameCommand.CommandType.LEAVE;

public class LeaveCommand extends UserGameCommand {
    private final boolean isObserver;

    public LeaveCommand(String authToken, int gameID, String username, boolean isObserver) {
        super(LEAVE, authToken, gameID, username);
        this.isObserver = isObserver;
    }

    public boolean isObserver() { return isObserver; }
}
