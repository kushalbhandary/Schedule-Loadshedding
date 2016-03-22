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
import java.util.HashMap;

/**
 * Created by Sherlock on 12/22/2015.
 */

public class Groups extends Fragment {

    HashMap<Integer,String[]> groupRoutine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle dataBundle = this.getArguments();
        int groupId = dataBundle.getInt("GROUP");

        View view =  inflater.inflate(R.layout.layout_group,container,false);

        //Setting up recycler view
        RecyclerView scheduleView = (RecyclerView) view.findViewById(R.id.scheduleList);
        scheduleView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        scheduleView.setLayoutManager(layoutManager);

        processSchedules(groupId);

        ScheduleListAdapter adapter = new ScheduleListAdapter(groupRoutine);
        scheduleView.setAdapter(adapter);

        return view;

    }

    /**
     * Makes string ready for displaying
     * */
    private void processSchedules(int groupId)
    {
        //Fetching routines for the provided group
        HashMap<String,String> tempList = MainActivity.loadsheddingRoutine.getRoutine(groupId);
        groupRoutine = new HashMap<>();

        //Fetching routines for each day and storing it in array
        String[] routines = new String[7];
        routines[0] = tempList.get("sunday");
        routines[1] = tempList.get("monday");
        routines[2] = tempList.get("tuesday");
        routines[3] = tempList.get("wednesday");
        routines[4] = tempList.get("thursday");
        routines[5] = tempList.get("friday");
        routines[6] = tempList.get("saturday");

        //Since fetched routine are concatenated using &, splitting them and storing it
        String[] daysRoutine;
        for (int i=0;i<routines.length;i++)
        {
            String[] routineItems = routines[i].split(" & ");
            groupRoutine.put(i,routineItems);
        }
    }

}
