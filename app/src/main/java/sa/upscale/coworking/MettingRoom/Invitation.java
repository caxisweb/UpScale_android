package sa.upscale.coworking.MettingRoom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sa.upscale.coworking.Login;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class Invitation extends AppCompatActivity {

    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";
    JSONObject data_invitation = new JSONObject();

    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();

    Button btn_send;
    TextView tv_name, tv_location, tv_capacity, tv_timming, tv_reTime, tv_sprice, tv_rprice;
    ImageView img_space;

    EditText ed_Email[];
    EditText ed_mobNo[];

    String mstr_email, mstr_contact, mstr_bookid, mstr_bookingDate, mstr_bookingTime;


    private EditText edInvitationEmail1, edInvitationMobile1, edInvitationEmail2,
            edInvitationMobile2, edInvitationEmail3, edInvitationMobile3, edInvitationEmail4,
            edInvitationMobile4, edInvitationEmail5, edInvitationMobile5, edInvitationEmail6,
            edInvitationMobile6, edInvitationEmail7, edInvitationMobile7, edInvitationEmail8,
            edInvitationMobile8, edInvitationEmail9, edInvitationMobile9;

    private String str_edInvitationEmail1 = "", str_edInvitationMobile1 = "", str_edInvitationEmail2 = "",
            str_edInvitationMobile2 = "", str_edInvitationEmail3 = "", str_edInvitationMobile3 = "", str_edInvitationEmail4 = "",
            str_edInvitationMobile4 = "", str_edInvitationEmail5 = "", str_edInvitationMobile5 = "", str_edInvitationEmail6 = "",
            str_edInvitationMobile6 = "", str_edInvitationEmail7 = "", str_edInvitationMobile7 = "", str_edInvitationEmail8 = "",
            str_edInvitationMobile8 = "", str_edInvitationEmail9 = "", str_edInvitationMobile9 = "";

    private TableRow tblRw4Input;

    private TableRow tblRw5Input;
    private TableRow tblRw6Input;
    private TableRow tblRw7Input;

    private TableRow tblRw8Input;
    private TableRow tblRw9Input;

    private LinearLayout llInvitationAddmore, llgoUp;


    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        custActionbar();

        findControl();

        sessionManager = new SessionManager(Invitation.this);
        user_details = sessionManager.getUserDetails();

        Bundle b=getIntent().getExtras();
        mstr_bookid=b.getString("booking_id");

        tv_name.setText(Booking_MettingRoom_list_details.strName);
        tv_location.setText(Booking_MettingRoom_list_details.str_location);
        tv_capacity.setText(Booking_MettingRoom_list_details.str_capacity);
        tv_sprice.setText(Booking_MettingRoom_list_details.str_price);

        Glide.with(Invitation.this).load(Url_info.main_img + "space/" + Booking_MettingRoom_list_details.str_space_img).error(R.drawable.logo1).into(img_space);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_edInvitationEmail1 = edInvitationEmail1.getText().toString().trim();
                str_edInvitationMobile1 = edInvitationMobile1.getText().toString().trim();
                str_edInvitationEmail2 = edInvitationEmail2.getText().toString().trim();
                str_edInvitationMobile2 = edInvitationMobile2.getText().toString().trim();
                str_edInvitationEmail3 = edInvitationEmail3.getText().toString().trim();
                str_edInvitationMobile3 = edInvitationMobile3.getText().toString().trim();
                str_edInvitationEmail4 = edInvitationEmail4.getText().toString().trim();
                str_edInvitationMobile4 = edInvitationMobile4.getText().toString().trim();
                str_edInvitationEmail5 = edInvitationEmail5.getText().toString().trim();
                str_edInvitationMobile5 = edInvitationMobile5.getText().toString().trim();
                str_edInvitationEmail6 = edInvitationEmail6.getText().toString().trim();
                str_edInvitationMobile6 = edInvitationMobile6.getText().toString().trim();
                str_edInvitationEmail7 = edInvitationEmail7.getText().toString().trim();
                str_edInvitationMobile7 = edInvitationMobile7.getText().toString().trim();
                str_edInvitationEmail8 = edInvitationEmail8.getText().toString().trim();
                str_edInvitationMobile8 = edInvitationMobile8.getText().toString().trim();
                str_edInvitationEmail9 = edInvitationEmail9.getText().toString().trim();
                str_edInvitationMobile9 = edInvitationMobile9.getText().toString().trim();


                mstr_email = str_edInvitationEmail1 + "," + str_edInvitationEmail2 + "," + str_edInvitationEmail3 + "," +
                        str_edInvitationEmail4 + "," + str_edInvitationEmail5 + "," + str_edInvitationEmail6 + "," +
                        str_edInvitationEmail7 + "," + str_edInvitationEmail8 + "," + str_edInvitationEmail9;

                mstr_contact = str_edInvitationMobile1 + "," + str_edInvitationMobile2 + "," + str_edInvitationMobile3 + "," +
                        str_edInvitationMobile4 + "," + str_edInvitationMobile5 + "," + str_edInvitationMobile6 + "," +
                        str_edInvitationMobile7 + "," + str_edInvitationMobile8 + "," + str_edInvitationMobile9;

                String str_1 = String.valueOf(str_edInvitationMobile1.startsWith("05"));
                String str_2 = String.valueOf(str_edInvitationMobile2.startsWith("05"));
                String str_3 = String.valueOf(str_edInvitationMobile3.startsWith("05"));
                String str_4 = String.valueOf(str_edInvitationMobile4.startsWith("05"));
                String str_5 = String.valueOf(str_edInvitationMobile5.startsWith("05"));
                String str_6 = String.valueOf(str_edInvitationMobile6.startsWith("05"));
                String str_7 = String.valueOf(str_edInvitationMobile7.startsWith("05"));
                String str_8 = String.valueOf(str_edInvitationMobile8.startsWith("05"));
                String str_9 = String.valueOf(str_edInvitationMobile9.startsWith("05"));


                if (str_edInvitationEmail1.length() == 0) {
                    edInvitationEmail1.setError("Please Enter Email");
                    edInvitationEmail1.requestFocus();
                } else if (!str_edInvitationEmail1.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
                    /*[a-zA-Z0-9._-]+@[a-z]+.[a-z]+*/
                    edInvitationEmail1.setError("Invalid Email Address");
                    edInvitationEmail1.setFocusable(true);
                } else if (str_edInvitationMobile1.length() == 0) {
                    edInvitationMobile1.setError("Please Enter Mobile");
                    edInvitationMobile1.requestFocus();

                } else if (str_1.equals("false")) {
                    edInvitationMobile1.setError("Number Start With 05");
                    edInvitationMobile1.requestFocus();
                } else if (str_edInvitationMobile1.length() != 10) {

                    edInvitationMobile1.setError("Please Enter Correct Number");
                    edInvitationMobile1.requestFocus();

                } else if (str_edInvitationEmail2.length() != 0 && !str_edInvitationEmail2.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {

                    edInvitationEmail2.setError("Invalid Email Address");
                    edInvitationEmail2.setFocusable(true);

                } else if (str_edInvitationMobile2.length() != 0 && str_edInvitationMobile2.length() != 10) {

                    edInvitationMobile2.setError("Please Enter Correct Number");
                    edInvitationMobile2.requestFocus();

                } else if (str_edInvitationMobile2.length() == 10 && str_2.equals("false")) {
                    edInvitationMobile2.setError("Number Start With 05");
                    edInvitationMobile2.requestFocus();
                } else if (str_edInvitationEmail3.length() != 0 && !str_edInvitationEmail3.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {

                    edInvitationEmail3.setError("Invalid Email Address");
                    edInvitationEmail3.setFocusable(true);

                } else if (str_edInvitationMobile3.length() != 0 && str_edInvitationMobile3.length() != 10) {

                    edInvitationMobile3.setError("Please Enter Correct Number");
                    edInvitationMobile3.requestFocus();

                } else if (str_edInvitationMobile3.length() == 10 && str_3.equals("false")) {
                    edInvitationMobile3.setError("Number Start With 05");
                    edInvitationMobile3.requestFocus();
                } else if (str_edInvitationEmail4.length() != 0 && !str_edInvitationEmail4.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {

                    edInvitationEmail4.setError("Invalid Email Address");
                    edInvitationEmail4.setFocusable(true);

                } else if (str_edInvitationMobile4.length() != 0 && str_edInvitationMobile4.length() != 10) {

                    edInvitationMobile4.setError("Please Enter Correct Number");
                    edInvitationMobile4.requestFocus();

                } else if (str_edInvitationMobile4.length() == 10 && str_4.equals("false")) {
                    edInvitationMobile4.setError("Number Start With 05");
                    edInvitationMobile4.requestFocus();
                } else if (str_edInvitationEmail5.length() != 0 && !str_edInvitationEmail5.matches("[[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {

                    edInvitationEmail5.setError("Invalid Email Address");
                    edInvitationEmail5.setFocusable(true);

                } else if (str_edInvitationMobile5.length() != 0 && str_edInvitationMobile5.length() != 10) {
                    edInvitationMobile5.setError("Please Enter Correct Number");
                    edInvitationMobile5.requestFocus();

                } else if (str_edInvitationMobile5.length() == 10 && str_5.equals("false")) {
                    edInvitationMobile5.setError("Number Start With 05");
                    edInvitationMobile5.requestFocus();
                } else if (str_edInvitationEmail6.length() != 0 && !str_edInvitationEmail6.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {

                    edInvitationEmail6.setError("Invalid Email Address");
                    edInvitationEmail6.setFocusable(true);

                } else if (str_edInvitationMobile6.length() != 0 && str_edInvitationMobile6.length() != 10) {

                    edInvitationMobile6.setError("Please Enter Correct Number");
                    edInvitationMobile6.requestFocus();

                } else if (str_edInvitationMobile6.length() == 10 && str_6.equals("false")) {
                    edInvitationMobile6.setError("Number Start With 05");
                    edInvitationMobile6.requestFocus();
                } else if (str_edInvitationEmail7.length() != 0 && !str_edInvitationEmail7.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {

                    edInvitationEmail7.setError("Invalid Email Address");
                    edInvitationEmail7.setFocusable(true);

                } else if (str_edInvitationMobile7.length() != 0 && str_edInvitationMobile7.length() != 10) {

                    edInvitationMobile7.setError("Please Enter Correct Number");
                    edInvitationMobile7.requestFocus();

                } else if (str_edInvitationMobile7.length() == 10 && str_7.equals("false")) {
                    edInvitationMobile7.setError("Number Start With 05");
                    edInvitationMobile7.requestFocus();
                } else if (str_edInvitationEmail8.length() != 0 && !str_edInvitationEmail8.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {

                    edInvitationEmail8.setError("Invalid Email Address");
                    edInvitationEmail8.setFocusable(true);

                } else if (str_edInvitationMobile8.length() != 0 && str_edInvitationMobile8.length() != 10) {

                    edInvitationMobile8.setError("Please Enter Correct Number");
                    edInvitationMobile8.requestFocus();

                } else if (str_edInvitationMobile8.length() == 10 && str_8.equals("false")) {
                    edInvitationMobile8.setError("Number Start With 05");
                    edInvitationMobile8.requestFocus();
                } else if (str_edInvitationEmail9.length() != 0 && !str_edInvitationEmail9.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$+")) {

                    edInvitationEmail9.setError("Invalid Email Address");
                    edInvitationEmail9.setFocusable(true);
                } else if (str_edInvitationMobile9.length() != 0 && str_edInvitationMobile9.length() != 10) {

                    edInvitationMobile9.setError("Please Enter Correct Number");
                    edInvitationMobile9.requestFocus();
                } else if (str_edInvitationMobile9.length() == 10 && str_9.equals("false")) {
                    edInvitationMobile9.setError("Number Start With 05");
                    edInvitationMobile9.requestFocus();
                } else {

                    try {
                        data_invitation.put("final_booking_id", mstr_bookid);
                        data_invitation.put("user_id", user_details.get(SessionManager.user_Id));
                        data_invitation.put("office_name", Booking_MettingRoom_list_details.strName);
                        //data_invitation.put("booking_date", mstr_bookingDate);
                        //data_invitation.put("booking_time", mstr_bookingTime);
                        data_invitation.put("email_id", mstr_email);
                        data_invitation.put("mobile_num", mstr_contact);

                        Log.d("Data", data_invitation.toString());

                        Task_Invitation task_invitation = new Task_Invitation();
                        task_invitation.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        llInvitationAddmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 0) {
                    tblRw4Input.setVisibility(View.VISIBLE);
                    tblRw5Input.setVisibility(View.VISIBLE);
                    tblRw6Input.setVisibility(View.VISIBLE);
                    flag = 1;
                    llgoUp.setVisibility(View.VISIBLE);
                    llInvitationAddmore.setVisibility(View.VISIBLE);
                } else {

                    tblRw7Input.setVisibility(View.VISIBLE);
                    tblRw8Input.setVisibility(View.VISIBLE);
                    tblRw9Input.setVisibility(View.VISIBLE);
                    flag = 0;
                    llgoUp.setVisibility(View.VISIBLE);
                    llInvitationAddmore.setVisibility(View.GONE);
                    edInvitationEmail4.setText("");
                    edInvitationEmail5.setText("");
                    edInvitationEmail6.setText("");
                    edInvitationEmail7.setText("");
                    edInvitationEmail8.setText("");
                    edInvitationEmail9.setText("");

                    edInvitationMobile4.setText("");
                    edInvitationMobile5.setText("");
                    edInvitationMobile6.setText("");
                    edInvitationMobile7.setText("");
                    edInvitationMobile8.setText("");
                    edInvitationMobile9.setText("");
                }
            }
        });

        llgoUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1) {
                    tblRw4Input.setVisibility(View.GONE);
                    tblRw5Input.setVisibility(View.GONE);
                    tblRw6Input.setVisibility(View.GONE);
                    flag = 0;
                    llgoUp.setVisibility(View.VISIBLE);
                    llInvitationAddmore.setVisibility(View.VISIBLE);
                } else {

                    tblRw7Input.setVisibility(View.GONE);
                    tblRw8Input.setVisibility(View.GONE);
                    tblRw9Input.setVisibility(View.GONE);
                    flag = 1;
                    llgoUp.setVisibility(View.VISIBLE);
                    llInvitationAddmore.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void findControl() {


        tv_name = (TextView) findViewById(R.id.tv_bookingList_detail_Title);
        tv_location = (TextView) findViewById(R.id.tv_bookingList_detais_location);
        tv_capacity = (TextView) findViewById(R.id.tv_bookingList_detail_person);
        tv_sprice = (TextView) findViewById(R.id.tv_price);
        img_space = (ImageView) findViewById(R.id.img_space);


        edInvitationEmail1 = (EditText) findViewById(R.id.ed_invitation_email_1);
        edInvitationMobile1 = (EditText) findViewById(R.id.ed_invitation_mobile_1);
        edInvitationEmail2 = (EditText) findViewById(R.id.ed_invitation_email_2);
        edInvitationMobile2 = (EditText) findViewById(R.id.ed_invitation_mobile_2);
        edInvitationEmail3 = (EditText) findViewById(R.id.ed_invitation_email_3);
        edInvitationMobile3 = (EditText) findViewById(R.id.ed_invitation_mobile_3);
        tblRw4Input = (TableRow) findViewById(R.id.tbl_rw_4_input);
        edInvitationEmail4 = (EditText) findViewById(R.id.ed_invitation_email_4);
        edInvitationMobile4 = (EditText) findViewById(R.id.ed_invitation_mobile_4);
        tblRw5Input = (TableRow) findViewById(R.id.tbl_rw_5_input);
        edInvitationEmail5 = (EditText) findViewById(R.id.ed_invitation_email_5);
        edInvitationMobile5 = (EditText) findViewById(R.id.ed_invitation_mobile_5);
        tblRw6Input = (TableRow) findViewById(R.id.tbl_rw_6_input);
        edInvitationEmail6 = (EditText) findViewById(R.id.ed_invitation_email_6);
        edInvitationMobile6 = (EditText) findViewById(R.id.ed_invitation_mobile_6);
        tblRw7Input = (TableRow) findViewById(R.id.tbl_rw_7_input);
        edInvitationEmail7 = (EditText) findViewById(R.id.ed_invitation_email_7);
        edInvitationMobile7 = (EditText) findViewById(R.id.ed_invitation_mobile_7);
        tblRw8Input = (TableRow) findViewById(R.id.tbl_rw_8_input);
        edInvitationEmail8 = (EditText) findViewById(R.id.ed_invitation_email_8);
        edInvitationMobile8 = (EditText) findViewById(R.id.ed_invitation_mobile_8);

        tblRw9Input = (TableRow) findViewById(R.id.tbl_rw_9_input);
        edInvitationEmail9 = (EditText) findViewById(R.id.ed_invitation_email_9);
        edInvitationMobile9 = (EditText) findViewById(R.id.ed_invitation_mobile_9);
        llInvitationAddmore = (LinearLayout) findViewById(R.id.ll_invitation_addmore);
        llgoUp = (LinearLayout) findViewById(R.id.ll_invitation_backUp);

        btn_send = (Button) findViewById(R.id.btn_invitation_Send);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(Invitation.this, NavigationActivity.class));
    }

    private class Task_Invitation extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata postdata = new Postdata();
                Log.i("request",data_invitation.toString());
                String str_invite = postdata.post(Url_info.main_url + "invite_user.php", data_invitation.toString());
                Log.i("responce",str_invite);

                JSONObject jsonObject = new JSONObject(str_invite);
                status1 = jsonObject.getString(status);

                if (status1.equals("1")) {
                    Log.d("Invitation", "Success");
                } else {
                    message1 = jsonObject.getString(message);
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

                Toast.makeText(Invitation.this, "Invitation has been successfully sent", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Invitation.this, NavigationActivity.class));

            } else {
                Toast.makeText(Invitation.this, "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(Invitation.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(getString(R.string.invitation));
            progressDialog.show();

            super.onPreExecute();
        }
    }


    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        TextView imageButton = (TextView) view.findViewById(R.id.img_back);
        TextView tv_title = (TextView) view.findViewById(R.id.action_bar_title);
        String strName = getString(R.string.invitation);
        tv_title.setText(strName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
