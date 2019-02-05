package sa.upscale.coworking.Feedback;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedBack extends Fragment {

    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";

    JSONObject data_feedback = new JSONObject();

    View view;
    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();

    EditText ed_feedBack;
    Button btn_submit;

    public FeedBack() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed_back, container, false);

        navImageIcon();
        NavigationActivity.backFlag = 1;
        sessionManager = new SessionManager(getActivity());

        ed_feedBack = view.findViewById(R.id.edt_feedBack_msg);
        btn_submit = view.findViewById(R.id.btn_feedBack_Submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_feedback = ed_feedBack.getText().toString().trim();
                if (str_feedback.length() == 0) {
                    ed_feedBack.setError("Enter Your Feedback");
                    ed_feedBack.requestFocus();
                } else {

                    try {

                        if(sessionManager.isLoggedIn()) {

                            userDetails = sessionManager.getUserDetails();

                            data_feedback.put("user_id", userDetails.get(SessionManager.user_Id));
                            data_feedback.put("feedback", str_feedback);

                            Task_feedback task_feedback = new Task_feedback();
                            task_feedback.execute();

                        }else{

                            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.login_message), Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        return view;
    }

    private void navImageIcon() {

        ImageView imageView = view.findViewById(R.id.navIcon);
        TextView tv_title = view.findViewById(R.id.tv_nav_action_titile);
        tv_title.setText(getResources().getString(R.string.feedback));
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }


    private class Task_feedback extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {
                Postdata postdata = new Postdata();
                String feedback = postdata.post(Url_info.main_url + "insert_feedback.php", data_feedback.toString());

                JSONObject jsonObject = new JSONObject(feedback);
                status1 = jsonObject.getString(status);

                if (status1.equals("1")) {

                    message1=jsonObject.getString(message);

                } else {

                    message1 = jsonObject.getString(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait !!!");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (status1.equals("1")) {
                ed_feedBack.setText("");
                Toast.makeText(getActivity(), "Feedback Send", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), NavigationActivity.class));
            } else {
                Toast.makeText(getActivity(), "" + message1, Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();

        }
    }
}
