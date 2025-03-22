package client;

import java.util.Arrays;

import exception.ResponseException;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;

public class PreLoginClient extends UIClient {

    public PreLoginClient(String serverUrl, String authToken, String username) {
        super(serverUrl, authToken, username);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Invalid Input. Possible commands: " + help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResult res = server.registerUser(new RegisterRequest(params[0], params[1], params[2]));
            username = res.username;
            authToken = res.authToken;
            return "PostLoginClient";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResult res = server.loginUser(new LoginRequest(params[0], params[1]));
            username = res.username;
            authToken = res.authToken;
            return "PostLoginClient";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String help() {
        return """
                Possible commands:
                - register <USERNAME> <PASSWORD> <EMAIL> - create an account
                - login <USERNAME> <PASSWORD> - sign in
                - quit - exit the app
                - help - display possible commands
                """;
    }

}