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
import ButtonListener.jumpFromTo;

public class allStock extends Activity {
    private final String[] name={"id","name","num","inprice","outprice","outprice_wholesale"};
    private database db;
    private String belongtoString="";
    private String id = null;
    private String product_name = null;
    private String num = null;
    private String inprice = null;
    private String outprice = null;
    private String outprice_wholesale = null;
    private String authorityString;
    private table myTable;
    private Button btn_stockChange;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(allStock.this);

            final AlertDialog dialog = builder.create();
            View dialogView = View.inflate(allStock.this, R.xml.add_stock, null);
            dialog.setView(dialogView);
            dialog.show();
            //设置大小
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = 800; params.height = 1600 ;
            dialog.getWindow().setAttributes(params);

            final EditText et_name = dialogView.findViewById(R.id.et_name);
            final EditText et_num = dialogView.findViewById(R.id.et_num);
            final Button btn_login = dialogView.findViewById(R.id.btn_save);
            final Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
            final EditText et_inprice=dialogView.findViewById(R.id.et_inprice);
            final EditText et_outprice=dialogView.findViewById(R.id.et_outprice);
            final EditText et_outprice_wholesale=dialogView.findViewById(R.id.outprice_wholesale);
            if (!authorityString.equals("manager")){
                et_inprice.setVisibility(View.GONE);
                et_outprice.setVisibility(View.GONE);
                et_outprice_wholesale.setVisibility(View.GONE);
            }
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    product_name = et_name.getText().toString();
                    num = et_num.getText().toString();
                    if (TextUtils.isEmpty(product_name) || TextUtils.isEmpty(num)) {
                        Toast.makeText(allStock.this, "货品名，数目不能为空!", Toast.LENGTH_SHORT).show();
                        return;
                    }else if (!num.matches("[0-9]+")){
                        Toast.makeText(allStock.this, "货品数目必须为数字组成!", Toast.LENGTH_SHORT).show();
                        return;
                    } else{
                        try {
                            //先去总仓库查找是否有这个商品
                            JSONArray find_product_repositoryAll = db.executeFind("repository_all", "name",
                                    "'"+et_name.getText().toString()+"'", "repository");
                            if (authorityString.equals("manager")){
                                if (find_product_repositoryAll.length() == 0) {
                                    inprice=String.valueOf(et_inprice.getText());
                                    outprice=String.valueOf(et_outprice.getText());
                                    outprice_wholesale=String.valueOf(et_outprice_wholesale.getText());
                                    db.executeInsert(belongtoString+"(name,num,inprice,outprice,outprice_wholesale)",
                                            "'"+ product_name +"','"+ num+"','"+inprice+"','"+outprice+"','"+outprice_wholesale+"'");
                                }else {
                                    JSONObject productRepositoryAll_jsonObject = (JSONObject) find_product_repositoryAll.get(0);
                                    int myNum=Integer.valueOf(productRepositoryAll_jsonObject.getString("num"));
                                    String newNum_repositorySelf=String.valueOf(myNum+Integer.valueOf(num));
                                    db.executeUpdate(belongtoString, "num", newNum_repositorySelf,
                                            "name", "'" + product_name + "'");
                                    myTable.clearTable(allStock.this,R.id.MyTableData);
                                    myTable.showData(db.executeFindAll(belongtoString,"repository"),allStock.this,name,R.id.MyTableData);
                                    dialog.dismiss();
                                }
                                return;
                            }else{
                                //如果总仓库没有，报错
                                if (find_product_repositoryAll.length() == 0) {
                                    Toast.makeText(allStock.this, "总仓库中没有此商品，无法进货!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                /*如果总仓库有，在自己的仓库中添加此商品*/
                                JSONObject addStock_jsonObject = (JSONObject) find_product_repositoryAll.get(0);
                                int oldNum=Integer.valueOf(addStock_jsonObject.getString("num"));
                                if (oldNum<Integer.valueOf(num)){
                                    Toast.makeText(allStock.this, "总仓库中商品数量不够，库存为"+oldNum+"无法进货!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                //更新总仓库的商品数量
                                String newNum_repositoryALl=String.valueOf(oldNum-Integer.valueOf(num));
                                db.executeUpdate("repository_all", "num", newNum_repositoryALl,
                                        "name", "'" + product_name + "'");
                                //在自己的仓库查找一下有么有，有的话更新数量，而不是增加一行
                                JSONArray find_product_repositorySelf = db.executeFind(belongtoString, "name",
                                        "'"+et_name.getText().toString()+"'", "repository");
                                if (find_product_repositorySelf.length()>0){
                                    JSONObject productMyRepository_jsonObject = (JSONObject) find_product_repositorySelf.get(0);
                                    int myNum=Integer.valueOf(productMyRepository_jsonObject.getString("num"));
                                    String newNum_repositorySelf=String.valueOf(myNum+Integer.valueOf(num));
                                    db.executeUpdate(belongtoString, "num", newNum_repositorySelf,
                                            "name", "'" + product_name + "'");
                                    myTable.clearTable(allStock.this,R.id.MyTableData);
                                    myTable.initHeader(name,allStock.this,R.id.MyTableData);
                                    myTable.showData(db.executeFindAll(belongtoString,"repository"),allStock.this,name,R.id.MyTableData);
                                    dialog.dismiss();
                                    return;
                                }
                                //自己仓库中没有，在自己仓库中增加一行
                                inprice=addStock_jsonObject.getString("inprice");
                                outprice=addStock_jsonObject.getString("outprice");
                                outprice_wholesale=addStock_jsonObject.getString("outprice_wholesale");
                                db.executeInsert(belongtoString+"(name,num,inprice,outprice,outprice_wholesale)",
                                        "'"+ product_name +"','"+ num+"','"+inprice+"','"+outprice+"','"+outprice_wholesale+"'");
                            }
                            //查找最新的id
                            JSONArray id_JSONArray = db.executeFindMAXID("customermanager", "add_customer");
                            JSONObject id_jsonObject = (JSONObject) id_JSONArray.get(0);
                            id=id_jsonObject.getString("id");
                            JSONObject jsonObject = new JSONObject(convertTOJSON());
                            myTable.addData(jsonObject, allStock.this, name, R.id.MyTableData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                }

                private String convertTOJSON() {
                    String result = "{";
                    String id_tmp = "\"id\":" + id + ",";
                    String name_tmp = "\"name\":\"" + product_name + "\",";
                    String num_tmp = "\"num\":\"" + num + "\",";
                    String inprice_tmp = "\"inprice\":\"" + inprice + "\",";
                    String outprice_tmp = "\"outprice\":\"" + outprice + "\",";
                    String outprice_wholesale_tmp = "\"outprice_wholesale\":\"" + outprice_wholesale + "\"}";
                    return result + id_tmp + name_tmp + num_tmp + inprice_tmp+outprice_tmp+outprice_wholesale_tmp;
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
        //获取共享的数据库类
        DBapplication dBapplication=(DBapplication)getApplication();
        this.db=dBapplication.getDB();
        myTable=new table();
        //初始化表头
        myTable.initHeader(name,this,R.id.MyTableData);
        //从数据库提取属于哪个仓库
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String user_name=bundle.getString("user_name");//getString()返回指定key的值
        JSONArray belongto=db.executeFind("login","user_name","'"+user_name+"'","login");
        try {
            JSONObject jsonObject= (JSONObject) belongto.get(0);
            belongtoString=jsonObject.getString("belongto");
            authorityString=jsonObject.getString("authority");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (authorityString.equals("manager")){
            belongtoString="repository_all";
            setContentView(R.layout.manager_stock);
            btn_stockChange=findViewById(R.id.product_change);
            btn_stockChange.setVisibility(View.VISIBLE);
            btn_stockChange.setOnClickListener(new jumpFromTo(this,productChange.class,user_name,belongtoString));
        }
        TextView textView=findViewById(R.id.textView1);
        textView.setText("库存");
        myTable.showData(db.executeFindAll(belongtoString,"repository"),this,name,R.id.MyTableData);
        Button btnadd = findViewById(R.id.add);//控件与代码绑定
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
