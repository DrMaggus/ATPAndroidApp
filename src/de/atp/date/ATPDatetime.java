package de.atp.date;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ATPDatetime implements ATPTimestampable<ATPDatetime>, Comparable<ATPDatetime> {

    public static final int FIELD_HOUR = Calendar.HOUR_OF_DAY;
    public static final int FIELD_MINUTE = Calendar.MINUTE;
    public static final int FIELD_SECOND = Calendar.SECOND;

    public static final int FIELD_DAY = Calendar.DAY_OF_MONTH;
    public static final int FIELD_MONTH = Calendar.MONTH;
    public static final int FIELD_YEAR = Calendar.YEAR;

    private ATPDate date;
    private ATPTime time;

    public ATPDatetime() {
        this.date = new ATPDate();
        this.time = new ATPTime();
    }

    public ATPDatetime(Date date) {
        this.date = new ATPDate(date);
        this.time = new ATPTime(date);
    }

    public ATPDatetime(Calendar cal) {
        this.date = new ATPDate(cal);
        this.time = new ATPTime(cal);
    }

    public ATPDatetime(long timestamp) {
        this.date = new ATPDate(timestamp);
        this.time = new ATPTime(timestamp);
    }

    public ATPDatetime(int day, int month, int year, int hour, int minute, int second) {
        this.date = new ATPDate(day, month, year);
        this.time = new ATPTime(hour, minute, second);
    }

    @Override
    public boolean before(ATPDatetime other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public boolean after(ATPDatetime other) {
        return this.compareTo(other) > 0;
    }

    @Override
    public void up(int field) {
        this.modify(field, 1);
    }

    @Override
    public void down(int field) {
        this.modify(field, -1);
    }

    @Override
    public void modify(int field, int diff) {
        switch (field) {
            case FIELD_DAY :
            case FIELD_MONTH :
            case FIELD_YEAR :
                date.modify(field, diff);
                break;
            case FIELD_SECOND :
            case FIELD_MINUTE :
            case FIELD_HOUR :
                time.modify(field, diff);
                break;
            default :
                return;
        }
    }

    public int getHours() {
        return time.getHours();
    }

    public void setHours(int hours) {
        time.setHours(hours);
    }

    public int getMinutes() {
        return time.getMinutes();
    }

    public void setMinutes(int minutes) {
        time.setMinutes(minutes);
    }

    public int getSeconds() {
        return time.getSeconds();
    }

    public void setSeconds(int seconds) {
        time.setSeconds(seconds);
    }

    public int getDay() {
        return date.getDay();
    }

    public void setDay(int day) {
        date.setDay(day);
    }

    public int getMonth() {
        return date.getMonth();
    }

    public void setMonth(int month) {
        date.setMonth(month);
    }

    public int getYear() {
        return date.getYear();
    }

    public void setYear(int year) {
        date.setYear(year);
    }

    @Override
    public void setTime(Date date) {
        this.date.setTime(date);
        this.time.setTime(date);
    }

    @Override
    public void setTimestamp(long time) {
        this.date.setTimestamp(time);
        this.time.setTimestamp(time);
    }

    @Override
    public Date asDate() {
        Calendar tmp = GregorianCalendar.getInstance();
        tmp.set(date.getYear(), date.getMonth(), date.getDay(), time.getHours(), time.getMinutes(), time.getSeconds());
        return tmp.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (this == o)
            return true;
        if (!(o instanceof ATPDatetime))
            return false;

        ATPDatetime other = (ATPDatetime) o;

        return this.time.equals(other.time) && this.date.equals(other.date);
    }

    @Override
    public int compareTo(ATPDatetime another) {
        int cmpr = this.date.compareTo(another.date);
        if (cmpr == 0)
            return this.time.compareTo(another.time);
        else
            return cmpr;
    }

    @Override
    public String format(DateFormat formatter) {
        return formatter.format(asDate());
    }
}
