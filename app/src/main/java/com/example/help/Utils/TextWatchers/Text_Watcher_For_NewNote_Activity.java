package com.example.help.Utils.TextWatchers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;

import com.example.help.R;
import com.example.help.Utils.StatesForButtons;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Text_Watcher_For_NewNote_Activity implements TextWatcher {
    TextInputEditText username;
    TextInputEditText passward;
    TextInputEditText description;

    TextInputLayout username_layout;
    TextInputLayout passward_layout;
    TextInputLayout description_layout;

    AppCompatButton save_note;
    Context context;

    public Text_Watcher_For_NewNote_Activity(TextInputEditText username, TextInputEditText passward, TextInputEditText description, TextInputLayout username_layout, TextInputLayout passward_layout, TextInputLayout description_layout, AppCompatButton save_note, Context context) {
        this.username = username;
        this.passward = passward;
        this.description = description;
        this.username_layout = username_layout;
        this.passward_layout = passward_layout;
        this.description_layout = description_layout;
        this.save_note = save_note;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (username.getText().toString().length() == 0 && passward.getText().toString().length() == 0) {
            username_layout.setErrorEnabled(false);
            passward_layout.setErrorEnabled(false);
            StatesForButtons.disable_login_button(save_note, context);

        } else if (username.getText().toString().length() == 0 || passward.getText().toString().length() == 0) {
            username_layout.setErrorEnabled(false);
            StatesForButtons.disable_login_button(save_note,context);

        }
        else if(passward.getText().toString().length() < 8){

            passward_layout.setErrorEnabled(true);
            passward_layout.setError(context.getString(R.string.TextWatcher_valid_pass));
            StatesForButtons.disable_login_button(save_note,context);
        }

        else if(username.getText().toString().length()<5 ){

            username_layout.setErrorEnabled(true);
            username_layout.setError(context.getString(R.string.TextWatcher_username_));
            StatesForButtons.disable_login_button(save_note,context);
        }
        else {
            username_layout.setErrorEnabled(false);
            passward_layout.setErrorEnabled(false);
            StatesForButtons.enable_login_button(save_note,context);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
