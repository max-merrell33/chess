package model.request;

import chess.ChessGame;

public class JoinRequest extends Request {
    String authToken;
    ChessGame.TeamColor playerColor;
    int gameID;
}
