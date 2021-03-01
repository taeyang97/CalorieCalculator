package com.example.caloriecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.caloriecalculator.loadpage.Fragment1;
import com.example.caloriecalculator.loadpage.Fragment2;
import com.example.caloriecalculator.loadpage.LoadData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// DB 담당 클래스
// 오픈 API 데이터를 가져와 SQLite에 저장한다.
public class DataBaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DataBaseHelper"; // Logcat에 출력할 태그이름
    // database 의 파일 경로
    public static String DB_PATH = "";
    private static String DB_NAME = "CalorieCarculator.db";
    private Context mContext;
    String _id, date, today, max;
    String _ids, dates, memo;

    // 생성과 동시에 파일을 생성해준다.
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);

        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        dataBaseCheck();
    }

    // 파일이 생성돼있지 않다면 파일 생성
    private void dataBaseCheck() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            dbCopy();
            Log.d(TAG, "Database is copied.");
        }
    }

    // db를 assets에서 복사해온다.
    // db를 수정했을 경우 파일 길이가 달라져 다시 저장한다.
    private void dbCopy() {

        try {
            File folder = new File(DB_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }

            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String out_filename = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(out_filename);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = inputStream.read(mBuffer)) > 0) {
                outputStream.write(mBuffer, 0, mLength);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dbCopy", "IOException 발생함");
        }
    }

    /*

    여기부터 SQLite DB DML

    */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 구조 생성로직
        Log.d(TAG, "onCreate()");
        db.execSQL("DROP TABLE IF EXISTS loadCalorie;");
        db.execSQL("CREATE TABLE loadCalorie (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, today TEXT, max TEXT);");
        db.execSQL("DROP TABLE IF EXISTS memo;");
        db.execSQL("CREATE TABLE memo (_ids INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dates TEXT, memo TEXT);");
        db.execSQL("DROP TABLE IF EXISTS todayCalorie;");
        db.execSQL("CREATE TABLE todayCalorie (date TEXT, today TEXT);");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 삭제하고 onCreate() 다시 로드시킨다.
        Log.d(TAG, "onUpgrade() : DB Schema Modified and Excuting onCreate()");
        db.execSQL("DROP TABLE IF EXISTS loadCalorie;");
        db.execSQL("DROP TABLE IF EXISTS memo;");
        onCreate(db);
    }


    // 누적 칼로리 DB
    // 데이터를 추가하고 ture/false값을 반환한다.
    public boolean insertData(String date, String today, String max) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("today", today);
        values.put("max", max);
        long result = sqlDB.insert("loadCalorie", null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // 누적 칼로리 DB
    // 저장 목록 지우기
    public Integer deleteDate(String _id) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        return sqlDB.delete("loadCalorie", "_id = ?", new String[]{_id});
    }


    // 누적 칼로리 DB
    /*
        DB에서 데이터를 모두 가져와서 ResyclerView 어답터에서 사용할 Items 배열에
        순대로 추가하는 함수. 여기저기에서 불러서 사용해야 할경우를 대비 여기에 넣어 둠
     */

    public void updateItems() {
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        Fragment1.items.clear(); // 초기화
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM loadCalorie", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                _id = cursor.getString(0);
                date = cursor.getString(1);
                today = cursor.getString(2);
                max = cursor.getString(3);

                Fragment1.items.add(0, new LoadData(_id, date, today, max));
            }
        }
        cursor.close();
        dbHelper.close();
    }


    // 누적 메모 DB
    // 데이터를 추가하고 ture/false값을 반환한다.
    public boolean insertDataMemo(String dates, String memo) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dates", dates);
        values.put("memo", memo);
        long result = sqlDB.insert("memo", null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // 누적 메모 DB
    // 데이터 수정 업데이트
    public boolean updateDataMemo(String _ids, String dates, String memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_ids", _ids);
        values.put("dates", dates);
        values.put("memo", memo);
        db.update("memo", values, "_ids = ?", new String[]{_ids});
        return true;
    }

    // 누적 메모 DB
    // 저장 목록 지우기
    public Integer deleteDateMemo(String _ids) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        return sqlDB.delete("memo", "_ids = ?", new String[]{_ids});
    }

    // 누적 메모 DB
    /*
        DB에서 데이터를 모두 가져와서 ResyclerView 어답터에서 사용할 Items 배열에
        순더대로 추가하는 함수. 여기저기에서 불러서 사용해야 할경우를 대비 여기에 넣어 둠
     */

    public void updateItemsMemo() {
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        Fragment2.items.clear(); // 초기화
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM memo", null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                _ids = cursor.getString(0);
                dates = cursor.getString(1);
                memo = cursor.getString(2);

                Fragment2.items.add(0, new LoadData(_ids, dates, memo));
            }
        }
        cursor.close();
        dbHelper.close();
    }

    // 하루 동안 먹은 칼로리 양
    public void insertDataTodayCalorie(String date, String today) {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("today", today);
        sqlDB.insert("todayCalorie", null, values);
    }

    // 하루가 지나고 다음 날 칼로리 초기화
    public void deleteDateTodayCalorie() {
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL("DELETE FROM todayCalorie");
    }
}
