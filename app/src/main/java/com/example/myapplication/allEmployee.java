package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class allEmployee extends Activity {
    private final String[] name={"user_name","phonenum","authority","belongto"};
    private database db;
    private Button button_add;
    private String employee_type;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(allEmployee.this);

            final AlertDialog dialog = builder.create();
            View dialogView = View.inflate(allEmployee.this, R.xml.employee_manage, null);
            dialog.setView(dialogView);
            dialog.show();
            //设置大小
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = 800; params.height = 1000 ;
            dialog.getWindow().setAttributes(params);

            final EditText et_name = dialogView.findViewById(R.id.et_name);
            final EditText et_phone = dialogView.findViewById(R.id.et_phone);
            final Spinner spinner = dialogView.findViewById(R.id.spinner_type);
            final EditText et_repository = dialogView.findViewById(R.id.et_repository);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    switch (pos){
                        case 0:
                            employee_type="shopkeeper";
                            break;
                        case 1:
                            employee_type="employee";
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                    employee_type="shopkeeper";
                }
            });
            final Button btn_confirm = dialogView.findViewById(R.id.btn_save);
            final Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = et_name.getText().toString();
                    String phone = et_phone.getText().toString();
                    String repository=et_repository.getText().toString();
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone)||TextUtils.isEmpty(repository)) {
                        Toast.makeText(allEmployee.this, "用户名，手机号,工作地点或类型不能为空!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        try {
                            String result = "{";
                            JSONArray allRepository=db.executeFind("repository_name","name",
                                    "'"+repository+"'","repository_name");
                            if (employee_type.equals("shopkeeper")){
                                if (allRepository.length()==0){
                                    db.executeInsert("repository_name(name)","'"+repository+"'");
                                    db.executeCreate(repository,"repository");
                                    db.executeCreate(repository+"_order","order");
                                    db.executeCreate(repository+"_item_order","item_order");
                                }
                            }else {
                                if (allRepository.length()==0){
                                    Toast.makeText(allEmployee.this, "店员的工作地点必须存在，请检查输入!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            db.executeInsert("login(user_name,user_password,phonenum,authority,belongto)"
                                    ,"'"+username+"','123456"+"','"+phone+"','"+employee_type+"','"+repository+"'");
                            String j_username = "\"user_name\":\"" + username + "\",";
                            String j_phonenum = "\"phonenum\":\"" + phone + "\",";
                            String j_authority="\"authority\":\"" + employee_type + "\",";
                            String j_belongto = "\"belongto\":\"" + repository + "\"}";
                            result=result+j_username+j_phonenum+j_authority+j_belongto;
                            JSONObject jsonObject = new JSONObject(result);
                            table.addData(jsonObject, allEmployee.this, name, R.id.MyTableData);
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
        button_add =findViewById(R.id.add);
        textView.setText("员工详情");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (belongtoString.equals("all")){
            table.showData(db.executeFindAll("login","login"),this,name,R.id.MyTableData);
            button_add.setOnClickListener(new ButtonListener());
        }else{
            button_add.setVisibility(View.GONE);
            table.showData(db.executeFind("login","belongto","'"+belongtoString+"'","login"),this,name,R.id.MyTableData);
        }
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
