package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {
    private UIClient client;
    private String serverUrl;
    private State state;

    public Repl(String serverUrl) {
        client = new PreLoginClient(serverUrl);
        //client = new ChessClient(serverUrl, "f24d8352-dfde-44e3-a025-61080d269beb", "test", 3, false);
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
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + "[" + state + "]" + EscapeSequences.RESET_TEXT_COLOR + " >>> ");
    }

    private boolean checkClientChange(String result) {
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