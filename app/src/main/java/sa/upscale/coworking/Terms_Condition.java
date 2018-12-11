package sa.upscale.coworking;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import bolts.AppLinks;


public class Terms_Condition extends AppCompatActivity {

    WebView web_termsCondition;
    ImageView img_back;
    TextView tv_text1, tv_text2, tv_text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms__condition);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        img_back = (ImageView) findViewById(R.id.img_back_signUp);
        tv_text1 = (TextView) findViewById(R.id.tv_termsCond_text1);
        tv_text2 = (TextView) findViewById(R.id.tv_termsCond_text2);
        tv_text3 = (TextView) findViewById(R.id.tv_termsCond_text3);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(Sign_Up.this,Login.class));
            }
        });

        tv_text1.setText(getString(R.string.termsCondition_text1));
        tv_text2.setText(getString(R.string.termsCondition_text2));
        tv_text3.setText(getString(R.string.termsCondition_text3));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}
