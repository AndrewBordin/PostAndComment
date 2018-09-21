package com.example.andrew.postandcomment.AccountActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andrew.postandcomment.MainActivity;
import com.example.andrew.postandcomment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.content.ContentValues.TAG;

public class SignupActivity extends Activity {

    private EditText inputEmail, inputPassword, inputUsername;
    private Button btnSignup, btnSignin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        btnSignin = findViewById(R.id.btnSignin);
        btnSignup = findViewById(R.id.btnCreateAccount);
        inputEmail = findViewById(R.id.edtEmail);
        inputPassword = findViewById(R.id.edtPassword);
        inputUsername = findViewById(R.id.edtUsername);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String displayName = inputUsername.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter in an Email Address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Enter in a password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.length() < 6){
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(displayName)){
                    Toast.makeText(getApplicationContext(), "Please enter in a display name.", Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                }else{
                                    FirebaseUser user;
                                    user = FirebaseAuth.getInstance().getCurrentUser();

                                    String username = inputUsername.getText().toString().trim();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                }
                                            });
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });



            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
