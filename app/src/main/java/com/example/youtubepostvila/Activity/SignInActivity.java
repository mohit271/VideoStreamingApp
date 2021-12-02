package com.example.youtubepostvila.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.youtubepostvila.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding= ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Log in");
        progressDialog.setMessage("Logging to your account");
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText().toString().trim()).matches()){
                    binding.etEmail.setError("Invalid email Address");
                    binding.etEmail.requestFocus();
                    return;
                }
                if(binding.etPassword.getText().toString().trim().length()<6) {
                    binding.etPassword.setError("Invalid Password");
                    binding.etPassword.requestFocus();
                    return;
                }
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        binding.tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if(mAuth.getCurrentUser()!=null){
            Intent intent= new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}