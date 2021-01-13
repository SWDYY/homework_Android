package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import MyHander.setDialogHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Activity {
    private Button btnLogin;//登录按钮
    private EditText user_name;
    private EditText user_password;
    private Activity self = this;
    private database db;

    private Handler handler = new setDialogHandler(Login.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置组件
        this.setContentView(R.layout.login);
        btnLogin = (Button) findViewById(R.id.login);//控件与代码绑定
        btnLogin.setOnClickListener(new ButtonListener());//使用点击事件
        user_name = (EditText) findViewById(R.id.username);//控件与代码绑定
        user_password = (EditText) findViewById(R.id.password);//控件与代码绑定
        DBapplication dBapplication=(DBapplication) getApplication();
        db=dBapplication.getDB();
    }

    class ButtonListener implements View.OnClickListener {

        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loginCheck();
                }
            }).start();
        }
    }

    private void loginCheck() {
        try {
            if (String.valueOf(user_name.getText()).equals("")) {
                user_password.setText("");
                Message message = handler.obtainMessage();
                message.obj = "ERROR," + "用户名输入为空";
                handler.sendMessage(message);
            } else if (String.valueOf(user_password.getText()).equals("")) {
                user_name.setText("");
                Message message = handler.obtainMessage();
                message.obj = "ERROR," + "密码输入为空";
                handler.sendMessage(message);
            } else {
                JSONArray results = db.executeFind("login", "user_name", "'" + String.valueOf(user_name.getText()) + "'","login");
                if (results == null || results.length() == 0) {
                    user_password.setText("");
                    Message message = handler.obtainMessage();
                    message.obj = "ERROR," + "查询结果为空";
                    handler.sendMessage(message);
                } else {
                    JSONObject result = new JSONObject(String.valueOf(results.get(0)));
                    if (result.getString("user_password").equals(String.valueOf(user_password.getText()))) {
                        if (result.getString("authority").equals("manager")) {
                            user_password.setText("");
                            Intent intent = new Intent();
                            intent.setClass(Login.this, shopkeeperCircleMainUI.class);
                            startActivity(intent);
                        } else if (result.getString("authority").equals("shopkeeper")) {
                            user_password.setText("");
                            Intent intent = new Intent();
                            intent.setClass(Login.this, shopkeeperCircleMainUI.class);
                            intent.putExtra("user_name", String.valueOf(user_name.getText()));
                            startActivity(intent);
                        } else if (result.getString("authority").equals("employee")) {
                            user_password.setText("");
                            Intent intent = new Intent();
                            intent.setClass(Login.this, employeeCircleMainUI.class);
                            intent.putExtra("user_name", String.valueOf(user_name.getText()));
                            startActivity(intent);
                        }
                    } else {
                        user_password.setText("");
                        Message message = handler.obtainMessage();
                        message.obj = "ERROR," + "用户名或密码错误";
                        handler.sendMessage(message);
                    }
                }
            }
        } catch (JSONException throwables) {
            throwables.printStackTrace();
        }
    }
}
