package com.example.signin_signup_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPassWord;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Login");

        editTextLoginEmail = findViewById(R.id.edittext_login_email);
        editTextLoginPassWord = findViewById(R.id.edittext_login_pwd);
        progressBar = findViewById(R.id.progressBar);

        showHidePassword();

//        register User
        TextView textViewRegister = findViewById(R.id.textView_link_regiter);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

        auth = FirebaseAuth.getInstance();

//        login User
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPassWord.getText().toString();
                //     check if all the dât are present before sign in  user
                if (TextUtils.isEmpty(textEmail))
                {
                    Toast.makeText(MainActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is Required");
                    editTextLoginEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(MainActivity.this, "Plase Regester email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid Email is Required");
                    editTextLoginEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textPassword)){
                    Toast.makeText(MainActivity.this, "Please enter your Pasword", Toast.LENGTH_SHORT).show();
                    editTextLoginPassWord.setError("Pasword is Required");
                    editTextLoginPassWord.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPassword);
                }
            }
        });

    }

    private void loginUser(String textEmail, String textPassword) {
    auth.signInWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful())
            {
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent userProfileActivity = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(userProfileActivity);
            }else {
                try {
                    throw task.getException();
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        }
    });
    }


    //    show hide pw use eye icon
    private void showHidePassword() {

        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);

        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextLoginPassWord.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    editTextLoginPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
//                    change Icon
                }else {
                    editTextLoginPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });

    }
}