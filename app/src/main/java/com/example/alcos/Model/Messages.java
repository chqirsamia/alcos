package com.example.alcos.Model;

public class Messages
{
    private String from, message, type,  messageID, time, date,id_admin;

    public Messages()
    {

    }

    public Messages(String from, String message, String type, String messageID, String time, String date, String id_admin) {
        this.from = from;
        this.message = message;
        this.type = type;
        this.messageID = messageID;
        this.time = time;
        this.date = date;
        this.id_admin = id_admin;
    }

    public String getId_admin() {
        return id_admin;
    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
