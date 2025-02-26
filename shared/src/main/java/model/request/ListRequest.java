package model.request;

public class ListRequest extends Request {
    public String authToken;

    public ListRequest(String authToken) {
        this.authToken = authToken;
    }
}
