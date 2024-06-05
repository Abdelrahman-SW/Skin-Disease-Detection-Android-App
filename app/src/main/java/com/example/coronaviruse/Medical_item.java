package com.example.coronaviruse;

public class Medical_item {
    private String title ;
    private String report ;
    private int drawable_id ;

    public Medical_item(String title, String report, int drawable_id) {
        this.title = title;
        this.report = report;
        this.drawable_id = drawable_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public int getDrawable_id() {
        return drawable_id;
    }

    public void setDrawable_id(int drawable_id) {
        this.drawable_id = drawable_id;
    }
}
