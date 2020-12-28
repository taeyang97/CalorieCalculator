package com.example.caloriecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<ItemData> mPersons;
    private LayoutInflater mInflate;
    private Context mContext;
    String dates,memo;
    int cYear, cMonth, cDay;

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

        final EditText et = new EditText(mContext);
        holder._id.setText(mPersons.get(position)._id);
        holder.date.setText(mPersons.get(position).date);
        holder.tvLoadTodayMaxCal.setText(mPersons.get(position).today + "kal / " + mPersons.get(position).max + "kal");
        holder.pbLoadBar.setMax(Integer.parseInt(mPersons.get(position).max));
        holder.pbLoadBar.setProgress(Integer.parseInt(mPersons.get(position).today));
        holder.ibCreate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("메모를 작성하세요");
                if (et.getParent() != null){
                    ((ViewGroup) et.getParent()).removeView(et); // view에는 하나의 setView만 할 수 있기 때문
                }
                builder.setView(et);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar cal = Calendar.getInstance(); // 핸드폰의 날짜와 시간을 가져와 시간을 넣어준다.
                        cYear = cal.get(Calendar.YEAR);
                        cMonth = cal.get(Calendar.MONTH);
                        cDay = cal.get(Calendar.DAY_OF_MONTH); // 그 달의 일수

                        dates = cYear + "-" + (cMonth + 1) + "-" + cDay;
                        memo = et.getText().toString();
                        et.setText("");
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
                        boolean isInserted = dataBaseHelper.insertDataMemo(dates,memo);
                        if(isInserted == true){
                            dataBaseHelper.updateItemsMemo();
                            Fragment2.rAdaptermemo.notifyDataSetChanged();
                            showToast("저장되었습니다.");
                        } else {
                            showToast("다시 저장해주세요.");
                        }
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.setCancelable(false);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.ibClear.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("기록을 제거하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dataBaseHelper;
                        dataBaseHelper = new DataBaseHelper(mContext);
                        dataBaseHelper.deleteDate(mPersons.get(position)._id);
                        dataBaseHelper.updateItems();
                        Fragment1.rAdapter.notifyDataSetChanged();
                        showToast("제거되었습니다.");
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    void showToast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder { // 자료를 담고 있는 클래스
        TextView _id, date, tvLoadTodayMaxCal;
        ImageButton ibClear,ibCreate;
        ProgressBar pbLoadBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            _id = itemView.findViewById(R.id.tv_id);
            date = itemView.findViewById(R.id.tvDate);
            tvLoadTodayMaxCal = itemView.findViewById(R.id.tvLoadTodayMaxCal);
            ibClear = itemView.findViewById(R.id.ibClear);
            ibCreate = itemView.findViewById(R.id.ibCreate);
            pbLoadBar = itemView.findViewById(R.id.pbLoadBar);
        }
    }
}

