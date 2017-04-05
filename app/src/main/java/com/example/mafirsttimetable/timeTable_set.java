package com.example.mafirsttimetable;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class timeTable_set extends Activity {

    ListView SettingList;
    public int start, end, lastyoil;

    Intent settingData = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_set);
        Button setBtn = (Button)findViewById(R.id.setButton);
        ArrayAdapter<CharSequence> sadapt;
        sadapt = ArrayAdapter.createFromResource(this, R.array.setting, android.R.layout.simple_list_item_1);
        SettingList = (ListView) findViewById(R.id.set_list);
        SettingList.setBackgroundColor(Color.GRAY);
        SettingList.setDivider(new ColorDrawable(Color.BLACK));
        SettingList.setDividerHeight(2);
        SettingList.setAdapter(sadapt);

        SettingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        new AlertDialog.Builder(timeTable_set.this)
                                .setTitle("시작시간을 설정하세요. (기본값 : 9시)")
                                .setItems(new String[]{"8시", "9시", "10시", "11시", "12시"},
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                start = which;
                                                settingData.putExtra("startTime", start);
                                            }
                                        })
                                .setNegativeButton("취소", null)
                                .show();
                        break;
                    case 1:
                        new AlertDialog.Builder(timeTable_set.this)
                                .setTitle("종료시간을 설정하세요. (기본값 : 18시)")
                                .setItems(new String[]{"17시", "18시", "19시", "20시", "21시"},
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                end = which;
                                                settingData.putExtra("endTime", end);
                                            }
                                        })
                                .setNegativeButton("취소", null)
                                .show();
                        break;
                    case 2:
                        new AlertDialog.Builder(timeTable_set.this)
                                .setTitle("마지막 요일을 설정하세요. (기본값 : 금요일)")
                                .setItems(new String[]{"금요일", "토요일", "일요일"},
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                lastyoil = which;
                                                settingData.putExtra("lastYoil", lastyoil);
                                            }
                                        })
                                .setNegativeButton("취소", null)
                                .show();
                        break;
                }

            }

        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK,settingData);
                Toast.makeText(timeTable_set.this,"설정이 저장되었습니다.",Toast.LENGTH_SHORT).show();
                timeTable_set.this.finish();
            }
        });
    }
    @Override
    public void onPause(){
        super.onPause();
        /*System.out.println(lastyoil+"\n"+start+"\n"+end);*/

    }
}
