package com.example.help.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;


import com.example.help.MainActivity;
import com.example.help.R;
import com.example.help.Utils.ProgressBar_Animation;
import com.example.help.Utils.StatesForButtons;
import com.example.help.Utils.TextWatchers.Text_Watcher_For_Login_Activity;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.auth.User;

import org.sumon.eagleeye.EagleEye;
import org.sumon.eagleeye.OnChangeConnectivityListener;


public class LoginActivity extends AppCompatActivity  {
    private static final String TAG = "TAG";
    private static final int RC_SIGN_IN = 1;
    private TextInputLayout email_layout;
    private TextInputLayout passward_layout;
    private TextInputEditText email_edittext;
    private TextInputEditText passward_edittext;
    private AppCompatButton login_button;
    private CardView login_with_google_view;
    private AppCompatTextView forgot_passward_button;
    private AppCompatTextView sign_up_text;
    private ProgressBar progressBar_login;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInOptions gso;
    private GoogleSignInClient google_sign_In_Client;
    private  boolean networkIsActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        // Configure Google Sign In
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        google_sign_In_Client = GoogleSignIn.getClient(this, gso);

        //Initializing the Variables and views of the Activity
        email_layout = findViewById(R.id.login_email_edittext_layout);
        passward_layout = findViewById(R.id.login_passward_edittext_layout);
        email_edittext = findViewById(R.id.login_email_edittext);
        passward_edittext = findViewById(R.id.login_passward_edittext);
        login_button = findViewById(R.id.login_button);
        login_with_google_view = findViewById(R.id.login_with_google_button);
        forgot_passward_button = findViewById(R.id.forgot_passward_btn);
        sign_up_text = findViewById(R.id.sign_up_text);
        progressBar_login = (ProgressBar) findViewById(R.id.progress_bar_login);


        StatesForButtons.disable_login_button(login_button, LoginActivity.this);


        EagleEye.getStatus(this, new OnChangeConnectivityListener() {
            @Override
            public void onChanged(boolean internet_available) {
                if(internet_available){
                     networkIsActive=true;
                }
                else {
                    Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.no_Internet_Detection), Toast.LENGTH_SHORT).show();
                    networkIsActive=false;
                }
            }
        });




        //Initiate The TextWatcher For Our Activity
        Text_Watcher_For_Login_Activity text_watcherForLoginActivity = new Text_Watcher_For_Login_Activity(email_edittext, passward_edittext, email_layout, passward_layout, login_button, this);

        //Add text watcher to the editText
        passward_edittext.addTextChangedListener(text_watcherForLoginActivity);
        email_edittext.addTextChangedListener(text_watcherForLoginActivity);

        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

                 }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_progressbar();
                if(networkIsActive){
                login_existing_user(email_edittext,passward_edittext);}
                else{
                    stop_progressbar();
                    dialog.show_no_wifi_Dialog(LoginActivity.this);
                }
            }
        });

        login_with_google_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                google_sign_in_intent();
            }
        });

        forgot_passward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswardActivity.class));


            }
        });

          }


    private void start_progressbar() {
        ProgressBar_Animation.start_wave_progress_anim(login_button, progressBar_login);
    }


    private void stop_progressbar() {
        ProgressBar_Animation.stop_wave_progress_anim(login_button, progressBar_login, this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //Disconnect The Firebase Listener
        if (null != mAuthListener) {
            mAuth.removeAuthStateListener(mAuthListener);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Connect The Firebase Listener

        if (null != mAuthListener) {
            mAuth.addAuthStateListener(mAuthListener);
        }

    }










//Manually Create The SignUp Request
    private void login_existing_user(TextInputEditText email, TextInputEditText passward) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), passward.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success,
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            stop_progressbar();
                            finish();
                        } else {


                            // If sign in fails, display a error to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            stop_progressbar();

                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            Log.e(TAG, "onComplete: "+errorCode );




                            switch (errorCode) {


                                case "ERROR_INVALID_CREDENTIAL":
                                    dialog.show_Unknown_Error_Dialog(LoginActivity.this, "Invalid Credentials");

                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    dialog.show_Unknown_Error_Dialog(LoginActivity.this, "Invalid Password");
                                    break;


                                case "ERROR_USER_DISABLED":
                                    dialog.show_Unknown_Error_Dialog(LoginActivity.this, "User has been disabled");
                                    break;


                                case "ERROR_USER_NOT_FOUND":
                                    dialog.show_Unknown_Error_Dialog(LoginActivity.this, "User Not Found");
                                    break;

                                case "ERROR_TOO_MANY_REQUESTS":
                                    dialog.show_Unknown_Minimal_Error_Dialog(LoginActivity.this);
                                        break;


                                default:
                                    dialog.show_Unknown_Error_Dialog(LoginActivity.this, "Please Try Again");
                                    break;


                            }











                        }


                    }
                });


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
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            dialog.show_Unknown_Minimal_Error_Dialog(LoginActivity.this);
                        }

                        // ...
                    }
                });
    }


}