package com.k.ecomapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewCatalogue extends AppCompatActivity {

    protected static final String ViewCatalogueUID = "ViewCatalogueUID";
    protected static List<Integer> Cart_list;
    SQLiteDatabase DB;
    DBhandler handler;
    Context ctx;
    ViewCatalogAdapter adapter;
    ListView catalogListView;
    List<Catalogue_Master> catalogList;
    int UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_catalogue);
        setTitle("Catalogue of Items");
        ctx = getApplication();

        catalogListView = (ListView) findViewById(R.id.listViewUserCatalog);
        handler = new DBhandler(ctx);
        DB = handler.getWritableDatabase();

        Cart_list = new ArrayList<>();
        catalogList = new ArrayList<>();
        findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transact();
            }
        });

        UID = getIntent().getIntExtra(ViewCatalogueUID,-2);

        ShowAllCatalogue();
    }

    private void ShowAllCatalogue(){

        Cursor cur = DB.rawQuery("SELECT * FROM "+handler.CATALOGUE_MASTER,null);

        if(cur.moveToFirst()){
            do{
                catalogList.add(new Catalogue_Master(
                        cur.getInt(0),
                        cur.getString(1),
                        cur.getString(2),
                        cur.getDouble(3),
                        cur.getInt(4)
                ));
            }while(cur.moveToNext());
        }
        cur.close();
        adapter = new ViewCatalogAdapter(this,R.layout.list_layout_user_catalog,catalogList);
        catalogListView.setAdapter(adapter);

    }

    private void transact(){

        Cursor cur;

        if(!Cart_list.isEmpty()) {

            for (int cid : Cart_list) {
                /*
                Need to insert in to Transaction master
                */
                cur = DB.rawQuery("SELECT "+handler.price+" FROM "+handler.CATALOGUE_MASTER+
                        " where "+handler.c_id+"="+cid+" ;",null);
                double amount;
                //int stock;
                if(cur.moveToFirst()){
                     amount = cur.getDouble(0);
                     //stock = cur.getInt(1);
                }else{
                    break;
                }
                cur.close();

                String sql1 = "INSERT INTO "+handler.TRANSACTION_MASTER+" ("+
                        handler.u_id+","+handler.c_id+","+handler.amount+","+handler.TimeStamp+") VALUES(?,?,?,?);";

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String Date = sdf.format(cal.getTime());

                DB.execSQL(sql1,new String[]{
                        String.valueOf(UID),
                        String.valueOf(cid),
                        String.valueOf(amount),
                        Date
                });


                adapter.notifyDataSetChanged();


            }

        }else{
            Toast.makeText(ctx,"Nothing to Buy?!",Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(getApplicationContext(),Home.class);
        intent.putExtra(Home.UserID,UID);
        startActivity(intent);

    }

}
