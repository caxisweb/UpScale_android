package sa.upscale.coworking.MyProfiles;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sa.upscale.coworking.MettingRoom.Booking_MettingRoom_list_details;
import sa.upscale.coworking.MettingRoom.TImePicker.CustomTimePickerDialog;
import sa.upscale.coworking.MyHistory.adapter.Active_History_Adapter;
import sa.upscale.coworking.MyProfiles.adapter.adapter_wish_list;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class WishList extends Fragment {


    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_wishList = new JSONObject();

    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();

    ArrayList<String> ar_space_id = new ArrayList<>();
    ArrayList<String> ar_capacity = new ArrayList<>();
    ArrayList<String> ar_name = new ArrayList<>();
    ArrayList<String> ar_email = new ArrayList<>();
    ArrayList<String> ar_phone = new ArrayList<>();
    ArrayList<String> ar_location = new ArrayList<>();
    ArrayList<String> ar_logo = new ArrayList<>();
    ArrayList<String> ar_price = new ArrayList<>();
    ArrayList<String> ar_rating = new ArrayList<>();
    ArrayList<String> ar_rating_count = new ArrayList<>();
    ArrayList<String> ar_projector = new ArrayList<>();
    ArrayList<String> ar_scanner = new ArrayList<>();
    ArrayList<String> ar_parking = new ArrayList<>();
    ArrayList<String> ar_ac = new ArrayList<>();
    ArrayList<String> ar_locker = new ArrayList<>();
    ArrayList<String> ar_ph = new ArrayList<>();
    ArrayList<String> ar_mail = new ArrayList<>();
    ArrayList<String> ar_wifi = new ArrayList<>();
    ArrayList<String> ar_work = new ArrayList<>();


    ListView lst_wishList;
    Activity mContext;

    View view;

   public static String str_date_, str_fromtime_, str_totime_,str_bookEveryDayId = "0",mstr_spaceId,str_endDate,str_flag_wish="1";
    public WishList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_wish_list, container, false);
        NavigationActivity.backFlag = 1;
        navImageIcon();
        sessionManager=new SessionManager(getActivity());
        user_details=sessionManager.getUserDetails();

        //custActionbar();
        mContext = getActivity();
        sessionManager = new SessionManager(mContext);
        user_details = sessionManager.getUserDetails();

        lst_wishList = (ListView) view.findViewById(R.id.lst_myprofile_wishList);


        Task_getWishlist task_getWishlist = new Task_getWishlist();
        task_getWishlist.execute();


       /* lst_wishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                str_capacityId=ar_capacity.get(position);
                str_spaceId=ar_space_id.get(position);
                editHistory_dlg();


            }
        });*/

        return view;
    }

    private void navImageIcon() {

        ImageView imageView = (ImageView) view.findViewById(R.id.navIcon);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_nav_action_titile);
        String strName = getString(R.string.wish_list);
        tv_title.setText(strName);
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }


    private class Task_getWishlist extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            clearArraylist();
            try {
                data_wishList.put("user_id", user_details.get(SessionManager.user_Id));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Postdata postdata = new Postdata();
                Log.d("wishList",data_wishList.toString());
                String str_dettails = postdata.post(Url_info.main_url + "fetch_favorite.php", data_wishList.toString());
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {
                    JSONArray job_fav = job_details.getJSONArray("favorite");
                    for (int i = 0; i < job_fav.length(); i++) {

                        JSONObject jobj_favorite = job_fav.getJSONObject(i);
                        ar_space_id.add(jobj_favorite.getString("space_id"));
                        ar_name.add(jobj_favorite.getString("name"));
                        ar_email.add(jobj_favorite.getString("email"));
                        ar_phone.add(jobj_favorite.getString("phone"));
                        ar_projector.add(jobj_favorite.getString("projector"));
                        ar_scanner.add(jobj_favorite.getString("scanner_printer"));
                        ar_parking.add(jobj_favorite.getString("parking"));
                        ar_ac.add(jobj_favorite.getString("ac"));
                        ar_locker.add(jobj_favorite.getString("locker"));
                        ar_ph.add(jobj_favorite.getString("ph"));
                        ar_mail.add(jobj_favorite.getString("mail"));
                        ar_wifi.add(jobj_favorite.getString("wifi"));
                        ar_work.add(jobj_favorite.getString("work_24"));
                        ar_location.add(jobj_favorite.getString("location"));
                        ar_logo.add(jobj_favorite.getString("logo"));
                        ar_price.add(jobj_favorite.getString("price"));
                        ar_rating.add(jobj_favorite.getString("rating"));
                        ar_rating_count.add(jobj_favorite.getString("rating_count"));
                        ar_capacity.add(jobj_favorite.getString("capacity"));


                    }

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

                adapter_wish_list adapter_wish_list = new adapter_wish_list(mContext, ar_space_id, ar_name, ar_email,
                        ar_phone, ar_projector, ar_scanner, ar_parking, ar_ac, ar_locker, ar_ph, ar_mail, ar_wifi, ar_work,
                        ar_location, ar_logo, ar_price, ar_rating, ar_rating_count,ar_capacity);

                lst_wishList.setAdapter(adapter_wish_list);


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
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private void clearArraylist() {

        ar_space_id.clear();
        ar_capacity.clear();
        ar_name.clear();
        ar_email.clear();
        ar_phone.clear();
        ar_location.clear();
        ar_logo.clear();
        ar_price.clear();
        ar_rating.clear();
        ar_rating_count.clear();
        ar_projector.clear();
        ar_scanner.clear();
        ar_parking.clear();
        ar_ac.clear();
        ar_locker.clear();
        ar_ph.clear();
        ar_mail.clear();
        ar_wifi.clear();
        ar_work.clear();


    }

}
