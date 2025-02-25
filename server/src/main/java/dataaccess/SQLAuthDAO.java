package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    public AuthData createAuth(String authToken, String username) throws DataAccessException {
        return null;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void removeAuth(String authToken) throws DataAccessException {

    }

    public void deleteAllAuths() throws DataAccessException {

    }
}
