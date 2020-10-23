package com.karim.savelives;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText enteremail;
    private Button frorgetpass;
    private FirebaseAuth firebaseAuth;
    private String useremil, emailreset, errorreset , empetemailreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        // Ui design
        init();

        frorgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Function to reset password
                ResetPassword();
            }
        });
    }

    // Function to reset password
    public void ResetPassword() {
        useremil = enteremail.getText().toString().trim();
        if (!useremil.equals("")) {
            firebaseAuth.sendPasswordResetEmail(useremil).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgetPassword.this, emailreset, Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ForgetPassword.this, Loginform.class));
                    } else {
                        Toast.makeText(ForgetPassword.this, "Error Reset", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, empetemailreset, Toast.LENGTH_SHORT).show();
        }
    }

    // Function to ui design
    public void init() {
        enteremail = ( EditText ) findViewById(R.id.forgetpass_email_edt);
        frorgetpass = ( Button ) findViewById(R.id.forgetpass_btn);
        firebaseAuth = FirebaseAuth.getInstance();
//        emailreset = ForgetPassword.this.getResources().getString(R.string.resetemail);
//        errorreset = ForgetPassword.this.getResources().getString(R.string.reseterror);
//        empetemailreset = ForgetPassword.this.getResources().getString(R.string.empetyresetemail);
    }
}
