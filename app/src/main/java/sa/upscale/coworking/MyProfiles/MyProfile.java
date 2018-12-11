package sa.upscale.coworking.MyProfiles;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import sa.upscale.coworking.MyHistory.My_History_Activity;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends Fragment implements View.OnClickListener {

    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_fatchProfile = new JSONObject();

    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();


    CircleImageView img_profile;
    TextView tv_name,tv_director,tv_ratingCount;
    RatingBar rt_rating;
String picpath_identify;
    android.support.v4.app.FragmentTransaction ft;

    public MyProfile() {
        // Required empty public constructor
    }

    View view;
    LinearLayout btn_editProfile, btn_myWishlist, btn_myHistory, btn_mySpace, btn_bookNow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        NavigationActivity.backFlag = 1;
        navImageIcon();
        sessionManager=new SessionManager(getActivity());
        user_details=sessionManager.getUserDetails();

        finViews();

        Task_getProfileDetails task_getProfileDetails=new Task_getProfileDetails();
        task_getProfileDetails.execute();
        return view;
    }

    private void finViews() {

        img_profile= (CircleImageView) view.findViewById(R.id.img_profile);
        tv_name= (TextView) view.findViewById(R.id.tv_myprofile_name);
        tv_director= (TextView) view.findViewById(R.id.tv_myprofile_director);
        tv_ratingCount= (TextView) view.findViewById(R.id.tv_myprofile_ratingcount);
        rt_rating= (RatingBar) view.findViewById(R.id.rating_myProfile);


        btn_editProfile = (LinearLayout) view.findViewById(R.id.ll_myprofile_editprofile);
        btn_myWishlist = (LinearLayout) view.findViewById(R.id.ll_myprofile_mywishlist);
        btn_myHistory = (LinearLayout) view.findViewById(R.id.ll_myprofile_myHistory);
        btn_mySpace = (LinearLayout) view.findViewById(R.id.ll_myprofile_mySpace);
        btn_bookNow = (LinearLayout) view.findViewById(R.id.ll_myprofile_bookNow);

        btn_editProfile.setOnClickListener(this);
        btn_myWishlist.setOnClickListener(this);
        btn_myHistory.setOnClickListener(this);
        btn_mySpace.setOnClickListener(this);
        btn_bookNow.setOnClickListener(this);
    }

    private void navImageIcon() {

        ImageView imageView = (ImageView) view.findViewById(R.id.navIcon);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_nav_action_titile);
        String strName = getString(R.string.my_profile);
        tv_title.setText(strName);
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }


    @Override
    public void onClick(View v) {
        int Id = v.getId();

        switch (Id) {

            case R.id.ll_myprofile_editprofile:

                startActivity(new Intent(getActivity(),EditProfile.class));
                break;

            case R.id.ll_myprofile_mywishlist:
                startActivity(new Intent(getActivity(),WishList.class));
                break;

            case R.id.ll_myprofile_myHistory:

                My_History_Activity history = new My_History_Activity();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.mainContent, history).commit();

                break;

            case R.id.ll_myprofile_mySpace:
                break;

            case R.id.ll_myprofile_bookNow:
                //Toast.makeText(getActivity(), "Success test", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),NavigationActivity.class));



                break;
        }
    }


    private class Task_getProfileDetails extends AsyncTask<String, String, String> {

        String mstr_name, mstr_phone, mstr_email, mstr_userType, mstr_img;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {

                data_fatchProfile.put("user_id", user_details.get(SessionManager.user_Id));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                Postdata postdata = new Postdata();
                Log.d("Profile", data_fatchProfile.toString());
                String str_dettails = postdata.post(Url_info.main_url + "my_profile.php", data_fatchProfile.toString());
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {
                    mstr_name = job_details.getString("name");
                    mstr_phone = job_details.getString("phone");

                    mstr_email = job_details.getString("email");
                    mstr_img = job_details.getString("image");
                    mstr_userType = job_details.getString("user_type");


                } else {
                    message1 = job_details.getString(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {

                try {
                    tv_name.setText(mstr_name);

                    if (mstr_userType.equalsIgnoreCase("select"))
                    {
                        tv_director.setVisibility(View.GONE);
                    }else {
                        String ss=mstr_userType.toUpperCase();
                        tv_director.setText(ss);
                    }

                   // edProfileEmailId.setText(mstr_email);

                    picpath_identify = mstr_img.substring(0, 5);
                    if (picpath_identify.equals("https"))
                    {
                        Picasso.with(getActivity()).load(mstr_img).placeholder(R.drawable.logo3).into(img_profile);
                    }else {
                        Picasso.with(getActivity()).load(Url_info.main_img+"profile/"+mstr_img).placeholder(R.drawable.logo3).into(img_profile);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ..");
            progressDialog.show();
            super.onPreExecute();
        }

    }

}
