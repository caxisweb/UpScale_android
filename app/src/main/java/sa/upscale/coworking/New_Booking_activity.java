package sa.upscale.coworking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sa.upscale.coworking.MettingRoom.Meeting_Room_activity;

public class New_Booking_activity extends Fragment implements View.OnClickListener {

    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_sendLocation = new JSONObject();

    SessionManager sessionManager;
    android.support.v4.app.FragmentTransaction ft;

    private AutoCompleteTextView ed_loation;
    private ArrayList<String> ar_location = new ArrayList<>();
    private ArrayList<String> ar_locationId = new ArrayList<>();

    private FrameLayout btn_meetingroom, btn_desk, btn_discuroom, btn_privateroom, btn_conferenceroom, btn_others;
    private TextView tv_count1, tv_count2, tv_count3, tv_count4, tv_count5, tv_count6;
    String mstr_type = "0", mstr_typeName, mstr_count = "0", mstr_meeting = "0", mstr_desk = "0", mstr_discussion = "0", mstr_private = "0", mstr_conference = "0", mstr_other = "0";
    String str_location;
    private LinearLayout btn_next;
    LinearLayout linearLayout;
    public static String str_spaceName, location;

    View view;
    ArrayList<String> ar_selectedItem = new ArrayList<>();


    CarouselView img_slider;
    int[] sliding_images = {R.drawable.slide1, R.drawable.slider_2, R.drawable.slider_3};

    private Button btn_shareDemo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_new__booking, container, false);
        NavigationActivity.backFlag = 0;
        navImageIcon();

        sessionManager = new SessionManager(getActivity());

       /* btn_shareDemo= (Button) view.findViewById(R.id.btn_share_demo);*/

        findViews();

        img_slider.setPageCount(sliding_images.length);

        img_slider.setImageListener(imageListener);

        if (isNetworkAvailable()) {

            Task_getLocation task_getLocation = new Task_getLocation();
            task_getLocation.execute();

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

        ed_loation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ed_loation.showDropDown();
                return false;
            }
        });


        ed_loation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (isNetworkAvailable()) {
                    str_location = (String) parent.getItemAtPosition(position);
                    for (int i = 0; i < ar_locationId.size(); i++) {
                        if (str_location.equals(ar_location.get(i))) {
                            location = ar_locationId.get(i);
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed_loation.getWindowToken(), 0);
                    try {
                        data_sendLocation.put("location", location);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Task_setLocation task_setLocation = new Task_setLocation();
                    task_setLocation.execute();
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
        });

        return view;
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sliding_images[position]);
        }
    };

    private void navImageIcon() {

        ImageView imageView = view.findViewById(R.id.navIcon);
        TextView tv_title = view.findViewById(R.id.tv_nav_action_titile);
        String strName = getString(R.string.booking);
        tv_title.setText(strName);
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard(getActivity());
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void findViews() {


        img_slider = view.findViewById(R.id.imgslider_booking);

        linearLayout = view.findViewById(R.id.ll_snackbar);
        btn_meetingroom = view.findViewById(R.id.fl_space_meetingRoom);
        btn_desk = view.findViewById(R.id.fl_space_desk);
        btn_discuroom = view.findViewById(R.id.fl_space_discussonroom);
        btn_privateroom = view.findViewById(R.id.fl_space_privateRoom);
        btn_conferenceroom = view.findViewById(R.id.fl_space_conferenceRoom);
        btn_others = view.findViewById(R.id.fl_space_otherRoom);

        tv_count1 = view.findViewById(R.id.tv_newBooking_count1);
        tv_count2 = view.findViewById(R.id.tv_newBooking_count2);
        tv_count3 = view.findViewById(R.id.tv_newBooking_count3);
        tv_count4 = view.findViewById(R.id.tv_newBooking_count4);
        tv_count5 = view.findViewById(R.id.tv_newBooking_count5);
        tv_count6 = view.findViewById(R.id.tv_newBooking_count6);

        btn_next = view.findViewById(R.id.btn_bookingNext);


        ed_loation = view.findViewById(R.id.ed_locationNAme);
        ed_loation.setThreshold(1);

        btn_next.setOnClickListener(this);

        btn_meetingroom.setOnClickListener(this);
        btn_desk.setOnClickListener(this);
        btn_discuroom.setOnClickListener(this);
        btn_privateroom.setOnClickListener(this);
        btn_conferenceroom.setOnClickListener(this);
        btn_others.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.fl_space_meetingRoom:

                if (mstr_meeting != "0") {

                    /*if (temp_checked1.equals("0")) {
                        temp_checked1 = "1";
                        mstr_count = mstr_meeting;
                        mstr_itemSelectedstatus = "1";
                        mstr_meeting_status = "1";
                        btn_meetingroom.setAlpha((float) 0.5);
                        ar_selectedItem.add("Meeting ROOM");
                    } else {
                        temp_checked1 = "0";
                        mstr_count = mstr_meeting;
                        mstr_itemSelectedstatus = "0";
                        mstr_meeting_status = "0";
                        btn_meetingroom.setAlpha(1);
                        ar_selectedItem.remove("Meeting ROOM");

                    }*/


                    mstr_count = mstr_meeting;
                    mstr_type = "1";
                    btn_meetingroom.setAlpha((float) 0.5);
                    btn_desk.setAlpha(1);
                    btn_discuroom.setAlpha(1);
                    btn_privateroom.setAlpha(1);
                    btn_conferenceroom.setAlpha(1);
                    btn_others.setAlpha(1);
                    //mstr_typeName="Meeting ROOM";
                    str_spaceName = getString(R.string.meeting_room);
                    mstr_typeName = getString(R.string.meeting_room);
                }
                break;
            case R.id.fl_space_desk:

                if (mstr_desk != "0") {

                    mstr_count = mstr_desk;
                    mstr_type = "2";
                    btn_meetingroom.setAlpha(1);
                    btn_desk.setAlpha((float) 0.5);
                    btn_discuroom.setAlpha(1);
                    btn_privateroom.setAlpha(1);
                    btn_conferenceroom.setAlpha(1);
                    btn_others.setAlpha(1);

                    str_spaceName = getString(R.string.desk);
                    mstr_typeName = getString(R.string.desk);

                }
                break;
            case R.id.fl_space_discussonroom:
                if (mstr_discussion != "0") {
                    mstr_count = mstr_discussion;
                    mstr_type = "3";
                    btn_meetingroom.setAlpha(1);
                    btn_desk.setAlpha(1);
                    btn_discuroom.setAlpha((float) 0.5);
                    btn_privateroom.setAlpha(1);
                    btn_conferenceroom.setAlpha(1);
                    btn_others.setAlpha(1);

                    str_spaceName = getString(R.string.discussion_room);
                    mstr_typeName = getString(R.string.discussion_room);
                }
                break;
            case R.id.fl_space_privateRoom:

                if (mstr_private != "0") {

                    mstr_count = mstr_private;
                    mstr_type = "4";
                    btn_meetingroom.setAlpha(1);
                    btn_desk.setAlpha(1);
                    btn_discuroom.setAlpha(1);
                    btn_privateroom.setAlpha((float) 0.5);
                    btn_conferenceroom.setAlpha(1);
                    btn_others.setAlpha(1);

                    // mstr_typeName="PRIVATE ROOM";
                    str_spaceName = getString(R.string.private_room);
                    mstr_typeName = getString(R.string.private_room);

                }
                break;
            case R.id.fl_space_conferenceRoom:


                if (mstr_conference != "0") {

                    mstr_count = mstr_conference;
                    mstr_type = "5";
                    btn_meetingroom.setAlpha(1);
                    btn_desk.setAlpha(1);
                    btn_discuroom.setAlpha(1);
                    btn_privateroom.setAlpha(1);
                    btn_conferenceroom.setAlpha((float) 0.5);
                    btn_others.setAlpha(1);
                    // mstr_typeName="CONFERENCE ROOM";
                    str_spaceName = getString(R.string.conference_room);
                    mstr_typeName = getString(R.string.conference_room);

                }
                break;

            case R.id.fl_space_otherRoom:


                if (mstr_other != "0") {

                    mstr_count = mstr_other;
                    mstr_type = "6";
                    btn_meetingroom.setAlpha(1);
                    btn_desk.setAlpha(1);
                    btn_discuroom.setAlpha(1);
                    btn_privateroom.setAlpha(1);
                    btn_conferenceroom.setAlpha(1);
                    btn_others.setAlpha((float) 0.5);
                    // mstr_typeName="CONFERENCE ROOM";
                    str_spaceName = getString(R.string.others);
                    mstr_typeName = getString(R.string.others);

                }
                break;

            case R.id.btn_bookingNext:

                int arsize = ar_selectedItem.size();
                //Fragment_demo firstFragment=new Fragment_demo();
                if (ed_loation.getText().toString().length() == 0) {
                    ed_loation.setError("Please Enter CityName");
                    ed_loation.setFocusable(true);
                }/* else if (arsize==0) {
                    Toast.makeText(getActivity(), "Please Select your Space Type", Toast.LENGTH_SHORT).show();

                }else if (mstr_type.equals("0")) {
                    Toast.makeText(getActivity(), "Please Select your Space Type", Toast.LENGTH_SHORT).show();

                } */ else {
                    /*Meeting_Room_activity meeting_room_activity = new Meeting_Room_activity();

                    ft = getFragmentManager().beginTransaction();

                    Bundle bundle = new Bundle();*/

                    Intent intent = new Intent(getActivity(), Meeting_Room_activity.class);
                    intent.putExtra("location", str_location);
                    intent.putExtra("locationId", location);
                    intent.putExtra("count", mstr_count);
                    intent.putExtra("typaName", mstr_typeName);
                    intent.putExtra("space_type", mstr_type);

                    startActivity(intent);


                }

                break;


        }
    }

    private class Task_getLocation extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            ar_location.clear();
            ar_locationId.clear();

            try {
                Getdata getdata = new Getdata();
                String mstr_location = getdata.getJSONFromUrl(Url_info.main_url + "fetch_city.php");
                JSONObject job_location = new JSONObject(mstr_location);
                status1 = job_location.getString(status);

                if (status1.equals("1")) {
                    JSONArray j_loc = job_location.getJSONArray("city");
                    for (int i = 0; i < j_loc.length(); i++) {
                        JSONObject location = j_loc.getJSONObject(i);

                        ar_locationId.add(location.getString("city_id"));
                        ar_location.add(location.getString("city_name"));
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

                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ar_location);
                ed_loation.setAdapter(adapter);

                ed_loation.setText(ar_location.get(0));
                location = ar_locationId.get(0);
                try {
                    data_sendLocation.put("location", ar_locationId.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Task_setLocation task_setLocation = new Task_setLocation();
                task_setLocation.execute();

            } else {
                Toast.makeText(getActivity(), "" + message1, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class Task_setLocation extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {


            try {
                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "available_roomtype.php", data_sendLocation.toString());
                JSONObject jobj_roomtype = new JSONObject(data_user);

                status1 = jobj_roomtype.getString(status);

                if (status1.equals("1")) {

                    mstr_meeting = jobj_roomtype.getString("meeting");
                    mstr_desk = jobj_roomtype.getString("desk");
                    mstr_discussion = jobj_roomtype.getString("discussion");
                    mstr_private = jobj_roomtype.getString("private");
                    mstr_conference = jobj_roomtype.getString("conference");
                    mstr_other = jobj_roomtype.getString("others");


                } else {
                    message1 = jobj_roomtype.getString(message);
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

                if (mstr_meeting.equals("0")) {

                    tv_count1.setText(mstr_meeting);
                    btn_meetingroom.setBackgroundColor(getResources().getColor(R.color.bookingBox));
                } else {

                    tv_count1.setText(mstr_meeting);
                    btn_meetingroom.setBackground(getResources().getDrawable(R.drawable.space_item_cornor));

                }
                if (mstr_desk.equals("0")) {
                    tv_count2.setText(mstr_desk);
                    btn_desk.setBackgroundColor(getResources().getColor(R.color.bookingBox));

                } else {
                    tv_count2.setText(mstr_desk);
                    btn_desk.setBackground(getResources().getDrawable(R.drawable.space_item_cornor));
                }
                if (mstr_discussion.equals("0")) {

                    tv_count3.setText(mstr_discussion);
                    btn_discuroom.setBackgroundColor(getResources().getColor(R.color.bookingBox));
                } else {
                    tv_count3.setText(mstr_discussion);
                    btn_discuroom.setBackground(getResources().getDrawable(R.drawable.space_item_cornor));

                }
                if (mstr_private.equals("0")) {

                    tv_count4.setText(mstr_private);
                    btn_privateroom.setBackgroundColor(getResources().getColor(R.color.bookingBox));
                } else {
                    tv_count4.setText(mstr_private);
                    btn_privateroom.setBackground(getResources().getDrawable(R.drawable.space_item_cornor));

                }
                if (mstr_conference.equals("0")) {
                    tv_count5.setText(mstr_conference);
                    btn_conferenceroom.setBackgroundColor(getResources().getColor(R.color.bookingBox));

                } else {
                    tv_count5.setText(mstr_conference);
                    btn_conferenceroom.setBackground(getResources().getDrawable(R.drawable.space_item_cornor));

                }
                if (mstr_other.equals("0")) {
                    tv_count6.setText(mstr_other);
                    btn_others.setBackgroundColor(getResources().getColor(R.color.bookingBox));

                } else {
                    tv_count6.setText(mstr_other);
                    btn_others.setBackground(getResources().getDrawable(R.drawable.space_item_cornor));

                }


            } else {

                Toast.makeText(getActivity(), "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
