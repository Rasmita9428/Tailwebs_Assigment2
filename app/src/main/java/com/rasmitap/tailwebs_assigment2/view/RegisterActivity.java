package com.rasmitap.tailwebs_assigment2.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.rasmitap.tailwebs_assigment2.R;
import com.rasmitap.tailwebs_assigment2.db.DatabaseHelper;
import com.rasmitap.tailwebs_assigment2.model.LoginData;
import com.rasmitap.tailwebs_assigment2.utils.GlobalMethods;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_login_title, txt_login_desc, btn_login, txt_signup_login;

    EditText edt_email_login, edt_password_login, edt_number_login;

    private long lastClickTime = 0;
    private DatabaseHelper databaseHelper;
    LoginData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txt_login_title = findViewById(R.id.txt_login_title);
        txt_login_desc = findViewById(R.id.txt_login_desc);
        btn_login = findViewById(R.id.btn_login);
        edt_email_login = findViewById(R.id.edt_email_login);
        edt_password_login = findViewById(R.id.edt_password_login);
        edt_number_login = findViewById(R.id.edt_number_login);
        txt_signup_login = findViewById(R.id.txt_signup_login);

        btn_login.setOnClickListener(this);
        txt_signup_login.setOnClickListener(this);
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        user = new LoginData();

    }

    private long mLastClickTime = 0;

    @Override
    public void onClick(View view) {

        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {

        } else {
            switch (view.getId()) {
                case R.id.btn_login:

                    try {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (edt_email_login.getText().toString().equalsIgnoreCase("")) {
                        GlobalMethods.Dialog(RegisterActivity.this, "Please enter username");
                    } else if (edt_password_login.getText().toString().equalsIgnoreCase("")) {
                        GlobalMethods.Dialog(RegisterActivity.this, "Please enter password");
                    } else if (edt_number_login.getText().toString().equalsIgnoreCase("")) {
                        GlobalMethods.Dialog(RegisterActivity.this, "Please enter phone number");
                    } else if (edt_number_login.getText().toString().length() != 10) {
                        GlobalMethods.Dialog(RegisterActivity.this, "Please enter valid phone number");
                    } else {
                        RegisterApi(edt_email_login.getText().toString(), edt_password_login.getText().toString(), edt_number_login.getText().toString());

                    }
                    break;
                case R.id.txt_signup_login:
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

            }
        }
        lastClickTime = SystemClock.elapsedRealtime();

    }

    public void RegisterApi(final String email, final String password, final String phone) {
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        if (databaseHelper.checkUserName(email)) {
            MoveDialog(RegisterActivity.this, "Username Already Exist!");
            progressDialog.dismiss();
        } else if (databaseHelper.checkUser(email, password)) {
            MoveDialog(RegisterActivity.this, "Username and Password Already Exist!");
            progressDialog.dismiss();
        } else {
            user.setUsername(email);
            user.setPassword(password);
            user.setPhone(phone);
            databaseHelper.addUser(user);
            progressDialog.dismiss();

            MoveDialog(RegisterActivity.this, "User Register Successfully");

        }
    }

    public void MoveDialog(final Context context, String msg) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_dialog_msg = dialog.findViewById(R.id.txt_dialog_msg);
        TextView txt_dialog_ok = dialog.findViewById(R.id.txt_dialog_ok);

        txt_dialog_msg.setText(msg);
        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
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


}
