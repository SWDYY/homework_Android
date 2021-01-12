package com.example.myapplication;

import Database.database;
import Table.MyTableTextView;
import android.app.Activity;
import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.myapplication.R.id.*;

public class table {
    public void initHeader(String[] name, Activity activity, int table_id) {
        //初始化标题
        TableLayout table = activity.findViewById(table_id);
        table.setStretchAllColumns(true);//自动填充空白处
        TableRow tablerow = new TableRow(activity);
        for (int j = 0; j < name.length; j++) {
            MyTableTextView textview = new MyTableTextView(activity, Color.RED);
            textview.setText(name[j]);
            tablerow.addView(textview);
        }
        table.addView(tablerow);
    }

    public void showData(JSONArray results, Activity activity, String[] name, int table_id) {
        //初始化数据
        TableLayout table = activity.findViewById(table_id);
        table.setStretchAllColumns(true);//自动填充空白处
        for (int i = 0; i < results.length(); i++) {
            TableRow tablerow = new TableRow(activity);
            try {
                JSONObject jsonObject = (JSONObject) results.get(i);
                for (int j = 0; j < name.length; j++) {
                    MyTableTextView textview = new MyTableTextView(activity, Color.BLACK);
                    textview.setText(String.valueOf(jsonObject.get(name[j])));
                    tablerow.addView(textview);
                }
                table.addView(tablerow);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDataWithCheckBox(JSONArray results, Activity activity, String[] name, int table_id) {
        //初始化数据
        TableLayout table = activity.findViewById(table_id);
        table.setStretchAllColumns(true);//自动填充空白处
        if (results != null) {
            for (int i = 0; i < results.length(); i++) {
                TableRow tablerow = new TableRow(activity);
                try {
                    JSONObject jsonObject = (JSONObject) results.get(i);
                    for (int j = 0; j < name.length - 1; j++) {
                        MyTableTextView textview = new MyTableTextView(activity, Color.BLACK);
                        textview.setText(String.valueOf(jsonObject.get(name[j])));
                        tablerow.addView(textview);
                    }
                    CheckBox checkBox = new CheckBox(activity);
                    checkBox.setHeight(80);
                    tablerow.addView(checkBox);
                    table.addView(tablerow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addData(JSONObject jsonObject, Activity activity, String[] name, int table_id) {
        TableLayout table = activity.findViewById(table_id);
        TableRow tablerow = new TableRow(activity);
        for (int j = 0; j < name.length; j++) {
            MyTableTextView textview = new MyTableTextView(activity, Color.BLACK);
            try {
                textview.setText(String.valueOf(jsonObject.get(name[j])));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tablerow.addView(textview);
        }
        table.addView(tablerow);
    }

    public void selectCheckBox_checked(int table_id, Activity activity, String targetState, int checkBox_index, database db, String belongtoString) {
        TableLayout table = activity.findViewById(table_id);
        TableRow[] childs = new TableRow[table.getChildCount()];
        for (int i = 1; i < childs.length; i++) {
            childs[i] = (TableRow) table.getChildAt(i);
            TextView textViewtmp = (TextView) childs[i].getChildAt(0);
            String id = String.valueOf(textViewtmp.getText());
            CheckBox tmp_checkedBox = (CheckBox) childs[i].getChildAt(checkBox_index);
            //提取出每一行
            if (tmp_checkedBox.isChecked()) {
                db.executeUpdate(belongtoString, "state", "'" + targetState + "'", "id", id);
                if (targetState.equals("已退货")) {
                    numAdd(id, db, belongtoString);
                }
            }
        }
        table.removeAllViews();
    }

    private void numAdd(String id, database db, String belongtoString) {
        String[] belongTo_tmp = belongtoString.split("_");

        JSONArray jsonArray_orderItem = db.executeFind(belongTo_tmp[0] + "_item_order", "order_id", id, "item_order");
        JSONObject jsonObject = null;
        JSONObject jsonObject_repository_tmp=null;
        try {
            for (int i = 0; i < jsonArray_orderItem.length(); i++) {
                jsonObject = (JSONObject) jsonArray_orderItem.get(i);
                String name = jsonObject.getString("product_name");
                String num = jsonObject.getString("num");
                JSONArray jsonArray_order = db.executeFind(belongTo_tmp[0], "name", "'"+name+"'", "repository");
                jsonObject_repository_tmp = (JSONObject) jsonArray_order.get(0);
                String old_num=jsonObject_repository_tmp.getString("num");
                int new_num=Integer.valueOf(old_num)+Integer.valueOf(num);
                db.executeUpdate(belongTo_tmp[0],"num",String.valueOf(new_num),"name","'"+name+"'");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
