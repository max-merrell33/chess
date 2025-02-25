package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String authToken, String username);

    public AuthData getAuth(String authToken);

    public void removeAuth(String authToken);

    public void deleteAllAuths();
}
