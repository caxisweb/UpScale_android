package sa.upscale.coworking.Notifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sa.upscale.coworking.NavigationActivity;
import sa.upscale.coworking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends Fragment {


    public Notification() {
        // Required empty public constructor
    }

View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_notification, container, false);
        NavigationActivity.backFlag = 1;
        navImageIcon();
        return view;

    }

    private void navImageIcon() {

        ImageView imageView = (ImageView) view.findViewById(R.id.navIcon);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_nav_action_titile);
        String strName = getString(R.string.notifications);
        tv_title.setText(strName);
        final NavigationActivity navigationActivity = (NavigationActivity) getActivity();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationActivity.drawerLayout.openDrawer(navigationActivity.navView, true);
            }
        });
    }


}
