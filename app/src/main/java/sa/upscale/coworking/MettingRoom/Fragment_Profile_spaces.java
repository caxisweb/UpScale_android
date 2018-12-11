package sa.upscale.coworking.MettingRoom;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import sa.upscale.coworking.MettingRoom.adapter.Custome_Map_Item;
import sa.upscale.coworking.MettingRoom.adapter.adapter_profile_spaceList;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.Url_info;

public class Fragment_Profile_spaces extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";
    JSONObject data_profileSpacelist = new JSONObject();

    View view;
    ListView lst_profileSpace;
    LinearLayout ll_tab_list, ll_tab_map, ll_list_details, ll_mapdetails;

    private ArrayList<String> ar_space_id = new ArrayList<>();
    private ArrayList<String> ar_name = new ArrayList<>();
    private ArrayList<String> ar_projector = new ArrayList<>();
    private ArrayList<String> ar_scanner = new ArrayList<>();
    private ArrayList<String> ar_parking = new ArrayList<>();
    private ArrayList<String> ar_ac = new ArrayList<>();
    private ArrayList<String> ar_locker = new ArrayList<>();
    private ArrayList<String> ar_ph = new ArrayList<>();
    private ArrayList<String> ar_mail = new ArrayList<>();
    private ArrayList<String> ar_wifi = new ArrayList<>();
    private ArrayList<String> ar_work = new ArrayList<>();
    private ArrayList<String> ar_location = new ArrayList<>();
    private ArrayList<String> ar_price = new ArrayList<>();
    private ArrayList<String> ar_capacity = new ArrayList<>();
    private ArrayList<String> ar_img = new ArrayList<>();
    private ArrayList<String> ar_lat = new ArrayList<>();
    private ArrayList<String> ar_rating = new ArrayList<>();
    private ArrayList<String> ar_rating_count = new ArrayList<>();
    private ArrayList<String> ar_long = new ArrayList<>();
    private ArrayList<String> ar_distance = new ArrayList<>();

    private Hashtable<String, String> markers = new Hashtable<>();
    private Marker marker;
    private GoogleMap mMap;
    LinearLayout linearLayout;
    int i = 0;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public Fragment_Profile_spaces() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                Task_profileSpaceList task_profileSpaceList = new Task_profileSpaceList();
                task_profileSpaceList.execute();

            } else {

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__profile_spaces, container, false);

        findViews();

        initImageLoader();
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)        //    Display Stub Image
                .showImageForEmptyUri(R.mipmap.ic_launcher)    //    If Empty image found
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

        if (isNetworkAvailable()) {

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

        return view;
    }

    private void clearArrayList() {

        ar_space_id.clear();
        ar_name.clear();
        ar_projector.clear();
        ar_scanner.clear();
        ar_parking.clear();
        ar_ac.clear();
        ar_locker.clear();
        ar_ph.clear();
        ar_mail.clear();
        ar_wifi.clear();
        ar_work.clear();
        ar_location.clear();
        ar_price.clear();
        ar_capacity.clear();
        ar_img.clear();
        ar_lat.clear();
        ar_long.clear();
        ar_distance.clear();
    }

    private void findViews() {
        lst_profileSpace = (ListView) view.findViewById(R.id.lst_profile_spaceList);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll_snackbar);
        ll_tab_list = (LinearLayout) view.findViewById(R.id.ll_tab_list);
        ll_tab_map = (LinearLayout) view.findViewById(R.id.ll_tab_map);

        ll_list_details = (LinearLayout) view.findViewById(R.id.ll_list_details);
        ll_mapdetails = (LinearLayout) view.findViewById(R.id.ll_map_details);

        ll_mapdetails.setVisibility(View.GONE);

        ll_tab_map.setOnClickListener(this);
        ll_tab_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.ll_tab_list:
                ll_list_details.setVisibility(View.VISIBLE);
                ll_mapdetails.setVisibility(View.GONE);

                if (isNetworkAvailable()) {
                    Task_profileSpaceList task_profileSpaceList = new Task_profileSpaceList();
                    task_profileSpaceList.execute();

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
                break;
            case R.id.ll_tab_map:

                if (isNetworkAvailable()) {
                    /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map1);
                    mapFragment.getMapAsync(Booking_MettingRoom_List.this);*/

                    SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                            .findFragmentById(R.id.map_space);
                    mapFragment.getMapAsync(this);

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
                ll_list_details.setVisibility(View.GONE);
                ll_mapdetails.setVisibility(View.VISIBLE);

                break;

            /*
                SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                        .findFragmentById(R.id.map1);

                mapFragment.getMapAsync(this);

                ll_list_details.setVisibility(View.GONE);
                ll_mapdetails.setVisibility(View.VISIBLE);
                break;*/

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setInfoWindowAdapter(new Fragment_Profile_spaces.CustomInfoWindowAdapter());

        /*LatLng location = new LatLng(Double.parseDouble(ar_lat.get(0)), Double.parseDouble(ar_long.get(0)));
        final Marker hamburg = googleMap.addMarker(new MarkerOptions().position(location).title(ar_name.get(0)));
        markers.put(hamburg.getId(), "http://img.india-forums.com/images/100x100/37525-a-still-image-of-akshay-kumar.jpg");*/
        LatLng location = null;
        for (i = 0; i < ar_lat.size(); i++) {
            String str_lat = ar_lat.get(i);
            String str_lng = ar_long.get(i);

            if (str_lat.equals("0") && str_lng.equals("0")) {

            } else {
                location = new LatLng(Double.parseDouble(ar_lat.get(i)), Double.parseDouble(ar_long.get(i)));

                Gson gson = new Gson();
                String title = ar_name.get(i);
                String address = ar_location.get(i);
                String price = ar_price.get(i);
                String capacity = ar_capacity.get(i);
                String distance = ar_distance.get(i);
                String space_id=ar_space_id.get(i);

                String spnippt = gson.toJson(new Custome_Map_Item(title, address, price, capacity, distance,space_id));

                final Marker kiel = googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(ar_name.get(0))
                        .snippet(spnippt)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.map)));

                markers.put(kiel.getId(), Url_info.main_img + "space/" + ar_img.get(0));

            }


        }


    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.marker_popup,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (Fragment_Profile_spaces.this.marker != null
                    && Fragment_Profile_spaces.this.marker.isInfoWindowShown()) {
                Fragment_Profile_spaces.this.marker.hideInfoWindow();
                Fragment_Profile_spaces.this.marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            Fragment_Profile_spaces.this.marker = marker;

            String url = null;

            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null &&
                        markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final ImageView image = ((ImageView) view.findViewById(R.id.img_bookingList_hotelPic));

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {
                imageLoader.displayImage(url, image, options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view,
                                        loadedImage);
                                getInfoContents(marker);
                            }
                        });
            } else {
                image.setImageResource(R.drawable.logo3);
            }




            final String Detail_object = marker.getSnippet();
            String str_address = null;
            String str_price = null, str_distance = null, str_capacity = null,str_title = null;
            try {
                JSONObject object = new JSONObject(Detail_object);
                str_address = object.getString("Address");
                str_price = object.getString("Price");
                str_distance = object.getString("distance");
                str_capacity = object.getString("capacity");
                str_title = object.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final TextView txt_address = ((TextView) view
                    .findViewById(R.id.tv_bookingList_hotel_location));
            final TextView txt_price = (TextView) view.findViewById(R.id.tv_bookingList_hotel_price);
            final TextView txt_capacity = (TextView) view.findViewById(R.id.tv_bookingList_hotel_person_capacity);
            final TextView txt_distance = (TextView) view.findViewById(R.id.tv_bookingList_hotel_location_1);
            final TextView titleUi = ((TextView) view.findViewById(R.id.tv_bookingList_hotel_Title));

            if (str_address != null) {
                txt_address.setText(str_address);
            } else {
                txt_address.setText("");
            }
            if (str_price != null) {
                txt_price.setText(str_price);
            } else {
                txt_price.setText("");
            }
            if (str_capacity != null) {
                txt_capacity.setText(str_capacity);
            } else {
                txt_capacity.setText("");
            }
            if (str_distance != null) {
                txt_distance.setText(str_distance);
            } else {
                txt_distance.setText("");
            }
            if (str_title != null) {
                titleUi.setText(str_title);
            } else {
                titleUi.setText("");
            }
            return view;
        }
    }


    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) getActivity().
                    getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity()).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);
    }


    private class Task_profileSpaceList extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            clearArrayList();
            try {
                data_profileSpacelist.put("space_user_id", Booking_MettingRoom_List.mstr_bmettingList_space_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Postdata postdata = new Postdata();
                String lstspace = postdata.post(Url_info.main_url + "space_list.php", data_profileSpacelist.toString());
                JSONObject jobj_space = new JSONObject(lstspace);

                status1 = jobj_space.getString(status);
                if (status1.equals("1")) {

                    Log.d("signup", "successfully register");

                    JSONArray s_list = jobj_space.getJSONArray("space_list");

                    for (int i = 0; i < s_list.length(); i++) {
                        JSONObject jobj_list = s_list.getJSONObject(i);

                        ar_space_id.add(jobj_list.getString("space_id"));
                        ar_name.add(jobj_list.getString("name"));
                        ar_projector.add(jobj_list.getString("projector"));
                        ar_scanner.add(jobj_list.getString("scanner"));
                        ar_parking.add(jobj_list.getString("parking"));
                        ar_ac.add(jobj_list.getString("ac"));
                        ar_locker.add(jobj_list.getString("locker"));
                        ar_ph.add(jobj_list.getString("ph"));
                        ar_mail.add(jobj_list.getString("mail"));
                        ar_wifi.add(jobj_list.getString("wifi"));
                        ar_work.add(jobj_list.getString("work"));
                        ar_location.add(jobj_list.getString("location"));
                        ar_price.add(jobj_list.getString("price"));
                        ar_capacity.add(jobj_list.getString("capacity"));
                        ar_img.add(jobj_list.getString("img"));
                        ar_lat.add(jobj_list.getString("lat"));
                        ar_long.add(jobj_list.getString("long"));
                        ar_rating.add(jobj_list.getString("rating"));
                        ar_rating_count.add(jobj_list.getString("rating_count"));
                        ar_distance.add(jobj_list.getString("distance"));

                    }

                } else {
                    message1 = jobj_space.getString(message);
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


                adapter_profile_spaceList adapter_meeting_room_list = new adapter_profile_spaceList(getActivity(), ar_space_id, ar_name, ar_projector,
                        ar_scanner, ar_parking, ar_ac, ar_locker, ar_ph, ar_mail, ar_wifi, ar_work, ar_location, ar_price, ar_capacity, ar_img, ar_rating, ar_rating_count, ar_distance);

                lst_profileSpace.setAdapter(adapter_meeting_room_list);

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
