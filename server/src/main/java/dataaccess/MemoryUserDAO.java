package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> allUserData= new HashMap<>();
    public void createUser(UserData userData) throws DataAccessException {
        allUserData.put(userData.username(), userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        return allUserData.get(username);
    }

    public void deleteAllUsers() throws DataAccessException {
        allUserData.clear();
    }
}
