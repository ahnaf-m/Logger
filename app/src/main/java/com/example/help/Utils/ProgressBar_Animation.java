package com.example.help.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.help.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

public class ProgressBar_Animation {


    public static void start_wave_progress_anim(Button btn , ProgressBar progressBar){

        Sprite wave = new Wave();
        btn.setText("");
        btn.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(wave);


    }


    public static void stop_wave_progress_anim(Button btn , ProgressBar progressBar, Context context){

        progressBar.setVisibility(View.GONE);
        btn.setText(context.getResources().getString(R.string.log_in_screen_login_button_text));
        btn.setClickable(true);
    }
}
