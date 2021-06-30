package com.soulter.mcservermanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import main.api.IServerInfo;
import main.conn.MinecraftServer;

import static com.soulter.mcservermanager.MCServerMng.minecraftServer;

public class ServerViewActivity extends AppCompatActivity{


    TextView playerNumTv;
    TextView gameModeTv;
    TextView gameVerTv;
    TextView serverNameTv;
    TextView serverHostTv;
    TextView serverDescription;
    TextView aboutTv;
    TextView noPlayersDataTv;
    ProgressBar loadingBar;
    ImageView serverFavicon;
    CardView displayFailed;
    ListView playersListView;
    View divideLineLv;

    String serverIp;
    int serverPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_view);


        aboutTv = (TextView)findViewById(R.id.about_tv);
        serverNameTv = (TextView)findViewById(R.id.basic_server_name);
        serverHostTv = (TextView)findViewById(R.id.basic_server_host);
        playerNumTv = (TextView)findViewById(R.id.basic_people);
        gameModeTv = (TextView)findViewById(R.id.basic_mode);
        gameVerTv = (TextView)findViewById(R.id.basic_version);
        serverDescription = (TextView)findViewById(R.id.basic_server_extra_description);
        loadingBar = (ProgressBar)findViewById(R.id.loading_bar);
        serverFavicon = (ImageView)findViewById(R.id.server_favicon);
        divideLineLv = (View)findViewById(R.id.divide_line_list_view);
        playersListView = (ListView)findViewById(R.id.player_list_view);
        noPlayersDataTv = (TextView)findViewById(R.id.unsupported_players_data_tv);
        loadingBar.setVisibility(View.GONE);
        displayFailed = (CardView)findViewById(R.id.result_warning);
        serverIp = getIntent().getStringExtra("server_ip");
        serverPort = getIntent().getIntExtra("server_port",-1);

        init();
    }


    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadingBar.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingBar.setVisibility(View.VISIBLE);
                        }
                    });
                    Log.v("lwl","SERVER GET "+serverIp+serverPort);
                    minecraftServer = new MinecraftServer(serverIp,serverPort,true);

                    Log.v("lwl",""+ minecraftServer.isAvailable());

                    Log.v("lwl","raw:"+ minecraftServer.getRawJSONString());


                    if (minecraftServer.isAvailable()){
                        loadingBar.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingBar.setVisibility(View.GONE);
                            }
                        });
                        Log.v("tag","version:name:"+ minecraftServer.getVersionName()+" protocol:"+ minecraftServer.getVersionProtocol());
                        playerNumTv.post(new Runnable() {
                            @Override
                            public void run() {
                                playerNumTv.setVisibility(View.VISIBLE);
                                playerNumTv.setText("人数："+ minecraftServer.getOnlinePlayer() + "/" + minecraftServer.getMaxPlayer());
                            }
                        });
                        gameModeTv.post(new Runnable() {
                            @Override
                            public void run() {
                                gameModeTv.setVisibility(View.VISIBLE);
                                gameModeTv.setText("模式：");
                            }
                        });
                        gameVerTv.post(new Runnable() {
                            @Override
                            public void run() {
                                gameVerTv.setVisibility(View.VISIBLE);
                                gameVerTv.setText("版本："+ minecraftServer.getVersionName());
                            }
                        });
                        serverNameTv.post(new Runnable() {
                            @Override
                            public void run() {
                                serverNameTv.setVisibility(View.VISIBLE);
                                serverNameTv.setText("新的Minecraft服务器");
                            }
                        });
                        serverHostTv.post(new Runnable() {
                            @Override
                            public void run() {
                                serverHostTv.setVisibility(View.VISIBLE);
                                serverHostTv.setText(serverIp+":"+serverPort);
                            }
                        });
                        serverDescription.post(new Runnable() {
                            @Override
                            public void run() {
                                serverDescription.setVisibility(View.VISIBLE);
                                IServerInfo.ExtraDescr[] extraDescrs=minecraftServer.getExtraDescription();
                                String extraText = "";
                                String defaultText = minecraftServer.getDefaultDescriptionText();
                                if (defaultText != null){
                                    defaultText = defaultText+"\n";
                                }
                                for (IServerInfo.ExtraDescr extraDescr:extraDescrs){
                                    extraText = extraText + extraDescr.text;

                                }
                                serverDescription.setText(""+defaultText+extraText);
                            }
                        });
                        serverFavicon.post(new Runnable() {
                            @Override
                            public void run() {
                                serverFavicon.setVisibility(View.VISIBLE);
                                try {
                                    byte[] decode = Base64.decode(minecraftServer.getFaviconBase64().split(",")[1], Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                                    serverFavicon.setImageBitmap(bitmap);
                                }catch (Exception e){
                                    serverFavicon.setImageResource(R.drawable.defaulticon);
                                }

                            }
                        });
                        divideLineLv.post(new Runnable() {
                            @Override
                            public void run() {
                                divideLineLv.setVisibility(View.VISIBLE);
                            }
                        });
                        playersListView.post(new Runnable() {
                            @Override
                            public void run() {
                                playersListView.setVisibility(View.VISIBLE);
                                List<List<String>> playersList = new ArrayList<>();

                                IServerInfo.Player[] players=minecraftServer.getPlayerList();
                                for (IServerInfo.Player p:players){
                                    Log.v("tag-players",p.name);
                                    List<String> itemList = new ArrayList<>();
                                    itemList.add(p.name);
                                    itemList.add(p.id);
                                    playersList.add(itemList);
                                }
                                if (minecraftServer.getOnlinePlayer() != 0 && playersList.size()==0){
                                    noPlayersDataTv.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            noPlayersDataTv.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else{
                                    PlayerListAdapter playerListAdapter = new PlayerListAdapter(ServerViewActivity.this,R.layout.item_player_list,playersList);
                                    playersListView.setAdapter(playerListAdapter);
                                }


                            }
                        });
                    }else{
                        Log.v("lwl","get failed");
                        Toast.makeText(getApplicationContext(),"获取世界信息失败了！",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Log.v("lwl","failed");

                    displayFailed.post(new Runnable() {
                        @Override
                        public void run() {
                            displayFailed.setVisibility(View.VISIBLE);
                        }
                    });
                    loadingBar.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingBar.setVisibility(View.GONE);
                        }
                    });
                    e.printStackTrace();
                }



            }
        }).start();

    }

}