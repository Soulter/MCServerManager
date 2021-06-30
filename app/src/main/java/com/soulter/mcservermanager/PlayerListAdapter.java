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
import java.util.List;

import main.conn.MinecraftServer;

public class PlayerListAdapter extends ArrayAdapter<List<String>> {


    private int resourceId;
    private List<List<String>> playersList = new ArrayList<>();

    public PlayerListAdapter(Context context, int itemResId , List<List<String>> list ){
        super(context, itemResId, list);
        resourceId = itemResId;
        playersList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        PlayerListAdapter.ViewHolder viewHolder;
        if (convertView==null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.playerName=view.findViewById(R.id.item_player_name);
            viewHolder.playerName.setTypeface(viewHolder.playerName.getTypeface(), Typeface.BOLD);
            viewHolder.playerUuid=view.findViewById(R.id.item_player_uuid);
            viewHolder.playerUuid.setTypeface(viewHolder.playerUuid.getTypeface(), Typeface.BOLD);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(PlayerListAdapter.ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.playerName.setText(playersList.get(position).get(0));
        viewHolder.playerUuid.setText(playersList.get(position).get(1));


        return view;

    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView playerName;
        TextView playerUuid;
    }
}
