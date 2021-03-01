package com.example.caloriecalculator.loadpage;

public class LoadData {
    String _id, date, today, max;
    String _ids, dates, memo;

    public LoadData(String _id, String date, String today, String max) {
        this._id = _id;
        this.date = date;
        this.today = today;
        this.max = max;
    }

    public LoadData(String _ids, String dates, String memo) {
        this._ids = _ids;
        this.dates = dates;
        this.memo = memo;
    }
}
