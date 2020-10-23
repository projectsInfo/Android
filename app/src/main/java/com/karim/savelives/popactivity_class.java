package com.karim.savelives;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by mahmo on 10/9/2017.
 */

public class popactivity_class extends DialogFragment {

    View view;
    Context con;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.popup_activity, container);
        // Init Ui design
        Button cancell_popup = view.findViewById(R.id.btn_Accept);
        // Init Ui design
        final TextView donorname = view.findViewById(R.id.donor_name_tv_Donor);
        final TextView donorphone = view.findViewById(R.id.donor_phone_tv_Donor);
        final TextView donoremail = view.findViewById(R.id.donor_email_tv_Donor);
        final TextView donorbloodtype = view.findViewById(R.id.donor_bloodtype_tv_Donor);

        DatabaseReference getDonorId = FirebaseDatabase.getInstance().getReference().child("Users").child("DonorId");
        getDonorId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = ( Map<String, Object> ) dataSnapshot.getValue();
                    map.get("donorReftId");
                    String user_id = map.get("donorReftId").toString();
                    Log.i(TAG, "Domnnnoorrrrrid: " + user_id);

                    DatabaseReference getDonorInformations = FirebaseDatabase.getInstance().getReference().child("UserName").child(user_id);

                    getDonorInformations.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Map<String, Object> map = ( Map<String, Object> ) dataSnapshot.getValue();
                                if (map.get("Useremail") != null) {
                                    String email = map.get("Useremail").toString();
                                    Log.i(TAG, "onDataemailllll: " + email);
                                    donoremail.setText(email);
                                }
                                if (map.get("Username") != null) {
                                    String name = map.get("Username").toString();
                                    donorname.setText(name);
                                }
                                if (map.get("bloodtype") != null) {
                                    String bloodtype = map.get("bloodtype").toString();
                                    donorbloodtype.setText(bloodtype);
                                }
                                if (map.get("userphone") != null) {
                                    String phone = map.get("userphone").toString();
                                    donorphone.setText(phone);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cancell_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }


}
