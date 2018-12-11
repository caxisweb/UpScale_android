package sa.upscale.coworking.MyProfiles.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import sa.upscale.coworking.MettingRoom.Booking_MettingRoom_list_details;
import sa.upscale.coworking.MettingRoom.TImePicker.CustomTimePickerDialog;
import sa.upscale.coworking.MyProfiles.WishList;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;
import sa.upscale.coworking.fregment.Home_freg;

/**
 * Created by codeclinic on 19/04/17.
 */


public class adapter_wish_list extends ArrayAdapter<String> {


    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_favorite = new JSONObject();
    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();

    ArrayList<String> ar_space_id1 = new ArrayList<>();
    ArrayList<String> ar_name1 = new ArrayList<>();
    ArrayList<String> ar_email1 = new ArrayList<>();
    ArrayList<String> ar_phone1 = new ArrayList<>();
    ArrayList<String> ar_location1 = new ArrayList<>();
    ArrayList<String> ar_logo1 = new ArrayList<>();
    ArrayList<String> ar_price1 = new ArrayList<>();
    ArrayList<String> ar_rating1 = new ArrayList<>();
    ArrayList<String> ar_rating_count1 = new ArrayList<>();
    ArrayList<String> ar_projector1 = new ArrayList<>();
    ArrayList<String> ar_scanner1 = new ArrayList<>();
    ArrayList<String> ar_parking1 = new ArrayList<>();
    ArrayList<String> ar_ac1 = new ArrayList<>();
    ArrayList<String> ar_locker1 = new ArrayList<>();
    ArrayList<String> ar_ph1 = new ArrayList<>();
    ArrayList<String> ar_mail1 = new ArrayList<>();
    ArrayList<String> ar_wifi1 = new ArrayList<>();
    ArrayList<String> ar_work1 = new ArrayList<>();
    ArrayList<String> ar_capacity1 = new ArrayList<>();
    Activity mContext;

    String mstr_userId;
    String mstr_capacityId;

    TextView tv_date, tv_from, tv_to, tv_endDate;
    private int mYear, mMonth, mDay;
    String str_chkRepeat = "0", mstr_enddDate;
    String format;
    android.support.v4.app.FragmentTransaction ft;

    Dialog dialog;

    public adapter_wish_list(Activity context,
                             ArrayList<String> ar_space_id1, ArrayList<String> ar_name1, ArrayList<String> ar_email1,
                             ArrayList<String> ar_phone1, ArrayList<String> ar_projector1, ArrayList<String> ar_scanner1,
                             ArrayList<String> ar_parking1, ArrayList<String> ar_ac1, ArrayList<String> ar_locker1,
                             ArrayList<String> ar_ph1, ArrayList<String> ar_mail1, ArrayList<String> ar_wifi1,
                             ArrayList<String> ar_work1, ArrayList<String> ar_location1, ArrayList<String> ar_logo1,
                             ArrayList<String> ar_price1, ArrayList<String> ar_rating1, ArrayList<String> ar_rating_count1, ArrayList<String> ar_capacity1) {
        super(context, R.layout.cust_wishlist);

        this.ar_space_id1 = ar_space_id1;
        this.ar_name1 = ar_name1;
        this.ar_email1 = ar_email1;
        this.ar_phone1 = ar_phone1;
        this.ar_projector1 = ar_projector1;
        this.ar_scanner1 = ar_scanner1;
        this.ar_parking1 = ar_parking1;
        this.ar_ac1 = ar_ac1;
        this.ar_locker1 = ar_locker1;
        this.ar_ph1 = ar_ph1;
        this.ar_mail1 = ar_mail1;
        this.ar_wifi1 = ar_wifi1;
        this.ar_work1 = ar_work1;
        this.ar_location1 = ar_location1;
        this.ar_logo1 = ar_logo1;
        this.ar_price1 = ar_price1;
        this.ar_rating1 = ar_rating1;
        this.ar_rating_count1 = ar_rating_count1;
        this.mContext = context;
        this.ar_capacity1 = ar_capacity1;

        sessionManager = new SessionManager(mContext);
        user_details = sessionManager.getUserDetails();
        mstr_userId = user_details.get(SessionManager.user_Id);


    }


    @Override
    public int getCount() {
        return ar_space_id1.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cust_wishlist, parent, false);

        LinearLayout ll_wishlist, ll_wishlistDetails;
        Button rl_wishlist_booking;
        TextView tv_hotelTitle, tv_hotelLocation, tv_capacity, tv_hotelPrice, tv_ratingReview, tv_hotelNearLocation;
        RatingBar rt_rating;

        tv_ratingReview = (TextView) v.findViewById(R.id.tv__bookingList_rating);
        rt_rating = (RatingBar) v.findViewById(R.id.rating_bookingList_rating);

        tv_hotelTitle = (TextView) v.findViewById(R.id.tv_bookingList_hotel_Title);
        tv_hotelLocation = (TextView) v.findViewById(R.id.tv_bookingList_hotel_location);
        tv_capacity = (TextView) v.findViewById(R.id.tv_bookingList_hotel_person_capacity);
        tv_hotelPrice = (TextView) v.findViewById(R.id.tv_bookingList_hotel_price);
        tv_hotelNearLocation = (TextView) v.findViewById(R.id.tv_bookingList_hotel_location_1);
        ll_wishlist = (LinearLayout) v.findViewById(R.id.ll_img_favroite);
        ll_wishlistDetails = (LinearLayout) v.findViewById(R.id.ll_cust_wishlist);
        rl_wishlist_booking = (Button) v.findViewById(R.id.btn_book);


        String img = Url_info.main_img + "logo/" + ar_logo1.get(position);


        tv_hotelTitle.setText(ar_name1.get(position));
        tv_hotelLocation.setText(ar_location1.get(position));
        tv_hotelPrice.setText(ar_price1.get(position));
        tv_capacity.setText(ar_capacity1.get(position));

        String wifi = ar_wifi1.get(position);
        String call = ar_ph1.get(position);
        String mail = ar_mail1.get(position);
        String cofee = ar_work1.get(position);


        if (ar_rating1.get(position) == null) {

        } else {
           /* rt_rating.setRating(Float.parseFloat(ar_rating1.get(position)));
            tv_ratingReview.setText("(" + ar_rating_count1.get(position) + " reviews)");*/
        }

        ll_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WishList.mstr_spaceId = ar_space_id1.get(position);

                Task_InsertFavorite removeFavorite = new Task_InsertFavorite();
                removeFavorite.execute();

            }
        });

        rl_wishlist_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bundle = new Intent(mContext, Booking_MettingRoom_list_details.class);
                bundle.putExtra("type", "list");
                bundle.putExtra("spaceId", ar_space_id1.get(position));
                bundle.putExtra("capacity_id", ar_capacity1.get(position));
                bundle.putExtra("date", Home_freg.str_date);
                bundle.putExtra("from_time", Home_freg.str_fromtime);
                bundle.putExtra("to_time", Home_freg.str_totime);
                mContext.startActivity(bundle);

                //editHistory_dlg(position);
            }
        });
        return v;
    }

    private void editHistory_dlg(final int position) {


        String ar_repeatBooking[] = {"Every Day", "Every Week", "Every Month"};

        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
        dialog = adb.setView(new View(mContext)).create();
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.cust_history_edit_item);
        dialog.setCancelable(false);


        RelativeLayout rl_date = (RelativeLayout) dialog.findViewById(R.id.rl_history_date);
        RelativeLayout rl_from = (RelativeLayout) dialog.findViewById(R.id.rl_history_from);
        RelativeLayout rl_to = (RelativeLayout) dialog.findViewById(R.id.rl_history_to);
        RelativeLayout rl_endDate = (RelativeLayout) dialog.findViewById(R.id.rl_history_endDate);

        final LinearLayout ll_repeatView = (LinearLayout) dialog.findViewById(R.id.rl_history_repeatView);
        final CheckBox chk_repeat = (CheckBox) dialog.findViewById(R.id.chk_history_repeat);

        final Spinner sp_repeat = (Spinner) dialog.findViewById(R.id.tv_history_repeatBook);

        tv_date = (TextView) dialog.findViewById(R.id.tv_history_date);
        tv_from = (TextView) dialog.findViewById(R.id.tv_history_from);
        tv_to = (TextView) dialog.findViewById(R.id.tv_history_to);
        tv_endDate = (TextView) dialog.findViewById(R.id.tv_history_endDate);


        ImageView btn_close = (ImageView) dialog.findViewById(R.id.img_close);
        Button btn_cancle = (Button) dialog.findViewById(R.id.btn_hiostory_edit_cancle);
        Button btn_update = (Button) dialog.findViewById(R.id.btn_hiostory_edit_update);
        dialog.show();


        btn_update.setText("Booking");
        btn_update.setBackgroundColor(mContext.getResources().getColor(R.color.forgotButton));

        Calendar d = Calendar.getInstance();
        CharSequence s = DateFormat.format("MMMM dd, yyyy", d);
        Log.d("Date", String.valueOf(s));
        tv_date.setText(s);

        ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, ar_repeatBooking);
        sp_repeat.setAdapter(arrayAdapter);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        chk_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chk_repeat.isChecked()) {
                    str_chkRepeat = "1";
                    WishList.str_bookEveryDayId = "1";
                    ll_repeatView.setVisibility(View.VISIBLE);
                } else {
                    str_chkRepeat = "0";
                    WishList.str_bookEveryDayId = "0";
                    ll_repeatView.setVisibility(View.GONE);
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WishList.mstr_spaceId = ar_space_id1.get(position);
                mstr_capacityId = ar_capacity1.get(position);


                WishList.str_date_ = tv_date.getText().toString().trim();
                WishList.str_fromtime_ = tv_from.getText().toString().trim();
                WishList.str_totime_ = tv_to.getText().toString().trim();

                String str_repeatBooking = sp_repeat.getSelectedItem().toString();
                WishList.str_endDate = tv_endDate.getText().toString().trim();
                String str_chkValidation = null;


                if (str_chkRepeat.equals("1")) {
                    if (WishList.str_endDate.length() == 0) {
                        Toast.makeText(mContext, "Select Ending date", Toast.LENGTH_SHORT).show();
                        mstr_enddDate = " ";
                        str_chkValidation = "0";
                    } else {
                        mstr_enddDate = WishList.str_endDate;
                        str_chkValidation = "1";
                    }

                    if (str_repeatBooking.equals("Every Day")) {
                        WishList.str_bookEveryDayId = "1";
                    } else if (str_repeatBooking.equals("Every Week")) {
                        WishList.str_bookEveryDayId = "2";
                    } else if (str_repeatBooking.equals("Every Month")) {
                        WishList.str_bookEveryDayId = "3";
                    }

                } else {
                    mstr_enddDate = " ";
                    str_chkValidation = "1";
                }


                if (str_chkValidation.equals("1")) {

                    if (WishList.str_fromtime_.length() == 0) {
                        tv_from.setError("Please Select Time");
                        tv_from.setFocusable(true);

                    } else if (WishList.str_totime_.length() == 0) {
                        tv_to.setError("Please Select Time");
                        tv_to.setFocusable(true);
                    } else {
                        Intent intent = new Intent(mContext, Booking_MettingRoom_list_details.class);
                        intent.putExtra("type", "wishlist");
                        intent.putExtra("spaceId", WishList.mstr_spaceId);
                        intent.putExtra("capacity_id", mstr_capacityId);
                        intent.putExtra("date", WishList.str_date_);
                        intent.putExtra("from_time", WishList.str_fromtime_);
                        intent.putExtra("to_time", WishList.str_totime_);
                        mContext.startActivity(intent);

                        dialog.dismiss();
                    }


                } else {
                    Toast.makeText(mContext, "Select Ending date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();

            }
        });

        rl_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                from_timePicker();
            }
        });

        rl_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                to_timePicker();
            }
        });

        rl_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                endDate_Picker();

            }
        });


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
            tv_to.setText(hourOfDay + ":" + str_minute + " " + format);
        }
    };


    private void endDate_Picker() {

        // Get Current Date
        final Calendar c;

        c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    String month;

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int m = monthOfYear + 1;
                        if (m == 1) {
                            month = "January";
                        } else if (m == 2) {
                            month = "February";
                        } else if (m == 3) {
                            month = "March";
                        } else if (m == 4) {
                            month = "April";
                        } else if (m == 5) {
                            month = "May";
                        } else if (m == 6) {
                            month = "June";
                        } else if (m == 7) {
                            month = "July";
                        } else if (m == 8) {
                            month = "August";
                        } else if (m == 9) {
                            month = "September";
                        } else if (m == 10) {
                            month = "October";
                        } else if (m == 11) {
                            month = "November";
                        } else if (m == 12) {
                            month = "December";
                        }


                        // tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        tv_endDate.setText(month + " " + dayOfMonth + "," + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

    }

    private void to_timePicker() {


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(mContext, timeSetListener, hour, minute, false);
        timePickerDialog.show();


    }

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


    private void from_timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(mContext, timeSetListener1, hour, minute, false);
        timePickerDialog.show();


        /*final Calendar c;
        c = Calendar.getInstance();

        m_from_Hour = c.get(Calendar.HOUR_OF_DAY);
        m_from_Minute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        tv_from.setText(hourOfDay + ":" + minute);
                    }
                }, m_from_Hour, m_from_Minute, false);
        timePickerDialog.show();*/

    }


    private void datePicker() {

        // Get Current Date
        final Calendar c;

        c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    String month;

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int m = monthOfYear + 1;
                        if (m == 1) {
                            month = "January";
                        } else if (m == 2) {
                            month = "February";
                        } else if (m == 3) {
                            month = "March";
                        } else if (m == 4) {
                            month = "April";
                        } else if (m == 5) {
                            month = "May";
                        } else if (m == 6) {
                            month = "June";
                        } else if (m == 7) {
                            month = "July";
                        } else if (m == 8) {
                            month = "August";
                        } else if (m == 9) {
                            month = "September";
                        } else if (m == 10) {
                            month = "October";
                        } else if (m == 11) {
                            month = "November";
                        } else if (m == 12) {
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


    private class Task_InsertFavorite extends AsyncTask<String, String, String> {


        // ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {

                data_favorite.put("user_id", mstr_userId);
                data_favorite.put("space_id", WishList.mstr_spaceId);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                Postdata postdata = new Postdata();
                Log.d("like", "data" + data_favorite.toString());
                String str_dettails = postdata.post(Url_info.main_url + "insert_favorite.php", data_favorite.toString());
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {
                    Log.d("like", "favorite Successfully removed");

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

                // ((WishList)mContext).recreate();
                WishList wishList = new WishList();
                ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, wishList).commit();

            } else {
                Toast.makeText(mContext, "" + message1, Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();
            //progressDialog.dismiss();
        }

/*

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
*/

    }

}
