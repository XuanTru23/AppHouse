package com.example.apphouse.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.apphouse.R;

public class LoadingProgressDialog {
    Context context;
    Dialog dialog;

    public LoadingProgressDialog(Context context) {
        this.context = context;
    }

    public void ShowDilag(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textview_progress_dialog = dialog.findViewById(R.id.textview_progress_dialog);
        textview_progress_dialog.setText(title);
        dialog.create();
        dialog.show();
    }
    public void HideDialog(){
        dialog.dismiss();
    }
}
