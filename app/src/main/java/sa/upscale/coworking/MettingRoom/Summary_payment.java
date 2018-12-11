package sa.upscale.coworking.MettingRoom;

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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sa.upscale.coworking.MyHistory.My_History_Activity;
import sa.upscale.coworking.MyProfiles.WishList;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class Summary_payment extends AppCompatActivity implements View.OnClickListener {


    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";

    JSONObject data_payment = new JSONObject();
    JSONObject visa_data_payment = new JSONObject();
    JSONObject data_updateHistory = new JSONObject();

    SessionManager sessionManager;
    HashMap<String, String> user_Details = new HashMap<>();

    LinearLayout btn_pay, btn_back, btn_cashOndelivery, btn_Visa, btn_visaDetails;
    EditText ed_cardName, ed_carNum, ed_cardDMonth, ed_cardDyear, ed_cardverification;
    String str_cardName, str_carNum, str_cardDMonth, str_cardDyear, str_cardverification;
    android.support.v4.app.FragmentTransaction ft;

    String visa_payment_url;
    static String str_booking_id, str_reference_no;
    LinearLayout linearLayout;
    String str_editHistory_type = "", str_editHistory_bookingId, str_editHistory_bookingDate,
            str_editHistory_fromTime, str_editHistory_toTime, str_editHistory_repeat, str_editHistory_enddate, str_editHistory_diffrentTime, str_editHistory_price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_payment);

        custActionbar();
        sessionManager = new SessionManager(Summary_payment.this);
        user_Details = sessionManager.getUserDetails();
        /*NavigationActivity.backFlag = 1;*/
        // navImageIcon();

        findViews();

        Intent intent = getIntent();
        str_editHistory_type = intent.getStringExtra("type");//,"edit");
        if (str_editHistory_type.equals("edit")) {

            str_editHistory_bookingId = intent.getStringExtra("editBooking_id");//,str_bookingId);
            str_editHistory_bookingDate = intent.getStringExtra("editBooking_date");//,strdate);
            str_editHistory_fromTime = intent.getStringExtra("editFrom_time");//,strfrom);
            str_editHistory_toTime = intent.getStringExtra("editto_time");//,strto);
            str_editHistory_repeat = intent.getStringExtra("editRepeat");//,str_bookEveryDayId);
            str_editHistory_enddate = intent.getStringExtra("editEnd_date");//,mstr_enddDate);
            str_editHistory_diffrentTime = intent.getStringExtra("editTimeDifferent");//, str_timeDiff);
            str_editHistory_price = intent.getStringExtra("editPrice");//,str_price_bookingrespose);
        }


    }

    private void findViews() {

        linearLayout = (LinearLayout) findViewById(R.id.ll_snackbar);
        btn_pay = (LinearLayout) findViewById(R.id.ll_summaryPayment_Pay);
        btn_back = (LinearLayout) findViewById(R.id.ll_summaryPayment_back);
        btn_cashOndelivery = (LinearLayout) findViewById(R.id.ll_summaryPayment_cashOndelivery);
        btn_Visa = (LinearLayout) findViewById(R.id.ll_summaryPayment_Visa);
        btn_visaDetails = (LinearLayout) findViewById(R.id.ll_summaryPayment_Visa_details);

        ed_cardName = (EditText) findViewById(R.id.ed_summaryPayment_cardName);
        ed_carNum = (EditText) findViewById(R.id.ed_summaryPayment_cardNumber);
        ed_cardDMonth = (EditText) findViewById(R.id.ed_summaryPayment_cardDmonth);
        ed_cardDyear = (EditText) findViewById(R.id.ed_summaryPayment_cardDyear);
        ed_cardverification = (EditText) findViewById(R.id.ed_summaryPayment_verificationCode);


        btn_cashOndelivery.setOnClickListener(this);
        btn_Visa.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_pay.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.ll_summaryPayment_cashOndelivery:
                btn_visaDetails.setVisibility(View.GONE);
                btn_pay.setVisibility(View.VISIBLE);

                if (isNetworkAvailable()) {

                    if (str_editHistory_type.equals("edit")) {

                        try {

                            data_updateHistory.put("booking_id", str_editHistory_bookingId);
                            data_updateHistory.put("booking_date", str_editHistory_bookingDate);
                            data_updateHistory.put("from_time", str_editHistory_fromTime);
                            data_updateHistory.put("to_time", str_editHistory_toTime);
                            data_updateHistory.put("repeat", str_editHistory_repeat);
                            data_updateHistory.put("end_date", str_editHistory_enddate);
                            data_updateHistory.put("total_price", str_editHistory_price);
                            data_updateHistory.put("hours", str_editHistory_diffrentTime);

                            //Log.d("Data",data_updateHistory.toString());
                            Task_history_edit task_history_edit = new Task_history_edit();
                            task_history_edit.execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {


                            if (Meeting_Room_activity.str_flag_book.equals("1")) {

                                data_payment.put("space_id", Booking_MettingRoom_List.mstr_bmettingList_space_id);
                                data_payment.put("user_id", user_Details.get(SessionManager.user_Id));
                                data_payment.put("date", Meeting_Room_activity.mstr_book_date);
                                data_payment.put("from_time", Meeting_Room_activity.mstr_book_from);
                                data_payment.put("to_time", Meeting_Room_activity.mstr_book_to);
                                data_payment.put("repeat", Meeting_Room_activity.str_bookEveryDayId);

                                if (Summary.promocode_id.equals("0")) {
                                    data_payment.put("amount", Booking_MettingRoom_list_details.mstr_book_price);
                                } else {
                                    data_payment.put("amount", Summary.final_price);
                                }

                                data_payment.put("payment_type", "1");
                                data_payment.put("base_amount", Booking_MettingRoom_list_details.str_price);
                                data_payment.put("hours", Booking_MettingRoom_list_details.mstr_book_time_diff);
                                data_payment.put("end_date", Meeting_Room_activity.mstr_enddDate);
                                data_payment.put("promocode_id", Summary.promocode_id);
                                data_payment.put("actual_amount", Booking_MettingRoom_list_details.mstr_book_price);
                                data_payment.put("discount_price", Summary.discount_amount);


                                Task_Payment task_payment1 = new Task_Payment();
                                task_payment1.execute();
                            } else {

                                data_payment.put("space_id", WishList.mstr_spaceId);
                                data_payment.put("user_id", user_Details.get(SessionManager.user_Id));
                                data_payment.put("date", WishList.str_date_);
                                data_payment.put("from_time", WishList.str_fromtime_);
                                data_payment.put("to_time", WishList.str_totime_);
                                data_payment.put("repeat", WishList.str_bookEveryDayId);
                                if (Summary.promocode_id.equals("0")) {
                                    data_payment.put("amount", Booking_MettingRoom_list_details.mstr_book_price);
                                } else {
                                    data_payment.put("amount", Summary.final_price);
                                }
                                data_payment.put("payment_type", "1");
                                data_payment.put("base_amount", Booking_MettingRoom_list_details.str_price);
                                data_payment.put("hours", Booking_MettingRoom_list_details.mstr_book_time_diff);
                                data_payment.put("end_date", WishList.str_endDate);
                                data_payment.put("promocode_id", Summary.promocode_id);
                                data_payment.put("actual_amount", Booking_MettingRoom_list_details.mstr_book_price);
                                data_payment.put("discount_price", Summary.discount_amount);


                                Task_Payment task_payment1 = new Task_Payment();
                                task_payment1.execute();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);

                    snackbar.show();

                }

                break;
            case R.id.ll_summaryPayment_Visa:

                //btn_visaDetails.setVisibility(View.VISIBLE);
                //btn_pay.setVisibility(View.VISIBLE);

                try {

                    visa_data_payment.put("InputEmail", Url_info.esaal_mer_email);
                    visa_data_payment.put("InputPassword", Url_info.esaal_mer_pass);
                    visa_data_payment.put("products", Booking_MettingRoom_list_details.str_esaal_product_id);
                    visa_data_payment.put("quantities", Booking_MettingRoom_list_details.mstr_book_time_diff);
                    visa_data_payment.put("customers", user_Details.get(SessionManager.user_essal_cust_id));
                    visa_data_payment.put("courier", "0");

                    if(Integer.parseInt(Summary.discount_amount)==0){
                        visa_data_payment.put("discounts_type", "");
                    }else {
                        if (Summary.code_type.equals("percent")) {
                            visa_data_payment.put("discounts_type", "p");
                        } else {
                            visa_data_payment.put("discounts_type", "f");
                        }
                    }

                    visa_data_payment.put("discounts",Summary.discount_amount );
                    visa_data_payment.put("delivery_options", "no");

                    Task_Payment_visa task_payment_visa = new Task_Payment_visa();
                    task_payment_visa.execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.ll_summaryPayment_back:

                finish();
                /*Summary summary = new Summary();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, summary).commit();*/
                break;
            case R.id.ll_summaryPayment_Pay:

                str_cardName = ed_cardName.getText().toString().trim();
                str_carNum = ed_carNum.getText().toString().trim();
                str_cardDMonth = ed_cardDMonth.getText().toString().trim();
                str_cardDyear = ed_cardDyear.getText().toString().trim();
                str_cardverification = ed_cardverification.getText().toString().trim();

                if (isNetworkAvailable()) {

                    if (str_editHistory_type.equals("edit")) {
                        try {


                            data_updateHistory.put("booking_id", str_editHistory_bookingId);
                            data_updateHistory.put("booking_date", str_editHistory_bookingDate);
                            data_updateHistory.put("from_time", str_editHistory_fromTime);
                            data_updateHistory.put("to_time", str_editHistory_toTime);
                            data_updateHistory.put("repeat", str_editHistory_repeat);
                            data_updateHistory.put("end_date", str_editHistory_enddate);
                            data_updateHistory.put("total_price", str_editHistory_price);
                            data_updateHistory.put("hours", str_editHistory_diffrentTime);


                            //Log.d("Data",data_updateHistory.toString());
                            Task_history_edit task_history_edit = new Task_history_edit();
                            task_history_edit.execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {


                        try {

                            if (Meeting_Room_activity.str_flag_book.equals("1")) {

                                data_payment.put("space_id", Booking_MettingRoom_List.mstr_bmettingList_space_id);
                                data_payment.put("user_id", user_Details.get(SessionManager.user_Id));
                                data_payment.put("date", Meeting_Room_activity.mstr_book_date);
                                data_payment.put("from_time", Meeting_Room_activity.mstr_book_from);
                                data_payment.put("to_time", Meeting_Room_activity.mstr_book_to);
                                data_payment.put("repeat", Meeting_Room_activity.str_bookEveryDayId);
                                if (Summary.promocode_id.equals("0")) {
                                    data_payment.put("amount", Booking_MettingRoom_list_details.mstr_book_price);
                                } else {
                                    data_payment.put("amount", Summary.final_price);
                                }

                                data_payment.put("payment_type", "1");
                                data_payment.put("base_amount", Booking_MettingRoom_list_details.str_price);
                                data_payment.put("hours", Booking_MettingRoom_list_details.mstr_book_time_diff);
                                data_payment.put("end_date", Meeting_Room_activity.mstr_enddDate);
                                data_payment.put("promocode_id", Summary.promocode_id);
                                data_payment.put("actual_amount", Booking_MettingRoom_list_details.mstr_book_price);
                                data_payment.put("discount_price", Summary.discount_amount);

                                Task_Payment task_payment1 = new Task_Payment();
                                task_payment1.execute();

                            } else {

                                data_payment.put("space_id", WishList.mstr_spaceId);
                                data_payment.put("user_id", user_Details.get(SessionManager.user_Id));
                                data_payment.put("date", WishList.str_date_);
                                data_payment.put("from_time", WishList.str_fromtime_);
                                data_payment.put("to_time", WishList.str_totime_);
                                data_payment.put("repeat", WishList.str_bookEveryDayId);

                                if (Summary.promocode_id.equals("0")) {
                                    data_payment.put("amount", Booking_MettingRoom_list_details.mstr_book_price);
                                } else {
                                    data_payment.put("amount", Summary.final_price);
                                }

                                data_payment.put("payment_type", "1");
                                data_payment.put("base_amount", Booking_MettingRoom_list_details.str_price);
                                data_payment.put("hours", Booking_MettingRoom_list_details.mstr_book_time_diff);
                                data_payment.put("end_date", WishList.str_endDate);
                                data_payment.put("promocode_id", Summary.promocode_id);
                                data_payment.put("actual_amount", Booking_MettingRoom_list_details.mstr_book_price);
                                data_payment.put("discount_price", Summary.discount_amount);

                                Task_Payment task_payment1 = new Task_Payment();
                                task_payment1.execute();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);

                    snackbar.show();

                }

               /* }*/

                break;
        }
    }

    private class Task_Payment extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata postdata = new Postdata();
                Log.i("request", data_payment.toString());
                String str_review = postdata.post(Url_info.main_url + "insert_booking.php", data_payment.toString());
                JSONObject jobj_payment = new JSONObject(str_review);
                status1 = jobj_payment.getString(status);

                if (status1.equals("1")) {

                    str_booking_id = jobj_payment.getString("booking_id");
                    str_reference_no = jobj_payment.getString("reference_no");
                } else {
                    message1 = jobj_payment.getString(message);
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

                /*ThankYou thankYou = new ThankYou();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, thankYou).commit();*/
                Intent i_thankyou = new Intent(Summary_payment.this, ThankYou.class);
                i_thankyou.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i_thankyou);

            } else {
                Toast.makeText(Summary_payment.this, "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Summary_payment.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    public class Task_history_edit extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {


            try {
                Postdata postdata = new Postdata();
                String mstr_delete = postdata.post(Url_info.main_url + "edit_booking.php", data_updateHistory.toString());
                JSONObject job_delete = new JSONObject(mstr_delete);

                status1 = job_delete.getString(status);
                if (status1.equals("1")) {

                    Log.d("updated", "success");
                } else {
                    message1 = job_delete.getString(message);
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


               /* My_History_Activity history = new My_History_Activity();
                ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, history).commit();*/
                //((Activity)mContext).recreate();
                Toast.makeText(Summary_payment.this, "Booking has been successfully changed", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(Summary_payment.this, NavigationActivity.class));


            } else {
                Toast.makeText(Summary_payment.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Summary_payment.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
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
                finish();
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private class Task_Payment_visa extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject pay_option = new JSONObject();
                pay_option.put("InputEmail", Url_info.esaal_mer_email);
                pay_option.put("InputPassword", Url_info.esaal_mer_pass);

                Postdata postdata = new Postdata();
                String str_payment_op = postdata.post(Url_info.esaal_main_url + "invoices/get_payment_options", pay_option.toString());
                JSONObject jobj_payment_op = new JSONObject(str_payment_op);

                JSONArray jobj_data_op = jobj_payment_op.getJSONArray("data");

                String pay_op_id = "0";

                for (int i = 0; i < jobj_data_op.length(); i++) {
                    JSONObject s_data = jobj_data_op.getJSONObject(i);

                    if (s_data.getString("pymntopt_label_en").equals("VISA / MASTERCARD")) {
                        pay_op_id = s_data.getString("id");
                    }
                }


                visa_data_payment.put("payment_options", pay_op_id);

                Log.i("req", visa_data_payment.toString());
                String str_url = postdata.post(Url_info.esaal_main_url + "invoices/create", visa_data_payment.toString());
                Log.i("response", str_url);
                JSONObject jobj_payment = new JSONObject(str_url);

                status1 = jobj_payment.getString("success").trim();

                Log.i("sucess", status1);

                if (status1.equals("true")) {

                    JSONArray jobj_data = jobj_payment.getJSONArray("data");

                    for (int i = 0; i < jobj_data.length(); i++) {

                        JSONObject s_data = jobj_data.getJSONObject(i);

                        visa_payment_url = s_data.getString("payment_url");

                    }


                } else {
                    message1 = jobj_payment.getString("response_message");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("true")) {

                Intent i_url = new Intent(Summary_payment.this, Visa_payment_url.class);
                i_url.putExtra("visa_url", visa_payment_url);
                startActivityForResult(i_url, 10);

            } else {
                Toast.makeText(Summary_payment.this, "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Summary_payment.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {

            if (resultCode == Activity.RESULT_OK) {

                try {

                    data_payment.put("space_id", Booking_MettingRoom_List.mstr_bmettingList_space_id);
                    data_payment.put("user_id", user_Details.get(SessionManager.user_Id));
                    data_payment.put("date", Meeting_Room_activity.mstr_book_date);
                    data_payment.put("from_time", Meeting_Room_activity.mstr_book_from);
                    data_payment.put("to_time", Meeting_Room_activity.mstr_book_to);
                    data_payment.put("repeat", Meeting_Room_activity.str_bookEveryDayId);
                    if (Summary.promocode_id.equals("0")) {
                        data_payment.put("amount", Booking_MettingRoom_list_details.mstr_book_price);
                    } else {
                        data_payment.put("amount", Summary.final_price);
                    }

                    data_payment.put("payment_type", "2");
                    data_payment.put("base_amount", Booking_MettingRoom_list_details.str_price);
                    data_payment.put("hours", Booking_MettingRoom_list_details.mstr_book_time_diff);
                    data_payment.put("end_date", Meeting_Room_activity.mstr_enddDate);
                    data_payment.put("promocode_id", Summary.promocode_id);
                    data_payment.put("actual_amount", Booking_MettingRoom_list_details.mstr_book_price);
                    data_payment.put("discount_price", Summary.discount_amount);

                    Task_Payment task_payment1 = new Task_Payment();
                    task_payment1.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(), "Payment Fail Please Try again", Toast.LENGTH_LONG).show();
            }
        }
    }
}
