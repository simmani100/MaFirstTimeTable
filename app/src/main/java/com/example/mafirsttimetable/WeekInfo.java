package com.example.mafirsttimetable;
/**
 * Created by 재현 on 2016-11-06.
 // 시간표에 들어갈 시간 정보에 대한 상수 정의 (배열 인덱스)
 */

public class WeekInfo{
    public static int tableHeight;
    public static boolean allCheck = false;
    public static String [] week = new String[]{"월","화","수","목","금","토","일"};
    public static String [] week_to_Saturday = new String[]{"월","화","수","목","금","토"};
    public static String [] week_to_Friday = new String[]{"월","화","수","목","금"};

    public static int[] startHour = new int[]{8, 9, 10, 11, 12};
    public static int[] endHour = new int[]{17, 18, 19, 20, 21};
    public static int[] subjectMinute = {1,2,3,4,5};
}