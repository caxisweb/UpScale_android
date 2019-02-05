package sa.upscale.coworking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.HashMap;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    SessionManager sessionManager;
    HashMap<String, String> lang = new HashMap<>();

    int flag = 0;
    Location location;
    GoogleApiClient mGoogleApiClient;
    RelativeLayout linearLayout;
    final int REQUEST_LOCATION = 1;

    Locale myLocale;
    static String str_language_Code = "";

    static String str_defaluLangFlag = "0";

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        linearLayout = (RelativeLayout) findViewById(R.id.ll_snackbar);
        sessionManager = new SessionManager(SplashScreen.this);
        final int flag = sessionManager.checkLogin();

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            mGoogleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                        }
                    }).build();

            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {

                                status.startResolutionForResult(SplashScreen.this, REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                                e.printStackTrace();
                            }

                            break;
                        case LocationSettingsStatusCodes.SUCCESS:

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (isNetworkAvailable()) {

                                        // int flag = session.checskip();

                                        Handler mHandler = new Handler();
                                        mHandler.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                //start your activity here

                                                lang = sessionManager.getlanguageCode();
                                                str_language_Code = lang.get(SessionManager.user_languageCode);

                                                try {

                                                    if (str_language_Code == null) {

                                                        myLocale = new Locale("en");
                                                        Resources res = getResources();
                                                        DisplayMetrics dm = res.getDisplayMetrics();
                                                        Configuration conf = res.getConfiguration();
                                                        conf.locale = myLocale;
                                                        res.updateConfiguration(conf, dm);

                                                        str_language_Code = "1";
                                                        sessionManager.setLanguagestate(str_language_Code);
                                                        Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
                                                        startActivity(refresh);
                                                        finish();
                                                    } else if (str_language_Code.equals("1"))  //english
                                                    {
                                                        myLocale = new Locale("en");
                                                        Resources res = getResources();
                                                        DisplayMetrics dm = res.getDisplayMetrics();
                                                        Configuration conf = res.getConfiguration();
                                                        conf.locale = myLocale;
                                                        res.updateConfiguration(conf, dm);

                                                        str_language_Code = "1";
                                                        sessionManager.setLanguagestate(str_language_Code);
                                                        Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
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
                                                        sessionManager.setLanguagestate(str_language_Code);
                                                        Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
                                                        startActivity(refresh);
                                                        finish();

                                                    }
                                                }catch (Exception e){

                                                    e.printStackTrace();

                                                    myLocale = new Locale("en");
                                                    Resources res = getResources();
                                                    DisplayMetrics dm = res.getDisplayMetrics();
                                                    Configuration conf = res.getConfiguration();
                                                    conf.locale = myLocale;
                                                    res.updateConfiguration(conf, dm);

                                                    str_language_Code = "1";
                                                    sessionManager.setLanguagestate(str_language_Code);
                                                    Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
                                                    startActivity(refresh);
                                                    finish();
                                                }
                                                /*if (flag != 0) {
                                                    finish();
                                                    startActivity(new Intent(SplashScreen.this, NavigationActivity.class));
                                                } else {
                                                    //langugeDlg();
                                                    Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
                                                    startActivity(refresh);
                                                    finish();


                                                }
*/
                                            }

                                        }, 2000L);
                                    } else {

                                        checkNetworkConnection();
                                    }
                                }
                            }, 2000);


                            break;
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isNetworkAvailable()) {

                    //int flag = session.checskip();

                    lang = sessionManager.getlanguageCode();
                    str_language_Code = lang.get(SessionManager.user_languageCode);

                    try {


                        if (str_language_Code == null) {

                            myLocale = new Locale("en");
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            Configuration conf = res.getConfiguration();
                            conf.locale = myLocale;
                            res.updateConfiguration(conf, dm);

                            str_language_Code = "1";
                            sessionManager.setLanguagestate(str_language_Code);
                            Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
                            startActivity(refresh);
                            finish();

                        } else if (str_language_Code.equals("1"))  //english
                        {
                            myLocale = new Locale("en");
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            Configuration conf = res.getConfiguration();
                            conf.locale = myLocale;
                            res.updateConfiguration(conf, dm);

                            str_language_Code = "1";
                            sessionManager.setLanguagestate(str_language_Code);
                            Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
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
                            sessionManager.setLanguagestate(str_language_Code);
                            Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
                            startActivity(refresh);
                            finish();

                        }
                    }catch (Exception e){

                        myLocale = new Locale("en");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);

                        str_language_Code = "1";
                        sessionManager.setLanguagestate(str_language_Code);
                        Intent refresh = new Intent(SplashScreen.this, NavigationActivity.class);
                        startActivity(refresh);
                        finish();
                    }
                } else {

                    checkNetworkConnection();


                }
            }
        }, 2000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isNetworkAvailable()) {
            finish();
            startActivity(new Intent(SplashScreen.this, NavigationActivity.class));
        } else {
            checkNetworkConnection();
        }
    }

    public void checkNetworkConnection() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity");
                startActivity(intent);
                recreate();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void langugeDlg() {

        LayoutInflater li = LayoutInflater.from(SplashScreen.this);
        final View promptsView = li.inflate(R.layout.language_popup_layout, null);
        final RadioGroup radioGroup = promptsView.findViewById(R.id.rg_language);
        final RadioButton rb_english = promptsView.findViewById(R.id.rb_english);
        final RadioButton rb_arbi = promptsView.findViewById(R.id.rb_persian);
        Button btn_ok = promptsView.findViewById(R.id.btn_langOK);


        AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
        alert.setTitle(R.string.select_language);
        alert.setView(promptsView);
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() != -1) {

                    int selectlanguage = radioGroup.getCheckedRadioButtonId();
                    RadioButton button = promptsView.findViewById(selectlanguage);
                    String selectedlanguage = button.getText().toString();
                    // Toast.makeText(SplashScreen.this, "" + selectedlanguage, Toast.LENGTH_SHORT).show();


                    if (selectedlanguage.equals("English")) {

                        myLocale = new Locale("en");
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);

                        str_language_Code = "1";
                        Intent refresh = new Intent(SplashScreen.this, Login.class);
                        startActivity(refresh);
                        finish();

                        dialog.dismiss();
                    } else {

                        myLocale = new Locale("ar");  //arbi
                        Resources res = getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        Configuration conf = res.getConfiguration();
                        conf.locale = myLocale;
                        res.updateConfiguration(conf, dm);

                        str_language_Code = "2";
                        Intent refresh = new Intent(SplashScreen.this, Login.class);
                        startActivity(refresh);
                        finish();


                        dialog.dismiss();

                    }


                } else {
                    Toast.makeText(SplashScreen.this, "Please Select One Language", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
