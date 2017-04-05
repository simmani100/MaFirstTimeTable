package com.example.mafirsttimetable;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by 재현 on 2016-11-09.
 */

public class TimeTableFileManagement extends SQLiteOpenHelper{
    public static final int DB_VERSION = 1;
    List<TimeTable_Main_Item> items = null;
    public TimeTableFileManagement(Context context){
        super(context,"TimeTable.db",null,DB_VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE Timetable (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " tableName TEXT," +            // 시간표 이름   tableName
                " startTime INTEGER," +             // 시간표의 시작 시간(String)   startTime
                " endTime INTEGER," +               // 시간표의 끝 시간(String)     endTime
                " tableWeek TEXT," +                // 시간표의 요일               tableWeek
                " subjectName TEXT," +          // 과목 이름     subjectName
                " planLocation TEXT," +         // 일정 장소 이름 planLocation
                " admin TEXT," +                // 담당자(교수)   admin
                " memo TEXT," +                 // 메모          memo
                " subjectEndTime INTEGER," +        // 과목의 끝 시간(String)      subjectEndTime
                " subjectStartTime INTEGER," +       // 과목의 시작 시간(String)   subjectStartTime
                " subjectWeek TEXT);");             // 과목의 요일                 subjectWeek

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
    public void CreateNewtableDB(SQLiteDatabase db,String title,int startTime,int endTime,int weekinfo){
        if(title != null){
            db.execSQL("INSERT INTO Timetable VALUES(" +
                    "null," +
                    " '"+title+"'," +
                    " "+WeekInfo.startHour[startTime]+"," +
                    " "+WeekInfo.endHour[endTime]+"," +
                    " '"+WeekInfo.week[4+weekinfo]+"'," +
                    " null," +
                    " null," +
                    " null," +
                    " null," +
                    " null," +
                    " null," +
                    " null);");

        }
    }
}
