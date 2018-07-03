package com.k.ecomapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class DBhandler extends SQLiteOpenHelper{

    //DB name
    private static final String DB_NAME = "EcomDB.DB";

    //Table Names
    public static final String USER_MASTER = "USER_MASTER";
    public static final String CATALOGUE_MASTER = "CATALOGUE_MASTER";
    public static final String TRANSACTION_MASTER = "TRANSACTION_MASTER";

    //Columns for USER_MASTER
    public static final String u_id = "u_id";
    public static final String u_name = "u_name";
    public static final String u_pass = "u_pass";
    public static final String reg_date = "reg_date";

    //Columns for CATALOGUE_MASTER
    public static String c_id= "c_id";
    public static String c_type= "c_type";
    public static String ProductName= "ProductName";
    public static String price= "price";
    public static String stock= "stock";

    //Columns for TRANSACTION_MASTER
    public static String t_id= "t_id";
    public static String amount = "amount";
    public static String TimeStamp= "TimeStamp";


    private static final String Create_USER_MASTER = "CREATE TABLE IF NOT EXISTS " +
            USER_MASTER +"( "+ u_id + " INTEGER NOT NULL CONSTRAINT user_pk PRIMARY KEY AUTOINCREMENT, "
            +u_name+" TEXT NOT NULL, "+ u_pass +" TEXT , "+reg_date+" TEXT);";

    private static final String Create_CATALOGUE_MASTER = "CREATE TABLE IF NOT EXISTS " +
            CATALOGUE_MASTER +"( "+ c_id + " INTEGER NOT NULL CONSTRAINT catalogue_pk PRIMARY " +
            "KEY AUTOINCREMENT, "+c_type+" TEXT NOT NULL, "+ ProductName +" TEXT , "+price+" REAL," +
            stock+" INTEGER);";

    private static final String Create_TRANSACTION_MASTER = "CREATE TABLE IF NOT EXISTS " +
            TRANSACTION_MASTER +"( "+ t_id + " INTEGER NOT NULL CONSTRAINT transaction_pk" +
            " PRIMARY KEY AUTOINCREMENT, "+u_id+" INTEGER NOT NULL, "+ c_id +" INTEGER NOT NULL , "+
            amount+" INTEGER NOT NULL," +
            TimeStamp+" TEXT );";


    private static final String First_entry_user = "INSERT INTO "+USER_MASTER+" ("+
            u_name+","+u_pass+","+reg_date+") VALUES(?,?,?);";
    private static final String First_entry_catalogue = "INSERT INTO "+CATALOGUE_MASTER+" ("+
            c_type+","+ProductName+","+price+","+stock+") VALUES(?,?,?,?);";
    private static final String First_entry_transaction = "INSERT INTO "+TRANSACTION_MASTER+" ("+
            u_id+","+c_id+","+amount+","+TimeStamp+") VALUES(?,?,?,?);";


    public DBhandler(Context context){
        super(context, DB_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(Create_USER_MASTER);
        db.execSQL(Create_CATALOGUE_MASTER);
        db.execSQL(Create_TRANSACTION_MASTER);
        db.execSQL(First_entry_user,new String[]{"Admin","1","2018-06-20 10:00:00"});

        db.execSQL(First_entry_catalogue,new String[]{
                "Laptop",
                "HP-OMEN",
                "100000",
                "12"
        });
        db.execSQL(First_entry_catalogue,new String[]{
                "Laptop",
                "Apple-MACKBOOK",
                "120000",
                "2"
        });
        db.execSQL(First_entry_transaction,new String[]{
                "1",
                "1",
                "2",
                "2018-06-20 10:00:00"
        });

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + CATALOGUE_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_MASTER);
        onCreate(db);
    }

}
