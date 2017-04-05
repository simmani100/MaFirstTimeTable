package com.example.mafirsttimetable;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.media.tv.TvView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Size;
import android.support.design.widget.TabLayout;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeTable extends Activity {
    HashMap<Integer,Integer> hourIndexInfo;
    private int TIME_PICKER_INTERVAL = 10;
    TimeTableFileManagement tableData;
    SQLiteDatabase dbController;
    TextView weekText,timeText, tableName;
    int startTime,endTime,yoil,week,minToMaxTime_in_TimeTable;
    int weekInteger=0;
    Spanning span;
    GridLayout table;
    TimePicker startTimePicker,endTimePicker;
    String subjectStartTime, subjectStartEndTime=null;
    Spinner weekSpin;
    ArrayAdapter<CharSequence> weekAdapter;
    EditText subjectName,planLocation,admin,memo;
    DisplayMetrics metrics = new DisplayMetrics();
    int tableHeight;


    // Secondly fetch the window manager using method chaining
    public int convertToDp(Context context, int input) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input * scale + 0.5f);
    }       //PIXEL -> DP 변환 메소드
    public int convertToPx(Context context, int input) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (input / scale + 0.5f);
    }       //DP -> PIXEL 변환 메소드


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_create_layout);
        Intent getIdata = getIntent();
        String tableTitle = getIdata.getStringExtra("title");
        startTime = getIdata.getIntExtra("settedstartTime",1);
        endTime = getIdata.getIntExtra("settedEndTime",1);
        yoil = getIdata.getIntExtra("settedLastYoil", 0); // 설정값 및 시간표 제목으로부터 문자열, 숫자값을 Intent 에 담아 받아옴.
        minToMaxTime_in_TimeTable = (WeekInfo.endHour[endTime]-WeekInfo.startHour[startTime]);
        this.week = 4 + yoil;
        hourIndexInfo = new HashMap<>();
        setHourIndexInfo(hourIndexInfo);

        tableData = new TimeTableFileManagement(this);
        dbController = tableData.getReadableDatabase();
                            // DataBase 테이블 정보 //
/*              " _id INT AUTOINCREMENT PK      // ID값, 자동 증가 및 기본키
                " tableName TEXT," +            // 시간표 이름   tableName
                " startTime INTEGER," +         // 시간표의 시작 시간(String)   startTime
                " endTime INTEGER," +           // 시간표의 끝 시간(String)     endTime
                " tableWeek TEXT," +            // 시간표의 요일               tableWeek
                " subjectName TEXT," +          // 과목 이름     subjectName
                " planLocation TEXT," +         // 일정 장소 이름 planLocation
                " admin TEXT," +                // 담당자(교수)   admin
                " memo TEXT," +                 // 메모          memo
                " subjectEndTime INTEGER," +    // 과목의 끝 시간(String)      subjectEndTime
                " subjectStartTime INTEGER," +  // 과목의 시작 시간(String)   subjectStartTime
                " subjectWeek TEXT);");         // 과목의 요일                 subjectWeek*/
        
        tableName = (TextView)findViewById(R.id.tableName);
        tableName.setText(tableTitle);

        final LinearLayout time = (LinearLayout)findViewById(R.id.time_table_Linear);
        LinearLayout.LayoutParams timeParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        time.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {

            @Override
            public void onGlobalLayout()
            {
                // get width and height
                WeekInfo.tableHeight = time.getHeight();
                System.out.println("리니어 레이아웃 높이 1 : "+WeekInfo.tableHeight);
                // gets called after layout has been done but before display.
                time.getViewTreeObserver().removeOnGlobalLayoutListener(this);


            }
        });
        System.out.println("리니어 레이아웃 높이 2 : "+WeekInfo.tableHeight);
        table = (GridLayout)View.inflate(this,R.layout.time_grid,null);         //시간표의 격자무늬 생성
        table.setColumnCount(week+2);                                                   // 열의 갯수(요일)
        table.setRowCount((WeekInfo.endHour[endTime]-WeekInfo.startHour[startTime])*6+1); // 행의 갯수(시간)
        time.addView(table, timeParam);
        table.setBackgroundColor(Color.parseColor("#BCE9B7"));

        getWindowManager().getDefaultDisplay().getMetrics(metrics);



        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        int heightDP = convertToDp(this,height);
        int widthDP = convertToDp(this,width);
        int tableHeightDP = convertToDp(this,WeekInfo.tableHeight);

        for(int i = 0;i<=week;i++) {
            weekText = new TextView(this);
            weekText.setText(WeekInfo.week[i]);
            weekText.setTextSize(metrics.scaledDensity*5);
            weekText.setWidth(convertToPx(this,widthDP)/(week+2));
            weekText.setHeight(convertToPx(this,tableHeightDP)/(WeekInfo.endHour[endTime]-WeekInfo.startHour[startTime]+2));
            weekText.setBackgroundColor(Color.parseColor("#ff33b5e5"));
            weekText.setGravity(Gravity.CENTER);
            weekText.setPadding(10,10,10,10);
            table.addView(weekText,new GridLayout.LayoutParams(GridLayout.spec(0),GridLayout.spec(i+1)));
        }               // 설정값으로부터 요일을 격자의 열에 표현
        int rowNumber = 1;
        for(int i = WeekInfo.startHour[startTime];i<=WeekInfo.endHour[endTime];i++) {
            timeText = new TextView(this);
            timeText.setText(i+"시");
            timeText.setTextSize(metrics.scaledDensity*3);
            timeText.setPadding(10,10,10,10);
            timeText.setGravity(Gravity.CENTER);
            timeText.setWidth(convertToPx(this,widthDP)/(week+2));
            timeText.setHeight(convertToPx(this,tableHeightDP)/(WeekInfo.endHour[endTime]-WeekInfo.startHour[startTime]+2));
            timeText.setBackgroundColor(Color.parseColor("#ff33b5e5"));
            table.addView(timeText,new GridLayout.LayoutParams(GridLayout.spec(rowNumber,5,GridLayout.FILL),GridLayout.spec(0)));
            rowNumber+=5;
        }               // 설정값으로부터 시간을 격자의 행에 표현*/
        Toast.makeText(this,tableHeight+"\n" +
                getWindowManager().getDefaultDisplay().getWidth()+"\n" +
                convertToDp(this,metrics.widthPixels)/(week)+
                "\n" +metrics.densityDpi+
                "\n" +metrics.density+
                "\n" +metrics.heightPixels+
                "\n" +metrics.xdpi+
                "\n" +metrics.ydpi,Toast.LENGTH_LONG).show();
    }

    public void editTable(View v){

        switch(v.getId()){
            case R.id.addTimeTableComponent:
                final LinearLayout inputForm = (LinearLayout) View.inflate(TimeTable.this,R.layout.input_subject_form,null);
                weekSpin = (Spinner)inputForm.findViewById(R.id.weekSpinner);
                weekAdapter = getWeekAdapter(inputForm,week);
                weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                weekSpin.setAdapter(weekAdapter);
                weekSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        weekInteger = position+1;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                subjectName = (EditText)inputForm.findViewById(R.id.subjectName);
                planLocation = (EditText)inputForm.findViewById(R.id.plan_location);
                memo = (EditText)inputForm.findViewById(R.id.Memo);
                admin = (EditText)inputForm.findViewById(R.id.adminName);

                startTimePicker = (TimePicker)inputForm.findViewById(R.id.startTimePicker);
                SetTimePicker(startTimePicker,WeekInfo.startHour[startTime],WeekInfo.endHour[endTime]);
                endTimePicker = (TimePicker)inputForm.findViewById(R.id.endTimePicker);
                SetTimePicker(endTimePicker,WeekInfo.startHour[startTime],WeekInfo.endHour[endTime]);   // 과목의 시간 설정 부분
                // 시간표에 과목 추가시 대화상자에 필요한 동적인 레이아웃 구성.
                new AlertDialog.Builder(TimeTable.this)
                        .setTitle("강의정보 입력")
                        .setView(inputForm)
                        .setPositiveButton("추가",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    span = subjectTimeCaculating(startTimePicker, endTimePicker, hourIndexInfo);

                                    Toast.makeText(table.getContext(), "시작시간 분 : "
                                            + startTimePicker.getCurrentMinute()
                                            +"\n시작시간 시간 " +startTimePicker.getCurrentHour()
                                            + "\n시작 그리드 좌표" + span.getStart()
                                            + "\n사이즈 " + span.getSize()
                                            , Toast.LENGTH_LONG).show();

                                    TextView sampleTexet = new TextView(TimeTable.this);
                                    sampleTexet.setMaxWidth(170);
                                    sampleTexet.setText(""
                                            + subjectName.getText()
                                            + "\n" +planLocation.getText()
                                            + "\n" + admin.getText()
                                            + "\n" + memo.getText()
                                            + "\n ~ "+endTimePicker.getCurrentHour()
                                            +" : " +endTimePicker.getCurrentMinute()*10);
                                    table.addView(sampleTexet,
                                            new GridLayout.LayoutParams(GridLayout.spec(span.getStart(), span.getSize(), GridLayout.FILL),
                                                    GridLayout.spec(weekInteger)));

                                    sampleTexet.setBackgroundColor(Color.LTGRAY);
                                    sampleTexet.setGravity(Gravity.CENTER);
                                }catch (NullPointerException e){
                                    Toast.makeText(inputForm.getContext(), "강의 종료시간은 시작시간보다 늦어야 합니다." +
                                            "\n현재 강의 시작 시간 : " + startTimePicker.getCurrentHour() + " : " + startTimePicker.getCurrentMinute() * 10 + "", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소",null)
                        .show();
                break;
            case R.id.SaveTimeTable:
                break;
            case R.id.CancelTimeTableComponent:
                this.finish();
                break;
        }
    }
    public void onPause(){
        super.onPause();
        tableData = new TimeTableFileManagement(this);
        dbController = tableData.getWritableDatabase();
        hourIndexInfo.clear();
    }

    @SuppressLint("NewApi")
    private void SetTimePicker(TimePicker tp, int min, int max){
        tp.setIs24HourView(true);
        try {
            final NumberPicker hourPicker;
            final NumberPicker minutePicker;
            List<String> displayedHourValues;
            Class<?> classForid = Class.forName("com.android.internal.R$id");

            Field hfield = classForid.getField("hour");
            hourPicker = (NumberPicker)tp.findViewById(hfield.getInt(null));
            hourPicker.setMinValue(min);
            hourPicker.setMaxValue(max);
            displayedHourValues = new ArrayList<String>();
            for (int i = min; i <= max; i++) {
                displayedHourValues.add(String.format("%02d", i));
            }
            hourPicker.setDisplayedValues(displayedHourValues
                    .toArray(new String[0]));
            hourPicker.setWrapSelectorWheel(true);

            Field mfield = classForid.getField("minute");
            minutePicker = (NumberPicker)tp.findViewById(mfield.getInt(null));

            final List<String> displayedMinuteValues;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(5);

            displayedMinuteValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedMinuteValues.add(String.format("%02d", i));
            }
            tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    if(hourPicker.getValue() == WeekInfo.endHour[endTime]){
                        displayedMinuteValues.clear();
                        for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                            displayedMinuteValues.add(String.format("%02d", 0));
                        }
                        minutePicker.setDisplayedValues(new String[]{String.format("%02d", 0),
                                String.format("%02d", 0),
                                String.format("%02d", 0),
                                String.format("%02d", 0),
                                String.format("%02d", 0),
                                String.format("%02d", 0)});
                        /*setDisplayedValues(displayedMinuteValues
                                .toArray(new String[0]));*/
                    }
                    else{
                        displayedMinuteValues.clear();
                        for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                            displayedMinuteValues.add(String.format("%02d", i));
                        }
                        minutePicker.setDisplayedValues(displayedMinuteValues
                                .toArray(new String[0]));
                    }
                }
            });
            /*minutePicker.setDisplayedValues(displayedMinuteValues
                    .toArray(new String[0]));*/
            minutePicker.setWrapSelectorWheel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// 시간표 과목의 시간 설정하는 TimePicker 커스터마이징 메소드

    ArrayAdapter<CharSequence> getWeekAdapter(LinearLayout linear, int weekinfo){
        ArrayAdapter<CharSequence> getAdapter = ArrayAdapter.createFromResource(linear.getContext(),R.array.week_to_sunday,R.layout.support_simple_spinner_dropdown_item);
        switch (weekinfo){
            case 4:
                getAdapter = ArrayAdapter.createFromResource(linear.getContext(),R.array.week_to_friday,R.layout.support_simple_spinner_dropdown_item);
                break;
            case 5:
                getAdapter = ArrayAdapter.createFromResource(linear.getContext(),R.array.week_to_saturday,R.layout.support_simple_spinner_dropdown_item);
                break;
            case 6:
                getAdapter = ArrayAdapter.createFromResource(linear.getContext(),R.array.week_to_sunday,R.layout.support_simple_spinner_dropdown_item);
                break;
        }
        return getAdapter;
    } //요일 구분시키기위한 메소드

    private void setHourIndexInfo(HashMap<Integer,Integer> hashMap){
        int minValue = WeekInfo.startHour[startTime];
        int maxValue = WeekInfo.endHour[endTime];
        int columnNumber = 1;
        for( int i = minValue; i<=maxValue;i++){
            hashMap.put(i,columnNumber);
            columnNumber+=5;
        }
    }
    class Spanning{
        int start;
        int size;

        public int getStart() {
            return start;
        }

        public int getSize() {
            return size;
        }

        public Spanning(int start, int size) {

            this.start = start;
            this.size = size;
        }
    }

    private Spanning subjectTimeCaculating(TimePicker startTimePicker, TimePicker endTimePicker, HashMap<Integer,Integer> hourIndex){
        Spanning spanning;
        int start,size;
        int startHour,startMinute,endHour,endMinute;
        startHour = startTimePicker.getCurrentHour();
        startMinute = startTimePicker.getCurrentMinute();
        endHour = endTimePicker.getCurrentHour();
        endMinute = endTimePicker.getCurrentMinute();

        if(startHour <= endHour){
            if(startMinute <= endMinute)
            {
                start = hourIndex.get(startHour)+(startMinute);
                size = (hourIndex.get(endHour)-hourIndex.get(startHour))+(Math.abs(endMinute-startMinute));
                spanning = new Spanning(start,size);
                return spanning;
            }
            else
            {
                start = hourIndex.get(startHour)+(startMinute);
                size = (hourIndex.get(endHour)-hourIndex.get(startHour)-1)+(Math.abs(endMinute+(5-startMinute)));
                spanning = new Spanning(start,size);
                return spanning;
            }
        }
        else{
            return null;
        }
    }
}

