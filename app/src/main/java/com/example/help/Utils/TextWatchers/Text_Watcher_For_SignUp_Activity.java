package com.example.help.Utils.TextWatchers;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import androidx.appcompat.widget.AppCompatButton;

import com.example.help.R;
import com.example.help.Utils.EmailValidator;
import com.example.help.Utils.StatesForButtons;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static android.content.ContentValues.TAG;

public class Text_Watcher_For_SignUp_Activity implements TextWatcher {



    AppCompatButton sign_up;
    Context context;

    TextInputLayout mEmail_layout;
    TextInputLayout mPassward_layout;
    TextInputLayout mConfirm_passward_layout;

    TextInputEditText mEmail;
    TextInputEditText mPassward;
    TextInputEditText mConfirm_passward;

    public Text_Watcher_For_SignUp_Activity(AppCompatButton sign_up, Context context, TextInputLayout mEmail_layout, TextInputLayout mPassward_layout, TextInputLayout mConfirm_passward_layout, TextInputEditText mEmail, TextInputEditText mPassward, TextInputEditText mConfirm_passward) {
        this.sign_up = sign_up;
        this.context = context;
        this.mEmail_layout = mEmail_layout;
        this.mPassward_layout = mPassward_layout;
        this.mConfirm_passward_layout = mConfirm_passward_layout;
        this.mEmail = mEmail;
        this.mPassward = mPassward;
        this.mConfirm_passward = mConfirm_passward;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void afterTextChanged(Editable editable) {



        if (mEmail.getText().toString().length()==0 && mPassward.getText().toString().length()==0 && mConfirm_passward.getText().toString().length()==0) {
            mEmail_layout.setErrorEnabled(false);
            mPassward_layout.setErrorEnabled(false);
            mConfirm_passward_layout.setErrorEnabled(false);
            StatesForButtons.disable_login_button(sign_up, context);

        } else if (mEmail.getText().toString().length()==0 || mPassward.getText().toString().length()==0 || mConfirm_passward.getText().toString().length()==0) {
            mEmail_layout.setErrorEnabled(false);
            mPassward_layout.setErrorEnabled(false);
            mConfirm_passward_layout.setErrorEnabled(false);
            StatesForButtons.disable_login_button(sign_up, context);
        }

        else if (!EmailValidator.isValidEmail(mEmail)) {
            mEmail_layout.setError(context.getString(R.string.TextWatcher_valid_email));
            mEmail_layout.setErrorEnabled(true);
            StatesForButtons.disable_login_button(sign_up, context);


        }

        else if(!mPassward.getText().toString().equals(mConfirm_passward.getText().toString())){

            mConfirm_passward_layout.setErrorEnabled(true);
            mPassward_layout.setErrorEnabled(true);
            mConfirm_passward_layout.setError(context.getString(R.string.TextWatcher_pass_dont_match));
            mPassward_layout.setError(context.getString(R.string.TextWatcher_pass_dont_match));
            StatesForButtons.disable_login_button(sign_up, context);
        }


        else if(mPassward.getText().toString().length() < 8){
            mPassward_layout.setErrorEnabled(true);
            mConfirm_passward_layout.setError(context.getString(R.string.TextWatcher_valid_pass));
            StatesForButtons.disable_login_button(sign_up, context);
        }

        else if(mConfirm_passward.getText().toString().length() < 8){
            mConfirm_passward_layout.setErrorEnabled(true);
                mConfirm_passward_layout.setError(context.getString(R.string.TextWatcher_valid_pass));
            StatesForButtons.disable_login_button(sign_up, context);
        }


        else if(!mConfirm_passward.getText().toString().equals(mPassward.getText().toString())){

            mConfirm_passward_layout.setErrorEnabled(true);
            mPassward_layout.setErrorEnabled(true);
            mConfirm_passward_layout.setError(context.getString(R.string.TextWatcher_pass_dont_match));
            mPassward_layout.setError(context.getString(R.string.TextWatcher_pass_dont_match));
            StatesForButtons.disable_login_button(sign_up, context);
        }

        else {
            mEmail_layout.setErrorEnabled(false);
            mPassward_layout.setErrorEnabled(false);
            mConfirm_passward_layout.setErrorEnabled(false);
            StatesForButtons.enable_login_button(sign_up, context);
        }
    }
}