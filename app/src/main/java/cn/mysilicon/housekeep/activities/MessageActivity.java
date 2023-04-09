package cn.mysilicon.housekeep.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.MsgAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Msg;

public class MessageActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<Msg>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //接收传递过来的数据
        Bundle bundle = getIntent().getExtras();
        Integer object = bundle.getInt("object");
        initMsgs(object); // 初始化消息数据
        Log.d("MessageActivity", "onCreate: " + object);

        //初始标题栏
        Toolbar toolbar = findViewById(R.id.tb_register_back);
        toolbar.setTitle("聊天");
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                }
            }
        });
    }
    private void initMsgs(Integer object) {
        switch (object) {
            case 0:
                Msg msg1 = new Msg("你好请问有什么需要帮助您的？", Msg.TYPE_RECEIVED);
                msgList.add(msg1);
                break;
            case 1:
                Msg msg2 = new Msg("你好，这里是全屋保洁，请问有什么需要帮助您的？", Msg.TYPE_RECEIVED);
                msgList.add(msg2);
                break;
            case 2:
                Msg msg3 = new Msg("你好，这里是家电清洁，请问有什么需要帮助您的？", Msg.TYPE_RECEIVED);
                msgList.add(msg3);
                break;
            case 3:
                Msg msg4 = new Msg("你好，这里是育儿保姆，请问有什么需要帮助您的？", Msg.TYPE_RECEIVED);
                msgList.add(msg4);
                break;
            default:
                break;
        }
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