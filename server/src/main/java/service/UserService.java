package service;

import dataaccess.UserDAO;

public class UserService {
    // register, login, logout, clear
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


}
