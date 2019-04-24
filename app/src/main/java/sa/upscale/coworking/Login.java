package sa.upscale.coworking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import sa.upscale.coworking.fregment.Home_freg;

import static sa.upscale.coworking.SplashScreen.location;
import static sa.upscale.coworking.Utils.REDIRECT_URI;
import static sa.upscale.coworking.Utils.RESPONSE_TYPE_VALUE;
import static sa.upscale.coworking.Utils.STATE;
import static sa.upscale.coworking.Utils.STATE_PARAM;
import static sa.upscale.coworking.Utils.accessToken;
import static sa.upscale.coworking.Utils.getAccessTokenUrl;
import static sa.upscale.coworking.Utils.getAuthorizationUrl;
import static sa.upscale.coworking.Utils.getProfileUrl;


/**
 * Created by Piyush Patel on 12-Apr-17.
 */
public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    private static final String status = "status";
    private static final String message = "message";

    private static final String s_email = "email";
    private static final String s_password = "password";
    private static final String s_gcm_id = "gcm_id";
    private static final String TAG = "Twitter";
    private static final int RC_SIGN_IN = 1;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mr4zFMgZQtUh4iVGvuiEUvd0d";
    private static final String TWITTER_SECRET = "QskHYWA1kupQN28SKe1GIbBqFFE4SuiJGvEEnfMCBPrJyhDbZl";
    public static String mstr_locationAddress;
    public static String mstr_locationCity;
    String status1 = "0", message1 = "try Again";
    SessionManager session;
    JSONObject data_login = new JSONObject();
    JSONObject data_forgotPwd = new JSONObject();
    JSONObject data_chngPwd = new JSONObject();
    String mstr_email, mstr_pasword;
    String mstr_forgot_email, mstr_uId, mstr_chng_otp, mstr_chng_newpwd, mstr_chng_reNewpwd;
    String fb_name, fb_email, fb_id = "0";
    String temp_keepme = "0";
    ImageView fbLoginButton, btnSignIn, btn_linkedin, btn_twitter1;
    RelativeLayout linearLayout;
    JSONObject social_user_data = new JSONObject();
    String str_u_id, str_u_name, str_u_email, str_u_mobile, str_u_profile, str_esaal_cust_id;
    String fb_img = "";
    int login_flag = 0;
    String str_language_Code = "";
    Locale myLocale;
    TwitterAuthConfig authConfig;
    String twitter_name, twitter_email, twitter_Id, twitter_profilePic;
    TwitterAuthClient mTwitterAuthClient;
    TwitterSession session1;
    Activity activity;
    AlertDialog alertDialog;
    private EditText ed_email, ed_password;
    private Button btn_signin, btn_signup;
    private CheckBox ch_loginkeep;
    private ViewFlipper fliper_add;
    private AdView google_AdView;
    private LinearLayout btn_listurSpace;
    private TextView btn_forgotPwd;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private TwitterLoginButton btn_twitter;

    public static List<android.location.Address> getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<android.location.Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {

                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                mstr_locationAddress = cityName + "," + stateName + "," + countryName;
                mstr_locationCity = addresses.get(0).getLocality();
                //Log.d("LocationAddress", mstr_locationAddress+"\n"+addresses.get(0).getAddressLine(0));
                //Log.d("City", addresses.get(0).getLocality());
                return addresses;
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }
        return addresses;
    }

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
        activity = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FacebookSdk.sdkInitialize(getApplicationContext());
        //Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        try {

            Bundle b = getIntent().getExtras();
            login_flag = b.getInt("flag");

        } catch (Exception e) {
            login_flag = 0;
        }

        session = new SessionManager(Login.this);
        findViews();

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mstr_email = ed_email.getText().toString().trim();
                mstr_pasword = ed_password.getText().toString().trim();

                if (mstr_email.length() == 0) {
                    ed_email.setError("Please Enter Email");
                    ed_email.setFocusable(true);
                } else if (!mstr_email.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
                    /*[a-zA-Z0-9._-]+@[a-z]+.[a-z]+*/
                    ed_email.setError("Invalid Email Address");
                    ed_email.setFocusable(true);
                } else if (mstr_pasword.length() == 0) {
                    ed_password.setError("Please Enter Password");
                    ed_password.setFocusable(true);
                } else if (temp_keepme.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please checked Keep me Login", Toast.LENGTH_SHORT).show();
                } else {

                    if (isNetworkAvailable()) {


                        try {

                            data_login.put(s_email, mstr_email);
                            data_login.put(s_password, mstr_pasword);
                            data_login.put(s_gcm_id, "1");

                            Task_Login taskLogin = new Task_Login();
                            taskLogin.execute();

                        } catch (JSONException e) {
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
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Login.this, Sign_Up.class));

            }
        });

        btn_listurSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCountryName(Login.this, location.getLatitude(), location.getLongitude());

                Intent intent = new Intent(Login.this, List_ur_Space.class);
                intent.putExtra("type", "login");
                intent.putExtra("address", mstr_locationAddress);
                intent.putExtra("city", mstr_locationCity);
                startActivity(intent);

            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new
                FacebookCallback<LoginResult>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Facebook Login Successful!");
                        System.out.println("Logged in user Details : ");
                        System.out.println("--------------------------");
                        System.out.println("User ID  : " + loginResult.getAccessToken().getUserId() + "\n");

                        System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
                        //Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_LONG).show();

                        fb_id = loginResult.getAccessToken().getUserId();


                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {


                                        Log.i("LoginActivity", graphResponse.toString());
                                        // Get facebook data from login
                                        String email = jsonObject.optString("email");
                                        Log.d("Facebook", "email:\t" + email);
                                        Bundle bFacebookData = getFacebookData(jsonObject);

                                        fb_name = bFacebookData.getString("first_name") + " " + bFacebookData.getString("last_name");
                                        fb_email = bFacebookData.getString("email");
                                        String fb_id = bFacebookData.getString("idFacebook");
                                        fb_img = bFacebookData.getString("profile_pic");

                                        Log.d("FaceBook", fb_name + "\n Emial :\t" + fb_email + "\n" + fb_id + "\n" + fb_img);

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
                        //parameters.putString("fields", "id,name,email,gender, birthday,picture");
                        //parameters.putString("public_profile", "id,name,email");
                        parameters.putString("fields", "id, first_name, last_name, email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Login.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");

                    }

                    @Override
                    public void onError(FacebookException e) {
                        Toast.makeText(Login.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
                        System.out.println("Facebook Login failed!!");
                    }
                });

        btn_forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
                LayoutInflater inflater = Login.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.forgot_pwd, null);
                dialogBuilder.setView(dialogView);

                final EditText edt_email = dialogView.findViewById(R.id.edt_fprgotPwd_email);
                ImageView btn_close = dialogView.findViewById(R.id.btn_fprgotPwd_close);
                Button btn_send = dialogView.findViewById(R.id.btn_fprgotPwd_send);


                dialogBuilder.setCancelable(false);
                final AlertDialog b = dialogBuilder.create();
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mstr_forgot_email = edt_email.getText().toString().trim();

                        if (mstr_forgot_email.length() == 0) {
                            edt_email.setError("Please Enter Email Id");
                            edt_email.setFocusable(true);
                        } else {

                            if (isNetworkAvailable()) {

                                try {
                                    data_forgotPwd.put("email", mstr_forgot_email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Task_ForgotPassword task_forgotPassword = new Task_ForgotPassword();
                                task_forgotPassword.execute();

                                b.dismiss();
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
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();
                    }
                });

                b.show();

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(Login.this)
                        .enableAutoManage(Login.this, Login.this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email"));
            }
        });

        mTwitterAuthClient = new TwitterAuthClient();

        btn_twitter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTwitterAuthClient.authorize(Login.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
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
                //The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
               *//* TwitterSession session1 = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                Log.d("Twitterdata", result.data.toString());
                String msg = "@" + session1.getUserName() + " logged in! (#" + session1.getUserId() + ")";
                Log.d("TwitterData", session1.getUserName() + "\nuserId :->" + session1.getUserId());
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();*//*

                //Twitter.getApiClient(session).getAccountService().verifyCredentials()


                TwitterSession session1 =
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


                TwitterSession session = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

               *//* Log.i(TAG,"TOKEN TWITTER:" + token);
                Log.i(TAG,"SECRET TWITTER:" + secret);*//*

                TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        // Do something with the result, which provides the email address

                        twitter_email = result.data;
                        *//*Log.i(TAG,"RESULT:" + result.data);
         *//*
                        Log.i("Twitter Email :", result.data);
                        //seesf;

                        if (twitter_email.length() != 0) {


                            try {
                                Log.d(TAG, twitter_name + "\n" + twitter_email + "\n" + twitter_Id + "\n" + twitter_profilePic);

                                social_user_data.put("name", twitter_name);
                                social_user_data.put("email", twitter_email);
                                social_user_data.put("type", "twitter");
                                //social_user_data.put("type", "linkedin");
                                social_user_data.put("type_id", twitter_Id);
                                social_user_data.put("image", twitter_profilePic);
                                social_user_data.put("gcm_id", 1);

                                FB_Login fbLogin = new FB_Login();
                                fbLogin.execute();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(Login.this, "Sorry Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        Log.i(TAG, "EMAIL FAILURE");
                        Toast.makeText(Login.this, "Please Click on Allow For Email Access", Toast.LENGTH_SHORT).show();
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
                //Show a progress dialog to the user
                final ProgressDialog pd = ProgressDialog.show(Login.this, "", "Please Wait...", true);

                LayoutInflater inflater = LayoutInflater.from(Login.this);
                final View linkedview = inflater.inflate(R.layout.linkedin_webview, null);
                alertDialog = new AlertDialog.Builder(Login.this).create();
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

        ch_loginkeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    temp_keepme = "1";
                } else {
                    temp_keepme = "0";
                }
            }
        });


        if (Home_freg.add_url.size() > 0) {

            fliper_add.setVisibility(View.VISIBLE);
            google_AdView.setVisibility(View.GONE);

            fliper_add.removeAllViews();

            for (int i = 0; i < Home_freg.add_url.size(); i++) {

                View add_view = LayoutInflater.from(
                        Login.this).inflate(
                        R.layout.custome_addview, null);

                ImageView img_adv = add_view.findViewById(R.id.img_adv);

                Picasso.with(Login.this)
                        .load(Url_info.main_img + "advertise/" + Home_freg.add_img.get(i))
                        .error(R.drawable.addbaner)
                        .into(img_adv);

                final int k = i;

                img_adv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Home_freg.add_url.get(k).equals("")) {

                        } else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Home_freg.add_url.get(k)));
                            startActivity(browserIntent);
                        }
                    }
                });

                fliper_add.addView(add_view);
            }

            fliper_add.startFlipping();

        } else {


            fliper_add.setVisibility(View.GONE);
            google_AdView.setVisibility(View.VISIBLE);

            MobileAds.initialize(Login.this, getString(R.string.google_admobo_app_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            google_AdView.loadAd(adRequest);


        }

    }

    private void Fatchtwitter_email() {

        mTwitterAuthClient.requestEmail(session1, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                Log.i(TAG, " EMAIL EXITO!");
                twitter_email = result.data;
                Log.i(TAG, "RESULT:" + result.data);

                //seesf;

                if (twitter_email.length() != 0) {


                    try {
                        Log.d(TAG, twitter_name + "\n" + twitter_email + "\n" + twitter_Id + "\n" + twitter_profilePic);

                        social_user_data.put("name", twitter_name);
                        social_user_data.put("email", twitter_email);
                        social_user_data.put("type", "twitter");
                        //social_user_data.put("type", "linkedin");
                        social_user_data.put("type_id", twitter_Id);
                        social_user_data.put("image", twitter_profilePic);
                        social_user_data.put("gcm_id", 1);

                        FB_Login fbLogin = new FB_Login();
                        fbLogin.execute();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Login.this, "Sorry Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.i(TAG, "EMAIL FAILURE");
                Toast.makeText(Login.this, "Please Click on Allow For Email Access", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void changeLanguageDLG() {

        if (str_language_Code.equals("1"))  //english
        {
            myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            str_language_Code = "1";
            session.setLanguagestate(str_language_Code);
            Intent refresh = new Intent(Login.this, Login.class);
            startActivity(refresh);
            finish();

        } else {

            myLocale = new Locale("ar");  //arabic
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            str_language_Code = "2";
            session.setLanguagestate(str_language_Code);
            Intent refresh = new Intent(Login.this, Login.class);
            startActivity(refresh);
            finish();

        }

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


            return bundle;
        } catch (JSONException e) {
            Log.d("Facebook", "Error parsing JSON");
        }

        return null;
    }

    private void findViews() {

        linearLayout = findViewById(R.id.ll_snackbar);
        ch_loginkeep = findViewById(R.id.chk_loginKeepme);
        ed_email = findViewById(R.id.edt_login_email);
        ed_password = findViewById(R.id.edt_login_password);
        btn_forgotPwd = findViewById(R.id.tv_forgotPwd);
        btn_signin = findViewById(R.id.btn_login_signin);
        btn_signup = findViewById(R.id.btn_login_signup);
        btn_listurSpace = findViewById(R.id.btn_login_listurSpace);
        fbLoginButton = findViewById(R.id.login_button_fb);

        btn_twitter1 = findViewById(R.id.btn_login_twitter1);
        btn_twitter = findViewById(R.id.btn_login_twitter);

        btn_linkedin = findViewById(R.id.btn_login_linkedIn);

        btnSignIn = findViewById(R.id.btn_sign_in);

        fliper_add = findViewById(R.id.flipper_add);
        google_AdView = findViewById(R.id.adView);
    }

    public void getFbKeyHash(String packageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("YourKeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
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
    public void onBackPressed() {

    /*    myLocale = new Locale("en");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        str_language_Code = "1";
        session.setLanguagestate(str_language_Code);
*/
        finish();
        if (login_flag == 0) {
            startActivity(new Intent(Login.this, NavigationActivity.class));
        }

        super.onBackPressed();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        btn_twitter.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Login.this, "", "Please Wait...", true);
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
                String profileUrl = getProfileUrl(accessToken);
                new GetProfileRequestAsyncTask().execute(profileUrl);
            }
        }
    }

    private class GetProfileRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(Login.this, "", "Please Wait...", true);
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

    private class Task_changePwd extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {


            try {
                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "reset_password.php", data_chngPwd.toString());
                JSONObject jobj_login = new JSONObject(data_user);

                status1 = jobj_login.getString(status);

                if (status1.equals("1")) {

                    finish();
                    startActivity(new Intent(Login.this, Login.class));

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

                Toast.makeText(Login.this, "success", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(Login.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private class Task_ForgotPassword extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {


            try {
                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "forgot_password.php", data_forgotPwd.toString());
                JSONObject jobj_login = new JSONObject(data_user);

                status1 = jobj_login.getString(status);

                if (status1.equals("1")) {

                    mstr_uId = jobj_login.getString("user_id");

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

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
                LayoutInflater inflater = Login.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.change_pasword, null);
                dialogBuilder.setView(dialogView);

                final EditText ed_ch_otp = dialogView.findViewById(R.id.edt_changePwd_otp);
                final EditText ed_ch_newPwd = dialogView.findViewById(R.id.edt_changePwd_newPwd);
                final EditText ed_ch_re_newPwd = dialogView.findViewById(R.id.edt_changePwd_re_typenewPwd);
                ImageView btn_close = dialogView.findViewById(R.id.btn_fprgotPwd_close);
                Button btn_reset = dialogView.findViewById(R.id.btn_chngPwd_resetpwd);


                dialogBuilder.setCancelable(false);
                final AlertDialog b = dialogBuilder.create();
                btn_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mstr_chng_otp = ed_ch_otp.getText().toString().trim();
                        mstr_chng_newpwd = ed_ch_newPwd.getText().toString().trim();
                        mstr_chng_reNewpwd = ed_ch_re_newPwd.getText().toString().trim();

                        if (mstr_chng_otp.length() == 0) {
                            ed_ch_otp.setError("Please Enter OTP");
                            ed_ch_otp.setFocusable(true);
                        } else if (mstr_chng_newpwd.length() == 0) {
                            ed_ch_newPwd.setError("Please Enter Re Password");
                            ed_ch_newPwd.setFocusable(true);
                        } else if (mstr_chng_reNewpwd.length() == 0) {
                            ed_ch_re_newPwd.setError("Please Enter Re-Password");
                            ed_ch_re_newPwd.setFocusable(true);
                        } else {

                            if (isNetworkAvailable()) {
                                try {
                                    data_chngPwd.put("user_id", mstr_uId);
                                    data_chngPwd.put("otp", mstr_chng_otp);
                                    data_chngPwd.put("password", mstr_chng_newpwd);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Task_changePwd task_changePwd = new Task_changePwd();
                                task_changePwd.execute();
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
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();
                    }
                });

                b.show();

            } else {

                Toast.makeText(Login.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private class Task_Login extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {


            try {

                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "login.php", data_login.toString());

                Log.i("responce", data_user);

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

                session.createLoginSession(str_u_id, str_u_name, str_u_email, str_u_mobile, str_u_profile, str_language_Code, str_esaal_cust_id);
                finish();

                if (login_flag == 0) {
                    startActivity(new Intent(Login.this, NavigationActivity.class));
                }

            } else {
                ed_email.setText("");
                ed_password.setText("");
                Toast.makeText(Login.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private class FB_Login extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Postdata p_user = new Postdata();
                Log.d("Social", social_user_data.toString());
                String data_user = p_user.post(Url_info.main_url + "social_login.php", social_user_data.toString());
                JSONObject jobj_login = new JSONObject(data_user);

                status1 = jobj_login.getString(status);

                if (status1.equals("1")) {

                    str_u_id = jobj_login.getString("user_id");
                    str_u_name = jobj_login.getString("user_name");
                    str_u_email = jobj_login.getString("user_email");
                    str_u_mobile = jobj_login.getString("user_mobile");
                    str_u_profile = jobj_login.getString("user_image");
                    str_esaal_cust_id = jobj_login.getString("essal_customer_id");


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

                session.createLoginSession(str_u_id, str_u_name, str_u_email, str_u_mobile, str_u_profile, str_language_Code, str_esaal_cust_id);

                finish();
                if (login_flag == 0) {
                    startActivity(new Intent(Login.this, NavigationActivity.class));
                }

            } else {
                Toast.makeText(Login.this, message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }


}
