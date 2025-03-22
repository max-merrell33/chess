package model.request;

public class ListRequest extends AuthenticatedRequest {
    public ListRequest(String authToken) {
        this.authToken = authToken;
    }
}
