package com.soulter.mcservermanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlayerListAdapter extends ArrayAdapter<List<String>> {


    private int resourceId;
    private List<List<String>> playersList = new ArrayList<>();
    private Map<Integer,Bitmap> avatarsMap = new HashMap<>();

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

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.playerName=view.findViewById(R.id.item_player_name);
            viewHolder.playerName.setTypeface(viewHolder.playerName.getTypeface(), Typeface.BOLD);
            viewHolder.playerUuid=view.findViewById(R.id.item_player_uuid);
            viewHolder.playerUuid.setTypeface(viewHolder.playerUuid.getTypeface(), Typeface.BOLD);
            viewHolder.playerAvatar=view.findViewById(R.id.item_player_avatar);
            viewHolder.avatarLoadBar=view.findViewById(R.id.item_avatar_loading);


            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);



            // 获取控件实例，并调用set...方法使其显示出来
            viewHolder.playerName.setText(playersList.get(position).get(0));
            viewHolder.playerUuid.setText(playersList.get(position).get(1));

            viewHolder.avatarLoadBar.setVisibility(View.VISIBLE);
            if (avatarsMap.containsKey(position)){
                viewHolder.avatarLoadBar.setVisibility(View.GONE);
                viewHolder.playerAvatar.setVisibility(View.VISIBLE);
                viewHolder.playerAvatar.setImageBitmap(avatarsMap.get(position));

            } else{
                String avatarUrl = "https://crafthead.net/avatar/"+playersList.get(position).get(1);
                Log.v("lwl","avatarUrl:"+avatarUrl);
                Request request = new Request.Builder()
                        .url(avatarUrl)
                        .build();
                OkHttpClient okHttpClient =  new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("lwl", "onFailure: " + e.getMessage());
                        viewHolder.avatarLoadBar.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.avatarLoadBar.setVisibility(View.GONE);
                            }
                        });
                        viewHolder.playerAvatar.post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.playerAvatar.setVisibility(View.VISIBLE);
                                viewHolder.playerAvatar.setImageResource(R.drawable.steve);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            byte[] picture_bt = response.body().bytes();
                            Bitmap bitmap = BitmapFactory.decodeByteArray(picture_bt, 0, picture_bt.length);;
                            viewHolder.avatarLoadBar.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.avatarLoadBar.setVisibility(View.GONE);
                                }
                            });
                            viewHolder.playerAvatar.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.v("lwl","setBitmap:"+bitmap);
                                    viewHolder.playerAvatar.setVisibility(View.VISIBLE);
                                    viewHolder.playerAvatar.setImageBitmap(bitmap);
                                    avatarsMap.put(position,bitmap);

                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                            viewHolder.avatarLoadBar.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.avatarLoadBar.setVisibility(View.GONE);
                                }
                            });
                            viewHolder.playerAvatar.post(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.playerAvatar.setVisibility(View.VISIBLE);
                                    viewHolder.playerAvatar.setImageResource(R.drawable.steve);
                                }
                            });

                        }

                    }
                });
            }



        return view;

    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView playerName;
        TextView playerUuid;
        ImageView playerAvatar;
        ProgressBar avatarLoadBar;
    }
}
