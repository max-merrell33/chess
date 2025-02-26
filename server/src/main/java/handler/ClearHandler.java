package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public static Object clearHandler(Request req, Response res) throws DataAccessException {
        return new Gson().toJson(new ClearService().clear());
    }

}
