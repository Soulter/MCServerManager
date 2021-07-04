package com.soulter.mcservermanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView mcServerList;
    private TextView aboutTv;
//    sendValue mSendValue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.open_add_server_fab);
        mcServerList = (ListView) findViewById(R.id.server_list_view);
        aboutTv = (TextView) findViewById(R.id.about_tv);

//        mSendValue = (sendValue) getApplication();  //??????????????????????????????????????????????/



        List<ServerInfoBean> serversList = getServersList();
        ServerListAdapter serverListAdapter = new ServerListAdapter(this,R.layout.item_server_list,serversList);
        mcServerList.setAdapter(serverListAdapter);

        if (serversList.size() == 0){
            aboutTv.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder addServerDialog = new AlertDialog.Builder(MainActivity.this);


                final SharedPreferences spfs = getApplicationContext().getSharedPreferences("spfs", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("spfs", Context.MODE_PRIVATE).edit();

                final View serverAddView = (LinearLayout)getLayoutInflater().inflate(R.layout.add_server_dialog,null);

                addServerDialog
                        .setView(serverAddView)
                        .setNegativeButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText serverIPInput = (EditText)serverAddView.findViewById(R.id.server_ip);
                                EditText serverPortInput = (EditText)serverAddView.findViewById(R.id.server_port);

                                String serverIpIn = serverIPInput.getText().toString();
                                String serverPortIn = serverPortInput.getText().toString();

                                if(!serverIpIn.equals("") && !serverPortIn.equals("")){
                                    int serverCount = spfs.getInt("server_count",0);
                                    Gson gson = new Gson();
                                    ServerInfoBean serverInfoBean = new ServerInfoBean(String.valueOf(serverCount+1),serverIpIn,Integer.parseInt(serverPortIn),"新的Minecraft服务器","");
                                    String serverInfoJson = gson.toJson(serverInfoBean);
                                    Log.v("lwl","logactivity  serverinfojson:"+serverInfoJson);
                                    editor.putString(String.valueOf(serverCount+1),serverInfoJson);
                                    editor.apply();
                                    editor.putInt("server_count",serverCount+1);
                                    editor.apply();
                                    ServerListAdapter serverListAdapter1 = new ServerListAdapter(MainActivity.this,R.layout.item_server_list,getServersList());
                                    mcServerList.setAdapter(serverListAdapter1);
                                    aboutTv.setVisibility(View.GONE);
                                }

                            }
                        }).show();

            }
        });

        mcServerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //TODO  GET DATA(get tag) AND COMMUNICATE WITH ACTIVITY
                ServerInfoBean serverInfoBean = (ServerInfoBean) view.getTag(R.string.server_info_bean);
                Log.v("lwl","onItemClick "+serverInfoBean.getServerIP());

                Intent intent = new Intent(MainActivity.this,ServerViewActivity.class);
                intent.putExtra("server_ip",serverInfoBean.getServerIP());
                intent.putExtra("server_port",serverInfoBean.getPort());
                startActivity(intent);
            }
        });
        mcServerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("是否删除该服务器？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ServerInfoBean serverInfoBean = (ServerInfoBean) view.getTag(R.string.server_info_bean);
                                final SharedPreferences spfs = getApplicationContext().getSharedPreferences("spfs", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("spfs", Context.MODE_PRIVATE).edit();
                                editor.remove(serverInfoBean.key);
                                editor.apply();
                                ServerListAdapter serverListAdapter1 = new ServerListAdapter(MainActivity.this,R.layout.item_server_list,getServersList());
                                mcServerList.setAdapter(serverListAdapter1);
                                aboutTv.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                return true;
            }
        });


    }

//    public interface sendValue{
//        void send(ServerInfoBean serverInfoBean);
//    }

    public List<ServerInfoBean> getServersList(){
        final SharedPreferences spfs = this.getSharedPreferences("spfs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = this.getSharedPreferences("spfs", Context.MODE_PRIVATE).edit();
        List<ServerInfoBean> list = new ArrayList<>();
        Map<String,?> map= spfs.getAll();
        Log.v("lwl","server map:"+map);
        Iterator iter = map.entrySet().iterator();

        Gson gson = new Gson();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next(); //?????
            String key = entry.getKey().toString();
            if (!key.equals("server_count")){
                String val = entry.getValue().toString();
                ServerInfoBean serverInfoBean = gson.fromJson(val,ServerInfoBean.class);
                list.add(serverInfoBean);
            }

        }

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}