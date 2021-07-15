package com.example.help.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.help.MainActivity;
import com.example.help.R;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartingActivity extends AppCompatActivity {
    private static final String TAG ="TAG" ;
    private  ViewAnimator animator_view;
    ImageView img;

    private   FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);




      img= findViewById(R.id.animated_img);
        animator_view = ViewAnimator
                .animate(img)
                .rotation(360)
                .repeatCount(Animation.INFINITE)
                .start();




       Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                //what ever you do here will be done after 3 seconds delay.



                //Firebase Methods For Checking If User is Signed In
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        if (currentUser != null) {
                            //User is signed In


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            // User Not Signed


                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                };




            }
        };
        handler.postDelayed(r, 2500);

    }

    @Override
    protected void onStop() {
        super.onStop();


        Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                //what ever you do here will be done after 3 seconds delay.
                //Disconnect The Firebase Listener
                if (null != mAuthListener) {
                    mAuth.removeAuthStateListener(mAuthListener);

                }
            }
        };
        handler.postDelayed(r, 3000);




    }

    @Override
    protected void onResume() {
        super.onResume();




        Handler handler=new Handler();
        Runnable r=new Runnable() {
            public void run() {
                //what ever you do here will be done after 3 seconds delay.
//Connect The Firebase Listener

                if (null != mAuthListener) {
                    mAuth.addAuthStateListener(mAuthListener);
                }
            }
        };
        handler.postDelayed(r, 3000);


    }









}