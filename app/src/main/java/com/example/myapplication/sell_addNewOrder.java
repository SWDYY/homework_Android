package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class sell_addNewOrder extends Activity {
    private database db;
    private Button button_findCustomer;
    private Button button_find_product;
    private Button button_save;
    private String[] name={"product_name","num","outprice","price_all"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopkeeper_sell);
        TextView textView=findViewById(R.id.textView1);
        textView.setText("销售");
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        //绑定按钮
        button_findCustomer=findViewById(R.id.find_customerButton);
        button_find_product=findViewById(R.id.find_product_Button);
        button_save=findViewById(R.id.save);

        table table_orderitem=new table();
        table_orderitem.initHeader(name,this,R.id.table_addOrder);
    }


}
