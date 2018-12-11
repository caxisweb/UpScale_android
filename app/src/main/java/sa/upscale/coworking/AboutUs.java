package sa.upscale.coworking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;




/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment {


    public AboutUs() {
        // Required empty public constructor
    }

    View view;
    TextView tv_p1, tv_p2, tv_p3, tv_p4, tv_p5, tv_p6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        NavigationActivity.backFlag = 1;

        navImageIcon();

        tv_p1 = (TextView) view.findViewById(R.id.tv_aboutUs_p_1);
        tv_p2 = (TextView) view.findViewById(R.id.tv_aboutUs_p_2);
        tv_p3 = (TextView) view.findViewById(R.id.tv_aboutUs_p_3);
        tv_p4 = (TextView) view.findViewById(R.id.tv_aboutUs_p_4);
        tv_p5 = (TextView) view.findViewById(R.id.tv_aboutUs_p_5);
        tv_p6 = (TextView) view.findViewById(R.id.tv_aboutUs_p_6);

        tv_p1.setText(getString(R.string.aboutus_text1));
        tv_p2.setText(getString(R.string.aboutus_text2));
        tv_p3.setText(getString(R.string.aboutus_text3));
        tv_p4.setText(getString(R.string.aboutus_text4));
        tv_p5.setText(getString(R.string.aboutus_text5));
        tv_p6.setText(getString(R.string.aboutus_text6));

        return view;
    }

    private void navImageIcon() {

        ImageView imageView = (ImageView) view.findViewById(R.id.navIcon);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_nav_action_titile);
        tv_title.setText(getString(R.string.about_us));
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }

}
