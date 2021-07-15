package com.example.help.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.core.content.res.ResourcesCompat;

import com.example.help.R;

public class StatesForButtons {


    public static void disable_login_button(Button button,Context context) {
        button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.disabled_button_text_color, null));
       button.setClickable(false);
    }

    public static void enable_login_button(Button button ,Context context) {


       button.getBackground().setColorFilter(null);
        button.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.white, null));
        button.setClickable(true);
    }



}
