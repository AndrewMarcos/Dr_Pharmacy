package pharmacy.morcos.andrew.drpharmacy.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import pharmacy.morcos.andrew.drpharmacy.Data.data_delevery;
import pharmacy.morcos.andrew.drpharmacy.R;

/**
 * Created by andre on 05-Mar-17.
 */

public class Delevery_Adapter extends BaseAdapter {

    ArrayList<data_delevery> list;
    Activity activity;
    LayoutInflater inflater;

    public Delevery_Adapter(ArrayList<data_delevery> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        inflater = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.delevery_list_item, null);

        TextView Sender = (TextView) convertView.findViewById(R.id.textNameDeleveryItem);
        TextView OrderNo = (TextView) convertView.findViewById(R.id.textOrdersNo);
        TextView Time = (TextView) convertView.findViewById(R.id.textTimeDeleveryItem);
        TextView New = (TextView) convertView.findViewById(R.id.textNewDeleveryItem);


        data_delevery dataDelvery = list.get(position);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        calendar.setTimeInMillis(Long.parseLong(dataDelvery.getTime()));

        Calendar current = Calendar.getInstance();

        int ycurrent = current.get(Calendar.YEAR);
        int mcurrent = current.get(Calendar.MONTH) + 1;
        int dcurrent = current.get(Calendar.DAY_OF_MONTH);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int min = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        int am_pm = calendar.get(Calendar.AM_PM);
        String time, a_p;

        if (ycurrent == year) {
            if (mcurrent == month) {
                if (dcurrent == day) {
                    if (am_pm == 0)
                        a_p = "am";
                    else
                        a_p = "pm";
                    if (min < 10) {
                        time = Integer.toString(hour) + ":0" + Integer.toString(min) + " " + a_p;
                    } else
                        time = Integer.toString(hour) + ":" + Integer.toString(min) + " " + a_p;
                } else if ((dcurrent - day) == 1) {
                    time = "YESTERDAY";
                } else
                    time = day + "/" + month + "/" + year;
            } else
                time = day + "/" + month + "/" + year;
        } else
            time = day + "/" + month + "/" + year;


        Sender.setText(dataDelvery.getSender());
        OrderNo.setText(dataDelvery.getOrderNo());
        switch (dataDelvery.getOrderNo()) {
            case "1":
                OrderNo.setTextColor(Color.parseColor("#3F51B5"));
                break;
            case "2":
                OrderNo.setTextColor(Color.parseColor("#CDDC39"));
                break;
            case "3":
                OrderNo.setTextColor(Color.parseColor("#FF9800"));
                break;
            case "4":
                OrderNo.setTextColor(Color.parseColor("#795548"));
                break;
            case "5":
                OrderNo.setTextColor(Color.parseColor("#607D8B"));
                break;
            case "6":
                OrderNo.setTextColor(Color.parseColor("#FFFF00"));
                break;
            default:
                OrderNo.setTextColor(Color.parseColor("#000000"));



        }
        Time.setText(time);

        if (dataDelvery.getSeen()) {
            New.setVisibility(View.INVISIBLE);
        } else
            Time.setTextColor(Color.parseColor("#CDDC39"));
        return convertView;
    }
}
