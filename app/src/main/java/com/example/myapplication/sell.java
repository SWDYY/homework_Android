package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class sell extends Activity {
    private String[] name={"id","user_name","user_password","phonenum","authority","belongto"};
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopkeeper_sell);
        TextView textView=findViewById(R.id.textView1);
        textView.setText("销售");
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        table table=new table();
        //初始化表头
        table.initHeader(name,this);
        table.showData(db.executeFindAll("login","login"),this,name);
    }
}
