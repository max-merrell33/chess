package websocket.commands;

import static websocket.commands.UserGameCommand.CommandType.RESIGN;

public class ResignCommand extends UserGameCommand {

    public ResignCommand(String authToken, int gameID, String username) {
        super(RESIGN, authToken, gameID, username);
    }

}
