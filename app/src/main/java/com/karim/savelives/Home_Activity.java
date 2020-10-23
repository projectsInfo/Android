package com.karim.savelives;

import android.app.ActionBar;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

public class Home_Activity extends AppCompatActivity {

    Button Donate, Request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);

        Donate = ( Button ) findViewById(R.id.Donate_btn);
        // calling onClick() method
        Request = ( Button ) findViewById(R.id.Request_btn);
        // Function to nav
        initialize();


    }


    public void initialize() {
        Donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Home_Activity.this, DonorsMaps.class);
                startActivity(in);
            }
        });

        Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Home_Activity.this, MapsActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.profile_btn) {
            Toast.makeText(this, "Profile menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Profile.class));

        } else if (id == R.id.settings_btn) {
            Toast.makeText(this, "setting menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Home_Activity.class));


        } else if (id == R.id.about_btn) {
            Toast.makeText(this, "About menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AboutActivity.class));

        } else if (id == R.id.logout_btn) {
            Toast.makeText(this, "Log out menu is Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Loginform.class));

        }
        return super.onOptionsItemSelected(item);


    }

    public void onBackPressed() {

    }
}