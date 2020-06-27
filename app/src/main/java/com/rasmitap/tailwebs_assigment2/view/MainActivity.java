package com.rasmitap.tailwebs_assigment2.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.rasmitap.tailwebs_assigment2.R;
import com.rasmitap.tailwebs_assigment2.db.DatabaseHelper;
import com.rasmitap.tailwebs_assigment2.model.Datamodel;
import com.rasmitap.tailwebs_assigment2.model.LoginData;
import com.rasmitap.tailwebs_assigment2.utils.ConstantStore;
import com.rasmitap.tailwebs_assigment2.utils.GPSTracker;
import com.rasmitap.tailwebs_assigment2.utils.GlobalMethods;
import com.rasmitap.tailwebs_assigment2.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recycler_userlist;
    RecyclerView.LayoutManager layoutManager;
    List<LoginData> ListofData = new ArrayList<>();
    FloatingActionButton fab, fab1;
    TextView toolbar_logout;
    DatabaseHelper databaseHelper;
    private long mLastClickTime = 0;
    String Username;
    GPSTracker gpsTracker;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_userlist = findViewById(R.id.recycler_userlist);
        toolbar_logout = findViewById(R.id.toolbar_logout);
        recycler_userlist.setHasFixedSize(true);
        Username = Utility.getStringSharedPreferences(getApplicationContext(), ConstantStore.UserName);
        gpsTracker = new GPSTracker(MainActivity.this);
        client = LocationServices.getFusedLocationProviderClient(this);
        databaseHelper = new DatabaseHelper(MainActivity.this);

        ListofData = databaseHelper.getAllUser();

        UserList_Adapter UserList_Adapter = new UserList_Adapter(MainActivity.this, ListofData);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
        recycler_userlist.setLayoutManager(manager);
        recycler_userlist.setAdapter(UserList_Adapter);
        UserList_Adapter.notifyDataSetChanged();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        toolbar_logout.setOnClickListener(this);
    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager lm = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_getlocation);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.show();

                TextView btn_getlocation = (TextView) dialog.findViewById(R.id.btn_getlocation);

                btn_getlocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        if (isGPSEnabled(MainActivity.this)) {
                            Intent intent = new Intent(MainActivity.this,
                                    MapActivity.class);
                            startActivity(intent);
                        } else {
                            gpsTracker.showSettingsAlert();
                            //gpsTracker.getLocation();
                        }
                    }
                });
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.windowAnimations = R.style.DialogAnimation;
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);


                break;
            case R.id.toolbar_logout:
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                openLogoutConfirmDialog();
                break;
        }
    }

    public class UserList_Adapter extends RecyclerView.Adapter<UserList_Adapter.MyHolder> {
        List<LoginData> ListofViewUser = new ArrayList<>();
        Context context;


        public UserList_Adapter(MainActivity context, List<LoginData> list) {
            this.ListofViewUser = list;
            this.context = context;
        }


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_datadetails, parent, false);

            return new MyHolder(view);
        }

        @Override
        public int getItemCount() {

            return ListofViewUser.size();

        }

        public class MyHolder extends RecyclerView.ViewHolder {

            TextView txt_name, txt_phone;

            public MyHolder(View itemView) {
                super(itemView);
                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);

            }
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            holder.txt_name.setText(ListofViewUser.get(position).getUsername());
            holder.txt_phone.setText(ListofViewUser.get(position).getPhone());

        }
    }

    public void openLogoutConfirmDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_confirm_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView txt_logout_title = (TextView) dialog.findViewById(R.id.txt_logout_title);
        TextView txt_logout_tagline = (TextView) dialog.findViewById(R.id.txt_logout_tagline);

        txt_logout_title.setText("Logout");
        txt_logout_tagline.setText("Are you sure you want to logout?");
        tv_yes.setText("Yes");
        tv_no.setText("No");

        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }

        });

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Utility.clearPreference(MainActivity.this);

                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //  permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {
            case ConstantStore.PERMISSION_CODE:
                if (GlobalMethods.isPermissionNotGranted(MainActivity.this, permissions)) {
                    GlobalMethods.whichPermisionNotGranted(MainActivity.this, permissions, grantResults);
                } else {
                    //                    mapFragment.getMapAsync(ContactUsFragment.this);
                    gpsTracker.getLocation();

                }
                break;
            default:
                break;
        }
    }

}