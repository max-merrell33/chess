//package dataaccess;
//
//import model.AuthData;
//import model.request.CreateRequest;
//import model.request.ListRequest;
//import model.request.LoginRequest;
//import model.request.RegisterRequest;
//import model.result.RegisterResult;
//import org.junit.jupiter.api.*;
//import server.ResponseException;
//import service.ClearService;
//import service.GameService;
//import service.UserService;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class DBUnitTests {
//
//    private static UserDAO userDAO;
//    private static AuthDAO authDAO;
//    private static GameDAO gameDAO;
//
//    @BeforeAll
//    public static void init() {
//        UserDAO userDAO = new MemoryUserDAO();
//        AuthDAO authDAO = new MemoryAuthDAO();
//        GameDAO gameDAO = new MemoryGameDAO();
//    }
//
//    @BeforeEach
//    public void beforeEach() throws ResponseException {
//        clearService.clear();
//
//        //Register one user and save the auth token
//        RegisterResult res = userService.register(testUserReg);
//        testAuthToken = res.authToken;
//
//    }
//
//    @Test
//    @DisplayName("Good Clear")
//    public void goodClear() throws ResponseException {
//
//    }
//}
