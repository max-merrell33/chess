package client;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import exception.ResponseException;
import model.GameData;
import model.request.GetGameRequest;
import model.request.UpdateGameRequest;
import model.result.GetGameResult;
import ui.EscapeSequences;

import java.util.Arrays;

public class ChessClient extends UIClient {
    public ChessClient(String serverUrl, String authToken, String username, int gameID, boolean playerIsWhite) {
        super(serverUrl, authToken, username, gameID, playerIsWhite);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> print(params);
                case "move" -> move(params);
                case "leave" -> leave(params);
                case "resign" -> resign(params);
                case "highlight" -> highlight(params);
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
            return printBoard(res.gameData.game().getBoard(), playerIsWhite);
        }
        throw new ResponseException(400, "Unexpected characters after command");
    }

    public String printBoard(ChessBoard board, boolean playerIsWhite) {
        String[] rows = board.toString().split("\n");
        StringBuilder output = new StringBuilder();

        output.append(EscapeSequences.ERASE_SCREEN);  // Clear screen before drawing
        output.append(EscapeSequences.SET_TEXT_BOLD);

        boolean whiteSquare = true;

        String cols = playerIsWhite ? "    a  b  c  d  e  f  g  h    " : "    h  g  f  e  d  c  b  a    ";
        output.append(EscapeSequences.SET_BG_COLOR_BLACK).append(cols);
        output.append(EscapeSequences.RESET_BG_COLOR).append("\n");

        int startRow = playerIsWhite ? 0 : 7;
        int endRow = playerIsWhite ? 8 : -1;
        int stepRow = playerIsWhite ? 1 : -1;

        for (int i = startRow; i != endRow; i += stepRow) {
            String row = rows[i];

            output.append(EscapeSequences.SET_BG_COLOR_BLACK).append(" ").append(8-i).append(" ");

            int startCol = playerIsWhite ? 0 : 7;
            int endCol = playerIsWhite ? 8 : -1;
            int stepCol = playerIsWhite ? 1 : -1;

            for (int j = startCol; j != endCol; j += stepCol) {
                char piece = row.charAt(j);

                String bgColor = whiteSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
                whiteSquare = !whiteSquare;

                String pieceSymbol = switch (piece) {
                    case 'r' -> EscapeSequences.BLACK_ROOK;
                    case 'n' -> EscapeSequences.BLACK_KNIGHT;
                    case 'b' -> EscapeSequences.BLACK_BISHOP;
                    case 'q' -> EscapeSequences.BLACK_QUEEN;
                    case 'k' -> EscapeSequences.BLACK_KING;
                    case 'p' -> EscapeSequences.BLACK_PAWN;
                    case 'R' -> EscapeSequences.WHITE_ROOK;
                    case 'N' -> EscapeSequences.WHITE_KNIGHT;
                    case 'B' -> EscapeSequences.WHITE_BISHOP;
                    case 'Q' -> EscapeSequences.WHITE_QUEEN;
                    case 'K' -> EscapeSequences.WHITE_KING;
                    case 'P' -> EscapeSequences.WHITE_PAWN;
                    default -> EscapeSequences.EMPTY;
                };

                output.append(bgColor).append(pieceSymbol);
            }
            output.append(EscapeSequences.SET_BG_COLOR_BLACK);
            output.append(" ").append(8-i).append(" ");
            output.append(EscapeSequences.RESET_BG_COLOR).append("\n");
            whiteSquare = !whiteSquare;
        }

        output.append(EscapeSequences.SET_BG_COLOR_BLACK).append(cols);
        output.append(EscapeSequences.RESET_BG_COLOR).append("\n");

        output.append(EscapeSequences.RESET_TEXT_COLOR).append(EscapeSequences.RESET_BG_COLOR).append(EscapeSequences.RESET_TEXT_BOLD_FAINT);

        return output.toString();
    }

    public String move(String... params) throws ResponseException {
        if (params.length == 1) {
            GetGameResult gameRes = server.getGame(new GetGameRequest(authToken, gameID));
            GameData gameData = gameRes.gameData;
            try {
                gameData.game().makeMove(createMove(params[0]));
            } catch (InvalidMoveException ex) {
                throw new ResponseException(400, "Invalid move.");
            }
            server.updateGame(new UpdateGameRequest(authToken,gameData));
            return "Move made successfully";
        }
        throw new ResponseException(400, "Expected: <MOVE> (ex. e2e4)");
    }

    private ChessMove createMove(String stringMove) throws ResponseException {
        if (stringMove == null || stringMove.length() != 4) {
            throw new ResponseException(400, "Invalid move format. Expected format: e2e4");
        }

        return new ChessMove(createPosition(stringMove.substring(0,2)), createPosition(stringMove.substring(2,4)), null);
    }

    private ChessPosition createPosition(String stringPosition) throws ResponseException {
        if (stringPosition == null || stringPosition.length() != 2) {
            throw new ResponseException(400, "Invalid position format. Expected format: e2");
        }

        int col = stringPosition.charAt(0) - 'a' + 1;
        int row = Character.getNumericValue(stringPosition.charAt(1));

        return new ChessPosition(row, col);
    }

    public String leave(String... params) throws ResponseException {
        return "PostLoginClient";
    }

    public String resign(String... params) throws ResponseException {
        return "PostLoginClient";
    }

    public String highlight(String... params) throws ResponseException {
        return "PostLoginClient";
    }


    public String help() {
        return """
                Possible commands:
                - redraw - redraw the game board
                - move <MOVE> - make a move (e2e4)
                - leave - leave the game, if you are a player you can rejoin later
                - resign - forfeit the game
                - highlight <POSITION> - highlight all legal moves for a piece (e2)
                - help - display possible commands
                """;
    }
}
