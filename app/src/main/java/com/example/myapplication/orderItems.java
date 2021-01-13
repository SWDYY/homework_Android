package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;

public class orderItems extends Activity {
    private final String[] name={"product_name","num"};
    private database db;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_bottombtn_tableandtop);
        TextView textView=findViewById(R.id.textView1);
        btn_add =findViewById(R.id.add);
        btn_add.setVisibility(View.GONE);//因为没用增加功能，设置为不可见
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        table table=new table();
        //初始化表头
        table.initHeader(name,this,R.id.MyTableData);
        //从上一个activity提取属于哪个仓库
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String belongToString=bundle.getString("belongTo");//getString()返回指定key的值
        String id_String=bundle.getString("id");
        textView.setText("订单"+id_String+"详情");
        JSONArray orderItems_jsonArray=db.executeFind(belongToString+"_item_order","order_id",
                "'"+id_String+"'","item_order");
        table.showData(orderItems_jsonArray,this,name,R.id.MyTableData);
    }
}
