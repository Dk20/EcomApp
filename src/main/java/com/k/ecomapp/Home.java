package com.k.ecomapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    DBhandler handler;
    SQLiteDatabase DB;
    private int uid;
    List<Transaction_Master> userTransactions;
    ListView UserTransacListView;
    HomeAdapter adapter;


    protected static final String UserID = "HOMEUID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        handler = new DBhandler(getApplicationContext());
        DB = handler.getReadableDatabase();

        userTransactions = new ArrayList<>();
        UserTransacListView = findViewById(R.id.listViewUserTransaction);

        uid = getIntent().getIntExtra(UserID,-2);

        findViewById(R.id.buttonViewCatalogue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ViewCatalogue.class);
                intent.putExtra(ViewCatalogue.ViewCatalogueUID,uid);
                startActivity(intent);
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Home.this.finish();
            }
        });


        TextView greet = findViewById(R.id.greetText);


        Cursor cur  = DB.rawQuery("Select "+handler.u_name+" from "+ handler.USER_MASTER+" where " +
                        handler.u_id+"="+uid+" ;",
                null);

        String UserName;
        if(cur.moveToFirst()){
            UserName = cur.getString(0);
        }else{
            UserName = "Some thing is wrong!";
        }
        cur.close();

        greet.setText("Hi "+UserName+"!\nHere are your Previous Transaction Details:");

        ShowAllTransactions();
    }

    private void ShowAllTransactions(){
        Cursor cur = DB.rawQuery("select * from "+handler.TRANSACTION_MASTER+" where "
                +handler.u_id+"="+uid+" order by "+handler.t_id+" DESC ;",null);
        if(cur.moveToFirst()){
            do{
                userTransactions.add(new Transaction_Master(
                        cur.getInt(0),
                        cur.getInt(1),
                        cur.getInt(2),
                        cur.getDouble(3),
                        cur.getString(4)
                ));
            }while(cur.moveToNext());
        }
        cur.close();

        adapter = new HomeAdapter(this,R.layout.list_layout_home_transac,userTransactions);
        UserTransacListView.setAdapter(adapter);
    }

}
