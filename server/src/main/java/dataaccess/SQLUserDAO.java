package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.sql.ResultSet;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    UtilsDB utilsDB = new UtilsDB();

    public SQLUserDAO() throws DataAccessException {
        utilsDB.configureDatabase();
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        utilsDB.executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAllUsers() throws DataAccessException {
        var statement = "TRUNCATE users";
        utilsDB.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }


}
