package sa.upscale.coworking.MettingRoom;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sa.upscale.coworking.Feedback.FeedBack;
import sa.upscale.coworking.MyProfiles.WishList;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;


/**
 * A simple {@link Fragment} subclass.
 */
public class Summary extends AppCompatActivity {

    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";

    JSONObject data_code = new JSONObject();

    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();

    TextView tv_name, tv_location, tv_capacity, tv_timming, tv_reTime, tv_sprice, tv_rprice;

    String temp_switch = "0";
    String code_flag = "0";

    Switch sw_terms;

    private LinearLayout btn_confirmsummary, btn_backSummary, btn_code_apply;
    private RelativeLayout lv_final_price, lv_dis_amount;
    private LinearLayout lv_promocode, lv_apply, lv_code_cancel;
    private EditText et_promo_code;
    private TextView tv_dis_amount, tv_finalprice;

    static String coupan_code = "0", promocode_id = "0", code_type, code_amount = "0", discount_amount = "0", final_price = "0";

    String str_editHistory_type = "", str_editHistory_bookingId, str_editHistory_bookingDate,
            str_editHistory_fromTime, str_editHistory_toTime, str_editHistory_repeat, str_editHistory_enddate, str_editHistory_diffrentTime, str_editHistory_price;
    String str_d_name, str_d_location, str_d_dateTime, str_d_amount, str_d_baseamount, str_d_hour, str_d_capacity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_summary);

        custActionbar();
        findViews();

        // for history Editt
        Intent intent = getIntent();
        str_editHistory_type = intent.getStringExtra("type");//,"edit");

        sessionManager = new SessionManager(getApplicationContext());
        userDetails = sessionManager.getUserDetails();

        if (str_editHistory_type.equals("edit")) {

            str_editHistory_bookingId = intent.getStringExtra("editBooking_id");
            str_editHistory_bookingDate = intent.getStringExtra("editBooking_date");
            str_editHistory_fromTime = intent.getStringExtra("editFrom_time");
            str_editHistory_toTime = intent.getStringExtra("editto_time");
            str_editHistory_repeat = intent.getStringExtra("editRepeat");
            str_editHistory_enddate = intent.getStringExtra("editEnd_date");
            str_editHistory_diffrentTime = intent.getStringExtra("editTimeDifferent");
            str_editHistory_price = intent.getStringExtra("editPrice");

            str_d_name = intent.getStringExtra("d_name");
            str_d_capacity = intent.getStringExtra("d_capacity");
            str_d_location = intent.getStringExtra("d_location");
            str_d_dateTime = intent.getStringExtra("d_datetime");
            str_d_hour = intent.getStringExtra("d_hours");
            str_d_baseamount = intent.getStringExtra("d_baseAmount");
            str_d_amount = intent.getStringExtra("d_amount");

            String timing = str_editHistory_bookingDate + " ," + getResources().getString(R.string.from) + str_editHistory_fromTime + " " + getResources().getString(R.string.to) + str_editHistory_toTime;

            tv_name.setText(str_d_name);
            tv_location.setText(str_d_location);
            tv_capacity.setText(str_d_capacity);
            tv_timming.setText(timing);
            tv_reTime.setText(str_editHistory_diffrentTime + " h");
            tv_sprice.setText("cost " + str_d_baseamount + " per hour");

            tv_rprice.setText(str_editHistory_price);//+"SAR");


        } else if (str_editHistory_type.equals("list")) {

            //come from Normal FLow in List

            String timing = Meeting_Room_activity.mstr_book_date + " ," + getResources().getString(R.string.from) + Meeting_Room_activity.mstr_book_from + " " + getResources().getString(R.string.to) + Meeting_Room_activity.mstr_book_to;
            tv_name.setText(Booking_MettingRoom_list_details.strName);
            tv_location.setText(Booking_MettingRoom_list_details.str_location);
            tv_capacity.setText(Booking_MettingRoom_list_details.str_capacity);
            tv_timming.setText(timing);
            tv_reTime.setText(Booking_MettingRoom_list_details.mstr_book_time_diff + " h");
            tv_sprice.setText("cost " + Booking_MettingRoom_list_details.str_price + " per hour");
            tv_rprice.setText(Booking_MettingRoom_list_details.mstr_book_price);//+"SAR");

            lv_promocode.setVisibility(View.VISIBLE);

        } else {

            //wishList
            String timing = WishList.str_date_ + " ," + getResources().getString(R.string.from) + WishList.str_fromtime_ + " " + getResources().getString(R.string.to) + WishList.str_totime_;

            tv_name.setText(Booking_MettingRoom_list_details.strName);
            tv_location.setText(Booking_MettingRoom_list_details.str_location);
            tv_capacity.setText(Booking_MettingRoom_list_details.str_capacity);
            tv_timming.setText(timing);
            tv_reTime.setText(Booking_MettingRoom_list_details.mstr_book_time_diff + " h");
            tv_sprice.setText("cost " + Booking_MettingRoom_list_details.str_price + " per hour");
            tv_rprice.setText(Booking_MettingRoom_list_details.mstr_book_price);//+"SAR");


        }


        sw_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    temp_switch = "1";
                } else {
                    temp_switch = "0";
                }
            }
        });

        lv_code_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_code_apply.setVisibility(View.VISIBLE);
                lv_code_cancel.setVisibility(View.GONE);
                lv_apply.setVisibility(View.GONE);

                promocode_id = "0";
                code_flag = "0";
                discount_amount = "0";
                final_price = "0";

            }
        });

        btn_code_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideSoftKeyboard();
                coupan_code = et_promo_code.getText().toString().trim();

                if (coupan_code.equals("")) {
                    et_promo_code.setError("Please Enter Coupan Code");
                } else {

                    try {

                        data_code.put("user_id", userDetails.get(SessionManager.user_Id));
                        data_code.put("promocode", coupan_code);
                        data_code.put("space_id", Booking_MettingRoom_List.mstr_bmettingList_space_id);


                        Log.i("req", data_code.toString());

                        Task_apply task_apply = new Task_apply();
                        task_apply.execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void findViews() {

        sw_terms = (Switch) findViewById(R.id.sw_summary_terms);

        btn_confirmsummary = (LinearLayout) findViewById(R.id.ll_summary_confirm);
        btn_code_apply = (LinearLayout) findViewById(R.id.lv_code_apply);
        btn_backSummary = (LinearLayout) findViewById(R.id.ll_summary_back);

        lv_promocode = (LinearLayout) findViewById(R.id.lv_promocoe);
        lv_apply = (LinearLayout) findViewById(R.id.lv_apply);
        lv_code_cancel = (LinearLayout) findViewById(R.id.lv_code_cancel);
        lv_dis_amount = (RelativeLayout) findViewById(R.id.lv_dis_amount);
        lv_final_price = (RelativeLayout) findViewById(R.id.lv_final_price);

        et_promo_code = (EditText) findViewById(R.id.et_promo_code);
        tv_dis_amount = (TextView) findViewById(R.id.tv_dis_amount);
        tv_finalprice = (TextView) findViewById(R.id.tv_finalprice);

        tv_name = (TextView) findViewById(R.id.tv_summary_title);
        tv_location = (TextView) findViewById(R.id.tv_summary_location);
        tv_capacity = (TextView) findViewById(R.id.tv_summary_capacity);
        tv_timming = (TextView) findViewById(R.id.tv_summary_timing);
        tv_reTime = (TextView) findViewById(R.id.tv_summary_time);
        tv_sprice = (TextView) findViewById(R.id.tv_summary_sprice);
        tv_rprice = (TextView) findViewById(R.id.tv_summary_rprice);


        btn_backSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_editHistory_type.equals("edit")) {
                    //finish();
                    if (getFragmentManager().getBackStackEntryCount() == 0) {
                        Summary.this.finish();
                    } else {
                        //super.onBackPressed(); //replaced
                    }
                } else {
                    finish();
                }

            }
        });

        btn_confirmsummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (str_editHistory_type.equals("edit")) {

                    Intent intent = new Intent(Summary.this, Summary_payment.class);
                    intent.putExtra("type", str_editHistory_type);
                    intent.putExtra("editBooking_id", str_editHistory_bookingId);
                    intent.putExtra("editBooking_date", str_editHistory_bookingDate);
                    intent.putExtra("editFrom_time", str_editHistory_fromTime);
                    intent.putExtra("editto_time", str_editHistory_toTime);
                    intent.putExtra("editRepeat", str_editHistory_repeat);
                    intent.putExtra("editEnd_date", str_editHistory_enddate);
                    intent.putExtra("editTimeDifferent", str_editHistory_diffrentTime);
                    intent.putExtra("editPrice", str_editHistory_price);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(Summary.this, Summary_payment.class);
                    intent.putExtra("type", "booking");
                    startActivity(intent);

                    if (temp_switch.equals("0")) {
                        //Toast.makeText(Summary.this, "Please Enable Terms & Condition", Toast.LENGTH_SHORT).show();
                    } else {

                    /*Summary_payment summary_payment=new Summary_payment();
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.mainContent, summary_payment).commit();*/
                        startActivity(new Intent(Summary.this, Summary_payment.class));
                    }
                }


            }
        });
    }


    private class Task_apply extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata postdata = new Postdata();
                String responce = postdata.post(Url_info.main_url + "check_promocode.php", data_code.toString());

                Log.i("Responce", responce);
                JSONObject jsonObject = new JSONObject(responce);
                status1 = jsonObject.getString(status);

                if (status1.equals("1")) {

                    promocode_id = jsonObject.getString("promocode_id");
                    code_type = jsonObject.getString("type");
                    code_amount = jsonObject.getString("amount");
                    message1 = jsonObject.getString(message);

                } else {

                    message1 = jsonObject.getString(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Summary.this);
            progressDialog.setMessage("Please Wait !!!");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {

                lv_apply.setVisibility(View.VISIBLE);
                btn_code_apply.setVisibility(View.GONE);
                lv_code_cancel.setVisibility(View.VISIBLE);

                code_flag = "1";

                if (code_type.equals("percent")) {

                    discount_amount = String.valueOf((Float.parseFloat(Booking_MettingRoom_list_details.mstr_book_price) * Float.parseFloat(code_amount)) / 100);
                    tv_dis_amount.setText(discount_amount);
                    final_price = String.valueOf(Float.parseFloat(Booking_MettingRoom_list_details.mstr_book_price) - Float.parseFloat(discount_amount));
                    //Booking_MettingRoom_list_details.mstr_book_price=final_price;
                    tv_finalprice.setText(final_price);

                } else {

                    if (Float.parseFloat(code_amount) < Float.parseFloat(Booking_MettingRoom_list_details.mstr_book_price)) {

                        tv_dis_amount.setText(code_amount);
                        discount_amount = code_amount;
                        final_price = String.valueOf(Float.parseFloat(Booking_MettingRoom_list_details.mstr_book_price) - Float.parseFloat(discount_amount));
                        tv_finalprice.setText(final_price);

                    } else {

                        code_amount = Booking_MettingRoom_list_details.mstr_book_price;
                        tv_dis_amount.setText(code_amount);
                        discount_amount = code_amount;
                        final_price = String.valueOf(Float.parseFloat(Booking_MettingRoom_list_details.mstr_book_price) - Float.parseFloat(discount_amount));

                        tv_finalprice.setText(final_price);
                    }
                }

            } else {

               /* code_amount="0";
                discount_amount="0";
                final_price="0";
*/
                Toast.makeText(getApplicationContext(), message1, Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();

        }
    }

    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        TextView tv_title = (TextView) view.findViewById(R.id.action_bar_title);
        String strName = getString(R.string.summary);
        tv_title.setText(strName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_editHistory_type.equals("edit")) {
                    if (getFragmentManager().getBackStackEntryCount() == 0) {
                        Summary.this.finish();
                    } else {
                        //super.onBackPressed(); //replaced
                    }
                } else {
                    finish();
                }
            }
        });

        ImageView img_filter = (ImageView) view.findViewById(R.id.action_bar_filter);
        img_filter.setVisibility(View.GONE);
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (str_editHistory_type.equals("edit")) {
           /* finish();
            My_History_Activity history = new My_History_Activity();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainContent, history).commit();*/


            if (getFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                super.onBackPressed(); //replaced
            }

        } else {
            finish();
        }

    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_promo_code.getWindowToken(), 0);
        }
    }

}
