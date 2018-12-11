package sa.upscale.coworking.MettingRoom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import sa.upscale.coworking.R;

/**
 * Created by codeclinic on 19/04/17.
 */


public class adapter_profile_Reviews_more extends ArrayAdapter<String> {


    ArrayList<String> ar_reviewId = new ArrayList<>();
    ArrayList<String> ar_user_id = new ArrayList<>();
    ArrayList<String> ar_user_name = new ArrayList<>();
    ArrayList<String> ar_rate = new ArrayList<>();
    ArrayList<String> ar_review = new ArrayList<>();
    ArrayList<String> ar_datetime = new ArrayList<>();

    Context mContext;

    public adapter_profile_Reviews_more(Context context, ArrayList<String> ar_reviewId, ArrayList<String> ar_user_id, ArrayList<String> ar_user_name, ArrayList<String> ar_rate, ArrayList<String> ar_review, ArrayList<String> ar_datetime) {
        super(context, R.layout.cust_more_reviews);

        this.mContext = context;
        this.ar_reviewId = ar_reviewId;
        this.ar_user_id = ar_user_id;
        this.ar_user_name=ar_user_name;
        this.ar_rate=ar_rate;
        this.ar_review=ar_review;
        this.ar_datetime=ar_datetime;
    }


    @Override
    public int getCount() {
        return ar_reviewId.size();
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
            v = inflater.inflate(R.layout.cust_more_reviews, null);
        }


        CircularImageView img_userProfile;
        TextView tv_userName,tv_date,tv_reviews;
        RatingBar rt_rating;


        img_userProfile= (CircularImageView) v.findViewById(R.id.img_custreview_user_pic);
        tv_userName= (TextView) v.findViewById(R.id.tv_custreview_user_Name);
        tv_date= (TextView) v.findViewById(R.id.tv_custreview_time);
        tv_reviews= (TextView) v.findViewById(R.id.tv_custreview_reviewDetails);
        rt_rating= (RatingBar) v.findViewById(R.id.rating_custreview_rate);

        try {

            rt_rating.setRating(Float.parseFloat(ar_rate.get(position)));
            tv_userName.setText(ar_user_name.get(position));
            tv_date.setText(ar_datetime.get(position));
            tv_reviews.setText(ar_review.get(position));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
}
