package model.request;

public class CreateRequest extends AuthenticatedRequest {
    public String gameName;

    public CreateRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }
}
