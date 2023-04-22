package com.example.infoapp.Model;
public class Information {
     String Title;
     String value;

     Information(){}
     public Information(String t, String v)
     {
         this.Title=t;
         this.value=v;
     }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
