package com.reddevil.loadshedding.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class TimeParser {

    private String[] daysArr = {"sunday","monday", "tuesday", "wednesday", "thursday",
            "friday", "saturday"};
    private HashMap<String, String> mSchedules;
    private String mEndTiming;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private int currentDay, nextDay;
    //Indicates whether currently loadshedding is going on or not.
    //0 -> Currently no loadshedding  1 -> currently loadshedding
    private int loadsheddingStatus = 0;

    public TimeParser() {
        computeDayOfWeek();
    }

    public void setRoutineData(HashMap<String,String> schedules)
    {
        this.mSchedules = schedules;
        processSchedules();
    }

    private void computeDayOfWeek()
    {
        //Getting days of the week
        Date date = new Date();
        //SimpleDateFormat dayFormat = new SimpleDateFormat("uuuu");
        this.currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        this.nextDay = (this.currentDay > 6) ? 1 : this.currentDay + 1;
    }


    public int[] getRemainingTime() {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        try
        {
            Date currentTime = timeFormat.parse(timeFormat.format(new Date()));
            Date endTime = timeFormat.parse(mEndTiming);
            long timeDiff = endTime.getTime() - currentTime.getTime();

            if(timeDiff < 0)
            {
                Date dateMax = timeFormat.parse("24:00:00");
                Date dateMin = timeFormat.parse("00:00:00");
                timeDiff=(dateMax.getTime() -currentTime.getTime() )+(endTime.getTime()-dateMin.getTime());
            }

            if(timeDiff == 0)
            {
                processSchedules();
            }

            seconds = (int) (timeDiff / 1000) % 60 ;
            minutes = (int) ((timeDiff / (1000*60)) % 60);
            hours   = (int) ((timeDiff / (1000*60*60)) % 24);

        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return new int[]{hours,minutes,seconds,loadsheddingStatus};
    }

    private void processSchedules() {

        ArrayList<String> timings = getTimings();
        // Check for special case of end time 00:00:00
        // Check for special case when end time is smaller than current time
        Date finalTime, currentTime;

        try {
            currentTime = new Date();
            currentTime = timeFormat.parse(timeFormat.format(currentTime));

            int i ;
            for(i =0;i<timings.size();++i)
            {
                finalTime = timeFormat.parse(timings.get(i));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(finalTime);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);

                //handling special case when end time is within
                //range 00:00:00 - 00:59:59
                if(hour == 0)
                {
                    calendar.set(Calendar.HOUR_OF_DAY, 24);
                    finalTime = calendar.getTime();

                    if(currentTime.before(finalTime))
                    {
                        mEndTiming = timings.get(i);
                        break;
                    }
                }

                if (i % 2 == 0)
                {
                    loadsheddingStatus = 0;
                }
                else
                {
                    loadsheddingStatus = 1;
                }

                //for other timing cases
                if(currentTime.before(finalTime)){
                    mEndTiming = timings.get(i);
                    break;
                }


            }
            //When the timings for current day is complete
            if(i == timings.size())
            {
                //fetch schedules for tomorrow
                //and set final time directly
                this.currentDay = this.nextDay;
                timings = getTimings();
                mEndTiming = timings.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Computes timings and sanitizes them. Returns timings for current day and next day
     * */
    private ArrayList<String> getTimings() {
        //Getting schedules
        ArrayList<String> endTimings = new ArrayList<>();
        String todaySchedule = mSchedules.get(daysArr[currentDay - 1]);
        processTimings(todaySchedule,endTimings);
        return endTimings;
    }

    /*
     * Sanitizes timings and appends it to the provided array list
     * */
    private void processTimings(String schedule,ArrayList<String> endTimings)
    {
        String[] durations = schedule.split(" & ");
        for (String eachDuration : durations) {
            String[] temp = eachDuration.split(" - ");
            for (String timings : temp) {
                String sanitizedTime = timings.replace("AM", "");
                sanitizedTime = sanitizedTime.replace("PM", "");
                sanitizedTime = sanitizedTime.replace(" ", "");
                endTimings.add(sanitizedTime.concat(":00"));
            }
        }
    }

}
