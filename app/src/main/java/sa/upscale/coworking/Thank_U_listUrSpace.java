package sa.upscale.coworking;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Thank_U_listUrSpace extends AppCompatActivity {


    TextView tv_ref_no,tv_invite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank__u_list_ur_space);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        tv_ref_no= (TextView) findViewById(R.id.tv_referal_no);
        tv_invite= (TextView) findViewById(R.id.tv_invite);



        tv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Thank_U_listUrSpace.this,NavigationActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(Thank_U_listUrSpace.this,Login.class));
    }
}
