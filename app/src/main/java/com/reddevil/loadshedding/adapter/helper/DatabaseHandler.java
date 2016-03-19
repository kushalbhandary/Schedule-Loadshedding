package com.reddevil.loadshedding.adapter.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.WindowDecorActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mansubh on 1/20/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TimeManager";
    private static final String TABLE_TIME = "Time";

    private static final String KEY_ID = "id";
    private static final String KEY_STARTTIME = "StartTime";
    private static final String KEY_ENDTIME = "EndTIme";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //creating table

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TIME_TABLE="CREATE TABLE " + TABLE_TIME + "("+KEY_ID + " INTEGER PRIMARY KEY," + KEY_STARTTIME + " TEXT," +
                KEY_ENDTIME + " TEXT" + ")";
        db.execSQL(CREATE_TIME_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME);
        onCreate(db);
    }

    public void addTime(Time time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STARTTIME,time.getStartTime());
        values.put(KEY_ENDTIME,time.getEndTime());
        db.insert(TABLE_TIME,null,values);
        db.close();

    }
    public Time getTime(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TIME,new String[]{KEY_ID,KEY_STARTTIME,KEY_ENDTIME},
                KEY_ID +"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Time time = new Time(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
        return time;




    }
    public List<Time> getAllTime(){
        List<Time> timelist = new ArrayList<Time>();
        String selectQuery = "SELECT * FROM " + TABLE_TIME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do {
                Time time = new Time();
                time.set_id(Integer.parseInt(cursor.getString(0)));
                time.setStartTime(cursor.getString(1));
                time.setEndTime(cursor.getString(2));
                //add time to list
                timelist.add(time);

            }while (cursor.moveToNext());
        }
        return timelist;
    }

    public int updateTime(Time time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STARTTIME,time.getStartTime());
        values.put(KEY_ENDTIME,time.getEndTime());

        return db.update(TABLE_TIME,values,KEY_ID + "=?",new String[]{String.valueOf(time.getID())});
    }
    public void deleteTime(Time time){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIME,KEY_ID + "=?",new String[]{String.valueOf(time.getID())});
        db.close();

    }
}
