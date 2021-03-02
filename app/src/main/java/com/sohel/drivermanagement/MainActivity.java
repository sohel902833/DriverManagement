package com.sohel.drivermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.sohel.drivermanagement.User.HomeCreateActivity;
import com.sohel.drivermanagement.User.ProductCategoryActivity;

public class MainActivity extends AppCompatActivity {

    private Button homeCreateButton;
    private TextView seeVideoTutorialTextview;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    private void sendUserToHomeCreateButton() {
        Intent intent=new Intent(MainActivity.this, HomeCreateActivity.class);
        startActivity(intent);
    }
}