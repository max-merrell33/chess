package client;

import exception.ResponseException;
import model.GameDataTX;
import model.request.*;
import model.result.ListResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient extends UIClient {
    private final Map<Integer, Integer> gameNumToGameID = new HashMap<>();

    public PostLoginClient(String serverUrl, String authToken, String username) throws ResponseException{
        super(serverUrl, authToken, username, 0, true, false);
        ListResult res = server.listGames(new ListRequest(authToken));
        int gameNum = 1;
        for (GameDataTX game : res.games) {
            gameNumToGameID.put(gameNum, game.gameID());
            gameNum++;
        }
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
        throw new ResponseException(0, "Expected: <NAME>");
    }

    public String list(String... params) throws ResponseException {
        if (params.length == 0) {
            ListResult res = server.listGames(new ListRequest(authToken));
            return printListGamesTable(res);
        }
        throw new ResponseException(0, "Unexpected characters after command");
    }

    private String printListGamesTable(ListResult res) {
        StringBuilder returnString = new StringBuilder();

        int idWidth = "Game #".length();
        int nameWidth = "Name".length();
        int whiteWidth = "White".length();
        int blackWidth = "Black".length();

        int gameNum = 1;
        for (GameDataTX game : res.games) {
            gameNumToGameID.put(gameNum, game.gameID());
            idWidth = Math.max(idWidth, String.valueOf(gameNum).length());
            nameWidth = Math.max(nameWidth, game.gameName() != null ? game.gameName().length() : 1);
            whiteWidth = Math.max(whiteWidth, game.whiteUsername() != null ? game.whiteUsername().length() : 1);
            blackWidth = Math.max(blackWidth, game.blackUsername() != null ? game.blackUsername().length() : 1);
            gameNum++;
        }

        int padding = 4;
        idWidth += padding;
        nameWidth += padding;
        whiteWidth += padding;
        blackWidth += padding;

        String format = "%-" + idWidth + "s%-" + nameWidth + "s%-" + whiteWidth + "s%-" + blackWidth + "s%n";
        returnString.append(String.format(format, "Game #", "Name", "White", "Black"));

        int totalWidth = idWidth + nameWidth + whiteWidth + blackWidth;
        returnString.append("-".repeat(totalWidth)).append("\n");

        gameNum = 1;
        for (GameDataTX game : res.games) {
            returnString.append(String.format(
                    format,
                    gameNum,
                    game.gameName() != null ? game.gameName() : "-",
                    game.whiteUsername() != null ? game.whiteUsername() : "-",
                    game.blackUsername() != null ? game.blackUsername() : "-"
            ));
            gameNum++;
        }
        return returnString.toString();
    }

    public String join(String... params) throws ResponseException {
        if (params.length == 2) {
            int gameNum;
            try {
                gameNum = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                throw new ResponseException(0, "Expected: <GAME #>");
            }
            if (!gameNumToGameID.containsKey(gameNum)) {
                throw new ResponseException(0, "Invalid game number");
            }
            int gameID = gameNumToGameID.get(gameNum);

            String playerColor = params[1].toUpperCase();
            server.joinGame(new JoinRequest(authToken, playerColor, gameID));
            return enterChessClient(gameID, playerColor.equals("WHITE"), false);
        }
        throw new ResponseException(0, "Expected: <GAME #> [WHITE|BLACK]");
    }

    public String observe(String... params) throws ResponseException {
        if (params.length == 1) {
            return enterChessClient(getGameIDFromGameNum(params[0]), true, true);
        }
        throw new ResponseException(0, "Expected: <GAME #>");
    }

    public String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            server.logoutUser(new LogoutRequest(authToken));
            return "PreLoginClient";
        }
        throw new ResponseException(0, "Unexpected characters after command");
    }

    public String help() {
        return """
                Possible commands:
                - create <NAME> - create a game with a given name
                - list - list all the available games
                - join <GAME #> [WHITE|BLACK] - join a game
                - observe <GAME #> - observe a game
                - logout - logout and go to main menu
                - quit - exit the app
                - help - display possible commands
                """;
    }

    private String enterChessClient(int gameID, boolean isWhite, boolean isObserver) {
        this.gameID = gameID;
        this.playerIsWhite = isWhite;
        this.playerIsObserver = isObserver;
        return "ChessClient";
    }

    private int getGameIDFromGameNum(String gameNum) throws ResponseException {
        int gameNumInt;
        try {
            gameNumInt = Integer.parseInt(gameNum);
        } catch (NumberFormatException ex) {
            throw new ResponseException(0, "Invalid game number");
        }
        if (!gameNumToGameID.containsKey(gameNumInt)) {
            throw new ResponseException(0, "No game with number " + gameNum + " exists");
        }

        return gameNumToGameID.get(gameNumInt);
    }

}
