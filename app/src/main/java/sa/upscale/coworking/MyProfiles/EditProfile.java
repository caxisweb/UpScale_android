package sa.upscale.coworking.MyProfiles;

import android.Manifest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.Postdata;
import sa.upscale.coworking.R;
import sa.upscale.coworking.SessionManager;
import sa.upscale.coworking.Url_info;
import sa.upscale.coworking.fregment.Home_freg;

import static android.app.Activity.RESULT_OK;

public class EditProfile extends Fragment {


    private static final String status = "status";
    private static final String message = "message";
    String status1 = "0", message1 = "try Again";

    JSONObject data_fatchProfile = new JSONObject();
    JSONObject data_updateProfile = new JSONObject();
    SessionManager sessionManager;
    HashMap<String, String> user_details = new HashMap<>();

    private CircleImageView imgProfile;
    private Spinner spProfileCity, sp_expertise;
    private EditText edProfileName, edProfileEmailId, edProfileMobileNo, edProfileNewPassword, edProfileRePassword;
    private String str_ProfileName, str_city = "0", str_expertise = "0", str_ProfileEmailId, str_ProfileMobileNo, str_ProfileNewPassword, str_ProfileRePassword;
    private Button btn_save;

    Uri selectedImage;
    String selectedImage1 = "", picpath_identify;

    String ar_expertise[] = {"Select", "IT", "HR ", " Finance", "Marketing ", "Legal", " Sales", " Business ", "Startup", "Digital Media", "Branding & UX ", "Other"};
    String ar_city[] = {"Select", "Riyadh", "Jeddah", "Dammam", "Makkah", "Medina", "Qassim"};

    String mstr_name, mstr_phone, mstr_email, mstr_userType, mstr_userCity, mstr_img;
    android.support.v4.app.FragmentTransaction ft;


    public EditProfile() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_edit_profile, container, false);
        NavigationActivity.backFlag = 1;

        navImageIcon();

        sessionManager = new SessionManager(getActivity());
        user_details = sessionManager.getUserDetails();

        //custActionbar();

        findViews();

        /*ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ar_expertise);
        sp_expertise.setAdapter(arrayAdapter);

        ArrayAdapter arrayAdapter_city = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ar_city);
        spProfileCity.setAdapter(arrayAdapter_city);

        Task_getProfileDetails profileDetails = new Task_getProfileDetails();
        profileDetails.execute();*/

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_ProfileName = edProfileName.getText().toString().trim();
                str_ProfileEmailId = edProfileEmailId.getText().toString().trim();
                str_ProfileMobileNo = edProfileMobileNo.getText().toString().trim();
//                str_ProfileNewPassword = edProfileNewPassword.getText().toString().trim();
//                str_ProfileRePassword = edProfileRePassword.getText().toString().trim();
//                str_expertise = sp_expertise.getSelectedItem().toString();
//                str_city = spProfileCity.getSelectedItem().toString();

/*                if (str_ProfileNewPassword.length() != 0) {
                    if (str_ProfileRePassword.length() == 0) {
                        Toast.makeText(getActivity(), "Please Enter RePassword", Toast.LENGTH_SHORT).show();
                    } else if (!str_ProfileNewPassword.equals(str_ProfileRePassword)) {
                        //Toast.makeText(EditProfile.this, "Enter Password missMatch", Toast.LENGTH_SHORT).show();
                    }
                } else {



                if (str_expertise.equals("Select")) {
                    str_expertise = "";
                    Toast.makeText(getActivity(), "Please select your Expertise", Toast.LENGTH_SHORT).show();
                } else if (str_city.equals("Select")) {
                    str_city = "";
                    Toast.makeText(getActivity(), "Please select your City", Toast.LENGTH_SHORT).show();
                } else {*/

                String str = String.valueOf(str_ProfileMobileNo.startsWith("05"));

                if (str_ProfileName.length() == 0) {
                    edProfileName.setError("Please Enter Name");
                    edProfileName.setFocusable(true);
                } else if (str_ProfileMobileNo.length() == 0) {
                    edProfileMobileNo.setError("Please Enter Contact No.");
                    edProfileMobileNo.setFocusable(true);
                } else if (str.equals("false")) {
                    edProfileMobileNo.setError("Number Start With 05");
                    edProfileMobileNo.setFocusable(true);
                    edProfileMobileNo.requestFocus();
                } else if (str_ProfileMobileNo.length() != 10) {
                    edProfileMobileNo.setError("Please Enter Correct Number");
                    edProfileMobileNo.setFocusable(true);
                } else if (str_ProfileEmailId.length() == 0) {
                    edProfileMobileNo.setError("Please Enter Email Id");
                    edProfileMobileNo.setFocusable(true);
                } else if (!str_ProfileEmailId.matches("[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
                    edProfileEmailId.setError("Invalid Email Address");
                    edProfileEmailId.setFocusable(true);
                } else {


                    if (isNetworkAvailable()) {
                        try {

                            data_updateProfile.put("user_id", user_details.get(SessionManager.user_Id));
                            data_updateProfile.put("name", str_ProfileName);
                            data_updateProfile.put("phone", str_ProfileMobileNo);
                            data_updateProfile.put("email", str_ProfileEmailId);
                            data_updateProfile.put("new_pass", str_ProfileNewPassword);
                            data_updateProfile.put("re_pass", str_ProfileRePassword);
                            data_updateProfile.put("user_type", str_expertise);
                            data_updateProfile.put("city", str_city);

                            Task_updateProfiles updateProfiles = new Task_updateProfiles();
                            updateProfiles.execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Switch on your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(),

                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        selectImage();

                    } else {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"}, 200);
                        // No explanation needed, we can request the permission.
                    }
                } else {
                    selectImage();
                }

            }

        });

        return view;

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

    private void selectImage() {
        {

            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals("Take Photo")) {

                        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                        File newdir = new File(dir);
                        newdir.mkdirs();

                        String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";

                        File newfile = new File(file);
                        try {
                            newfile.createNewFile();
                        } catch (IOException ignored) {

                        }

                        selectedImage = Uri.fromFile(newfile);

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                        startActivityForResult(cameraIntent, 1);

                    } else if (options[item].equals("Choose from Gallery")) {

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);


                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {

                    if (selectedImage != null) {
                        if (selectedImage.toString().startsWith("file:")) {

                            selectedImage1 = selectedImage.getPath();

                            InputStream in = null;
                            try {
                                final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
                                in = getActivity().getContentResolver().openInputStream(selectedImage);
                                // Decode image size
                                BitmapFactory.Options o = new BitmapFactory.Options();
                                o.inJustDecodeBounds = true;
                                BitmapFactory.decodeStream(in, null, o);
                                try {
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                int scale = 1;
                                while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                                        IMAGE_MAX_SIZE) {
                                    scale++;
                                }
                                Bitmap b = null;
                                in = getActivity().getContentResolver().openInputStream(selectedImage);
                                if (scale > 1) {
                                    scale--;
                                    // scale to max possible inSampleSize that still yields an image
                                    // larger than target
                                    o = new BitmapFactory.Options();
                                    o.inSampleSize = scale;
                                    b = BitmapFactory.decodeStream(in, null, o);

                                    // resize to desired dimensions
                                    int height = b.getHeight();
                                    int width = b.getWidth();

                                    double y = Math.sqrt(IMAGE_MAX_SIZE
                                            / (((double) width) / height));
                                    double x = (y / height) * width;

                                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                                            (int) y, true);
                                    b.recycle();
                                    b = scaledBitmap;

                                    System.gc();
                                } else {
                                    b = BitmapFactory.decodeStream(in);
                                }
                                in.close();
                                /*bitmapArrayListGallery.add(b);
                                view_imageAdapter.notifyDataSetChanged();*/
                                imgProfile.setImageBitmap(b);
                            } catch (Exception ignored) {

                            }

                            // ar_imgGallery.add(selectedImage1);

                        } else { // uri.startsWith("content:")

                            Cursor c = getActivity().getContentResolver().query(selectedImage, null,
                                    null, null, null);

                            if (c != null && c.moveToFirst()) {

                                int id = c.getColumnIndex(MediaStore.Images.Media.DATA);
                                if (id != -1) {
                                    selectedImage1 = c.getString(id);
                                    //ar_imgGallery.add(selectedImage1);
                                    Log.d("TAG", "selectedImage  === " + selectedImage1);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.i("path of image from galle", picturePath+"");
                Log.i("IMage Path", picturePath + "");
                selectedImage1 = picturePath;
                imgProfile.setImageBitmap(thumbnail);
                /*lv_list.addView(img_pic);
                ar_imgPanorama.add(picturePath);*/
            }
        }
    }


    private void findViews() {

        sp_expertise = (Spinner) view.findViewById(R.id.sp_profile_expertise);

        imgProfile = (CircleImageView) view.findViewById(R.id.img_profile);
        edProfileName = (EditText) view.findViewById(R.id.ed_profile_name);
        edProfileEmailId = (EditText) view.findViewById(R.id.ed_profile_emailId);
        edProfileMobileNo = (EditText) view.findViewById(R.id.ed_profile_mobileNo);
        spProfileCity = (Spinner) view.findViewById(R.id.sp_profile_city);
        edProfileNewPassword = (EditText) view.findViewById(R.id.ed_profile_newPassword);
        edProfileRePassword = (EditText) view.findViewById(R.id.ed_profile_rePassword);
        btn_save = (Button) view.findViewById(R.id.btn_save);

        edProfileName.setText(user_details.get(SessionManager.user_name));
        edProfileEmailId.setText(user_details.get(SessionManager.user_email));
        edProfileMobileNo.setText(user_details.get(SessionManager.user_mobile));

        picpath_identify = user_details.get(SessionManager.user_profile).substring(0, 5);

        if (picpath_identify.equals("https")) {
            Picasso.with(getActivity()).load(user_details.get(SessionManager.user_profile)).placeholder(R.drawable.logo1).error(R.drawable.logo1).into(imgProfile);
        } else {
            Picasso.with(getActivity()).load(Url_info.main_img + "profile/" + user_details.get(SessionManager.user_profile)).placeholder(R.drawable.logo1).error(R.drawable.logo1).into(imgProfile);
        }
    }


   /* private void custActionbar() {

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        TextView tv_title = (TextView) view.findViewById(R.id.action_bar_title);
        String strName = getString(R.string.edit_profile);
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
*/

    private class Task_getProfileDetails extends AsyncTask<String, String, String> {

        String mstr_name, mstr_phone, mstr_email, mstr_userType, mstr_userCity, mstr_img;
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
                Log.d("responce", str_dettails);
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {

                    mstr_name = job_details.getString("name");
                    mstr_phone = job_details.getString("phone");

                    mstr_email = job_details.getString("email");
                    mstr_img = job_details.getString("image");
                    mstr_userType = job_details.getString("user_type");
                    mstr_userCity = job_details.getString("user_city");


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
                    edProfileName.setText(mstr_name);
                    if (mstr_phone == null || mstr_phone.length() == 4) {

                    } else {
                        edProfileMobileNo.setText(mstr_phone);
                    }

                    //edProfileUsertype.setText(mstr_userType);
                    edProfileEmailId.setText(mstr_email);

                    picpath_identify = mstr_img.substring(0, 5);
                    if (picpath_identify.equals("https")) {
                        Picasso.with(getActivity()).load(mstr_img).placeholder(R.drawable.logo3).into(imgProfile);
                    } else {
                        Picasso.with(getActivity()).load(Url_info.main_img + "profile/" + mstr_img).placeholder(R.drawable.logo3).into(imgProfile);
                    }

                    String compareValue = mstr_userType;
                    Log.d("expertise", compareValue);
                    if (compareValue.length() == 0 || compareValue == null) {
                        compareValue = "Select";
                        Log.d("expertise", "11" + compareValue);
                    }
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_list_item_1, ar_expertise);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    sp_expertise.setAdapter(adapter);

                    if (!compareValue.equals(null)) {
                        int spinnerPosition = adapter.getPosition(compareValue);
                        sp_expertise.setSelection(spinnerPosition);
                    }


                    String compareValue_city = mstr_userCity;
                    Log.d("city", compareValue_city);
                    if (compareValue_city.length() == 0 || compareValue_city == null) {
                        compareValue_city = "Select";
                        Log.d("city", "11" + compareValue_city);
                    }

                    ArrayAdapter<CharSequence> adapter_city = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_list_item_1, ar_city);
                    adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spProfileCity.setAdapter(adapter_city);


                    if (!compareValue_city.equals(null)) {
                        int spinnerPosition1 = adapter_city.getPosition(compareValue_city);
                        spProfileCity.setSelection(spinnerPosition1);
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

    private class Task_updateProfiles extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {

                Postdata postdata = new Postdata();
                String str_dettails = postdata.post(Url_info.main_url + "update_profile.php", data_updateProfile.toString());
                JSONObject job_details = new JSONObject(str_dettails);
                status1 = job_details.getString(status);

                if (status1.equals("1")) {

                    if (selectedImage1.equals("")) {

                    } else {

                        File sourceFile = new File(selectedImage1);
                        Imageupload img = new Imageupload();
                        String userID = user_details.get(SessionManager.user_Id);
                        status1 = img.post(Url_info.main_url + "update_profile_image.php", sourceFile, userID);
                        Log.d("status", status1);
                        JSONObject job_img = new JSONObject(status1);
                        status1 = job_img.getString(status);

                    }

                    data_fatchProfile.put("user_id", user_details.get(SessionManager.user_Id));

                    Postdata postdata2 = new Postdata();
                    //Log.d("Profile", data_fatchProfile.toString());
                    String str_dettails2 = postdata2.post(Url_info.main_url + "my_profile.php", data_fatchProfile.toString());
                    //Log.d("responce", str_dettails);
                    JSONObject job_details2 = new JSONObject(str_dettails2);
                    status1 = job_details2.getString(status);

                    if (status1.equals("1")) {

                        mstr_name = job_details2.getString("name");
                        mstr_phone = job_details2.getString("phone");

                        mstr_email = job_details2.getString("email");
                        mstr_img = job_details2.getString("image");
                        mstr_userType = job_details2.getString("user_type");
                        mstr_userCity = job_details2.getString("user_city");


                    } else {
                        message1 = job_details.getString(message);
                    }


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

                sessionManager.clearUser();
                sessionManager.createLoginSession(user_details.get(SessionManager.user_Id), mstr_name, mstr_email, mstr_phone, mstr_img, user_details.get(SessionManager.user_languageCode), user_details.get(SessionManager.user_essal_cust_id));

                user_details.clear();
                user_details = sessionManager.getUserDetails();

                edProfileName.setText(user_details.get(SessionManager.user_name));
                edProfileEmailId.setText(user_details.get(SessionManager.user_email));
                edProfileMobileNo.setText(user_details.get(SessionManager.user_mobile));

                picpath_identify = user_details.get(SessionManager.user_profile).substring(0, 5);

                if (picpath_identify.equals("https")) {
                    Picasso.with(getActivity()).load(user_details.get(SessionManager.user_profile)).placeholder(R.drawable.logo1).into(imgProfile);
                } else {
                    Picasso.with(getActivity()).load(Url_info.main_img + "profile/" + user_details.get(SessionManager.user_profile)).placeholder(R.drawable.logo1).into(imgProfile);
                }

                Toast.makeText(getActivity(), "" + "Profile Update Successful", Toast.LENGTH_SHORT).show();

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

    /*private class Task_imageUpload extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {

            try {


                File sourceFile = new File(selectedImage1);
                Imageupload img = new Imageupload();
                String userID = user_details.get(SessionManager.user_Id);
                status1 = img.post(Url_info.main_url + "update_profile_image.php", sourceFile, userID);
                Log.d("status", status1);
                JSONObject job_img = new JSONObject(status1);
                status1 = job_img.getString(status);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (status1.equals("1")) {
                Log.d("imageUpload", "successfully");
                // Toast.makeText(getApplicationContext(), "Successfully Submit!!!", Toast.LENGTH_LONG).show();

                String userId=user_details.get(SessionManager.user_Id);
                String userName=user_details.get(SessionManager.user_name);
                String email=user_details.get(SessionManager.user_email);
                String lanstatus=user_details.get(SessionManager.user_langStatus);
                String lanCode=user_details.get(SessionManager.user_languageCode);
                String mobile=user_details.get(SessionManager.user_mobile);
                String profile=user_details.get(SessionManager.user_profile);

                sessionManager.clearUser();
                sessionManager.createLoginSession(userId,userName,email,mobile,profile,lanCode);



                //  finish();
                startActivity(new Intent(getActivity(), NavigationActivity.class));


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

    }*/


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}





