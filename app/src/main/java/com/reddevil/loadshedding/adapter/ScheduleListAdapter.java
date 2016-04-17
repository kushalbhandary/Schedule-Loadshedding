package com.reddevil.loadshedding.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reddevil.loadshedding.R;

import java.util.HashMap;


public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {

    private static String TAG = "ScheduleAdapter";

    HashMap<Integer,String[]> routineList;
    LinearLayout.LayoutParams params1,params2,params3;

    private String[] days = {"S","M","T","W","Th","F","Sa"};

    public ScheduleListAdapter(HashMap<Integer,String[]> routineList)
    {
        //to be used when there is 1 routine
        params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,3f);

        //to be used when there are 2 routines
        params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f);

        //to be used when there are 3 routines
        params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);

        this.routineList = routineList;
    }


    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
    {
        TextView day, firstTiming,secondTiming,thirdTiming;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.day);
            firstTiming = (TextView) itemView.findViewById(R.id.firstTiming);
            secondTiming = (TextView) itemView.findViewById(R.id.secondTiming);
            thirdTiming= (TextView) itemView.findViewById(R.id.thirdTiming);
        }
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {

        //Get Data
        String[] routines = routineList.get(position);

        holder.day.setText(days[position]);

        if (routines.length == 1)
        {
            holder.firstTiming.setLayoutParams(params1);
            holder.secondTiming.setVisibility(View.GONE);
            holder.thirdTiming.setVisibility(View.GONE);
            holder.firstTiming.setText(routines[0]);
        }
        else if(routines.length == 2)
        {
            holder.firstTiming.setLayoutParams(params2);
            holder.secondTiming.setLayoutParams(params2);
            holder.thirdTiming.setVisibility(View.GONE);
            holder.firstTiming.setText(routines[0]);
            holder.secondTiming.setText(routines[1]);
        }
        else if(routines.length == 3)
        {
            holder.firstTiming.setLayoutParams(params3);
            holder.secondTiming.setLayoutParams(params3);
            holder.thirdTiming.setLayoutParams(params3);
            holder.firstTiming.setText(routines[0]);
            holder.secondTiming.setText(routines[1]);
            holder.thirdTiming.setText(routines[2]);
        }
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,parent,false);
        return new ScheduleViewHolder(view);
    }

}
