package client;

import java.util.Arrays;

import exception.ResponseException;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;

public class PreLoginClient extends UIClient {

    public PreLoginClient(String serverUrl) {
        super(serverUrl, null, null, 0, true);
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
                default -> "Invalid Input. " + help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResult res = server.registerUser(new RegisterRequest(params[0], params[1], params[2]));
            return enterPostLoginClient(res.username, res.authToken);
        }
        throw new ResponseException(0, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResult res = server.loginUser(new LoginRequest(params[0], params[1]));
            return enterPostLoginClient(res.username, res.authToken);

        }
        throw new ResponseException(0, "Expected: <USERNAME> <PASSWORD>");
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

    private String enterPostLoginClient(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
        return "PostLoginClient";
    }

}