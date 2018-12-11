package sa.upscale.coworking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import sa.upscale.coworking.R;
import sa.upscale.coworking.model.Package;

/**
 * Created by DELL on 21-02-2018.
 */

public class Packageadepter extends ArrayAdapter<String> {

    ArrayList<Package> package_list = new ArrayList<>();
    Context mContext;

    public Packageadepter(Context context, ArrayList<Package> package_list){
        super(context, R.layout.custom_package_view);

        this.mContext = context;
        this.package_list = package_list;
    }

    @Override
    public int getCount() {
        return package_list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_package_view, null);
        }

        TextView tv_pname= (TextView) v.findViewById(R.id.tv_package_name);
        TextView tv_ptype= (TextView) v.findViewById(R.id.tv_package_type);
        TextView tv_pprice= (TextView) v.findViewById(R.id.tv_package_price);
        TextView tv_is_subscriber= (TextView) v.findViewById(R.id.tv_is_subscribe);
        TextView tv_company= (TextView) v.findViewById(R.id.tv_company);
        //RatingBar prat= (RatingBar) v.findViewById(R.id.ret_package);

        tv_pname.setText(package_list.get(position).getPack_name());
        tv_pprice.setText(package_list.get(position).getPack_price()+" SAR");
        tv_ptype.setText(package_list.get(position).getPack_type());
        tv_company.setText(package_list.get(position).getStr_company_name());

        if(package_list.get(position).getStr_subscribe_status().equals("1")){
            tv_is_subscriber.setText("Subscribed");
            tv_is_subscriber.setVisibility(View.VISIBLE);
        }else{
            tv_is_subscriber.setVisibility(View.GONE);
        }

        return v;
    }
}
