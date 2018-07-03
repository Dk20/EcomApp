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
import android.widget.TextView;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction_Master> {
    SQLiteDatabase DB;
    DBhandler handler;
    Context ctx;
    List<Transaction_Master> transactionList;
    int layoutRes;

    public TransactionAdapter(@NonNull Context ctx, int layoutRes,List<Transaction_Master> transactionList){
        super(ctx,layoutRes,transactionList);
        this.ctx = ctx;
        this.handler = new DBhandler(ctx);
        DB = handler.getWritableDatabase();

        this.transactionList = transactionList;
        this.layoutRes = layoutRes;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(layoutRes,null);

        TextView U_ID = view.findViewById(R.id.TransactionUid);
        TextView C_ID = view.findViewById(R.id.TransactionCid);
        TextView amount = view.findViewById(R.id.TransactionAmount);
        TextView timestamp = view.findViewById(R.id.TransactionTimeStamp);

        final Transaction_Master transaction = transactionList.get(position);

        Cursor cur = DB.rawQuery("select "+handler.u_name+" from "+handler.USER_MASTER+" where " +
                handler.u_id+"="+transaction.getU_id(),null);

        String name="",item="";
        if(cur.moveToFirst()){
            name = cur.getString(0);
        }
        cur = DB.rawQuery("select "+handler.ProductName+" from "+handler.CATALOGUE_MASTER+" where " +
                handler.c_id+"="+transaction.getC_id(),null);
        if(cur.moveToFirst()){
            item = cur.getString(0);
        }
        cur.close();

        U_ID.setText("User: "+name);
        C_ID.setText("Item Name: "+item);
        amount.setText("Price: "+String.valueOf(transaction.getAmount()));
        timestamp.setText("TimeStamp: "+transaction.getTimeStamp());


        Button Delete = view.findViewById(R.id.TransactionDelete);




        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DB.execSQL("DELETE FROM "+handler.TRANSACTION_MASTER+" WHERE "+handler.t_id
                                        +" =? ;"
                                ,new Integer[]{transaction.getT_id()});
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

    private void reload(){

        Cursor cur = DB.rawQuery("SELECT * FROM "+handler.TRANSACTION_MASTER,null);

        if(cur.moveToFirst()){
            transactionList.clear();
            do{
                transactionList.add(new Transaction_Master(
                        cur.getInt(0),
                        cur.getInt(1),
                        cur.getInt(2),
                        cur.getDouble(3),
                        cur.getString(4)
                ));
            }while(cur.moveToNext());
        }else{transactionList.clear();}
        cur.close();
        notifyDataSetChanged();
    }
}
