package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> allAuthData = new HashMap<>();

    //createAuth
    public AuthData createAuth(String authToken, String username) throws DataAccessException {
        var newAuthData = new AuthData(authToken, username);

        allAuthData.put(authToken, newAuthData);
        return newAuthData;
    }

    //getAuth
    public AuthData getAuth(String authToken) throws DataAccessException {
        return allAuthData.get(authToken);
    }

    //deleteAuth
    public void removeAuth(String authToken) throws DataAccessException {
        allAuthData.remove(authToken);
    }

    //deleteAllAuth
    public void deleteAllAuths() throws DataAccessException {
        allAuthData.clear();
    }
}
