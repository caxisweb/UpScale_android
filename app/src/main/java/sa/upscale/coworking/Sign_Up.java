package sa.upscale.coworking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;

import static sa.upscale.coworking.Utils.REDIRECT_URI;
import static sa.upscale.coworking.Utils.RESPONSE_TYPE_VALUE;
import static sa.upscale.coworking.Utils.STATE;
import static sa.upscale.coworking.Utils.STATE_PARAM;
import static sa.upscale.coworking.Utils.accessToken;
import static sa.upscale.coworking.Utils.getAccessTokenUrl;
import static sa.upscale.coworking.Utils.getAuthorizationUrl;
import static sa.upscale.coworking.Utils.getProfileUrl;


public class Sign_Up extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String status = "status";
    private static final String message = "message";

    private static final String s_email = "email";
    private static final String s_password = "password";
    private static final String s_name = "name";
    private static final String s_mobile = "mobile";
    private static final String TAG = "Twitter_error";
    private static final int RC_SIGN_IN = 1;
    private static final String TWITTER_KEY = "mr4zFMgZQtUh4iVGvuiEUvd0d";
    private static final String TWITTER_SECRET = "QskHYWA1kupQN28SKe1GIbBqFFE4SuiJGvEEnfMCBPrJyhDbZl";
    String status1 = "0", message1 = "try Again";
    JSONObject data_signup = new JSONObject();
    String fb_name, fb_email;
    CheckBox ch_terms;
    JSONObject social_user_data = new JSONObject();
    String fb_img = "";
    TwitterAuthConfig authConfig;
    TwitterAuthClient mTwitterAuthClient;
    TwitterSession session1;
    String twitter_name, twitter_email, twitter_Id, twitter_profilePic;
    LinearLayout linearLayout;
    SessionManager session;
    HashMap<String, String> userDetails = new HashMap<>();
    ImageView fbLoginButton, btnSignIn, btn_linkedin, btn_twitter1;
    AlertDialog alertDialog;
    private EditText ed_name, ed_mobile, ed_email, ed_password, ed_password_again;
    private Button btn_signup;
    private String mstr_name, mstr_mobile, mstr_email, mstr_password, str_esaal_cust_id, mstr_password_again;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private TwitterLoginButton btn_twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(authConfig)//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);
        //Fabric.with(this, new Twitter(authConfig));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_sign__up);
        session = new SessionManager(Sign_Up.this);
        userDetails = session.getUserDetails();

        findViews();

        ch_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    startActivity(new Intent(Sign_Up.this, Terms_Condition.class));
                } else {

                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mstr_name = ed_name.getText().toString().trim();
                mstr_mobile = ed_mobile.getText().toString().trim();
                mstr_email = ed_email.getText().toString().trim();
                mstr_password = ed_password.getText().toString().trim();
                mstr_password_again = ed_password_again.getText().toString().trim();

                String str = String.valueOf(mstr_mobile.startsWith("05"));

                if (mstr_name.length() == 0) {
                    ed_name.setError("Please Enter Name");
                    ed_name.setFocusable(true);
                } else if (mstr_mobile.length() == 0) {
                    ed_mobile.setError("Please Enter Contact No.");
                    ed_mobile.setFocusable(true);
                } else if (str.equals("false")) {
                    ed_mobile.setError("Number Start With 05");
                    ed_mobile.setFocusable(true);
                    ed_mobile.requestFocus();
                } else if (mstr_mobile.length() != 10) {
                    ed_mobile.setError("Please Enter Correct Number");
                    ed_mobile.setFocusable(true);
                } else if (mstr_email.length() == 0) {
                    ed_email.setError("Please Enter Email Id");
                    ed_email.setFocusable(true);
                } else if (!mstr_email.matches("[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
                    ed_email.setError("Invalid Email Address");
                    ed_email.setFocusable(true);
                } else if (mstr_password.length() == 0) {
                    ed_password.setError("Please Enter Password");
                    ed_password.setFocusable(true);
                } else if (!mstr_password.equals(mstr_password_again)) {
                    ed_password_again.setError("Password Does not match");
                } else if (!ch_terms.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please Enable Terms & Condition", Toast.LENGTH_SHORT).show();
                } else {

                    if (isNetworkAvailable()) {
                        try {
                            data_signup.put(s_email, mstr_email);
                            data_signup.put(s_password, mstr_password);
                            data_signup.put(s_name, mstr_name);
                            data_signup.put(s_mobile, mstr_mobile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Task_signUp task_signUp = new Task_signUp();
                        task_signUp.execute();

                    } else {
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //setMobileDataEnabled(Login.this);

                                    }
                                });

                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);

                        snackbar.show();

                    }
                }
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                if (accessToken == null) {

                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                    Log.i("LoginActivity", graphResponse.toString());
                                    // Get facebook data from login
                                    Bundle bFacebookData = getFacebookData(jsonObject);

                                    fb_name = bFacebookData.getString("first_name") + " " + bFacebookData.getString("last_name");
                                    fb_email = bFacebookData.getString("email");
                                    String fb_id = bFacebookData.getString("idFacebook");
                                    fb_img = bFacebookData.getString("profile_pic");

                                    Log.d("FaceBook", fb_name + "\n" + fb_email + "\n" + fb_id + "\n" + fb_img);

                                    try {

                                        social_user_data.put("name", fb_name);
                                        social_user_data.put("email", fb_email);
                                        social_user_data.put("type", "facebook");
                                        social_user_data.put("type_id", fb_id);
                                        social_user_data.put("image", fb_img);
                                        social_user_data.put("gcm_id", 1);

                                        FB_Login fbLogin = new FB_Login();
                                        fbLogin.execute();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email");
                    request.setParameters(parameters);
                    request.executeAsync();
                } else {

                    GraphRequest request = GraphRequest.newMeRequest(accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                    Log.i("LoginActivity", graphResponse.toString());
                                    // Get facebook data from login
                                    Bundle bFacebookData = getFacebookData(jsonObject);

                                    fb_name = bFacebookData.getString("first_name") + " " + bFacebookData.getString("last_name");
                                    fb_email = bFacebookData.getString("email");
                                    String fb_id = bFacebookData.getString("idFacebook");
                                    fb_img = bFacebookData.getString("profile_pic");

                                    Log.d("FaceBook", fb_name + "\n" + fb_email + "\n" + fb_id + "\n" + fb_img);

                                    try {

                                        social_user_data.put("name", fb_name);
                                        social_user_data.put("email", fb_email);
                                        social_user_data.put("type", "facebook");
                                        social_user_data.put("type_id", fb_id);
                                        social_user_data.put("image", fb_img);
                                        social_user_data.put("gcm_id", 1);

                                        FB_Login fbLogin = new FB_Login();
                                        fbLogin.execute();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(Sign_Up.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(Sign_Up.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                System.out.println("Facebook Login failed!!");
                Log.i("fb login error", e.getMessage());
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager.getInstance().setLoginBehavior("SUPPRESS_SSO");
                LoginManager.getInstance().logInWithReadPermissions(Sign_Up.this, Arrays.asList("public_profile", "email"));
                //setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);


            }
        });

        mTwitterAuthClient = new TwitterAuthClient();

        btn_twitter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTwitterAuthClient.authorize(Sign_Up.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {

                        // Success
                        session1 = result.data;

                        Call<User> userResult = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(true, false, true);
                        userResult.enqueue(new Callback<User>() {

                            @Override
                            public void failure(TwitterException e) {

                            }

                            @Override
                            public void success(Result<User> userResult) {

                                User user = userResult.data;

                                try {

                                    twitter_profilePic = user.profileImageUrl;
                                    twitter_name = user.name;
                                    twitter_Id = String.valueOf(user.id);

                                    Fatchtwitter_email();
                                    Log.d(TAG, "+aa\n" + twitter_profilePic + "\n" + twitter_name + "\n" + twitter_Id + "email" + user.email);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        });
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        /*btn_twitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                //TwitterAuthToken authToken = session.getAuthToken();
                //String token = authToken.token;
                //String secret = authToken.secret;

                TwitterAuthClient authClient = new TwitterAuthClient();

                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        // Do something with the result, which provides the email address
                        Log.i(TAG, " EMAIL EXITO!");
                        twitter_email = result.data;

                        if (twitter_email.length() != 0) {

                            try {
                                Log.d(TAG, twitter_name + "\n" + twitter_email + "\n" + twitter_Id + "\n" + twitter_profilePic);

                                social_user_data.put("name", twitter_name);
                                social_user_data.put("email", twitter_email);
                                social_user_data.put("type", "twitter");
                                social_user_data.put("type_id", twitter_Id);
                                social_user_data.put("image", twitter_profilePic);
                                social_user_data.put("gcm_id", 1);

                                FB_Login fbLogin = new FB_Login();
                                fbLogin.execute();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(Sign_Up.this, "Sorry Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        Log.i(TAG, exception.getMessage());
                        Toast.makeText(Sign_Up.this, "Please Click on Allow For Email Access", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });*/

        btn_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog pd = ProgressDialog.show(Sign_Up.this, "", "Please Wait...", true);

                LayoutInflater inflater = LayoutInflater.from(Sign_Up.this);
                final View linkedview = inflater.inflate(R.layout.linkedin_webview, null);
                alertDialog = new AlertDialog.Builder(Sign_Up.this).create();
                alertDialog.setView(linkedview);
                //get the webView from the layout
                final WebView webView = linkedview.findViewById(R.id.main_activity_web_view);
                EditText editText = linkedview.findViewById(R.id.edt_temp);
                editText.requestFocus();
                //Request focus for the webview
                view.requestFocus(View.FOCUS_DOWN);

                //Set a custom web view client
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        //This method will be executed each time a page finished loading.
                        //The only we do is dismiss the progressDialog, in case we are showing any.
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }

                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                        //This method will be called when the Auth proccess redirect to our RedirectUri.
                        //We will check the url looking for our RedirectUri.

                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (authorizationUrl.startsWith(REDIRECT_URI)) {
                            Log.i("Authorize", "");
                            Uri uri = Uri.parse(authorizationUrl);
                            //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                            //If not, that means the request may be a result of CSRF and must be rejected.
                            String stateToken = uri.getQueryParameter(STATE_PARAM);
                            if (stateToken == null || !stateToken.equals(STATE)) {
                                Log.e("Authorize", "State token doesn't match");
                                return true;
                            }

                            //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                            String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                            if (authorizationToken == null) {
                                Log.i("Authorize", "The user doesn't allow authorization.");
                                return true;
                            }
                            Log.i("Authorize", "Auth token received: " + authorizationToken);

                            //Generate URL for requesting Access Token
                            String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                            //We make the request in a AsyncTask
                            new PostRequestAsyncTask().execute(accessTokenUrl);

                        } else {
                            //Default behaviour
                            Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                            webView.loadUrl(authorizationUrl);
                        }
                        return true;
                    }
                });

                //Get the authorization Url
                String authUrl = getAuthorizationUrl();
                Log.i("Authorize", "Loading Auth Url: " + authUrl);
                //Load the authorization URL into the webView
                webView.loadUrl(authUrl);

                alertDialog.show();

            }
        });


    }

    private void Fatchtwitter_email() {

        mTwitterAuthClient.requestEmail(session1, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                Log.i(TAG, " EMAIL EXITO!");
                twitter_email = result.data;
                Log.i(TAG, "RESULT:" + result.data);

                if (twitter_email.length() != 0) {


                    try {

                        Log.d(TAG, twitter_name + "\n" + twitter_email + "\n" + twitter_Id + "\n" + twitter_profilePic);

                        social_user_data.put("name", twitter_name);
                        social_user_data.put("email", twitter_email);
                        social_user_data.put("type", "twitter");
                        social_user_data.put("type_id", twitter_Id);
                        social_user_data.put("image", twitter_profilePic);
                        social_user_data.put("gcm_id", 1);

                        FB_Login fbLogin = new FB_Login();
                        fbLogin.execute();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Sign_Up.this, "Sorry Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.i(TAG, "EMAIL FAILURE");
                Toast.makeText(Sign_Up.this, "Please Click on Allow For Email Access", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private Bundle getFacebookData(JSONObject object) {


        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));

            if (object.has("picture"))
                bundle.putString("picture", object.getString("picture"));
            return bundle;
        } catch (JSONException e) {
            Log.d("Facebook", "Error parsing JSON");
        }

        return null;
    }

    private void findViews() {

        linearLayout = findViewById(R.id.ll_snackbar);
        ch_terms = findViewById(R.id.chk_terms);
        ed_name = findViewById(R.id.edt_signup_name);
        ed_mobile = findViewById(R.id.edt_signup_mobile);
        ed_email = findViewById(R.id.edt_signup_email);
        ed_password = findViewById(R.id.edt_signup_password);
        ed_password_again = findViewById(R.id.edt_signup_cpassword);

        btn_signup = findViewById(R.id.btn_signup_signup);
        fbLoginButton = findViewById(R.id.login_button_fb);
        // fbLoginButton.setReadPermissions(Arrays.asList("public_profile,email"));

        btn_twitter = findViewById(R.id.btn_login_twitter);
        btn_twitter1 = findViewById(R.id.btn_login_twitter1);
        btn_linkedin = findViewById(R.id.btn_login_linkedIn);


        btnSignIn = findViewById(R.id.btn_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //generateHashkey("com.inforaam.upscale");
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {

        callbackManager.onActivityResult(reqCode, resCode, i);

        if (reqCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(i);
            handleSignInResult(result);
        }

        btn_twitter.onActivityResult(reqCode, resCode, i);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            if (isNetworkAvailable()) {

                try {

                    GoogleSignInAccount acct = result.getSignInAccount();

                    String personName = acct.getDisplayName();

                    String personPhotoUrl = String.valueOf(acct.getPhotoUrl());
                    Log.d("Photo", personPhotoUrl);
                    if (personPhotoUrl.equals("null")) {
                        personPhotoUrl = "";
                    }


                    String email = acct.getEmail();
                    String google_id = acct.getId();


                    social_user_data.put("name", personName);
                    social_user_data.put("email", email);
                    social_user_data.put("type", "google");
                    social_user_data.put("type_id", google_id);
                    social_user_data.put("image", personPhotoUrl);
                    social_user_data.put("gcm_id", 1);

                    FB_Login fbLogin = new FB_Login();
                    fbLogin.execute();

                } catch (JSONException e) {
                    //Logger.logError(e);
                }
            } else {
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //setMobileDataEnabled(Login.this);

                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);

                snackbar.show();

            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {

        Intent i_navigation = new Intent(Sign_Up.this, NavigationActivity.class);
        finish();
        startActivity(i_navigation);

        super.onBackPressed();
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Sign_Up.this, "", "Please Wait...", true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if (urls.length > 0) {
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(url);
                try {
                    HttpResponse response = httpClient.execute(httpost);
                    if (response != null) {
                        //If status is OK 200
                        if (response.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            JSONObject resultJson = new JSONObject(result);
                            //Extract data from JSON Response
                            int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;
                            accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;

                            if (expiresIn > 0 && accessToken != null) {
                                Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                                //Calculate date of expiration
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.SECOND, expiresIn);
                                long expireDate = calendar.getTimeInMillis();

                                //Store both expires in and access token in shared preferences
                                SharedPreferences preferences = getSharedPreferences("user_info", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("expires", expireDate);
                                editor.putString("accessToken", accessToken);
                                editor.apply();

                                return true;
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (status) {
                //If everything went Ok, change to another activity.
                /*Intent startProfileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                MainActivity.this.startActivity(startProfileActivity);*/
                String profileUrl = getProfileUrl(accessToken);
                new GetProfileRequestAsyncTask().execute(profileUrl);
            }
        }
    }

    private class GetProfileRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Sign_Up.this, "", "Please Wait...", true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            if (urls.length > 0) {
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                httpget.setHeader("x-li-format", "json");
                try {
                    HttpResponse response = httpClient.execute(httpget);
                    if (response != null) {
                        //If status is OK 200
                        if (response.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            return new JSONObject(result);
                        }
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            if (data != null) {

                try {

                    String Name = data.getString("firstName");
                    String Email = data.getString("emailAddress");
                    String ID = data.getString("id");
                    String userpic = data.getString("pictureUrl");

                    social_user_data.put("name", Name);
                    social_user_data.put("email", Email);
                    social_user_data.put("type", "linkedin");
                    social_user_data.put("type_id", ID);
                    social_user_data.put("image", userpic);
                    social_user_data.put("gcm_id", 1);

                    FB_Login fbLogin = new FB_Login();
                    fbLogin.execute();

                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing json " + e.getLocalizedMessage());
                }
            }
        }
    }

    private class Task_signUp extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Log.i("request", data_signup.toString());

                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "registration.php", data_signup.toString());
                JSONObject jobj_login = new JSONObject(data_user);

                status1 = jobj_login.getString(status);

                if (status1.equals("1")) {

                    Log.d("signup", "successfully register");
                } else {
                    message1 = jobj_login.getString(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {

                ed_email.setText("");
                ed_password.setText("");
                ed_name.setText("");
                ed_mobile.setText("");
                finish();
                startActivity(new Intent(Sign_Up.this, Login.class));
                //Toast.makeText(Sign_Up.this, "Success", Toast.LENGTH_SHORT).show();
            } else {
                ed_email.setText("");
                ed_password.setText("");
                ed_name.setText("");
                ed_mobile.setText("");
                Toast.makeText(Sign_Up.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Sign_Up.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();

            super.onPreExecute();
        }
    }

    private class FB_Login extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String str_u_id, str_u_name, str_u_email, str_u_mobile, str_u_profile = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Sign_Up.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "social_login.php", social_user_data.toString());
                JSONObject jobj_login = new JSONObject(data_user);

                status1 = jobj_login.getString(status);

                if (status1.equals("1")) {

                    str_u_id = jobj_login.getString("user_id");
                    str_u_name = jobj_login.getString("user_name");
                    str_u_email = jobj_login.getString("user_email");
                    str_u_mobile = jobj_login.getString("user_mobile");
                    str_u_profile = jobj_login.getString("user_image");
                    str_esaal_cust_id = jobj_login.getString("esaal_customer_id");

                } else {
                    message1 = jobj_login.getString(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {

                ed_email.setText("");
                ed_password.setText("");
                session = new SessionManager(getApplicationContext());
                session.createLoginSession(str_u_id, str_u_name, str_u_email, str_u_mobile, str_u_profile, "1", str_esaal_cust_id);
                finish();
                startActivity(new Intent(Sign_Up.this, NavigationActivity.class));

            } else {
                Toast.makeText(Sign_Up.this, message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }
}
