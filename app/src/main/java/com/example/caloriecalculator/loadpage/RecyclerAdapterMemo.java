package com.example.caloriecalculator.loadpage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caloriecalculator.DataBaseHelper;
import com.example.caloriecalculator.option.OnSingleClickListener;
import com.example.caloriecalculator.R;

import java.util.ArrayList;

public class RecyclerAdapterMemo extends RecyclerView.Adapter<RecyclerAdapterMemo.ViewHolder> {
    private ArrayList<LoadData> mPersons;
    private LayoutInflater mInflate;
    private Context mContext;
    Dialog memoInfoDialog;

    public RecyclerAdapterMemo(Context context, ArrayList<LoadData> persons) {
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mPersons = persons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflate.inflate(R.layout.loadpagelistviewmemo, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.dates.setText(mPersons.get(position).dates);
        holder.memo.setText(mPersons.get(position).memo);

        // 메모를 수정, 삭제해주는 버튼
        holder.ibUpdate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                memoInfoDialog = new Dialog(mContext);
                memoInfoDialog.setContentView(R.layout.loadpagememoinfodialog);

                ImageButton ibBack = (ImageButton) memoInfoDialog.findViewById(R.id.ibBack);
                TextView tvDate = (TextView) memoInfoDialog.findViewById(R.id.tvDate);
                EditText etMemoInfo = (EditText) memoInfoDialog.findViewById(R.id.etMemoInfo);
                Button btnRemove = (Button) memoInfoDialog.findViewById(R.id.btnRemove);
                Button btnUpdate = (Button) memoInfoDialog.findViewById(R.id.btnUpdate);

                memoInfoDialog.show();
                memoInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                memoInfoDialog.setCancelable(false);
                etMemoInfo.setText(mPersons.get(position).memo);
                tvDate.setText(mPersons.get(position).dates);
                ibBack.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        memoInfoDialog.dismiss();
                    }
                });
                btnUpdate.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        DataBaseHelper dataBaseHelper;
                        dataBaseHelper = new DataBaseHelper(mContext);
                        dataBaseHelper.updateDataMemo(mPersons.get(position)._ids, mPersons.get(position).dates, etMemoInfo.getText().toString());
                        dataBaseHelper.updateItemsMemo();
                        Fragment2.rAdaptermemo.notifyDataSetChanged();
                        showToast("메모가 변경 되었습니다.");
                        memoInfoDialog.dismiss();
                    }
                });
                btnRemove.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        DataBaseHelper dataBaseHelper;
                        dataBaseHelper = new DataBaseHelper(mContext);
                        dataBaseHelper.deleteDateMemo(mPersons.get(position)._ids);
                        dataBaseHelper.updateItemsMemo();
                        Fragment2.rAdaptermemo.notifyDataSetChanged();
                        showToast("메모가 삭제 되었습니다.");
                        memoInfoDialog.dismiss();
                    }
                });
            }
        });
    }

    void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder { // 자료를 담고 있는 클래스
        TextView dates, memo;
        ImageButton ibUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dates = itemView.findViewById(R.id.tvDates);
            memo = itemView.findViewById(R.id.tvMemo);
            ibUpdate = itemView.findViewById(R.id.ibUpdate);
        }
    }
}

