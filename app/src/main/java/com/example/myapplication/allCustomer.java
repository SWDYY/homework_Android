package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import MyHander.setDialogHandler;
import Table.MyTableTextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.TextUtils;
import Database.database;

public class allCustomer extends Activity {
    private String[] name={"id","name","phonenum","classification"};
    private database db;
    private Button btnadd;
    String username = null;
    String pwd = null;
    table table=new table();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(allCustomer.this);

            final AlertDialog dialog = builder.create();
            View dialogView = View.inflate(allCustomer.this, R.xml.customer, null);
            dialog.setView(dialogView);
            dialog.show();
//            dialog.getWindow().setLayout(900,600);

            final EditText et_name = dialogView.findViewById(R.id.et_name);
            final EditText et_pwd = dialogView.findViewById(R.id.et_num);
            final EditText et_type = dialogView.findViewById(R.id.et_type);

            final Button btn_login = dialogView.findViewById(R.id.btn_login);
            final Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = et_name.getText().toString();
                    pwd = et_pwd.getText().toString();
                    String type=et_type.getText().toString();
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
                        Toast.makeText(allCustomer.this, "用户名，手机号或类型不能为空!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        try {
                            String result = "{";
                            db.executeInsert("customermanager(name,phonenum,classification)","'"+username+"','"+
                                    pwd+"','"+type+"'");
                            String j_username = "\"name\":\"" + username + "\",";
                            String j_pwd = "\"phonenum\":\"" + pwd + "\",";
                            String j_type = "\"classification\":\"" + type + "\"}";
                            result=result+"\"id\":\"5\","+j_username+j_pwd+j_type;
                            JSONObject jsonObject = new JSONObject(result);
                            table.addData(jsonObject, allCustomer.this, name, R.id.MyTableData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
//                    Toast.makeText(allCustomer.this, "用户名：" + username + "\n" + "用户手机：" + pwd, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    };
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

        //初始化表头
        table.initHeader(name,this,R.id.MyTableData);
        JSONArray allCustomer=db.executeFindAll("customermanager","customer");
        table.showData(allCustomer,this,name,R.id.MyTableData);
        btnadd = (Button) findViewById(R.id.add);//控件与代码绑定
        btnadd.setOnClickListener(new ButtonListener());//使用点击事件
    }
    class ButtonListener implements View.OnClickListener {

        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // todo
                    Message message = handler.obtainMessage();
                    message.obj = "ERROR," + "用户名输入为空";
                    handler.sendMessage(message);
                }
            }).start();
        }
    }




}


