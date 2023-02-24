package com.example.altbeacon;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;


public class MainListAdapter extends ArrayAdapter<Beacon> {

    private final Activity context;
    private final ArrayList<Beacon> beacon;

    public MainListAdapter(Activity context, ArrayList<Beacon> beacon) {
        super(context, R.layout.listbox_layout, beacon);
        this.context = context;
        this.beacon  = beacon;
    }
    @Override
    public int getCount() {
        return beacon .size();
    }

    @Override
    public Beacon getItem(int position) {
        return beacon .get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(Beacon item) {
        return beacon .indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void copyBeacons(Collection<Beacon> beacons)
    {
        beacon.clear();
        for (Beacon b : beacons)
        {
            beacon.add(b);
        }
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listbox_layout, null,true);

        if(beacon.size() > position) {
            TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
            firstLine.setText("UUID : "+beacon.get(position).getId1());

            TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
            secondLine.setText("Major: " + beacon.get(position).getId2() + "  Minor: " + beacon.get(position).getId3());



            TextView thirdLine = (TextView) rowView.findViewById(R.id.thirdLine);
            thirdLine.setText("Distance : " + beacon.get(position).getDistance() + " meters");

            TextView rrsi = (TextView) rowView.findViewById(R.id.rrsi);
            rrsi.setText("RRSI\n" +beacon.get(position).getRssi() );


        }
        return rowView;
    };
}
