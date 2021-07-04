package com.soulter.mcservermanager;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetDecoratedMCString {

    /*
    THE METHOD IS MADE BY SOULTER
     */

    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

    public SpannableStringBuilder getDMCS(String s){

        String tempText = "";
        String afterText = "";
        List<String> oriList = Arrays.asList(s.split("§"));
        Log.v("lwl2","oriList"+oriList.toString());
        List<String> styleList = new ArrayList<>();
        boolean isFoundDoubleS = false;

        for (String s0 : oriList){
            if (!s0.equals("")) {
                Log.v("lwl2", "s0:" + s0);
                afterText = afterText + s0.substring(1);
            }
        }
        Log.v("lwl2","afterT:"+afterText);
        spannableStringBuilder.append(afterText);


        for (String s1 : oriList){
            if (!s1.equals("")){
                if (s1.length() == 1){
                    isFoundDoubleS = true;
                    styleList.add(s1);
                }else {
                    if (isFoundDoubleS){
                        styleList.add(s1.substring(0,1));
                        isFoundDoubleS = false;
                        int begin = tempText.length();
                        tempText = tempText + s1.substring(1);
                        int end = tempText.length();
                        Log.v("lwl2","2 tT:"+tempText);
                        decorateText(begin,end,styleList);//xxaaaa
                        Log.v("lwl2","3 b:"+begin+" n:"+end);

                    }else{
                        isFoundDoubleS = false;
                        styleList.add(s1.substring(0,1));
                        int begin = tempText.length();
                        tempText = tempText + s1.substring(1);
                        int end = tempText.length();
                        Log.v("lwl2","3 tT:"+tempText);
                        decorateText(begin,end,styleList);//xxaaaa
                        Log.v("lwl2","3 b:"+begin+" n:"+end);
                        Log.v("lwl2","styleList"+styleList.toString());
                        styleList = new ArrayList<>();
                    }
                }
            }

        }
        return spannableStringBuilder;

    }

    public void decorateText(int begin,int end,List<String> styleList){
        for (String s : styleList){
            switch (s){
                case "l":
                    spannableStringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "m":
                    spannableStringBuilder.setSpan(new StrikethroughSpan(),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "n":
                    spannableStringBuilder.setSpan(new UnderlineSpan(),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "o":
                    spannableStringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "0":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "1":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0000AA")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "2":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#00AA00")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "3":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#00AAAA")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "4":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#AA0000")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "5":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#AA00AA")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "6":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFAA00")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "7":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#AAAAAA")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "8":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#555555")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "9":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#5555FF")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "a":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#55FF55")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "b":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#55FFFF")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "c":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5555")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "d":

                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF55FF")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "e":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFF55")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "f":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                case "g":
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#DDD605")),begin,end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    break;
                default:
                    break;

            }
        }

    }
}
