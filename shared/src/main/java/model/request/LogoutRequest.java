package model.request;

public class LogoutRequest extends AuthenticatedRequest {
    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }
}
