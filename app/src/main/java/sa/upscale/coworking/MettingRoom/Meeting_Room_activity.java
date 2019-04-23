package sa.upscale.coworking.MettingRoom;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sa.upscale.coworking.MettingRoom.TImePicker.CustomTimePickerDialog;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class Meeting_Room_activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_meetingroom = new JSONObject();

    SessionManager sessionManager;
    HashMap<String,String> user_Details=new HashMap<>();

    private ArrayList<String> ar_space_id = new ArrayList<>();
    private ArrayList<String> ar_name = new ArrayList<>();
    private ArrayList<String> ar_projector = new ArrayList<>();
    private ArrayList<String> ar_scanner = new ArrayList<>();
    private ArrayList<String> ar_parking = new ArrayList<>();
    private ArrayList<String> ar_ac = new ArrayList<>();
    private ArrayList<String> ar_locker = new ArrayList<>();
    private ArrayList<String> ar_ph = new ArrayList<>();
    private ArrayList<String> ar_mail = new ArrayList<>();
    private ArrayList<String> ar_wifi = new ArrayList<>();
    private ArrayList<String> ar_work = new ArrayList<>();
    private ArrayList<String> ar_location = new ArrayList<>();
    private ArrayList<String> ar_price = new ArrayList<>();
    private ArrayList<String> ar_capacity = new ArrayList<>();
    private ArrayList<String> ar_img = new ArrayList<>();
    private ArrayList<String> ar_lat = new ArrayList<>();
    private ArrayList<String> ar_rating = new ArrayList<>();
    private ArrayList<String> ar_rating_count = new ArrayList<>();
    private ArrayList<String> ar_long = new ArrayList<>();
    private ArrayList<String> ar_distance = new ArrayList<>();
    private ArrayList<String> ar_wish_status = new ArrayList<>();


   /* ArrayList<String> ar_selectedItem = new ArrayList<>();*/

    String mstr_location, mstr_locationId, mstr_count, mstr_type;
    public static String mstr_typeName;/* mstr_meeting_status, mstr_desk_status, mstr_discussion_status, mstr_private_status, mstr_conference_status*/
    public static String mstr_book_date, mstr_book_from, mstr_book_to;
    private TextView tv_count, tv_date, tv_from, tv_to;
    private RelativeLayout btn_date, btn_from, btn_to;
    private LinearLayout btn_back, btn_next,ll_repeatView;
    private Spinner sp_bookingEveryday;
    ImageView img_areaSelected;

    private int mYear, mMonth, mDay, m_from_Hour, m_from_Minute, m_to_Hour, m_to_Minute;
    String format;
    android.support.v4.app.FragmentTransaction ft;

    RelativeLayout linearLayout,btn_endDate;

    static int lat_flag = 0;
    Location location;
    GoogleApiClient mGoogleApiClient;
    final int REQUEST_LOCATION = 1;
    private static final int LOCATION_REQUEST_CODE = 1;

    String str_lat = "0", str_long = "0";
    Activity activity;
    TextView tv_areaName;
    LinearLayout ll_areaName;
   /* int areasize = 0;*/

    TextView tv_enddate;
    CheckBox chk_bookingReapt;
    String str_chkBookRepeat = "0",mstr_date;
    static String str_bookEveryDayId = "0",mstr_enddDate,str_flag_book="0";

    String ar_sp_bookingEveryday[] = {"Every Day", "Every Week", "Every Month"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_room);

        activity = this;
        custActionbar();
        sessionManager=new SessionManager(Meeting_Room_activity.this);
        user_Details=sessionManager.getUserDetails();
        Intent intent = getIntent();

        mstr_location = intent.getStringExtra("location");
        mstr_locationId = intent.getStringExtra("locationId");
        mstr_count = intent.getStringExtra("count");
        mstr_typeName = intent.getStringExtra("typaName");
        mstr_type = intent.getStringExtra("space_type");

       findViews();



        tv_areaName.setText(mstr_typeName);
        tv_count.setText(mstr_count);

        if (mstr_type.equals("1"))
        {
            img_areaSelected.setBackgroundResource(R.drawable.meeting1);
        }else if (mstr_type.equals("2"))
        {
            img_areaSelected.setBackgroundResource(R.drawable.desk_blank);
        }else if (mstr_type.equals("3"))
        {
            img_areaSelected.setBackgroundResource(R.drawable.discussroom_black);
        }
        else if (mstr_type.equals("4"))
        {
            img_areaSelected.setBackgroundResource(R.drawable.private_room_black);
        }
        else if (mstr_type.equals("5"))
        {
            img_areaSelected.setBackgroundResource(R.drawable.conforance_black);
        } else if (mstr_type.equals("6"))
        {
            img_areaSelected.setBackgroundResource(R.drawable.other_black);
        }else {

        }

            // requestLocationPermission();
        enableLocationService();
//        Date d = new Date();
        Calendar d = Calendar.getInstance();
        CharSequence s = DateFormat.format("MMMM dd, yyyy", d);
        Log.d("Date", String.valueOf(s));
        tv_date.setText(s);

        ArrayAdapter arrayAdapter = new ArrayAdapter(Meeting_Room_activity.this, android.R.layout.simple_list_item_1, ar_sp_bookingEveryday);
        sp_bookingEveryday.setAdapter(arrayAdapter);

        sp_bookingEveryday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String name = sp_bookingEveryday.getSelectedItem().toString();

                if (name.equalsIgnoreCase("Every Day")) {
                    str_bookEveryDayId = "1";
                }else if (name.equalsIgnoreCase("Every Week")) {
                    str_bookEveryDayId = "2";
                } else if (name.equalsIgnoreCase("Every Month")) {
                    str_bookEveryDayId = "3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        chk_bookingReapt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (chk_bookingReapt.isChecked()) {
                    str_chkBookRepeat = "1";
                    str_bookEveryDayId="1";
                    ll_repeatView.setVisibility(View.VISIBLE);
                } else {
                    str_chkBookRepeat = "0";
                    str_bookEveryDayId="0";
                    ll_repeatView.setVisibility(View.GONE);
                }
            }
        });


    }

    /*private void navImageIcon() {

        ImageView imageView = (ImageView) view.findViewById(R.id.navIcon);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_nav_action_titile);
        tv_title.setText("Booking");
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }*/
    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = view.findViewById(R.id.action_bar_back);
        TextView tv_title = view.findViewById(R.id.action_bar_title);
        String strName = getString(R.string.booking);
        tv_title.setText(strName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView img_filter = view.findViewById(R.id.action_bar_filter);
        img_filter.setVisibility(View.GONE);
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void findViews() {

        ll_areaName = findViewById(R.id.ll_areaNameDisplay);
        tv_areaName = findViewById(R.id.tv_selected_areaName);
        img_areaSelected = findViewById(R.id.img_selected_areaImg);
        linearLayout = findViewById(R.id.ll_snackbar);
        tv_count = findViewById(R.id.tv_meetingRoom_count);
        tv_date = findViewById(R.id.tv_meetingRoom_date);
        tv_from = findViewById(R.id.tv_meetingRoom_from);
        tv_to = findViewById(R.id.tv_meetingRoom_to);

        tv_count = findViewById(R.id.tv_meetingRoom_count);
        btn_date = findViewById(R.id.rl_meetingRoom_date);
        btn_from = findViewById(R.id.rl_meetingRoom_from);
        btn_to = findViewById(R.id.rl_meetingRoom_to);

        btn_back = findViewById(R.id.ll_meetingRoom_back);
        btn_next = findViewById(R.id.ll_meetingRoom_next);
        sp_bookingEveryday = findViewById(R.id.sp_bookingList_detail_repeatBookingEveryday);

        tv_enddate = findViewById(R.id.tv_bookingrommDetails_enddate);
        chk_bookingReapt = findViewById(R.id.chk_bookingrommDetails_repeat);
        ll_repeatView = findViewById(R.id.ll_repeatView);
        btn_endDate = findViewById(R.id.rl_bookingrommDetails_enddate);


        btn_date.setOnClickListener(this);
        btn_from.setOnClickListener(this);
        btn_to.setOnClickListener(this);

        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_endDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.rl_meetingRoom_date:

                datePicker();

                break;

            case R.id.rl_meetingRoom_from:
                from_timePicker();
                break;

            case R.id.rl_meetingRoom_to:
                to_timePicker();
                break;

            case R.id.rl_bookingrommDetails_enddate:
                datePicker_endDate();
                break;

            case R.id.ll_meetingRoom_back:

                finish();
               /* New_Booking_activity new_booking_activity = new New_Booking_activity();
                ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.mainContent, new_booking_activity).commit();*/
                break;

            case R.id.ll_meetingRoom_next:

                requestLocationPermission();
                enableLocationService();
                str_flag_book="1";

                mstr_book_date = tv_date.getText().toString();
                mstr_book_from = tv_from.getText().toString();
                mstr_book_to = tv_to.getText().toString();

                String strendDate = tv_enddate.getText().toString().trim();
                String str_chkValidation = null;

                if (str_chkBookRepeat.equals("1")) {

                    if (strendDate.length()==0) {

                        Toast.makeText(this, "Select Ending date", Toast.LENGTH_SHORT).show();
                        mstr_enddDate=" ";
                        break;

                    }else {
                        mstr_enddDate=strendDate;
                        str_chkValidation="1";
                    }
                }else {
                    mstr_enddDate=" ";
                    str_chkValidation="1";
                }



                if (mstr_book_date.length() == 0) {
                    tv_date.setError("select date");
                    tv_date.setFocusable(true);
                } else if (mstr_book_from.length() == 0) {
                    tv_from.setError("Select Time");
                    tv_from.setFocusable(true);
                } else if (mstr_book_to.length() == 0) {
                    tv_to.setError("Select Time");
                    tv_to.setFocusable(true);
                } /*else if (str_lat.equals("0") || str_long.equals("0")) {
                    // Toast.makeText(activity, "please click Again  ", Toast.LENGTH_SHORT).show();
                    requestLocationPermission();
                    enableLocationService();
                }*/ else {

                    if (isNetworkAvailable()) {
                        if (str_chkValidation.equals("1")) {

                            try {
                                requestLocationPermission();
                                enableLocationService();

                                data_meetingroom.put("user_id", user_Details.get(SessionManager.user_Id));
                                data_meetingroom.put("date", mstr_book_date);
                                data_meetingroom.put("fromtime", mstr_book_from);
                                data_meetingroom.put("totime", mstr_book_to);
                                data_meetingroom.put("location", mstr_locationId);
                                data_meetingroom.put("space_type", mstr_type);
                                data_meetingroom.put("lat", str_lat);
                                data_meetingroom.put("long", str_long);


                                // Toast.makeText(activity, ""+str_lat+"\n"+str_long, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Intent intent = new Intent(Meeting_Room_activity.this, Booking_MettingRoom_List.class);
                            intent.putExtra("user_id", user_Details.get(SessionManager.user_Id));
                            intent.putExtra("date", mstr_book_date);
                            intent.putExtra("fromtime", mstr_book_from);
                            intent.putExtra("totime", mstr_book_to);
                            intent.putExtra("location", mstr_locationId);
                            intent.putExtra("space_type", mstr_type);
                            intent.putExtra("lat", str_lat);
                            intent.putExtra("longi", str_long);
                            startActivity(intent);
                        /*Task_meetingRoomBooking meetingRoomBooking = new Task_meetingRoomBooking();
                        meetingRoomBooking.execute();*/
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

                break;
        }
    }


    private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String str_minute;

            if (hourOfDay == 0) {
                hourOfDay += 12;
                /*format = getString(R.string.am);*/
                format = "AM";
            } else if (hourOfDay == 12) {
                //format = getString(R.string.pm);
                format = "PM";
            } else if (hourOfDay > 12) {
                hourOfDay -= 12;
                //format = getString(R.string.pm);
                format = "PM";
            } else {
                //format = getString(R.string.am);
                format = "AM";
            }

            if (minute == 0) {
                str_minute = "0" + minute;
            } else {
                str_minute = String.valueOf(minute);
            }

            // mstr_toTime = hourOfDay + ":" + str_minute + " " + format;

            tv_to.setText(hourOfDay + ":" + str_minute + " " + format);
        }
    };

    private CustomTimePickerDialog.OnTimeSetListener timeSetListener1 = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String str_minute;
            if (hourOfDay == 0) {
                hourOfDay += 12;
                //format = getString(R.string.am);
                format = "AM";
            } else if (hourOfDay == 12) {
                //format = getString(R.string.pm);
                format = "PM";
            } else if (hourOfDay > 12) {
                hourOfDay -= 12;
                //format = getString(R.string.pm);
                format = "PM";
            } else {
                //format = getString(R.string.am);
                format = "AM";
            }

            if (minute == 0) {
                str_minute = "0" + minute;
            } else {
                str_minute = String.valueOf(minute);
            }


            tv_from.setText(hourOfDay + ":" + str_minute + " " + format);
        }
    };

    private void to_timePicker() {


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Meeting_Room_activity.this, timeSetListener, hour, minute, false);
        timePickerDialog.show();

    }

    private void from_timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Meeting_Room_activity.this, timeSetListener1, hour, minute, false);
        timePickerDialog.show();

    }


    private void datePicker_endDate() {


        // Get Current Date
        final Calendar c;
        c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Meeting_Room_activity.this,
                new DatePickerDialog.OnDateSetListener() {

                    String month;

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int m = monthOfYear + 1;
                        if (m == 1) {
                            month = "Jan";
                        } else if (m == 2) {
                            month = "Feb";
                        } else if (m == 3) {
                            month = "Mar";
                        } else if (m == 4) {
                            month = "Apr";
                        } else if (m == 5) {
                            month = "May";
                        } else if (m == 6) {
                            month = "Jun";
                        } else if (m == 7) {
                            month = "Jul";
                        } else if (m == 8) {
                            month = "Aug";
                        } else if (m == 9) {
                            month = "Sep";
                        } else if (m == 10) {
                            month = "Oct";
                        } else if (m == 11) {
                            month = "Nov";
                        } else if (m == 12) {
                            month = "Dec";
                        }

                        // tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mstr_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        tv_enddate.setText(month + " " + dayOfMonth +","+year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void datePicker() {

        // Get Current Date
        final Calendar c;

        c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Meeting_Room_activity.this,
                new DatePickerDialog.OnDateSetListener() {

                    String month;

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int m = monthOfYear + 1;
                        if (m == 1) {
                            //month = getString(R.string.jaunuary);
                            month = "January";
                        } else if (m == 2) {
                            //month = getString(R.string.february);
                            month = "February";
                        } else if (m == 3) {
                            //month = getString(R.string.march);
                            month = "March";
                        } else if (m == 4) {
                            //month = getString(R.string.april);
                            month = "April";
                        } else if (m == 5) {
                            //month = getString(R.string.may);
                            month = "May";
                        } else if (m == 6) {
//                            month = getString(R.string.june);
                            month = "June";
                        } else if (m == 7) {
                            //month = getString(R.string.july);
                            month = "July";
                        } else if (m == 8) {
                            //month = getString(R.string.august);
                            month = "August";
                        } else if (m == 9) {
                            //month = getString(R.string.september);
                            month = "September";
                        } else if (m == 10) {
                            //month = getString(R.string.octomer);
                            month = "October";
                        } else if (m == 11) {
                            //month = getString(R.string.november);
                            month = "November";
                        } else if (m == 12) {
                            //month = getString(R.string.december);
                            month = "December";
                        }


                        // tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        tv_date.setText(month + " " + dayOfMonth + "," + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

    }

    private class Task_meetingRoomBooking extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            clearArrayList();

            try {
                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "booking_datetime.php", data_meetingroom.toString());
                JSONObject jobj_login = new JSONObject(data_user);
                status1 = jobj_login.getString(status);

                if (status1.equals("1")) {

                    Log.d("signup", "successfully register");

                    JSONArray s_list = jobj_login.getJSONArray("space_list");

                    for (int i = 0; i < s_list.length(); i++) {
                        JSONObject jobj_list = s_list.getJSONObject(i);

                        ar_space_id.add(jobj_list.getString("space_id"));
                        ar_name.add(jobj_list.getString("name"));
                        ar_projector.add(jobj_list.getString("projector"));
                        ar_scanner.add(jobj_list.getString("scanner"));
                        ar_parking.add(jobj_list.getString("parking"));
                        ar_ac.add(jobj_list.getString("ac"));
                        ar_locker.add(jobj_list.getString("locker"));
                        ar_ph.add(jobj_list.getString("ph"));
                        ar_mail.add(jobj_list.getString("mail"));
                        ar_wifi.add(jobj_list.getString("wifi"));
                        ar_work.add(jobj_list.getString("work"));
                        ar_location.add(jobj_list.getString("location"));
                        ar_price.add(jobj_list.getString("price"));
                        ar_capacity.add(jobj_list.getString("capacity"));
                        ar_img.add(jobj_list.getString("img"));
                        ar_lat.add(jobj_list.getString("lat"));
                        ar_long.add(jobj_list.getString("long"));
                        ar_rating.add(jobj_list.getString("rating"));
                        ar_rating_count.add(jobj_list.getString("rating_count"));
                        ar_distance.add(jobj_list.getString("distance"));
                        ar_wish_status.add(jobj_list.getString("wish_status"));


                    }

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


                Intent bundle = new Intent(Meeting_Room_activity.this, Booking_MettingRoom_List.class);

                bundle.putStringArrayListExtra("space_id", ar_space_id);
                bundle.putStringArrayListExtra("name", ar_name);
                bundle.putStringArrayListExtra("projector", ar_projector);
                bundle.putStringArrayListExtra("scanner", ar_scanner);
                bundle.putStringArrayListExtra("parking", ar_parking);
                bundle.putStringArrayListExtra("ac", ar_ac);
                bundle.putStringArrayListExtra("locker", ar_locker);
                bundle.putStringArrayListExtra("ph", ar_ph);
                bundle.putStringArrayListExtra("mail", ar_mail);
                bundle.putStringArrayListExtra("wifi", ar_wifi);
                bundle.putStringArrayListExtra("work", ar_work);
                bundle.putStringArrayListExtra("location", ar_location);
                bundle.putStringArrayListExtra("price", ar_price);
                bundle.putStringArrayListExtra("capacity", ar_capacity);
                bundle.putStringArrayListExtra("img", ar_img);
                bundle.putStringArrayListExtra("lat", ar_lat);
                bundle.putStringArrayListExtra("long", ar_long);

                bundle.putStringArrayListExtra("rating", ar_rating);
                bundle.putStringArrayListExtra("rating_count", ar_rating_count);
                bundle.putStringArrayListExtra("distance", ar_distance);
                bundle.putStringArrayListExtra("wish_status", ar_wish_status);
                startActivity(bundle);

                Log.d("size", String.valueOf(ar_space_id.size()));


            } else {

                Toast.makeText(Meeting_Room_activity.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Meeting_Room_activity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private void clearArrayList() {

        ar_space_id.clear();
        ar_name.clear();
        ar_projector.clear();
        ar_scanner.clear();
        ar_parking.clear();
        ar_ac.clear();
        ar_locker.clear();
        ar_ph.clear();
        ar_mail.clear();
        ar_wifi.clear();
        ar_work.clear();
        ar_location.clear();
        ar_price.clear();
        ar_capacity.clear();
        ar_img.clear();
        ar_lat.clear();
        ar_long.clear();
        ar_distance.clear();
        ar_wish_status.clear();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestLocationPermission();
        } else {

            try {
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (location == null ) {
                    str_lat = "0";
                    str_long = "0";
                } else {
                    str_lat = String.valueOf(location.getLatitude());
                    str_long = String.valueOf(location.getLongitude());
                }
            } catch (Exception e) {
                e.printStackTrace();
                str_lat = "0";
                str_long = "0";
            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        requestLocationPermission();
                       // enableLocationService();
                        Log.i("permissioncancel", "cancel");
                       /* Snackbar.make(rlContainerMain, R.string.permission_location_rationale,
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.dialog_ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        requestLocationPermission();
                                    }
                                }).show();*/
                        break;
                    }
                    case Activity.RESULT_OK: {

                        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {

                                requestLocationPermission();
                            } else {

                                try {
                                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                                    //Log.i("lat,latng3",location.getLatitude()+","+location.getLongitude());
                                    if (location == null) {
                                        str_lat = "0";
                                        str_long = "0";
                                    } else {
                                        str_lat = String.valueOf(location.getLatitude());
                                        str_long = String.valueOf(location.getLongitude());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    str_lat = "0";
                                    str_long = "0";
                                }

                            }
                        }
                        break;
                    }

                    default: {
                        break;
                    }
                }
                break;
        }
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
                            //Log.i("lat,lang", location.getLatitude() + "," + location.getLongitude());
                            //if (location != null)
                            //Log.i("Test", "" + location);
                            if (getIntent().hasExtra("fromSetting") && getIntent().getStringExtra("fromSetting")
                                    .equalsIgnoreCase("Setting")) {


                            } else {

                               // Log.i("lat,lang2", location.getLatitude() + "," + location.getLongitude());

                                try {
                                    if (location == null) {
                                        str_lat = "0";
                                        str_long = "0";
                                    } else {
                                        str_lat = String.valueOf(location.getLatitude());
                                        str_long = String.valueOf(location.getLongitude());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    str_lat = "0";
                                    str_long = "0";
                                }

                               /* Task_index_nearby t_nearby=new Task_index_nearby();
                                t_nearby.execute();
*/
                            }
                        } catch (Exception e) {


                        }
                    }
                } else {
                    if (getIntent().hasExtra("fromSetting") && getIntent().getStringExtra("fromSetting")
                            .equalsIgnoreCase("Setting")) {

                        Log.i("location", "cancel");
                    } else {
                        //    new WalkthroughActivity.CitiesParsingTask().execute();
                        //        checkForStoragePermission();

                       // Log.i("lat,lang123", location.getLatitude() + "," + location.getLongitude());

                        try {
                            if (location == null) {
                                str_lat = "0";
                                str_long = "0";
                            } else {
                                str_lat = String.valueOf(location.getLatitude());
                                str_long = String.valueOf(location.getLongitude());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            str_lat = "0";
                            str_long = "0";
                        }

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

                                try {
                                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                                    if (location == null) {
                                        str_lat = "0";
                                        str_long = "0";
                                    } else {
                                        str_lat = String.valueOf(location.getLatitude());
                                        str_long = String.valueOf(location.getLongitude());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    str_lat = "0";
                                    str_long = "0";
                                }
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


}
