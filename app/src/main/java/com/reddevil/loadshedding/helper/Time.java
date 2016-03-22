package com.reddevil.loadshedding.helper;

/**
 * Created by mansubh on 1/20/16.
 */
public class Time {

    int _id;
    String _starttime;
    String _endtime;

    public Time(){

    }
    public Time(int id,String starttime,String endtime){
        this._id = id;
        this._starttime = starttime;
        this._endtime = endtime;
    }
    public Time(String startime,String endtime){
        this._starttime = startime;
        this._endtime = endtime;
    }
    public int getID(){
        return this._id;
    }
    public void set_id(int id){
        this._id = id;
    }
    public String getStartTime(){
        return this._starttime;
    }
    public void setStartTime(String starttime){
        this._starttime = starttime;
    }
    public String getEndTime(){
        return this._endtime;
    }
    public void setEndTime(String endtime){
        this._endtime = endtime;
    }
}
