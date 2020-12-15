package com.example.caloriecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<ItemData> mPersons;
    private LayoutInflater mInflate;
    private Context mContext;

    public RecyclerAdapter(Context context, ArrayList<ItemData> persons) {
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mPersons = persons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflate.inflate(R.layout.listviewcalorie,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._id.setText(mPersons.get(position)._id);
        holder.date.setText(mPersons.get(position).date);
        holder.today.setText(mPersons.get(position).today + "kal");
        holder.max.setText(mPersons.get(position).max + "kal");
        holder.tvLoadTodayMaxCal.setText(mPersons.get(position).today + "kal / " + mPersons.get(position).max + "kal");
        holder.pbLoadBar.setProgress(Integer.parseInt(mPersons.get(position).today));
        holder.pbLoadBar.setMax(Integer.parseInt(mPersons.get(position).max));
        holder.ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dataBaseHelper;
                dataBaseHelper = new DataBaseHelper(mContext);
                dataBaseHelper.deleteDate(mPersons.get(position)._id);
                dataBaseHelper.updateItems();
                Fragment1.rAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder { // 자료를 담고 있는 클래스
        TextView _id, date, today, max, tvLoadTodayMaxCal;
        ImageButton ibClear;
        ProgressBar pbLoadBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _id = itemView.findViewById(R.id.tv_id);
            date = itemView.findViewById(R.id.tvDate);
            today = itemView.findViewById(R.id.tvToday);
            max = itemView.findViewById(R.id.tvMax);
            tvLoadTodayMaxCal = itemView.findViewById(R.id.tvLoadTodayMaxCal);
            ibClear = itemView.findViewById(R.id.ibClear);
            pbLoadBar = itemView.findViewById(R.id.pbLoadBar);
        }
    }
}

