package cn.mysilicon.housekeep.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import cn.mysilicon.housekeep.Adapter.MsgAdapter;
import cn.mysilicon.housekeep.R;
import cn.mysilicon.housekeep.model.Msg;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";

    private List<Msg> msgList = new ArrayList<Msg>();
    private String merchant_name;
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
        merchant_name = bundle.getString("name");


        Boolean deleteConversation = false;
        String conversationID = merchant_name;
        //获取指定的会话 ID。
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(conversationID);
        // 删除指定会话。如果需要保留历史消息，`isDeleteServerMessages` 传 `false`。
        EMClient.getInstance().chatManager().deleteConversationFromServer(conversationID, EMConversation.EMConversationType.Chat, deleteConversation, new EMCallBack() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(int code, String error) {
            }
        });


        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        initView();
        initMsgs(merchant_name); // 初始化消息数据
        initListener();
    }


    private void initListener() {
        send.setOnClickListener(v -> {
            String content = inputText.getText().toString();
            // 创建一条文本消息，`content` 为消息文字内容，`conversationId` 为会话 ID，在单聊时为对端用户 ID、群聊时为群组 ID，聊天室时为聊天室 ID。
            EMMessage message = EMMessage.createTextSendMessage(content, merchant_name.toString());
            // 发送消息。
            EMClient.getInstance().chatManager().sendMessage(message);
            // 发送消息时可以设置 `EMCallBack` 的实例，获得消息发送的状态。可以在该回调中更新消息的显示状态。例如消息发送失败后的提示等等。
            message.setMessageStatusCallback(new EMCallBack() {
                @Override
                public void onSuccess() {
                    // 发送消息成功
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    handler.sendEmptyMessage(1);
                    handler.sendEmptyMessage(2);
                    handler.sendEmptyMessage(3);
                }

                @Override
                public void onError(int code, String error) {
                    // 发送消息失败
                    Looper.prepare();
                    Toast.makeText(MessageActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onProgress(int progress, String status) {

                }
            });
        });

        EMMessageListener msgListener = new EMMessageListener() {
            // 收到消息，遍历消息队列，解析和显示。
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    String content = message.getBody().toString();
                    content = content.replace("txt:\"", "");
                    content = content.substring(0, content.length() - 1);
                    Msg msg = new Msg(content, Msg.TYPE_RECEIVED);
                    msgList.add(msg);
                }
                handler.sendEmptyMessage(1);
                handler.sendEmptyMessage(2);
            }
        };
        // 注册消息监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        // 解注册消息监听
//        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    private void initView() {
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

    }

    private void initMsgs(String conversationId) {
        EMConversation.EMConversationType conversationType = EMConversation.EMConversationType.Chat; // 单聊会话类型
        // 异步方法。同步方法为 fetchHistoryMessages(String, EMConversationType, int, String, EMConversation.EMSearchDirection)。
        int pageSize = 20;
        String startMsgId = "";
        EMConversation.EMSearchDirection searchDirection = EMConversation.EMSearchDirection.UP;
        EMClient.getInstance().chatManager().asyncFetchHistoryMessage(
                conversationId,
                conversationType,
                pageSize,
                startMsgId,
                searchDirection,
                new EMValueCallBack<EMCursorResult<EMMessage>>() {
                    @Override
                    public void onSuccess(EMCursorResult<EMMessage> value) {
                        // 获取成功，value 获取了所取的消息列表
                        List<EMMessage> messages = value.getData();
                        for (EMMessage message : messages) {
                            String content = message.getBody().toString();
                            content = content.replace("txt:\"", "");
                            content = content.substring(0, content.length() - 1);
                            if (message.getFrom().equals(merchant_name)) {
                                Msg msg = new Msg(content, Msg.TYPE_RECEIVED);
                                msgList.add(msg);
                            } else if (message.getTo().equals(merchant_name)) {
                                Msg msg = new Msg(content, Msg.TYPE_SENT);
                                msgList.add(msg);
                            }

                        }
                        handler.sendEmptyMessage(1);
                        handler.sendEmptyMessage(2);
                    }

                    @Override
                    public void onError(int error, String errorMsg) {

                    }
                }
        );

    }

    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    // 当有新消息时，刷新ListView中的显示
                    adapter.notifyItemInserted(msgList.size() - 1);
                    // 将ListView定位到最后一行
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    break;
                case 3:
                    inputText.setText(""); // 清空输入框中的内容
                    break;
                default:
                    break;
            }
        }
    };

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