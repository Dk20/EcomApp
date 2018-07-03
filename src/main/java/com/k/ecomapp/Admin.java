package com.k.ecomapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findViewById(R.id.buttonGoToUser).setOnClickListener(this);
        findViewById(R.id.buttonGoToCatalogue).setOnClickListener(this);
        findViewById(R.id.buttonGoToTransaction).setOnClickListener(this);
        findViewById(R.id.logoutAdmin).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonGoToUser:
                startActivity(new Intent(this,Admin_User.class));
                break;

            case R.id.buttonGoToCatalogue:
                startActivity(new Intent(this,Admin_catalogue.class));
                break;
            case R.id.buttonGoToTransaction:
                startActivity(new Intent(this,Admin_transaction.class));
                break;
            case R.id.logoutAdmin:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
        }
    }
}
