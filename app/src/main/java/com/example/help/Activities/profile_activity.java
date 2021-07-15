package com.example.help.Activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.help.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profile_activity extends AppCompatActivity {

    private static final String TAG = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.light_orange));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);


        setTitle("Profile");
        String userEmail="";
        String  full_user_ID="";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
             userEmail = user.getEmail();
             full_user_ID=user.getUid();
            Log.d(TAG, "onCreate: "+full_user_ID);
        } else {
            finish();
        }



        String simplified_uid = "";

        if(full_user_ID.length()> 25){
            simplified_uid=full_user_ID.substring(0,15);
        }
        else {
            simplified_uid=full_user_ID;
        }

        String added_Dots=simplified_uid.trim()+"...";

        TextView email_id = findViewById(R.id.emai_real_holder);
        email_id.setText(userEmail);
        TextView id_holder = findViewById(R.id.user_id_container);
        id_holder.setText(added_Dots);

        AppCompatButton log_btn = findViewById(R.id.log_out_btn);
        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_out();
            }
        });

    }


    public void log_out() {
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(profile_activity.this, LoginActivity.class));
        profile_activity.this.finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}