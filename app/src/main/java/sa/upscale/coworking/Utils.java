package sa.upscale.coworking;

public class Utils {
    /*CONSTANT FOR THE AUTHORIZATION PROCESS*/

    //This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    public static final String STATE = "E3ZYKC1T6H2yP4z";
    //This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
    //We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    public static final String REDIRECT_URI = "https://hive.sa";
    public static final String RESPONSE_TYPE_VALUE = "code";
    public static final String STATE_PARAM = "state";
    /****FILL THIS WITH YOUR INFORMATION*********/
    //This is the public api key of our application
    private static final String API_KEY = "81r0d97aegatex";
    //This is the private api key of our application
    private static final String SECRET_KEY = "6eLxJGvYf7WXE9ma";
    /*********************************************/

    //These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    private static final String SCOPE = "scope";
    private static final String FULL_PROFILE_PER = "r_basicprofile";
    private static final String EMAIL_PER = "r_emailaddress";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    private static final String SPACE = "%20";


    private static final String PROFILE_URL = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";
    private static final String OAUTH_ACCESS_TOKEN_PARAM = "oauth2_access_token";

    public static String accessToken;

    /**
     * Method that generates the url for get the access token from the Service
     *
     * @return Url
     */
    public static String getAccessTokenUrl(String authorizationToken) {
        return ACCESS_TOKEN_URL + QUESTION_MARK + GRANT_TYPE_PARAM + EQUALS +
                GRANT_TYPE + AMPERSAND + RESPONSE_TYPE_VALUE + EQUALS + authorizationToken +
                AMPERSAND + CLIENT_ID_PARAM + EQUALS + API_KEY + AMPERSAND +
                REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI + AMPERSAND
                + SECRET_KEY_PARAM + EQUALS + SECRET_KEY;
    }

    public static String getProfileUrl(String accessToken) {
        return PROFILE_URL
                + QUESTION_MARK
                + OAUTH_ACCESS_TOKEN_PARAM + EQUALS + accessToken;
    }

    /**
     * Method that generates the url for get the authorization token from the Service
     *
     * @return Url
     */
    public static String getAuthorizationUrl() {
        return AUTHORIZATION_URL + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE + AMPERSAND + CLIENT_ID_PARAM + EQUALS + API_KEY + AMPERSAND + STATE_PARAM + EQUALS + STATE + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI +
                AMPERSAND + SCOPE + EQUALS + FULL_PROFILE_PER + SPACE + EMAIL_PER;
    }
}
