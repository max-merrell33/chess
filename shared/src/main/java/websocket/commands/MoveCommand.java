package websocket.commands;


public class MoveCommand extends UserGameCommand {
    private final String move;

    public MoveCommand(String authToken, int gameID, String username, String move) {
        super(CommandType.MAKE_MOVE, authToken, gameID, username);
        this.move = move;
    }

    public String getMove() { return move; }
}
