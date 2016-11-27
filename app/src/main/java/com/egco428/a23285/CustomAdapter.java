package com.egco428.a23285;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<UserData> {
    Context context;
    List<UserData> objects;
    TextView txt;

    public CustomAdapter(Context context, int resource, List<UserData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserData userdata = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE); //link with interface
        View view = inflater.inflate(R.layout.listview_row, null);

        txt = (TextView) view.findViewById(R.id.userListview);
        txt.setText(userdata.getUsername());
        return view;
    }
}
