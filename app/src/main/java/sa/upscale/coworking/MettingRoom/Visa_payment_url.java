package sa.upscale.coworking.MettingRoom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import sa.upscale.coworking.R;

/**
 * Created by DELL on 17-12-2017.
 */

public class Visa_payment_url extends AppCompatActivity {

    String visa_payment_url;
    WebView we_payment_url;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visa_payment_url);

        Bundle b = getIntent().getExtras();
        visa_payment_url = b.getString("visa_url");

        custActionbar();

        we_payment_url = findViewById(R.id.web_payment_url);
        we_payment_url.setWebViewClient(new myWebClient());
        WebSettings webSettings = we_payment_url.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //we_payment_url.loadUrl(visa_payment_url);
        we_payment_url.loadUrl(visa_payment_url);
    }

    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = view.findViewById(R.id.action_bar_back);
        TextView tv_title = view.findViewById(R.id.action_bar_title);
        String strName = "Visa Payment";
        tv_title.setText(strName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("status","Fail");
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

        ImageView img_filter = view.findViewById(R.id.action_bar_filter);
        img_filter.setVisibility(View.GONE);
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            Log.i("webview_url ",url);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // Page loading finished
            Log.i("webview_url 2",url);

            if(url.equals("https://esal-sa.com/home/thankyou/success")){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("status","success");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }else{

            }

        }
    }

    // To handle &quot;Back&quot; key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && we_payment_url.canGoBack()) {
            we_payment_url.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("status","Fail");
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
        super.onBackPressed();
    }
}
