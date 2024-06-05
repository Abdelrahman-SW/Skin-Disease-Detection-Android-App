package com.example.coronaviruse;

public class DoctorItem {

    private String name ;
    private String Type ;
    private boolean available ;
    private float rating ;
    private String Time ;
    private int from , Work_hours;
    private int img ;
    private String Tel , Messanger ;

    public DoctorItem (String name, String type, boolean available, float rating, String time, int from, int workhours, int img , String Mess , String Tel) {
        this.name = name;
        Type = type;
        this.available = available;
        this.rating = rating;
        Time = time;
        this.from = from;
        Work_hours = workhours;
        this.img = img ;
        this.Tel = Tel ;
        this.Messanger = Mess ;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public String getMessanger() {
        return Messanger;
    }

    public void setMessanger(String messanger) {
        Messanger = messanger;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getWork_hours() {
        return Work_hours;
    }

    public void setWork_hours(int work_hours) {
        Work_hours = work_hours;
    }
}
