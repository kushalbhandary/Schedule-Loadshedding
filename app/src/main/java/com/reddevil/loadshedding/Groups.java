package com.reddevil.loadshedding;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.reddevil.loadshedding.adapter.ScheduleListAdapter;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sherlock on 12/22/2015.
 */


public class Groups extends Fragment {
   private String[] dataSun;
    String[] dataForList = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    public static String url = "http://loadsheddingnepal.herokuapp.com/schedules";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle dataBundle = this.getArguments();
        int groupId = dataBundle.getInt("GROUP");


        View view =  inflater.inflate(R.layout.layout_group,container,false);

        RecyclerView scheduleView = (RecyclerView) view.findViewById(R.id.scheduleList);
        scheduleView.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        scheduleView.setLayoutManager(layoutManager);


        ConnectivityManager connectivityManager = (ConnectivityManager)inflater.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if(info!=null&& info.isConnected()) {
            new JSonParse(scheduleView).execute(url + groupId);

        }else {
            Toast.makeText(getActivity(),"No network. Check your connection",Toast.LENGTH_LONG).show();
        }

        return view;

    }

    private class JSonParse extends AsyncTask<String, String,String> {
        private final RecyclerView scheduleView;
        private JSonParse(RecyclerView scheduleView) {
            this.scheduleView = scheduleView;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {
            String myUrl = params[0];
            InputStream is = null;
            int len = 1024;
            try {
                URL url = new URL(myUrl);
                HttpURLConnection myconn = (HttpURLConnection) url.openConnection();
                myconn.setReadTimeout(10000);
                myconn.setConnectTimeout(15000);
                myconn.setRequestMethod("GET");
                myconn.setDoInput(true);

                myconn.connect();

                int response = myconn.getResponseCode();
                Log.d("RESPONSE","The response is:"+response);
                is = myconn.getInputStream();

                //readIt method in android of String class error convert the input stream in string

                String contentAsString = readIt(is,len);
                return contentAsString;

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if (is!=null){
                        is.close();
                    }

                }catch (Exception e){

                }
            }
            return null;
        }

        public String readIt(InputStream stream , int len)throws IOException{
            Reader reader;
            reader = new InputStreamReader(stream,"UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject reader = new JSONObject(s);

            /*    JSONObject sunday= reader.getJSONObject("Sunday");
                Log.d("MANSUBH",""+sunday.get("end_time_2"));*/

                if (reader.has("Sunday")){
                    JSONObject time =reader.getJSONObject("Sunday");
                    for(int i = 0;i<time.length();i++) {
                    }

                    if(reader.has("Monday")){
                        JSONObject timemon =reader.getJSONObject("Monday");
                        String monstart1 = time.getString("start_time_1");
                        String monend1 = time.getString("end_time_1");
                        String monstart2 = time.getString("start_time_2");
                        String monend2 = time.getString("end_time_2");

                    }
                }
                ScheduleListAdapter adapter = new ScheduleListAdapter(dataForList,new String[] {"1", "2", "3", "4"});
                scheduleView.setAdapter(adapter);

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ERROR","onPostExecute ",e);
                ScheduleListAdapter adapter = new ScheduleListAdapter(dataForList,new String[] {"A", "B", "C", "D"});
                scheduleView.setAdapter(adapter);


            }

        }
    }
}
