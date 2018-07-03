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



public class UserAdapter extends ArrayAdapter<User_Master> {

    Context ctx;
    List<User_Master> userList;
    int layoutRes;
    SQLiteDatabase DB;
    DBhandler handler;


    public UserAdapter(@NonNull Context ctx, int layoutRes, List<User_Master> userList) {
        super(ctx, layoutRes,userList);
        this.ctx = ctx;
        this.userList = userList;
        this.layoutRes = layoutRes;
        this.handler = new DBhandler(ctx);
        this.DB = handler.getWritableDatabase();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(layoutRes, null);

        //getting particular user
        final User_Master user = userList.get(position);

        //getting views
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewPassword = view.findViewById(R.id.textViewPassword);
        TextView textViewRegDate = view.findViewById(R.id.textViewRegDate);

        //adding data to views
        textViewName.setText("User Name: "+user.getU_name());
        textViewPassword.setText("Password: "+user.getU_pass());
        textViewRegDate.setText("Registered Date:\n\n"+user.getReg_date());

        //we will use these buttons later for update and delete operation
        Button buttonDelete = view.findViewById(R.id.buttonDeleteUser);
        Button buttonEdit = view.findViewById(R.id.buttonEditUser);


        //update operation
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser(user);
            }
        });


        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM " + handler.USER_MASTER + " WHERE " + handler.u_id + " = ? ;";
                        DB.execSQL(sql, new Integer[]{user.getU_id()});
                        reloadDB();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                //Admin.reload();

            }
        });


        return view;
    }

    private void updateUser(final User_Master user) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.dialog_update_user, null);
        builder.setView(view);


        final EditText editName = view.findViewById(R.id.editUpdateName);
        final EditText editPass = view.findViewById(R.id.editUpdatePass);

        editName.setText(user.getU_name());
        editPass.setText(user.getU_pass());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String pass = editPass.getText().toString().trim();

                if (name.isEmpty()) {
                    editName.setError("Name can't be blank");
                    editName.requestFocus();
                    return;
                }

                String sql = "UPDATE "+handler.USER_MASTER+
                        " SET "+handler.u_name+" = ?, " +
                        handler.u_pass+" = ? " +
                        "WHERE "+handler.u_id+" = ?; ";

                DB.execSQL(sql, new String[]{name,pass, String.valueOf(user.getU_id()) } );
                Toast.makeText(ctx, "User Updated", Toast.LENGTH_SHORT).show();
                reloadDB();
                dialog.dismiss();
            }
        });


    }
    private void reloadDB(){
        Cursor cur = DB.rawQuery("SELECT * FROM "+handler.USER_MASTER, null );

        if(cur.moveToFirst()){
            userList.clear();
            do{
                userList.add(new
                        User_Master(
                        cur.getInt(0),
                        cur.getString(1),
                        cur.getString(2),
                        cur.getString(3)
                ));

            }while(cur.moveToNext());
        }else{userList.clear();}
        cur.close();
        notifyDataSetChanged();

    }
}