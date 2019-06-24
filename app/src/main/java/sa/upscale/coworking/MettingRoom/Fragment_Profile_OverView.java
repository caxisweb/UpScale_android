package sa.upscale.coworking.MettingRoom;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

public class Fragment_Profile_OverView extends Fragment {

    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";
    JSONObject data_pOverView = new JSONObject();


    SessionManager sessionManager;
    HashMap<String, String> user_Details = new HashMap<>();

    View view;
    private CarouselView img_slider;


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


    ImageView img_hoteLogo;
    TextView tv_hName, tv_location, tv_description;
    private TextView tv_from_mon, tv_from_tue, tv_from_wed, tv_from_thu, tv_from_fri, tv_from_sat, tv_from_sun, tv_to_mon, tv_to_tue, tv_to_wed, tv_to_thu, tv_to_fri, tv_to_sat, tv_to_sun;
    private TextView tv_amenities_projector, tv_amenities_aircon, tv_amenities_mail, tv_amenities_scanner, tv_amenities_looker,
            tv_amenities_wifi, tv_amenities_parking, tv_amenities_phone, tv_amenities_work24,
            tv_amenities_male, tv_amenities_female, tv_amenities_cofee;

    LinearLayout btn_contactUs, linearLayout;
    MapView mMapView;
    private GoogleMap googleMap;
    String str_lat, str_long, str_logo, str_email, str_phone, str_description, str_spacId, str_spaceuserId;
    ;

    Dialog dialog;
    int[] logo = {R.drawable.logo1};
    String temp_sliderimg = "0", user_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__profile__over_view, container, false);


        findViews();
        sessionManager = new SessionManager(getActivity());
        user_Details = sessionManager.getUserDetails();

        user_id = user_Details.get(SessionManager.user_Id);


        img_slider = (CarouselView) view.findViewById(R.id.imgslider_bookingDetails);

        if (isNetworkAvailable()) {

            Task_ProfileOverview task_profileOverview = new Task_ProfileOverview();
            task_profileOverview.execute();

            img_slider.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    if (temp_sliderimg.equals("0")) {
                        imageView.setImageResource(logo[position]);
                    } else {
                        Glide.with(getActivity()).load(Url_info.main_img + "space/" + ar_images.get(position)).centerCrop().into(imageView);
                    }


                }
            });

            mapLoad(savedInstanceState);

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

        btn_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactUs();
            }
        });
        return view;
    }

    private void ContactUs() {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        dialog = adb.setView(new View(getActivity())).create();
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.cust_contact_me);
        dialog.setCancelable(true);

        ImageView btn_close = (ImageView) dialog.findViewById(R.id.img_close);
        TextView tv_email = (TextView) dialog.findViewById(R.id.tv_contactUs_email);
        TextView tv_number = (TextView) dialog.findViewById(R.id.tv_contactUs_number);

        tv_email.setText(str_email);
        tv_number.setText(str_phone);
        dialog.show();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", str_email, null));
               /* emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");*/
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        });
        tv_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + str_phone));
                startActivity(intent);
            }
        });

    }

    private void mapLoad(Bundle savedInstanceState) {

        mMapView = (MapView) view.findViewById(R.id.map_overView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                try {

                    // For dropping a marker at a point on the Map
                    double lat = Double.parseDouble(str_lat);
                    double long1 = Double.parseDouble(str_long);
                    LatLng location = new LatLng(lat, long1);

                    Log.d("Location", String.valueOf(lat) + "\n" + long1);
                      /*
              final Marker kiel =*/
                    googleMap.addMarker(new MarkerOptions()
                            .position(location)
//                        .snippet("mar")
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.map)));
                    //markers.put(kiel.getId(), Url_info.main_img + ar_img.get(i))

                    //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void findViews() {


        linearLayout = (LinearLayout) view.findViewById(R.id.ll_snackbar);
        img_hoteLogo = (ImageView) view.findViewById(R.id.img_profile_overView_hotelLogo);
        tv_hName = (TextView) view.findViewById(R.id.tv_profile_overView__hName);
        tv_location = (TextView) view.findViewById(R.id.tv_profile_overView__location);
        btn_contactUs = (LinearLayout) view.findViewById(R.id.ll_profile_overView__contactUs);
        tv_description = (TextView) view.findViewById(R.id.tv_profile_overView_desciprion);

        tv_from_mon = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_from_mon);
        tv_from_tue = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_from_tue);
        tv_from_wed = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_from_wed);
        tv_from_thu = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_from_thu);
        tv_from_fri = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_from_fri);
        tv_from_sat = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_from_sat);
        tv_from_sun = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_from_sun);

        tv_to_mon = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_to_mon);
        tv_to_tue = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_to_tue);
        tv_to_wed = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_to_wed);
        tv_to_thu = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_to_thu);
        tv_to_fri = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_to_fri);
        tv_to_sat = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_to_sat);
        tv_to_sun = (TextView) view.findViewById(R.id.tv_bookingList_detail_openhours_to_sun);

        tv_amenities_projector = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_projectors);
        tv_amenities_aircon = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_aircond);
        tv_amenities_mail = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_mail);
        tv_amenities_scanner = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_scanner);
        tv_amenities_looker = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_looker);
        tv_amenities_wifi = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_wifi);
        tv_amenities_parking = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_parking);
        tv_amenities_phone = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_phone);
        tv_amenities_work24 = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_work24);
        tv_amenities_male = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_male);
        tv_amenities_female = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_female);
        tv_amenities_cofee = (TextView) view.findViewById(R.id.tv_bookingList_detail_amenities_cofee);


        //btn_contactUs.setOnClickListener(this);


    }


    private class Task_ProfileOverview extends AsyncTask<String, String, String> {

        String strName, strprojector = "0", strscanner = "0", strParking = "0", str_ac = "0", str_locker = "0",
                str_ph = "0", str_mail = "0", str_wifi = "0", str_work = "0", str_male = "0", str_female = "0", str_coffee = "0", str_location, str_description,
                str_price, str_capacity, str_rating, str_rating_count;

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
                data_pOverView.put("user_id", user_id);
                data_pOverView.put("your_space_id", Profile.mstr_spaceId);
                data_pOverView.put("capacity_id", Profile.mstr_capacity);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Postdata postdata = new Postdata();
                String str_dettails = postdata.post(Url_info.main_url + "your_space_detail.php", data_pOverView.toString());
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {
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
                    str_coffee = job_details.getString("coffee");

                    str_location = job_details.getString("location");
                    str_description = job_details.getString("description");
                    str_price = job_details.getString("price");
                    str_capacity = job_details.getString("capacity");
                    str_rating = job_details.getString("rating");
                    str_rating_count = job_details.getString("rating_count");
                    str_lat = job_details.getString("lat");
                    str_long = job_details.getString("long");

                    str_logo = job_details.getString("logo");
                    str_email = job_details.getString("email");
                    str_phone = job_details.getString("phone");


                    JSONArray job_day = job_details.getJSONArray("days");
                    for (int i = 0; i < job_day.length(); i++) {
                        JSONObject day = job_day.getJSONObject(i);
                        ar_openTime.add(day.getString("open_time"));
                        ar_closeTime.add(day.getString("close_time"));
                        ar_dayname.add(day.getString("day"));
                    }

                    JSONArray jobj_images = job_details.getJSONArray("images");

                    for (int i = 0; i < jobj_images.length(); i++) {
                        JSONObject images = jobj_images.getJSONObject(i);

                        ar_images.add(images.getString("image"));

                    }

                    JSONArray jobj_review = job_details.getJSONArray("review");

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


//                img_slider.setPageCount(sampleImages.length);


                Glide.with(getActivity()).load(Url_info.main_img + "logo/" + str_logo).into(img_hoteLogo);

                Log.d("ArSizeImages", String.valueOf(ar_images.size()));

                if (ar_images.size() == 0) {
                    temp_sliderimg = "0";
                    img_slider.setPageCount(logo.length);
                } else {
                    temp_sliderimg = "1";
                    img_slider.setPageCount(ar_images.size());
                }

                /*tv_personCapacity.setText("\tFor " + str_capacity + " People");


                tv_ratingreview.setText("(" + str_rating_count + " reviews)");
                tv_summarydetails.setText(str_description);
*/

                tv_hName.setText(strName);
                tv_location.setText(str_location);
                tv_description.setText(str_description);

                if (strprojector.equals("1")) {
                    tv_amenities_projector.setAlpha(1);
                } else {
                    tv_amenities_projector.setAlpha((float) 0.4);
                }

                if (strscanner.equals("1")) {
                    tv_amenities_scanner.setAlpha(1);
                } else {
                    tv_amenities_scanner.setAlpha((float) 0.4);
                }

                if (strParking.equals("1")) {
                    tv_amenities_parking.setAlpha(1);
                } else {
                    tv_amenities_parking.setAlpha((float) 0.4);
                }

                if (str_ac.equals("1")) {
                    tv_amenities_aircon.setAlpha(1);
                } else {
                    tv_amenities_aircon.setAlpha((float) 0.4);
                }

                if (str_locker.equals("1")) {
                    tv_amenities_looker.setAlpha(1);
                } else {
                    tv_amenities_looker.setAlpha((float) 0.4);
                }

                if (str_ph.equals("1")) {
                    tv_amenities_phone.setAlpha(1);
                } else {
                    tv_amenities_phone.setAlpha((float) 0.4);
                }

                if (str_mail.equals("1")) {
                    tv_amenities_mail.setAlpha(1);
                } else {
                    tv_amenities_mail.setAlpha((float) 0.4);
                }

                if (str_wifi.equals("1")) {
                    tv_amenities_wifi.setAlpha(1);
                } else {
                    tv_amenities_wifi.setAlpha((float) 0.4);
                }

                if (str_work.equals("1")) {
                    tv_amenities_work24.setAlpha(1);
                } else {
                    tv_amenities_work24.setAlpha((float) 0.4);
                }

                if (str_male.equals("1")) {
                    tv_amenities_male.setAlpha(1);
                } else {
                    tv_amenities_male.setAlpha((float) 0.4);
                }
                if (str_female.equals("1")) {
                    tv_amenities_female.setAlpha(1);
                } else {
                    tv_amenities_female.setAlpha((float) 0.4);
                }
                if (str_coffee.equals("1")) {
                    tv_amenities_cofee.setAlpha(1);
                } else {
                    tv_amenities_cofee.setAlpha((float) 0.4);
                }

                tv_from_mon.setText(ar_openTime.get(0));
                tv_from_tue.setText(ar_openTime.get(1));
                tv_from_wed.setText(ar_openTime.get(2));
                tv_from_thu.setText(ar_openTime.get(3));
                tv_from_fri.setText(ar_openTime.get(4));
                tv_from_sat.setText(ar_openTime.get(5));
                tv_from_sun.setText(ar_openTime.get(6));

                tv_to_mon.setText(ar_closeTime.get(0));
                tv_to_tue.setText(ar_closeTime.get(1));
                tv_to_wed.setText(ar_closeTime.get(2));
                tv_to_thu.setText(ar_closeTime.get(3));
                tv_to_fri.setText(ar_closeTime.get(4));
                tv_to_sat.setText(ar_closeTime.get(5));
                tv_to_sun.setText(ar_closeTime.get(6));

               /* tv_reviewno.setText(str_rating_count + " Reviews");
                int count = Integer.parseInt(str_rating_count);
                if (str_rating_count.equals("0")) {
                    tv_viewMore.setVisibility(View.GONE);
                } else if (count > 2) {

                    tv_viewMore.setVisibility(View.VISIBLE);
                }

                String reviewCount = String.valueOf(ar_reviewId.size());
                if (reviewCount.equals("1")) {
                    ll_review_1.setVisibility(View.VISIBLE);
                    ll_review_2.setVisibility(View.GONE);
                    tv_review_uName.setText(ar_user_name.get(0));
                    tv_review_date.setText(ar_datetime.get(0));
                    tv_review_description.setText(ar_review.get(0));


                } else if (reviewCount.equals("2")) {
                    ll_review_1.setVisibility(View.VISIBLE);
                    ll_review_2.setVisibility(View.VISIBLE);
                    tv_review_uName1.setText(ar_user_name.get(1));
                    tv_review_date1.setText(ar_datetime.get(1));
                    tv_review_description1.setText(ar_review.get(1));

                } else {
                    ll_review_1.setVisibility(View.GONE);
                    ll_review_2.setVisibility(View.GONE);
                }
*/

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
