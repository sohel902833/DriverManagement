package com.sohel.drivermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sohel.drivermanagement.Admin.AdminUtilitiesCategory;

public class AdminMainActivity extends AppCompatActivity {
  private Button utilitiesButton;
  private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        toolbar=findViewById(R.id.admin_Main_Toolbarid);
        setSupportActionBar(toolbar);


        utilitiesButton=findViewById(R.id.admin_main_UtilitiesButtonid);



        utilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToUtilitiesCategoryActivity();
            }
        });



    }

    private void sendUserToUtilitiesCategoryActivity() {
        Intent intent=new Intent(AdminMainActivity.this, AdminUtilitiesCategory.class);
        startActivity(intent);


    }
}