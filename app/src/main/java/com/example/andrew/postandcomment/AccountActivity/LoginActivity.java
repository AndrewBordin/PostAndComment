package com.example.andrew.postandcomment.AccountActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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

public class LoginActivity extends Activity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Button btnLogin, btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();


        inputEmail = findViewById(R.id.edtEmail);
        inputPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Enter an email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.length() < 6){
                    Toast.makeText(getApplicationContext(), "Passowrd must be 6 characters long!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Now authenticate the user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
