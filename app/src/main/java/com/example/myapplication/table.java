package com.example.myapplication;

import Table.MyTableTextView;
import android.app.Activity;
import android.graphics.Color;
import android.widget.TableLayout;
import android.widget.TableRow;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class table {
    public void initHeader(String[] name, Activity activity,int table_id) {
        //初始化标题
        TableLayout table = activity.findViewById(table_id);
        table.setStretchAllColumns(true);//自动填充空白处
        TableRow tablerow = new TableRow(activity);
        for(int j=0;j<name.length;j++){
            MyTableTextView textview = new MyTableTextView(activity, Color.RED);
            textview.setText(name[j]);
            tablerow.addView(textview);
        }
        table.addView(tablerow);
    }

    public void showData(JSONArray results,Activity activity,String[] name,int table_id){
        //初始化数据
        TableLayout table = activity.findViewById(table_id);
        table.setStretchAllColumns(true);//自动填充空白处
        for (int i=0;i<results.length();i++) {
            TableRow tablerow = new TableRow(activity);
            try {
                JSONObject jsonObject= (JSONObject) results.get(i);
                for(int j=0;j<name.length;j++){
                    MyTableTextView textview = new MyTableTextView(activity,Color.BLACK);
                    textview.setText(String.valueOf(jsonObject.get(name[j])));
                    tablerow.addView(textview);
                }
                table.addView(tablerow);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(JSONObject jsonObject,Activity activity,String[] name,int table_id){
        TableLayout table = activity.findViewById(table_id);
        TableRow tablerow = new TableRow(activity);
        for(int j=0;j<name.length;j++){
            MyTableTextView textview = new MyTableTextView(activity,Color.BLACK);
            try {
                textview.setText(String.valueOf(jsonObject.get(name[j])));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tablerow.addView(textview);
        }
        table.addView(tablerow);
    }
}
