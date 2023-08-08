package server.commander;

public class RequestConstants {
    public static final String STATUS = "--Status";
    public static final String REASON = "--Reason";
    public static final String SUCCESS = "Succesfull";
    public static final String FAILED = "Failed";
    public static final String UUID_REGISTRATION = "UUID-Registration";
    public static final String INIT_GAME_SESSION = "Init-Game-Session";
    public static final String CONNECT_TO_GAME_SESSION = "Connect-To-Game-Session";
    public static final String QUIT_FROM_GAME_SESSION = "Quit-From-Game-Session";
    public static final String GET_DATA_FROM_GAME_SESSION = "Get-Data-From-Game-Session";
    public static final String SET_DATA_TO_GAME_SESSION = "Set-Data-To-Game-Session";
    public static final String ERROR_REQUEST = "Error-Request";
    public static final String COULDNT_FIND_YOUR_ID = "Couldn't find your ID";
    public static final String UNSUPPORTED_REQUEST_FORMAT = "Couldn't parse Request data";

    private RequestConstants() {

    }
}
