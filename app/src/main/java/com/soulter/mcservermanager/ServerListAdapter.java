package com.soulter.mcservermanager;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.conn.MinecraftServer;

public class ServerListAdapter extends ArrayAdapter<ServerInfoBean> {

    private int resourceId;
    private List<ServerInfoBean> serversList = new ArrayList<>();
    private Map<Integer,Boolean> statusMap = new HashMap<>();


    public ServerListAdapter(Context context, int itemResId , List<ServerInfoBean> list ){
        super(context, itemResId, list);
        resourceId = itemResId;
        serversList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.serverName=view.findViewById(R.id.item_tv_server_name);
            viewHolder.serverName.setTypeface(viewHolder.serverName.getTypeface(), Typeface.BOLD);
            viewHolder.serverIP=view.findViewById(R.id.item_tv_server_ip);
            viewHolder.serverIP.setTypeface(viewHolder.serverIP.getTypeface(), Typeface.BOLD);
            viewHolder.serverStatus = view.findViewById(R.id.item_image_server_status);
            viewHolder.loadingBar = view.findViewById(R.id.item_loading);


            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);

            // 获取控件实例，并调用set...方法使其显示出来
            viewHolder.serverName.setText(serversList.get(position).getServerName());
            viewHolder.serverIP.setText(serversList.get(position).getServerIP()+":"+serversList.get(position).getPort());
            view.setTag(R.string.server_info_bean,serversList.get(position));

            if (statusMap.containsKey(position)){
                viewHolder.loadingBar.setVisibility(View.INVISIBLE);
                if (statusMap.get(position)){
                    viewHolder.serverStatus.setImageResource(R.drawable.online);
                }else {
                    viewHolder.serverStatus.setImageResource(R.drawable.offline);
                }
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            MinecraftServer minecraftServer = new MinecraftServer(serversList.get(position).getServerIP(),serversList.get(position).getPort(),true);
                            viewHolder.loadingBar.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.loadingBar.setVisibility(View.INVISIBLE);
                                }
                            });
                            if (minecraftServer.isAvailable()){
                                Log.v("lwl","available. "+viewHolder.serverIP.getText().toString());
                                viewHolder.serverStatus.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.serverStatus.setImageResource(R.drawable.online);
                                    }
                                });
                                statusMap.put(position,true);

                            }else{

                                viewHolder.serverStatus.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewHolder.serverStatus.setImageResource(R.drawable.offline);
                                    }
                                });
                                statusMap.put(position,false);
                            }
                        }catch (Exception e){
                            Log.v("lwl","new a server failed. "+viewHolder.serverIP.getText().toString());
                            viewHolder.loadingBar.post(new Runnable() {
                                @Override
                                public void run() {

                                    viewHolder.loadingBar.setVisibility(View.INVISIBLE);
                                }
                            });

                            viewHolder.serverStatus.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.serverStatus.setImageResource(R.drawable.offline);
                                }
                            });
                        }

                    }
                }).start();
            }




        return view;

    }



    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView serverName;
        TextView serverIP;
        ImageView serverStatus;
        ProgressBar loadingBar;
    }


//    public void swapRecyclerView(List<ServerInfoBean> newServerList) {
//        serversList.clear();
//        serversList = newServerList;
//        Log.v("lwl","refresh!  newServerList:"+newServerList);
//        this.notifyDataSetChanged();
//    }
}
