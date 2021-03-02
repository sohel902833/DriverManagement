package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.LoginActivity;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.FloorList;
import com.sohel.drivermanagement.User.DataModuler.FloorList2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FloorAloteActivity extends AppCompatActivity {
    private TextView floorAloteDetailsTextview;
    private EditText renterNameEdittext,renterPhoneNumberEdittext,advanceEdittext,rentAmountEdittext;
    private EditText waterBillEdittext,gasBillEdittext,electricityBillEditext,utilitesBillEdittext;
    private RadioGroup waterRadioGroup,gasRadioGroup,electricityRadioGroup,utilitiesRadioGroup;
    private TextView renterDateTextview,paidDateTextview;



    private String renterName,renterPhoneNumber="",renterDue="0",renterAdvance="",rentAmount="0",waterBill="0",gasBill="0",electricityBill="0",utilitesBill="0";
    private Button saveAloteButton;
    private CheckBox alarmCheckBox;

    String homeName,floorName,floorId,homeId;

    boolean gasEx=true,waterEx=true,electriciyEx=true,utilitsEx=true;

    private DatabaseReference renterRef,floorRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    DatePickerDialog.OnDateSetListener setListener;

    private  int todayDate,todayMonth,todayYear;
    private  int renterDate,renterMonth,renterYear;
    private  int alarmDate,alarmMonth,alarmYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_alote);


        progressDialog=new ProgressDialog(this);


         homeName=getIntent().getStringExtra("homeName");
         homeId=getIntent().getStringExtra("homeId");
         floorName=getIntent().getStringExtra("floorName");
         floorId=getIntent().getStringExtra("floorId");


       // saveAloteButton.setText("Save And Alote Floor "+(floorCounter+2));


        mAuth=FirebaseAuth.getInstance();
         floorRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");
         renterRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Renter");

          init();
        floorAloteDetailsTextview.setText("House Name : "+homeName+"\n Floor "+floorName+".  Alote To");
       saveAloteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int waterSelectedId = waterRadioGroup.getCheckedRadioButtonId();
                int utilitSelectedId = utilitiesRadioGroup.getCheckedRadioButtonId();
                int electricitySelectedId=electricityRadioGroup.getCheckedRadioButtonId();
                int gasSelectedId=gasRadioGroup.getCheckedRadioButtonId();
                RadioButton waterRadioButton = (RadioButton) findViewById(waterSelectedId);
                RadioButton electricityRadioButton = (RadioButton) findViewById(electricitySelectedId);
                RadioButton gasRadioButton = (RadioButton) findViewById(gasSelectedId);
                RadioButton utilitiRadioButton = (RadioButton) findViewById(utilitSelectedId);

                waterBill=waterBillEdittext.getText().toString();
                gasBill=gasBillEdittext.getText().toString();
                electricityBill=electricityBillEditext.getText().toString();
                utilitesBill=utilitesBillEdittext.getText().toString();

                waterEx=isIncluded(waterRadioButton.getText().toString());
                gasEx=isIncluded(gasRadioButton.getText().toString());
                electriciyEx=isIncluded(electricityRadioButton.getText().toString());
                utilitsEx=isIncluded(utilitiRadioButton.getText().toString());
                renterName=renterNameEdittext.getText().toString();
                renterPhoneNumber=renterPhoneNumberEdittext.getText().toString();
                renterAdvance=advanceEdittext.getText().toString();
                rentAmount=rentAmountEdittext.getText().toString();
                validate();
            }
        });


       renterDateTextview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatePickerDialog datePickerDialog=new DatePickerDialog(
                       FloorAloteActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int day) {
                       renterDate=day;
                       renterYear=year;
                       renterMonth=month+1;
                       renterDateTextview.setText(renterDate+"/"+renterMonth+"/"+renterYear);
                   }
               },todayYear,todayMonth,todayDate
               );
               datePickerDialog.show();
           }
       });
       paidDateTextview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatePickerDialog datePickerDialog=new DatePickerDialog(
                       FloorAloteActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int day) {
                       alarmDate=day;
                       alarmYear=year;
                       alarmMonth=month+1;
                       paidDateTextview.setText(alarmDate+"/"+alarmMonth+"/"+alarmYear);
                   }
               },todayYear,todayMonth,todayDate
               );
               datePickerDialog.show();
           }
       });






    }

    private void validate() {

        if(renterName.isEmpty()){
            renterNameEdittext.setError("Write Renter Name");
            renterNameEdittext.requestFocus();
        }else if(rentAmount.isEmpty()){
            rentAmountEdittext.setError("Please Write Rent amount");
            rentAmountEdittext.requestFocus();
        }else{
            saveRenterDetails();
        }
    }

    private void saveRenterDetails() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving Your Renter Details");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String renterUserId=renterRef.push().getKey()+System.currentTimeMillis();
        HashMap<String,Object> renterMap=new HashMap<>();
        renterMap.put("renterId",renterUserId);
        renterMap.put("homeId",homeId);
        renterMap.put("name",renterName);
        renterMap.put("phone",renterPhoneNumber);
        renterMap.put("floorId",floorId);
        renterMap.put("advanced",renterAdvance);

        if(!renterAdvance.equals("")){
            renterDue=String.valueOf(Integer.parseInt(rentAmount)-Integer.parseInt(renterAdvance));
        }


        HashMap<String,Object> floorMap=new HashMap<>();
        floorMap.put("alotedPerson",renterName);
        floorMap.put("alotedRenterId",renterUserId);
        floorMap.put("waterBill",waterBill);
        floorMap.put("electricityBill",electricityBill);
        floorMap.put("gasBill",gasBill);
        floorMap.put("utilitiesBill",utilitesBill);
        floorMap.put("rentAmount",rentAmount);
        floorMap.put("waterIncluded",waterEx);
        floorMap.put("aloted","true");
        floorMap.put("gasIncluded",gasEx);
        floorMap.put("utilitsIncluded",utilitsEx);
        floorMap.put("electricityIncluded",electriciyEx);
        floorMap.put("dueAmount",renterDue);
        renterRef.child(renterUserId)
                .setValue(renterMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                floorRef.child(floorId)
                                        .updateChildren(floorMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                               progressDialog.dismiss();
                                                Toasty.success(FloorAloteActivity.this, "Floor Aloted Success!", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }
                            });
                        }else{
                            progressDialog.dismiss();
                            Toasty.warning(FloorAloteActivity.this, "Floor Alote Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                        }
                        }
                    });
    }


    public boolean isIncluded(String value){
        boolean check=true;
       if(value.equals("Included")) {
           check=true;
       }
        else if(value.equals("Excluded")){
           check= false;
       }
        return  check;
    }
    private void init() {
        floorAloteDetailsTextview=findViewById(R.id.floorAlote_DetailsTextviewid);
        renterNameEdittext=findViewById(R.id.floorAlote_RenterNameEdittextid);
        renterPhoneNumberEdittext=findViewById(R.id.floatAlote_RenterPhoneNumberid);
        advanceEdittext=findViewById(R.id.floorAlote_AdvanceEdittextied);
        rentAmountEdittext=findViewById(R.id.floorAlote_RentAmountEdittextied);
        waterBillEdittext=findViewById(R.id.floorAlote_WaterEdittextid);
        gasBillEdittext=findViewById(R.id.floorAlote_GasEdittextid);
        electricityBillEditext=findViewById(R.id.floorAlote_ElectricityEdittextid);
        utilitesBillEdittext=findViewById(R.id.floorAlote_UtilitsEdittextid);
        saveAloteButton=findViewById(R.id.saveAloteButton);


        renterDateTextview=findViewById(R.id.floorAlote_renterDateTextviewid);
        paidDateTextview=findViewById(R.id.floorAlote_RentPaidLimitDateTextView);
        alarmCheckBox=findViewById(R.id.alarmCheckSwitchId);

        Calendar calendar=Calendar.getInstance();
         todayYear=calendar.get(Calendar.YEAR);
        todayMonth=calendar.get(Calendar.MONTH);
        todayDate=calendar.get(Calendar.DAY_OF_MONTH);

         renterDateTextview.setText(todayDate+"/"+(todayMonth+1)+"/"+todayYear);

        waterRadioGroup=findViewById(R.id.water_RadioGroup);
        electricityRadioGroup=findViewById(R.id.electriciy_RadioGroup);
        gasRadioGroup=findViewById(R.id.gas_RadioGroup);
        utilitiesRadioGroup=findViewById(R.id.utilities_RadioGroup);
    }
}