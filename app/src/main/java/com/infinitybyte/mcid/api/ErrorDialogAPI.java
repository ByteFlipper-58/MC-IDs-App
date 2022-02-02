package com.infinitybyte.mcid.api;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import com.infinitybyte.mcid.R;

import java.io.IOException;

public class ErrorDialogAPI {

    private Activity activity;
    private Context context;

    public ErrorDialogAPI(Activity myActivity) {
        activity = myActivity;
    }

    public void showErrorDialog(String ERROR, Activity activity) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        dialog.setTitle("Error")
                .setIcon(R.drawable.ic_baseline_error_24)
                .setMessage(ERROR)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setNegativeButtonIcon(activity.getDrawable(R.drawable.ic_baseline_content_copy_24))
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .setNeutralButtonIcon(activity.getDrawable(R.drawable.ic_baseline_feedback_24))
                .setNeutralButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });

        AlertDialog alert = dialog.create();
        alert.show();

        //Feedback Btn
        Button feedbackBtn = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
        Drawable feedback_icon = activity.getDrawable(R.drawable.ic_baseline_feedback_24);

        // set the bounds to place the drawable a bit right
        feedback_icon.setBounds((int) (feedback_icon.getIntrinsicWidth() * 0.5),
                0, (int) (feedback_icon.getIntrinsicWidth() * 1.5), feedback_icon.getIntrinsicHeight());
        feedbackBtn.setCompoundDrawables(feedback_icon, null, null, null);

        //Feedback Btn
        Button copyBtn = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
        Drawable copy_icon = activity.getDrawable(R.drawable.ic_baseline_content_copy_24);

        // set the bounds to place the drawable a bit right
        copy_icon.setBounds((int) (copy_icon.getIntrinsicWidth() * 0.5),
                0, (int) (copy_icon.getIntrinsicWidth() * 1.5), copy_icon.getIntrinsicHeight());
        copyBtn.setCompoundDrawables(copy_icon, null, null, null);
    }
}
