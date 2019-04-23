package sa.upscale.coworking;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by DELL on 03-02-2016.
 */
@SuppressWarnings("ALL")
public class LocationService extends Service implements LocationListener {

    JSONObject user_data1 = new JSONObject();
    HashMap<String, String> user_detail = new HashMap<String, String>();
    Handler handler2 = new Handler();

    double lat = 0;
    double lng = 0;
    private String d_id = "0";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location From GPS
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider, 20000, 0, LocationService.this);

        /*try {
            Bundle b = intent.getExtras();
            d_id = b.getString("d_id");
        } catch (Exception e) {

        }*/

        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {


                /*final LocationManager mlocmag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                final LocationListener mlocList = new MyLocationList();
                final Location loc = mlocmag.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mlocmag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, mlocList);
*/

                Task_locationupdate t_location = new Task_locationupdate();
                t_location.execute();


                handler2.postDelayed(this, 300000);

            }
        }, 300000);


    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude(); // Updated lat
        lng = location.getLongitude(); // Updated long

        Log.i("latlg",location.getLatitude()+" , "+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class Task_locationupdate extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            SessionManager session = new SessionManager(getApplicationContext());
            user_detail = session.getUserDetails();

            try {

                try {

                    user_data1.put("emp_id", user_detail.get(SessionManager.user_Id));
                    user_data1.put("lat", lat);
                    user_data1.put("lag", lng);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("lat lg", user_data1.toString());
                Postdata p_cancel = new Postdata();
                String data_cancel = p_cancel.post(Url_info.main_url + "update_latlag.php", user_data1.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            super.onPreExecute();
        }

    }

    /*public class MyLocationList implements LocationListener {

        public void onLocationChanged(Location loc) {
            // TODO Auto-generated method stub

            lat = loc.getLatitude(); // Updated lat
            lng = loc.getLongitude(); // Updated long

            //Task_locationupdate t_cancel = new Task_locationupdate();
            //t_cancel.execute();


        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "GPS Disable ", Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }*/
}