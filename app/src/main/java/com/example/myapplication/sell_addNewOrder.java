package com.example.myapplication;

import Database.DBapplication;
import Database.database;
import MyHander.setDialogHandler;
import MyHander.set_Editview_noInput;
import android.app.Activity;
import android.content.Intent;
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
    private table table_orderitem;
    private Button button_findCustomer;
    private Button button_find_product;
    private Button button_save;//保存
    private Button Button_add_newOrderItem;
    private EditText edit_find_customer;
    private TextView customerClassification;
    private EditText edit_addNewProduct_name;
    private EditText edit_addNewProduct_num;
    private String outprice;
    private String price_all;
    private String authority;
    private String product_name;
    private String num;
    private setDialogHandler handler =new setDialogHandler(sell_addNewOrder.this);//设置弹窗
    private set_Editview_noInput set_editview_noInput;
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
        Button_add_newOrderItem=findViewById(R.id.add_newOrderItem_Button);

        button_findCustomer.setOnClickListener(new find_customer_buttonListener());
        button_find_product.setOnClickListener(new find_product_buttonListener());
        Button_add_newOrderItem.setOnClickListener(new add_newOrderItem());

        table_orderitem = new table();
        table_orderitem.initHeader(name, this, R.id.table_addOrder);
    }

    class find_product_buttonListener implements View.OnClickListener {
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //从数据库提取属于哪个仓库
                    Intent intent=getIntent();
                    Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
                    String user_name=bundle.getString("user_name");//getString()返回指定key的值
                    JSONArray belongto=db.executeFind("login","user_name","'"+user_name+"'","login");
                    String belongtoString="";
                    try {
                        JSONObject jsonObject= (JSONObject) belongto.get(0);
                        belongtoString=jsonObject.getString("belongto");
                        if (belongtoString.equals("all")){//@TODO  经理不一定用到
                            belongtoString="repository_all";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //去数据库查找
                    JSONArray findProduct=db.executeFind(belongtoString,"name","'"+edit_addNewProduct_name.getText()+"'","repository");
                    if (findProduct.length()==0){
                        //@TODO  弹窗不好用
                        Message message = handler.obtainMessage();
                        message.obj = "ERROR," + "未查到相关商品，请重新输入";
                        handler.sendMessage(message);
                    }else{
                        try {
                            JSONObject jsonObject= (JSONObject) findProduct.get(0);
                            if (authority.equals("retail")){
                                outprice=jsonObject.getString("outprice");
                            }else {
                                outprice=jsonObject.getString("outprice_wholesale");
                            }
                            edit_addNewProduct_name.setTextColor(Color.BLUE);
                            product_name=jsonObject.getString("name");
                            //TODO  设置不可输入
//                            set_editview_noInput=new set_Editview_noInput(sell_addNewOrder.this,edit_find_customer);
//                            Message message = set_editview_noInput.obtainMessage();
//                            set_editview_noInput.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        }
    }

    class  find_customer_buttonListener implements View.OnClickListener {
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String name="'"+String.valueOf(edit_find_customer.getText())+"'";
                    JSONArray find_name=db.executeFind("customermanager","name",name,"customer");
                    if (find_name.length()==0){
                        //@TODO  弹窗不好用
                        Message message = handler.obtainMessage();
                        message.obj = "ERROR," + "未查到相关客户，请新增客户或重新输入";
                        handler.sendMessage(message);
                    }else{
                        try {
                            JSONObject jsonObject= (JSONObject) find_name.get(0);
                            authority=jsonObject.getString("classification");
                            customerClassification.setText(authority);
                            edit_find_customer.setTextColor(Color.BLUE);
//                            set_editview_noInput=new set_Editview_noInput(sell_addNewOrder.this,edit_find_customer);
//                            Message message = set_editview_noInput.obtainMessage();
//                            set_editview_noInput.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        }
    }
    class add_newOrderItem implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String num_input=String.valueOf(edit_addNewProduct_num.getText());
            if (num_input.equals("")){
                //@TODO  弹窗不好用
                Message message = handler.obtainMessage();
                message.obj = "ERROR," + "商品数目为0，不能增加";
                handler.sendMessage(message);
            }else if (!num_input.matches("[0-9]+")){
                //@TODO  弹窗不好用
                Message message = handler.obtainMessage();
                message.obj = "ERROR," + "输入含有非法字符，请重新输入，不能增加";
                handler.sendMessage(message);
            }else{
                //TODO 数据库orderItem增加
                try {
                    num=num_input;
                    price_all=String.valueOf(Float.valueOf(outprice)*Integer.valueOf(num_input));
                    JSONObject jsonObject=new JSONObject(convertTOJSON());
                    table_orderitem.addData(jsonObject,sell_addNewOrder.this,name,R.id.table_addOrder);
                    edit_addNewProduct_name.setText("");
                    edit_addNewProduct_name.setTextColor(Color.BLACK);
                    edit_addNewProduct_num.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String convertTOJSON(){
            String result="{";
            product_name="\"product_name\":"+product_name+",";
            num="\"num\":\""+num+"\",";
            outprice="\"outprice\":\""+outprice+"\",";
            price_all="\"price_all\":\""+price_all+"\"}";
            return result+product_name+num+outprice+price_all;
        }
    }
}
