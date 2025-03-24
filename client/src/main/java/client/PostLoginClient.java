package client;

import exception.ResponseException;
import model.GameDataTX;
import model.request.*;
import model.result.ListResult;

import java.util.Arrays;

public class PostLoginClient extends UIClient {
    //TODO add a map so that the list table does not list the game IDs but a separate numbering system

    public PostLoginClient(String serverUrl, String authToken, String username) {
        super(serverUrl, authToken, username, 0, true);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Invalid Input. Possible commands: " + help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String create(String... params) throws ResponseException {
        if (params.length == 1) {
            server.createGame(new CreateRequest(authToken, params[0]));
            return "Game Created Successfully";
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String list(String... params) throws ResponseException {
        if (params.length == 0) {
            ListResult res = server.listGames(new ListRequest(authToken));
            return printListGamesTable(res);
        }
        throw new ResponseException(400, "Unexpected characters after command");
    }

    private String printListGamesTable(ListResult res) {
        StringBuilder returnString = new StringBuilder();

        int idWidth = "ID".length();
        int nameWidth = "Name".length();
        int whiteWidth = "White".length();
        int blackWidth = "Black".length();

        for (GameDataTX game : res.games) {
            idWidth = Math.max(idWidth, String.valueOf(game.gameID()).length());
            nameWidth = Math.max(nameWidth, game.gameName() != null ? game.gameName().length() : 1);
            whiteWidth = Math.max(whiteWidth, game.whiteUsername() != null ? game.whiteUsername().length() : 1);
            blackWidth = Math.max(blackWidth, game.blackUsername() != null ? game.blackUsername().length() : 1);
        }

        int padding = 4;
        idWidth += padding;
        nameWidth += padding;
        whiteWidth += padding;
        blackWidth += padding;

        String format = "%-" + idWidth + "s%-" + nameWidth + "s%-" + whiteWidth + "s%-" + blackWidth + "s%n";
        returnString.append(String.format(format, "ID", "Name", "White", "Black"));

        int totalWidth = idWidth + nameWidth + whiteWidth + blackWidth;
        returnString.append("-".repeat(totalWidth)).append("\n");

        for (GameDataTX game : res.games) {
            returnString.append(String.format(
                    format,
                    game.gameID(),
                    game.gameName() != null ? game.gameName() : "-",
                    game.whiteUsername() != null ? game.whiteUsername() : "-",
                    game.blackUsername() != null ? game.blackUsername() : "-"
            ));
        }

        return returnString.toString();
    }

    public String join(String... params) throws ResponseException {
        if (params.length == 2) {
            String playerColor = params[1].toUpperCase();
            server.joinGame(new JoinRequest(authToken, playerColor, Integer.parseInt(params[0])));
            return enterChessClient(Integer.parseInt(params[0]), playerColor.equals("WHITE"));
        }
        throw new ResponseException(400, "Expected: <GAMEID> [WHITE|BLACK]");
    }

    public String observe(String... params) throws ResponseException {
        if (params.length == 1) {
            return enterChessClient(Integer.parseInt(params[0]), true);
        }
        throw new ResponseException(400, "Expected: <GAMEID>");
    }

    public String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            server.logoutUser(new LogoutRequest(authToken));
            return "PreLoginClient";
        }
        throw new ResponseException(400, "Unexpected characters after command");
    }

    public String help() {
        return """
                Possible commands:
                - create <NAME> - create a game with a given name
                - list - list all the available games
                - join <GAMEID> [WHITE|BLACK] - join a game
                - observe <GAMEID> - observe a game
                - logout - logout and go to main menu
                - quit - exit the app
                - help - display possible commands
                """;
    }

    private String enterChessClient(int gameID, boolean isWhite) {
        this.gameID = gameID;
        this.playerIsWhite = isWhite;
        return "ChessClient";
    }

}
