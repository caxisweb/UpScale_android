package sa.upscale.coworking.MettingRoom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import sa.upscale.coworking.R;
import sa.upscale.coworking.adapter.Pager;

public class Profile extends AppCompatActivity {

    TabLayout tabLayout;
    private ViewPager viewPager;


    static String mstr_capacity, mstr_spaceId,mstr_hoteName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        custActionbar();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_profile);
        //tabLayout.setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent=getIntent();
        mstr_spaceId = intent.getStringExtra("spaceId");
        mstr_capacity = intent.getStringExtra("capacity_id");
        mstr_hoteName=intent.getStringExtra("hoteName");

    }

    private void setupViewPager(ViewPager viewPager) {
        Pager adapter = new Pager(getSupportFragmentManager());
        adapter.addFrag(new Fragment_Profile_OverView(), getString(R.string.overView));
        adapter.addFrag(new Fragment_Profile_spaces(), getString(R.string.space));
        adapter.addFrag(new Fragment_Profile_Reviews(), getString(R.string.reviews1));

        viewPager.setAdapter(adapter);
    }

    private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        TextView tv_title = (TextView) view.findViewById(R.id.action_bar_title);
        String strName=getString(R.string.userProfile);
        tv_title.setText(strName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView img_filter = (ImageView) view.findViewById(R.id.action_bar_filter);
        img_filter.setVisibility(View.GONE);
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



}
