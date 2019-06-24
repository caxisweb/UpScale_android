package sa.upscale.coworking.MettingRoom.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sa.upscale.coworking.MettingRoom.Booking_MettingRoom_list_details;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;
import sa.upscale.coworking.fregment.Home_freg;
import sa.upscale.coworking.model.Fav_status;

/**
 * Created by codeclinic on 19/04/17.
 */


public class adapter_meeting_room_list extends ArrayAdapter<String> {

    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_favorite = new JSONObject();
    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();

    private ArrayList<String> ar_space_id1 = new ArrayList<>();
    private ArrayList<String> ar_name1 = new ArrayList<>();
    private ArrayList<String> ar_projector1 = new ArrayList<>();
    private ArrayList<String> ar_scanner1 = new ArrayList<>();
    private ArrayList<String> ar_parking1 = new ArrayList<>();
    private ArrayList<String> ar_ac1 = new ArrayList<>();
    private ArrayList<String> ar_locker1 = new ArrayList<>();
    private ArrayList<String> ar_ph1 = new ArrayList<>();
    private ArrayList<String> ar_mail1 = new ArrayList<>();
    private ArrayList<String> ar_wifi1 = new ArrayList<>();
    private ArrayList<String> ar_work1 = new ArrayList<>();
    private ArrayList<String> ar_male1 = new ArrayList<>();
    private ArrayList<String> ar_female1 = new ArrayList<>();
    private ArrayList<String> ar_coffee1 = new ArrayList<>();

    private ArrayList<String> ar_location1 = new ArrayList<>();
    private ArrayList<String> ar_price1 = new ArrayList<>();
    private ArrayList<String> ar_capacity1 = new ArrayList<>();
    private ArrayList<String> ar_img1 = new ArrayList<>();
    private ArrayList<String> ar_rating1 = new ArrayList<>();
    private ArrayList<String> ar_rating_count1 = new ArrayList<>();
    private ArrayList<String> ar_distance1 = new ArrayList<>();
    private ArrayList<String> ar_wish_status1 = new ArrayList<>();
    private ArrayList<Fav_status> wish_status = new ArrayList<>();
    Fav_status test;

    Activity mContext;
    String templike = "0", mstr_userId, mstr_spaceId, temp = "0";
    String wishStatus;
    String wish_status_1 = "0";
    LayoutInflater inflater = null;

    public adapter_meeting_room_list(Activity context, ArrayList<String> ar_space_id1, ArrayList<String> ar_name1, ArrayList<String> ar_projector1, ArrayList<String> ar_scanner1, ArrayList<String> ar_parking1, ArrayList<String> ar_ac1, ArrayList<String> ar_locker1, ArrayList<String> ar_ph1, ArrayList<String> ar_mail1, ArrayList<String> ar_wifi1, ArrayList<String> ar_work1, ArrayList<String> ar_male1, ArrayList<String> ar_female1, ArrayList<String> ar_coffee1, ArrayList<String> ar_location1, ArrayList<String> ar_price1, ArrayList<String> ar_capacity1, ArrayList<String> ar_img1, ArrayList<String> ar_rating1, ArrayList<String> ar_rating_count1, ArrayList<String> ar_distance1, ArrayList<String> ar_wish_status1, ArrayList<Fav_status> wish_status) {
        super(context, R.layout.cust_booking_list);

        this.ar_space_id1 = ar_space_id1;
        this.ar_name1 = ar_name1;
        this.ar_projector1 = ar_projector1;
        this.ar_scanner1 = ar_scanner1;
        this.ar_parking1 = ar_parking1;
        this.ar_ac1 = ar_ac1;
        this.ar_locker1 = ar_locker1;
        this.ar_ph1 = ar_ph1;
        this.ar_mail1 = ar_mail1;
        this.ar_wifi1 = ar_wifi1;
        this.ar_work1 = ar_work1;
        this.ar_male1 = ar_male1;
        this.ar_female1 = ar_female1;
        this.ar_coffee1 = ar_coffee1;

        this.ar_location1 = ar_location1;
        this.ar_price1 = ar_price1;
        this.ar_capacity1 = ar_capacity1;
        this.ar_img1 = ar_img1;
        this.ar_rating1 = ar_rating1;
        this.ar_rating_count1 = ar_rating_count1;
        this.ar_distance1 = ar_distance1;
        this.ar_wish_status1 = ar_wish_status1;
        this.mContext = context;
        this.wish_status = wish_status;

        sessionManager = new SessionManager(mContext);
        user_details = sessionManager.getUserDetails();
        mstr_userId = user_details.get(SessionManager.user_Id);


        //layoutInflater=s
    }


    @Override
    public int getCount() {
        return ar_space_id1.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //View view=layoutInflater.inflate(R.layout.cust_booking_list,null);

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.cust_booking_list, null);
        }

        ImageView img_hotel_pic, img_wifi, img_call, img_mail, img_cofee;
        TextView tv_hotelTitle, tv_hotelLocation, tv_capacity, tv_hotelPrice, tv_ratingReview, tv_hotelNearLocation;
        RatingBar rt_rating;
        Button btn_book;

        LinearLayout lv_bookinglist_details;

        tv_ratingReview = (TextView) v.findViewById(R.id.tv__bookingList_rating);
        rt_rating = (RatingBar) v.findViewById(R.id.rating_bookingList_rating);
        img_hotel_pic = (ImageView) v.findViewById(R.id.img_bookingList_hotelPic);
        img_wifi = (ImageView) v.findViewById(R.id.img_bookingList_hotel_wifi);
        img_call = (ImageView) v.findViewById(R.id.img_bookingList_hotel_call);
        img_mail = (ImageView) v.findViewById(R.id.img_bookingList_hotel_message);
        img_cofee = (ImageView) v.findViewById(R.id.img_bookingList_hotel_cofee);

        tv_hotelTitle = (TextView) v.findViewById(R.id.tv_bookingList_hotel_Title);
        tv_hotelLocation = (TextView) v.findViewById(R.id.tv_bookingList_hotel_location);
        tv_capacity = (TextView) v.findViewById(R.id.tv_bookingList_hotel_person_capacity);
        tv_hotelPrice = (TextView) v.findViewById(R.id.tv_bookingList_hotel_price);
        tv_hotelNearLocation = (TextView) v.findViewById(R.id.tv_bookingList_hotel_location_1);

        btn_book = (Button) v.findViewById(R.id.btn_book);
        lv_bookinglist_details = (LinearLayout) v.findViewById(R.id.rl_bookinglist_details);

        LinearLayout ll_favorite = (LinearLayout) v.findViewById(R.id.ll_img_favroite);
        LinearLayout ll_share = (LinearLayout) v.findViewById(R.id.ll_img_share);

        RelativeLayout lv_addvertice = (RelativeLayout) v.findViewById(R.id.lv_addvertice);
        ViewFlipper fliper_add = (ViewFlipper) v.findViewById(R.id.flipper_add);
        AdView google_AdView = (AdView) v.findViewById(R.id.adView);

        if(position==0) {


            lv_addvertice.setVisibility(View.VISIBLE);

            if(Home_freg.add_url.size()>0){

                fliper_add.setVisibility(View.VISIBLE);
                google_AdView.setVisibility(View.GONE);

                fliper_add.removeAllViews();

                for (int i = 0; i < Home_freg.add_url.size(); i++) {

                    View add_view = LayoutInflater.from(mContext).inflate(
                            R.layout.custome_addview, null);

                    ImageView img_adv= (ImageView) add_view.findViewById(R.id.img_adv);


                    Glide.with(mContext)
                            .load(Url_info.main_img + "advertise/" + Home_freg.add_img.get(i))
                            .error(R.drawable.addbaner)
                            .into(img_adv);

                    final int k=i;

                    img_adv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(Home_freg.add_url.get(k).equals("")){

                            }else {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Home_freg.add_url.get(k)));
                                mContext.startActivity(browserIntent);
                            }
                        }
                    });

                    fliper_add.addView(add_view);
                }

                fliper_add.startFlipping();

            }else{

                fliper_add.setVisibility(View.GONE);
                google_AdView.setVisibility(View.VISIBLE);

                MobileAds.initialize(mContext,mContext.getString(R.string.google_admobo_app_id));
                AdRequest adRequest = new AdRequest.Builder().build();
                google_AdView.loadAd(adRequest);

            }


        }else{

            lv_addvertice.setVisibility(View.GONE);
        }

        final ImageView img_likeDislike = (ImageView) v.findViewById(R.id.img_bookingList_like_dislike);


        if (ar_distance1.get(position).equals("0")) {
            tv_hotelNearLocation.setVisibility(View.GONE);
        } else {
            tv_hotelNearLocation.setVisibility(View.VISIBLE);
            tv_hotelNearLocation.setText(ar_distance1.get(position));
        }


        //String str = ar_capacity1.get(position);
        //Log.d("Data", "tst :\t " + str);

        try {

            tv_capacity.setText(ar_capacity1.get(position));
            tv_hotelTitle.setText(ar_name1.get(position));
            tv_hotelLocation.setText(ar_location1.get(position));
            tv_hotelPrice.setText(ar_price1.get(position));

            Log.i("image_url",Url_info.main_img + "space/" + ar_img1.get(position));
            Glide.with(mContext).load(Url_info.main_img + "space/" + ar_img1.get(position)).into(img_hotel_pic);

            String wifi = ar_wifi1.get(position);
            String call = ar_ph1.get(position);
            String mail = ar_mail1.get(position);
            String cofee = ar_work1.get(position);
            mstr_spaceId = ar_space_id1.get(position);

            rt_rating.setRating(Float.parseFloat(ar_rating1.get(position)));
            tv_ratingReview.setText("(" + ar_rating_count1.get(position) + " reviews)");
            //tv_hotelNearLocation.setVisibility(View.GONE);
            if (wifi.equals("1")) {
                img_wifi.setVisibility(View.VISIBLE);
            } else {
                img_wifi.setVisibility(View.GONE);
            }

            if (call.equals("1")) {
                img_call.setVisibility(View.VISIBLE);
            } else {
                img_call.setVisibility(View.GONE);
            }

            if (mail.equals("1")) {
                img_mail.setVisibility(View.VISIBLE);
            } else {
                img_mail.setVisibility(View.GONE);
            }

            if (cofee.equals("1")) {
                img_cofee.setVisibility(View.VISIBLE);
            } else {
                img_cofee.setVisibility(View.GONE);
            }

            wishStatus = wish_status.get(position).getWish_status();

            if (wishStatus.equals("1")) {
                img_likeDislike.setImageResource(R.mipmap.like);
            } else {
                img_likeDislike.setImageResource(R.drawable.heart);
            }

        }catch (Exception e){

        }



        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bundle = new Intent(mContext, Booking_MettingRoom_list_details.class);
                bundle.putExtra("type", "list");
                bundle.putExtra("spaceId", ar_space_id1.get(position));
                bundle.putExtra("capacity_id", ar_capacity1.get(position));
                bundle.putExtra("date", Home_freg.str_date);
                bundle.putExtra("from_time", Home_freg.str_fromtime);
                bundle.putExtra("to_time", Home_freg.str_totime);

                mContext.startActivity(bundle);

            }
        });


        lv_bookinglist_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bundle = new Intent(mContext, Booking_MettingRoom_list_details.class);
                bundle.putExtra("type", "list");
                bundle.putExtra("spaceId", ar_space_id1.get(position));
                bundle.putExtra("capacity_id", ar_capacity1.get(position));
                bundle.putExtra("date", Home_freg.str_date);
                bundle.putExtra("from_time", Home_freg.str_fromtime);
                bundle.putExtra("to_time", Home_freg.str_totime);

                mContext.startActivity(bundle);

            }
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i_share = new Intent(Intent.ACTION_SEND);
                i_share.putExtra(Intent.EXTRA_TEXT, "Book a Space " + ar_name1.get(position) + " with Capacity " + ar_capacity1.get(position) + " from Hive, Download Hive from https://play.google.com/store/apps/details?id=sa.upscale.coworking");
                i_share.setType("text/plain");
                Intent i_op = Intent.createChooser(i_share, "Share Space");
                mContext.startActivityForResult(i_op, 1);
            }
        });

        ll_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionManager.isLoggedIn()) {

                    new AsyncTask<String, String, String>() {

                        ProgressDialog progressDialog;

                        @Override
                        protected String doInBackground(String... params) {


                            try {

                                data_favorite.put("user_id", mstr_userId);
                                data_favorite.put("space_id", mstr_spaceId);

                                Postdata postdata = new Postdata();

                                String str_dettails = postdata.post(Url_info.main_url + "insert_favorite.php", data_favorite.toString());
                                Log.d("responce", str_dettails);

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

                                Log.i("wish status", wish_status_1);
                                // test.setWish_status((String) img_likeDislike.getTag());

                                if (wish_status_1.equals("1")) {

                                    img_likeDislike.setImageResource(R.mipmap.like);
                                    wish_status.get(position).setWish_status("1");

                                } else {

                                    img_likeDislike.setImageResource(R.drawable.heart);
                                    wish_status.get(position).setWish_status("0");
                                }


                                notifyDataSetChanged();
                                //mContext.recreate();

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
                        }
                    }.execute();
                } else {

                    Toast.makeText(mContext, mContext.getResources().getString(R.string.login_message), Toast.LENGTH_LONG).show();
                }
               /* Task_InsertFavorite task_insertFavorite=new Task_InsertFavorite();
                task_insertFavorite.execute();*/

            }
        });


        return v;
    }


}
