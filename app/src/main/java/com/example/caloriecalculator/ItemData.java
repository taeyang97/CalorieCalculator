package com.example.caloriecalculator;

public class ItemData {
    String _id, date, today, max;

    public ItemData(String _id, String date, String today, String max) {
        this._id = _id;
        this.date = date;
        this.today = today;
        this.max = max;
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
}
