package com.sohel.drivermanagement.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sohel.drivermanagement.MainActivity;
import com.sohel.drivermanagement.R;

public class StartActivity extends AppCompatActivity {
    private Button homeCreateButton;
    private TextView seeVideoTutorialTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        homeCreateButton=findViewById(R.id.start_EnterAllRentButton);
        seeVideoTutorialTextview=findViewById(R.id.videoTutorialLink);
        homeCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeCreateButton();
            }
        });


        seeVideoTutorialTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void sendUserToHomeCreateButton() {
        Intent intent=new Intent(StartActivity.this, HomeCreateActivity.class);
        startActivity(intent);
    }
}