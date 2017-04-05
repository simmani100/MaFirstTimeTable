package com.example.mafirsttimetable;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by 재현 on 2016-11-14.
 */

public class TimeTable_Main_Item {
    String name,weekInfo;
    int startTime, endTime;
    boolean readyToDelete;

    public TimeTable_Main_Item(String name, int startTime, int endTime, String weekInfo) {
        this.endTime = endTime;
        this.startTime = startTime;

        this.name = name;
        this.weekInfo = weekInfo;
        this.readyToDelete = false;
    }

    public int getEndTime() {
    
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeekInfo() {
        return weekInfo;
    }

    public boolean isReadyToDelete() {
        return readyToDelete;
    }

    public void setReadyToDelete(boolean readyToDelete) {
        this.readyToDelete = readyToDelete;
    }
    public void setWeekInfo(String weekInfo) {
        this.weekInfo = weekInfo;
    }
}
