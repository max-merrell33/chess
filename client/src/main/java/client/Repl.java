package client;

import exception.ResponseException;
import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {
    private UIClient client;
    private final String serverUrl;
    private State state;

    public Repl(String serverUrl) {
        //client = new PreLoginClient(serverUrl);
        client = new ChessClient(serverUrl, "22b948ad-1069-4a73-9c60-afc09b0ad34d", "test", 3, true);
        this.serverUrl = serverUrl;
        state = State.LOGGED_OUT;
    }

    public void run() {
        System.out.println("Welcome to the chess game. Sign in to start.\n");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (!checkClientChange(result)) {
                    System.out.print(result);
                }
            } catch (ResponseException e) {
                var msg = e.getMessage();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + "[" + state + "]" + EscapeSequences.RESET_TEXT_COLOR + " >>> ");
    }

    private boolean checkClientChange(String result) throws ResponseException {
        switch (result) {
            case "PreLoginClient" -> {
                client = new PreLoginClient(serverUrl);
                System.out.println("Logout Successful\n");
                System.out.print(client.help());
                state = State.LOGGED_OUT;
                return true;
            }
            case "PostLoginClient" -> {
                client = new PostLoginClient(serverUrl, client.getAuthToken(), client.getUsername());
                System.out.print("Login Successful\n\n");
                System.out.print(client.help());
                state = State.LOGGED_IN;
                return true;
            }
            case "ChessClient" -> {
                client = new ChessClient(serverUrl, client.getAuthToken(), client.getUsername(), client.getGameID(), client.playerIsWhite());
                state = State.CHESS_GAME;
                System.out.print(client.eval("print"));
                return true;
            }
            default -> {
                return false;
            }
        }
    }

}