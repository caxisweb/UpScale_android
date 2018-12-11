package sa.upscale.coworking;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by DELL on 09-10-2016.
 */

public class Imageupload {

    private final OkHttpClient client = new OkHttpClient();
    Response response;
    String result;

    String post(String serverURL, File file, String id) {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image

        client.setConnectTimeout(700, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(700, TimeUnit.SECONDS);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file))
                .addFormDataPart("space_id", id)
                .build();

        Request request = new Request.Builder()
                .url(serverURL)
                .post(requestBody)
                .build();

        try {

            response = client.newCall(request).execute();

            result = response.body().string();

            //Log.i("data",result);
        } catch (Exception e) {

            e.printStackTrace();

        }

        Log.i("result", result);

        return result;
    }
}
