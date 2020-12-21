package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import MyHander.MyHandler;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sell_addNewOrder extends Activity {
    private database db;
    private Button button_findCustomer;
    private Button button_find_product;
    private Button button_save;//保存
    private EditText edit_find_customer;
    private TextView customerClassification;
    private EditText edit_addNewProduct_name;
    private EditText edit_addNewProduct_num;
    private MyHandler handler =new MyHandler(sell_addNewOrder.this);

    private String[] name = {"product_name", "num", "outprice", "price_all"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopkeeper_sell);
        TextView textView = findViewById(R.id.textView1);
        textView.setText("销售");
        //获取共享的数据库类
        DBapplication dBapplication = (DBapplication) getApplication();
        this.db = dBapplication.getDB();
        //绑定按钮
        button_findCustomer = findViewById(R.id.find_customerButton);
        button_find_product = findViewById(R.id.find_product_Button);
        button_save = findViewById(R.id.save);
        edit_find_customer = findViewById(R.id.find_customer);
        customerClassification = findViewById(R.id.textview_customerClassification);
        edit_addNewProduct_name=findViewById(R.id.edit_addNewProduct_name);
        edit_addNewProduct_num=findViewById(R.id.edit_addNewProduct_num);

        button_findCustomer.setOnClickListener(new find_buttonListener());
        table table_orderitem = new table();
        table_orderitem.initHeader(name, this, R.id.table_addOrder);
    }

    class find_buttonListener implements View.OnClickListener {
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String name="'"+String.valueOf(edit_find_customer.getText())+"'";
                    JSONArray find_name=db.executeFind("customermanager","name",name,"customer");
                    if (find_name.length()==0){
                        Message message = handler.obtainMessage();
                        message.obj = "ERROR," + "未查到相关客户，请重新输入";
                        handler.sendMessage(message);
                    }else{
                        try {
                            JSONObject jsonObject= (JSONObject) find_name.get(0);
                            edit_addNewProduct_name.setText(jsonObject.getString("name"));
                            edit_addNewProduct_name.setEnabled(false);
                            edit_addNewProduct_name.setTextColor(Color.BLUE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        }
    }
}
