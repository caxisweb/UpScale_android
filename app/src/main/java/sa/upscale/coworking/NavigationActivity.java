package sa.upscale.coworking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
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
import com.google.android.gms.plus.PlusShare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import sa.upscale.coworking.Feedback.FeedBack;
import sa.upscale.coworking.MyHistory.My_History_Activity;
import sa.upscale.coworking.MyProfiles.EditProfile;
import sa.upscale.coworking.MyProfiles.WishList;
import sa.upscale.coworking.Notifications.Notification;
import sa.upscale.coworking.fregment.Home_freg;
import sa.upscale.coworking.fregment.Package_freg;

import static sa.upscale.coworking.SplashScreen.location;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int LOCATION_REQUEST_CODE = 111;
    private static final String status = "status";
    private static final String message = "message";

    public static String mstr_locationAddress;
    public static String mstr_locationCity;
    public static int backFlag = 0;
    final int REQUEST_LOCATION = 11;
    public CustomNavigationView navView;
    public DrawerLayout drawerLayout;
    String status1 = "0", message1 = "try Again";
    JSONObject data_fatchProfile = new JSONObject();
    android.support.v4.app.FragmentTransaction ft;

    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();
    HashMap<String, String> lang = new HashMap<>();
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout nav_profile, nav_wishList, nav_notification, nav_package, nav_about, nav_location, nav_chat, nav_chngLanguage, nav_feedback, nav_logOut, nav_list_space, nav_signup, nav_login;
    RelativeLayout nav_home, nav_history;
    ImageView img_facebook, img_twitter, img_google;
    String picpath_identify = "";
    Activity activity;
    String str_language_Code = "";
    Locale myLocale;
    TextView tv_swEnglish, tv_swArabic;
    ImageView img_profile, img_logo;
    TextView tv_userName;
    private GoogleApiClient mGoogleApiClient;

    //    public static List<android.location.Address> getCountryName(Context context, double latitude, double longitude) {
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
        setContentView(R.layout.activity_navigation);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        activity = this;

        sessionManager = new SessionManager(this);

        navView = findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.activity_main);

        navView.setHeaderView(getHeader(), 20);
        navView.setScrollState(CustomNavigationView.MENU_ITEM_SCROLLABLE);
        navView.setSelectionBackGround(getResources().getColor(R.color.trance));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerLayout.getChildAt(0).setTranslationX(slideOffset * drawerView.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        if (!isGPSEnabled(this)) {
            enableLocationService();
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Home_freg new_booking_activity = new Home_freg();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, new_booking_activity).commit();
        }
    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager lm = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private View getHeader() {

        View view = getLayoutInflater().inflate(R.layout.header, null);
        img_profile = view.findViewById(R.id.img_profile);
        img_logo = view.findViewById(R.id.img_logo);
        tv_userName = view.findViewById(R.id.tv_userName);


        tv_swEnglish = view.findViewById(R.id.tv_sw_english);
        tv_swArabic = view.findViewById(R.id.tv_sw_arabic);

        // setdefault_languageselected();

        tv_swArabic.setOnClickListener(this);
        tv_swEnglish.setOnClickListener(this);

        nav_home = view.findViewById(R.id.nav_home);

        nav_profile = view.findViewById(R.id.nav_profile);
        nav_wishList = view.findViewById(R.id.nav_wishlist);
        nav_notification = view.findViewById(R.id.nav_notification);
        nav_package = view.findViewById(R.id.nav_package);
        nav_about = view.findViewById(R.id.nav_aboutUs);
        nav_location = view.findViewById(R.id.nav_ourLocation);
        nav_chat = view.findViewById(R.id.nav_chat);
        nav_chngLanguage = view.findViewById(R.id.nav_language_change);
        nav_history = view.findViewById(R.id.nav_history);
        nav_feedback = view.findViewById(R.id.nav_feedBack);
        nav_signup = view.findViewById(R.id.nav_list_signup);
        nav_login = view.findViewById(R.id.nav_list_login);
        nav_logOut = view.findViewById(R.id.nav_logOut);
        nav_list_space = view.findViewById(R.id.nav_list_space);

        img_facebook = view.findViewById(R.id.btn_share_fb);
        img_twitter = view.findViewById(R.id.btn_share_twitter1);
        img_google = view.findViewById(R.id.btn_google_share);

        if (sessionManager.isLoggedIn()) {

            user_details = sessionManager.getUserDetails();

            tv_userName.setText(user_details.get(SessionManager.user_name));
            picpath_identify = user_details.get(SessionManager.user_profile).substring(0, 5);

            if (picpath_identify.equals("https")) {

                img_profile.setVisibility(View.VISIBLE);
                Glide.with(NavigationActivity.this).load(user_details.get(SessionManager.user_profile)).placeholder(R.drawable.logo1).error(R.drawable.logo1).into(img_profile);

            } else {

                //img_logo.setVisibility(View.GONE);
                img_profile.setVisibility(View.VISIBLE);

                Glide.with(NavigationActivity.this).load(Url_info.main_img + "profile/" + user_details.get(SessionManager.user_profile)).placeholder(R.drawable.logo1).error(R.drawable.logo1).into(img_profile);
            }

            nav_signup.setVisibility(View.GONE);
            nav_login.setVisibility(View.GONE);
        } else {

            nav_profile.setVisibility(View.GONE);
            nav_wishList.setVisibility(View.GONE);
            nav_history.setVisibility(View.GONE);
            nav_logOut.setVisibility(View.GONE);
            img_profile.setVisibility(View.GONE);
            img_logo.setVisibility(View.VISIBLE);

            Glide.with(NavigationActivity.this).load(R.drawable.logo1).into(img_logo);
        }

        nav_home.setOnClickListener(this);

        nav_profile.setOnClickListener(this);
        nav_notification.setOnClickListener(this);
        nav_package.setOnClickListener(this);
        nav_about.setOnClickListener(this);
        nav_location.setOnClickListener(this);
        nav_chat.setOnClickListener(this);
        nav_chngLanguage.setOnClickListener(this);
        nav_wishList.setOnClickListener(this);
        nav_feedback.setOnClickListener(this);
        nav_history.setOnClickListener(this);
        nav_list_space.setOnClickListener(this);
        nav_logOut.setOnClickListener(this);
        nav_signup.setOnClickListener(this);
        nav_login.setOnClickListener(this);

        lang = sessionManager.getlanguageCode();
        str_language_Code = lang.get(SessionManager.user_languageCode);
//        Log.i("language_code",str_language_Code);

        if (str_language_Code == null) {

            tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_swEnglish.setTextColor(getResources().getColor(R.color.white));
            tv_swArabic.setBackgroundColor(getResources().getColor(R.color.sw_color));
            tv_swArabic.setTextColor(getResources().getColor(R.color.black));
            str_language_Code = "1";
        } else if (str_language_Code.equals("1")) {

            str_language_Code = "1";
            tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_swEnglish.setTextColor(getResources().getColor(R.color.white));
            tv_swArabic.setBackgroundColor(getResources().getColor(R.color.sw_color));
            tv_swArabic.setTextColor(getResources().getColor(R.color.black));

        } else if (str_language_Code.equals("2")) {

            str_language_Code = "2";
            tv_swArabic.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_swArabic.setTextColor(getResources().getColor(R.color.white));
            tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.sw_color));
            tv_swEnglish.setTextColor(getResources().getColor(R.color.black));


        } else {

            str_language_Code = "1";
            tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_swEnglish.setTextColor(getResources().getColor(R.color.white));
            tv_swArabic.setBackgroundColor(getResources().getColor(R.color.sw_color));
            tv_swArabic.setTextColor(getResources().getColor(R.color.black));

        }

        img_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String str_msg = "Please explore available space and join the coworkers community now by booking a space from https://play.google.com/store/apps/details?id=sa.upscale.coworking";

                        ShareDialog shareDialog = new ShareDialog(NavigationActivity.this);
                        ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                                .putString("og:type", "books.book")
                                /*.putString("og:title", "A Game of Thrones")*/
                                .putString("og:description", str_msg)
                                .putString("books:isbn", "0-553-57340-3").build();

                        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                                .setActionType("books.reads").putObject("book", object)
                                .build();
                        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                                .setPreviewPropertyName("book")
                                .setAction(action)
                                .build();
                        shareDialog.show(content);

                    }
                });

            }
        });

        img_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_google.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String str_msg = "Please explore available space and join the coworkers community now by booking a space from https://play.google.com/store/apps/details?id=sa.upscale.coworking";
                        Intent shareIntent = new PlusShare.Builder(NavigationActivity.this)
                                .setType("text/plain")
                                .setText(str_msg)
                                .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                                .getIntent();

                        startActivityForResult(shareIntent, 0);

                    }
                });

            }
        });

        img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAppInstalled = appInstalledOrNot("com.twitter.android");
                Intent intent = new Intent();

                String str_msg = "Please explore available space and join the coworkers community now by booking a space from https://play.google.com/store/apps/details?id=sa.upscale.coworking";

                if (isAppInstalled) {

                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, str_msg);
                    intent.setType("text/plain");
                    // intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/jpeg");
                    intent.setPackage("com.twitter.android");


                } else {
                    // Do whatever we want to do if application not installed
                    // For example, Redirect to play store

                    String tweetUrl = "https://twitter.com/intent/tweet?text=" + str_msg;
                    Uri uri = Uri.parse(tweetUrl);
                    intent = new Intent(Intent.ACTION_VIEW, uri);

                }
                startActivity(intent);

            }
        });

        return view;
    }

    private void setdefault_languageselected() {

        String str_lanStatus = "1";

        if (sessionManager.isLoggedIn()) {
            str_lanStatus = user_details.get(SessionManager.user_languageCode);
        } else {
            str_lanStatus = "1";
        }

        if (str_lanStatus == null) {

            Log.d("status", "sdfsdf" + str_lanStatus);
            str_language_Code = "1";
            tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tv_swEnglish.setTextColor(getResources().getColor(R.color.white));
            tv_swArabic.setBackgroundColor(getResources().getColor(R.color.sw_color));
            tv_swArabic.setTextColor(getResources().getColor(R.color.black));

            changeLanguageDLG();

        } else {

            Log.d("status", str_lanStatus);

            if (str_lanStatus.equals("1")) {
                str_language_Code = "1";
                tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.sw_lang_drawer));
                tv_swEnglish.setTextColor(getResources().getColor(R.color.white));
                tv_swArabic.setBackgroundColor(getResources().getColor(R.color.sw_color));
                tv_swArabic.setTextColor(getResources().getColor(R.color.black));
            } else {
                //str_language_Code = "2";
                tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.sw_color));
                tv_swEnglish.setTextColor(getResources().getColor(R.color.black));
                tv_swArabic.setBackgroundColor(getResources().getColor(R.color.sw_lang_drawer));
                tv_swArabic.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.nav_profile:

                EditProfile editProfile = new EditProfile();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, editProfile).commit();

                break;

            case R.id.nav_wishlist:

                WishList wishList = new WishList();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, wishList).commit();
                break;

            case R.id.nav_notification:

                Notification notification = new Notification();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, notification).commit();

                break;


            case R.id.nav_package:

                Package_freg packg_freg = new Package_freg();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, packg_freg).commit();

                break;

            case R.id.nav_aboutUs:

                AboutUs aboutUs = new AboutUs();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, aboutUs).commit();
                break;

            case R.id.nav_ourLocation:

                New_Booking_activity home4 = new New_Booking_activity();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, home4).commit();


                break;

            case R.id.nav_home:

                // startActivity(new Intent(NavigationActivity.this,Invitation.class));
                Home_freg home = new Home_freg();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, home).commit();

                break;

            case R.id.nav_history:
                My_History_Activity history = new My_History_Activity();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, history).commit();
                break;

            case R.id.nav_language_change:
                //changeLanguageDlg();
                break;

            case R.id.nav_feedBack:
                FeedBack feedBack = new FeedBack();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, feedBack).commit();

                break;

            case R.id.nav_list_space:

                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                getCountryName(this, location.getLatitude(), location.getLongitude());

                Intent intent = new Intent(NavigationActivity.this, List_ur_Space.class);
                intent.putExtra("type", "login");
                intent.putExtra("lati", location.getLatitude());
                intent.putExtra("longi", location.getLongitude());
                intent.putExtra("address", mstr_locationAddress);
                intent.putExtra("city", mstr_locationCity);
                startActivity(intent);

                break;
            case R.id.nav_list_signup:

                finish();
                startActivity(new Intent(NavigationActivity.this, Sign_Up.class));

                break;
            case R.id.nav_list_login:

                Intent i_login = new Intent(NavigationActivity.this, Login.class);
                i_login.putExtra("flag", 0);
                finish();
                startActivity(i_login);

                break;
            case R.id.nav_logOut:

                finish();
                sessionManager.logoutUser();

                break;
            case R.id.tv_sw_english:

                str_language_Code = "1";
                tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_swEnglish.setTextColor(getResources().getColor(R.color.white));
                tv_swArabic.setBackgroundColor(getResources().getColor(R.color.sw_color));
                tv_swArabic.setTextColor(getResources().getColor(R.color.black));

                changeLanguageDLG();

                break;
            case R.id.tv_sw_arabic:

                str_language_Code = "2";
                tv_swArabic.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_swArabic.setTextColor(getResources().getColor(R.color.white));
                tv_swEnglish.setBackgroundColor(getResources().getColor(R.color.sw_color));
                tv_swEnglish.setTextColor(getResources().getColor(R.color.black));

                changeLanguageDLG();

                break;

        }

        drawerLayout.closeDrawers();
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
            sessionManager.setLanguagestate(str_language_Code);

            /*Home_freg new_booking_activity = new Home_freg();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, new_booking_activity).commit();
*/
            Intent refresh = new Intent(NavigationActivity.this, NavigationActivity.class);
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

            Intent refresh = new Intent(NavigationActivity.this, NavigationActivity.class);
            startActivity(refresh);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (backFlag == 0) {

            if (doubleBackToExitPressedOnce) {

                backFlag = 0;

                myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);

                str_language_Code = "1";
                sessionManager.setLanguagestate(str_language_Code);

                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {

            Home_freg new_booking_activity = new Home_freg();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, new_booking_activity).commit();

        }
    }

    private void langugeDlg() {

        if (sessionManager.isLoggedIn()) {
            str_language_Code = user_details.get(SessionManager.user_languageCode);
        } else {
            str_language_Code = "1";
        }

        if (str_language_Code.equals("1")) {

            myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            //str_language_Code = "1";

        } else {
            myLocale = new Locale("ar");  //arbi
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            // str_language_Code = "2";

        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void enableLocationService() {

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(NavigationActivity.this, REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                                e.printStackTrace();
                            }

                            break;
                        case LocationSettingsStatusCodes.SUCCESS:
                            if (ActivityCompat.checkSelfPermission(NavigationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NavigationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            Home_freg new_booking_activity = new Home_freg();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.mainContent, new_booking_activity).commit();
                            break;
                    }
                }
            });
        }

    }

    @SuppressLint("MissingPermission")
    private void requestLocationPermission() {

        // BEGIN_INCLUDE(Location_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(NavigationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(NavigationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

            ActivityCompat.requestPermissions(NavigationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);

            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            Home_freg new_booking_activity = new Home_freg();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, new_booking_activity).commit();

        } else {

            // Location permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(NavigationActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_CANCELED:
                    enableLocationService();
                    break;
                case Activity.RESULT_OK:
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    Home_freg new_booking_activity = new Home_freg();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainContent, new_booking_activity).commit();
                    break;
                default:
                    break;

            }
        } else {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            Home_freg new_booking_activity = new Home_freg();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, new_booking_activity).commit();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Home_freg new_booking_activity = new Home_freg();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainContent, new_booking_activity).commit();

                }
            }, 500);
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_option_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_filter) {

        }

        return super.onOptionsItemSelected(item);

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    private class Task_getProfileDetails extends AsyncTask<String, String, String> {

        String mstr_name, mstr_phone, mstr_email, mstr_userType, mstr_img;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {

                data_fatchProfile.put("user_id", user_details.get(SessionManager.user_Id));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                Postdata postdata = new Postdata();
                Log.d("Profile", data_fatchProfile.toString());
                String str_dettails = postdata.post(Url_info.main_url + "my_profile.php", data_fatchProfile.toString());
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {

                    mstr_name = job_details.getString("name");
                    mstr_phone = job_details.getString("phone");

                    mstr_email = job_details.getString("email");
                    mstr_img = job_details.getString("image");
                    mstr_userType = job_details.getString("user_type");


                } else {
                    message1 = job_details.getString(message);
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

                try {

                    tv_userName.setText(mstr_name);
                    picpath_identify = mstr_img.substring(0, 5);

                    if (picpath_identify.equals("https")) {

                        //img_profile.setVisibility(View.GONE);
                        img_profile.setVisibility(View.VISIBLE);
                        Glide.with(NavigationActivity.this).load(mstr_img).placeholder(R.drawable.logo1).into(img_profile);

                    } else {

                        //img_logo.setVisibility(View.GONE);
                        img_profile.setVisibility(View.VISIBLE);
                        Log.i("img_url", mstr_img);
                        Glide.with(NavigationActivity.this).load(Url_info.main_img + "profile/" + mstr_img).placeholder(R.drawable.logo1).into(img_profile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(NavigationActivity.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

    }
}
