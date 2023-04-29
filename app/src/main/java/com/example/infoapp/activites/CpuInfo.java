package com.example.infoapp.activites;

import static android.provider.SyncStateContract.Columns.DATA;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.infoapp.R;
import com.example.infoapp.adapter.InfoAdapter;

import java.io.IOException;
import java.io.InputStream;

public class CpuInfo extends AppCompatActivity {

    //for CPU info
    ProcessBuilder processBuilder;
    String Holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArry ;


    TextView tetxView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu_info);

        getSupportActionBar().setTitle("CPU");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tetxView = findViewById(R.id.txtCPUInfo);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 101 :
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                        //means user does not give you permission to read device state so return
                        return;
                    }

                    tetxView.setText(getCPU());
                }
                }
                }


    // when our application runs on background
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
                tetxView.setText(getCPU());
    }

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

}