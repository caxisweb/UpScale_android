package sa.upscale.coworking.MettingRoom;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.util.List;

import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.R;
import sa.upscale.coworking.Url_info;
import sa.upscale.coworking.fregment.Home_freg;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThankYou extends AppCompatActivity {

    private ViewFlipper fliper_add;

    TextView tv_invitation;
    android.support.v4.app.FragmentTransaction ft;

    TextView tv_refCode;
    ImageView img_facebook, img_twitter, img_google, img_close,img_linkedin;

    String str_reference_no, str_booking_id;
    View view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_thank__u_list_ur_space);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle b = getIntent().getExtras();
        str_booking_id = b.getString("booking_id");
        str_reference_no = b.getString("reference_no");


        tv_invitation = findViewById(R.id.tv_invite);
        tv_refCode = findViewById(R.id.tv_referal_no);
        img_facebook = findViewById(R.id.login_button_fb);
        img_twitter = findViewById(R.id.img_thankyou_twitter);
        img_google = findViewById(R.id.img_thankyou_googlePlus);
        img_linkedin=findViewById(R.id.btn_login_linkedIn);
        img_close = findViewById(R.id.img_thankyou_close);

        tv_refCode.setText(str_reference_no);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_home = new Intent(ThankYou.this, NavigationActivity.class);
                i_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i_home);
            }
        });

        tv_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_invite = new Intent(ThankYou.this, Invitation.class);
                i_invite.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i_invite.putExtra("booking_id",str_booking_id);
                finish();
                startActivity(i_invite);


            }
        });


        img_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String str_msg = "Please explore " + Booking_MettingRoom_list_details.strName + " and join the coworkers community now by booking a space" + "\n" + "https://coworkinghive.com/app.php";

                ShareDialog shareDialog = new ShareDialog(ThankYou.this);
                ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                        .putString("og:type", "books.book")
                        /*.putString("og:title", "A Game of Thrones")*/
                        .putString("og:description", str_msg)
                        .putString("books:isbn", "0-553-57340-3").build();

                ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                        .setActionType("books.reads").putObject("book", object)
                        .build();
                ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                        .setPreviewPropertyName("book")
                        .setAction(action)
                        .build();
                shareDialog.show(content);

            }
        });

        img_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String str_msg = "Please explore " + Booking_MettingRoom_list_details.strName + " and join the coworkers community now by booking a space" + "\n" + "https://coworkinghive.com/app.php";
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hive Upscale");
                shareIntent.putExtra(Intent.EXTRA_TEXT, str_msg);
                startActivity(Intent.createChooser(shareIntent, "choose one"));

                /*Intent shareIntent = new PlusShare.Builder(ThankYou.this)
                        .setType("text/plain")
                        .setText(str_msg)
                        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);*/

            }
        });

        img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isAppInstalled = appInstalledOrNot("com.twitter.android");
                Intent intent = new Intent();
                String str_msg = "Please explore " + Booking_MettingRoom_list_details.strName + " and join the coworkers community now by booking a space" + "\n" + "https://coworkinghive.com/app.php";

                if (isAppInstalled) {

                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, str_msg);
                    intent.setType("text/plain");
                    // intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/jpeg");
                    intent.setPackage("com.twitter.android");


                } else {
                    // Do whatever we want to do if application not installed
                    // For example, Redirect to play store

                    String tweetUrl = "https://twitter.com/intent/tweet?text=" + str_msg;
                    Uri uri = Uri.parse(tweetUrl);
                    intent = new Intent(Intent.ACTION_VIEW, uri);

                }
                startActivity(intent);

            }
        });

        img_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str_msg = "Please explore " + Booking_MettingRoom_list_details.strName + " and join the coworkers community now by booking a space" + "\n" + "https://coworkinghive.com/app.php";

                Intent linkedinIntent = new Intent(Intent.ACTION_SEND);
                linkedinIntent.setType("text/plain");
                linkedinIntent.putExtra(Intent.EXTRA_TEXT, str_msg);

                boolean linkedinAppFound = false;
                List<ResolveInfo> matches2 = getPackageManager()
                        .queryIntentActivities(linkedinIntent, 0);

                for (ResolveInfo info : matches2) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith(
                            "com.linkedin")) {
                        linkedinIntent.setPackage(info.activityInfo.packageName);
                        linkedinAppFound = true;
                        break;
                    }
                }

                if (linkedinAppFound) {
                    startActivity(linkedinIntent);
                }
                else
                {
                    Toast.makeText(ThankYou.this,"LinkedIn app not Insatlled in your mobile", Toast.LENGTH_LONG).show();
                }

            }
        });

        ViewFlipper fliper_add = findViewById(R.id.flipper_add);
        AdView google_AdView = findViewById(R.id.adView);

        if(Home_freg.add_url.size()>0){

            fliper_add.setVisibility(View.VISIBLE);
            google_AdView.setVisibility(View.GONE);
            fliper_add.removeAllViews();

            for (int i = 0; i < Home_freg.add_url.size(); i++) {

                View add_view = LayoutInflater.from(
                        ThankYou.this).inflate(
                        R.layout.custome_addview, null);

                ImageView img_adv = add_view.findViewById(R.id.img_adv);

                Picasso.with(ThankYou.this)
                        .load(Url_info.main_img + "advertise/" + Home_freg.add_img.get(i))
                        .error(R.drawable.addbaner)
                        .into(img_adv);

                final int k=i;

                img_adv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(Home_freg.add_url.get(k).equals("")){

                        }else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Home_freg.add_url.get(k)));
                            startActivity(browserIntent);
                        }
                    }
                });


                fliper_add.addView(add_view);
            }

            fliper_add.startFlipping();

        }else{

            fliper_add.setVisibility(View.GONE);
            google_AdView.setVisibility(View.VISIBLE);

            MobileAds.initialize(ThankYou.this,getString(R.string.google_admobo_app_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            google_AdView.loadAd(adRequest);

        }

    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i_home = new Intent(ThankYou.this, NavigationActivity.class);
        i_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i_home);
    }
}
