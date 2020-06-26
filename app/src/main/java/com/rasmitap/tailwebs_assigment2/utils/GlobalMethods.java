package com.rasmitap.tailwebs_assigment2.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.rasmitap.tailwebs_assigment2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dip Patel on 12/6/18.
 */
public class GlobalMethods {


    public static void Dialog(Context context, String msg) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_dialog_msg = dialog.findViewById(R.id.txt_dialog_msg);
        TextView txt_dialog_ok = dialog.findViewById(R.id.txt_dialog_ok);

        txt_dialog_msg.setText(msg);

//        txt_dialog_msg.setTypeface(SetFontTypeFace.setClanPro_News(context));
//        txt_dialog_ok.setTypeface(SetFontTypeFace.setClanPro_Bold(context));

        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        dialog.show();


    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupUI(View view, final Activity activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView,activity);
            }
        }
    }

    public static String changeDateFormat(String date) {
        String outputDate = null;
        SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        SimpleDateFormat input = new SimpleDateFormat("EEE MMM dd, yyyy", Locale.ENGLISH);
        try {


            Date oneWayTripDate = input.parse(date);// parse input


            //Crashlytics.logException(new Throwable("this is issue:"+oneWayTripDate.toString()));
            Log.e("", "datenewinfunction : " + date.toString());// format output
            outputDate = output.format(oneWayTripDate);
        } catch (ParseException e) {


            outputDate = date;

            e.printStackTrace();
        }
        return outputDate;
    }

}
