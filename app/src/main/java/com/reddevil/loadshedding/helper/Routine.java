package com.reddevil.loadshedding.helper;

import android.util.SparseArray;
import java.util.HashMap;

/**
 * Created by Sherlock on 3/20/2016.
 */
public class Routine {

    private SparseArray<HashMap<String,String>> routineList;

    public Routine()
    {
        routineList = new SparseArray<>();
    }

    /*
    * Adds routine list for given group
    * */
    public void addRoutine(int groupId,HashMap<String,String> routine)
    {
        routineList.append(groupId,routine);
    }

    /*
    * Returns routine list for given group
    * */
    public HashMap<String,String> getRoutine(int groupId)
    {
        return routineList.get(groupId);
    }



    /*
    * Returns routine for given group for given day
    * */
    public String getRoutine(int groupId,String day)
    {
        return routineList.get(groupId).get(day);
    }




}
