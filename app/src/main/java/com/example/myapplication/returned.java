package com.example.myapplication;

import ButtonListener.jumpFromTo;
import Database.DBapplication;
import Database.database;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class returned extends Activity {
    private String user_name;
    private String belongToString;
    private final String[] name={"id","name","price_all","state"};
    private database db;
    private table table=new table();
    private Button ButtonHeadReturnMain;
    private Button button_head_save;
    private Button ButtonBottom_addNewOrder;
    private Button ButtonBottom_unchecked;
    private Button ButtonBottom_unpaid ;
    private Button ButtonBottom_finished;
    private Button ButtonBottom_unreturned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopkeeper_sell_unchecked);
        //从数据库提取属于哪个仓库
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        user_name=bundle.getString("user_name");//getString()返回指定key的值
        belongToString =bundle.getString("belongTo");

        button_head_save=findViewById(R.id.save);
        ButtonHeadReturnMain=findViewById(R.id.returnMain);
        ButtonBottom_addNewOrder= findViewById(R.id.radio0);
        ButtonBottom_unchecked = findViewById(R.id.radio1);
        ButtonBottom_unpaid = findViewById(R.id.radio2);
        ButtonBottom_finished = findViewById(R.id.radio3);
        ButtonBottom_unreturned = findViewById(R.id.radio4);
        ButtonBottom_addNewOrder.setOnClickListener(new jumpFromTo(this,sell_addNewOrder.class,user_name,belongToString));
        ButtonBottom_unchecked.setOnClickListener(new jumpFromTo(this,unChecked.class,user_name,belongToString));
        ButtonBottom_unpaid.setOnClickListener(new jumpFromTo(this,unPaid.class,user_name,belongToString));
        ButtonBottom_finished.setOnClickListener(new jumpFromTo(this,finished.class,user_name,belongToString));
        ButtonBottom_unreturned.setOnClickListener(new jumpFromTo(this,unReturned.class,user_name,belongToString));
        button_head_save.setVisibility(View.GONE);
        ButtonHeadReturnMain.setOnClickListener(new jumpFromTo(this,shopkeeperCircleMainUI.class,user_name,belongToString));

        TextView textView=findViewById(R.id.textView1);
        textView.setText("已退货");
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        //初始化表头
        table.initHeader(name,this,R.id.MyTableData);
        table.showData(db.executeFind(belongToString+"_order","state","'已退货'","order")
                ,this,name,R.id.MyTableData);
    }

}
