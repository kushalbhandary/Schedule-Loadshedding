package com.reddevil.loadshedding.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reddevil.loadshedding.R;


public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {

    String[] dataList;
    LinearLayout.LayoutParams params1,params2;
    String[] eachData;


    public ScheduleListAdapter(String[] dataList,String[] eachData)
    {

        params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f);

        params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);

        this.dataList = dataList;
        this.eachData = eachData;
    }


    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
    {
        TextView day, firstTiming1,firstTiming2,secondTiming1,secondTiming2,thirdTiming;
        public ScheduleViewHolder(View itemView) {
            super(itemView);

            day = (TextView) itemView.findViewById(R.id.day);
            firstTiming1 = (TextView) itemView.findViewById(R.id.firstTiming1);
            firstTiming2=(TextView)itemView.findViewById(R.id.firsttiming2);
            secondTiming1 = (TextView) itemView.findViewById(R.id.secondTiming1);
            secondTiming2 = (TextView)itemView.findViewById(R.id.secondTiming2);
            thirdTiming= (TextView) itemView.findViewById(R.id.thirdTiming);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.length;
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {

        holder.thirdTiming.setVisibility(View.GONE);
        holder.day.setText(dataList[position]);
        holder.firstTiming1.setLayoutParams(params1);
        holder.firstTiming1.setText(dataList[position] + " First Item");
        holder.firstTiming2.setText(dataList[position]);
        holder.secondTiming1.setText(dataList[position] + " Second Item");
        holder.secondTiming2.setText(dataList[position]);
        holder.secondTiming1.setLayoutParams(params1);



    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,parent,false);

        ScheduleViewHolder vh = new ScheduleViewHolder(view);
        return vh;
    }
}
