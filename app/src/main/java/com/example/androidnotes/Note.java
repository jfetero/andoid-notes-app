package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Note implements Serializable {
    private String title;
    private String body;
    private String dateTime;

    public Note(String title, String body){
        setTitle(title);
        setBody(body);
        setDateTime();
    }
    public Note(String title, String body, String dateTime){
        setTitle(title);
        setBody(body);
        setDateTime(dateTime);
    }

    public void setDateTime(){
        Date currDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEE MMM dd, hh:mm a");

        this.dateTime = timeFormat.format(currDate);
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){return title;}
    public String getBody(){return body;}
    public String getDateTime(){return dateTime;}

    @NonNull
    public String toString(){
        try{
            StringWriter sw = new StringWriter();
            JsonWriter jw = new JsonWriter(sw);
            jw.beginObject();
            jw.name("title").value(getTitle());
            jw.name("body").value(getBody());
            jw.name("dateTime").value(getDateTime());
            jw.endObject();
            jw.close();
            return sw.toString();
        } catch (IOException e){ e.printStackTrace(); }
        return "";
    }
}
