package dataaccess;

import model.UserData;

public interface UserDAO {
    //createUser
    public void createUser(UserData userData) throws DataAccessException;

    //getUser
    public UserData getUser(String username) throws DataAccessException;

    //deleteAllUsers
    public void deleteAllUsers() throws DataAccessException;
}
