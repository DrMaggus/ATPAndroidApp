package de.atp.date;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ATPTime implements ATPTimestampable<ATPTime>, Comparable<ATPTime> {

    public static final int FIELD_HOUR = Calendar.HOUR_OF_DAY;
    public static final int FIELD_MINUTE = Calendar.MINUTE;
    public static final int FIELD_SECOND = Calendar.SECOND;

    private Calendar cal;

    public ATPTime() {
        this(Calendar.getInstance());
    }

    public ATPTime(Date date) {
        this();
        cal.setTime(date);
    }

    public ATPTime(Calendar cal) {
        this.cal = cal;
    }

    public ATPTime(long timestamp) {
        this();
        this.cal.setTimeInMillis(timestamp);
    }

    public ATPTime(int hour, int minute, int second) {
        this();
        setHours(hour);
        setMinutes(minute);
        setSeconds(second);
    }

    @Override
    public boolean before(ATPTime other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public boolean after(ATPTime other) {
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
        cal.add(field, diff);
    }

    public int getHours() {
        return cal.get(FIELD_HOUR);
    }

    public void setHours(int hours) {
        cal.set(FIELD_HOUR, hours);
    }

    public int getMinutes() {
        return cal.get(FIELD_MINUTE);
    }

    public void setMinutes(int minutes) {
        cal.set(FIELD_MINUTE, minutes);
    }

    public int getSeconds() {
        return cal.get(FIELD_SECOND);
    }

    public void setSeconds(int seconds) {
        cal.set(FIELD_SECOND, seconds);
    }

    @Override
    public void setTime(Date date) {
        this.cal.setTime(date);
    }

    @Override
    public void setTimestamp(long time) {
        this.setTimestamp(time);
    }

    @Override
    public Date asDate() {
        return this.cal.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (this == o)
            return true;
        if (!(o instanceof ATPTime))
            return false;

        ATPTime other = (ATPTime) o;

        return this.getHours() == other.getHours() && this.getMinutes() == other.getMinutes() && this.getSeconds() == other.getSeconds();
    }

    @Override
    public int compareTo(ATPTime another) {
        int otherTime = another.getHours() * 60 * 60 + another.getMinutes() * 60 + another.getSeconds();
        int thisTime = this.getHours() * 60 * 60 + this.getMinutes() * 60 + this.getSeconds();
        return thisTime - otherTime;
    }
    
    @Override
    public String format(DateFormat formatter) {
        return format(formatter);
    }
}
