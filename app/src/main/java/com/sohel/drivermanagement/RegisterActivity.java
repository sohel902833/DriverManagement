package com.sohel.drivermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sohel.drivermanagement.User.StartActivity;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private EditText emailEdittext,passwordEdittext,nameEdittext;
    private Button registerButton;
    private TextView loginLink;
    String email,password,name;

    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRef= FirebaseDatabase.getInstance().getReference().child("users");


        mAuth=FirebaseAuth.getInstance();
        nameEdittext=findViewById(R.id.registerNameEdittextid);
        emailEdittext=findViewById(R.id.registerEmailEdittextid);
        passwordEdittext=findViewById(R.id.registerPasswordEdittextid);
        registerButton=findViewById(R.id.registerButtonid);
        loginLink=findViewById(R.id.loginLinkTextviewid);


        progressBar=new ProgressDialog(this);
        progressBar.setMessage("Please Wait .while we are Creating your account.");
        progressBar.setTitle("Loading...");



        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailEdittext.getText().toString();
                password=passwordEdittext.getText().toString();
                name=nameEdittext.getText().toString();
                if(name.isEmpty()){
                    nameEdittext.setError("Please Write Your Name.");
                    nameEdittext.requestFocus();
                }
                else if(email.isEmpty()){
                    emailEdittext.setError("Please Write Your Email.");
                    emailEdittext.requestFocus();
                }else if(password.isEmpty()){
                    passwordEdittext.setError("Please Write your Password");
                    passwordEdittext.requestFocus();
                }else{
                    registerUser();
                }

            }
        });







    }


    private void registerUser() {
        progressBar.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid=mAuth.getCurrentUser().getUid();
                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("uid",uid);
                            userMap.put("email",email);
                            userMap.put("homeAdded","false");
                            userMap.put("password",password);
                            userMap.put("name",name);
                            userRef.child(uid).setValue(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressBar.dismiss();
                                                    Toasty.success(RegisterActivity.this, "Register Success!", Toast.LENGTH_SHORT, true).show();
                                                    sendUserToStartActivity();
                                                }else{
                                                    progressBar.dismiss();
                                                    Toasty.warning(RegisterActivity.this, "Register Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();

                                                }
                                            }
                                        });
                        } else{
                            progressBar.dismiss();
                            Toasty.warning(RegisterActivity.this, "Register Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                            nameEdittext.setText(task.getException().getMessage());
                        }
                    }
                });


    }

    private void sendUserToLoginActivity() {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void sendUserToMainActivity() {
        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void sendUserToStartActivity() {
        Intent intent=new Intent(RegisterActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }





}