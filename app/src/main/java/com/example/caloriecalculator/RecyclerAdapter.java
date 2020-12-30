package com.example.caloriecalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<ItemData> mPersons;
    private LayoutInflater mInflate;
    private Context mContext;
    String dates,memo;
    int cYear, cMonth, cDay;
    Dialog memoDialog, removeDialog;
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
        holder.tvLoadTodayMaxCal.setText(mPersons.get(position).today + "kal / " + mPersons.get(position).max + "kal");
        holder.pbLoadBar.setMax(Integer.parseInt(mPersons.get(position).max));
        holder.pbLoadBar.setProgress(Integer.parseInt(mPersons.get(position).today));
        holder.ibCreate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                memoDialog = new Dialog(mContext);
                memoDialog.setContentView(R.layout.memodialog);

                EditText etMemo = (EditText)memoDialog.findViewById(R.id.etMemo);
                Button btnExit = (Button)memoDialog.findViewById(R.id.btnExit);
                Button btnLoad = (Button)memoDialog.findViewById(R.id.btnLoad);

                memoDialog.show();
                memoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnLoad.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        Calendar cal = Calendar.getInstance(); // 핸드폰의 날짜와 시간을 가져와 시간을 넣어준다.
                        cYear = cal.get(Calendar.YEAR);
                        cMonth = cal.get(Calendar.MONTH);
                        cDay = cal.get(Calendar.DAY_OF_MONTH); // 그 달의 일수

                        dates = cYear + "-" + (cMonth + 1) + "-" + cDay;
                        memo = etMemo.getText().toString();
                        etMemo.setText("");
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
                btnExit.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        memoDialog.dismiss();
                    }
                });
            }
        });
        holder.ibClear.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                removeDialog = new Dialog(mContext);
                removeDialog.setContentView(R.layout.removedialog);

                Button btnCancel = (Button)removeDialog.findViewById(R.id.btnCancel);
                Button btnRemove = (Button)removeDialog.findViewById(R.id.btnRemove);

                removeDialog.show();
                removeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btnRemove.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        DataBaseHelper dataBaseHelper;
                        dataBaseHelper = new DataBaseHelper(mContext);
                        dataBaseHelper.deleteDate(mPersons.get(position)._id);
                        dataBaseHelper.updateItems();
                        Fragment1.rAdapter.notifyDataSetChanged();
                        showToast("제거되었습니다.");
                        removeDialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        removeDialog.dismiss();
                    }
                });
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

