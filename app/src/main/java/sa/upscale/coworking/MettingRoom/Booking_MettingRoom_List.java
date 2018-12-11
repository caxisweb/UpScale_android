package sa.upscale.coworking.MettingRoom;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

import sa.upscale.coworking.Getdata;
import sa.upscale.coworking.MettingRoom.TImePicker.CustomTimePickerDialog;
import sa.upscale.coworking.MettingRoom.adapter.Custome_Map_Item;
import sa.upscale.coworking.MettingRoom.adapter.adapter_meeting_room_list;
import sa.upscale.coworking.New_Booking_activity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class Booking_MettingRoom_List extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {

    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";


    JSONObject data_meetingroom = new JSONObject();
    JSONObject data_Filter = new JSONObject();

    SessionManager sessionManager;
    HashMap<String, String> user_Details = new HashMap<>();

    LinearLayout ll_tab_list, ll_tab_map, ll_list_details, ll_mapdetails;
    ListView lst_hotelList;

    int i = 0;
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
    private ArrayList<String> ar_male = new ArrayList<>();
    private ArrayList<String> ar_female = new ArrayList<>();
    private ArrayList<String> ar_coffee = new ArrayList<>();
    private ArrayList<String> ar_location = new ArrayList<>();
    private ArrayList<String> ar_price = new ArrayList<>();
    private ArrayList<String> ar_capacity = new ArrayList<>();
    private ArrayList<String> ar_img = new ArrayList<>();
    private ArrayList<String> ar_lat = new ArrayList<>();
    private ArrayList<String> ar_long = new ArrayList<>();
    private ArrayList<String> ar_rating = new ArrayList<>();
    private ArrayList<String> ar_rating_count = new ArrayList<>();
    private ArrayList<String> ar_distance = new ArrayList<>();
    private ArrayList<String> ar_wish_status = new ArrayList<>();

    private Hashtable<String, String> markers = new Hashtable<>();
    private Marker marker;

    private ArrayList<String> ar_location_filter = new ArrayList<>();
    private ArrayList<String> ar_locationId_filter = new ArrayList<>();


    android.support.v4.app.FragmentTransaction ft;
    private GoogleMap mMap;

    static String mstr_bmettingList_space_id, mstr_bmettingList_capacity, str_wishStatus;
    Dialog dialog;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    LinearLayout linearLayout;

    String str_userId, str_date, str_fromtime, str_totime, str_location, str_spacetype, str_lat, str_longi;
    RangeSeekBar rangeSeekbar;

    private int mYear, mMonth, mDay;
    private String mstr_date, format;

    TextView tv_date, tv_from, tv_To;
    CheckBox chk_projector, chk_aircondi, chk_mailservice, chk_scanner, chk_locker, chk_internet,
            chk_parking, chk_phone, chk_work, chk_male, chk_female, chk_coffee;

    String mstr_projector, mstr_aircondi, mstr_mailservice, mstr_scanner, mstr_locker, mstr_internet,
            mstr_parking, mstr_phone, mstr_work,mstr_male,mstr_female,mstr_coffee;
    String str_minPrice = "0", str_maxPrice = "0", str_typeName;

    TabHost tabHost;
    AutoCompleteTextView ed_loation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__metting_room_list);

        //custActionbar();
        initImageLoader();

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)        //    Display Stub Image
                .showImageForEmptyUri(R.mipmap.ic_launcher)    //    If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

        findViews();

        Intent intent = getIntent();
        str_userId = intent.getStringExtra("user_id");
        str_date = intent.getStringExtra("date");
        str_fromtime = intent.getStringExtra("fromtime");
        str_totime = intent.getStringExtra("totime");
        str_location = intent.getStringExtra("location");
        str_spacetype = intent.getStringExtra("space_type");
        str_lat = intent.getStringExtra("lat");
        str_longi = intent.getStringExtra("longi");


        if (isNetworkAvailable()) {
            try {
                data_meetingroom.put("user_id", str_userId);
                data_meetingroom.put("date", str_date);
                data_meetingroom.put("fromtime", str_fromtime);
                data_meetingroom.put("totime", str_totime);
                data_meetingroom.put("location", str_location);
                data_meetingroom.put("space_type", str_spacetype);
                data_meetingroom.put("lat", str_lat);
                data_meetingroom.put("long", str_longi);

                data_meetingroom.put("min_price", str_maxPrice);
                data_meetingroom.put("max_price", str_minPrice);

                data_meetingroom.put("min_price", str_minPrice);
                data_meetingroom.put("max_price", str_maxPrice);

                data_meetingroom.put("is_projector", mstr_projector);
                data_meetingroom.put("is_scaner", mstr_scanner);
                data_meetingroom.put("is_parking", mstr_parking);

                data_meetingroom.put("is_ac", mstr_aircondi);
                data_meetingroom.put("is_lockers", mstr_locker);
                data_meetingroom.put("is_phone", mstr_phone);

                data_meetingroom.put("is_mail_service", mstr_mailservice);
                data_meetingroom.put("is_internet", mstr_internet);
                data_meetingroom.put("is_work_24", mstr_work);

                data_meetingroom.put("is_male", mstr_male);
                data_meetingroom.put("is_female", mstr_female);
                data_meetingroom.put("is_coffee", mstr_coffee);


                Task_meetingRoomBooking task_meetingRoomBooking = new Task_meetingRoomBooking();
                task_meetingRoomBooking.execute();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //loadTabList();

        /*lst_hotelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("listview_Click","true");

                mstr_bmettingList_space_id = ar_space_id.get(position);
                mstr_bmettingList_capacity = ar_capacity.get(position);

                Intent bundle = new Intent(Booking_MettingRoom_List.this, Booking_MettingRoom_list_details.class);
                bundle.putExtra("type", "list");
                bundle.putExtra("spaceId", mstr_bmettingList_space_id);
                bundle.putExtra("capacity_id", mstr_bmettingList_capacity);
                bundle.putExtra("date", str_date);
                bundle.putExtra("from_time", str_fromtime);
                bundle.putExtra("to_time", str_totime);

                startActivity(bundle);


            }
        });
*/

    }

    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        TextView tv_title = (TextView) view.findViewById(R.id.action_bar_title);
        String strName = getString(R.string.booking);
        tv_title.setText(strName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView img_filter = (ImageView) view.findViewById(R.id.action_bar_filter);
        img_filter.setVisibility(View.VISIBLE);
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDlg();
            }
        });
    }

    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) Booking_MettingRoom_List.this.
                    getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                Booking_MettingRoom_List.this).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);
    }


    private void filterDlg() {

        final Spinner sp_spaceType;
        String ar_spacetype[] = {getResources().getString(R.string.meeting_room), getResources().getString(R.string.desk),
                getResources().getString(R.string.discussion_room), getResources().getString(R.string.private_room),
                getResources().getString(R.string.conference_room),
                getResources().getString(R.string.others)};


        AlertDialog.Builder adb = new AlertDialog.Builder(Booking_MettingRoom_List.this);
        dialog = adb.setView(new View(Booking_MettingRoom_List.this)).create();
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);


        //final Dialog dialog = new Dialog(Booking_MettingRoom_list_details.this);
        dialog.setContentView(R.layout.cust_filter);
        dialog.setCancelable(false);

        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_filter_close);
        sp_spaceType = (Spinner) dialog.findViewById(R.id.sp_cust_filter_spaceType);
        ed_loation = (AutoCompleteTextView) dialog.findViewById(R.id.at_location);
        rangeSeekbar = (RangeSeekBar) dialog.findViewById(R.id.seekbar_price);

        /*RelativeLayout rl_date = (RelativeLayout) dialog.findViewById(R.id.rl_bookingList_detail_date_filter);
        RelativeLayout rl_fromTime = (RelativeLayout) dialog.findViewById(R.id.rl_bookingList_detail_from_filter);
        RelativeLayout rl_toTime = (RelativeLayout) dialog.findViewById(R.id.rl_bookingList_detail_to_filter);
*/
        tv_date = (TextView) dialog.findViewById(R.id.tv_bookingList_detail_date_filter);
        tv_from = (TextView) dialog.findViewById(R.id.tv_bookingList_detail_from_filter);
        tv_To = (TextView) dialog.findViewById(R.id.tv_bookingList_detail_to_filter);

        chk_projector = (CheckBox) dialog.findViewById(R.id.chk_space_projector);
        chk_aircondi = (CheckBox) dialog.findViewById(R.id.chk_space_air_condi);
        chk_mailservice = (CheckBox) dialog.findViewById(R.id.chk_space_mailservice);

        chk_scanner = (CheckBox) dialog.findViewById(R.id.chk_space_scanner);
        chk_locker = (CheckBox) dialog.findViewById(R.id.chk_space_locker);
        chk_internet = (CheckBox) dialog.findViewById(R.id.chk_space_internet);

        chk_parking = (CheckBox) dialog.findViewById(R.id.chk_space_parking);
        chk_phone = (CheckBox) dialog.findViewById(R.id.chk_space_phone);
        chk_work = (CheckBox) dialog.findViewById(R.id.chk_space_work);

        chk_male = (CheckBox) dialog.findViewById(R.id.chk_space_male);
        chk_female = (CheckBox) dialog.findViewById(R.id.chk_space_female);
        chk_coffee = (CheckBox) dialog.findViewById(R.id.chk_space_coffee);

        Button btn_filter = (Button) dialog.findViewById(R.id.btn_bookingList_Filter);


        chk_projector.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_aircondi.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_mailservice.setOnCheckedChangeListener(Booking_MettingRoom_List.this);

        chk_scanner.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_locker.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_internet.setOnCheckedChangeListener(Booking_MettingRoom_List.this);

        chk_parking.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_phone.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_work.setOnCheckedChangeListener(Booking_MettingRoom_List.this);

        chk_male.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_female.setOnCheckedChangeListener(Booking_MettingRoom_List.this);
        chk_coffee.setOnCheckedChangeListener(Booking_MettingRoom_List.this);

        tv_date.setText(str_date);
        tv_To.setText(str_totime);
        tv_from.setText(str_fromtime);

        str_typeName = Meeting_Room_activity.mstr_typeName;


        try {
            Task_getLocation task_getLocation = new Task_getLocation();
            task_getLocation.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.textview, ar_spacetype);
        sp_spaceType.setAdapter(arrayAdapter);

        if (!sp_spaceType.equals(null)) {
            int spinnerPosition1 = arrayAdapter.getPosition(str_typeName);
            sp_spaceType.setSelection(spinnerPosition1);
        }

        filter_aminities_init();
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        ed_loation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ed_loation.showDropDown();
                return false;
            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePicker_filter();
            }
        });

        tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                from_timePicker_filter();
            }
        });

        tv_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_timePicker_filter();
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    str_minPrice = String.valueOf(rangeSeekbar.getSelectedMinValue());
                    str_maxPrice = String.valueOf(rangeSeekbar.getSelectedMaxValue());

                    String str_from = tv_from.getText().toString().trim();
                    String str_to = tv_To.getText().toString().trim();
                    String strdate = tv_date.getText().toString().trim();

                    String str_spaceType = String.valueOf(sp_spaceType.getSelectedItemPosition() + 1);


                    data_Filter.put("user_id", str_userId);
                    data_Filter.put("date", strdate);
                    data_Filter.put("fromtime", str_from);
                    data_Filter.put("totime", str_to);
                    data_Filter.put("location", str_location);
                    data_Filter.put("space_type", str_spaceType);
                    data_Filter.put("lat", str_lat);
                    data_Filter.put("long", str_longi);


                    data_Filter.put("min_price", str_minPrice);
                    data_Filter.put("max_price", str_maxPrice);

                    data_Filter.put("is_projector", mstr_projector);
                    data_Filter.put("is_scaner", mstr_scanner);
                    data_Filter.put("is_parking", mstr_parking);

                    data_Filter.put("is_ac", mstr_aircondi);
                    data_Filter.put("is_lockers", mstr_locker);
                    data_Filter.put("is_phone", mstr_phone);

                    data_Filter.put("is_mail_service", mstr_mailservice);
                    data_Filter.put("is_internet", mstr_internet);
                    data_Filter.put("is_work_24", mstr_work);

                    data_Filter.put("is_male", mstr_male);
                    data_Filter.put("is_female", mstr_female);
                    data_Filter.put("is_coffee", mstr_coffee);


                    Log.d("Data", data_Filter.toString());

                    Task_meetingRoomBooking_filter task_meetingRoomBooking_filter = new Task_meetingRoomBooking_filter();
                    task_meetingRoomBooking_filter.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        sp_spaceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Meeting_Room_activity.mstr_typeName = sp_spaceType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.show();


    }

    private class Task_getLocation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            ar_location_filter.clear();
            ar_locationId_filter.clear();

            try {
                Getdata getdata = new Getdata();
                String mstr_location = getdata.getJSONFromUrl(Url_info.main_url + "fetch_city.php");
                JSONObject job_location = new JSONObject(mstr_location);
                status1 = job_location.getString(status);

                if (status1.equals("1")) {
                    JSONArray j_loc = job_location.getJSONArray("city");
                    for (int i = 0; i < j_loc.length(); i++) {
                        JSONObject location = j_loc.getJSONObject(i);

                        ar_locationId_filter.add(location.getString("city_id"));
                        ar_location_filter.add(location.getString("city_name"));
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

                ArrayAdapter adapter = new ArrayAdapter(Booking_MettingRoom_List.this, android.R.layout.simple_list_item_1, ar_location_filter);
                ed_loation.setAdapter(adapter);
            } else {
                Toast.makeText(Booking_MettingRoom_List.this, "" + message1, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void filter_aminities_init() {
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

        boolean c_male = chk_male.isChecked();
        if (c_male == true) {
            mstr_male = "1";
        } else {
            mstr_male = "0";
        }

        boolean c_female = chk_female.isChecked();
        if (c_female == true) {
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
    }

    private void datePicker_filter() {


        // Get Current Date
        final Calendar c;
        c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Booking_MettingRoom_List.this,
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
                        tv_date.setText(month + " " + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void to_timePicker_filter() {


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Booking_MettingRoom_List.this, timeSetListener, hour, minute, false);
        timePickerDialog.show();


    }

    private void from_timePicker_filter() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Booking_MettingRoom_List.this, timeSetListener1, hour, minute, false);
        timePickerDialog.show();


    }

    private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String str_minute;

            if (hourOfDay == 0) {
                hourOfDay += 12;
                format = "AM";
            } else if (hourOfDay == 12) {
                format = "PM";
            } else if (hourOfDay > 12) {
                hourOfDay -= 12;
                format = "PM";
            } else {
                format = "AM";
            }

            if (minute == 0) {
                str_minute = "0" + minute;
            } else {
                str_minute = String.valueOf(minute);
            }

            // mstr_toTime = hourOfDay + ":" + str_minute + " " + format;


            tv_To.setText(hourOfDay + ":" + str_minute + " " + format);
        }
    };

    private CustomTimePickerDialog.OnTimeSetListener timeSetListener1 = new CustomTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String str_minute;
            if (hourOfDay == 0) {
                hourOfDay += 12;
                format = "AM";
            } else if (hourOfDay == 12) {
                format = "PM";
            } else if (hourOfDay > 12) {
                hourOfDay -= 12;
                format = "PM";
            } else {
                format = "AM";
            }

            if (minute == 0) {
                str_minute = "0" + minute;
            } else {
                str_minute = String.valueOf(minute);
            }

            //mstr_fromTime = hourOfDay + ":" + str_minute + " " + format;

            tv_from.setText(hourOfDay + ":" + str_minute + " " + format);
        }
    };


    private void findViews() {

        linearLayout = (LinearLayout) findViewById(R.id.ll_snackbar);
        ll_tab_list = (LinearLayout) findViewById(R.id.ll_tab_list);
        ll_tab_map = (LinearLayout) findViewById(R.id.ll_tab_map);

        ll_list_details = (LinearLayout) findViewById(R.id.ll_list_details);
        ll_mapdetails = (LinearLayout) findViewById(R.id.ll_map_details);
        lst_hotelList = (ListView) findViewById(R.id.lst_tab_bookingList);

        /*tabLayout = (TabLayout) findViewById(R.id.tabs_cat);

        createTabIcons();
*/
        ll_mapdetails.setVisibility(View.GONE);

        ll_tab_map.setOnClickListener(this);
        ll_tab_list.setOnClickListener(this);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        setNewTab(this, tabHost, getResources().getString(R.string.meeting_room), R.string.meeting_room,R.drawable.meeting_room_black, R.id.ll_snackbar);
        setNewTab(this, tabHost, getResources().getString(R.string.desk), R.string.desk,R.drawable.desk_black, R.id.ll_snackbar);
        setNewTab(this, tabHost,getResources().getString(R.string.discussion_room), R.string.discussion_room, R.drawable.discussroom_black, R.id.ll_snackbar);
        setNewTab(this, tabHost,getResources().getString(R.string.private_room), R.string.private_room, R.drawable.private_room_black, R.id.ll_snackbar);
        setNewTab(this, tabHost,getResources().getString(R.string.conference_room), R.string.conference_room, R.drawable.conforance_black, R.id.ll_snackbar);
        setNewTab(this, tabHost,getResources().getString(R.string.others), R.string.others, R.drawable.other_black, R.id.ll_snackbar);


/*
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec(getResources().getString(R.string.meeting_room));
        tabSpec1.setIndicator(getString(R.string.meeting_room),getResources().getDrawable(R.drawable.meeting_room));

        tabSpec1.setContent(R.id.ll_snackbar);
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec(getResources().getString(R.string.desk));
        tabSpec2.setIndicator(getString(R.string.desk),getResources().getDrawable(R.drawable.meeting_room));
        tabSpec2.setContent(R.id.ll_snackbar);
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec(getResources().getString(R.string.desk));
        tabSpec3.setIndicator(getString(R.string.discussion_room),getResources().getDrawable(R.drawable.discussroom_black));
        tabSpec3.setContent(R.id.ll_snackbar);
        tabHost.addTab(tabSpec3);
*/

        tabHost.setCurrentTab(1);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.ll_tab_list:
                ll_list_details.setVisibility(View.VISIBLE);
                ll_mapdetails.setVisibility(View.GONE);
                //loadTabList();
                Task_meetingRoomBooking task_meetingRoomBooking = new Task_meetingRoomBooking();
                task_meetingRoomBooking.execute();

                break;
            case R.id.ll_tab_map:

                if (isNetworkAvailable()) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map1);
                    mapFragment.getMapAsync(Booking_MettingRoom_List.this);

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
                ll_list_details.setVisibility(View.GONE);
                ll_mapdetails.setVisibility(View.VISIBLE);

                break;

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
        ar_male.clear();
        ar_female.clear();
        ar_coffee.clear();
        ar_location.clear();
        ar_price.clear();
        ar_capacity.clear();
        ar_img.clear();
        ar_lat.clear();
        ar_long.clear();
        ar_distance.clear();
        ar_wish_status.clear();
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
        }
        else if (id == R.id.chk_space_male) {

            if (chk_male.isChecked()) {
                mstr_male = "1";
            } else {
                mstr_male = "0";
            }
        }
        else if (id == R.id.chk_space_female) {

            if (chk_female.isChecked()) {
                mstr_female = "1";
            } else {
                mstr_female = "0";
            }
        }
        else if (id == R.id.chk_space_coffee) {

            if (chk_coffee.isChecked()) {
                mstr_coffee = "1";
            } else {
                mstr_coffee = "0";
            }
        }

    }

    private class Task_meetingRoomBooking extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            clearArrayList();

            try {
                Postdata p_user = new Postdata();
                Log.d("list", data_meetingroom.toString());
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
                        ar_male.add(jobj_list.getString("male"));
                        ar_female.add(jobj_list.getString("female"));
                        ar_coffee.add(jobj_list.getString("coffee"));

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

/*
                adapter_meeting_room_list adapter_meeting_room_list = new adapter_meeting_room_list(Booking_MettingRoom_List.this, ar_space_id, ar_name, ar_projector,
                        ar_scanner, ar_parking, ar_ac, ar_locker, ar_ph, ar_mail, ar_wifi, ar_work,ar_male,ar_female,ar_coffee, ar_location, ar_price, ar_capacity, ar_img, ar_rating, ar_rating_count, ar_distance, ar_wish_status);

                lst_hotelList.setAdapter(adapter_meeting_room_list);*/

            } else {

                Toast.makeText(Booking_MettingRoom_List.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Booking_MettingRoom_List.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private class Task_meetingRoomBooking_filter extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            clearArrayList();

            try {
                Postdata p_user = new Postdata();
                Log.d("list", data_Filter.toString());
                String data_user = p_user.post(Url_info.main_url + "booking_datetime1.php", data_Filter.toString());
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

                        ar_male.add(jobj_list.getString("male"));
                        ar_female.add(jobj_list.getString("female"));
                        ar_coffee.add(jobj_list.getString("coffee"));


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


                /*adapter_meeting_room_list adapter_meeting_room_list = new adapter_meeting_room_list(Booking_MettingRoom_List.this, ar_space_id, ar_name, ar_projector,
                        ar_scanner, ar_parking, ar_ac, ar_locker, ar_ph, ar_mail, ar_wifi, ar_work,ar_male,ar_female,ar_coffee, ar_location, ar_price, ar_capacity, ar_img, ar_rating, ar_rating_count, ar_distance, ar_wish_status);

                lst_hotelList.setAdapter(adapter_meeting_room_list);*/
                dialog.dismiss();


                Log.d("size", String.valueOf(ar_space_id.size()));


            } else {


                Toast.makeText(Booking_MettingRoom_List.this, "" + message1, Toast.LENGTH_SHORT).show();

                Task_meetingRoomBooking task_meetingRoomBooking = new Task_meetingRoomBooking();
                task_meetingRoomBooking.execute();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Booking_MettingRoom_List.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());


        LatLng location = null;

        for (i = 0; i < ar_lat.size(); i++) {

            String str_lat = ar_lat.get(i);
            String str_lng = ar_long.get(i);

            if (str_lat.equals("0") && str_lng.equals("0")) {

            } else {
                Log.d("Location", "Name:\t" + ar_name.get(i) + "\nLat:\t" + ar_lat.get(i) + "\nlng:\t" + ar_long.get(i) + "\nimages:\t" + ar_img.get(i));

                location = new LatLng(Double.parseDouble(ar_lat.get(i)), Double.parseDouble(ar_long.get(i)));

                Gson gson = new Gson();
                String title = ar_name.get(i);
                String address = ar_location.get(i);
                String price = ar_price.get(i);
                String capacity = ar_capacity.get(i);
                String distance = ar_distance.get(i);
                String space_id=ar_space_id.get(i);
                String spnippt = gson.toJson(new Custome_Map_Item(title, address, price, capacity, distance,space_id));

                final Marker kiel = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(ar_name.get(0))
                        .snippet(spnippt)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.map)));

                markers.put(kiel.getId(), Url_info.main_img + "space/" + ar_img.get(0));

            }

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = Booking_MettingRoom_List.this.getLayoutInflater().inflate(R.layout.marker_popup,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (Booking_MettingRoom_List.this.marker != null
                    && Booking_MettingRoom_List.this.marker.isInfoWindowShown()) {
                Booking_MettingRoom_List.this.marker.hideInfoWindow();
                Booking_MettingRoom_List.this.marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            Booking_MettingRoom_List.this.marker = marker;

            String url = null;

            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null &&
                        markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final ImageView image = ((ImageView) view.findViewById(R.id.img_bookingList_hotelPic));

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {
                imageLoader.displayImage(url, image, options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view,
                                        loadedImage);
                                getInfoContents(marker);
                            }
                        });
            } else {
                image.setImageResource(R.drawable.logo3);
            }


            final String Detail_object = marker.getSnippet();
            String str_address = null;
            String str_price = null, str_distance = null, str_capacity = null, str_title = null;
            try {
                JSONObject object = new JSONObject(Detail_object);
                str_address = object.getString("Address");
                str_price = object.getString("Price");
                str_distance = object.getString("distance");
                str_capacity = object.getString("capacity");
                str_title = object.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final TextView txt_address = ((TextView) view
                    .findViewById(R.id.tv_bookingList_hotel_location));
            final TextView txt_price = (TextView) view.findViewById(R.id.tv_bookingList_hotel_price);
            final TextView txt_capacity = (TextView) view.findViewById(R.id.tv_bookingList_hotel_person_capacity);
            final TextView txt_distance = (TextView) view.findViewById(R.id.tv_bookingList_hotel_location_1);
            final TextView titleUi = ((TextView) view.findViewById(R.id.tv_bookingList_hotel_Title));


            if (str_address != null) {
                txt_address.setText(str_address);
            } else {
                txt_address.setText("");
            }
            if (str_price != null) {
                txt_price.setText(str_price);
            } else {
                txt_price.setText("");
            }
            if (str_capacity != null) {
                txt_capacity.setText(str_capacity);
            } else {
                txt_capacity.setText("");
            }
            if (str_distance != null) {
                txt_distance.setText(str_distance);
            } else {
                txt_distance.setText("");
            }
            if (str_title != null) {
                titleUi.setText(str_title);
            } else {
                titleUi.setText("");
            }
            return view;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void setNewTab(Context context, TabHost tabHost, String tag, int title, int icon, int contentID ){

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        String titleString = getString(title);
        tabSpec.setIndicator(createTabView(icon, titleString));
        tabSpec.setContent(contentID);
        tabHost.addTab(tabSpec);
    }

    private View createTabView(final int id, String tab_name) {
        View view = LayoutInflater.from(this).inflate(R.layout.custome_tabview, null);
        ImageView imageView =   (ImageView) view.findViewById(R.id.img_tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        TextView tabName = (TextView) view.findViewById(R.id.tv_tab_title);
        tabName.setText(tab_name);
        return view;
    }
}

