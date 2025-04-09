package websocket.commands;


import chess.ChessMove;

public class MoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final String moveString;

    public MoveCommand(String authToken, int gameID, String username, ChessMove move, String moveString) {
        super(CommandType.MAKE_MOVE, authToken, gameID, username);
        this.move = move;
        this.moveString = moveString;
    }

    public ChessMove getMove() { return move; }

    public String getMoveString() { return moveString; }
}
