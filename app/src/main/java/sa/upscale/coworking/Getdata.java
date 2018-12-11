package sa.upscale.coworking;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class Getdata {

  String jsonData;
  OkHttpClient client = new OkHttpClient();

  public String getJSONFromUrl(String callurl) {

    //client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
  //  client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout

    Request request = new Request.Builder()
            .url(callurl)
            .build();
    Log.i("data",callurl);
    Response responses = null;

    try {

      responses = client.newCall(request).execute();
      jsonData= responses.body().string().trim();

    } catch (IOException e) {
      e.printStackTrace();
    }

//      Log.i("data",jsonData);
    return jsonData;
  }


  }
