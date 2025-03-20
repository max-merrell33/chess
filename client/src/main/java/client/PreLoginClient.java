package client;

import java.util.Arrays;

import exception.ResponseException;
import model.request.RegisterRequest;

public class PreLoginClient extends UIClient {

    public PreLoginClient(String serverUrl) {
        super(serverUrl);
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
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            server.registerUser(new RegisterRequest(params[0], params[1], params[2]));
            return "PostLoginClient";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) {
        return "";
    }


    public String help() {
        return """
                - register <USERNAME> <PASSWORD> <EMAIL> - create an account
                - login <USERNAME> <PASSWORD> - sign in
                - quit - exit the app
                - help - display possible commands
                """;
    }

}