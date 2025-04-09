package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private final String message;
    private final boolean redraw;
    private final String move;

    public NotificationMessage(String message, boolean redraw, String move) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
        this.redraw = redraw;
        this.move = move;
    }

    public String getMessage() {
        return message;
    }

    public boolean redraw() {
        return redraw;
    }

    public String getMove() {
        return move;
    }
}
