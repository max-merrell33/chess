
package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.request.*;
import model.result.*;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return makeRequest("POST", path, registerRequest, RegisterResult.class);
    }

    public LoginResult loginUser(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return makeRequest("POST", path, loginRequest, LoginResult.class);
    }

    public LogoutResult logoutUser(LogoutRequest logoutRequest) throws ResponseException {
        var path = "/session";
        return makeRequest("DELETE", path, logoutRequest, LogoutResult.class);
    }

    public ListResult listGames(ListRequest listRequest) throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, listRequest, ListResult.class);
    }

    public JoinResult joinGame(JoinRequest joinRequest) throws ResponseException {
        var path = "/game";
        return makeRequest("PUT", path, joinRequest, JoinResult.class);
    }

    public GetGameResult getGame(GetGameRequest getGameRequest) throws ResponseException {
        var path = "/game/" + getGameRequest.gameID;
        return makeRequest("GET", path, getGameRequest, GetGameResult.class);
    }

    public UpdateGameResult updateGame(UpdateGameRequest updateGameRequest) throws ResponseException {
        var path = "/move";
        return makeRequest("POST", path, updateGameRequest, UpdateGameResult.class);
    }

    public CreateResult createGame(CreateRequest createRequest) throws ResponseException {
        var path = "/game";
        return makeRequest("POST", path, createRequest, CreateResult.class);
    }

    public ClearResult clear() throws ResponseException {
        var path = "/db";
        return makeRequest("DELETE", path, null, ClearResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (request != null && !"GET".equalsIgnoreCase(method)) {
                http.setDoOutput(true);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request instanceof AuthenticatedRequest authRequest) {
            http.addRequestProperty("Authorization", authRequest.getAuthToken());
        }
        if (request != null && http.getDoOutput()) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
