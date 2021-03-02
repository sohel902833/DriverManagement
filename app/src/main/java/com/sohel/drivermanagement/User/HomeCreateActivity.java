package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sohel.drivermanagement.LoginActivity;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.SpinnerCustomAdapter;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class HomeCreateActivity extends AppCompatActivity {

    private EditText homeNameEdittext;
    private Spinner floorSpinner,unitSpinner;

    private Button nextButton,doneButton;

    private  String[] floors={
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"
    }; private  String[] units={
            "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"
    };


    private SpinnerCustomAdapter floorAdapter;
    private SpinnerCustomAdapter unitsAdapter;

    String homeName,selectedFloor="",selectedUnit="";


    //<---------------Firebase-------------------->
    private DatabaseReference homeRef,floorRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_create);

        mAuth=FirebaseAuth.getInstance();
        homeRef=FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Home");
        floorRef=FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");
        progressDialog=new ProgressDialog(this);

        homeNameEdittext=findViewById(R.id.homeCreate_HomeNameEdittext);
        floorSpinner=findViewById(R.id.homeCreate_FloorSelectSpinnerid);
        unitSpinner=findViewById(R.id.homeCreate_UnitSelectSpinnerid);
        nextButton=findViewById(R.id.homeCreate_NextButtonid);
        doneButton=findViewById(R.id.homeCreate_DoneButtonid);

        floorAdapter=new SpinnerCustomAdapter(this,floors);
        unitsAdapter=new SpinnerCustomAdapter(this,units);
        floorSpinner.setAdapter(floorAdapter);
        unitSpinner.setAdapter(unitsAdapter);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getAllDataFormUser();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeActivity();
            }
        });
    }

    private void getAllDataFormUser() {


         homeName=homeNameEdittext.getText().toString();
        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFloor=floors[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUnit=units[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(homeName.isEmpty()){
            homeNameEdittext.setError("Enter Your Home Name");
            homeNameEdittext.requestFocus();
        }else if(selectedFloor.isEmpty()){
            Toasty.warning(HomeCreateActivity.this, "Please Select Floor", Toast.LENGTH_SHORT, true).show();
        }else if(selectedUnit.isEmpty()){
            Toasty.warning(HomeCreateActivity.this, "Please Select Unit", Toast.LENGTH_SHORT, true).show();
        }else{
         saveHomeToDatabase();
        }


    }

    private void saveHomeToDatabase() {
        progressDialog.setTitle("Saving Information..");
        progressDialog.show();

        HashMap<String,Object> homeMap=new HashMap<>();
        String homeId=homeRef.push().getKey()+System.currentTimeMillis();
        homeMap.put("homeName",homeName);
        homeMap.put("totalFloor",selectedFloor);
        homeMap.put("totalUnit",selectedUnit);
        homeMap.put("homeId",homeId);

        homeRef.child(homeId)
                .updateChildren(homeMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            for(int i=1; i<=Integer.parseInt(selectedFloor); i++){
                               HashMap<String,Object> floorMap=new HashMap<>();
                               floorMap.put("homeId",homeId);
                               floorMap.put("alotedPerson","none");
                               floorMap.put("aloted","false");
                               floorMap.put("floorName",String.valueOf(i));
                               String floorId=floorRef.push().getKey()+System.currentTimeMillis();
                               floorMap.put("floorId",floorId);
                               floorMap.put("alotedRenterId","none");
                                floorMap.put("waterBill","0");
                                floorMap.put("electricityBill","0");
                                floorMap.put("gasBill","0");
                                floorMap.put("utilitiesBill","0");
                                floorMap.put("rentAmount","0");
                                floorMap.put("waterIncluded","true");
                                floorMap.put("gasIncluded","true");
                                floorMap.put("utilitsIncluded","true");
                                floorMap.put("electricityIncluded","true");
                                floorMap.put("dueAmount","0");
                                floorRef.child(floorId)
                                       .updateChildren(floorMap)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){

                                                   homeNameEdittext.setText("");


                                                   progressDialog.dismiss();
                                                   Toasty.success(HomeCreateActivity.this, "Home Create Success", Toast.LENGTH_SHORT, true).show();

                                               } else{
                                                   progressDialog.dismiss();
                                                   Toasty.warning(HomeCreateActivity.this, "Home Create Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                                               }
                                           }
                                       });
                            }

                        } else{
                            progressDialog.dismiss();
                            Toasty.warning(HomeCreateActivity.this, "Home Create Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void sendUserToHomeActivity() {
        Intent intent=new Intent(HomeCreateActivity.this,HomeListActivity.class);
        startActivity(intent);
    }
}