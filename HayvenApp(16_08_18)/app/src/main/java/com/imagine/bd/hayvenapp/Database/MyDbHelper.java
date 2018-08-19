package com.imagine.bd.hayvenapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.imagine.bd.hayvenapp.Model.ResponseData;
import com.imagine.bd.hayvenapp.Model.UserInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 10/2/2017.
 */

public class MyDbHelper extends SQLiteOpenHelper {

    private static MyDbHelper mInstance = null;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "hayven_db";


    // User table name
    private static final String USER_TABLE = "USER_TABLE";

    // Response table name
    private static final String SERVER_RESPONSE_TABLE = "SERVER_RESPONSE_TABLE";



    // USER Table Columns names
    private static final String KEY_ID = "id";
    private static final String USER_ID = "USER_ID";
    private static final String DEPT = "DEPT";
    private static final String DESIGNATION = "DESIGNATION";
    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String USER_NAME = "USER_NAME";
    private static final String IMG_PATH = "IMG_PATH";


    // Rsponse Table Columns names
  //  private static final String KEY_ID = "id";
    private static final String RESPONSE = "RESPONSE";
    private static final String IS_SIGNIN = "IS_SIGNIN";
    private static final String API_TYPE = "API_TYPE";




    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MyDbHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new MyDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create User table
        String CREATE_USER_TABLE = "CREATE TABLE  IF NOT EXISTS " + USER_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_ID + " TEXT,"
                + DEPT + " TEXT," + DESIGNATION + " TEXT,"
                + USER_EMAIL + " TEXT," + USER_NAME + " TEXT,"
                + IMG_PATH + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        // create response table
        String CREATE_SERVER_RESPONSE_TABLE = "CREATE TABLE  IF NOT EXISTS " + SERVER_RESPONSE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + API_TYPE + " TEXT,"
                + IS_SIGNIN + " TEXT,"
                + RESPONSE + " TEXT" + ")";
        db.execSQL(CREATE_SERVER_RESPONSE_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SERVER_RESPONSE_TABLE);

        // Create tables again
        onCreate(db);
    }

    // check alrady exist
    public boolean checkAlreadyExist(String reciverid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select reciverId from " + USER_TABLE
                + " where reciverId=?", new String[]{reciverid});
        boolean exists = (cursor.getCount() > 0);
        db.close();
        return exists;
    }



    // insert data to User table

    public void insertUserTable(UserInfo infodata) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //  values.put(KEY_ID, infodata.getKEY_ID());
        values.put(USER_ID, infodata.getId());
        values.put(DEPT, infodata.getDept());
        values.put(DESIGNATION, infodata.getDesignation());
        values.put(USER_EMAIL, infodata.getEmail());
        values.put(USER_NAME, infodata.getFullname());
        values.put(IMG_PATH, infodata.getImg());


        db.insert(USER_TABLE, null, values);

    }



    // insert data to response table

    public void insertResponseTable(ResponseData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //  values.put(KEY_ID, infodata.getKEY_ID());
        values.put(API_TYPE, data.getApiType());
        values.put(IS_SIGNIN, data.getIsSignin());
        values.put(RESPONSE, data.getResponse());

        db.insert(SERVER_RESPONSE_TABLE, null, values);

    }


    public void updateResponsetable(ResponseData data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //  values.put(KEY_ID, infodata.getKEY_ID());
        values.put(API_TYPE, data.getApiType());
        values.put(IS_SIGNIN, data.getIsSignin());
        values.put(RESPONSE, data.getResponse());

        db.update(SERVER_RESPONSE_TABLE, values, "id="+1, null);
    }

    //  response table insert or update
    public void ResponseInsetOrUpdate(ResponseData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(API_TYPE, data.getApiType());
        values.put(IS_SIGNIN, data.getIsSignin());
        values.put(RESPONSE, data.getResponse());


        int id = getResponseID(data.getApiType());
        if (id == -1) {
            db.insert(SERVER_RESPONSE_TABLE, null, values);
            Log.e("LastConversation table", "insert");
        } else {
            db.update(SERVER_RESPONSE_TABLE, values, "tableID=?", new String[]{Integer.toString(id)});
            Log.e("LastConversation table", "update");
        }

    }


    // get id from chat history table
    public int getResponseID(String apiType) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(SERVER_RESPONSE_TABLE, new String[]{"tableID"}, "userID =?", new String[]{apiType}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("tableID"));
        return -1;
    }

    // Getting student Count
    public int getResponseCountData() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + SERVER_RESPONSE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;

    }



    // Getting response
    public String getResponse(String api_type) {
        String response = "";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from SERVER_RESPONSE_TABLE where API_TYPE='" + api_type + "'", null);
        if (cursor.moveToFirst()) {
            do {
                response = cursor.getString(cursor.getColumnIndex(RESPONSE));

            } while (cursor.moveToNext());
        }

        return response;
    }




    // Getting issignin
    public String getSigninStatus(String api_type) {
        String status = "";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from SERVER_RESPONSE_TABLE where API_TYPE='" + api_type + "'", null);
        if (cursor.moveToFirst()) {
            do {
                status = cursor.getString(cursor.getColumnIndex(IS_SIGNIN));

            } while (cursor.moveToNext());
        }

        return status;
    }

    /*
    public String getUserType(String email) {
        String name = "";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from user_table where USER_EMAIL='" + email + "'", null);
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex(USER_TYPE));

            } while (cursor.moveToNext());
        }

        return name;
    }
*/

}