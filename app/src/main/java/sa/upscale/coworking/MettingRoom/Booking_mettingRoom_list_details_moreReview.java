package sa.upscale.coworking.MettingRoom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sa.upscale.coworking.MettingRoom.adapter.adapter_profile_Reviews_more;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.Url_info;

public class Booking_mettingRoom_list_details_moreReview extends AppCompatActivity {

    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";
    JSONObject data_reviews = new JSONObject();

    ListView lst_morereview;

    ArrayList<String> ar_reviewId = new ArrayList<>();
    ArrayList<String> ar_user_id = new ArrayList<>();
    ArrayList<String> ar_user_name = new ArrayList<>();
    ArrayList<String> ar_rate = new ArrayList<>();
    ArrayList<String> ar_review = new ArrayList<>();
    ArrayList<String> ar_datetime = new ArrayList<>();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_metting_room_list_details_more_review);

        context = this;
        lst_morereview = (ListView) findViewById(R.id.lst_moreReview);

        custActionbar();
        Task_Reviews task_reviews=new Task_Reviews();
        task_reviews.execute();

    }
    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        TextView tv_title = (TextView) view.findViewById(R.id.action_bar_title);
        tv_title.setText("Reviews");

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


    public class Task_Reviews extends AsyncTask<String, String, String> {

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
                data_reviews.put("space_id", Booking_MettingRoom_List.mstr_bmettingList_space_id);
                /*data_reviews.put("limit", "10");*/
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {

                Postdata postdata = new Postdata();
                String str_reviews = postdata.post(Url_info.main_url + "fetch_review.php", data_reviews.toString());

                JSONObject jobj_reviews = new JSONObject(str_reviews);
                status1 = jobj_reviews.getString(status);

                if (status1.equals("1")) {
                    JSONArray jobj_review = jobj_reviews.getJSONArray("review");

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
                    message1 = jobj_reviews.getString(message);
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

                adapter_profile_Reviews_more adapter_profile_reviews_more = new adapter_profile_Reviews_more(context,
                        ar_reviewId, ar_user_id, ar_user_name, ar_rate, ar_review, ar_datetime);
                lst_morereview.setAdapter(adapter_profile_reviews_more);

            } else {

                Toast.makeText(Booking_mettingRoom_list_details_moreReview.this, "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Booking_mettingRoom_list_details_moreReview.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

    }
}
