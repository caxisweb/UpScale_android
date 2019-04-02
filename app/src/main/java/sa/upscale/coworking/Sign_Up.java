package sa.upscale.coworking;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class Sign_Up extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String status = "status";
    private static final String message = "message";

    private static final String s_email = "email";
    private static final String s_password = "password";
    private static final String s_name = "name";
    private static final String s_mobile = "mobile";
    private static final String TAG = "Twitter_error";

    String status1 = "0", message1 = "try Again";

    JSONObject data_signup = new JSONObject();
    String fb_name, fb_email, fb_id = "0", fb_image = "0";
    JSONObject fb_user_data = new JSONObject();

    private EditText ed_name, ed_mobile, ed_email, ed_password,ed_password_again;
    private Button btn_signup;
    private String mstr_name, mstr_mobile, mstr_email, mstr_password,str_esaal_cust_id,mstr_password_again;
    private CallbackManager callbackManager;

    HashMap<String, String> language_code = new HashMap<>();

    CheckBox ch_terms;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1;

    JSONObject social_user_data = new JSONObject();

    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host
            + "/v1/people/~:" +
            "(id,email-address,formatted-name,phone-numbers,picture-urls::(original))";

    String fb_img = "";

    TwitterAuthConfig authConfig;
    TwitterAuthClient mTwitterAuthClient;
    TwitterSession session1;
    String twitter_name, twitter_email, twitter_Id, twitter_profilePic;


    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    LinearLayout linearLayout;

    SessionManager session;
    HashMap<String, String> userDetails = new HashMap<>();

    ImageView fbLoginButton, btnSignIn, btn_linkedin, btn_twitter1;

    private static final String TWITTER_KEY = "mr4zFMgZQtUh4iVGvuiEUvd0d";
    private static final String TWITTER_SECRET = "QskHYWA1kupQN28SKe1GIbBqFFE4SuiJGvEEnfMCBPrJyhDbZl";

    private TwitterLoginButton btn_twitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


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

                String str= String.valueOf(mstr_mobile.startsWith("05"));

                if (mstr_name.length() == 0) {
                    ed_name.setError("Please Enter Name");
                    ed_name.setFocusable(true);
                } else if (mstr_mobile.length() == 0) {
                    ed_mobile.setError("Please Enter Contact No.");
                    ed_mobile.setFocusable(true);
                }else if(str.equals("false")){
                    ed_mobile.setError("Number Start With 05");
                    ed_mobile.setFocusable(true);
                    ed_mobile.requestFocus();
                }
                else if (mstr_mobile.length() != 10) {
                    ed_mobile.setError("Please Enter Correct Number");
                    ed_mobile.setFocusable(true);
                } else if (mstr_email.length() == 0) {
                    ed_email.setError("Please Enter Email Id");
                    ed_email.setFocusable(true);
                } else if (!mstr_email.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
                    ed_email.setError("Invalid Email Address");
                    ed_email.setFocusable(true);
                } else if (mstr_password.length() == 0) {
                    ed_password.setError("Please Enter Password");
                    ed_password.setFocusable(true);
                }else if(!mstr_password.equals(mstr_password_again)){
                    ed_password_again.setError("Password Does not match");
                }
                else if (!ch_terms.isChecked()) {
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

                mTwitterAuthClient.authorize(Sign_Up.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        // Success
                        session1 = Twitter.getSessionManager().getActiveSession();

                        Call<User> userResult = Twitter.getApiClient(session1).getAccountService().verifyCredentials(true, false);
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


        btn_twitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                /*TwitterSession session1 =
                        Twitter.getSessionManager().getActiveSession();
                Call<User> userResult = Twitter.getApiClient(session1).getAccountService().verifyCredentials(true, false);
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

                            Log.d(TAG, "+aa\n" + twitter_profilePic + "\n" + twitter_name + "\n" + twitter_Id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                });
*/

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
        });


        btn_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LISessionManager.getInstance(getApplicationContext())
                        .init(Sign_Up.this, buildScope(), new AuthListener() {
                            @Override
                            public void onAuthSuccess() {

                                APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
                                apiHelper.getRequest(Sign_Up.this, url, new ApiListener() {
                                    @Override
                                    public void onApiSuccess(ApiResponse result) {

                                        if (isNetworkAvailable()) {

                                            try {

                                                String Name = result.getResponseDataAsJson().get("formattedName").toString();
                                                String Email = result.getResponseDataAsJson().get("emailAddress").toString();
                                                String ID = result.getResponseDataAsJson().get("id").toString();

                                                URI uri = new URI("http://api.linkedin.com/v1/people/" + ID + "/picture-url");
                                                String userpic = uri.toString();
                                                Log.d("UrlPic", userpic);

                                            /*JSONObject object = new JSONObject("pictureUrl");

                                            String ProfileImage = object.getString("values");*/

                                                social_user_data.put("name", Name);
                                                social_user_data.put("email", Email);
                                                social_user_data.put("type", "linkedin");
                                                social_user_data.put("type_id", ID);
                                                social_user_data.put("image", userpic);
                                                social_user_data.put("gcm_id", 1);

                                                FB_Login fbLogin = new FB_Login();
                                                fbLogin.execute();

                                                //showResult(result.getResponseDataAsJson());

                                            } catch (Exception e) {
                                                e.printStackTrace();
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

                                    @Override
                                    public void onApiError(LIApiError error) {
                                        Log.i("linkedin_error", error.toString());
                                    }
                                });
                            }

                            @Override
                            public void onAuthError(LIAuthError error) {

                                Toast.makeText(getApplicationContext(), "failed "
                                                + error.toString(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }, true);
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

        linearLayout = (LinearLayout) findViewById(R.id.ll_snackbar);
        ch_terms = (CheckBox) findViewById(R.id.chk_terms);
        ed_name = (EditText) findViewById(R.id.edt_signup_name);
        ed_mobile = (EditText) findViewById(R.id.edt_signup_mobile);
        ed_email = (EditText) findViewById(R.id.edt_signup_email);
        ed_password = (EditText) findViewById(R.id.edt_signup_password);
        ed_password_again = (EditText) findViewById(R.id.edt_signup_cpassword);

        btn_signup = (Button) findViewById(R.id.btn_signup_signup);
        fbLoginButton = (ImageView) findViewById(R.id.login_button_fb);
        // fbLoginButton.setReadPermissions(Arrays.asList("public_profile,email"));

        btn_twitter = (TwitterLoginButton) findViewById(R.id.btn_login_twitter);
        btn_twitter1 = (ImageView) findViewById(R.id.btn_login_twitter1);
        btn_linkedin = (ImageView) findViewById(R.id.btn_login_linkedIn);


        btnSignIn = (ImageView) findViewById(R.id.btn_sign_in);

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

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, reqCode, resCode, i);

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

                /*if (!acct.getPhotoUrl().equals(null)) {
                    personPhotoUrl = acct.getPhotoUrl().toString();
                }*/
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

    private class Task_signUp extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Log.i("request",data_signup.toString());

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
                    str_esaal_cust_id=jobj_login.getString("esaal_customer_id");

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
                session.createLoginSession(str_u_id, str_u_name, str_u_email, str_u_mobile, str_u_profile, "1",str_esaal_cust_id);
                finish();
                startActivity(new Intent(Sign_Up.this, NavigationActivity.class));

            } else {
                Toast.makeText(Sign_Up.this, message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onBackPressed() {

        Intent i_navigation=new Intent(Sign_Up.this,NavigationActivity.class);
        finish();
        startActivity(i_navigation);

        super.onBackPressed();
    }
}
