package com.k.ecomapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewCatalogAdapter extends ArrayAdapter<Catalogue_Master> {

    Context ctx;
    int  layoutRes;
    SQLiteDatabase DB;
    DBhandler handler;
    List<Catalogue_Master> UserCatalogList;

    public ViewCatalogAdapter(@NonNull Context ctx, int layoutRes, List<Catalogue_Master> UserCatalogList) {
        super(ctx, layoutRes, UserCatalogList);
        this.ctx = ctx;
        this.layoutRes = layoutRes;
        this.UserCatalogList = UserCatalogList;
        handler = new DBhandler(ctx);
        DB = handler.getWritableDatabase();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(layoutRes,null);

        TextView Type = view.findViewById(R.id.CatalogType);
        TextView Name = view.findViewById(R.id.CatalogName);
        TextView price = view.findViewById(R.id.CatalogItemPrice);
        TextView stock = view.findViewById(R.id.CatalogItemStock);

        final Catalogue_Master catalog = UserCatalogList.get(position);

        Name.setText(catalog.getProductName().toString());
        Type.setText(catalog.getC_type().toString());
        price.setText("Rs."+String.valueOf(catalog.getPrice()));
        stock.setText(String.valueOf(catalog.getStock()));

        Button Buy = view.findViewById(R.id.AddToCart);


        Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(catalog.getStock()>0){

                    ViewCatalogue.Cart_list.add(catalog.getC_id());
                    int stock = catalog.getStock();

                    Toast.makeText(ctx,catalog.getProductName().toString()+
                            " Added to CART",Toast.LENGTH_SHORT).show();

                    stock--;// decrementing stock

                    String sql="UPDATE "+handler.CATALOGUE_MASTER+
                            " SET "+handler.stock+" =? WHERE "
                            +handler.c_id+" =? ;";
                    DB.execSQL(sql,new String[]{
                            String.valueOf(stock),
                            String.valueOf(catalog.getC_id())
                    });

                    reload();

                }else{
                    Toast.makeText(ctx,catalog.getProductName().toString()+
                            " is OUT of Stock! ",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    private void reload(){

        Cursor cur = DB.rawQuery("SELECT * FROM "+handler.CATALOGUE_MASTER,null);

        if(cur.moveToFirst()){
            UserCatalogList.clear();
            do{
                UserCatalogList.add(new Catalogue_Master(
                        cur.getInt(0),
                        cur.getString(1),
                        cur.getString(2),
                        cur.getDouble(3),
                        cur.getInt(4)
                ));
            }while(cur.moveToNext());
        }else{UserCatalogList.clear();}
        cur.close();

        notifyDataSetChanged();
    }
}
