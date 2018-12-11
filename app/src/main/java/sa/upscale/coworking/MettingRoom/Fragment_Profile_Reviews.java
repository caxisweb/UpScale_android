package sa.upscale.coworking.MettingRoom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class Fragment_Profile_Reviews extends Fragment {


    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";
    JSONObject data_pReview = new JSONObject();
    JSONObject data_gReview=new JSONObject();
    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();
    View view;
    EditText ed_reviewDetails;
    Button btn_submit;
    String str_reviews, str_rating;

    ArrayList<String> ar_reviewId = new ArrayList<>();
    ArrayList<String> ar_user_id = new ArrayList<>();
    ArrayList<String> ar_user_name = new ArrayList<>();
    ArrayList<String> ar_rate = new ArrayList<>();
    ArrayList<String> ar_review = new ArrayList<>();
    ArrayList<String> ar_datetime = new ArrayList<>();
    private CircularImageView img_uprofile, img_uprofile1;
    private LinearLayout ll_review_1, ll_review_2;
    TextView tv_viewMore,tv_ratingError;
    RatingBar rating_review,rating_review1,rating_review2;
    private TextView tv_reviewno, tv_review_uName, tv_review_date, tv_review_description, tv_review_uName1, tv_review_date1, tv_review_description1;

    public Fragment_Profile_Reviews() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {

            if (getActivity()!=null)
            {
                Task_getReview task_getReview=new Task_getReview();
                task_getReview.execute();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__profile_reviews, container, false);

        findViews();


        sessionManager=new SessionManager(getActivity());
        user_details=sessionManager.getUserDetails();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_reviews = ed_reviewDetails.getText().toString().trim();
                str_rating= String.valueOf(rating_review.getRating());

                if (str_reviews.length() == 0) {
                    ed_reviewDetails.setError("Please give review");
                    ed_reviewDetails.setFocusable(true);
                }else if (str_rating.length()==0)
                { tv_ratingError.setVisibility(View.VISIBLE);
                }
                else {

                    try {
                        data_pReview.put("space_id", Profile.mstr_spaceId);
                        data_pReview.put("user_id", user_details.get(SessionManager.user_Id));
                        data_pReview.put("rating",str_rating);
                        data_pReview.put("review", ed_reviewDetails);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Task_postReview task_postReview=new Task_postReview();
                    task_postReview.execute();

                }

            }
        });


        return view;
    }

    private void findViews() {
        this.tv_ratingError= (TextView) view.findViewById(R.id.tv_ratingError);
        this.rating_review = (RatingBar) view.findViewById(R.id.rating_profile_review_rate);
        this.ed_reviewDetails = (EditText) view.findViewById(R.id.ed_profile_review_write);
        this.btn_submit = (Button) view.findViewById(R.id.btn_profile_review_sumit);
        this.tv_viewMore = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_more);

        this.ll_review_1 = (LinearLayout) view.findViewById(R.id.ll_bookingList_detail_review_1);
        this.ll_review_2 = (LinearLayout) view.findViewById(R.id.ll_bookingList_detail_review_2);

        this.tv_reviewno = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_reviewno);

        this.tv_review_uName = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_nameUser);
        this.img_uprofile = (CircularImageView) view.findViewById(R.id.img_bookingList_detail_review_profilepic);
        this.tv_review_date = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_time);
        this.tv_review_description = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_details);

        this.tv_review_uName1 = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_nameUser1);
        this.img_uprofile1 = (CircularImageView) view.findViewById(R.id.img_bookingList_detail_review_profilepic1);
        this.tv_review_date1 = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_time1);
        this.tv_review_description1 = (TextView) view.findViewById(R.id.tv_bookingList_detail_review_details1);

        this.rating_review1= (RatingBar) view.findViewById(R.id.rating_bookingList_detail_review_rating);
        this.rating_review2= (RatingBar) view.findViewById(R.id.rating_bookingList_detail_review_rating1);
        this. tv_viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Booking_mettingRoom_list_details_moreReview.class));
            }
        });
    }

    private class Task_postReview extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata postdata = new Postdata();
                String str_review = postdata.post(Url_info.main_url + "insert_review.php", data_pReview.toString());
                JSONObject jobj_review = new JSONObject(str_review);
                status1 = jobj_review.getString(status);

                if (status1.equals("1")) {
                    Log.d("Review", "sucess");
                } else {
                    message1 = jobj_review.getString(message);
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

                Log.d("Review", "sucess");

            } else {
                Toast.makeText(getActivity(), "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }
    }

    private class Task_getReview extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            ar_reviewId.clear();
            ar_user_id.clear();
            ar_user_name.clear();
            ar_rate.clear();
            ar_review.clear();
            ar_datetime.clear();

            try {
                data_gReview.put("space_id",Profile.mstr_spaceId);
                /*data_gReview.put("limit","2");*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                Postdata postdata = new Postdata();
                String str_review = postdata.post(Url_info.main_url + "fetch_review.php", data_gReview.toString());
                JSONObject jobj_review1 = new JSONObject(str_review);
                status1 = jobj_review1.getString(status);

                if (status1.equals("1")) {


                    JSONArray jobj_review = jobj_review1.getJSONArray("review");

                    for (int i = 0; i < jobj_review.length(); i++) {
                        JSONObject review = jobj_review.getJSONObject(i);
                        ar_reviewId.add(review.getString("review_id"));
                        ar_user_id.add(review.getString("user_id"));
                        ar_user_name.add(review.getString("user_name"));
                        ar_rate.add(review.getString("rate"));
                        ar_review.add(review.getString("review"));
                        ar_datetime.add(review.getString("datetime"));

                    }


                } else {
                    message1 = jobj_review1.getString(message);
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

                String str_rating_count;
                Log.d("Review", "sucess");
                str_rating_count= String.valueOf(ar_reviewId.size());
                tv_reviewno.setText(str_rating_count + " "+getResources().getString(R.string.reviews_1));
                int count = Integer.parseInt(str_rating_count);
                if (str_rating_count.equals("0")) {
                    tv_viewMore.setVisibility(View.GONE);
                } else if (count > 2) {

                    tv_viewMore.setVisibility(View.VISIBLE);
                }

                int reviewCount =ar_reviewId.size();
                if (reviewCount==1) {
                    ll_review_1.setVisibility(View.VISIBLE);
                    ll_review_2.setVisibility(View.GONE);
                    tv_review_uName.setText(ar_user_name.get(0));
                    tv_review_date.setText(ar_datetime.get(0));
                    tv_review_description.setText(ar_review.get(0));
                    rating_review1.setRating(Float.parseFloat(ar_rate.get(0)));


                } else if (reviewCount>1) {
                    ll_review_1.setVisibility(View.VISIBLE);
                    ll_review_2.setVisibility(View.VISIBLE);

                    tv_review_uName.setText(ar_user_name.get(0));
                    tv_review_date.setText(ar_datetime.get(0));
                    tv_review_description.setText(ar_review.get(0));
                    rating_review1.setRating(Float.parseFloat(ar_rate.get(0)));

                    tv_review_uName1.setText(ar_user_name.get(1));
                    tv_review_date1.setText(ar_datetime.get(1));
                    tv_review_description1.setText(ar_review.get(1));
                    rating_review2.setRating(Float.parseFloat(ar_rate.get(0)));

                } else {
                    ll_review_1.setVisibility(View.GONE);
                    ll_review_2.setVisibility(View.GONE);
                }

            } else {
                Toast.makeText(getActivity(), "" + message1, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
    }




}
