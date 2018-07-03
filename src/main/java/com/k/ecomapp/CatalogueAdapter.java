package com.k.ecomapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CatalogueAdapter extends ArrayAdapter<Catalogue_Master>{
    SQLiteDatabase DB;
    DBhandler handler;
    Context ctx;
    List<Catalogue_Master> catalogList;
    int layoutRes;

    public CatalogueAdapter(@NonNull Context ctx,int layoutRes,List<Catalogue_Master> catalogList){
        super(ctx, layoutRes, catalogList);
        this.ctx = ctx;

        this.handler = new DBhandler(ctx);
        DB = handler.getWritableDatabase();

        this.catalogList = catalogList;
        this.layoutRes = layoutRes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(layoutRes,null);

        TextView ProductType = view.findViewById(R.id.CatalogueProductType);
        TextView ProductName = view.findViewById(R.id.CatalogueProductName);
        TextView price = view.findViewById(R.id.CataloguePrice);
        TextView stock = view.findViewById(R.id.CatalogueStock);

        final Catalogue_Master catalog = catalogList.get(position);

        ProductName.setText("Name: "+catalog.getProductName().toString());
        ProductType.setText("Category: "+catalog.getC_type().toString()+"\n");
        price.setText("Price: Rs."+String.valueOf(catalog.getPrice()));
        stock.setText("Stock: "+String.valueOf(catalog.getStock()));

        Button Edit = view.findViewById(R.id.CatalogueEdit);
        Button Delete = view.findViewById(R.id.CatalogueDelete);




        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItem(catalog);
            }
        });



        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DB.execSQL("DELETE FROM "+handler.CATALOGUE_MASTER+" WHERE "+handler.c_id
                                +" =? ;"
                                ,new Integer[]{catalog.getC_id()});
                        reload();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




        return view;
    }





    private void updateItem(final Catalogue_Master catalog){

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        LayoutInflater inflator = LayoutInflater.from(ctx);
        View view = inflator.inflate(R.layout.dialog_update_item,null);
        builder.setView(view);

        final EditText productName = view.findViewById(R.id.editUpdateItemName);
        final EditText productType = view.findViewById(R.id.editUpdateItemType);
        final EditText productPrice = view.findViewById(R.id.editUpdateItemPrice);
        final EditText productStock = view.findViewById(R.id.editUpdateItemStock);

        productName.setText(catalog.getProductName().toString());
        productType.setText(catalog.getC_type().toString());
        productPrice.setText(String.valueOf(catalog.getPrice()));
        productStock.setText(String.valueOf(catalog.getStock()));

        Button update = view.findViewById(R.id.buttonUpdateCatalogue);

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
                String sql = "UPDATE "+handler.CATALOGUE_MASTER+
                        " SET "+handler.c_type+" =?, "
                        +handler.ProductName+" =?, "
                        +handler.price+" =?, "
                        +handler.stock+" =? WHERE "
                        +handler.c_id+" =? ;";

                DB.execSQL(sql,new String[]{type,name,price,stock,String.valueOf(catalog.getC_id())});
                Toast.makeText(ctx,"Item Updated",Toast.LENGTH_SHORT).show();
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
        }else{catalogList.clear();}
        cur.close();
        notifyDataSetChanged();

    }

}
