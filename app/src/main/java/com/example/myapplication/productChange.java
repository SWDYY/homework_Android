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

public class productChange extends Activity {
    private database db;
    private table myTable;
    private TableLayout productChange_tableLayout;
    private Spinner repositoryFrom_spinner;
    private Spinner repositoryTo_spinner;
    private String product_name;
    private String product_num;
    private String repositoryFrom_String;
    private String repositoryTo_String;
    private String dataNum;
    private String user_name;
    private String belongtoString;
    private EditText edit_addNewProduct_name;
    private EditText edit_addNewProduct_num;
    private Button button_find_product;
    private Button button_add_product;
    private Button button_return;
    private Button button_save;
    private set_Editview_noInput findProduct_set_editview_noInput;
    private set_Editview_canInput findProduct_set_editview_canInput;
    private setDialogHandler handler = new setDialogHandler(productChange.this);//设置弹窗


    private String[] name = {"name", "num"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取共享的数据库类
        DBapplication dBapplication = (DBapplication) getApplication();
        this.db = dBapplication.getDB();
        setContentView(R.layout.product_change);
        //从数据库提取属于哪个仓库
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        user_name=bundle.getString("user_name");//getString()返回指定key的值
        belongtoString=bundle.getString("belongTo");//getString()返回指定key的值
        //绑定所有组件
        TextView textView_head_label=findViewById(R.id.textView1);
        textView_head_label.setText("货品调配");
        TextView textView_return=findViewById(R.id.returnMain);
        textView_return.setText("返回库存详情");
        edit_addNewProduct_name = findViewById(R.id.edit_addNewProduct_name);
        edit_addNewProduct_num = findViewById(R.id.edit_addNewProduct_num);
        button_find_product = findViewById(R.id.find_product_Button);
        button_add_product = findViewById(R.id.button_addNewProduct);
        button_return=findViewById(R.id.returnMain);
        button_save=findViewById(R.id.save);
        productChange_tableLayout = findViewById(R.id.table_productChange);
        //绑定传递消息handler
        findProduct_set_editview_noInput = new set_Editview_noInput(productChange.this, edit_addNewProduct_name);
        findProduct_set_editview_canInput = new set_Editview_canInput(productChange.this, edit_addNewProduct_name);
        //为所有按钮设置监听
        button_return.setOnClickListener(new jumpFromTo(this,allStock.class,user_name,belongtoString));
        button_find_product.setOnClickListener(new find_product_buttonListener());
        button_add_product.setOnClickListener(new add_newOrderItem());
        button_save.setOnClickListener(new save());
        //设置下拉菜单内容
        repositoryFrom_spinner =findViewById(R.id.spinner_repository_from);
        repositoryTo_spinner=findViewById(R.id.spinner_repository_to);
        setSpinner();
        //初始化表头
        myTable = new table();
        myTable.initHeader(name, this, R.id.table_productChange);
    }

    class find_product_buttonListener implements View.OnClickListener {
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //去数据库查找
                    JSONArray findProduct = db.executeFind(repositoryFrom_String,
                            "name", "'" + edit_addNewProduct_name.getText() + "'", "repository");
                    if (findProduct.length() == 0) {
                        Message message = handler.obtainMessage();
                        message.obj = "ERROR," + "未查到相关商品，请重新输入";
                        handler.sendMessage(message);
                    } else {
                        try {
                            JSONObject jsonObject = (JSONObject) findProduct.get(0);
                            product_name = jsonObject.getString("name");
                            dataNum = jsonObject.getString("num");
                            Message message = findProduct_set_editview_noInput.obtainMessage();
                            findProduct_set_editview_noInput.sendMessage(message);
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
                message.obj = "ERROR," + "商品数目为空，不能增加";
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
                    product_num = num_input;
                    JSONObject jsonObject = new JSONObject(convertTOJSON());
                    myTable.addData(jsonObject, productChange.this, name, R.id.table_productChange);
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
            String product_name_tmp = "\"name\":" + product_name + ",";
            String product_num_tmp = "\"num\":\"" + product_num + "\"}";
            return result + product_name_tmp + product_num_tmp;
        }
    }

    class save implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            updateInDatabase();
        }

        private void updateInDatabase() {
            TableRow[] childs = new TableRow[productChange_tableLayout.getChildCount()];
            for (int i = 1; i < childs.length; i++) {
                childs[i] = (TableRow) productChange_tableLayout.getChildAt(i);
                //提取出每一行
                String product_name_tmp = ((TextView) childs[i].getChildAt(0)).getText().toString();//提取产品名字列
                String num_tmp = ((TextView) childs[i].getChildAt(1)).getText().toString();//提取数量列
                //读取调出仓库
                JSONArray find_product_repositoryFromSelf = db.executeFind(repositoryFrom_String, "name",
                        "'"+product_name_tmp+"'", "repository");
                //在数据库中更新调入仓库数据
                //在调入的仓库查找一下有么有，有的话更新数量，而不是增加一行
                JSONArray find_product_repositorySelf = db.executeFind(repositoryTo_String, "name",
                        "'"+product_name_tmp+"'", "repository");
                JSONObject productMyRepository_jsonObject;
                if (find_product_repositorySelf.length()>0){
                    try {
                        productMyRepository_jsonObject = (JSONObject) find_product_repositorySelf.get(0);
                        int myNum = Integer.valueOf(productMyRepository_jsonObject.getString("num"));
                        String newNum_repositoryTo=String.valueOf(myNum+Integer.valueOf(num_tmp));
                        db.executeUpdate(repositoryTo_String, "num", newNum_repositoryTo,
                                "name", "'" + product_name_tmp + "'");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        JSONObject productMyRepositoryFrom_jsonObject = (JSONObject) find_product_repositoryFromSelf.get(0);
                        //自己仓库中没有，在自己仓库中增加一行
                        String inprice=productMyRepositoryFrom_jsonObject.getString("inprice");
                        String outprice=productMyRepositoryFrom_jsonObject.getString("outprice");
                        String outprice_wholesale=productMyRepositoryFrom_jsonObject.getString("outprice_wholesale");
                        db.executeInsert(repositoryTo_String+"(name,num,inprice,outprice,outprice_wholesale)",
                                "'"+ product_name_tmp +"','"+ num_tmp+"','"+inprice+"','"+outprice+"','"+outprice_wholesale+"'");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //将库存中的数量减少
                int oldNum= 0;
                try {
                    JSONObject addStock_jsonObject = (JSONObject) find_product_repositoryFromSelf.get(0);
                    oldNum = Integer.valueOf(addStock_jsonObject.getString("num"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新总仓库的商品数量
                String newNum_repositoryFrom=String.valueOf(oldNum-Integer.valueOf(num_tmp));
                db.executeUpdate(repositoryFrom_String, "num", newNum_repositoryFrom,
                        "name", "'" + product_name_tmp + "'");
            }
            myTable.clearTable(productChange.this,R.id.table_productChange);
            myTable.initHeader(name,productChange.this,R.id.table_productChange);
        }
    }

    private void setSpinner(){
        JSONArray repository_name_JSONArray=db.executeFindAll("repository_name","repository_name");
        List<String> repository_name_list=new ArrayList<>();
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
        repositoryFrom_spinner.setAdapter(adapter);
        repositoryFrom_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                repositoryFrom_String=repository_name_list.get(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                repositoryFrom_String=repository_name_list.get(0);
            }
        });
        repositoryTo_spinner.setAdapter(adapter);
        repositoryTo_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                repositoryTo_String=repository_name_list.get(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                repositoryTo_String=repository_name_list.get(0);
            }
        });
    }
}
