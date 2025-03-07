package dataaccess;

import org.junit.jupiter.api.*;
import server.ResponseException;


import static org.junit.jupiter.api.Assertions.assertThrows;

public class DBUnitTests {

    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;

    @BeforeAll
    public static void init() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
    }

    @BeforeEach
    public void beforeEach() throws ResponseException {

    }




    //AuthDAO Tests
    @Test
    @DisplayName("Good Create Auth")
    public void goodCreateAuth() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Create Auth")
    public void badCreateAuth() throws DataAccessException {

    }

    @Test
    @DisplayName("Good Get Auth")
    public void goodGetAuth() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Get Auth")
    public void badGetAuth() throws DataAccessException {

    }

    @Test
    @DisplayName("Good Delete Auth")
    public void goodDeleteAuth() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Delete Auth")
    public void badDeleteAuth() throws DataAccessException {

    }

    @Test
    @DisplayName("Delete All Auths")
    public void deleteAllAuths() throws DataAccessException {

    }




    //GameDAO Tests
    @Test
    @DisplayName("Good Create Game")
    public void goodCreateGame() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Create Game")
    public void badCreateGame() throws DataAccessException {

    }

    @Test
    @DisplayName("Good Get Game")
    public void goodGetGame() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Get Game")
    public void badGetGame() throws DataAccessException {

    }

    @Test
    @DisplayName("Good Get All Games")
    public void goodGetAllGames() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Get All Games")
    public void badGetAllGames() throws DataAccessException {

    }

    @Test
    @DisplayName("Good Update Game")
    public void goodUpdateGame() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Update Game")
    public void badUpdateGame() throws DataAccessException {

    }

    @Test
    @DisplayName("Delete All Games")
    public void deleteAllGames() throws DataAccessException {

    }




    //GameDAO Tests
    @Test
    @DisplayName("Good Create User")
    public void goodCreateUser() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Create User")
    public void badCreateUser() throws DataAccessException {

    }

    @Test
    @DisplayName("Good Get User")
    public void goodGetUser() throws DataAccessException {

    }

    @Test
    @DisplayName("Bad Get User")
    public void badGetUser() throws DataAccessException {

    }

    @Test
    @DisplayName("Delete All Users")
    public void deleteAllUsers() throws DataAccessException {

    }
}
