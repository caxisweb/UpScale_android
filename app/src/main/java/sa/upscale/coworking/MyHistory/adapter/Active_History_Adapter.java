package sa.upscale.coworking.MyHistory.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import sa.upscale.coworking.MettingRoom.Booking_MettingRoom_list_details;
import sa.upscale.coworking.MettingRoom.Summary;
import sa.upscale.coworking.MettingRoom.TImePicker.CustomTimePickerDialog;
import sa.upscale.coworking.MyHistory.Active_History_Item;
import sa.upscale.coworking.MyHistory.My_History_Activity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.Url_info;
import sa.upscale.coworking.fregment.Home_freg;

/**
 * Created by codeclinic on 01/05/17.
 */

public class Active_History_Adapter extends RecyclerView.Adapter<Active_History_Adapter.ViewHolder> {


    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_deleteHistory = new JSONObject();
    JSONObject data_updateHistory = new JSONObject();


    Activity mContext;
    int flag = 0;
    ArrayList<Active_History_Item> arraylist;
    int i = 0;
    Dialog dialog;
    String str_bookingId, str_title, str_SpaceId, str_capacity, str_Repeat_b, str_baseAmount;
    private int mYear, mMonth, mDay, m_from_Hour, m_from_Minute, m_to_Hour, m_to_Minute;
    String format;
    TextView tv_date, tv_from, tv_to, tv_endDate;
    String statusActive;
    android.support.v4.app.FragmentTransaction ft;
    String str_chkRepeat = "0", str_bookEveryDayId = "0", mstr_enddDate;
    String strdate, strfrom, strto, d_name, d_location, d_dateTime, d_amount, d_baseamount, d_hour, d_capacity;


    public Active_History_Adapter(Activity activity, ArrayList<Active_History_Item> activeHistoryItems, String statusActive1) {
        this.mContext = activity;
        this.arraylist = activeHistoryItems;
        this.statusActive = statusActive1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cust_myhistory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        str_title = arraylist.get(position).getTitle();

        holder.txt_title.setText(arraylist.get(position).getTitle());
        holder.txt_location.setText(arraylist.get(position).getLocation());
        holder.txt_datetime.setText(arraylist.get(position).getDateTime());
        holder.txt_Amount.setText(arraylist.get(position).getAmount() + " SAR");
        holder.txt_capacity.setText(arraylist.get(position).getCapacity());
        //holder.txt_BaseAmount.setText("Cost " + arraylist.get(position).getBaseAmount() + " Per Hour");
        //holder.txt_hour.setText(arraylist.get(position).getHour() + "h");

        final String temp = arraylist.get(position).getBookingId();


        //arraylist.get(position)

        if (statusActive.equals("1")) {

            holder.ll_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (temp.equals(arraylist.get(position).getBookingId())) {
                        if (flag == 0) {
                            holder.ll_detail.setVisibility(View.VISIBLE);
                            flag = 1;
                        } else {
                            holder.ll_detail.setVisibility(View.GONE);
                            flag = 0;
                        }
                    } else {
                        holder.ll_detail.setVisibility(View.GONE);
                        flag = 0;
                    }
                }
            });
        } else {

        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteHistory_dlg(position);
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               editHistory_dlg(position);
            }
        });

        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.ll_detail.setVisibility(View.VISIBLE);

                String str_msg = "Please explore " + str_title + " and join the coworkers community now by booking a space" + "\n" + "http://hive.sa/app.php";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, str_msg);
                sendIntent.setType("text/plain");
                mContext.startActivity(sendIntent);
            }
        });


        if(statusActive.equals("1")){
            holder.btn_book.setText(mContext.getResources().getString(R.string.cancel));
        }else{
            holder.btn_book.setText(mContext.getResources().getString(R.string.book_now));
        }

        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(statusActive.equals("1")) {
                    deleteHistory_dlg(position);
                }else {
                    Intent bundle = new Intent(mContext, Booking_MettingRoom_list_details.class);
                    bundle.putExtra("type", "list");
                    bundle.putExtra("spaceId", arraylist.get(position).getSpce_Id());
                    bundle.putExtra("capacity_id", arraylist.get(position).getCapacity());
                    bundle.putExtra("date", Home_freg.str_date);
                    bundle.putExtra("from_time", Home_freg.str_fromtime);
                    bundle.putExtra("to_time", Home_freg.str_totime);

                    mContext.startActivity(bundle);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, txt_location, txt_datetime, txt_Amount,txt_capacity;
        LinearLayout ll_main, ll_detail, btn_delete, btn_edit, btn_share;
        Button btn_book;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_title = (TextView) itemView.findViewById(R.id.tv_history_title);
            txt_location = (TextView) itemView.findViewById(R.id.tv_history_location);
            txt_datetime = (TextView) itemView.findViewById(R.id.tv_history_date_time);
            txt_Amount = (TextView) itemView.findViewById(R.id.tv_history_summary_rprice);
            txt_capacity = (TextView) itemView.findViewById(R.id.tv_bookingList_hotel_person_capacity);
            //txt_BaseAmount = (TextView) itemView.findViewById(R.id.tv_history_summary_sprice);
            //txt_hour = (TextView) itemView.findViewById(R.id.tv_history_summary_time);

            btn_book=(Button)itemView.findViewById(R.id.btn_book);
            ll_main = (LinearLayout) itemView.findViewById(R.id.ll_main);
            ll_detail = (LinearLayout) itemView.findViewById(R.id.ll_detail);
            btn_delete = (LinearLayout) itemView.findViewById(R.id.ll_history_delete);
            btn_edit = (LinearLayout) itemView.findViewById(R.id.ll_history_edit);
            btn_share = (LinearLayout) itemView.findViewById(R.id.ll_history_share);
        }
    }


    private void deleteHistory_dlg(final int position) {

        AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
        dialog = adb.setView(new View(mContext)).create();
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 900;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        //final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.cust_history_delete_item);
        dialog.setCancelable(false);

        Button btn_cancle = (Button) dialog.findViewById(R.id.btn_hiostory_delete_cancle);
        Button btn_delete = (Button) dialog.findViewById(R.id.btn_hiostory_delete_delete);

        dialog.show();

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_bookingId = arraylist.get(position).getBookingId();
                dialog.dismiss();
                // deleteConfirmDialog();
                Task_history_delete task_history_delete = new Task_history_delete();
                task_history_delete.execute();
            }
        });


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

        tv_date.setText(arraylist.get(position).getDate());
        tv_from.setText(arraylist.get(position).getFromtime());
        tv_to.setText(arraylist.get(position).getTotime());

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
                    str_bookEveryDayId = "1";
                    ll_repeatView.setVisibility(View.VISIBLE);
                } else {
                    str_chkRepeat = "0";
                    str_bookEveryDayId = "0";
                    ll_repeatView.setVisibility(View.GONE);
                }
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strdate = tv_date.getText().toString().trim();
                strfrom = tv_from.getText().toString().trim();
                strto = tv_to.getText().toString().trim();

                Log.d("Data",strdate+"\n"+strfrom+"\n"+strto);
                String str_repeatBooking = sp_repeat.getSelectedItem().toString();
                String str_endDate = tv_endDate.getText().toString().trim();
                String str_chkValidation = null;

                str_bookingId = arraylist.get(position).getBookingId();
                str_SpaceId = arraylist.get(position).getSpce_Id();
                str_capacity = arraylist.get(position).getCapacity();
                str_Repeat_b = arraylist.get(position).getRepeat_b();
                str_baseAmount = arraylist.get(position).getBaseAmount();


                d_name = arraylist.get(position).getTitle();
                d_location = arraylist.get(position).getLocation();
                d_dateTime = arraylist.get(position).getDateTime();
                d_amount = arraylist.get(position).getAmount();
                d_baseamount = arraylist.get(position).getBaseAmount();
                d_hour = arraylist.get(position).getHour();
                d_capacity = arraylist.get(position).getCapacity();


                if (str_chkRepeat.equals("1")) {
                    if (str_endDate.length() == 0) {
                        Toast.makeText(mContext, "Select Ending date", Toast.LENGTH_SHORT).show();
                        mstr_enddDate = " ";
                        str_chkValidation = "0";
                    } else {
                        mstr_enddDate = str_endDate;
                        str_chkValidation = "1";
                    }

                    if (str_repeatBooking.equals("Every Day")) {
                        str_bookEveryDayId = "1";
                    } else if (str_repeatBooking.equals("Every Week")) {
                        str_bookEveryDayId = "2";
                    } else if (str_repeatBooking.equals("Every Month")) {
                        str_bookEveryDayId = "3";
                    }

                } else {
                    mstr_enddDate = " ";
                    str_chkValidation = "1";
                }

                if (str_chkValidation.equals("1")) {

                    try {
                        dialog.dismiss();

                       /* data_updateHistory.put("booking_id", str_bookingId);
                        data_updateHistory.put("booking_date", strdate);
                        data_updateHistory.put("from_time", strfrom);
                        data_updateHistory.put("to_time", strto);
                        data_updateHistory.put("repeat", str_bookEveryDayId);
                        data_updateHistory.put("end_date",mstr_enddDate);*/

                        data_updateHistory.put("space_id", str_SpaceId);
                        data_updateHistory.put("date", strdate);
                        data_updateHistory.put("from_time", strfrom);
                        data_updateHistory.put("to_time", strto);
                        data_updateHistory.put("cost", str_baseAmount);

                        //Log.d("Data",data_updateHistory.toString());
                        Task_history_edit task_history_edit = new Task_history_edit();
                        task_history_edit.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
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


    private void to_timePicker() {


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(mContext, timeSetListener, hour, minute, false);
        timePickerDialog.show();

        /*Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, Calendar.HOUR_OF_DAY);

        m_to_Hour = now.get(Calendar.HOUR_OF_DAY);
        m_to_Minute = 00;

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


    public class Task_history_edit extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String str_timeDiff, str_price_bookingrespose;

        @Override
        protected String doInBackground(String... params) {


            try {
                Postdata postdata = new Postdata();
                String mstr_delete = postdata.post(Url_info.main_url + "check_booking_available.php", data_updateHistory.toString());
                JSONObject job_delete = new JSONObject(mstr_delete);

                status1 = job_delete.getString(status);
                if (status1.equals("1")) {

                    Log.d("updated", "success");
                    str_timeDiff = job_delete.getString("time_diff");
                    str_price_bookingrespose = job_delete.getString("price");

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

                //Toast.makeText(mContext, "Booking has been successfully changed", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(mContext, Summary.class);

                intent.putExtra("type", "edit");
                intent.putExtra("editBooking_id", str_bookingId);
                intent.putExtra("editBooking_date", strdate);
                intent.putExtra("editFrom_time", strfrom);
                intent.putExtra("editto_time", strto);
                intent.putExtra("editRepeat", str_bookEveryDayId);
                intent.putExtra("editEnd_date", mstr_enddDate);
                intent.putExtra("editTimeDifferent", str_timeDiff);
                intent.putExtra("editPrice", str_price_bookingrespose);


                intent.putExtra("d_name", d_name);
                intent.putExtra("d_capacity",d_capacity);
                intent.putExtra("d_location", d_location);
                intent.putExtra("d_datetime", d_dateTime);
                intent.putExtra("d_hours", d_hour);
                intent.putExtra("d_baseAmount", d_baseamount);
                intent.putExtra("d_amount", d_amount);

                mContext.startActivity(intent);

            } else {
                Toast.makeText(mContext, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

    }


    public class Task_history_delete extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {


            try {
                data_deleteHistory.put("booking_id", str_bookingId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Postdata postdata = new Postdata();
                String mstr_delete = postdata.post(Url_info.main_url + "remove_booking.php", data_deleteHistory.toString());
                JSONObject job_delete = new JSONObject(mstr_delete);

                status1 = job_delete.getString(status);
                if (status1.equals("1")) {
                    Log.d("remove", "deleted");
                    dialog.dismiss();

                    //  ((Activity)mContext).recreate();
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
                dialog.dismiss();

                My_History_Activity history = new My_History_Activity();
                ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, history).commit();

            } else {
                Toast.makeText(mContext, "" + message1, Toast.LENGTH_SHORT).show();
            }

        }


    }
}
