package com.egco428.a23285;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class SignupActivity extends AppCompatActivity implements SensorEventListener {
    EditText username;
    EditText password;
    EditText lat;
    EditText lon;
    private SensorManager sensorManager;
    private long lastUpdate;
    int count = 0;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<UserData> datalist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set back button
        setTitle("Sign-up Page");

        datalist = new ArrayList();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Config.DATABASE_REF);

        lat = (EditText) findViewById(R.id.latiText);
        lon = (EditText) findViewById(R.id.longiText);
        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

        Button randomBtn = (Button) findViewById(R.id.randomBtn);
        randomBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                randLatLon();
            }
        });

        retrieveUserPass();

        Button addNewUserBtn = (Button) findViewById(R.id.addNewUserBtn);
        addNewUserBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (lat.getText().toString().equals("") || lon.getText().toString().equals("") || username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "Please fill in the blank", Toast.LENGTH_SHORT).show();
                } else {
                    boolean check = false;
                    for (int i = 0; i < datalist.size(); i++) {
                        if (username.getText().toString().equals(datalist.get(i).getUsername())) {
                            check = true;
                        }
                    }
                    if (check) {
                        Toast.makeText(SignupActivity.this, "Please change your username", Toast.LENGTH_SHORT).show();
                    } else {
                        String getUsername = username.getText().toString();
                        String getPassword = password.getText().toString();
                        double getLat = Double.parseDouble(lat.getText().toString());
                        double getLon = Double.parseDouble(lon.getText().toString());
                        UserData userData = new UserData(getUsername, getPassword, getLat, getLon);
                        myRef.push().setValue(userData);
                        finish();

                    }
                }
            }
        });
    }

    protected void randLatLon() {
        double minLat = -85.000000;
        double maxLat = 85.000000;
        double latitude = minLat + (double) (Math.random() * ((maxLat - minLat) + 1));
        double minLon = -179.999989;
        double maxLon = 179.999989;
        double longitude = minLon + (double) (Math.random() * ((maxLon - minLon) + 1));
        DecimalFormat df = new DecimalFormat("#.######"); //Convert 0.000000.. to 0.000000
        lat.setText(df.format(latitude));
        lon.setText(df.format(longitude));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelerationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelerationSquareRoot >= 2) {
            if (count >= 2) {
                randLatLon();
            }
        }
        if (actualTime - lastUpdate < 700) {
            return;
        }
        lastUpdate = actualTime;
        count++;
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}
