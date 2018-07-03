package com.k.ecomapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBhandler handler;
    SQLiteDatabase DB;
    List<User_Master> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userList = new ArrayList<>();
        handler = new DBhandler(getApplicationContext());
        DB = handler.getReadableDatabase();

    }

    public void OnEnter(View view){

        EditText user = (EditText)findViewById(R.id.user);
        EditText pass = (EditText)findViewById(R.id.password);

        String u = user.getText().toString();
        String p = pass.getText().toString();

        String user_ = getString(R.string.user_), pass_ = getString(R.string.pass_);
        if(u.equals(user_) && p.equals(pass_)){
            /*
            TextView head = (TextView)findViewById(R.id.textView3);
            head.setText("Done");
            */
            Intent intent = new Intent(this,Admin.class);
            startActivity(intent);
            return;

        }
        String sql = "SELECT * FROM "+handler.USER_MASTER;
        Cursor cur = DB.rawQuery(sql,null);
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
        boolean flag=false;
        int UID = -1;
        for(User_Master u1 :userList){
            if(u1.doesMatch(u,p))
            {flag = true;
            UID = u1.getU_id();}
        }
        if(flag){
            Intent intent = new Intent(this,Home.class);
            intent.putExtra(Home.UserID,UID);
            startActivity(intent);
            return;
        }
        else{

            TextView head = (TextView)findViewById(R.id.textView3);
            String WrongCreds = getString(R.string.WrongCreds);
            head.setText(WrongCreds);
            return;
        }
    }

}
