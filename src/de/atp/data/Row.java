package de.atp.data;

public class Row {
    
    public final static int DATA_LENGTH = 8;
    
    private String code;
    private String date;
    private String alarmTime;
    private String answerTime;
    private int abortStatus;
    private int contacts;
    private int hours;
    private int minutes;

    public Row(String code, String date, String alarmTime, String answerTime, int abortStatus, int contacts, int hours, int minutes) {
        this.code = code;
        this.date = date;
        this.alarmTime = alarmTime;
        this.answerTime = answerTime;
        this.abortStatus = abortStatus;
        this.contacts = contacts;
        this.hours = hours;
        this.minutes = minutes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public int getAbortStatus() {
        return abortStatus;
    }

    public void setAbortStatus(int abortStatus) {
        this.abortStatus = abortStatus;
    }

    public int getContacts() {
        return contacts;
    }

    public void setContacts(int contacts) {
        this.contacts = contacts;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%d;%d;%d;%d", code, date, alarmTime, answerTime, abortStatus, contacts, hours, minutes);
    }

}