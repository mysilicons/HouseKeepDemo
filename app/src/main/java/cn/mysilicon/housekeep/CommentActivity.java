package cn.mysilicon.housekeep;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;

import cn.mysilicon.housekeep.Adapter.CommentAdapter;
import cn.mysilicon.housekeep.model.Comment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentActivity extends AppCompatActivity {
    private Integer user_id;
    private List<Comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getInt("user_id", 0);

        Log.d("CommentActivity", user_id.toString());
        getComment(user_id);
    }

    private void getComment(Integer user_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://你的服务器地址/comment/getCommentByUserId?user_id=" + user_id;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                Response response = null;
                String result = null;
                try {
                    response = client.newCall(request).execute();
                    result = response.body().string();
                    commentList = JSON.parseArray(result, Comment.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.code()!=200){
                    Looper.prepare();
                    Toast.makeText(CommentActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else {
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    final Handler handler = new Handler(Looper.getMainLooper()){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    initView();
                    break;
                default:
                    break;
            }
        };
    };

    private void initView() {
        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("服务详情");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recyclerView = findViewById(R.id.rv_comment);
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(commentAdapter);
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

}