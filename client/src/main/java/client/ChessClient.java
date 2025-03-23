package client;

import chess.ChessBoard;
import exception.ResponseException;
import model.GameData;
import model.request.GetGameRequest;
import model.result.GetGameResult;

import java.util.Arrays;

public class ChessClient extends UIClient {
    public ChessClient(String serverUrl, String authToken, String username, int gameID) {
        super(serverUrl, authToken, username, gameID);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "print" -> print(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Invalid Input. " + help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String print(String... params) throws ResponseException {
        if (params.length == 0) {
            GetGameResult res = server.getGame(new GetGameRequest(authToken, gameID));
            return printBoard(res.gameData.game().getBoard());
        }
        throw new ResponseException(400, "Unexpected characters after command");
    }

    public String printBoard(ChessBoard board) {
        return board.toString();
    }

    public String help() {
        return """
                Possible commands:
                - print - print the board
                - quit - exit the game, back to menu
                - help - display possible commands
                """;
    }
}
