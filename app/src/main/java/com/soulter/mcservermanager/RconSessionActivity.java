package com.soulter.mcservermanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import nl.vv32.rcon.Rcon;

public class RconSessionActivity extends AppCompatActivity {

    private String rconPort;
    private String serverIP;
    private String rconPsw;
    private Button sendCommandBtn;
    private LinearLayout loadLayout;
    private TextView loadTv;
    private Rcon mcRcon;
    private EditText editCommand;
    private TextView responseTv;
    private String response = "";
    private ScrollView mScrollView;


    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcon_session);

        rconPort = getIntent().getStringExtra(ServerViewActivity.EXTRA_RCON_PORT);
        serverIP = getIntent().getStringExtra(ServerViewActivity.EXTRA_SERVER_IP);
        rconPsw = getIntent().getStringExtra(ServerViewActivity.EXTRA_RCON_PSW);
        loadLayout = findViewById(R.id.rcon_loading_layout);
        sendCommandBtn = findViewById(R.id.rcon_send_command);
        loadTv = findViewById(R.id.rcon_loading_tv);
        editCommand = findViewById(R.id.rcon_edit_command);
        responseTv = findViewById(R.id.rcon_response_tv);
        mScrollView = findViewById(R.id.scroll_view);

        //创建子线程
        HandlerThread thread = new HandlerThread("sendCommand Thread");
        thread.start();
        mHandler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                //这里就是子线程了！
                super.handleMessage(msg);

                switch (msg.what){
                    case 0: //启动Rcon
                        Log.v("lwl", "[Thread]start rcon");
                        try {
                            mcRcon = Rcon.open(serverIP, Integer.parseInt(rconPort));
                            if (mcRcon.authenticate(rconPsw)){
                                sendCommandBtn.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendCommandBtn.setEnabled(true);
                                    }
                                });
                                loadLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadLayout.setVisibility(View.GONE);
                                    }
                                });
                            }else {
                                loadTv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadTv.setText("信息验证失败...");
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 1: //发送Rcon
                        Log.v("lwl", "[Thread]send command");
                        try {
                            response = response+ "\n>" + msg.obj.toString();
                            responseTv.post(new Runnable() {
                                @Override
                                public void run() {
                                    responseTv.setText(""+response);
                                }
                            });
                            response = response +"\n<"+ mcRcon.sendCommand(msg.obj.toString());
                            responseTv.post(new Runnable() {
                                @Override
                                public void run() {
                                    responseTv.setText(""+response);
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(RconSessionActivity.this,"发送失败。。。",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;

                }
            }
        };

        mHandler.sendEmptyMessage(0);//发送指令给子线程，启动Rcon
        sendCommandBtn.setEnabled(false);
        sendCommandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editCommand.getText().toString().equals("")){
                    Log.v("lwl", "click->send command");

                    mHandler.obtainMessage(1,editCommand.getText().toString()).sendToTarget();//发送指令给子线程

                    editCommand.setText("");
                }
            }
        });

        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });




//        if (!rconPort.equals("")
//                && !rconPsw.equals("")
//                && !serverIP.equals("")){
//
//            new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    sendCommandBtn.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            sendCommandBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (mcRcon!=null){
//
//
//                                        String command = editCommand.getText().toString();
//                                        if (!command.equals("")){
//                                            try {
//                                                new Thread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        try {
//                                                            response = response + "\n"+mcRcon.sendCommand(editCommand.getText().toString());
//                                                            responseTv.post(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    responseTv.setText(response);
//                                                                }
//                                                            });
//                                                        }catch (Exception e){
//                                                            e.printStackTrace();
//                                                        }
//
//                                                    }
//                                                }).start();
//
//                                            }catch (Exception e){
//                                                e.printStackTrace();
//                                                Toast.makeText(RconSessionActivity.this,"发送失败。。。",Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }
//
//                                    }else{
//                                        Toast.makeText(RconSessionActivity.this,"发送失败，可能是仍在连接中。。。",Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    });
//
//                }
//            });
//        }

    }


}