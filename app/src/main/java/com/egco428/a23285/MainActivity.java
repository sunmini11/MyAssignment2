package com.egco428.a23285;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<UserData> adapter;
    private ArrayList<UserData> datalist = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button
        setTitle("Main Page");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Config.DATABASE_REF);

        retrieveUserPass();

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(this, 0, datalist);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final UserData userData = (UserData) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("latitude", userData.getLat());
                intent.putExtra("longitude", userData.getLon());
                intent.putExtra("username", userData.getUsername());
                startActivity(intent);
            }
        });
    }

    public void retrieveUserPass() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                datalist.add(userData);
                Log.e("size ", "" + datalist.size() + "");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); // where dialog appear
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            // when ans = yes do this
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
        return super.onOptionsItemSelected(item);
    }
}
