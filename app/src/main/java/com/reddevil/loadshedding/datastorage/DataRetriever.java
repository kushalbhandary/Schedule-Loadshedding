package com.reddevil.loadshedding.datastorage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.reddevil.loadshedding.helper.Routine;

import java.util.HashMap;


public class DataRetriever
{
    Context mContext;
    private Routine loadsheddingRoutine;

    public DataRetriever(Context context)
    {
        this.mContext = context;
    }

	/**
	* Retrieves routine from database and returns it
	*/
	public Routine retrieveData()
	{
		loadsheddingRoutine = new Routine();
		StorageHelper helper = new StorageHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projections = {
                StorageHelper.COLUMN_GROUP,
                StorageHelper.COLUMN_DAY_SUNDAY,
                StorageHelper.COLUMN_DAY_MONDAY,
                StorageHelper.COLUMN_DAY_TUESDAY,
                StorageHelper.COLUMN_DAY_WEDNESDAY,
                StorageHelper.COLUMN_DAY_THURSDAY,
                StorageHelper.COLUMN_DAY_FRIDAY,
                StorageHelper.COLUMN_DAY_SATURDAY
        };

        Cursor cursor = db.query(StorageHelper.TABLE_NAME,projections,null,null,null,null,null);
        if(cursor != null && cursor.moveToFirst())
        {
            HashMap<String,String> routineMap;
            do {
                routineMap = new HashMap<>();

                int groupId = cursor.getInt(cursor.getColumnIndex(StorageHelper.COLUMN_GROUP));
                String sunRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_SUNDAY));
                String monRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_MONDAY));
                String tueRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_TUESDAY));
                String wedRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_WEDNESDAY));
                String thuRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_THURSDAY));
                String friRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_FRIDAY));
                String satRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_SATURDAY));

                routineMap.put("sunday",sunRoutine);
                routineMap.put("monday",monRoutine);
                routineMap.put("tuesday",tueRoutine);
                routineMap.put("wednesday",wedRoutine);
                routineMap.put("thursday",thuRoutine);
                routineMap.put("friday",friRoutine);
                routineMap.put("saturday",satRoutine);

                loadsheddingRoutine.addRoutine(groupId,routineMap);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return loadsheddingRoutine;
	}



}