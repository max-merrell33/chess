package client;

import exception.ResponseException;
import model.GameDataTX;
import model.request.CreateRequest;
import model.request.ListRequest;
import model.request.LoginRequest;
import model.result.ListResult;

import java.util.Arrays;

public class PostLoginClient extends UIClient {
    public PostLoginClient(String serverUrl, String authToken, String username) {
        super(serverUrl, authToken, username);
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
        System.out.println("Made it to the function");

        if (params.length == 1) {
            System.out.println(authToken);
            server.createGame(new CreateRequest(authToken, params[0]));
            //TODO add the game ID to join here
            //return "ChessClient";

            return "Game Created";
        }
        System.out.println(authToken);
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String list(String... params) throws ResponseException {
        if (params.length == 0) {
            ListResult res = server.listGames(new ListRequest(authToken));
            StringBuilder returnString = new StringBuilder();
            for (GameDataTX game : res.games) {
                returnString.append("ID: ").append(game.gameID());
                returnString.append(" Name").append(game.gameName());
                returnString.append(" White: ").append(game.whiteUsername());
                returnString.append(" Black: ").append(game.blackUsername());
                returnString.append("\n");
            }
            return returnString.toString();
        }
        throw new ResponseException(400, "Unexpected characters after command");
    }

    public String join(String... params) throws ResponseException {
        if (params.length == 2) {
            server.loginUser(new LoginRequest(params[0], params[1]));
            return "PostLoginClient";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String observe(String... params) throws ResponseException {
        if (params.length == 2) {
            server.loginUser(new LoginRequest(params[0], params[1]));
            return "PostLoginClient";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String logout(String... params) throws ResponseException {
        if (params.length == 2) {
            server.loginUser(new LoginRequest(params[0], params[1]));
            return "PostLoginClient";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
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
}
