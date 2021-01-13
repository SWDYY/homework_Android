package com.example.myapplication;

import Database.DBapplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import Database.database;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 显示全部货品
 */
public class allProduct extends Activity {
    private String[] name={"id","name","outprice"};
    private database db;
    private Button btn_add;
    private ArrayList<String> repository_name_list;
    private table myTable;
    private String belongtoString="";
    private Spinner repository_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        myTable=new table();
        //从数据库提取属于哪个仓库
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String user_name=bundle.getString("user_name");//getString()返回指定key的值
        JSONArray belongto_JSONArray=db.executeFind("login","user_name","'"+user_name+"'","login");
        try {
            JSONObject jsonObject= (JSONObject) belongto_JSONArray.get(0);
            belongtoString=jsonObject.getString("belongto");
            if (belongtoString.equals("all")){
                belongtoString="repository_all";
                setContentView(R.layout.manager_orderlists);
                repository_spinner =findViewById(R.id.spinner1);
                setSpinner();
            }else {
                setContentView(R.layout.no_bottombtn_tableandtop);
                myTable.showData(db.executeFindAll(belongtoString,"repository"),this,name,R.id.MyTableData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //初始化表头
        myTable.initHeader(name,this,R.id.MyTableData);
        TextView textView=findViewById(R.id.textView1);
        textView.setText("全部商品");
        btn_add =findViewById(R.id.add);
        btn_add.setVisibility(View.GONE);//因为没用增加功能// ，设置为不可见
    }

    private void setSpinner(){
        JSONArray repository_name_JSONArray=db.executeFindAll("repository_name","repository_name");
        repository_name_list=new ArrayList<>();
        repository_name_list.add("repository_all");
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
                myTable.clearTable(allProduct.this,R.id.MyTableData);
                myTable.showData_clickable(db.executeFindAll(repository_name_list.get(pos),"repository"),
                        allProduct.this,name,R.id.MyTableData,belongtoString);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
                myTable.showData_clickable(db.executeFindAll(repository_name_list.get(0),"repository"),
                        allProduct.this,name,R.id.MyTableData,belongtoString);
            }
        });
    }
}
