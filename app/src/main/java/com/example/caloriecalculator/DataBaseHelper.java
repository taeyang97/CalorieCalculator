package com.example.caloriecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DataBaseHelper"; // Logcat에 출력할 태그이름
    // database 의 파일 경로
    public static String DB_PATH = "";
    private static String DB_NAME = "CalorieCarculator.db";
    private SQLiteDatabase mDataBase;
    private Context mContext;
    String _id,date,today,max;
    String _ids,dates,memo;

    public DataBaseHelper(Context context) {
        super(context,DB_NAME,null,1);

        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        dataBaseCheck();
    }

    private void dataBaseCheck() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            dbCopy();
            Log.d(TAG,"Database is copied.");
        }
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 구조 생성로직
        Log.d(TAG,"onCreate()");
        db.execSQL("DROP TABLE IF EXISTS loadCalorie;");
        db.execSQL("CREATE TABLE loadCalorie (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, today TEXT, max TEXT);");
        db.execSQL("DROP TABLE IF EXISTS memo;");
        db.execSQL("CREATE TABLE memo (_ids INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dates TEXT, memo TEXT);");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Toast.makeText(mContext,"onOpen()",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onOpen() : DB Opening!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 삭제하고 onCreate() 다시 로드시킨다.
        Log.d(TAG,"onUpgrade() : DB Schema Modified and Excuting onCreate()");
        db.execSQL("DROP TABLE IF EXISTS loadCalorie;");
        onCreate(db);
    }

    // db를 assets에서 복사해온다.
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
                outputStream.write(mBuffer,0,mLength);
            }
            outputStream.flush();;
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dbCopy","IOException 발생함");
        }
    }

    // 데이터를 추가하고 ture/false값을 반환한다.
    public boolean insertData(String date, String today, String max){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("today", today);
        values.put("max", max);
        long result = sqlDB.insert("loadCalorie", null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    // 저장 목록 지우기
    public Integer deleteDate(String _id){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        return sqlDB.delete("loadCalorie", "_id = ?", new String[] {_id});
    }

    // 모든 DB ID 별로 나타내기
    public Cursor getAllData(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("select * from loadCalorie order by _id desc",null);
        return cursor;
    }

    /*
        DB에서 데이터를 모두 가져와서 ResyclerView 어답터에서 사용할 Items 배열에
        순더대로 추가하는 함수. 여기저기에서 불러서 사용해야 할경우를 대비 여기에 넣어 둠
     */

    public void updateItems() {
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        Fragment1.items.clear(); // 초기화
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM loadCalorie",null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext())
            {
                _id=cursor.getString(0);
                date=cursor.getString(1);
                today=cursor.getString(2);
                max=cursor.getString(3);

                Fragment1.items.add(0, new ItemData(_id, date, today, max));
            }
        }
        cursor.close();
        dbHelper.close();
    }

    // today칼로리 값을 호출해주는 메소드
    /*static public int gettodayCalorie(Context context,int todayCalorie){
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT today FROM todayCalorie;",null);
        while (cursor.moveToNext())
        {
            todayCalorie=cursor.getInt(0);
        }
        cursor.close();
        dbHelper.close();
        return todayCalorie;
    }*/

    // 데이터를 추가하고 ture/false값을 반환한다.
    public boolean insertDataMemo(String dates, String memo){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dates", dates);
        values.put("memo", memo);
        long result = sqlDB.insert("memo", null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    //데이터 수정 업데이트
    public boolean updateDataMemo(String _ids, String dates, String memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_ids", _ids);
        values.put("dates", dates);
        values.put("memo", memo);
        db.update("memo", values, "_ids = ?", new String[] { _ids });
        return true;
    }

    // 저장 목록 지우기
    public Integer deleteDateMemo(String _ids){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        return sqlDB.delete("memo", "_ids = ?", new String[] {_ids});
    }

    // 모든 DB ID 별로 나타내기
    public Cursor getAllDataMemo(){
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("select * from memo order by _ids desc",null);
        return cursor;
    }

    /*
        DB에서 데이터를 모두 가져와서 ResyclerView 어답터에서 사용할 Items 배열에
        순더대로 추가하는 함수. 여기저기에서 불러서 사용해야 할경우를 대비 여기에 넣어 둠
     */

    public void updateItemsMemo() {
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        Fragment2.items.clear(); // 초기화
        Cursor cursor = sqlDB.rawQuery("SELECT * FROM memo",null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext())
            {
                _ids=cursor.getString(0);
                dates=cursor.getString(1);
                memo=cursor.getString(2);

                Fragment2.items.add(0, new ItemData(_ids, dates, memo));
            }
        }
        cursor.close();
        dbHelper.close();
    }
}
