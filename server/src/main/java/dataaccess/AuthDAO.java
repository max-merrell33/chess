package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String authToken, String username) throws DataAccessException;

    public AuthData getAuth(String authToken) throws DataAccessException;

    public void removeAuth(String authToken) throws DataAccessException;

    public void deleteAllAuths() throws DataAccessException;
}
