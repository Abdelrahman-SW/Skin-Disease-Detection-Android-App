package com.example.coronaviruse;

public class QuestionItem {
    private String  description ;
    private final int Image ;
    private String Selected = "None" ;

    public QuestionItem(String description, int image, String selected) {
        this.description = description;
        Image = image;
        Selected = selected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return Image;
    }

    public String getSelected() {
        return Selected;
    }

    public void setSelected(String selected) {
        Selected = selected;
    }
}
