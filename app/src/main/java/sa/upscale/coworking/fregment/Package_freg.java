package sa.upscale.coworking.fregment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sa.upscale.coworking.Login;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;
import sa.upscale.coworking.adapter.Packageadepter;
import sa.upscale.coworking.model.Package;

/**
 * Created by DELL on 21-02-2018.
 */

public class Package_freg extends Fragment {

    private static final String status = "status";
    private static final String message = "message";

    String status1 = "0", message1 = "try Again";

    JSONObject data_bookNow = new JSONObject();
    JSONObject data_user = new JSONObject();

    SessionManager session;
    HashMap<String, String> user_details = new HashMap<>();
    HashMap<String, String> lang = new HashMap<>();

    ArrayList<Package> package_list = new ArrayList<>();

    String pack_id, pack_name, pack_rat, pack_price, pack_type, pack_detail, pack_space, str_bank_name, str_iban, str_account_no, str_bank_name_ar, str_iban_ar, str_account_no_ar, str_is_subscribe;

    Dialog dialog;
    ListView lv_packagelist;
    View view;

    public Package_freg() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.packagelist_freg, container, false);
        NavigationActivity.backFlag = 1;
        navImageIcon();

        session = new SessionManager(getActivity());

        lv_packagelist = view.findViewById(R.id.lst_package);

        lv_packagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                pack_id = package_list.get(i).getPack_id();
                pack_name = package_list.get(i).getPack_name();
                pack_type = package_list.get(i).getPack_type();
                pack_price = package_list.get(i).getPack_price();
                pack_detail = package_list.get(i).getPack_detail();
                pack_space = package_list.get(i).getPack_space();
                str_bank_name = package_list.get(i).getStr_bank_name();
                str_account_no = package_list.get(i).getStr_account_no();
                str_iban = package_list.get(i).getStr_iban();
                str_bank_name_ar = package_list.get(i).getStr_bank_name_ar();
                str_account_no_ar = package_list.get(i).getStr_account_no_ar();
                str_iban_ar = package_list.get(i).getStr_iban_ar();
                str_is_subscribe = package_list.get(i).getStr_subscribe_status();

                detailDlg();

            }
        });

        Task_package t_p = new Task_package();
        t_p.execute();

        return view;
    }


    private class Task_package extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            package_list.clear();


            try {

                if (session.isLoggedIn()) {

                    user_details = session.getUserDetails();
                    data_user.put("user_id", user_details.get(SessionManager.user_Id));
                    data_user.put("country_code", Home_freg.country_code);

                } else {
                    data_user.put("user_id", "0");
                    data_user.put("country_code", Home_freg.country_code);
                }

                Postdata get_package = new Postdata();
                String data_package = get_package.post(Url_info.main_url + "package_list.php",data_user.toString());
                Log.i("responce", data_package);
                JSONObject jobj_package = new JSONObject(data_package);
                status1 = jobj_package.getString(status);

                if (status1.equals("1")) {

                    JSONArray p_list = jobj_package.getJSONArray("package_list");

                    for (int i = 0; i < p_list.length(); i++) {

                        JSONObject jobj_list = p_list.getJSONObject(i);

                        Package p1 = new Package(jobj_list.getString("package_id"), jobj_list.getString("package_name"), "0", jobj_list.getString("package_price"), jobj_list.getString("package_type"), jobj_list.getString("package_desc"), jobj_list.getString("space_name"), jobj_list.getString("bank_name"), jobj_list.getString("iban"), jobj_list.getString("account_no"), jobj_list.getString("bank_name_ar"), jobj_list.getString("iban_ar"), jobj_list.getString("account_no_ar"), jobj_list.getString("is_subscribe"),jobj_list.getString("company_name"));
                        package_list.add(p1);
                    }

                } else {
                    message1 = jobj_package.getString(message);
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

                Packageadepter adp_package = new Packageadepter(getActivity().getApplicationContext(), package_list);
                lv_packagelist.setAdapter(adp_package);

            } else {


                Toast.makeText(getActivity(), message1, Toast.LENGTH_SHORT).show();

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


    @SuppressLint({"NewApi", "WrongConstant"})
    private void detailDlg() {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        dialog = adb.setView(new View(getActivity())).create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.setContentView(R.layout.package_detail_view);
        dialog.setCancelable(false);

        ImageView img_close = dialog.findViewById(R.id.img_filter_close);
        Button btn_unsubscribe = dialog.findViewById(R.id.btn_unsubscribe);
        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);
        final RadioGroup rg_paymenthod = dialog.findViewById(R.id.rg_paymentmethod);

        RadioButton rd_cod = dialog.findViewById(R.id.rd_cod);
        RadioButton rd_visa = dialog.findViewById(R.id.rd_visa);
        RadioButton rd_bank = dialog.findViewById(R.id.rd_bank);

        TextView tv_package_detail = dialog.findViewById(R.id.tv_package_detail);
        TextView tv_package_space = dialog.findViewById(R.id.tv_package_space);
        final TextView tv_note_visa = dialog.findViewById(R.id.tv_visa_note);
        final LinearLayout lv_bank_detail = dialog.findViewById(R.id.lv_bank_detail);
        final TextView tv_bank_name = dialog.findViewById(R.id.tv_bank_name);
        final TextView tv_ibn = dialog.findViewById(R.id.tv_ibn);
        final TextView tv_account_no = dialog.findViewById(R.id.tv_account_no);

        tv_package_detail.setText(pack_detail);

        if(!session.getlanguageCode().equals("1")){
            tv_package_detail.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }

        String[] array3 = pack_space.split(",");
        String space_name = "";

        for (String temp : array3) {

            space_name = space_name + "" + temp + "\n";
        }

        tv_package_space.setText(space_name);

        if (str_bank_name.equals("") || str_bank_name == null) {

            rd_bank.setEnabled(false);

        } else {

            lang = session.getlanguageCode();

            String str_lang_code = lang.get(SessionManager.user_languageCode);

            if (str_lang_code == null) {

                tv_bank_name.setText(getResources().getString(R.string.bank_name) + " : " + str_bank_name);
                tv_ibn.setText(getResources().getString(R.string.ibn_no) + " : " + str_iban);
                tv_account_no.setText(getResources().getString(R.string.acc_no) + " : " + str_account_no);

            } else if (str_lang_code.equals("1")) {

                tv_bank_name.setText(getResources().getString(R.string.bank_name) + " : " + str_bank_name);
                tv_ibn.setText(getResources().getString(R.string.ibn_no) + " : " + str_iban);
                tv_account_no.setText(getResources().getString(R.string.acc_no) + " : " + str_account_no);

            } else if (str_lang_code.equals("2")) {

                tv_bank_name.setText(getResources().getString(R.string.bank_name) + " : " + str_bank_name_ar);
                tv_ibn.setText(getResources().getString(R.string.ibn_no) + " : " + str_iban_ar);
                tv_account_no.setText(getResources().getString(R.string.acc_no) + " : " + str_account_no_ar);
            }
        }
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (str_is_subscribe.equals("1")) {
            btn_unsubscribe.setVisibility(View.VISIBLE);
        } else {
            btn_unsubscribe.setVisibility(View.GONE);
        }

        btn_unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Unsubscribe this package ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!

                                if (session.isLoggedIn()) {

                                    try {
                                        user_details = session.getUserDetails();

                                        data_bookNow.put("package_id", pack_id);
                                        data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));


                                        Task_Unsubscribe task_unsubscribe = new Task_Unsubscribe();
                                        task_unsubscribe.execute();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {

                                    Intent i_login = new Intent(getActivity(), Login.class);
                                    i_login.putExtra("flag", 1);
                                    startActivity(i_login);

                                }


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
            }
        });

        rd_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_note_visa.setVisibility(View.VISIBLE);
                lv_bank_detail.setVisibility(View.GONE);
            }
        });

        rd_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_note_visa.setVisibility(View.GONE);
                lv_bank_detail.setVisibility(View.GONE);
            }
        });

        rd_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_note_visa.setVisibility(View.GONE);
                lv_bank_detail.setVisibility(View.VISIBLE);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Subscribe to this package ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!

                                if (session.isLoggedIn()) {

                                    int sel_method = rg_paymenthod.getCheckedRadioButtonId();

                                    if (R.id.rd_cod == sel_method) {

                                        try {

                                            user_details = session.getUserDetails();

                                            data_bookNow.put("package_id", pack_id);
                                            data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                                            data_bookNow.put("payment_type", "1");

                                            Task_Payment task_payment1 = new Task_Payment();
                                            task_payment1.execute();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (R.id.rd_bank == sel_method) {

                                        try {

                                            user_details = session.getUserDetails();

                                            data_bookNow.put("package_id", pack_id);
                                            data_bookNow.put("user_id", user_details.get(SessionManager.user_Id));
                                            data_bookNow.put("payment_type", "3");

                                            Task_Payment task_payment1 = new Task_Payment();
                                            task_payment1.execute();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        Toast.makeText(getActivity(), "Visa Payment Service is Temporary Not available", Toast.LENGTH_LONG).show();
                                    }

                                } else {

                                    Intent i_login = new Intent(getActivity(), Login.class);
                                    i_login.putExtra("flag", 1);
                                    startActivity(i_login);

                                }


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();


            }
        });


        dialog.show();

    }


    private class Task_Payment extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata postdata = new Postdata();
                Log.i("request", data_bookNow.toString());
                String str_responce = postdata.post(Url_info.main_url + "package_booking.php", data_bookNow.toString());
                Log.i("responce", str_responce);
                JSONObject jobj_payment = new JSONObject(str_responce);
                status1 = jobj_payment.getString(status);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {

                dialog.dismiss();
                Toast.makeText(getActivity(), "Package Successfully Subscribed", Toast.LENGTH_SHORT).show();
                Task_package t_p = new Task_package();
                t_p.execute();
            } else {

                Toast.makeText(getActivity(), "You are Already Subscriber of this package", Toast.LENGTH_SHORT).show();
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


    private class Task_Unsubscribe extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata postdata = new Postdata();
                Log.i("request", data_bookNow.toString());
                String str_responce = postdata.post(Url_info.main_url + "package_unsubscribe.php", data_bookNow.toString());
                Log.i("responce", str_responce);
                JSONObject jobj_payment = new JSONObject(str_responce);
                status1 = jobj_payment.getString(status);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {

                dialog.dismiss();
                Toast.makeText(getActivity(), "Package Successfully Unsubscribed", Toast.LENGTH_SHORT).show();
                Task_package t_p = new Task_package();
                t_p.execute();

            } else {

                Toast.makeText(getActivity(), "You are Not Subscriber of this package", Toast.LENGTH_SHORT).show();
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


    private void navImageIcon() {

        ImageView imageView = view.findViewById(R.id.navIcon);
        TextView tv_title = view.findViewById(R.id.tv_nav_action_titile);
        String strName = getString(R.string.packages);
        tv_title.setText(strName);
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard(getActivity());
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
