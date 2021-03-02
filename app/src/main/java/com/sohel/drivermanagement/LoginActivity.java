package com.sohel.drivermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sohel.drivermanagement.User.StartActivity;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEdittext,passwordEdittext;
    private Button loginButton;
    private TextView registerLink;


    String email,password;


    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        emailEdittext=findViewById(R.id.loginEmailEdittextid);
        passwordEdittext=findViewById(R.id.loginPasswordEdittextid);
        loginButton=findViewById(R.id.loginButton);
        registerLink=findViewById(R.id.registerLinkTextviewid);


        progressBar=new ProgressDialog(this);
        progressBar.setMessage("Logging in");
        progressBar.setTitle("Please Wait");



        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=emailEdittext.getText().toString();
                password=passwordEdittext.getText().toString();

                if(email.isEmpty()){
                    emailEdittext.setError("Please Write Your Email.");
                    emailEdittext.requestFocus();
                }else if(password.isEmpty()){
                    passwordEdittext.setError("Please Write your Password");
                    passwordEdittext.requestFocus();
                }else{
                    if(email.equals("admin@gmail.com") && password.equals("123456")){
                       sendUserToAdminMainActivity();
                    }else{
                        loginUser();
                    }

                }

            }
        });




    }

    private void sendUserToAdminMainActivity() {
        Intent intent=new Intent(LoginActivity.this,AdminMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUser() {
        progressBar.show();

        mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.dismiss();
                                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();
                                sendUserToStartActivity();
                            } else{
                                progressBar.dismiss();
                                Toasty.warning(LoginActivity.this, "Login Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    });


    }

    private void sendUserToRegisterActivity() {
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            sendUserToStartActivity();
        }
    }


    public void sendUserToMainActivity() {
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }    public void sendUserToStartActivity() {
        Intent intent=new Intent(LoginActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }



}