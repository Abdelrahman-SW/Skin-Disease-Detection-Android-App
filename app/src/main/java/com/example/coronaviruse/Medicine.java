package com.example.coronaviruse;

public class Medicine {
    private String med_name ;
    private String med_details ;

    public String getMed_name() {
        return med_name;
    }

    public void setMed_name(String med_name) {
        this.med_name = med_name;
    }

    public String getMed_details() {
        return med_details;
    }

    public void setMed_details(String med_details) {
        this.med_details = med_details;
    }

    public Medicine(String med_name, String med_details) {
        this.med_name = med_name;
        this.med_details = med_details;
    }
}
