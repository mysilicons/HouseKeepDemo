package cn.mysilicon.housekeep.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import cn.mysilicon.housekeep.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("id");

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Integer user_id = sharedPreferences.getInt("user_id", 0);

        TextInputEditText comment_detail = findViewById(R.id.tv_comment);
        Button btn_comment = findViewById(R.id.btn_comment);
        btn_comment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CommentDetailActivity.this);
            builder.setTitle("提示");
            builder.setMessage("确定提交评论吗？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                String comment = comment_detail.getText().toString();
                addComment(user_id, id, comment);
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.show();
        });
    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(CommentDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        };
    };


    private void addComment(Integer userId, Integer id, String comment) {
        new Thread(() -> {
            String url = "http://你的服务器地址/comment/add?user_id=" + userId + "&service_id=" + id + "&comment=" + comment+"";
            Log.d("CommentDetailActivity", url);
            OkHttpClient client = new OkHttpClient();
            Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(RequestBody.create("", null))
                    .build();
            Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                Log.d("CommentDetailActivity", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (response.code() != 200) {
                Looper.prepare();
                Toast.makeText(CommentDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}