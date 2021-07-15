package com.example.help;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.help.Utils.EncryptionClass;
import com.example.help.Utils.StatesForButtons;
import com.example.help.Utils.TextWatchers.Text_Watcher_For_NewNote_Activity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class NewNoteActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String unique = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public final CollectionReference reference = db.collection(unique);

    TextInputEditText editTextusername;
    TextInputEditText editTextpassward;
    TextInputEditText editTextDescription;
    AppCompatButton save_note;
    private TextInputLayout username_layout;
    private TextInputLayout passward_layout;
    private TextInputLayout description_layout;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        ActionBar actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.light_orange));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        //Sets the title of the action bar
        setTitle("Save Account Details");


        //Initializing the Variables and views of the Activity
        username_layout = findViewById(R.id.username_edittext_layout);
        passward_layout = findViewById(R.id.passward_edittext_layout);
        description_layout = findViewById(R.id.description_edittext_layout);


        editTextusername = findViewById(R.id.edit_text_username);
        editTextpassward = findViewById(R.id.edit_text_passward);
        editTextDescription = findViewById(R.id.edit_text_description);
        save_note = (AppCompatButton) findViewById(R.id.save_note);

        //initially disable the button
        StatesForButtons.disable_login_button(save_note, NewNoteActivity.this);

        id = getIntent().getStringExtra("id");
        String user_name = getIntent().getStringExtra("site_name");
        String pass = getIntent().getStringExtra("pass");
        String description = getIntent().getStringExtra("description");


        username_layout.setErrorEnabled(false);
        Text_Watcher_For_NewNote_Activity text_watcher_for_newNote_activity = new Text_Watcher_For_NewNote_Activity(editTextusername, editTextpassward, editTextDescription, username_layout, passward_layout, description_layout, save_note, this);
        editTextusername.addTextChangedListener(text_watcher_for_newNote_activity);
        editTextpassward.addTextChangedListener(text_watcher_for_newNote_activity);
        editTextDescription.addTextChangedListener(text_watcher_for_newNote_activity);


        //if id is null that means its a new click if not null its a edit click means the this is coming from the long press activity

        if (id == null || id.isEmpty()) {


        } else {
            editTextpassward.setText(pass);
            editTextusername.setText(user_name);
            if (description.isEmpty() || description.length() < 0) {
                editTextDescription.setText("");
            } else {
                editTextDescription.setText(description);
                setTitle("Edit Account Details");
            }
        }


        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        Log.d("TAG", "onCreate: " + " " + id + " " + user_name + " " + pass);

    }

    //logic for saving based on new or edit click
    private void saveNote() {
        String site_name = editTextusername.getText().toString();
        String site_passward = editTextpassward.getText().toString();
        String description = editTextDescription.getText().toString();




        String encrypt_site_name = "";
        String encrypt_passward = "";
        String encrypted_description = "";
        try {
            encrypt_site_name = EncryptionClass.encrypt(site_name);
            encrypt_passward = EncryptionClass.encrypt(site_passward);
            encrypted_description = EncryptionClass.encrypt(description);
            Log.d("TEST", "unencrypted1:" + site_name.length());
            Log.d("TEST", "uencrypted1:" + site_passward.length());
            Log.d("TEST", "encrypt_site_name:" + encrypt_site_name.length());
            Log.d("TEST", "encrypt_site_name:" + encrypt_passward.length());


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id == null || id.isEmpty()) {
            reference.add(new UsersContent(encrypt_site_name, encrypt_passward, encrypted_description));
            Toasty.success(this, "Saved Successfully", Toast.LENGTH_SHORT, true).show();

            finish();
        } else {

            reference.document(id).update("site_name", encrypt_site_name, "pass", encrypt_passward, "description", encrypted_description);
            Toasty.success(this, "Edited Successfully", Toast.LENGTH_SHORT, true).show();
            finish();

        }

    }

    //logic for when back pressed
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}