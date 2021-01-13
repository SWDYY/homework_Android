package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class orderLists extends Activity {
    private final String[] name={"id","name","price_all","state"};
    private database db;
    private table table;
    private Spinner repository_spinner;
    private String belongtoString="";
    private ArrayList<String> repository_name_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        table=new table();
        //从数据库提取属于哪个仓库
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String user_name=bundle.getString("user_name");//getString()返回指定key的值
        JSONArray belongto=db.executeFind("login","user_name","'"+user_name+"'","login");
        try {
            JSONObject jsonObject= (JSONObject) belongto.get(0);
            belongtoString=jsonObject.getString("belongto");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (belongtoString.equals("all")){
            setContentView(R.layout.manager_orderlists);
            //初始化表头
            table.initHeader(name,this,R.id.MyTableData);
            repository_spinner =findViewById(R.id.spinner1);
            setSpinner();
        }else{
            setContentView(R.layout.no_bottombtn_tableandtop);
            //初始化表头
            table.initHeader(name,this,R.id.MyTableData);
            table.showData_clickable(db.executeFindAll(belongtoString+"_order","order"),
                    this,name,R.id.MyTableData,belongtoString);
        }
        Button btn_add=findViewById(R.id.add);
        btn_add.setVisibility(View.GONE);
        TextView textView=findViewById(R.id.textView1);
        textView.setText("订单列表");
    }

    private void setSpinner(){
        JSONArray repository_name_JSONArray=db.executeFindAll("repository_name","repository_name");
        repository_name_list=new ArrayList<>();
        for (int i=0;i<repository_name_JSONArray.length();i++){
            try {
                JSONObject jsonObject_tmp= (JSONObject) repository_name_JSONArray.get(i);
                repository_name_list.add(jsonObject_tmp.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SpinnerAdapter adapter = new ArrayAdapter<String>(this,R.xml.spinner_textview,repository_name_list);
        repository_spinner.setAdapter(adapter);
        repository_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                table.clearTable(orderLists.this,R.id.MyTableData);
                table.showData_clickable(db.executeFindAll(repository_name_list.get(pos) +"_order","order"),
                        orderLists.this,name,R.id.MyTableData,repository_name_list.get(pos));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
                table.showData_clickable(db.executeFindAll(repository_name_list.get(0)+"_order","order"),
                        orderLists.this,name,R.id.MyTableData,repository_name_list.get(0));
            }
        });
    }
}
