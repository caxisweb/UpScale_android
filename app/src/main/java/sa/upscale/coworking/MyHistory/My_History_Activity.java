package sa.upscale.coworking.MyHistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sa.upscale.coworking.MyHistory.adapter.Active_History_Adapter;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

/**
 * Created by codeclinic on 01/05/17.
 */

public class My_History_Activity extends Fragment implements View.OnClickListener {

    View view;
    LinearLayout ll_tab_active, ll_tab_inactive;
    LinearLayout ll_list_active, ll_list_inactive;

    RecyclerView rv_active, rv_inactive;


    SessionManager sessionManager;
    HashMap<String, String> getdetail = new HashMap<>();

    Active_History_Adapter activeHistoryAdapter;
    JSONObject object = new JSONObject();
    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";
    int temp = 0;
    LinearLayout linearLayout;

    ArrayList<Active_History_Item> activeHistoryItems = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_history, container, false);

        navImageIcon();
        NavigationActivity.backFlag=1;
        sessionManager = new SessionManager(getActivity());
        getdetail = sessionManager.getUserDetails();

        this.linearLayout= (LinearLayout) view.findViewById(R.id.ll_snackbar);
        this.ll_tab_active = (LinearLayout) view.findViewById(R.id.ll_tab_active);
        this.ll_tab_inactive = (LinearLayout) view.findViewById(R.id.ll_tab_inactive);
        this.ll_list_active = (LinearLayout) view.findViewById(R.id.ll_list_active);
        this.ll_list_inactive = (LinearLayout) view.findViewById(R.id.ll_list_inactive);

        this.rv_active = (RecyclerView) view.findViewById(R.id.rv_active);
        rv_active.setHasFixedSize(true);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rv_active.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(R.color.forgotButton)
                        .sizeResId(R.dimen.recyclerview_space)
                        .build());

        rv_active.setLayoutManager(layoutManager);
        rv_active.setItemAnimator(new DefaultItemAnimator());

        this.rv_inactive = (RecyclerView) view.findViewById(R.id.rv_inactive);
        rv_inactive.setHasFixedSize(true);

        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rv_inactive.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(R.color.forgotButton)
                        .sizeResId(R.dimen.recyclerview_space)
                        .build());

        rv_inactive.setLayoutManager(layoutManager1);
        rv_inactive.setItemAnimator(new DefaultItemAnimator());

        ll_tab_active.setOnClickListener(this);
        ll_tab_inactive.setOnClickListener(this);

        if (isNetworkAvailable()) {

                try {
                    ll_list_active.setVisibility(View.VISIBLE);
                    ll_list_inactive.setVisibility(View.GONE);

                    object.put("user_id", getdetail.get(SessionManager.user_Id));
                    object.put("type", "active");

                    Active active = new Active();
                    active.execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }else {
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

        return view;
    }

    private void navImageIcon() {

        ImageView imageView = (ImageView) view.findViewById(R.id.navIcon);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_nav_action_titile);
        tv_title.setText(getActivity().getString(R.string.my_history));
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.ll_tab_active:

                temp = 0;

                ll_tab_active.setBackgroundColor(getResources().getColor(R.color.white));
                ll_tab_inactive.setBackgroundColor(getResources().getColor(R.color.bookingBox));

                ll_list_active.setVisibility(View.VISIBLE);
                ll_list_inactive.setVisibility(View.GONE);

                try {

                    object.put("user_id", getdetail.get(SessionManager.user_Id));
                    object.put("type", "active");

                    Active active = new Active();
                    active.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.ll_tab_inactive:

                ll_tab_active.setBackgroundColor(getResources().getColor(R.color.bookingBox));
                ll_tab_inactive.setBackgroundColor(getResources().getColor(R.color.white));

                temp = 1;

                ll_list_active.setVisibility(View.GONE);
                ll_list_inactive.setVisibility(View.VISIBLE);

                try {

                    object.put("user_id", getdetail.get(SessionManager.user_Id));
                    object.put("type", "inactive");

                    Active active = new Active();
                    active.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    public class Active extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
            activeHistoryItems.clear();
        }

        @Override
        protected String doInBackground(String... params) {

            activeHistoryItems.clear();

            try {

                Postdata p_user = new Postdata();
                String data_user = p_user.post(Url_info.main_url + "my_booking_list.php", object.toString());
                JSONObject jobj_login = new JSONObject(data_user);
                status1 = jobj_login.getString(status);

                if (status1.equals("1")) {
                    JSONArray array = jobj_login.getJSONArray("bookings");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String bookingId=jsonObject.getString("booking_id");
                        String spce_Id=jsonObject.getString("your_space_id");
                        String Title = jsonObject.getString("name");
                        String Location = jsonObject.getString("location");
                        String Date = jsonObject.getString("booking_date");
                        String Start = jsonObject.getString("from_time");
                        String End = jsonObject.getString("to_time");
                        String repeat_b=jsonObject.getString("repeat_b");
                        String capacity=jsonObject.getString("capacity");

                        String Amount = jsonObject.getString("amount");
                        String BaseAmount = jsonObject.getString("base_amount");
                        String Hours = jsonObject.getString("hours");

                        String DateTime = Date + " " + "From " + Start + " to " + End;
                        Log.d("Capacity",spce_Id);

                        activeHistoryItems.add(new Active_History_Item(bookingId,Title, Location, DateTime, Amount, BaseAmount, Hours,Date,Start,End,spce_Id,repeat_b,capacity));
                    }


                } else {
                    message1 = jobj_login.getString(message);
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

                if (temp==0){
                    activeHistoryAdapter = new Active_History_Adapter(getActivity(), activeHistoryItems,"1");
                    rv_active.setAdapter(activeHistoryAdapter);
                }else {
                    activeHistoryAdapter = new Active_History_Adapter(getActivity(), activeHistoryItems,"0");
                    rv_inactive.setAdapter(activeHistoryAdapter);
                }

            } else {
                Toast.makeText(getActivity(), message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activeHistoryAdapter = new Active_History_Adapter(getActivity(), activeHistoryItems,"1");
        rv_active.setAdapter(activeHistoryAdapter);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (isNetworkAvailable()) {

                try {
                    ll_list_active.setVisibility(View.VISIBLE);
                    ll_list_inactive.setVisibility(View.GONE);

                    object.put("user_id", getdetail.get(SessionManager.user_Id));
                    object.put("type", "active");

                    Active active = new Active();
                    active.execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
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
        }




    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static interface AdapterCallback {
        void onMethodCallback();
    }
}
