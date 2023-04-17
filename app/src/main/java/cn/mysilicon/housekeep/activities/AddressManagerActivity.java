package cn.mysilicon.housekeep.activities;

import static android.os.SystemClock.sleep;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.io.IOException;

import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Address;
import cn.mysilicon.housekeep.utils.JDCityPicker;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddressManagerActivity extends AppCompatActivity {

    public static final String TAG = "AddressManagerActivity";

    TextView mTvSlect;
    TextView mTvCity;
    EditText name;
    EditText phone;
    EditText address;
    JDCityPicker mJDCityPicker;
    Address address1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);


        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("地址管理");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        name = findViewById(R.id.et_receiver);
        phone = findViewById(R.id.et_phone);
        mTvSlect = findViewById(R.id.tv_select);
        mTvCity = findViewById(R.id.tv_city_pick);
        address = findViewById(R.id.et_address);
        Button BtnSave = findViewById(R.id.btn_save_address);

        mTvSlect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgAlpha(0.7f);
                hideKeyboard(AddressManagerActivity.this);
                mJDCityPicker = new JDCityPicker(AddressManagerActivity.this, new JDCityPicker.onCitySelect() {
                    @Override
                    public void onSelect(String province, String city, String area) {
                        mTvCity.setText(province + "   " + city + "   " + area);
                    }
                });
                mJDCityPicker.showAtLocation(mTvSlect, Gravity.BOTTOM, 0, 0);

                mJDCityPicker.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        bgAlpha(1.0f);
                    }
                });
            }

        });


        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                String phoneStr = phone.getText().toString();
                String cityStr = mTvCity.getText().toString();
                String addressStr = address.getText().toString();
                String finalAddress = cityStr + addressStr;
                if (nameStr.equals("") || phoneStr.equals("") || addressStr.equals("") || cityStr.equals("")) {
                    Toast.makeText(AddressManagerActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                    Integer user_id = sharedPreferences.getInt("user_id", 0);
                    address1 = new Address(user_id, nameStr, phoneStr, finalAddress);
                    //发送地址信息到服务器
                    try {
                        sendAddress(address1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sleep(500);
                    Toast.makeText(AddressManagerActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }

    //背景变暗
    private void bgAlpha(float f) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = f;
        getWindow().setAttributes(layoutParams);
    }


    /**
     * 监听标题栏按钮点击事件.
     *
     * @param item 按钮
     * @return 结果
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendAddress(Address address) throws IOException {

        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        // 创建RequestBody对象
        Gson gson = new Gson();
        String json = gson.toJson(address);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), json);

        // 创建Request对象
        Request request = new Request.Builder()
                .url("http://mysilicon.cn/address/add")
                .post(requestBody)
                .build();

        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(AddressManagerActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d(TAG, "onResponse: " + result);
                //Toast.makeText(AddressManagerActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }

        });


    }
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

}