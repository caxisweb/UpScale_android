package sa.upscale.coworking;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;


/**
 * Created by DELL on 17-11-2015.
 */
public class Postdata {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    Response response;
    RequestBody body;
    String result;

    public String post(String url, String json) {

        client.setConnectTimeout(300, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(300, TimeUnit.SECONDS);    // socket timeout

        body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.i("data",url);
        try {

            response = client.newCall(request).execute();

            result = response.body().string();

            Log.i("data", result);
        } catch (Exception e) {

            e.printStackTrace();

        }

        return result;
    }

}



