package com.example.mafirsttimetable;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 재현 on 2016-11-14.
 */

public class MainItemAdapter extends RecyclerView.Adapter<MainItemAdapter.ViewHolder> {
    Context context;
    List<TimeTable_Main_Item> items;
    int item_Layout;

    public MainItemAdapter(Context context, int item_Layout, List<TimeTable_Main_Item> items) {
        this.context = context;
        this.item_Layout = item_Layout;
        this.items = items;
        notifyDataSetChanged();
    }
    @Override
    public MainItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_main_card_item,parent,false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TimeTable_Main_Item item = items.get(position);
        holder.name.setText(item.getName());
        holder.startTimeView.setText("시작 : "+item.getStartTime());
        holder.endTimeView.setText("종료 : "+item.getEndTime());
        holder.weekView.setText(item.getWeekInfo()+"요일까지");

        //in some cases, it will prevent unwanted situations
        holder.CheckForDelete.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
        holder.CheckForDelete.setChecked(item.isReadyToDelete());
        /*if(WeekInfo.allCheck) {
            holder.CheckForDelete.setChecked(true);
        }
        else{
            holder.CheckForDelete.setChecked(false);
        }*/
        holder.CheckForDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                item.setReadyToDelete(isChecked);
            }
        });


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,startTimeView,endTimeView,weekView;
        public CheckBox CheckForDelete;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.card_title);
            startTimeView = (TextView)itemView.findViewById(R.id.card_StartTime);
            endTimeView = (TextView)itemView.findViewById(R.id.card_EndTime);
            weekView = (TextView)itemView.findViewById(R.id.card_Week);
            cardView = (CardView)itemView.findViewById(R.id.timeTableCardView);
            CheckForDelete = (CheckBox)itemView.findViewById(R.id.delete_Check);
        }

    }

}
