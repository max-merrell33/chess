package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> allUserData= new HashMap<>();
    public void createUser(UserData userData) {
        allUserData.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return allUserData.get(username);
    }

    public void deleteAllUsers() {
        allUserData.clear();
    }
}
