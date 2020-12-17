package com.example.caloriecalculator;

public class ItemData {
    String _id, date, today, max;
    String _ids, dates, memo;

    public ItemData(String _id, String date, String today, String max) {
        this._id = _id;
        this.date = date;
        this.today = today;
        this.max = max;
    }
    public ItemData(String _ids, String dates, String memo) {
        this._ids = _ids;
        this.dates = dates;
        this.memo = memo;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return date;
    }

    public String getSubtitle() {
        return today;
    }

    public String getMax() {
        return max;
    }

    public String get_ids() {
        return _ids;
    }

    public String getDates() {
        return dates;
    }

    public String getMemo() {
        return memo;
    }
}
