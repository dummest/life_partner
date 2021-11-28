package com.example.life_partner;

public class myData {
    int id, year, month, day, hour, minute;
    String title, description;

    public myData(int id, int year, int month, int day, int hour, int minute, String title, String description) {
        this.id = id;
        this.year =year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.title = title;
        this.description = description;
    }
    public int getId() {
        return id;
    }
    
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTitle(){
        return title;
    }
    public String getDescription() {
        return description;
    }
}
