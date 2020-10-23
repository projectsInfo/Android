package com.karim.savelives;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registration_Activity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mdatabaseuser;
    FirebaseAuth mAuth;

    private EditText usermopile, userbloodType, userphone, userpassword, username;
    private String bloodty, email, password, simpleemail, confirm_password, name, phone;

    Button register;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);

        //UI design
        init();
        // Function progress dialog
        progress();

        // Link with firebase
        mAuth = FirebaseAuth.getInstance();

        // Register Button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // progress dialog
                dialog.show();
                // Function to register
                StartRegister();

            }
        });


    }


    // Function to make registration
    public void StartRegister() {
        // Ui design
        name = username.getText().toString();
        email = usermopile.getText().toString();
        bloodty = userbloodType.getText().toString();
        phone = userphone.getText().toString();
        password = userpassword.getText().toString();
//        confirm_password = userpassword.getText().toString();

        if (!name.equals("") && !email.equals("") && !bloodty.equals("") && !phone.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration_Activity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        // Save users
                        String user_id = mAuth.getCurrentUser().getUid();
                        mdatabaseuser = FirebaseDatabase.getInstance().getReference().child("UserName");
                        DatabaseReference current_user_db = mdatabaseuser.child(user_id);

                        Map editor = new HashMap();
                        editor.put("Username", name);
                        editor.put("Useremail", email);
                        editor.put("bloodtype", bloodty);
                        editor.put("userphone", phone);

                        // editor.apply();
                        current_user_db.setValue(editor);


//                        Toast.makeText(Registration_Activity.this, "sucess", Toast.LENGTH_SHORT).show();


                        Intent mobile_intent = new Intent(Registration_Activity.this, Home_Activity.class);
                        startActivity(mobile_intent);


                    } else {
                        // progress dialog
                        dialog.dismiss();
                        Toast.makeText(Registration_Activity.this, "failed", Toast.LENGTH_SHORT).show();
                    }

                }

            });
        } else {
            // progress dialog
            dialog.dismiss();
            Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    // to start login if user record
    protected void onStart() {
        super.onStart();
        // mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  mAuth.removeAuthStateListener(firebaseAuthListener);

    }


    //Function Progress dialog
    public void progress() {
        dialog = new ProgressDialog(Registration_Activity.this);
        dialog.setMessage("Please waite...");
        dialog.setCancelable(false);

    }

    // Function to Ui design
    public void init() {

        usermopile = ( EditText ) findViewById(R.id.input_MobileNumber);
        userbloodType = ( EditText ) findViewById(R.id.input_BloodType);
        userphone = ( EditText ) findViewById(R.id.input_userphone);
        userpassword = ( EditText ) findViewById(R.id.input_password);
        register = ( Button ) findViewById(R.id.button_register);
        username = findViewById(R.id.input_name);
    }
}
