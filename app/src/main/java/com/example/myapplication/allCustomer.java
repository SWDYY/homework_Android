package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.TextUtils;

public class allCustomer extends Activity {
    private final String[] name={"id","name","phonenum","classification"};
    private database db;
    private String username = null;
    private String phone = null;
    private String type= null;
    private table table=new table();
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(allCustomer.this);

            final AlertDialog dialog = builder.create();
            View dialogView = View.inflate(allCustomer.this, R.xml.add_customer, null);
            dialog.setView(dialogView);
            dialog.show();
            //设置大小
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = 600; params.height = 800 ;
            dialog.getWindow().setAttributes(params);

            final EditText et_name = dialogView.findViewById(R.id.et_name);
            final EditText et_phone = dialogView.findViewById(R.id.et_phone);
            final Spinner spinner = dialogView.findViewById(R.id.spinner_type);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    String[] types = getResources().getStringArray(R.array.customer_type);
                    type=String.valueOf(types[pos]);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });
            final Button btn_login = dialogView.findViewById(R.id.btn_save);
            final Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = et_name.getText().toString();
                    phone = et_phone.getText().toString();
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone)) {
                        Toast.makeText(allCustomer.this, "用户名，手机号或类型不能为空!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        try {
                            String result = "{";
                            db.executeInsert("customermanager(name,phonenum,classification)","'"+username+"','"+
                                    phone +"','"+type+"'");
                            String j_username = "\"name\":\"" + username + "\",";
                            String j_pwd = "\"phonenum\":\"" + phone + "\",";
                            String j_type = "\"classification\":\"" + type + "\"}";
                            //查找最新的id
                            JSONArray id_JSONArray = db.executeFindMAXID("customermanager", "add_customer");
                            JSONObject id_jsonObject = (JSONObject) id_JSONArray.get(0);
                            String j_id = id_jsonObject.getString("id");
                            result=result+"\"id\":\""+j_id+"\","+j_username+j_pwd+j_type;
                            JSONObject jsonObject = new JSONObject(result);
                            table.addData(jsonObject, allCustomer.this, name, R.id.MyTableData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
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
        JSONArray allCustomer=db.executeFindAll("customermanager","add_customer");
        table.showData(allCustomer,this,name,R.id.MyTableData);
        Button btnadd = (Button) findViewById(R.id.add);//控件与代码绑定
        btnadd.setOnClickListener(new ButtonListener());//使用点击事件
    }
    class ButtonListener implements View.OnClickListener {

        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // todo
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                }
            }).start();
        }
    }




}


