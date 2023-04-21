package cn.mysilicon.housekeep.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.mysilicon.housekeep.R;


public class MainActivity extends AppCompatActivity {
    private String City;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否有权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        //读取SharedPreferences判断用户是否登录
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        Boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if (isLogin == false) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);
        City = loadCity();
        //传递数据
        Bundle bundle = new Bundle();
        bundle.putString("City", City);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home, R.id.classification, R.id.message, R.id.my)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


    //获取位置
    private String loadCity() {
        FileInputStream cityIn = null;
        BufferedReader cityReader = null;
        StringBuilder cityBuilder = new StringBuilder();
        try {
            cityIn = openFileInput("locationCity");
            cityReader = new BufferedReader(new InputStreamReader(cityIn));
            String line = "";
            while ((line = cityReader.readLine()) != null) {
                cityBuilder.append(line);
            }
        } catch (IOException e) {
            //如果没有文件，就创建一个文件
            try {
                FileOutputStream cityOut = openFileOutput("locationCity", Context.MODE_PRIVATE);
                BufferedWriter cityWriter = new BufferedWriter(new OutputStreamWriter(cityOut));
                cityWriter.write("未定位");
                cityWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();

            }


            e.printStackTrace();
        } finally {
            if (cityReader != null) {
                try {
                    cityReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cityBuilder.toString();
    }
}

