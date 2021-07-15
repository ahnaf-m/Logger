package com.example.help.Utils.TextWatchers;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.example.help.R;
import com.example.help.Utils.EmailValidator;
import com.example.help.Utils.StatesForButtons;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Text_Watcher_For_Login_Activity implements TextWatcher {
    TextInputEditText email;
    TextInputEditText passward;
    TextInputLayout email_layout;
    TextInputLayout passward_layout;
    AppCompatButton login_button;
    Context context;

    public Text_Watcher_For_Login_Activity(TextInputEditText email, TextInputEditText passward, TextInputLayout email_layout, TextInputLayout passward_layout, AppCompatButton login_button, Context context) {
        this.email = email;
        this.passward = passward;
        this.email_layout = email_layout;
        this.passward_layout = passward_layout;
        this.login_button = login_button;
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
        if (email.getText().toString().length() == 0 && passward.getText().toString().length() == 0) {
            email_layout.setErrorEnabled(false);
            StatesForButtons.disable_login_button(login_button, context);

        } else if (email.getText().toString().length() == 0 || passward.getText().toString().length() == 0) {
            StatesForButtons.disable_login_button(login_button,context);
            email_layout.setErrorEnabled(false);
        } else if (!EmailValidator.isValidEmail(email)) {
            email_layout.setError(context.getString(R.string.TextWatcher_valid_email));
            email_layout.setErrorEnabled(true);
            StatesForButtons.disable_login_button(login_button,context);
        } else if(passward.getText().toString().length() < 8){

            passward_layout.setErrorEnabled(true);
            passward_layout.setError(context.getString(R.string.TextWatcher_valid_pass));
            StatesForButtons.disable_login_button(login_button,context);
        }

        else {
            email_layout.setErrorEnabled(false);
            passward_layout.setErrorEnabled(false);
            StatesForButtons.enable_login_button(login_button,context);
        }
    }
}
