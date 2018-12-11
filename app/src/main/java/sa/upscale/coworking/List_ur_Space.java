package sa.upscale.coworking;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sa.upscale.coworking.fregment.Home_freg;

public class List_ur_Space extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String status = "status";
    private static final String message = "message";
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;

    String status1 = "0", message1 = "try Again";

    ArrayList<String> ar_capacity = new ArrayList<>();
    ArrayList<String> ar_avalibility = new ArrayList<>();

    private ArrayList<String> ar_city = new ArrayList<>();
    private ArrayList<String> ar_cityid = new ArrayList<>();

    ArrayList<String> ar_capacity2 = new ArrayList<>();
    ArrayList<String> ar_avalibility2 = new ArrayList<>();

    ArrayList<String> ar_capacity3 = new ArrayList<>();
    ArrayList<String> ar_avalibility3 = new ArrayList<>();

    ArrayList<String> space_type = new ArrayList<>();

    JSONObject data_space = new JSONObject();

    String mstr_type = "0", mstr_name, mstr_email, mstr_mobile, mstr_cap1, mstr_avali1, mstr_cap2 = "0", mstr_avali2 = "0", mstr_cap3 = "0", mstr_avali3 = "0", mstr_cityid;
    String mstr_projector, mstr_aircondi, mstr_mailservice, mstr_scanner, mstr_locker, mstr_internet,
            mstr_parking, mstr_phone, mstr_work, mstr_price, mstr_male, mstr_female, mstr_coffee;
    String flag = "0";

    //private FrameLayout btn_meetingroom, btn_desk, btn_discuroom, btn_privateroom, btn_conferenceroom,btn_others;
    private EditText sp_cpacity, sp_avalibility, ed_price;
    private Button btn_location;
    private EditText ed_name, ed_email, ed_mobile;
    private CheckBox chk_projector, chk_aircondi, chk_mailservice, chk_scanner, chk_locker, chk_internet,
            chk_parking, chk_phone, chk_work, chk_male, chk_female, chk_coffee;
    private Button btn_submit;
    private ImageView img_1, img_2, img_3, img_add1, img_add2, img_add3;
    private TextView tv_displayLocation;

    private int j = 0, b = 0;
    private String longitude = "0", latitude = "0";

    static int lat_flag = 0;
    Activity activity;
    List<Address> listAddress;
    Location location;
    GoogleApiClient mGoogleApiClient;

    final int REQUEST_LOCATION = 111;
    private static final int LOCATION_REQUEST_CODE = 121;
    private static String mstr_locationAddress, mstr_locationCity;
    private String str_img_path1 = "0", str_img_path2 = "0", str_img_path3 = "0", mstr_spaceId, img_flag = "0";

    TextView img_back;
    LinearLayout linearLayout;
    RadioGroup rg_gender;
    Spinner sp_space_type, sp_city;

    String str_lat = "0", str_lng = "0", str_address, str_city;
    String str_type_location;
    Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_list_ur__space);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        activity = this;

        findViews();

        Intent intent = getIntent();
        str_type_location = intent.getStringExtra("type");
        str_lat = String.valueOf(NavigationActivity.location.getLatitude());
        str_lng = String.valueOf(NavigationActivity.location.getLongitude());

        if (str_type_location.equals("login")) {

            str_address = intent.getStringExtra("address");
            str_city = intent.getStringExtra("city");
            tv_displayLocation.setText(str_address);
            //Toast.makeText(activity, "Current", Toast.LENGTH_SHORT).show();
        } else {

            str_address = intent.getStringExtra("address");
            str_city = intent.getStringExtra("city");
            tv_displayLocation.setText(str_address);

        }

        ar_avalibility.clear();
        ar_capacity.clear();

        ar_avalibility2.clear();
        ar_capacity2.clear();

        ar_avalibility3.clear();
        ar_capacity3.clear();

        space_type.add(getResources().getString(R.string.select_space_type));
        space_type.add(getResources().getString(R.string.meeting_room));
        space_type.add(getResources().getString(R.string.desk));
        //space_type.add(getResources().getString(R.string.discussion_room));
        space_type.add(getResources().getString(R.string.private_room));
        space_type.add(getResources().getString(R.string.conference_room));
        space_type.add(getResources().getString(R.string.others));

        ArrayAdapter<String> type_adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.custome_spiner_view, R.id.tv_item, space_type);
        //ArrayAdapter type_adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, space_type);
        sp_space_type.setAdapter(type_adp);

        sp_space_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    if(i>2){
                        mstr_type = String.valueOf(i+1);
                    }else {
                        mstr_type = String.valueOf(i);
                    }
                } else {
                    mstr_type = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    mstr_cityid = ar_cityid.get(i);
                } else {
                    mstr_cityid = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        boolean c_pro = chk_projector.isChecked();

        if (c_pro == true) {
            mstr_projector = "1";
        } else {
            mstr_projector = "0";
        }

        boolean c_ac = chk_aircondi.isChecked();
        if (c_ac == true) {
            mstr_aircondi = "1";
        } else {
            mstr_aircondi = "0";
        }

        boolean c_mail = chk_mailservice.isChecked();
        if (c_mail == true) {
            mstr_mailservice = "1";
        } else {
            mstr_mailservice = "0";
        }

        boolean c_sc = chk_scanner.isChecked();
        if (c_sc == true) {
            mstr_scanner = "1";
        } else {
            mstr_scanner = "0";
        }
        boolean c_lc = chk_locker.isChecked();
        if (c_lc == true) {
            mstr_locker = "1";
        } else {
            mstr_locker = "0";
        }
        boolean c_internet = chk_internet.isChecked();
        if (c_internet == true) {
            mstr_internet = "1";
        } else {
            mstr_internet = "0";
        }

        boolean c_parking = chk_parking.isChecked();
        if (c_parking == true) {
            mstr_parking = "1";
        } else {
            mstr_parking = "0";
        }
        boolean c_phone = chk_phone.isChecked();
        if (c_phone == true) {
            mstr_phone = "1";
        } else {
            mstr_phone = "0";
        }
        boolean c_work = chk_work.isChecked();
        if (c_work == true) {
            mstr_work = "1";
        } else {
            mstr_work = "0";
        }
        boolean c_man = chk_male.isChecked();
        if (c_man == true) {
            mstr_male = "1";
        } else {
            mstr_male = "0";
        }
        boolean c_woman = chk_female.isChecked();
        if (c_woman == true) {
            mstr_female = "1";
        } else {
            mstr_female = "0";
        }
        boolean c_coffee = chk_coffee.isChecked();
        if (c_coffee == true) {
            mstr_coffee = "1";
        } else {
            mstr_coffee = "0";
        }

        Task_getLocation1 t_location = new Task_getLocation1();
        t_location.execute();

    }


    private void findViews() {

        linearLayout = (LinearLayout) findViewById(R.id.ll_snackbar);
        img_back = (TextView) findViewById(R.id.img_back_listurSpace);

        sp_cpacity = (EditText) findViewById(R.id.sp_space_capacity);
        sp_avalibility = (EditText) findViewById(R.id.sp_space_avalibility);

        btn_location = (Button) findViewById(R.id.btn_space_location);

        ed_name = (EditText) findViewById(R.id.edt_space_name);
        ed_email = (EditText) findViewById(R.id.edt_space_email);
        ed_mobile = (EditText) findViewById(R.id.edt_space_mobileNumber);

        chk_projector = (CheckBox) findViewById(R.id.chk_space_projector);
        chk_aircondi = (CheckBox) findViewById(R.id.chk_space_air_condi);
        chk_mailservice = (CheckBox) findViewById(R.id.chk_space_mailservice);

        chk_scanner = (CheckBox) findViewById(R.id.chk_space_scanner);
        chk_locker = (CheckBox) findViewById(R.id.chk_space_locker);
        chk_internet = (CheckBox) findViewById(R.id.chk_space_internet);

        chk_parking = (CheckBox) findViewById(R.id.chk_space_parking);
        chk_phone = (CheckBox) findViewById(R.id.chk_space_phone);
        chk_work = (CheckBox) findViewById(R.id.chk_space_work);

        chk_male = (CheckBox) findViewById(R.id.chk_space_Man);
        chk_female = (CheckBox) findViewById(R.id.chk_space_female);
        chk_coffee = (CheckBox) findViewById(R.id.chk_space_coffee);

        rg_gender = (RadioGroup) findViewById(R.id.rg_listurSpace_gender);
        ed_price = (EditText) findViewById(R.id.edt_space_price);

        tv_displayLocation = (TextView) findViewById(R.id.tv_listUrSpace_CurrentLocation);

        img_1 = (ImageView) findViewById(R.id.img_space_1);
        img_2 = (ImageView) findViewById(R.id.img_space_2);
        img_3 = (ImageView) findViewById(R.id.img_space_3);
        img_add1 = (ImageView) findViewById(R.id.img_space_add1);
        img_add2 = (ImageView) findViewById(R.id.img_space_add2);
        img_add3 = (ImageView) findViewById(R.id.img_space_add3);

        btn_submit = (Button) findViewById(R.id.btn_space_submit);

        sp_space_type = (Spinner) findViewById(R.id.sp_type);
        sp_city = (Spinner) findViewById(R.id.sp_city);


        btn_submit.setOnClickListener(List_ur_Space.this);

        btn_location.setOnClickListener(List_ur_Space.this);

        chk_projector.setOnCheckedChangeListener(List_ur_Space.this);
        chk_aircondi.setOnCheckedChangeListener(List_ur_Space.this);
        chk_mailservice.setOnCheckedChangeListener(List_ur_Space.this);

        chk_scanner.setOnCheckedChangeListener(List_ur_Space.this);
        chk_locker.setOnCheckedChangeListener(List_ur_Space.this);
        chk_internet.setOnCheckedChangeListener(List_ur_Space.this);

        chk_parking.setOnCheckedChangeListener(List_ur_Space.this);
        chk_phone.setOnCheckedChangeListener(List_ur_Space.this);
        chk_work.setOnCheckedChangeListener(List_ur_Space.this);

        chk_male.setOnCheckedChangeListener(List_ur_Space.this);
        chk_female.setOnCheckedChangeListener(List_ur_Space.this);
        chk_coffee.setOnCheckedChangeListener(List_ur_Space.this);

        img_1.setOnClickListener(List_ur_Space.this);
        img_2.setOnClickListener(List_ur_Space.this);
        img_3.setOnClickListener(List_ur_Space.this);
        img_back.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.img_back_listurSpace:
                finish();
                //startActivity(new Intent(List_ur_Space.this,Login.class));
                break;

            case R.id.img_space_1:
                flag = "1";
                if (ContextCompat.checkSelfPermission(List_ur_Space.this,

                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(List_ur_Space.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(List_ur_Space.this,
                            Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(List_ur_Space.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        selectImage();

                    } else {
                        ActivityCompat.requestPermissions(List_ur_Space.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"}, 200);
                        // No explanation needed, we can request the permission.
                    }
                } else {
                    selectImage();
                }
                break;

            case R.id.img_space_2:
                flag = "2";
                if (ContextCompat.checkSelfPermission(List_ur_Space.this,

                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(List_ur_Space.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(List_ur_Space.this,
                            Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(List_ur_Space.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        selectImage();
                    } else {
                        ActivityCompat.requestPermissions(List_ur_Space.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"}, 200);
                        // No explanation needed, we can request the permission.
                    }
                } else {
                    selectImage();
                }
                break;

            case R.id.img_space_3:
                flag = "3";
                if (ContextCompat.checkSelfPermission(List_ur_Space.this,

                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(List_ur_Space.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(List_ur_Space.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(List_ur_Space.this,
                            Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(List_ur_Space.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(List_ur_Space.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        selectImage();
                    } else {

                        ActivityCompat.requestPermissions(List_ur_Space.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
                        // No explanation needed, we can request the permission.
                    }
                } else {
                    selectImage();
                }
                break;


            case R.id.btn_space_location:

                Intent intent = new Intent(List_ur_Space.this, list_ur_spce_location_map.class);
                intent.putExtra("lat", str_lat);
                intent.putExtra("lng", str_lng);
                intent.putExtra("address", mstr_locationAddress);
                intent.putExtra("city", mstr_locationCity);
                startActivityForResult(intent,10);

                //getCurrentlocation();

                break;
            case R.id.btn_space_submit:


                mstr_name = ed_name.getText().toString().trim();
                mstr_email = ed_email.getText().toString().trim();
                mstr_mobile = ed_mobile.getText().toString().trim();

                mstr_cap1 = sp_cpacity.getText().toString().trim();
                mstr_avali1 = sp_avalibility.getText().toString().trim();

                String str = String.valueOf(mstr_mobile.startsWith("05"));
                mstr_price = ed_price.getText().toString().trim();

                if (mstr_cap1.length() == 0) {
                    sp_cpacity.setError("Enter Capacity");
                    sp_cpacity.requestFocus();
                } else if (mstr_avali1.length() == 0) {
                    sp_avalibility.setError("Enter Count");
                    sp_avalibility.requestFocus();
                } else if (mstr_price.length() == 0) {
                    ed_price.setError("Enter Price");
                    ed_price.requestFocus();
                } else if (mstr_name.length() == 0) {
                    ed_name.setError("Please Enter Name");
                    ed_name.setFocusable(true);
                } else if (mstr_email.length() == 0) {
                    ed_email.setError("Please Enter Email");
                    ed_email.setFocusable(true);
                } else if (!mstr_email.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
                    /*[a-zA-Z0-9._-]+@[a-z]+.[a-z]+*/
                    ed_email.setError("Invalid Email Address");
                    ed_email.setFocusable(true);
                } else if (str.equals("false")) {
                    ed_mobile.setError("Number Start With 05");
                    ed_mobile.setFocusable(true);
                    ed_mobile.requestFocus();
                } else if (mstr_mobile.length() != 10) {
                    ed_mobile.setError("Please Enter Correct Number");
                    ed_mobile.setFocusable(true);
                } else if (str_lat.equals("0") && str_lng.equals("0")) {
                    Toast.makeText(activity, "Please Select Location", Toast.LENGTH_SHORT).show();
                } else if (mstr_type.equals("0")) {
                    Toast.makeText(activity, "Please Select Space Type", Toast.LENGTH_SHORT).show();
                } else if (mstr_cityid.equals("0")) {
                    Toast.makeText(activity, "Please Select City", Toast.LENGTH_SHORT).show();
                } else {

                    if (!str_img_path1.equals("0") || !str_img_path2.equals("0") || !str_img_path3.equals("0")) {

                        if (isNetworkAvailable()) {

                            try {

                                data_space.put("email", mstr_email);
                                data_space.put("type", mstr_type);
                                data_space.put("name", mstr_name);
                                data_space.put("mobile", mstr_mobile);

                                data_space.put("capacity1", mstr_cap1);
                                data_space.put("avail1", mstr_avali1);

                                data_space.put("capacity2", mstr_cap2);
                                data_space.put("avail2", mstr_avali2);

                                data_space.put("capacity3", mstr_cap3);
                                data_space.put("avail3", mstr_avali3);

                                data_space.put("projector", mstr_projector);
                                data_space.put("scanner", mstr_scanner);
                                data_space.put("parking", mstr_parking);

                                data_space.put("ac", mstr_aircondi);
                                data_space.put("locker", mstr_locker);
                                data_space.put("phone", mstr_phone);

                                data_space.put("mail_ser", mstr_mailservice);
                                data_space.put("wifi", mstr_internet);
                                data_space.put("work", mstr_work);

                                data_space.put("male", mstr_male);
                                data_space.put("female", mstr_female);
                                data_space.put("coffee", mstr_coffee);

                                //data_space.put("gender",rb_button);

                                data_space.put("lat", str_lat);
                                data_space.put("long", str_lng);

                                data_space.put("location", str_address);
                                data_space.put("cityname", str_city);
                                data_space.put("city_id", mstr_cityid);
                                data_space.put("price", mstr_price);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Task_Space task_space = new Task_Space();
                            task_space.execute();

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
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);

                            snackbar.show();

                        }
                    } else {
                        Toast.makeText(activity, "Please add min one Image", Toast.LENGTH_SHORT).show();

                    }


                }

                break;

        }

    }


    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(List_ur_Space.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/hive/";
                    File newdir = new File(dir);
                    newdir.mkdirs();

                    String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";

                    File newfile = new File(file);
                    try {
                        newfile.createNewFile();
                    } catch (IOException ignored) {

                    }

                    selectedImage = Uri.fromFile(newfile);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                    startActivityForResult(cameraIntent, 1);


                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                if (flag.equals("1")) {

                    try {
                        if (selectedImage != null) {

                            if (selectedImage.toString().startsWith("file:")) {

                                //sourceFile_sign = new File(selectedImage.getPath());

                                InputStream in = null;

                                try {
                                    final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
                                    in = getContentResolver().openInputStream(selectedImage);
                                    // Decode image size
                                    BitmapFactory.Options o = new BitmapFactory.Options();
                                    o.inJustDecodeBounds = true;
                                    BitmapFactory.decodeStream(in, null, o);
                                    try {
                                        in.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    int scale = 1;
                                    while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                                            IMAGE_MAX_SIZE) {
                                        scale++;
                                    }
                                    Bitmap b = null;
                                    in = getContentResolver().openInputStream(selectedImage);
                                    if (scale > 1) {
                                        scale--;
                                        // scale to max possible inSampleSize that still yields an image
                                        // larger than target
                                        o = new BitmapFactory.Options();
                                        o.inSampleSize = scale;
                                        b = BitmapFactory.decodeStream(in, null, o);

                                        // resize to desired dimensions
                                        int height = b.getHeight();
                                        int width = b.getWidth();

                                        double y = Math.sqrt(IMAGE_MAX_SIZE
                                                / (((double) width) / height));
                                        double x = (y / height) * width;

                                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                                                (int) y, true);
                                        b.recycle();
                                        b = scaledBitmap;

                                        System.gc();
                                    } else {
                                        b = BitmapFactory.decodeStream(in);
                                    }

                                    in.close();
                                    img_1.setImageBitmap(b);
                                    str_img_path1=selectedImage.getPath();
                                    img_add1.setVisibility(View.GONE);

                                } catch (Exception ignored) {
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                        /*Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                        str_img_path1=getRealPathFromURI(tempUri);
                        img_1.setImageBitmap(bitmap);
                        img_add1.setVisibility(View.GONE);
*/
                }

                if (flag.equals("2")) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    str_img_path2=getRealPathFromURI(tempUri);
                    img_2.setImageBitmap(bitmap);
                    img_add2.setVisibility(View.GONE);

                }

                if (flag.equals("3")) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    str_img_path3=getRealPathFromURI(tempUri);
                    img_3.setImageBitmap(bitmap);
                    img_add3.setVisibility(View.GONE);
                }

            } else if (requestCode == 2) {


                if (flag.equals("1")) {

                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    str_img_path1 = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(str_img_path1));
                    //Log.i("path of image from galle", picturePath+"");
                    Log.i("IMage Path", str_img_path1 + "");

                    img_1.setImageBitmap(thumbnail);
                    img_add1.setVisibility(View.GONE);
                }
                if (flag.equals("2")) {

                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    str_img_path2 = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(str_img_path2));
                    //Log.i("path of image from galle", picturePath+"");
                    Log.i("IMage Path", str_img_path2 + "");

                    img_2.setImageBitmap(thumbnail);
                    img_add2.setVisibility(View.GONE);
                }
                if (flag.equals("3")) {

                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    str_img_path3 = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(str_img_path3));
                    //Log.i("path of image from galle", picturePath+"");
                    Log.i("IMage Path", str_img_path3 + "");

                    img_3.setImageBitmap(thumbnail);
                    img_add3.setVisibility(View.GONE);
                }

                /*lv_list.addView(img_pic);
                ar_imgPanorama.add(picturePath);*/
            }

        }

        if (resultCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_CANCELED: {
                    // The user was asked to change settings, but chose not to

                    Log.i("permissioncancel", "cancel");
                    requestLocationPermission();
                    enableLocationService();

                    break;
                }
                case Activity.RESULT_OK: {

                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestLocationPermission();
                            enableLocationService();
                        } else {

                            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            //Log.i("lat,latng3",location.getLatitude()+","+location.getLongitude());

                        }
                    }
                    break;
                }

                default: {
                    break;
                }
            }
        }

        Log.i("result_code",requestCode+"");

        if(requestCode == 10){

            Log.i("in_select address","in");

            try {

                str_type_location = data.getStringExtra("type");
                str_lat = data.getStringExtra("lati");
                str_lng = data.getStringExtra("longi");

                if (str_type_location.equals("login")) {

                    str_address = data.getStringExtra("address");
                    str_city = data.getStringExtra("city");
                    tv_displayLocation.setText(str_address);
                    //Toast.makeText(activity, "Current", Toast.LENGTH_SHORT).show();
                } else {

                    str_address = data.getStringExtra("address");
                    str_city = data.getStringExtra("city");
                    tv_displayLocation.setText(str_address);

                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermission();
        } else {

            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.i("android_latlang", location.getLatitude() + "," + location.getLongitude());
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        requestLocationPermission();

                    } else {

                        try {

                            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                            if (getIntent().hasExtra("fromSetting") && getIntent().getStringExtra("fromSetting")
                                    .equalsIgnoreCase("Setting")) {


                            } else {

                                Log.i("lat,lang2", location.getLatitude() + "," + location.getLongitude());
                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                                getCountryName(this, Double.parseDouble(latitude), Double.parseDouble(longitude));
                                if (!latitude.equals("0") && !longitude.equals("0")) {
                                    lat_flag = 1;
                                } else {
                                    lat_flag = 0;
                                }


                            }
                        } catch (Exception e) {

                           /* try {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(List_ur_Space.this);

                                builder.setIcon(R.drawable.logo1);
                                *//*builder.setTitle("Salon Not Available");
                                builder.setMessage("Please Select Your city or area Manual ?");
*//*
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //DO TASK
                                        lat_flag = 1;
                                      *//*  Intent i_menual = new Intent(Home.this, Search_city.class);
                                        finish();
                                        startActivity(i_menual);*//*

                                    }
                                });

                                final android.support.v7.app.AlertDialog dialog = builder.create();
                                dialog.show();
                            } catch (Exception e1) {
                                e.printStackTrace();
                            }
*/
                        }
                    }
                } else {
                    if (getIntent().hasExtra("fromSetting") && getIntent().getStringExtra("fromSetting")
                            .equalsIgnoreCase("Setting")) {

                        Log.i("location", "cancel");
                    } else {
                        //    new WalkthroughActivity.CitiesParsingTask().execute();
                        //        checkForStoragePermission();

                        Log.i("lat,lang123", location.getLatitude() + "," + location.getLongitude());
                    }
                }
                break;
        }
    }


    /**
     * Enable Location service for display offer distance
     */
    private void enableLocationService() {

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                                //Log.i("latlang1",location.getLatitude()+","+location.getLongitude());
                            }
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
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                if (!activity.isFinishing()) {
                                    status.startResolutionForResult(activity, REQUEST_LOCATION);
                                    Log.i("step 1", "hello");
                                }


                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                                e.printStackTrace();
                            }

                            break;
                        case LocationSettingsStatusCodes.SUCCESS:
                            requestLocationPermission();
                            break;
                    }
                }
            });
        }

    }

    private void requestLocationPermission() {

        // BEGIN_INCLUDE(Location_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            /*Snackbar.make(rlContainerMain, R.string.permission_location_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.dialog_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
                    }).show();*/
        } else {

            // Location permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

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
                Log.d("LocationAddress", mstr_locationAddress);
                Log.d("City", addresses.get(0).getLocality());
                return addresses;
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }
        return addresses;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int id = buttonView.getId();

        if (id == R.id.chk_space_projector) {
            if (chk_projector.isChecked()) {
                mstr_projector = "1";
            } else {

                mstr_projector = "0";
            }
        } else if (id == R.id.chk_space_air_condi) {

            if (chk_aircondi.isChecked()) {
                mstr_aircondi = "1";
            } else {
                mstr_aircondi = "0";
            }
        } else if (id == R.id.chk_space_mailservice) {

            if (chk_mailservice.isChecked()) {
                mstr_mailservice = "1";
            } else {
                mstr_mailservice = "0";
            }
        } else if (id == R.id.chk_space_scanner) {

            if (chk_scanner.isChecked()) {
                mstr_scanner = "1";
            } else {
                mstr_scanner = "0";
            }
        } else if (id == R.id.chk_space_locker) {

            if (chk_locker.isChecked()) {
                mstr_locker = "1";
            } else {
                mstr_locker = "2";
            }
        } else if (id == R.id.chk_space_internet) {

            if (chk_internet.isChecked()) {
                mstr_internet = "1";
            } else {
                mstr_internet = "0";
            }
        } else if (id == R.id.chk_space_parking) {

            if (chk_parking.isChecked()) {
                mstr_parking = "1";
            } else {
                mstr_parking = "0";
            }
        } else if (id == R.id.chk_space_phone) {

            if (chk_phone.isChecked()) {
                mstr_phone = "1";
            } else {
                mstr_phone = "0";
            }
        } else if (id == R.id.chk_space_work) {

            if (chk_work.isChecked()) {
                mstr_work = "1";
            } else {
                mstr_work = "0";
            }
        } else if (id == R.id.chk_space_Man) {

            if (chk_male.isChecked()) {
                mstr_male = "1";
            } else {
                mstr_male = "0";
            }
        } else if (id == R.id.chk_space_female) {

            if (chk_female.isChecked()) {
                mstr_female = "1";
            } else {
                mstr_female = "0";
            }
        } else if (id == R.id.chk_space_coffee) {

            if (chk_coffee.isChecked()) {
                mstr_coffee = "1";
            } else {
                mstr_coffee = "0";
            }
        }

    }


    private class Task_Space extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata p_user = new Postdata();
                Log.i("Space", data_space.toString());
                String data_user = p_user.post(Url_info.main_url + "your_space.php", data_space.toString());
                Log.i("responce", data_user);

                JSONObject jobj_space = new JSONObject(data_user);

                status1 = jobj_space.getString(status);

                if (status1.equals("1")) {

                    Log.d("ListUrSpace", "successfully Inserted");
                    mstr_spaceId = jobj_space.getString("space_id");
                } else {
                    message1 = jobj_space.getString(message);
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

                progressDialog.dismiss();

                Log.d("space_id", mstr_spaceId);
                if (isNetworkAvailable()) {

                    Task_imageUpload task_imageUpload = new Task_imageUpload();
                    task_imageUpload.execute();

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
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);

                    snackbar.show();

                }

            } else {
                Toast.makeText(List_ur_Space.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(List_ur_Space.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private class Task_imageUpload extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {
                Log.i("path", str_img_path1.replace("Pictures/Pictures/","Pictures/") + "\n" + str_img_path2 + "\n" + str_img_path3);
                if (str_img_path1.equals("0")) {

                } else {

                    File sourceFile = new File(str_img_path1.replace("Pictures/Pictures/","Pictures/"));
                    Imageupload img = new Imageupload();
                    status1 = img.post(Url_info.main_url + "upload_space_pic.php", sourceFile, mstr_spaceId);
                    Log.i("status", status1);
                    JSONObject job_img = new JSONObject(status1);
                    status1 = job_img.getString(status);


                }
                if (str_img_path2.equals("0")) {

                } else {

                    File sourceFile = new File(str_img_path2);
                    Imageupload img = new Imageupload();
                    status1 = img.post(Url_info.main_url + "upload_space_pic.php", sourceFile, mstr_spaceId);
                    Log.i("status", status1);
                    JSONObject job_img = new JSONObject(status1);
                    status1 = job_img.getString(status);
                }
                if (str_img_path3.equals("0")) {

                } else {

                    File sourceFile = new File(str_img_path3);
                    Imageupload img = new Imageupload();
                    status1 = img.post(Url_info.main_url + "upload_space_pic.php", sourceFile, mstr_spaceId);
                    Log.i("status", status1);
                    JSONObject job_img = new JSONObject(status1);
                    status1 = job_img.getString(status);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {
                Log.d("imageUpload", "successfully");
                ed_email.setText("");
                ed_mobile.setText("");
                ed_name.setText("");
                ed_price.setText("");
                sp_space_type.setSelection(0);
                sp_avalibility.setText("");
                sp_cpacity.setText("");

                chk_aircondi.setChecked(false);
                chk_coffee.setChecked(false);
                chk_female.setChecked(false);
                chk_internet.setChecked(false);
                chk_locker.setChecked(false);
                chk_mailservice.setChecked(false);
                chk_male.setChecked(false);
                chk_parking.setChecked(false);
                chk_phone.setChecked(false);
                chk_projector.setChecked(false);
                chk_scanner.setChecked(false);
                chk_work.setChecked(false);

                img_1.setVisibility(View.GONE);
                img_add1.setVisibility(View.VISIBLE);

                img_2.setVisibility(View.GONE);
                img_add2.setVisibility(View.VISIBLE);

                img_3.setVisibility(View.GONE);
                img_add3.setVisibility(View.VISIBLE);

                // Toast.makeText(getApplicationContext(), "Successfully Submit!!!", Toast.LENGTH_LONG).show();
                //finish();
                //startActivity(new Intent(List_ur_Space.this, Thank_U_listUrSpace.class));

                Toast.makeText(activity, "Space is Successfully added", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(activity, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(List_ur_Space.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private class Task_getLocation1 extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            ar_city.clear();
            ar_cityid.clear();

            ar_city.add(getResources().getString(R.string.select_city));
            ar_cityid.add("0");

            try {

                Getdata getdata = new Getdata();
                String mstr_location = getdata.getJSONFromUrl(Url_info.main_url + "fetch_city.php");
                JSONObject job_location = new JSONObject(mstr_location);
                status1 = job_location.getString(status);

                if (status1.equals("1")) {
                    JSONArray j_loc = job_location.getJSONArray("city");

                    for (int i = 0; i < j_loc.length(); i++) {

                        JSONObject location = j_loc.getJSONObject(i);
                        ar_cityid.add(location.getString("city_id"));
                        ar_city.add(location.getString("city_name"));
                    }
                } else {
                    message1 = job_location.getString(message);
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

                //ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, ar_city);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custome_spiner_view, R.id.tv_item, ar_city);
                sp_city.setAdapter(adapter);

            } else {
                Toast.makeText(getApplicationContext(), "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(List_ur_Space.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Claim", null);
        return Uri.parse(path);
    }
}
