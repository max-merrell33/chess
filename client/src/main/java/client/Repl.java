package client;

import java.util.Scanner;

public class Repl {
    private UIClient client;
    private String serverUrl;
    // private final EscapeSequences es;

    public Repl(String serverUrl) {
        client = new PreLoginClient(serverUrl, null, null);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the chess game. Sign in to start.");
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
        System.out.print("\n" + ">>> ");
    }

    private boolean checkClientChange(String result) {
        if (result.equals("PreLoginClient")) {
            client = new PreLoginClient(serverUrl, client.getAuthToken(), client.getUsername());
            System.out.println("Logout Successful\n");
            System.out.print(client.help());
            return true;
        }
        if (result.equals("PostLoginClient")) {
            client = new PostLoginClient(serverUrl, client.getAuthToken(), client.getUsername());
            System.out.print("Login Successful\n\n");
            System.out.print(client.help());
            return true;
        }
        if (result.equals("ChessClient")) {
            client = new ChessClient(serverUrl, client.getAuthToken(), client.getUsername());
            return true;
        }
        return false;
    }

}