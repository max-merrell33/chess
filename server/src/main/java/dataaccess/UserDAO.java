package dataaccess;

import model.UserData;

public interface UserDAO {
    //createUser
    public void createUser(UserData userData);

    //getUser
    public UserData getUser(String username);

    //deleteAllUsers
    public void deleteAllUsers();
}
