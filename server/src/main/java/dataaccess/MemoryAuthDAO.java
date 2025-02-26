package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> allAuthData = new HashMap<>();

    //createAuth
    public AuthData createAuth(String authToken, String username) {
        var newAuthData = new AuthData(authToken, username);

        allAuthData.put(authToken, newAuthData);
        return newAuthData;
    }

    //getAuth
    public AuthData getAuth(String authToken) {
        return allAuthData.get(authToken);
    }

    //deleteAuth
    public void removeAuth(String authToken) {
        allAuthData.remove(authToken);
    }

    //deleteAllAuth
    public void deleteAllAuths() {
        allAuthData.clear();
    }
}
