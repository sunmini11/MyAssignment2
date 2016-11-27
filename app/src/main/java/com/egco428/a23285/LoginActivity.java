package com.egco428.a23285;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ArrayList<UserData> datalist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Sign-in Page");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Config.DATABASE_REF);
        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);

        retrieveUserPass();

        Button signinBtn = (Button) findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chkUserPass();
            }
        });

    }

    public void sendSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void clearEditText(View view) {
        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);
        username.setText("");
        password.setText("");
    }

    public void chkUserPass() {
        boolean check = false;
        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);
        for (int i = 0; i < datalist.size(); i++) {
            if (username.getText().toString().equals(datalist.get(i).getUsername()) &&
                    password.getText().toString().equals(datalist.get(i).getPassword())) {
                check = true;
            }
        }
        if (check) {
            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
        }
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

}
