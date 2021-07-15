package com.example.help.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.help.MainActivity;
import com.example.help.R;
import com.example.help.Utils.ProgressBar_Animation;
import com.example.help.Utils.StatesForButtons;
import com.example.help.Utils.TextWatchers.Text_Watcher_For_SignUp_Activity;
import com.example.help.Utils.dialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;

import org.sumon.eagleeye.EagleEye;
import org.sumon.eagleeye.OnChangeConnectivityListener;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private static final int RC_SIGN_IN = 1;
    private TextInputLayout signup_email_layout;
    private TextInputLayout signup_passward_layout;
    private TextInputLayout signup_confirm_passward_layout;
    private TextInputEditText signup_email_editText;
    private TextInputEditText signup_passward_editText;
    private TextInputEditText signup_confirm_passward_editText;
    private AppCompatButton signup_btn;
    private CardView sign_up_with_google;
    private GoogleSignInOptions gso;
    private GoogleSignInClient google_sign_In_Client;
    private ProgressBar progressBar_signup;
    private  boolean networkIsActive;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        //Getting a instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Initializing the Variables and views of the Activity
        progressBar_signup = (ProgressBar) findViewById(R.id.progress_bar_signup);
        signup_email_layout = findViewById(R.id.signup_email_edittext_layout);
        signup_passward_layout = findViewById(R.id.sign_up_passward_edittext_layout);
        signup_confirm_passward_layout = findViewById(R.id.sign_up_confirm_passward_edittext_layout);
        signup_email_editText = findViewById(R.id.sign_up_email_edittext);
        signup_passward_editText = findViewById(R.id.sign_up_passward_edittext);
        signup_confirm_passward_editText = findViewById(R.id.sign_up_confirm_passward_edittext);
        signup_btn = findViewById(R.id.sign_up_button);
        sign_up_with_google = findViewById(R.id.signup_with_google_button);


        // On the Start of the Activity disable the Button
        StatesForButtons.disable_login_button(signup_btn, this);

        //Toolbar For The Activity
        Toolbar toolbar = findViewById(R.id.sign_up_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //Initiate The TextWatcher For Our Activity
        Text_Watcher_For_SignUp_Activity text_watcher_for_signUp_activity = new Text_Watcher_For_SignUp_Activity(signup_btn, this, signup_email_layout, signup_passward_layout, signup_confirm_passward_layout, signup_email_editText, signup_passward_editText, signup_confirm_passward_editText);

        //Add text watcher to the editText
        signup_email_editText.addTextChangedListener(text_watcher_for_signUp_activity);
        signup_passward_editText.addTextChangedListener(text_watcher_for_signUp_activity);
        signup_confirm_passward_editText.addTextChangedListener(text_watcher_for_signUp_activity);

        EagleEye.getStatus(this, new OnChangeConnectivityListener() {
            @Override
            public void onChanged(boolean internet_available) {
                if(internet_available){
                    networkIsActive=true;
                }
                else {
                    Toast.makeText(SignUpActivity.this, SignUpActivity.this.getResources().getString(R.string.no_Internet_Detection), Toast.LENGTH_SHORT).show();
                    networkIsActive=false;
                }
            }
        });

        // Configure Google Sign In
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        google_sign_In_Client = GoogleSignIn.getClient(this, gso);



        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(networkIsActive){
                    start_progressbar();
                    signup_to_create_account_manually();

                }
                else{
                    stop_progressbar();
                    dialog.show_no_wifi_Dialog(SignUpActivity.this);
                }
            }
        });

        sign_up_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                google_sign_in_intent();
            }
        });

    }


    private void start_progressbar() {
        ProgressBar_Animation.start_wave_progress_anim(signup_btn, progressBar_signup);
    }


    private void stop_progressbar() {
        ProgressBar_Animation.stop_wave_progress_anim(signup_btn, progressBar_signup, this);
    }



    //Intent To Start The Google Sign In
    private void google_sign_in_intent() {
        google_sign_In_Client.signOut();
        Intent signInIntent = google_sign_In_Client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    //Returns Result From Google Sign In
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    //Firebase Authenticator For Google Account
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            dialog.show_Unknown_Minimal_Error_Dialog(SignUpActivity.this);
                        }

                        // ...
                    }
                });
    }



    //Manually Create The SignUp Request
    private void signup_to_create_account_manually() {
        mAuth.createUserWithEmailAndPassword(signup_email_editText.getText().toString(), signup_passward_editText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "createUserWithEmail:success");
                            stop_progressbar();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }

                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            stop_progressbar();
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {


                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    dialog.show_Unknown_Error_Dialog(SignUpActivity.this, "Email Already Exists");
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    dialog.show_Unknown_Error_Dialog(SignUpActivity.this, "Email Already Exists ");
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    dialog.show_Unknown_Error_Dialog(SignUpActivity.this, "Weak Password");
                                    break;
                                case "ERROR_USER_DISABLED":
                                    dialog.show_Unknown_Error_Dialog(SignUpActivity.this, "User Email has been disabled");
                                    break;

                                default:
                                    dialog.show_Unknown_Error_Dialog(SignUpActivity.this, "Please Try Again");
                                    break;


                            }
                        }


                    }
                });
    }


}