package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    String chessGame;

    public LoadGameMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.chessGame = game;
    }

    public String getGame() {
        return chessGame;
    }
}
