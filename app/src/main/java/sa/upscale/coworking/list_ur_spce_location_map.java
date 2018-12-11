package sa.upscale.coworking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class list_ur_spce_location_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;
    Context context = this;
    ImageButton img_back;
    TextView tv_actionText;
    String str_lat = "0", str_lng = "0", str_address, str_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ur_spce_location_map);

        img_back = (ImageButton) findViewById(R.id.action_bar_back);
        tv_actionText = (TextView) findViewById(R.id.action_bar_title);

        Intent intent = getIntent();
        str_lat = intent.getStringExtra("lat");
        str_lng = intent.getStringExtra("lng");
        str_address = intent.getStringExtra("address");
        str_city = intent.getStringExtra("city");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent1 = new Intent();
                intent1.putExtra("type", "mapLocation");
                intent1.putExtra("lati", str_lat);
                intent1.putExtra("longi", str_lng);
                intent1.putExtra("address", str_address);
                intent1.putExtra("city","null");
                setResult(Activity.RESULT_OK,intent1);
                finish();

            }
        });

        tv_actionText.setText(getResources().getString(R.string.getyourlocation));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double lat = Double.valueOf(str_lat);
        Double lng = Double.valueOf(str_lng);
        LatLng latlng = new LatLng(lat, lng);
        marker = mMap.addMarker(new MarkerOptions().position(latlng).title(str_address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));


        // Move the camera instantly to location with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng point) {


                try {

                    /*Geocoder geocoder;
                    List<android.location.Address> addresses;
                    geocoder = new Geocoder(list_ur_spce_location_map.this, Locale.getDefault());

                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    // str_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    str_city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();


                    if (isEmpty(str_city) == true) {
                        str_city = " ";

                    }
                    if (isEmpty(state) == true) {
                        state = "";
                    } else {
                        state = "," + state +",";
                    }
                    if (isEmpty(country) == true) {
                        country = "";
                    }

                    str_address = str_city + state + country;
*/

                    new AsyncTask<String, String, String>() {

                        ProgressDialog progressDialog;

                        @Override
                        protected String doInBackground(String... strings) {

                            try {


                                    String uri = "https://maps.google.com/maps/api/geocode/json?latlng="+point.latitude+","+point.longitude+"&location_type=ROOFTOP&result_type=street_address&key=AIzaSyBVjfD3hJd8XhjDqNZva0Af-M_L0EV7mDk";
                                    HttpGet httpGet = new HttpGet(uri);
                                    HttpClient client = new DefaultHttpClient();
                                    StringBuilder stringBuilder = new StringBuilder();

                                    try {

                                        HttpResponse response = client.execute(httpGet);
                                        HttpEntity entity = response.getEntity();
                                        InputStream stream = entity.getContent();
                                        int b;
                                        while ((b = stream.read()) != -1) {
                                            stringBuilder.append((char) b);
                                        }
                                    } catch (ClientProtocolException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    JSONObject jsonObject = new JSONObject();

                                    try {

                                        jsonObject = new JSONObject(stringBuilder.toString());

                                        Log.i("responce",jsonObject.toString());
                                        JSONArray job_address=jsonObject.getJSONArray("results");
                                        JSONObject jobadd1=job_address.getJSONObject(0);
                                        //JSONObject job_add2=jobadd1.getJSONObject(0);
                                        //JSONArray jobj_add2=job_add1.getJSONArray(0);
                                        JSONArray job_add3=jobadd1.getJSONArray("address_components");
                                        JSONObject job_last=job_add3.getJSONObject(1);
                                        str_address=job_last.getString("long_name");

                                        Log.i("responce",jsonObject.toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPreExecute() {

                            progressDialog = new ProgressDialog(list_ur_spce_location_map.this);
                            progressDialog.setMessage("Please Wait");
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            super.onPreExecute();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);

                            if (marker != null) {
                                marker.remove();
                            }

                            marker.showInfoWindow();
                            marker = mMap.addMarker(new MarkerOptions().position(point).title(str_address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            str_lat = String.valueOf(point.latitude);
                            str_lng = String.valueOf(point.longitude);

                            progressDialog.dismiss();
                        }
                    }.execute();




                    /*if (marker != null) {
                        marker.remove();
                    }

                    marker.showInfoWindow();
                    marker = mMap.addMarker(new MarkerOptions().position(point).title(str_address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    str_lat = String.valueOf(point.latitude);
                    str_lng = String.valueOf(point.longitude);*/
                    //Toast.makeText(list_ur_spce_location_map.this, "address:\t" + point.toString() + "\nLat :\t" + point.latitude + "\n longi :\t " + point.longitude, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent1 = new Intent();
        intent1.putExtra("type", "mapLocation");
        intent1.putExtra("lati", str_lat);
        intent1.putExtra("longi", str_lng);
        intent1.putExtra("address", str_address);
        intent1.putExtra("city", "null");
        setResult(Activity.RESULT_OK,intent1);
        finish();

    }

}
