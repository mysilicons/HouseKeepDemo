package cn.mysilicon.housekeep.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.widget.TextView;

import cn.mysilicon.housekeep.R;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        TextView AddAddress = findViewById(R.id.address_add_textview);
        AddAddress.setOnClickListener(v -> {
            //跳转到添加地址页面
            Intent intent = new Intent(PersonActivity.this, AddressManagerActivity.class);
            startActivity(intent);
        });
    }
}