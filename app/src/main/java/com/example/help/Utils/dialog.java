package com.example.help.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.help.R;

import id.ionbit.ionalert.IonAlert;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class dialog {







    public static void show_no_wifi_Dialog(Activity activity ) {
        final IonAlert network_error= new IonAlert(activity, IonAlert.WARNING_TYPE);
        network_error.setTitleText(Resources.getSystem().getString(R.string.dialog_no_wifi_header))
                .setContentText(Resources.getSystem().getString(R.string.dialog_no_wifi_body_mul))
                .setConfirmText(Resources.getSystem().getString(R.string.dialog_no_wifi_button_ok))
                .setConfirmClickListener(new IonAlert.ClickListener() {
                    @Override
                    public void onClick(IonAlert ionAlert) {
                        network_error.dismiss();
                    }
                })
                .setCustomImage(R.drawable.black_no_wifi_icon)
                .show();

    }


    public static void show_Unknown_Error_Dialog(Activity activity, String Text){

        final  Dialog dialog=new Dialog(activity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_unknown_error_occured);
        dialog.setCancelable(false);
        Button ok_button=(Button)dialog.findViewById(R.id.unknown_error_dialog_button);
        TextView textView=dialog.findViewById(R.id.problem_text) ;
        textView.setText(Text);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }


    public static void show_Unknown_Minimal_Error_Dialog(Activity activity){


        final    IonAlert error= new IonAlert(activity, IonAlert.ERROR_TYPE);
        error.setTitleText(Resources.getSystem().getString(R.string.dialog_unknown_error_header))
                .setContentText(Resources.getSystem().getString(R.string.dialog_unknown_error_body_mul))
                .setConfirmText(Resources.getSystem().getString(R.string.dialog_no_wifi_button_ok))
                .setConfirmClickListener(new IonAlert.ClickListener() {
                    @Override
                    public void onClick(IonAlert ionAlert) {
                        error.dismiss();
                    }
                })
                .show();

    }

    public static void show_reset_pass_email_sent_Dialog(Activity activity){

        final    IonAlert email= new IonAlert(activity, IonAlert.SUCCESS_TYPE);
        email.setTitleText(Resources.getSystem().getString(R.string.dialog_email_sent_header))
                .setContentText(Resources.getSystem().getString(R.string.dialog_email_sent_body_mul))
                .setConfirmText(Resources.getSystem().getString(R.string.dialog_no_wifi_button_ok))
                .setConfirmClickListener(new IonAlert.ClickListener() {
                    @Override
                    public void onClick(IonAlert ionAlert) {
                        email.dismiss();
                    }
                })
                .show();

    }

  




}