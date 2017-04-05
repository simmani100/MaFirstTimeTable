package com.example.mafirsttimetable;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FirstScreen extends AppCompatActivity {
    static final int SET_MODIFY = 1;
    Intent newTableData;
    Intent set3;
    SQLiteDatabase TableDatabase;
    TimeTableFileManagement tableData;
    String iTitle;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<TimeTable_Main_Item> main_items = new ArrayList<>();
    public int settedStartTime=1, settedEndTime=1, settedlastYoil=0;
    Cursor cursor;
    MenuItem mItem;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        tableData = new TimeTableFileManagement(this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = (RecyclerView)findViewById(R.id.tableRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        TableDatabase=tableData.getReadableDatabase();
        Refresh();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                final GestureDetector gestureDetector = new GestureDetector(FirstScreen.this, new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    TextView a = (TextView)rv.getChildViewHolder(child).itemView.findViewById(R.id.tableName);
                    Toast.makeText(FirstScreen.this,a.getText(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout sv = (LinearLayout) View.inflate(FirstScreen.this,R.layout.tabletitle,null);

                final AlertDialog.Builder CreateDialog = new AlertDialog.Builder(FirstScreen.this);
                CreateDialog
                        .setTitle("시간표 이름 입력")
                        .setView(sv)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int whichButton){

                                EditText title = (EditText) sv.findViewById(R.id.t_Title);
                                iTitle = title.getText().toString();

                                if(iTitle.length() == 0 || iTitle == null){
                                    Toast.makeText(FirstScreen.this, "시간표 제목을 입력하세요.", Toast.LENGTH_SHORT).show();
                                }
                                else if(IsDuplicatedTitle(iTitle)){
                                    Toast.makeText(FirstScreen.this, "이미 같은 이름의 시간표가 존재합니다.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    newTableData = new Intent(FirstScreen.this, TimeTable.class);
                                    newTableData.putExtra("title", iTitle);
                                    newTableData.putExtra("settedstartTime", settedStartTime);
                                    newTableData.putExtra("settedEndTime", settedEndTime);
                                    newTableData.putExtra("settedLastYoil", settedlastYoil);

                                    TableDatabase = tableData.getWritableDatabase();
                                    tableData.CreateNewtableDB(TableDatabase,iTitle,settedStartTime,settedEndTime,settedlastYoil);
//                                    cursor = TableDatabase.rawQuery("SELECT tableName,startTime,endTime,tableWeek FROM Timetable",null);
                                    Refresh();
                                    startActivity(newTableData);
                                   }
                            }
                        })
                        .setNegativeButton("취소",null)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_first_screen, menu);
        mItem = menu.findItem(R.id.allSelect);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id==R.id.objectDelete){
                new AlertDialog.Builder(this)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(main_items.size() == 0){
                                    Toast.makeText(FirstScreen.this,"생성한 시간표가 없습니다.\n+버튼으로 시간표를 생성해 주세요",Toast.LENGTH_SHORT).show();
                                }
                                for(int i = 0; i< main_items.size();i++){
                                    if(main_items.get(i).readyToDelete){
                                        TableDatabase.execSQL("DELETE FROM Timetable WHERE " +
                                                "tableName = '"+main_items.get(i).getName()+"';");
                                    }
                                }
                                Refresh();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setTitle("선택한 시간표를 삭제하시겠습니까?")
                        .show();
            return true;
        }

        if(id==R.id.TTsetting){
            set3 = new Intent(this,timeTable_set.class);
            startActivityForResult(set3,SET_MODIFY);
            return true;
        }

        if(id == R.id.allSelect){
            if(flag % 2 == 0) {
                main_items.clear();
                recyclerView.removeAllViewsInLayout();
                cursor = TableDatabase.rawQuery("SELECT tableName,startTime,endTime,tableWeek FROM Timetable ORDER BY _id DESC",null);
                while (cursor.moveToNext()) {
                    main_items.add(new TimeTable_Main_Item(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3)));
                }

                for (int i = 0; i < main_items.size(); i++) {
                    main_items.get(i).readyToDelete = true;
                }
                mItem.setTitle("선택해제");
                mItem.setIcon(R.drawable.allselectcancel);

                recyclerView.setAdapter(new MainItemAdapter(getApplicationContext(),R.layout.activity_first_screen,main_items));
                flag++;
            }else{
                main_items.clear();
                recyclerView.removeAllViewsInLayout();
                cursor = TableDatabase.rawQuery("SELECT tableName,startTime,endTime,tableWeek FROM Timetable ORDER BY _id DESC",null);
                while (cursor.moveToNext()) {
                    main_items.add(new TimeTable_Main_Item(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3)));
                }

                for (int i = 0; i < main_items.size(); i++) {
                    main_items.get(i).readyToDelete = false;
                }
                mItem.setTitle("전체선택");
                mItem.setIcon(R.drawable.allselect);


                recyclerView.setAdapter(new MainItemAdapter(getApplicationContext(),R.layout.activity_first_screen,main_items));
                flag++;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    private void Refresh() {
        main_items.clear();
        recyclerView.removeAllViewsInLayout();
        cursor = TableDatabase.rawQuery("SELECT tableName,startTime,endTime,tableWeek FROM Timetable ORDER BY _id DESC",null);
        while (cursor.moveToNext()) {
            main_items.add(new TimeTable_Main_Item(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3)));
        }
        recyclerView.setAdapter(new MainItemAdapter(getApplicationContext(),R.layout.activity_first_screen,main_items));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case SET_MODIFY:
                System.out.println(RESULT_OK);
                if(resultCode == RESULT_OK){
                    settedEndTime=data.getIntExtra("endTime",1);
                    settedStartTime=data.getIntExtra("startTime",1);
                    settedlastYoil=data.getIntExtra("lastYoil",0);
                }

                break;
        }
    }
    private boolean IsDuplicatedTitle(String input){
        boolean result = false;
        for (int i = 0 ; i < main_items.size() ; i++){
            if(input.equals(main_items.get(i).getName())){
                result = true;
                break;
            }
            else {
                result = false;
                break;
            }
        }
        return result;
    }

}
/*
recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
@Override
public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
final GestureDetector gestureDetector = new GestureDetector(FirstScreen.this, new GestureDetector.SimpleOnGestureListener(){
@Override
public boolean onSingleTapUp(MotionEvent e) {
        return true;
        }
        });
        if (child != null && gestureDetector.onTouchEvent(e)) {
        deleteSignal = (CheckBox) rv.getChildViewHolder(child).itemView.findViewById(R.id.delete_Check);

        System.out.println();
        deleteSignal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
        Toast.makeText(FirstScreen.this, "체크!", Toast.LENGTH_SHORT).show();

        } else
        Toast.makeText(FirstScreen.this, "체크 해제!", Toast.LENGTH_SHORT).show();

        }
        });

        }
        return false;
        }
@Override
public void onTouchEvent (RecyclerView rv, MotionEvent e){ }

@Override
public void onRequestDisallowInterceptTouchEvent ( boolean disallowIntercept){ }


        });*/
