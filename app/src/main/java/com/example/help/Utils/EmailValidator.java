package com.example.help.Utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class EmailValidator {



    public static boolean isValidEmail(EditText edit_text) {
        String target_email = edit_text.getText().toString();
        return (!TextUtils.isEmpty(target_email) && Patterns.EMAIL_ADDRESS.matcher(target_email).matches());


    }
}
