package com.k.ecomapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class Admin_User extends AppCompatActivity implements View.OnClickListener{

    private DBhandler handler;
    Context context;
    private SQLiteDatabase DB;
    ListView listViewUser;
    List<User_Master> userList;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);
        setTitle("User Management");
        context = getApplicationContext();

        listViewUser = (ListView) findViewById(R.id.listViewUser);
        userList = new ArrayList<>();

        findViewById(R.id.buttonAddUser).setOnClickListener(this);

        handler = new DBhandler(context);
        DB = handler.getReadableDatabase();

        ShowAllUser();
    }

    public void ShowAllUser(){

        Cursor cur = DB.rawQuery("SELECT * FROM "+handler.USER_MASTER, null );

        if(cur.moveToFirst()){
            do{
                userList.add(new
                        User_Master(
                                cur.getInt(0),
                                cur.getString(1),
                                cur.getString(2),
                                cur.getString(3)
                ));

            }while(cur.moveToNext());
        }
        cur.close();
        adapter = new UserAdapter(this,R.layout.list_layout_user,userList);
        listViewUser.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        adduser();
    }
    private void adduser(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Admin_User.this);
        LayoutInflater inflater = LayoutInflater.from(Admin_User.this);
        View view = inflater.inflate(R.layout.adduser,null);
        builder.setView(view);

        final EditText Name = view.findViewById(R.id.AddUserTextName);
        final EditText Pass = view.findViewById(R.id.AddUserTextPass);

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.AddUserButtonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_name = Name.getText().toString();
                String u_pass = Pass.getText().toString();
                //getting the current time for joining date
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String RegDate = sdf.format(cal.getTime());

                String sql = "INSERT INTO "+handler.USER_MASTER+" ("+
                        handler.u_name+","+handler.u_pass+","+handler.reg_date+") VALUES(?,?,?);";

                DB.execSQL(sql,new String[]{u_name,u_pass,RegDate});

                reload();

                Toast.makeText(context, "User Added Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }
    protected void reload() {

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
        }
        cur.close();
        //ShowAllUser();
        adapter.notifyDataSetChanged();
        /*
        Intent intent = getIntent();
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        */
    }
}

