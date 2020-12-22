package com.example.myapplication;

import ButtonListener.jumpFromTo;
import Database.DBapplication;
import Database.database;
import MyHander.setDialogHandler;
import MyHander.set_Editview_canInput;
import MyHander.set_Editview_noInput;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class sell_addNewOrder extends Activity {
    private database db;
    private table table_orderitem;
    private TableLayout addOderItem_tableLayout;
    private Button button_findCustomer;
    private Button button_find_product;
    private Button button_save;//保存
    private Button Button_add_newOrderItem;
    private Button ButtonHeadReturnMain;
    private Button ButtonBottom_unchecked;
    private Button ButtonBottom_unpaid;
    private Button ButtonBottom_finished;
    private Button ButtonBottom_unreturned;
    private Button ButtonBottom_returned;
    private EditText edit_find_customer;
    private TextView customerClassification;
    private EditText edit_addNewProduct_name;
    private EditText edit_addNewProduct_num;
    private String user_name;
    private String belongtoString;
    private String inpriceString;
    private String outprice;
    private String price_all;
    private String authority;
    private String product_name;
    private String inputNum;
    private String dataNum;
    private List<Float> profitList = new ArrayList<>();
    private setDialogHandler handler = new setDialogHandler(sell_addNewOrder.this);//设置弹窗
    private set_Editview_noInput findCustomer_set_editview_noInput;
    private set_Editview_noInput findProduct_set_editview_noInput;
    private set_Editview_canInput findCustomer_set_editview_canInput;
    private set_Editview_canInput findProduct_set_editview_canInput;

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
        //获取传递过来的消息
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        user_name = bundle.getString("user_name");//getString()返回指定key的值
        //从数据库提取属于哪个仓库
        JSONArray belongto = db.executeFind("login", "user_name", "'" + user_name + "'", "login");
        try {
            JSONObject jsonObject = (JSONObject) belongto.get(0);
            belongtoString = jsonObject.getString("belongto");
            if (belongtoString.equals("all")) {//@TODO  经理不一定用到
                belongtoString = "repository_all";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //绑定按钮
        button_findCustomer = findViewById(R.id.find_customerButton);
        button_find_product = findViewById(R.id.find_product_Button);
        ButtonHeadReturnMain=findViewById(R.id.returnMain);
        button_save = findViewById(R.id.save);
        edit_find_customer = findViewById(R.id.find_customer);
        customerClassification = findViewById(R.id.textview_customerClassification);
        edit_addNewProduct_name = findViewById(R.id.edit_addNewProduct_name);
        edit_addNewProduct_num = findViewById(R.id.edit_addNewProduct_num);
        Button_add_newOrderItem = findViewById(R.id.add_newOrderItem_Button);
        addOderItem_tableLayout = findViewById(R.id.table_addOrder);
        ButtonBottom_unchecked = findViewById(R.id.radio1);
        ButtonBottom_unpaid = findViewById(R.id.radio2);
        ButtonBottom_finished = findViewById(R.id.radio3);
        ButtonBottom_unreturned = findViewById(R.id.radio4);
        ButtonBottom_returned = findViewById(R.id.radio5);
        //绑定传递消息handler
        findCustomer_set_editview_noInput = new set_Editview_noInput(sell_addNewOrder.this, edit_find_customer);
        findProduct_set_editview_noInput = new set_Editview_noInput(sell_addNewOrder.this, edit_addNewProduct_name);
        findCustomer_set_editview_canInput = new set_Editview_canInput(sell_addNewOrder.this, edit_find_customer);
        findProduct_set_editview_canInput = new set_Editview_canInput(sell_addNewOrder.this, edit_addNewProduct_name);
        //按钮绑定监听
        button_findCustomer.setOnClickListener(new find_customer_buttonListener());
        button_find_product.setOnClickListener(new find_product_buttonListener());
        Button_add_newOrderItem.setOnClickListener(new add_newOrderItem());
        button_save.setOnClickListener(new save());
        ButtonBottom_unchecked.setOnClickListener(new jumpFromTo(this,unChecked.class,user_name,belongtoString));
        ButtonBottom_unpaid.setOnClickListener(new jumpFromTo(this,unPaid.class,user_name,belongtoString));
        ButtonBottom_finished.setOnClickListener(new jumpFromTo(this,finished.class,user_name,belongtoString));
        ButtonBottom_unreturned.setOnClickListener(new jumpFromTo(this,unReturned.class,user_name,belongtoString));
        ButtonBottom_returned.setOnClickListener(new jumpFromTo(this,returned.class,user_name,belongtoString));
        ButtonHeadReturnMain.setOnClickListener(new jumpFromTo(this,shopkeeperCircleMainUI.class,user_name,belongtoString));
        //初始化表头
        table_orderitem = new table();
        table_orderitem.initHeader(name, this, R.id.table_addOrder);
    }

    class find_product_buttonListener implements View.OnClickListener {
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //去数据库查找
                    JSONArray findProduct = db.executeFind(belongtoString,
                            "name", "'" + edit_addNewProduct_name.getText() + "'", "repository");
                    if (findProduct.length() == 0) {
                        Message message = handler.obtainMessage();
                        message.obj = "ERROR," + "未查到相关商品，请重新输入";
                        handler.sendMessage(message);
                    } else {
                        try {
                            JSONObject jsonObject = (JSONObject) findProduct.get(0);
                            if (authority.equals("retail")) {
                                outprice = jsonObject.getString("outprice");
                            } else {
                                outprice = jsonObject.getString("outprice_wholesale");
                            }
                            dataNum = jsonObject.getString("num");
                            inpriceString = jsonObject.getString("inprice");
                            product_name = jsonObject.getString("name");

                            Message message = findCustomer_set_editview_noInput.obtainMessage();
                            findProduct_set_editview_noInput.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    class find_customer_buttonListener implements View.OnClickListener {
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String name = "'" + String.valueOf(edit_find_customer.getText()) + "'";
                    JSONArray find_name = db.executeFind("customermanager", "name", name, "customer");
                    if (find_name.length() == 0) {
                        Message message = handler.obtainMessage();
                        message.obj = "ERROR," + "未查到相关客户，请新增客户或重新输入";
                        handler.sendMessage(message);
                    } else {
                        try {
                            JSONObject jsonObject = (JSONObject) find_name.get(0);
                            authority = jsonObject.getString("classification");
                            customerClassification.setText(authority);
                            findCustomer_set_editview_noInput.sendMessage(findCustomer_set_editview_noInput.obtainMessage());

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
            String num_input = String.valueOf(edit_addNewProduct_num.getText());
            if (num_input.equals("")) {
                Message message = handler.obtainMessage();
                message.obj = "ERROR," + "商品数目为0，不能增加";
                handler.sendMessage(message);
            } else if (!num_input.matches("[0-9]+")) {
                Message message = handler.obtainMessage();
                message.obj = "ERROR," + "输入含有非法字符，请重新输入，不能增加";
                handler.sendMessage(message);
            } else if (Integer.valueOf(num_input) > Integer.valueOf(dataNum)) {
                Message message = handler.obtainMessage();
                message.obj = "ERROR," + "销售的商品数量大于库存中数量，库存数量为" + dataNum + ",请重新输入，不能增加";
                handler.sendMessage(message);
            } else {
                try {
                    inputNum = num_input;
                    price_all = String.valueOf(Float.valueOf(outprice) * Integer.valueOf(num_input));
                    profitList.add(Float.valueOf(inpriceString) * Integer.valueOf(num_input));
                    JSONObject jsonObject = new JSONObject(convertTOJSON());
                    table_orderitem.addData(jsonObject, sell_addNewOrder.this, name, R.id.table_addOrder);
                    //设置可以输入
                    edit_addNewProduct_num.setText("");
                    findProduct_set_editview_canInput.sendMessage(findProduct_set_editview_canInput.obtainMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String convertTOJSON() {
            String result = "{";
            String product_name_tmp = "\"product_name\":" + product_name + ",";
            String inputNum_tmp = "\"num\":\"" + inputNum + "\",";
            String outprice_tmp = "\"outprice\":\"" + outprice + "\",";
            String price_all_tmp = "\"price_all\":\"" + price_all + "\"}";
            return result + product_name_tmp + inputNum_tmp + outprice_tmp + price_all_tmp;
        }
    }

    class save implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            findCustomer_set_editview_canInput.sendMessage(findCustomer_set_editview_canInput.obtainMessage());
            customerClassification.setText("");
            insertInDatabase();
        }

        private void insertInDatabase() {
            //首先创建一个订单，并插入数据库
            JSONArray orderJSONArray = db.executeInsert(belongtoString + "_order(name,state,orders,price_all,num,profit)",
                    "'" + edit_find_customer.getText() + "'," + "'待审核','0',0,0,0");
            String order_id = null;
            try {
                JSONObject jsonObject = (JSONObject) orderJSONArray.get(0);
                String flag = jsonObject.getString("flag");
                if (flag.equals("0")) {
                    orderJSONArray = db.executeFindMAXID(belongtoString + "_order", "order");
                    jsonObject = (JSONObject) orderJSONArray.get(0);
                    order_id = jsonObject.getString("id");
                } else {
                    Message message = handler.obtainMessage();
                    message.obj = "ERROR," + "创建一个新订单失败";
                    handler.sendMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            float order_price_all = 0, profit = 0;
            TableRow[] childs = new TableRow[addOderItem_tableLayout.getChildCount()];
            for (int i = 1; i < childs.length; i++) {
                childs[i] = (TableRow) addOderItem_tableLayout.getChildAt(i);
                //提取出每一行
                String product_name_tmp = ((TextView) childs[i].getChildAt(0)).getText().toString();//提取产品名字列
                String num = ((TextView) childs[i].getChildAt(1)).getText().toString();//提取数量列
                String price_all = ((TextView) childs[i].getChildAt(3)).getText().toString();//提取订单项总价列
                //将每一行的利润和总售价加起来
                profit += Float.valueOf(profitList.get(i - 1));
                order_price_all += Float.valueOf(price_all);
                //将每一行插入数据库
                JSONArray jsonArray = db.executeInsert(belongtoString + "_item_order(order_id,product_name,num)",
                        "'" + order_id + "','" + product_name_tmp + "'," + num);
                int targetNum = Integer.valueOf(dataNum) - Integer.valueOf(num);
                //将库存中的数量减少
                db.executeUpdate(belongtoString, "num", String.valueOf(targetNum), "name", "'" + product_name_tmp + "'");
//                System.out.println(product_name+" "+num+" "+outprice+" "+price_all+" "+ profitList.get(i-1));
            }
            //订单中更新总价和利润
            db.executeUpdate(belongtoString + "_order", "price_all",
                    String.valueOf(order_price_all), "id", order_id);
            db.executeUpdate(belongtoString + "_order", "profit",
                    String.valueOf(profit), "id", order_id);
            addOderItem_tableLayout.removeAllViews();
            table_orderitem.initHeader(name,sell_addNewOrder.this,R.id.table_addOrder);
        }
    }
}
