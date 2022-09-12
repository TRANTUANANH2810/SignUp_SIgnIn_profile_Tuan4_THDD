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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterEmail, editTextRegisternName, editTextRegisterPwd;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Sign up");

        findView();
        showHidePassword();

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextRegisterEmail.getText().toString();
                String textPassword = editTextRegisterPwd.getText().toString();
                String textName = editTextRegisternName.getText().toString();
                
//                check if all the dât are present before regisster user 
                if (TextUtils.isEmpty(textName))
                {
                    Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    editTextRegisternName.setError("Name is Required");
                    editTextRegisternName.requestFocus();
                }else if (TextUtils.isEmpty(textEmail))
                {
                    Toast.makeText(RegisterActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is Required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Plase Regester email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid Email is Required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this, "Please enter your Pasword", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Pasword is Required");
                    editTextRegisterPwd.requestFocus();
                }
                else if (textPassword.length()< 6){
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Pasword should be at least 6 digits ");
                    editTextRegisterPwd.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    regissterUre(textEmail,textName,textPassword);
                }
            }


        });

    }
    private void regissterUre(String textEmail, String textName, String textPassword) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//        check if the ures creation wess sucsess
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    if (firebaseUser != null){
//                        update display name of the user
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                setDisplayName(textName).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

//                       Open UserProfile Activity after user is created
                        Intent userProfileActivity = new Intent(RegisterActivity.this, UserProfileActivity.class);


                        userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(userProfileActivity);
                        finish();
                    }
                }else {
//                    handle exception
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showHidePassword() {
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);

        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextRegisterPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    editTextRegisterPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
//                    change Icon
                }else {
                    editTextRegisterPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });

    }

    private void findView() {
        editTextRegisterEmail = findViewById(R.id.edittext_register_Email);
        editTextRegisternName = findViewById(R.id.edittext_register_name);
        editTextRegisterPwd = findViewById(R.id.edittext_register_pwd);
        progressBar = findViewById(R.id.progressBar);
    }
}