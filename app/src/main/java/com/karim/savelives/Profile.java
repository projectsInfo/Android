package com.karim.savelives;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Profile extends AppCompatActivity {

    private TextView profilname, profileemail, profilphone, profilblood;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        getInformation();

    }


    public void getInformation() {

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();


        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userInformation = mdatabase.child("UserName").child(user_id);

        userInformation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> getinfo = ( Map<String, Object> ) dataSnapshot.getValue();
                    if (getinfo.get("Username") != null) {
                        String username = getinfo.get("Username").toString();
                        profilname.setText(username);

                    }  if (getinfo.get("Useremail") != null) {
                        String useremail = getinfo.get("Useremail").toString();
                        profileemail.setText(useremail);
                    }  if (getinfo.get("userphone") != null) {
                        String userphone = getinfo.get("userphone").toString();
                        profilphone.setText(userphone);
                    } if(getinfo.get("bloodtype") != null){
                        String userblood = getinfo.get("bloodtype").toString();
                        profilblood.setText(userblood);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void init() {
        profilname = findViewById(R.id.profil_user_name);
        profileemail = findViewById(R.id.profil_user_email);
        profilphone = findViewById(R.id.profil_user_phone);
        profilblood = findViewById(R.id.profil_user_bloodtype);

    }
}
