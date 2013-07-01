package de.atp.date;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ATPDate implements ATPTimestampable<ATPDate>, Comparable<ATPDate> {

    public static final int FIELD_DAY = Calendar.DAY_OF_MONTH;
    public static final int FIELD_MONTH = Calendar.MONTH;
    public static final int FIELD_YEAR = Calendar.YEAR;

    private Calendar cal;

    public ATPDate() {
        this(Calendar.getInstance());
    }

    public ATPDate(Date date) {
        this();
        cal.setTime(date);
    }

    public ATPDate(Calendar cal) {
        this.cal = cal;
    }

    public ATPDate(long timestamp) {
        this();
        cal.setTimeInMillis(timestamp);
    }

    public ATPDate(int day, int month, int year) {
        this();
        setDay(day);
        setMonth(month);
        setYear(year);
    }

    @Override
    public boolean before(ATPDate other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public boolean after(ATPDate other) {
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

    public int getDay() {
        return cal.get(FIELD_DAY);
    }

    public void setDay(int day) {
        cal.set(FIELD_DAY, day);
    }

    public int getMonth() {
        return cal.get(FIELD_MONTH);
    }

    public void setMonth(int month) {
        cal.set(FIELD_MONTH, month);
    }

    public int getYear() {
        return cal.get(FIELD_YEAR);
    }

    public void setYear(int year) {
        cal.set(FIELD_YEAR, year);
    }

    @Override
    public void setTime(Date date) {
        this.cal.setTime(date);
    }

    @Override
    public void setTimestamp(long time) {
        this.cal.setTimeInMillis(time);
    }

    @Override
    public Date asDate() {
        return cal.getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (this == o)
            return true;
        if (!(o instanceof ATPDate))
            return false;

        ATPDate other = (ATPDate) o;

        return this.getDay() == other.getDay() && this.getMonth() == other.getMonth() && this.getYear() == other.getYear();
    }

    @Override
    public int compareTo(ATPDate another) {
        Calendar c1 = GregorianCalendar.getInstance();
        c1.set(getYear(), getMonth(), getDay());
        Calendar c2 = GregorianCalendar.getInstance();
        c2.set(another.getYear(), another.getMonth(), another.getDay());

        return c1.compareTo(c2);
    }

    @Override
    public String format(DateFormat formatter) {
        return formatter.format(asDate());
    }
}
