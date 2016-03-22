package com.reddevil.loadshedding.networking;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.reddevil.loadshedding.datastorage.StorageHelper;
import com.reddevil.loadshedding.helper.AppConfig;
import com.reddevil.loadshedding.helper.AppConstants;
import com.reddevil.loadshedding.helper.Routine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Sherlock on 3/19/2016.
 *
 * Updater class which checks for the update for loadshedding schedules, retrieves if update is available and stores in database. Then it notifys the user.
 */
public class ScheduleUpdater {

    private static final String TAG = "ScheduleUpdater";

    public interface OnScheduleUpdatedListener {
        void isScheduleUpdated(Boolean isUpdated, String message);
        void getUpdatedRoutine(Routine routine);

    }

    private OnScheduleUpdatedListener mListener;
    private Context mContext;
    private NetworkConnection mNetworkConnection;

    public ScheduleUpdater(Context context) {
        this.mContext = context;
        mNetworkConnection = new NetworkConnection(mContext);
    }

    /**
     * Sets listener for this class
     */
    public void setListener(OnScheduleUpdatedListener listener) {
        this.mListener = listener;
    }

    /**
     * Starts updating of the schedules
     */
    public void update() {
        new UpdaterTask().execute();
    }


    /**
     * Class for retrieving loadshedding schedules and storing it in database
     */
    private class UpdaterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String jsonResponse = null;
            //Checking internet connection
            if (mNetworkConnection.checkConnectionStatus()) {
                try {

                    URL url = new URL(AppConstants.URL_ROUTINE_UPDATE);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(10000);
                    connection.setRequestMethod("GET");

                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {

                        Log.v(TAG,"Response Received successfully");
                        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                        jsonResponse = convertStreamToString(inputStream);
                        inputStream.close();
                        connection.disconnect();
                    } else {
                        //Response not OK
                        Log.v(TAG, "Http Request Error, Response Code not 200");
                        connection.disconnect();
                        mListener.isScheduleUpdated(false, "Http Error");
                        return null;
                    }
                } catch (IOException e) {
                    Log.v(TAG, "Http Connection Exception");
                    mListener.isScheduleUpdated(false,"Http Connection Exception");
                    e.printStackTrace();
                }
            } else {
                Log.v(TAG, "No Internet Connection");
                mListener.isScheduleUpdated(false,"No Internet Connection");
                Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            return jsonResponse;
        }


        @Override
        protected void onPostExecute(String response) {
            //If actual response is present
            if (response != null) {

                int currentUpdateNumber = new AppConfig(mContext).getRoutineId();
                Routine loadsheddingRoutine;

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        //getting update number for routine
                        int newUpdateNumber = jsonResponse.getInt("update_no");
                        //If actual update is available
                        if (currentUpdateNumber != newUpdateNumber) {

                            AppConfig config = new AppConfig(mContext);
                            config.setRoutineId(newUpdateNumber);
                            loadsheddingRoutine = new Routine();

                            //getting routine list for all groups
                            JSONArray routineArr = jsonResponse.getJSONArray("routine_list");

                            JSONObject routineItem;
                            HashMap<String, String> routineMap;
                            for (int i = 0; i < routineArr.length(); i++) {
                                routineMap = new HashMap<>();
                                routineItem = routineArr.getJSONObject(i);

                                int groupId = routineItem.getInt("group");
                                String sunRoutine = routineItem.getString("sunday");
                                String monRoutine = routineItem.getString("monday");
                                String tueRoutine = routineItem.getString("tuesday");
                                String wedRoutine = routineItem.getString("wednesday");
                                String thuRoutine = routineItem.getString("thursday");
                                String friRoutine = routineItem.getString("friday");
                                String satRoutine = routineItem.getString("saturday");

                                routineMap.put("sunday",sunRoutine);
                                routineMap.put("monday",monRoutine);
                                routineMap.put("tuesday",tueRoutine);
                                routineMap.put("wednesday",wedRoutine);
                                routineMap.put("thursday",thuRoutine);
                                routineMap.put("friday",friRoutine);
                                routineMap.put("saturday",satRoutine);

                                loadsheddingRoutine.addRoutine(groupId,routineMap);
                            }
                            addRoutineToDatabase(loadsheddingRoutine);

                        } else {
                            //No new update available
                            Toast.makeText(mContext, "No new update available", Toast.LENGTH_SHORT).show();
                            mListener.isScheduleUpdated(false,"No new update available");
                        }
                    } else {
                        String message = jsonResponse.getString("message");
                        Log.v(TAG, "Response Status: ERROR, Message: " + message);
                        mListener.isScheduleUpdated(false,message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mListener.isScheduleUpdated(false,"Json Exception");
                }
            } else {
                mListener.isScheduleUpdated(false,"No response received");
                Log.v(TAG, "Error Occurred, Response is null");
            }
        }

        /**
         * Inserts routine data to the database
         */
        public void addRoutineToDatabase(Routine routine)
        {
            Log.v(TAG,"Adding Routine to database");
            StorageHelper helper = new StorageHelper(mContext);
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues cv;

            for(int i=1;i<=7;i++)
            {
                cv = new ContentValues();
                cv.put(StorageHelper.COLUMN_GROUP,i);
                cv.put(StorageHelper.COLUMN_DAY_SUNDAY,routine.getRoutine(i,"sunday"));
                cv.put(StorageHelper.COLUMN_DAY_MONDAY,routine.getRoutine(i,"monday"));
                cv.put(StorageHelper.COLUMN_DAY_TUESDAY,routine.getRoutine(i,"tuesday"));
                cv.put(StorageHelper.COLUMN_DAY_WEDNESDAY,routine.getRoutine(i,"wednesday"));
                cv.put(StorageHelper.COLUMN_DAY_THURSDAY,routine.getRoutine(i,"thursday"));
                cv.put(StorageHelper.COLUMN_DAY_FRIDAY,routine.getRoutine(i,"friday"));
                cv.put(StorageHelper.COLUMN_DAY_SATURDAY,routine.getRoutine(i,"saturday"));
                db.insert(StorageHelper.TABLE_NAME, null, cv);
            }
            mListener.isScheduleUpdated(true,"Routine updated");
            mListener.getUpdatedRoutine(routine);
        }

        /*
        * Converts input stream to string
        * */
        public String convertStreamToString(InputStream inputStream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            try {
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

    }

}
