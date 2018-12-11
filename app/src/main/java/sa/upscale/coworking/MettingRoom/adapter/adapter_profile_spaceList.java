package sa.upscale.coworking.MettingRoom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sa.upscale.coworking.R;
import sa.upscale.coworking.Url_info;

/**
 * Created by codeclinic on 19/04/17.
 */


public class adapter_profile_spaceList extends ArrayAdapter<String> {


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
    private ArrayList<String> ar_location1 = new ArrayList<>();
    private ArrayList<String> ar_price1 = new ArrayList<>();
    private ArrayList<String> ar_capacity1 = new ArrayList<>();
    private ArrayList<String> ar_img1 = new ArrayList<>();
    private ArrayList<String> ar_rating1 = new ArrayList<>();
    private ArrayList<String> ar_rating_count1 = new ArrayList<>();
    private ArrayList<String> ar_distance1 = new ArrayList<>();

    Context mContext;


    public adapter_profile_spaceList(Context context, ArrayList<String> ar_space_id1, ArrayList<String> ar_name1, ArrayList<String> ar_projector1, ArrayList<String> ar_scanner1, ArrayList<String> ar_parking1, ArrayList<String> ar_ac1, ArrayList<String> ar_locker1, ArrayList<String> ar_ph1, ArrayList<String> ar_mail1, ArrayList<String> ar_wifi1, ArrayList<String> ar_work1, ArrayList<String> ar_location1, ArrayList<String> ar_price1, ArrayList<String> ar_capacity1, ArrayList<String> ar_img1, ArrayList<String> ar_rating1, ArrayList<String> ar_rating_count1,ArrayList<String> ar_distance1) {
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
        this.ar_location1 = ar_location1;
        this.ar_price1 = ar_price1;
        this.ar_capacity1 = ar_capacity1;
        this.ar_img1 = ar_img1;
        this.ar_rating1=ar_rating1;
        this.ar_rating_count1=ar_rating_count1;
        this.mContext = context;
        this.ar_distance1=ar_distance1;

        //layoutInflater=s
    }


    @Override
    public int getCount() {
        return ar_space_id1.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //View view=layoutInflater.inflate(R.layout.cust_booking_list,null);

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.cust_booking_list, null);
        }


        ImageView img_hotel_pic,img_wifi,img_call,img_mail,img_cofee;
        TextView tv_hotelTitle,tv_hotelLocation,tv_capacity,tv_hotelPrice,tv_ratingReview,tv_hotelNearLocation;
        RatingBar rt_rating;

        tv_ratingReview= (TextView) v.findViewById(R.id.tv__bookingList_rating);
        rt_rating= (RatingBar) v.findViewById(R.id.rating_bookingList_rating);
        img_hotel_pic= (ImageView) v.findViewById(R.id.img_bookingList_hotelPic);
        img_wifi= (ImageView) v.findViewById(R.id.img_bookingList_hotel_wifi);
        img_call= (ImageView) v.findViewById(R.id.img_bookingList_hotel_call);
        img_mail= (ImageView) v.findViewById(R.id.img_bookingList_hotel_message);
        img_cofee= (ImageView) v.findViewById(R.id.img_bookingList_hotel_cofee);

        tv_hotelTitle= (TextView) v.findViewById(R.id.tv_bookingList_hotel_Title);
        tv_hotelLocation= (TextView) v.findViewById(R.id.tv_bookingList_hotel_location);
        tv_capacity= (TextView) v.findViewById(R.id.tv_bookingList_hotel_person_capacity);
        tv_hotelPrice= (TextView) v.findViewById(R.id.tv_bookingList_hotel_price);
        tv_hotelNearLocation= (TextView) v.findViewById(R.id.tv_bookingList_hotel_location_1);

        String img=Url_info.main_img+"space/"+ar_img1.get(position);

        tv_capacity.setText(ar_capacity1.get(position));
        tv_hotelTitle.setText(ar_name1.get(position));
        tv_hotelLocation.setText(ar_location1.get(position));
        tv_hotelPrice.setText(ar_price1.get(position));
        tv_hotelNearLocation.setText(ar_distance1.get(position));
        Picasso.with(mContext).load(img).into(img_hotel_pic);

        String wifi=ar_wifi1.get(position);
        String call=ar_ph1.get(position);
        String mail=ar_mail1.get(position);
        String cofee=ar_work1.get(position);
        rt_rating.setRating(Float.parseFloat(ar_rating1.get(position)));
        tv_ratingReview.setText("("+ar_rating_count1.get(position)+" reviews)");


        if (wifi.equals("1"))
        {
            img_wifi.setVisibility(View.VISIBLE);
        }else {
            img_wifi.setVisibility(View.GONE);
        }

        if (call.equals("1"))
        {
            img_call.setVisibility(View.VISIBLE);
        }else {
            img_call.setVisibility(View.GONE);
        }

        if (mail.equals("1"))
        {
            img_mail.setVisibility(View.VISIBLE);
        }else {
            img_mail.setVisibility(View.GONE);
        }

        if (cofee.equals("1"))
        {
            img_cofee.setVisibility(View.VISIBLE);
        }else {
            img_cofee.setVisibility(View.GONE);
        }

        return v;
    }
}
