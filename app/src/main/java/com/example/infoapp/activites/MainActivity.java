package com.example.infoapp.activites;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.icu.text.IDNA;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.infoapp.Model.Information;
import com.example.infoapp.R;
import com.example.infoapp.adapter.InfoAdapter;
import com.example.infoapp.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    TextView textViewBrand;
    private FusedLocationProviderClient client;



    ArrayList<Information> data = new ArrayList<>();
    ActivityMainBinding binding ;
    Information d1 ,d2 , d3,d4,d5,d6,d7,d8,d9,d10,d11,d12;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCPU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CpuInfo.class);
                startActivity(intent);
            }
        });


        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,locationActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("INFO APP - SIDDHARTH");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 101);

        }

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 101);

        }



        d1 = new Information("Brand Name",Build.BRAND);
        d2 = new Information("Model", Build.MODEL);
        d3 = new Information("API level", ""+Build.VERSION.SDK_INT);
        d4 = new Information("Android Release", Build.VERSION.RELEASE);
        d5 = new Information("RAM", getRAM());
        d6 = new Information("DiskSpace", getDiskSize(this));

        d7 = new Information("IpAddress",getIpAddress());

        d9 = new Information("IS Camera Available",""+getCamera(this));

        data.add(d1);
        data.add(d2);
        data.add(d3);
        data.add(d4);
        data.add(d5);
        data.add(d6);
        data.add(d7);
//        data.add(d8);
        data.add(d9);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
//
//          String no=  telephonyManager.getImei();
//            d10 = new Information("IMEI", no);
//            data.add(d10);
//        }


        InfoAdapter adapter = new InfoAdapter(data,this);
        binding.listRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManger = new LinearLayoutManager(this);
        binding.listRecyclerView.setLayoutManager(layoutManger);

    }


    public String getRAM(){
       Runtime runtime = Runtime.getRuntime();

       long TotalRam = runtime.maxMemory()/(1024*1024);
       long freeRam  = runtime.freeMemory()/(1024*1024);
        return ("Total :"+TotalRam+"MB"+ "  Free :"+freeRam+"MB");

    }


    public String getDiskSize(Context context){
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        return new String("" + totalMemory/(1024*1024*1024) + "GB");
    }



    public String getIpAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i(TAG, "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }


    public boolean getCamera(Context context){
        /** Check if this device has a camera */

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            // get Camera parameters
            return true;

        }

        return false;
    }
}




