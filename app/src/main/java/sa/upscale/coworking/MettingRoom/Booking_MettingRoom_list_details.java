package sa.upscale.coworking.MettingRoom;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import sa.upscale.coworking.Login;
import sa.upscale.coworking.MettingRoom.TImePicker.CustomTimePickerDialog;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class Booking_MettingRoom_list_details extends AppCompatActivity implements View.OnClickListener {


    private static final String status = "status";
    private static final String message = "message";
    static String str_location, str_logo, str_description, str_esaal_product_id, str_spacId, str_spaceuserId, strName, str_capacity, str_price, mstr_book_time_diff, mstr_book_price, str_space_img, str_repet_no = "1";
    String status1 = "0", message1 = "try Again";
    JSONObject data_details = new JSONObject();
    JSONObject data_review = new JSONObject();
    JSONObject data_bookNow = new JSONObject();
    JSONObject visa_data_payment = new JSONObject();
    JSONObject data_code = new JSONObject();
    JSONObject data_favorite = new JSONObject();
    SessionManager session;
    HashMap<String, String> user_details = new HashMap<>();
    HashMap<String, String> lang = new HashMap<>();
    ArrayList<String> repet_type = new ArrayList<>();
    ArrayList<String> repet_no = new ArrayList<>();
    EditText et_promo_code;
    ArrayList<String> ar_dayname = new ArrayList<>();
    ArrayList<String> ar_openTime = new ArrayList<>();
    ArrayList<String> ar_closeTime = new ArrayList<>();
    ArrayList<String> ar_images = new ArrayList<>();
    ArrayList<String> ar_reviewId = new ArrayList<>();
    ArrayList<String> ar_user_id = new ArrayList<>();
    ArrayList<String> ar_user_name = new ArrayList<>();
    ArrayList<String> ar_rate = new ArrayList<>();
    ArrayList<String> ar_review = new ArrayList<>();
    ArrayList<String> ar_datetime = new ArrayList<>();
    ArrayList<String> ch_book_date = new ArrayList<>();
    ArrayList<String> book_date = new ArrayList<>();
    ArrayList<String> book_fromtime = new ArrayList<>();
    ArrayList<String> book_totime = new ArrayList<>();
    ArrayList<String> book_hour = new ArrayList<>();
    ArrayList<String> book_price = new ArrayList<>();
    ArrayList<String> book_tax_price = new ArrayList<>();
    String format;
    ImageView img_add_date;
    LinearLayout lv_bookdate;
    int book_date_count = 0;
    String strprojector = "0", strscanner = "0", strParking = "0", str_ac = "0", str_locker = "0", str_ph = "0",
            str_mail = "0", str_wifi = "0", str_work = "0", str_male = "0", str_female = "0", str_cofee = "0",
            str_rating, str_rating_count, str_wishStatus = "0";
    long total_hour = 0, final_calculate_hours;
    float total_price = 0;
    int privious_hour = 0;
    int vat_per = 0;
    float tex_amount = 0;
    String str_is_subscriber = "0";
    String str_pack_enddate;
    String str_pack_id = "0";
    String str_pack_name;
    String lati = "0", longi = "0";
    String str_bank_name, str_iban, str_account_no;
    String str_bank_name_ar, str_iban_ar, str_account_no_ar;
    String str_booking_id, str_reference_no;
    String visa_payment_url;
    RadioButton rd_visa, rd_cod, rd_bank;
    TextView tv_note_visa;
    RadioGroup rg_paymenthod;
    RelativeLayout linearLayout;
    RatingBar rating_profile;
    ImageView img_likeUnlike, img_mapdirection;
    TextView expand_button_1, expand_button_2, expand_button_3, expand_button_4;
    ExpandableLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4;
    Spinner sp_repet_type, sp_repet_no;
    LinearLayout lv_bank_detail;
    TextView tv_bank_name, tv_ibn, tv_account_no;
    TextView tv_subscriber;
    int[] logo = {R.drawable.logo1};
    String temp_sliderimg = "0", temp_imglike = "0";
    String str_type, str_spaceid, str_capacityId, str_date_, str_fromtime_, str_totime_;
    private String coupan_code = "0", promocode_id = "0", code_type, code_amount = "0", discount_amount = "0", final_price = "0";
    private TextView tv_title, tv_price, tv_date, tv_to, tv_from, tv_personCapacity, tv_location, tv_ratingreview, tv_summarydetails;
    private CarouselView img_slider;
    private Button btn_bookroom, btn_code_apply, btn_code_remove;
    private TextView tv_dis_amount;
    private int mYear, mMonth, mDay;
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


            tv_to.setText(hourOfDay + ":" + str_minute + " " + format);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__metting_room_list_details);

        custActionbar();

        session = new SessionManager(Booking_MettingRoom_list_details.this);
        //navImageIcon();
        findViews();

        Intent intent = getIntent();
        str_type = intent.getStringExtra("type");

        if (str_type.equals("list")) {

            str_spaceid = intent.getStringExtra("spaceId");
            str_capacityId = intent.getStringExtra("capacity_id");
            str_date_ = intent.getStringExtra("date");
            str_fromtime_ = intent.getStringExtra("from_time");
            str_totime_ = intent.getStringExtra("to_time");

        } else {

            // wishlist
            str_spaceid = intent.getStringExtra("spaceId");
            str_capacityId = intent.getStringExtra("capacity_id");
            str_date_ = intent.getStringExtra("date");
            str_fromtime_ = intent.getStringExtra("from_time");
            str_totime_ = intent.getStringExtra("to_time");

        }


        if (isNetworkAvailable()) {

            Task_bookinDetails task_bookinDetails = new Task_bookinDetails();
            task_bookinDetails.execute();

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

        img_slider.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                try {
                    if (temp_sliderimg.equals("0")) {
                        imageView.setImageResource(logo[position]);
                    } else {
                        Picasso.with(Booking_MettingRoom_list_details.this).load(Url_info.main_img + "space/" + ar_images.get(position)).fit().into(imageView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tv_date.setText(str_date_);
        tv_from.setText(str_fromtime_);
        tv_to.setText(str_totime_);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePicker();
            }
        });

        tv_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //from_timePicker();
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Booking_MettingRoom_list_details.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        try {

                            SimpleDateFormat hourSDF1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                            SimpleDateFormat hourSDF2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                            Date hourDt = hourSDF1.parse(selectedHour + ":" + selectedMinute);
                            tv_from.setText(hourSDF2.format(hourDt));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select From Time");
                mTimePicker.show();
            }
        });

        tv_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to_timePicker();

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                mcurrentTime.add(Calendar.HOUR, 1);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Booking_MettingRoom_list_details.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        try {

                            SimpleDateFormat hourSDF1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                            SimpleDateFormat hourSDF2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                            Date hourDt = hourSDF1.parse(selectedHour + ":" + selectedMinute);
                            tv_to.setText(hourSDF2.format(hourDt));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select To Time");
                mTimePicker.show();
            }
        });

        img_add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    if (tv_from.getText().toString().substring(tv_from.length() - 2, tv_from.length()).equals("PM") && tv_to.getText().toString().substring(tv_to.length() - 2, tv_to.length()).equals("AM")) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
                        Date date1 = simpleDateFormat.parse(tv_date.getText().toString() + " " + tv_from.getText().toString().trim());

                        String changetodate;
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(tv_date.getText().toString()));
                        c.add(Calendar.DATE, 1);
                        sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                        Date resultdate = new Date(c.getTimeInMillis());
                        changetodate = sdf.format(resultdate);

                        Date date2 = simpleDateFormat.parse(changetodate + " " + tv_to.getText().toString().trim());

                        long mills = date2.getTime() - date1.getTime();
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(mills);
                        final_calculate_hours = mills / (1000 * 60 * 60);

                        final_calculate_hours = (final_calculate_hours < 0 ? -final_calculate_hours : final_calculate_hours);

                        Log.i("hour", final_calculate_hours + "");
                        FinalCalculation();

                    } else if (tv_from.getText().toString().substring(tv_from.length() - 2, tv_from.length()).equals("AM") && tv_to.getText().toString().substring(tv_to.length() - 2, tv_to.length()).equals("PM")) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
                        Date date1 = simpleDateFormat.parse(tv_date.getText().toString() + " " + tv_from.getText().toString().trim());
                        Date date2 = simpleDateFormat.parse(tv_date.getText().toString() + " " + tv_to.getText().toString().trim());

                        long mills = date2.getTime() - date1.getTime();
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(mills);
                        final_calculate_hours = mills / (1000 * 60 * 60);

                        final_calculate_hours = (final_calculate_hours < 0 ? -final_calculate_hours : final_calculate_hours);

                        Log.i("hour", final_calculate_hours + "");
                        FinalCalculation();

                    } else {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                        Date date1 = simpleDateFormat.parse(tv_from.getText().toString().trim());
                        Date date2 = simpleDateFormat.parse(tv_to.getText().toString().trim());

                        long mills = date1.getTime() - date2.getTime();
                        final_calculate_hours = mills / (1000 * 60 * 60);

                        final_calculate_hours = (final_calculate_hours < 0 ? -final_calculate_hours : final_calculate_hours);

                        Log.i("hour", final_calculate_hours + "");

                        FinalCalculation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        rd_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_note_visa.setVisibility(View.VISIBLE);
                lv_bank_detail.setVisibility(View.GONE);
            }
        });

        rd_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_note_visa.setVisibility(View.GONE);
                lv_bank_detail.setVisibility(View.GONE);
            }
        });

        rd_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_note_visa.setVisibility(View.GONE);
                lv_bank_detail.setVisibility(View.VISIBLE);
            }
        });
    }

    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        TextView imageButton = view.findViewById(R.id.img_back);
        TextView tv_title = view.findViewById(R.id.action_bar_title);
        String strName = getString(R.string.booking);
        tv_title.setText(strName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_likeUnlike = view.findViewById(R.id.action_bar_filter);
        img_likeUnlike.setVisibility(View.VISIBLE);
        img_likeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.isLoggedIn()) {

                    user_details = session.getUserDetails();

                    try {
                        Task_InsertFavorite task_insertFavorite = new Task_InsertFavorite();
                        task_insertFavorite.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    Intent i_login = new Intent(Booking_MettingRoom_list_details.this, Login.class);
                    i_login.putExtra("flag", 1);
                    startActivity(i_login);
                }
            }
        });


        img_mapdirection = view.findViewById(R.id.action_bar_map);
        img_mapdirection.setVisibility(View.VISIBLE);
        img_mapdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Log.i("clatlong", lati + " " + longi);

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lati + "," + longi);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                    /*String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",NavigationActivity.location.getLatitude(), NavigationActivity.location.getLongitude(), Double.parseDouble(lati), Double.parseDouble(longi));

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void FinalCalculation() {

        if (final_calculate_hours > 0) {

            if (str_is_subscriber.equals("0")) {

                if (ch_book_date.indexOf(tv_date.getText().toString().trim() + "," + tv_from.getText().toString().trim() + "," + tv_to.getText().toString().trim()) < 0) {

                    btn_code_apply.setVisibility(View.VISIBLE);
                    btn_code_remove.setVisibility(View.GONE);
                    tv_dis_amount.setVisibility(View.GONE);
                    et_promo_code.setText("");

                    //tex_amount=(((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no))*vat_per)/100;
                    total_price = (Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no);
                    btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));

                    promocode_id = "0";
                    discount_amount = "0";

                    Task_checkdate task_checkdate = new Task_checkdate();
                    task_checkdate.execute();

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.duplicate_date), Toast.LENGTH_LONG).show();
                }

            } else if (str_is_subscriber.equals("1") && str_type.equals("2")) {

                if (ch_book_date.indexOf(tv_date.getText().toString().trim() + "," + tv_from.getText().toString().trim() + "," + tv_to.getText().toString().trim()) < 0) {

                    btn_code_apply.setVisibility(View.VISIBLE);
                    btn_code_remove.setVisibility(View.GONE);
                    tv_dis_amount.setVisibility(View.GONE);
                    et_promo_code.setText("");

                    //tex_amount=(((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no))*vat_per)/100;
                    total_price = (Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no);
                    btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));

                    promocode_id = "0";
                    discount_amount = "0";

                    Task_checkdate task_checkdate = new Task_checkdate();
                    task_checkdate.execute();

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.duplicate_date), Toast.LENGTH_LONG).show();
                }
            } else {
                Log.i("total_hour 2 : ", (total_hour + final_calculate_hours + privious_hour) + "");

                if ((total_hour + final_calculate_hours + privious_hour) < 5) {

                    if (ch_book_date.indexOf(tv_date.getText().toString().trim() + "," + tv_from.getText().toString().trim() + "," + tv_to.getText().toString().trim()) < 0) {

                        btn_code_apply.setVisibility(View.VISIBLE);
                        btn_code_remove.setVisibility(View.GONE);
                        tv_dis_amount.setVisibility(View.GONE);
                        et_promo_code.setText("");

                        //tex_amount=(((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no))*vat_per)/100;

                        total_price = (Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no);
                        btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));

                        promocode_id = "0";
                        discount_amount = "0";

                        Task_checkdate task_checkdate = new Task_checkdate();
                        task_checkdate.execute();

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.duplicate_date), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Your Can not book space for More than 4 Hour", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Your Can not book space for Less than 1 hour", Toast.LENGTH_LONG).show();
        }
    }

    private void findViews() {


        rating_profile = findViewById(R.id.rating_bookingList_detail_hote_rating);

        linearLayout = findViewById(R.id.ll_snackbar);

        img_slider = findViewById(R.id.imgslider_bookingDetails);

        tv_date = findViewById(R.id.tv_bookingList_detail_date);
        tv_from = findViewById(R.id.tv_bookingList_detail_from);
        tv_to = findViewById(R.id.tv_bookingList_detail_to);

        tv_title = findViewById(R.id.tv_bookingList_detail_Title);
        tv_price = findViewById(R.id.tv_price);

        tv_personCapacity = findViewById(R.id.tv_bookingList_detail_person);
        tv_location = findViewById(R.id.tv_bookingList_detais_location);
        tv_ratingreview = findViewById(R.id.tv_bookingList_detail_hote_ratingreview);

        sp_repet_type = findViewById(R.id.sp_repet_type);
        sp_repet_no = findViewById(R.id.sp_repet_no);

        lv_bookdate = findViewById(R.id.lv_bookdate);
        img_add_date = findViewById(R.id.img_add_date);

        tv_note_visa = findViewById(R.id.tv_visa_note);

        rd_cod = findViewById(R.id.rd_cod);
        rd_visa = findViewById(R.id.rd_visa);
        rd_bank = findViewById(R.id.rd_bank);
        rg_paymenthod = findViewById(R.id.rg_paymentmethod);

        lv_bank_detail = findViewById(R.id.lv_bank_detail);
        tv_bank_name = findViewById(R.id.tv_bank_name);
        tv_ibn = findViewById(R.id.tv_ibn);
        tv_account_no = findViewById(R.id.tv_account_no);

        tv_subscriber = findViewById(R.id.tv_subscriber);

        repet_type.add("Monthly");

        repet_no.add("1");
        repet_no.add("2");
        repet_no.add("3");

        ArrayAdapter<String> rep_type_adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.custome_spiner_view, R.id.tv_item, repet_type);
        sp_repet_type.setAdapter(rep_type_adp);

        final ArrayAdapter<String> rep_type_adp_no = new ArrayAdapter<String>(getApplicationContext(), R.layout.custome_spiner_view, R.id.tv_item, repet_no);
        sp_repet_no.setAdapter(rep_type_adp_no);

        expandableLayout1 = findViewById(R.id.expandable_layout_1);
        expand_button_1 = findViewById(R.id.expand_button_1);

        expandableLayout1.expand();
        expand_button_1.setTextColor(getResources().getColor(R.color.white));
        expand_button_1.setBackgroundColor(getResources().getColor(R.color.textcolor_input));
        expand_button_1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow_white, 0);

        expand_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expandableLayout1.isExpanded()) {
                    expandableLayout1.collapse();
                    expand_button_1.setTextColor(getResources().getColor(R.color.textcolor_input));
                    expand_button_1.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    expand_button_1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                } else {

                    expandableLayout1.expand();
                    expand_button_1.setTextColor(getResources().getColor(R.color.white));
                    expand_button_1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow_white, 0);
                    expand_button_1.setBackgroundColor(getResources().getColor(R.color.textcolor_input));
                }

                expandableLayout2.collapse();
                expand_button_2.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_2.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                expandableLayout3.collapse();
                expand_button_3.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_3.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                expandableLayout4.collapse();
                expand_button_4.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_4.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
            }
        });

        expandableLayout2 = findViewById(R.id.expandable_layout_2);
        expand_button_2 = findViewById(R.id.expand_button_2);
        expand_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expandableLayout2.isExpanded()) {

                    expandableLayout2.collapse();
                    expand_button_2.setTextColor(getResources().getColor(R.color.textcolor_input));
                    expand_button_2.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    expand_button_2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                } else {

                    expandableLayout2.expand();
                    expand_button_2.setTextColor(getResources().getColor(R.color.white));
                    expand_button_2.setBackgroundColor(getResources().getColor(R.color.textcolor_input));
                    expand_button_2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow_white, 0);

                }

                expandableLayout1.collapse();
                expand_button_1.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_1.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                expandableLayout3.collapse();
                expand_button_3.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_3.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                expandableLayout4.collapse();
                expand_button_4.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_4.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
            }
        });

        expandableLayout3 = findViewById(R.id.expandable_layout_3);
        expand_button_3 = findViewById(R.id.expand_button_3);
        expand_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expandableLayout3.isExpanded()) {

                    expandableLayout3.collapse();
                    expand_button_3.setTextColor(getResources().getColor(R.color.textcolor_input));
                    expand_button_3.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    expand_button_3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                } else {

                    expandableLayout3.expand();
                    expand_button_3.setTextColor(getResources().getColor(R.color.white));
                    expand_button_3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow_white, 0);
                    expand_button_3.setBackgroundColor(getResources().getColor(R.color.textcolor_input));
                }

                expandableLayout1.collapse();
                expand_button_1.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_1.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                expandableLayout2.collapse();
                expand_button_2.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_2.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);

                expandableLayout4.collapse();
                expand_button_4.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_4.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
            }
        });

        expandableLayout4 = findViewById(R.id.expandable_layout_4);
        expand_button_4 = findViewById(R.id.expand_button_4);
        expand_button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (expandableLayout4.isExpanded()) {
                    expandableLayout4.collapse();
                    expand_button_4.setTextColor(getResources().getColor(R.color.textcolor_input));
                    expand_button_4.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    expand_button_4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                } else {
                    expandableLayout4.expand();
                    expand_button_4.setTextColor(getResources().getColor(R.color.white));
                    expand_button_4.setBackgroundColor(getResources().getColor(R.color.textcolor_input));
                    expand_button_4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow_white, 0);
                }

                expandableLayout1.collapse();
                expand_button_1.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_1.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                expandableLayout2.collapse();
                expand_button_2.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_2.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
                expandableLayout3.collapse();
                expand_button_3.setTextColor(getResources().getColor(R.color.textcolor_input));
                expand_button_3.setBackgroundColor(getResources().getColor(R.color.light_gray));
                expand_button_3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0);
            }
        });

        tv_summarydetails = findViewById(R.id.tv_summarydetails);

        tv_dis_amount = findViewById(R.id.tv_dis_amount);

        et_promo_code = findViewById(R.id.et_promo_code);
        btn_bookroom = findViewById(R.id.btn_book_room);
        btn_code_apply = findViewById(R.id.btn_code_apply);
        btn_code_remove = findViewById(R.id.btn_code_remove);


        btn_bookroom.setOnClickListener(this);
        btn_code_apply.setOnClickListener(this);
        btn_code_remove.setOnClickListener(this);


    }

    private void to_timePicker() {


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Booking_MettingRoom_list_details.this, timeSetListener, hour, minute, false);
        timePickerDialog.show();

        /*Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, Calendar.HOUR_OF_DAY);

        m_to_Hour = now.get(Calendar.HOUR_OF_DAY);
        m_to_Minute = 00;

        TimePickerDialog timePickerDialog = new TimePickerDialog(Booking_MettingRoom_list_details.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        tv_from.setText(hourOfDay + ":" + minute);
                    }
                }, m_from_Hour, m_from_Minute, false);
        timePickerDialog.show();*/


    }

    private void from_timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(Booking_MettingRoom_list_details.this, timeSetListener1, hour, minute, false);
        timePickerDialog.show();


        /*final Calendar c;
        c = Calendar.getInstance();

        m_from_Hour = c.get(Calendar.HOUR_OF_DAY);
        m_from_Minute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(Booking_MettingRoom_list_details.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        tv_from.setText(hourOfDay + ":" + minute);
                    }
                }, m_from_Hour, m_from_Minute, false);
        timePickerDialog.show();*/

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.tv_bookingList_detail_date:

                break;

            case R.id.tv_bookingList_detail_review_more:
                startActivity(new Intent(Booking_MettingRoom_list_details.this, Booking_mettingRoom_list_details_moreReview.class));
                break;

            case R.id.btn_code_remove:

                btn_code_apply.setVisibility(View.VISIBLE);
                btn_code_remove.setVisibility(View.GONE);
                tv_dis_amount.setVisibility(View.GONE);
                et_promo_code.setText("");

                total_price = ((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no));
                btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));

                promocode_id = "0";
                discount_amount = "0";

                break;

            case R.id.btn_code_apply:

                hideSoftKeyboard();

                if (isNetworkAvailable()) {

                    if (book_date.size() > 0) {

                        if (session.isLoggedIn()) {

                            if (str_is_subscriber.equals("0")) {
                                coupan_code = et_promo_code.getText().toString().trim();

                                if (coupan_code.equals("")) {
                                    et_promo_code.setError("Please Enter Coupan Code");
                                } else {

                                    user_details = session.getUserDetails();

                                    try {

                                        data_code.put("user_id", user_details.get(SessionManager.user_Id));
                                        data_code.put("promocode", coupan_code);
                                        data_code.put("space_id", str_spaceid);

                                        Task_apply task_apply = new Task_apply();
                                        task_apply.execute();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {

                                Toast.makeText(getApplicationContext(), "You are Already Subscriber", Toast.LENGTH_LONG).show();
                            }
                        } else {

                            Intent i_login = new Intent(Booking_MettingRoom_list_details.this, Login.class);
                            i_login.putExtra("flag", 1);
                            startActivity(i_login);
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Please Select Date For booking", Toast.LENGTH_LONG).show();
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
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);

                    snackbar.show();

                }

                break;

            case R.id.btn_book_room:

                if (isNetworkAvailable()) {

                    if (session.isLoggedIn()) {
                        if (book_date.size() > 0) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(Booking_MettingRoom_list_details.this);
                            builder.setMessage("Are sure to make this Booking ?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!

                                            int sel_method = rg_paymenthod.getCheckedRadioButtonId();

                                            if (R.id.rd_cod == sel_method) {

                                                if (str_is_subscriber.equals("0")) {

                                                    try {

                                                        user_details = session.getUserDetails();

                                                        data_bookNow.put("space_id", str_spaceid);
                                                        data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                                                        data_bookNow.put("total_cost", String.valueOf(total_price));
                                                        data_bookNow.put("payment_type", "1");
                                                        data_bookNow.put("promocode_id", promocode_id);
                                                        data_bookNow.put("transcation_id", "0");
                                                        data_bookNow.put("payment_time", "");
                                                        data_bookNow.put("discount_amount", discount_amount);

                                                        JSONArray booking = new JSONArray();

                                                        for (int p = 0; p < book_date.size(); p++) {

                                                            JSONObject job_book = new JSONObject();
                                                            job_book.put("date", book_date.get(p));
                                                            job_book.put("from_time", book_fromtime.get(p));
                                                            job_book.put("to_time", book_totime.get(p));
                                                            job_book.put("base_amount", str_price);
                                                            job_book.put("hours", book_hour.get(p));
                                                            String dis_am = String.valueOf(((Float.parseFloat(book_price.get(p)) * Float.parseFloat(str_repet_no) * Float.parseFloat(code_amount)) / 100));

                                                            float tax_amount_i = ((Float.parseFloat(book_price.get(p)) - Float.parseFloat(dis_am)) * vat_per) / 100;
                                                            job_book.put("tax_amount", String.valueOf(tax_amount_i));

                                                            job_book.put("amount", String.valueOf(Double.parseDouble(book_price.get(p)) - Double.parseDouble(dis_am) + tax_amount_i));
                                                            job_book.put("discount_price", String.valueOf(Double.parseDouble(dis_am)));
                                                            job_book.put("actual_amount", String.valueOf(book_price.get(p)));

                                                            booking.put(job_book);
                                                        }

                                                        data_bookNow.put("booking", booking);
                                                        data_bookNow.put("package_id", str_pack_id);
                                                        data_bookNow.put("is_subscriber", str_is_subscriber);
                                                        data_bookNow.put("booking_from", "1");

                                                        Task_Payment task_payment1 = new Task_Payment();
                                                        task_payment1.execute();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    try {

                                                        user_details = session.getUserDetails();

                                                        data_bookNow.put("space_id", str_spaceid);
                                                        data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                                                        data_bookNow.put("total_cost", "0");
                                                        data_bookNow.put("payment_type", "1");
                                                        data_bookNow.put("promocode_id", promocode_id);
                                                        data_bookNow.put("transcation_id", "0");
                                                        data_bookNow.put("payment_time", "");
                                                        data_bookNow.put("discount_amount", "0");

                                                        JSONArray booking = new JSONArray();

                                                        for (int p = 0; p < book_date.size(); p++) {

                                                            JSONObject job_book = new JSONObject();
                                                            job_book.put("date", book_date.get(p));
                                                            job_book.put("from_time", book_fromtime.get(p));
                                                            job_book.put("to_time", book_totime.get(p));
                                                            job_book.put("base_amount", str_price);
                                                            job_book.put("hours", book_hour.get(p));

                                                            job_book.put("tax_amount", "0");

                                                            job_book.put("amount", "0");
                                                            job_book.put("discount_price", "0");
                                                            job_book.put("actual_amount", "0");

                                                            booking.put(job_book);
                                                        }

                                                        data_bookNow.put("booking", booking);
                                                        data_bookNow.put("package_id", str_pack_id);
                                                        data_bookNow.put("is_subscriber", str_is_subscriber);
                                                        data_bookNow.put("booking_from", "1");

                                                        Task_Payment task_payment1 = new Task_Payment();
                                                        task_payment1.execute();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } else if (R.id.rd_bank == sel_method) {

                                                if (str_is_subscriber.equals("0")) {
                                                    try {

                                                        user_details = session.getUserDetails();

                                                        data_bookNow.put("space_id", str_spaceid);
                                                        data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                                                        data_bookNow.put("total_cost", String.valueOf(total_price));
                                                        data_bookNow.put("payment_type", "3");
                                                        data_bookNow.put("promocode_id", promocode_id);
                                                        data_bookNow.put("transcation_id", "0");
                                                        data_bookNow.put("payment_time", "");
                                                        data_bookNow.put("discount_amount", discount_amount);

                                                        JSONArray booking = new JSONArray();

                                                        for (int p = 0; p < book_date.size(); p++) {

                                                            JSONObject job_book = new JSONObject();
                                                            job_book.put("date", book_date.get(p));
                                                            job_book.put("from_time", book_fromtime.get(p));
                                                            job_book.put("to_time", book_totime.get(p));
                                                            job_book.put("base_amount", str_price);
                                                            job_book.put("hours", book_hour.get(p));
                                                            String dis_am = String.valueOf(((Float.parseFloat(book_price.get(p)) * Float.parseFloat(str_repet_no) * Float.parseFloat(code_amount)) / 100));

                                                            float tax_amount_i = ((Float.parseFloat(book_price.get(p)) - Float.parseFloat(dis_am)) * vat_per) / 100;
                                                            job_book.put("tax_amount", String.valueOf(tax_amount_i));

                                                            job_book.put("amount", String.valueOf(Double.parseDouble(book_price.get(p)) - Double.parseDouble(dis_am) + tax_amount_i));
                                                            job_book.put("discount_price", String.valueOf(Double.parseDouble(dis_am)));
                                                            job_book.put("actual_amount", String.valueOf(book_price.get(p)));
                                                            booking.put(job_book);
                                                        }

                                                        data_bookNow.put("booking", booking);
                                                        data_bookNow.put("package_id", str_pack_id);
                                                        data_bookNow.put("is_subscriber", str_is_subscriber);
                                                        data_bookNow.put("booking_from", "1");

                                                        Task_Payment task_payment1 = new Task_Payment();
                                                        task_payment1.execute();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    try {

                                                        user_details = session.getUserDetails();

                                                        data_bookNow.put("space_id", str_spaceid);
                                                        data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                                                        data_bookNow.put("total_cost", "0");
                                                        data_bookNow.put("payment_type", "3");
                                                        data_bookNow.put("promocode_id", promocode_id);
                                                        data_bookNow.put("transcation_id", "0");
                                                        data_bookNow.put("payment_time", "");
                                                        data_bookNow.put("discount_amount", "0");

                                                        JSONArray booking = new JSONArray();

                                                        for (int p = 0; p < book_date.size(); p++) {

                                                            JSONObject job_book = new JSONObject();
                                                            job_book.put("date", book_date.get(p));
                                                            job_book.put("from_time", book_fromtime.get(p));
                                                            job_book.put("to_time", book_totime.get(p));
                                                            job_book.put("base_amount", str_price);
                                                            job_book.put("hours", book_hour.get(p));
                                                            job_book.put("tax_amount", "0");
                                                            job_book.put("amount", "0");
                                                            job_book.put("discount_price", "0");
                                                            job_book.put("actual_amount", "0");
                                                            booking.put(job_book);
                                                        }

                                                        data_bookNow.put("booking", booking);
                                                        data_bookNow.put("package_id", str_pack_id);
                                                        data_bookNow.put("is_subscriber", str_is_subscriber);
                                                        data_bookNow.put("booking_from", "1");

                                                        Task_Payment task_payment1 = new Task_Payment();
                                                        task_payment1.execute();

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            } else if (R.id.rd_visa == sel_method) {

                                                try {

                                                    visa_data_payment.put("InputEmail", Url_info.esaal_mer_email);
                                                    visa_data_payment.put("InputPassword", Url_info.esaal_mer_pass);
                                                    visa_data_payment.put("products", str_esaal_product_id);
                                                    visa_data_payment.put("quantities", total_hour);
                                                    visa_data_payment.put("customers", user_details.get(SessionManager.user_essal_cust_id));
                                                    visa_data_payment.put("courier", "0");

                                                    if (Integer.parseInt(Summary.discount_amount) == 0) {
                                                        visa_data_payment.put("discounts_type", "");
                                                    } else {
                                                        if (Summary.code_type.equals("percent")) {
                                                            visa_data_payment.put("discounts_type", "p");
                                                        } else {
                                                            visa_data_payment.put("discounts_type", "f");
                                                        }
                                                    }

                                                    visa_data_payment.put("discounts", discount_amount);
                                                    visa_data_payment.put("delivery_options", "no");

                                                   /* Task_Payment_visa task_payment_visa = new Task_Payment_visa();
                                                    task_payment_visa.execute();
*/
                                                    Toast.makeText(getApplicationContext(), "Visa Payment Service is temporary Not available", Toast.LENGTH_LONG).show();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog

                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.create().show();
                        } else {

                            Toast.makeText(getApplicationContext(), "Please Select Date For booking", Toast.LENGTH_LONG).show();

                        }
                    } else {

                        Intent i_login = new Intent(Booking_MettingRoom_list_details.this, Login.class);
                        i_login.putExtra("flag", 1);
                        startActivity(i_login);

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
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);

                    snackbar.show();

                }

                break;


        }

    }

    private void datePicker() {

        final Calendar c;
        c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                            str_date_ = sdf.format(sdf.parse(month + " " + dayOfMonth + ", " + year));
                            tv_date.setText(str_date_);

                        } catch (Exception e) {

                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        if (str_is_subscriber.equals("0")) {

        } else {
            try {


                Log.i("end_date", str_pack_enddate);
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(str_pack_enddate.trim());
                long milliseconds = date.getTime();
                Log.i("Time Mili secound : ", "" + milliseconds);
                datePickerDialog.getDatePicker().setMaxDate(milliseconds);


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {

            if (resultCode == Activity.RESULT_OK) {

                if (str_is_subscriber.equals("0")) {
                    try {


                        data_bookNow.put("space_id", str_spaceid);
                        data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                        data_bookNow.put("total_cost", String.valueOf(total_price));
                        data_bookNow.put("payment_type", "2");
                        data_bookNow.put("promocode_id", promocode_id);
                        data_bookNow.put("transcation_id", "0");
                        data_bookNow.put("payment_time", "");
                        data_bookNow.put("discount_amount", discount_amount);

                        JSONArray booking = new JSONArray();

                        for (int p = 0; p < book_date.size(); p++) {

                            JSONObject job_book = new JSONObject();
                            job_book.put("date", book_date.get(p));
                            job_book.put("from_time", book_fromtime.get(p));
                            job_book.put("to_time", book_totime.get(p));
                            job_book.put("base_amount", str_price);
                            job_book.put("hours", book_hour.get(p));
                            job_book.put("actual_amount", book_price.get(p));
                            job_book.put("discount_price", "0");
                            job_book.put("amount", book_price.get(p));

                            booking.put(job_book);
                        }

                        data_bookNow.put("booking", booking);
                        data_bookNow.put("package_id", str_pack_id);
                        data_bookNow.put("is_subscriber", str_is_subscriber);
                        data_bookNow.put("booking_from", "1");

                        Task_Payment task_payment1 = new Task_Payment();
                        task_payment1.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    try {

                        user_details = session.getUserDetails();

                        data_bookNow.put("space_id", str_spaceid);
                        data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                        data_bookNow.put("total_cost", "0");
                        data_bookNow.put("payment_type", "1");
                        data_bookNow.put("promocode_id", promocode_id);
                        data_bookNow.put("transcation_id", "0");
                        data_bookNow.put("payment_time", "");
                        data_bookNow.put("discount_amount", "0");

                        JSONArray booking = new JSONArray();

                        for (int p = 0; p < book_date.size(); p++) {

                            JSONObject job_book = new JSONObject();
                            job_book.put("date", book_date.get(p));
                            job_book.put("from_time", book_fromtime.get(p));
                            job_book.put("to_time", book_totime.get(p));
                            job_book.put("base_amount", str_price);
                            job_book.put("hours", book_hour.get(p));
                            job_book.put("amount", "0");
                            job_book.put("discount_price", "0");
                            job_book.put("actual_amount", "0");

                            booking.put(job_book);
                        }

                        data_bookNow.put("booking", booking);
                        data_bookNow.put("package_id", str_pack_id);
                        data_bookNow.put("is_subscriber", str_is_subscriber);
                        data_bookNow.put("booking_from", "1");

                        Task_Payment task_payment1 = new Task_Payment();
                        task_payment1.execute();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(), "Payment Fail Please Try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_promo_code.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {

        //Task_bookinDetails task_bookinDetails = new Task_bookinDetails();
        //task_bookinDetails.execute();

        super.onResume();

    }

    private class Task_bookinDetails extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            ar_closeTime.clear();
            ar_openTime.clear();
            ar_images.clear();
            ar_dayname.clear();

            ar_reviewId.clear();
            ar_user_id.clear();
            ar_user_name.clear();
            ar_rate.clear();
            ar_review.clear();
            ar_datetime.clear();


            try {

                data_details.put("user_id", user_details.get(SessionManager.user_Id));
                data_details.put("your_space_id", str_spaceid);
                data_details.put("capacity_id", str_capacityId);

                if (session.isLoggedIn()) {
                    user_details = session.getUserDetails();
                    data_details.put("user_id", user_details.get(SessionManager.user_Id));
                } else {
                    data_details.put("user_id", "0");
                }

                Postdata postdata = new Postdata();
                Log.d("Review", data_details.toString());
                String str_dettails = postdata.post(Url_info.main_url + "your_space_detail.php", data_details.toString());
                Log.i("responce", str_dettails);

                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {

                    str_spacId = job_details.getString("space_id");
                    str_esaal_product_id = job_details.getString("esaal_product_id");
                    str_spaceuserId = job_details.getString("space_user_id");

                    strName = job_details.getString("name");
                    strprojector = job_details.getString("projector");
                    strscanner = job_details.getString("scanner");
                    strParking = job_details.getString("parking");
                    str_ac = job_details.getString("ac");
                    str_locker = job_details.getString("locker");
                    str_ph = job_details.getString("ph");
                    str_mail = job_details.getString("mail");
                    str_wifi = job_details.getString("wifi");
                    str_work = job_details.getString("work");

                    str_male = job_details.getString("male");
                    str_female = job_details.getString("female");
                    str_cofee = job_details.getString("coffee");

                    str_location = job_details.getString("location");
                    str_description = job_details.getString("description");
                    str_logo = job_details.getString("logo");
                    str_price = job_details.getString("price");
                    str_capacity = job_details.getString("capacity");
                    str_rating = job_details.getString("rating");
                    str_rating_count = job_details.getString("rating_count");
                    str_wishStatus = job_details.getString("wish_status");
                    lati = job_details.getString("lat");
                    longi = job_details.getString("long");
                    str_bank_name = job_details.getString("bank_name");
                    str_iban = job_details.getString("iban");
                    str_account_no = job_details.getString("account_no");

                    str_bank_name_ar = job_details.getString("bank_name_ar");
                    str_iban_ar = job_details.getString("iban_ar");
                    str_account_no_ar = job_details.getString("account_no_ar");

                    str_is_subscriber = job_details.getString("is_subscriber");
                    //str_is_subscriber = "1";
                    str_pack_enddate = job_details.getString("package_end_date");
                    str_pack_id = job_details.getString("package_id");
                    str_pack_name = job_details.getString("package_name");
                    vat_per = Integer.parseInt(job_details.getString("vat_per"));

                    Log.i("vat_per", vat_per + "");

                    if (job_details.getString("images").equals("null")) {

                    } else {

                        JSONArray jobj_images = job_details.getJSONArray("images");

                        for (int i = 0; i < jobj_images.length(); i++) {

                            JSONObject images = jobj_images.getJSONObject(i);
                            ar_images.add(images.getString("image"));

                        }
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

                status1 = "0";

                if (ar_images.size() == 0) {
                    temp_sliderimg = "0";
                    img_slider.setPageCount(logo.length);
                } else {
                    temp_sliderimg = "1";
                    img_slider.setPageCount(ar_images.size());
                }

                if (str_wishStatus.equals("1")) {
                    img_likeUnlike.setImageResource(R.mipmap.like);
                } else {
                    img_likeUnlike.setImageResource(R.drawable.heart);
                }

                rating_profile.setRating(Float.parseFloat(str_rating));

                tv_personCapacity.setText(str_capacity);
                tv_title.setText(strName);
                tv_location.setText(str_location);
                tv_ratingreview.setText("(" + str_rating_count + " reviews)");
                tv_summarydetails.setText(str_description);
                tv_price.setText(str_price);

                if (str_bank_name.equals("")) {

                    rd_bank.setEnabled(false);

                } else {

                    lang = session.getlanguageCode();

                    String str_lang_code = lang.get(SessionManager.user_languageCode);

                    if (str_lang_code == null) {

                        tv_bank_name.setText(getResources().getString(R.string.bank_name) + " : " + str_bank_name);
                        tv_ibn.setText(getResources().getString(R.string.ibn_no) + " : " + str_iban);
                        tv_account_no.setText(getResources().getString(R.string.acc_no) + " : " + str_account_no);

                    } else if (str_lang_code.equals("1")) {

                        tv_bank_name.setText(getResources().getString(R.string.bank_name) + " : " + str_bank_name);
                        tv_ibn.setText(getResources().getString(R.string.ibn_no) + " : " + str_iban);
                        tv_account_no.setText(getResources().getString(R.string.acc_no) + " : " + str_account_no);

                    } else if (str_lang_code.equals("2")) {

                        tv_bank_name.setText(getResources().getString(R.string.bank_name) + " : " + str_bank_name_ar);
                        tv_ibn.setText(getResources().getString(R.string.ibn_no) + " : " + str_iban_ar);
                        tv_account_no.setText(getResources().getString(R.string.acc_no) + " : " + str_account_no_ar);
                    }
                }

                if (ar_images.size() == 0) {
                    str_space_img = " ";
                } else {
                    str_space_img = ar_images.get(0);
                }

                if (str_is_subscriber.equals("0")) {
                    tv_subscriber.setVisibility(View.GONE);
                    btn_bookroom.setText(str_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));
                } else {
                    tv_subscriber.setVisibility(View.VISIBLE);
                    tv_subscriber.setText(getResources().getString(R.string.subscribed_msg) + " " + str_pack_name);
                    btn_bookroom.setText("Book Now");
                }

                if (str_is_subscriber.equals("1")) {

                    book_date.clear();
                    book_totime.clear();
                    book_fromtime.clear();
                    book_date.clear();
                    book_hour.clear();
                    book_price.clear();
                    ch_book_date.clear();

                    total_hour = 0;
                    total_price = 0;
                    lv_bookdate.removeAllViews();
                }

            } else {
                Toast.makeText(Booking_MettingRoom_list_details.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Booking_MettingRoom_list_details.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(getString(R.string.fatching_Information));
            progressDialog.show();

        }
    }

    private class Task_InsertFavorite extends AsyncTask<String, String, String> {

        String wish_status_1;

        @Override
        protected String doInBackground(String... params) {


            try {

                data_favorite.put("user_id", user_details.get(SessionManager.user_Id));
                data_favorite.put("space_id", str_spacId);

                Postdata postdata = new Postdata();
                String str_dettails = postdata.post(Url_info.main_url + "insert_favorite.php", data_favorite.toString());
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {

                    wish_status_1 = job_details.getString("wish_status");
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

                status1 = "0";

                if (wish_status_1.equals("1")) {
                    img_likeUnlike.setImageResource(R.mipmap.like);
                    //templike="1";
                } else {
                    //templike="0";
                    img_likeUnlike.setImageResource(R.drawable.heart);
                }

            } else {
                Toast.makeText(Booking_MettingRoom_list_details.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            //progressDialog.dismiss();
        }


    }

    private class Task_checkdate extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        JSONObject data_ch_date = new JSONObject();

        @Override
        protected String doInBackground(String... params) {

            try {

                data_ch_date.put("space_id", str_spacId);
                data_ch_date.put("date", tv_date.getText().toString().trim());
                data_ch_date.put("from_time", tv_from.getText().toString().trim());
                data_ch_date.put("to_time", tv_to.getText().toString().trim());
                data_ch_date.put("is_subscribe", str_is_subscriber);

                if (session.isLoggedIn()) {
                    user_details = session.getUserDetails();
                    data_ch_date.put("u_id", user_details.get(SessionManager.user_Id));
                } else {
                    data_ch_date.put("u_id", "0");
                }

                Postdata postdata = new Postdata();
                Log.i("req", data_ch_date.toString());
                String str_dettails = postdata.post(Url_info.main_url + "check_available_date.php", data_ch_date.toString());
                Log.i("req", str_dettails);

                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);
                message1 = job_details.getString(message);

                if (status1.equals("1")) {
                    privious_hour = job_details.getInt("hours");
                } else {
                    privious_hour = 0;
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

                status1 = "0";

                book_date_count = book_date_count + 1;
                final View layout2 = LayoutInflater.from(Booking_MettingRoom_list_details.this).inflate(R.layout.custome_sdate_view, lv_bookdate, false);

                TextView tv_sdate = layout2.findViewById(R.id.tv_sdate);
                ImageView img_date = layout2.findViewById(R.id.img_remove);
                tv_sdate.setText(tv_date.getText().toString() + " " + tv_from.getText().toString() + " To " + tv_to.getText().toString());


                try {

                    /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    Date date1 = simpleDateFormat.parse(tv_from.getText().toString().trim());
                    Date date2 = simpleDateFormat.parse(tv_to.getText().toString().trim());

                    long mills = date1.getTime() - date2.getTime();
                    long hours = mills / (1000 * 60 * 60);

                    hours = (hours < 0 ? -hours : hours);*/

                    book_date.add(tv_date.getText().toString().trim());
                    book_fromtime.add(tv_from.getText().toString().trim());
                    book_totime.add(tv_to.getText().toString().trim());
                    book_hour.add(String.valueOf(final_calculate_hours));
                    book_price.add(String.valueOf(Float.parseFloat(str_price) * final_calculate_hours));
                    ch_book_date.add(tv_date.getText().toString().trim() + "," + tv_from.getText().toString().trim() + "," + tv_to.getText().toString().trim());

                    total_hour = total_hour + final_calculate_hours;
                    tex_amount = ((Float.parseFloat(str_price) * total_hour * Float.parseFloat(str_repet_no)) * vat_per) / 100;

                    Log.i("text_total", tex_amount + "");

                    total_price = ((Float.parseFloat(str_price) * total_hour) * Float.parseFloat(str_repet_no)) + tex_amount;

                    if (str_is_subscriber.equals("0")) {
                        btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));
                    } else {
                        btn_bookroom.setText("Book Now");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                img_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        btn_code_apply.setVisibility(View.VISIBLE);
                        btn_code_remove.setVisibility(View.GONE);
                        tv_dis_amount.setVisibility(View.GONE);
                        et_promo_code.setText("");

                        total_price = ((Float.parseFloat(str_price) * total_hour) * Float.parseFloat(str_repet_no));
                        btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));

                        book_date_count = book_date_count - 1;
                        total_hour = total_hour - Long.parseLong(book_hour.get(book_date_count));

                        book_totime.remove(book_date_count);
                        book_fromtime.remove(book_date_count);
                        book_date.remove(book_date_count);
                        book_hour.remove(book_date_count);
                        book_price.remove(book_date_count);
                        ch_book_date.remove(book_date_count);

                        tex_amount = ((Float.parseFloat(str_price) * total_hour * Float.parseFloat(str_repet_no)) * vat_per) / 100;
                        total_price = (Float.parseFloat(str_price) * total_hour) * Float.parseFloat(str_repet_no) + tex_amount;

                        if (str_is_subscriber.equals("0")) {
                            if (total_price == 0) {
                                btn_bookroom.setText(str_price + " SAR " + getResources().getString(R.string.paynow));
                            } else {
                                btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));
                            }
                        } else {
                            btn_bookroom.setText("Book Now");
                        }

                        lv_bookdate.removeView(layout2);
                    }
                });

                lv_bookdate.addView(layout2);
                Toast.makeText(Booking_MettingRoom_list_details.this, "" + message1, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(Booking_MettingRoom_list_details.this, "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Booking_MettingRoom_list_details.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

    }

    private class Task_Payment extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                //data_bookNow.put("tax_amount", tex_amount);

                Postdata postdata = new Postdata();
                Log.i("request", data_bookNow.toString());
                String str_responce = postdata.post(Url_info.main_url + "insert_booking.php", data_bookNow.toString());
                Log.i("responce", str_responce);
                JSONObject jobj_payment = new JSONObject(str_responce);
                status1 = jobj_payment.getString(status);

                if (status1.equals("1")) {

                    str_booking_id = jobj_payment.getString("final_booking_id");
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

                Intent i_thankyou = new Intent(Booking_MettingRoom_list_details.this, ThankYou.class);
                i_thankyou.putExtra("booking_id", str_booking_id);
                i_thankyou.putExtra("reference_no", str_reference_no);
                i_thankyou.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i_thankyou);

            } else {
                Toast.makeText(Booking_MettingRoom_list_details.this, "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Booking_MettingRoom_list_details.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private class Task_apply extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Log.i("request", data_code.toString());
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

            progressDialog = new ProgressDialog(Booking_MettingRoom_list_details.this);
            progressDialog.setMessage("Please Wait !!!");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {

                if (code_type.equals("percent")) {

                    discount_amount = String.valueOf(((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no) * Float.parseFloat(code_amount)) / 100);

                    tex_amount = ((Integer.parseInt(str_price) * total_hour * Integer.parseInt(str_repet_no) - Float.parseFloat(discount_amount)) * vat_per) / 100;

                    total_price = ((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no)) - Float.parseFloat(discount_amount) + tex_amount;

                    btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));
                    tv_dis_amount.setText("You get " + discount_amount + " SAR " + getResources().getString(R.string.discount_amount));
                    btn_code_apply.setVisibility(View.GONE);
                    btn_code_remove.setVisibility(View.VISIBLE);
                    tv_dis_amount.setVisibility(View.VISIBLE);

                } else {

                    if (Float.parseFloat(code_amount) < (Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no)) {

                        discount_amount = code_amount;
                        total_price = ((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no)) - Float.parseFloat(discount_amount);
                        //tv_finalprice.setText(final_price);
                        btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));

                        tv_dis_amount.setText("You get " + discount_amount + " SAR " + getResources().getString(R.string.discount_amount));

                    } else {

                        code_amount = String.valueOf((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no));

                        discount_amount = code_amount;
                        total_price = ((Integer.parseInt(str_price) * total_hour) * Integer.parseInt(str_repet_no)) - Float.parseFloat(discount_amount);
                        btn_bookroom.setText(total_price + " SAR " + vat_per + " " + getResources().getString(R.string.paynow));
                        //tv_finalprice.setText(final_price);
                        tv_dis_amount.setText("You get " + discount_amount + " SAR " + getResources().getString(R.string.discount_amount));
                    }

                    btn_code_apply.setVisibility(View.GONE);
                    btn_code_remove.setVisibility(View.VISIBLE);
                    tv_dis_amount.setVisibility(View.VISIBLE);
                }

            } else {

                Toast.makeText(getApplicationContext(), message1, Toast.LENGTH_LONG).show();
            }

            progressDialog.dismiss();

        }
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

                Intent i_url = new Intent(Booking_MettingRoom_list_details.this, Visa_payment_url.class);
                i_url.putExtra("visa_url", visa_payment_url);
                startActivityForResult(i_url, 10);

            } else {
                Toast.makeText(Booking_MettingRoom_list_details.this, "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Booking_MettingRoom_list_details.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }


}
