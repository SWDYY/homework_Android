package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;

public class allCustomer extends Activity {
    private String[] name={"id","name","phonenum","classification"};
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_bottombtn_tableandtop);
        TextView textView=findViewById(R.id.textView1);
        textView.setText("客户详情");
//        mainLinerLayout = this.findViewById(R.id.MyTable);
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        table table=new table();
        //初始化表头
        table.initHeader(name,this,R.id.MyTableData);
        JSONArray allCustomer=db.executeFindAll("customermanager","customer");
        table.showData(allCustomer,this,name,R.id.MyTableData);
    }
}
