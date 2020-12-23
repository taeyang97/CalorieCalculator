package com.example.caloriecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterMemo extends RecyclerView.Adapter<RecyclerAdapterMemo.ViewHolder>{
    private ArrayList<ItemData> mPersons;
    private LayoutInflater mInflate;
    private Context mContext;
    String _ids, dates, memo;

    public RecyclerAdapterMemo(Context context, ArrayList<ItemData> persons) {
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mPersons = persons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflate.inflate(R.layout.listviewmemo,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final EditText et = new EditText(mContext);

        holder.dates.setText(mPersons.get(position).dates);
        holder.memo.setText(mPersons.get(position).memo);
        holder.ibUpdate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("메모를 확인하세요");
                if (et.getParent() != null){
                    ((ViewGroup) et.getParent()).removeView(et); // view에는 하나의 setView만 할 수 있기 때문
                }
                et.setText(mPersons.get(position).memo);
                builder.setMessage(mPersons.get(position).dates);
                builder.setView(et);
                builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dataBaseHelper;
                        dataBaseHelper = new DataBaseHelper(mContext);
                        dataBaseHelper.updateDataMemo(mPersons.get(position)._ids,mPersons.get(position).dates,et.getText().toString());
                        dataBaseHelper.updateItemsMemo();
                        Fragment2.rAdaptermemo.notifyDataSetChanged();
                        showToast("메모가 변경 되었습니다.");
                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dataBaseHelper;
                        dataBaseHelper = new DataBaseHelper(mContext);
                        dataBaseHelper.deleteDateMemo(mPersons.get(position)._ids);
                        dataBaseHelper.updateItemsMemo();
                        Fragment2.rAdaptermemo.notifyDataSetChanged();
                        showToast("메모가 삭제 되었습니다.");
                    }
                });
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

