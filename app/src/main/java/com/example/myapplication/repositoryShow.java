package com.example.myapplication;

import Database.DBapplication;
import Table.MyTableTextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import Database.database;
import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class repositoryShow extends Activity {
    private LinearLayout mainLinerLayout;
    private RelativeLayout relativeLayout;
    private String[] name={"id","name","outprice"};
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout);
        mainLinerLayout = this.findViewById(R.id.MyTable);
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        //初始化表头
        initHeader();
        showData();
    }

    //绑定数据
    private void initHeader() {
        //初始化标题
//        relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_everyline, null);
        TableLayout table = findViewById(R.id.MyTableData);
        table.setStretchAllColumns(true);//自动填充空白处
        TableRow tablerow = new TableRow(repositoryShow.this);
        for(int j=0;j<name.length;j++){
            MyTableTextView textview = new MyTableTextView(repositoryShow.this);
            textview.setText(name[j]);
            tablerow.addView(textview);
        }
        table.addView(tablerow);
//        mainLinerLayout.addView(table);
    }

    public void showData(){
        //初始化内容
        JSONArray results=db.executeFindAll("repository1");
        System.out.println(getIntent().getStringExtra("host"));
        for (int i=0;i<results.length();i++) {
            try {
                JSONObject jsonObject= (JSONObject) results.get(i);
                relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.table_everyline, null);
                MyTableTextView txt;
                for(int j=0;j<name.length;j++){
                    String idString="list_1_"+(j+1);
                    int id=relativeLayout.getResources().getIdentifier(idString,"id",getApplicationContext().getPackageName());
                    txt=relativeLayout.findViewById(id);
                    txt.setText(String.valueOf(jsonObject.get(name[j])));
                }
                mainLinerLayout.addView(relativeLayout);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
