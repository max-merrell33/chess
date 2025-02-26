package model.request;

import chess.ChessGame;

public class JoinRequest extends Request {
    public String authToken;
    public ChessGame.TeamColor playerColor;
    public int gameID;
}
