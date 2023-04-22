package com.example.infoapp.activites;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
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
import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textViewBrand;
    private FusedLocationProviderClient client;



    ArrayList<Information> data = new ArrayList<>();
    ActivityMainBinding binding ;
    Information d1 ,d2 , d3,d4,d5,d6,d7,d8,d9,d10,d11,d12;


    //for CPU info
    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("INFO APP - SIDDHARTH");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 101 :
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){

                        //means user does not give you permission to read device state so return
                        return;
                    }

                    //we reach here means user grant us permission so display it on textview
//                     textViewBrand.setText("API :  "+ Build.VERSION.SDK_INT + "" + Build.VERSION.RELEASE);
//
//                    textViewBrand.setText(Build.MANUFACTURER);
//                    textViewBrand.setText(Build.MODEL);



                    d1 = new Information("Brand Name",Build.BRAND);
                    d2 = new Information("Model", Build.MODEL);
                    d3 = new Information("API level", ""+Build.VERSION.SDK_INT);
                    d4 = new Information("Android Release", Build.VERSION.RELEASE);
                    d5 = new Information("RAM", getRAM(this));
                    d6 = new Information("DiskSpace", getDiskSize());
                    d7 = new Information("CPU", getCPU());
//                    d8 = new Information("GPS", getGPSLocation());
//                    d8 = new Information("IpAddress",getIpAddress());
//                    d10 = new Information("IMEI", getIMEIno());

                    data.add(d1);
                    data.add(d2);
                    data.add(d3);
                    data.add(d4);
                    data.add(d5);
                    data.add(d6);
                    data.add(d7);
//                    data.add(d8);
//                    data.add(d9);
//                    data.add(d10);


                    InfoAdapter adapter = new InfoAdapter(data,this);
                    binding.listRecyclerView.setAdapter(adapter);

                    LinearLayoutManager layoutManger = new LinearLayoutManager(this);
                    binding.listRecyclerView.setLayoutManager(layoutManger);


                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

// when our application runs on background
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        InfoAdapter adapter = new InfoAdapter(data,this);
        binding.listRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManger = new LinearLayoutManager(this);
        binding.listRecyclerView.setLayoutManager(layoutManger);
//        textViewBrand.setText("API :  "+ Build.VERSION.SDK_INT + "" + Build.VERSION.RELEASE );
//        binding.txtBrand.setText(Build.BRAND);
    }



    public String getRAM( Context context){
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;

        return new String("" + totalMemory);
    }


    public String getDiskSize(){
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long totalDiskSpace = statFs.getBlockCount() * statFs.getBlockSize();
        return new String ("" + totalDiskSpace);
    }



//    public String getGPSLocation(){
//
//        final String[] curLoc = new String[1];
//
//        client = LocationServices.getFusedLocationProviderClient(this);
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
//            return "Access denied";
//        }
//
//        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                 curLoc[0] = location.toString();
//            }
//        });
//
//        return curLoc[0];
//    }




    public String getCPU(){

        byteArry = new byte[1024];

        try{
            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            inputStream = process.getInputStream();

            while(inputStream.read(byteArry) != -1){

                Holder = Holder + new String(byteArry);
            }

            inputStream.close();

        } catch(IOException ex){

            ex.printStackTrace();
        }

        return Holder;
    }



//    public String getIpAddress(){
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        String ipAdd = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
//        return ipAdd;
//    }

//    public String getIMEIno(){
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        return  telephonyManager.getDeviceId();
//    }




    public boolean getCamera(Context context){
        /** Check if this device has a camera */

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            // get Camera parameters
            return true;

        }

        return false;
    }


    public void getSensor(){}

}




