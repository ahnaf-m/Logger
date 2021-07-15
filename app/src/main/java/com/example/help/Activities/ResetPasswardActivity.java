package com.example.help.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.help.R;
import com.example.help.Utils.ProgressBar_Animation;
import com.example.help.Utils.StatesForButtons;
import com.example.help.Utils.TextWatchers.Text_Watcher_For_Reset_Passward_Activity;
import com.example.help.Utils.dialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import org.sumon.eagleeye.EagleEye;
import org.sumon.eagleeye.OnChangeConnectivityListener;

public class ResetPasswardActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private TextInputEditText reset_passward_edittext;
    private TextInputLayout reset_passward_edittext_layout;
    private AppCompatButton send_email_btn;
    private ProgressBar progressBar_reset_email;
    private FirebaseAuth auth;
    private  boolean networkIsActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passward);


        //Initializing the Variables and views of the Activity
        reset_passward_edittext = (TextInputEditText) findViewById(R.id.reset_pass_email_edittext);
        reset_passward_edittext_layout = (TextInputLayout) findViewById(R.id.reset_pass_email_edittext_layout);
        send_email_btn = (AppCompatButton) findViewById(R.id.send_email_button);
        progressBar_reset_email = findViewById(R.id.progress_bar_reset_pass);

        // On the Start of the Activity disable the Button
        StatesForButtons.disable_login_button(send_email_btn, this);

        //Toolbar For The Activity
        Toolbar toolbar = findViewById(R.id.reset_pass_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Getting a instance of Firebase Authentication
        auth = FirebaseAuth.getInstance();



        EagleEye.getStatus(this, new OnChangeConnectivityListener() {
            @Override
            public void onChanged(boolean internet_available) {
                if(internet_available){
                    networkIsActive=true;
                }
                else {
                    Toast.makeText(ResetPasswardActivity.this, ResetPasswardActivity.this.getResources().getString(R.string.no_Internet_Detection), Toast.LENGTH_SHORT).show();
                    networkIsActive=false;
                }
            }
        });




        //Initiate The TextWatcher For Our Activity
        Text_Watcher_For_Reset_Passward_Activity text_watcher_for_reset_passward_activity = new Text_Watcher_For_Reset_Passward_Activity(reset_passward_edittext, reset_passward_edittext_layout, send_email_btn, this);


        //Add text watcher to the editText
        reset_passward_edittext.addTextChangedListener(text_watcher_for_reset_passward_activity);

        send_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(networkIsActive){
                    start_progressbar();
                    send_reset_email();
                    }
                else{
                    stop_progressbar();
                    dialog.show_no_wifi_Dialog(ResetPasswardActivity.this);
                }

            }
        });


    }

    //Start The ProgressBar
    private void start_progressbar() {
        ProgressBar_Animation.start_wave_progress_anim(send_email_btn, progressBar_reset_email);
    }

    //Stop The ProgressBar
    private void stop_progressbar() {
        ProgressBar_Animation.stop_wave_progress_anim(send_email_btn, progressBar_reset_email, this);
    }


    //Method To Reset The Email
    public void send_reset_email() {

        auth.sendPasswordResetEmail(reset_passward_edittext.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            //The Task Is SuccessFull
                            Log.d(TAG, "Email sent.");
                            stop_progressbar();

                            //Show the dialog to tell the user that the email is sent
                            dialog.show_reset_pass_email_sent_Dialog(ResetPasswardActivity.this);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //Stop th ProgressBar
                            stop_progressbar();

                            //Make Sure to Catch the error occurred
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_USER_DISABLED":
                                    dialog.show_Unknown_Error_Dialog(ResetPasswardActivity.this, "User has been disabled By Administrator");
                                    break;

                                default:
                                    dialog.show_Unknown_Minimal_Error_Dialog(ResetPasswardActivity.this);
                                    break;


                            }


                        }

                    }
                });

    }
}