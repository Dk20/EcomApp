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
import android.widget.TextView;

import java.util.List;

public class HomeAdapter extends ArrayAdapter<Transaction_Master>{

    List<Transaction_Master> UserTransacList;
    Context ctx;
    int layoutRes;
    SQLiteDatabase DB;
    DBhandler handler;

    public HomeAdapter(@NonNull Context ctx,int layoutRes,List<Transaction_Master> UserTransacList) {
        super(ctx, layoutRes, UserTransacList);
        this.UserTransacList = UserTransacList;
        this.ctx = ctx;
        this.layoutRes = layoutRes;
        handler = new DBhandler(ctx);
        DB = handler.getReadableDatabase();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(layoutRes,null);

        TextView Name = view.findViewById(R.id.TransactionItemName);
        TextView Price = view.findViewById(R.id.TransactionItemPrice);
        TextView timestamp = view.findViewById(R.id.TransactionDate);

        final Transaction_Master transaction = UserTransacList.get(position);

        Cursor cur = DB.rawQuery("select "+handler.ProductName+" from "+handler.CATALOGUE_MASTER
                +" where "+handler.c_id+"="+transaction.getC_id()+" ;",null);
        String name;
        if(cur.moveToFirst()){
            name = cur.getString(0);
        }else{
            return view;
        }
        cur.close();

        Name.setText("Item Name: "+name);
        Price.setText("Price: Rs."+String.valueOf(transaction.getAmount()));
        timestamp.setText("Date/Time : "+transaction.getTimeStamp());

        return view;
    }
}
