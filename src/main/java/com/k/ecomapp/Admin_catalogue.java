package com.k.ecomapp;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Admin_catalogue extends AppCompatActivity {

    private DBhandler handler;
    private SQLiteDatabase DB;
    Context ctx;
    List<Catalogue_Master> catalogList;
    CatalogueAdapter adapter;
    ListView listViewCatalogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catalogue);
        setTitle("Catalogue Management");

        ctx = getApplicationContext();

        listViewCatalogue = findViewById(R.id.listViewCatalogue);
        catalogList = new ArrayList<>();

        handler = new DBhandler(ctx);
        DB = handler.getWritableDatabase();

        findViewById(R.id.buttonAddItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });

        ShowAllItems();
    }

    private void ShowAllItems(){

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
        adapter = new CatalogueAdapter(this,R.layout.list_layout_catalogue,catalogList);
        listViewCatalogue.setAdapter(adapter);
    }


    private void additem(){


        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_catalogue.this);
        LayoutInflater inflator = LayoutInflater.from(Admin_catalogue.this);
        View view = inflator.inflate(R.layout.additem,null);
        builder.setView(view);

        final EditText productName = view.findViewById(R.id.editAddItemName);
        final EditText productType = view.findViewById(R.id.editAddItemType);
        final EditText productPrice = view.findViewById(R.id.editAddItemPrice);
        final EditText productStock = view.findViewById(R.id.editAddItemStock);

        Button update = view.findViewById(R.id.buttonAddCatalogue);

        final AlertDialog dialog = builder.create();
        dialog.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = productType.getText().toString().trim();
                String name = productName.getText().toString().trim();
                String price = productPrice.getText().toString().trim();
                String stock = productStock.getText().toString().trim();
                if (type.isEmpty()) {
                    productType.setError("Type can't be blank");
                    productType.requestFocus();
                    return;
                }
                if (name.isEmpty()) {
                    productName.setError("Name can't be blank");
                    productName.requestFocus();
                    return;
                }
                if (price.isEmpty()) {
                    productPrice.setError("Yo can't give things for free!");
                    productPrice.requestFocus();
                    return;
                }
                if (stock.isEmpty()) {
                    productStock.setError("Stock can't be blank");
                    productStock.requestFocus();
                    return;
                }
                String sql = "INSERT INTO "+handler.CATALOGUE_MASTER+" ("+
                        handler.c_type+","+handler.ProductName+","+handler.price+","+handler.stock+
                        ") VALUES(?,?,?,?);";
                DB.execSQL(sql,new String[]{type,name,price,stock});
                Toast.makeText(ctx,"Item Added",Toast.LENGTH_SHORT).show();
                reload();
                dialog.dismiss();
            }
        });

    }

    private void reload(){
        Cursor cur = DB.rawQuery("SELECT * FROM "+handler.CATALOGUE_MASTER,null);

        if(cur.moveToFirst()){
            catalogList.clear();
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
        adapter.notifyDataSetChanged();
    }
}
