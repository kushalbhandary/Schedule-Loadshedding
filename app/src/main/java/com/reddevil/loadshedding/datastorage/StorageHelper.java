package com.reddevil.loadshedding.datastorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sherlock on 3/21/2016.
 */
public class StorageHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION  = 1;
    private static final String DATABASE_NAME = "loadshedding_db";

    public static final String TABLE_NAME= "loadshedding_routine";
    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_GROUP = "routine_group";
    public static final String COLUMN_DAY_SUNDAY = "sunday";
    public static final String COLUMN_DAY_MONDAY = "monday";
    public static final String COLUMN_DAY_TUESDAY = "tuesday";
    public static final String COLUMN_DAY_WEDNESDAY = "wednesday";
    public static final String COLUMN_DAY_THURSDAY = "thursday";
    public static final String COLUMN_DAY_FRIDAY = "friday";
    public static final String COLUMN_DAY_SATURDAY = "saturday";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " ( "  + COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_GROUP + " INTEGER," +
            COLUMN_DAY_SUNDAY + " TEXT," +
            COLUMN_DAY_MONDAY + " TEXT," +
            COLUMN_DAY_TUESDAY + " TEXT," +
            COLUMN_DAY_WEDNESDAY + " TEXT," +
            COLUMN_DAY_THURSDAY + " TEXT," +
            COLUMN_DAY_FRIDAY + " TEXT," +
            COLUMN_DAY_SATURDAY + " TEXT" +
            " )";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public StorageHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
