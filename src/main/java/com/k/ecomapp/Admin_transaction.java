package com.k.ecomapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Admin_transaction extends AppCompatActivity {

    private DBhandler handler;
    private SQLiteDatabase DB;
    Context ctx;
    List<Transaction_Master> transactionList;
    TransactionAdapter adapter;
    ListView listViewTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transaction);
        setTitle("Transaction Management");

        ctx = getApplicationContext();

        listViewTransaction = findViewById(R.id.listViewTransaction);
        transactionList = new ArrayList<>();

        handler = new DBhandler(ctx);
        DB = handler.getWritableDatabase();

        ShowAllTransactions();

    }

    private void ShowAllTransactions(){

        Cursor cur = DB.rawQuery("SELECT * FROM "+handler.TRANSACTION_MASTER+" order by "+
                handler.t_id+" DESC ;",null);

        if(cur.moveToFirst()){
            do{
                transactionList.add(new Transaction_Master(
                        cur.getInt(0),
                        cur.getInt(1),
                        cur.getInt(2),
                        cur.getDouble(3),
                        cur.getString(4)
                ));
            }while(cur.moveToNext());
        }
        cur.close();
        adapter = new TransactionAdapter(this,R.layout.list_layout_transaction,transactionList);
        listViewTransaction.setAdapter(adapter);

    }
}
