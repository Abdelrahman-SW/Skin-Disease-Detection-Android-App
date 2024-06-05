package com.example.coronaviruse;

import com.google.firebase.firestore.Exclude;

public class ProtocolMedicines {

    private String documentId;
    private String First;
    private String Second;
    private String Third;
    private String Fourth;
    private String Fifth;
    private String Sixth;
    private String Seven;
    private String Eight;


    public ProtocolMedicines()
    {

    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ProtocolMedicines(String first, String second, String third, String fourth, String fifth, String sixth, String seven, String eight) {
        First = first;
        Second = second;
        Third = third;
        Fourth = fourth;
        Fifth = fifth;
        Sixth = sixth;
        Seven = seven;
        Eight = eight;
    }

    public String getSecond() {
        return Second;
    }

    public void setSecond(String second) {
        Second = second;
    }

    public String getThird() {
        return Third;
    }

    public void setThird(String third) {
        Third = third;
    }

    public String getFourth() {
        return Fourth;
    }

    public void setFourth(String fourth) {
        Fourth = fourth;
    }

    public String getFifth() {
        return Fifth;
    }

    public void setFifth(String fifth) {
        Fifth = fifth;
    }

    public String getSixth() {
        return Sixth;
    }

    public void setSixth(String sixth) {
        Sixth = sixth;
    }

    public String getSeven() {
        return Seven;
    }

    public void setSeven(String seven) {
        Seven = seven;
    }

    public String getEight() {
        return Eight;
    }

    public void setEight(String eight) {
        Eight = eight;
    }

    public String getFirst() {
        return First;
    }

    public void setFirst(String first) {
        First = first;
    }
}


