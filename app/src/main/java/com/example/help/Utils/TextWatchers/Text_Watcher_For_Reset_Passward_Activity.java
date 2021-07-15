package com.example.help.Utils.TextWatchers;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;

import com.example.help.R;
import com.example.help.Utils.EmailValidator;
import com.example.help.Utils.StatesForButtons;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Text_Watcher_For_Reset_Passward_Activity implements TextWatcher {

    TextInputEditText email;
    TextInputLayout email_layout;
    AppCompatButton send_email;
    Context context;

    public Text_Watcher_For_Reset_Passward_Activity(TextInputEditText email, TextInputLayout email_layout, AppCompatButton send_email, Context context) {
        this.email = email;
        this.email_layout = email_layout;
        this.send_email = send_email;
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (email.getText().toString().length() == 0 ) {
            email_layout.setErrorEnabled(false);
            StatesForButtons.disable_login_button(send_email, context);

        }
        else if (email.getText().toString().length() == 0 ) {
            StatesForButtons.disable_login_button(send_email,context);
            email_layout.setErrorEnabled(false);
        }
        else if (!EmailValidator.isValidEmail(email)) {
            email_layout.setError(context.getString(R.string.TextWatcher_valid_email));
            email_layout.setErrorEnabled(true);
            StatesForButtons.disable_login_button(send_email,context);
        }
        else {
            email_layout.setErrorEnabled(false);
            StatesForButtons.enable_login_button(send_email,context);
        }
    }
}

