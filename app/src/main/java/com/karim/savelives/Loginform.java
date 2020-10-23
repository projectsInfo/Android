package com.karim.savelives;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Loginform extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mdatabaseuser;
    FirebaseAuth mAuth;
    private String user_id, email, password;
    private EditText usermobile, usepassword;
    private Button resetpass;
    private ProgressDialog dialog;


    Button register, Loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginform);

        // UI design
        init();
        // Function to progress dialog
        progress();

        // linke with firebase
        mAuth = FirebaseAuth.getInstance();

        Loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Loginuser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Loginform.this, Registration_Activity.class);
                startActivity(in);
            }
        });

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Loginform.this , ForgetPassword.class);
                startActivity(in);
            }
        });
    }


    // Function to user login
    public void Loginuser() {
        // UI design
        email = usermobile.getText().toString();
        password = usepassword.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Loginform.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkuserexist();

                    } else {
                        // progress dialog
                        dialog.dismiss();
                        // make Toast
                        Toast.makeText(Loginform.this, "Invalid Email Or password", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }else{
            // progress dialog
            dialog.dismiss();
            // Toast
            Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        }
    }


    // Function to if user exist or no
    private void checkuserexist() {
        // get user id
        user_id = mAuth.getCurrentUser().getUid();
        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("UserName");
//        DatabaseReference current_user_db = mdatabaseuser.child(user_id);

        mdatabaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    // go to home activity
                    Intent map_intent = new Intent(Loginform.this, Home_Activity.class);
                    map_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(map_intent);

                } else {
                    // go to splash activity
                    Intent setup_intent = new Intent(Loginform.this, AboutActivity.class);
                    setup_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setup_intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onBackPressed() {

    }

    @Override
    protected void onStart() {
        super.onStart();
//         mAuth.addAuthStateListener(mAuthListener);
    }


    //Function Progress dialog
    public void progress() {
        dialog = new ProgressDialog(Loginform.this);
        dialog.setMessage("Please waite...");
        dialog.setCancelable(false);

    }


    public void init() {

        usermobile = ( EditText ) findViewById(R.id.loginform_input_usermobile);
        usepassword = ( EditText ) findViewById(R.id.loginform_input_password);
        register = ( Button ) findViewById(R.id.loginform_button_register);
        Loginbutton = ( Button ) findViewById(R.id.loginform_button_login);
        resetpass = findViewById(R.id.loginform_button_forgot_password);
    }

}

