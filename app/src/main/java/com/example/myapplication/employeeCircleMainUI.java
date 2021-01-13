package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class employeeCircleMainUI extends Activity {
    private String name;
    private Button button_sell;
    private Button button_allProduct;
    private Button button_allCustomer;
    private Button button_orderLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        name=bundle.getString("user_name");//getString()返回指定key的值

        setContentView(R.layout.employee_circle);
        button_sell=findViewById(R.id.bt3);//销售
        button_allProduct=findViewById(R.id.bt1);//商品详情
        button_allCustomer=findViewById(R.id.bt2);//客户详情
        button_orderLists=findViewById(R.id.bt4);//订单列表
        button_orderLists.setOnClickListener(new ButtonListener(orderLists.class));
        button_allCustomer.setOnClickListener(new ButtonListener(allCustomer.class));
        button_sell.setOnClickListener(new ButtonListener(sell_addNewOrder.class));
        button_allProduct.setOnClickListener(new ButtonListener(allProduct.class));
    }

    class ButtonListener implements View.OnClickListener {
        private Class toActivity;

        public ButtonListener(Class activity){
            this.toActivity=activity;
        }
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // todo
                    Intent intent = new Intent();
                    //setClass函数的第一个参数是一个Context对象
                    //Context是一个类，Activity是Context类的子类，也就是说，所有的Activity对象，都可以向上转型为Context对象
                    //setClass函数的第二个参数是一个Class对象，在当前场景下，应该传入需要被启动的Activity类的class对象
                    intent.setClass(employeeCircleMainUI.this, toActivity);
                    intent.putExtra("user_name",name);
                    startActivity(intent);
                }
            }).start();
        }
    }


}