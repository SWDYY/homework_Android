package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class shopkeeperCircleMainUI extends Activity {
    private String name;
    private Button button_sell;
    private Button button_allStocks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        name=bundle.getString("user_name");//getString()返回指定key的值

        setContentView(R.layout.shopkeepercircle);
        button_sell=findViewById(R.id.bt3);//销售
        button_allStocks=findViewById(R.id.bt1);//货品详情
        button_sell.setOnClickListener(new ButtonListener(sell.class));
        button_allStocks.setOnClickListener(new ButtonListener(allStock.class));
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
                    intent.setClass(shopkeeperCircleMainUI.this, toActivity);
                    intent.putExtra("user_name",name);
                    startActivity(intent);
                }
            }).start();
        }
    }


}
