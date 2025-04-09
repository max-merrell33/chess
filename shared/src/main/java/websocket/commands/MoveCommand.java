package websocket.commands;


import chess.ChessMove;

public class MoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final String moveString;
    private final boolean isObserver;
    private final boolean isWhite;

    public MoveCommand(String authToken, int gameID, String username, ChessMove move, String moveString, boolean isObserver, boolean isWhite) {
        super(CommandType.MAKE_MOVE, authToken, gameID, username);
        this.move = move;
        this.moveString = moveString;
        this.isObserver = isObserver;
        this.isWhite = isWhite;
    }

    public ChessMove getMove() { return move; }

    public String getMoveString() { return moveString; }

    public boolean isObserver() { return isObserver; }

    public boolean isWhite() { return isWhite; }
}
