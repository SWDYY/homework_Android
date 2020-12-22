package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class unChecked extends Activity {
    private final String[] name={"id","name","price_all","state","stateChange"};
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopkeeper_sell_unchecked);
        TextView textView=findViewById(R.id.textView1);
        textView.setText("待审核");
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        table table=new table();
        //初始化表头
        table.initHeader(name,this,R.id.MyTableData);
        //从数据库提取属于哪个仓库
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String user_name=bundle.getString("user_name");//getString()返回指定key的值
        JSONArray belongto=db.executeFind("login","user_name","'"+user_name+"'","login");
        String belongtoString="";
        try {
            JSONObject jsonObject= (JSONObject) belongto.get(0);
            belongtoString=jsonObject.getString("belongto");
            if (belongtoString.equals("all")){
                belongtoString="repository_all";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        table.showDataWithCheckBox(db.executeFindAll(belongtoString+"_order","order"),this,name,R.id.MyTableData);
    }
}
