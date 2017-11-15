package tokyproduction.volleystats;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Marco on 01.06.2017.
 */

class CustomAdapter extends BaseAdapter{

    ArrayList<Punkt> points;
    Context context;


    public CustomAdapter(Context context,ArrayList<Punkt> punkte) {
        this.context =context;
        this.points =punkte;
    }

    @Override
    public int getCount() {
        return points.size();
    }

    @Override
    public Object getItem(int position) {
        return points.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.custom_row,parent,false);

        Punkt actualPoint = points.get(position);

        String strPoint = actualPoint.getGrund();
        String strtime  =actualPoint.getTs();
        String strTeam =actualPoint.getTeam();
        String strPlayer = actualPoint.getSpieler();


        TextView team = (TextView) rowView.findViewById(R.id.colTeam);
        TextView time = (TextView) rowView.findViewById(R.id.colTime);
        TextView cause = (TextView) rowView.findViewById(R.id.colCause);
        TextView point = (TextView) rowView.findViewById(R.id.colPoint);
        team.setText(strTeam);
        time.setText(strtime);
        cause.setText(strPoint);
        point.setText(strPlayer);


        return rowView;

    }
}
