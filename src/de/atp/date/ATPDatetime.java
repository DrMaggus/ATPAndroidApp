package de.atp.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Wrapper for the calendar class containing the year, the month, the day and
 * it's hour, minute and second
 */
public class ATPDatetime implements ATPTimestamp<ATPDatetime>, Comparable<ATPDatetime> {

    /** The hour of the day. First hour is 0 */
    public static final int FIELD_HOUR = ATPTime.FIELD_HOUR;
    /** The minute on an hour. First minute is 0 */
    public static final int FIELD_MINUTE = ATPTime.FIELD_MINUTE;
    /** The second of a minute. First second is 0 */
    public static final int FIELD_SECOND = ATPTime.FIELD_SECOND;

    /** The day of the month. First day is 1 */
    public static final int FIELD_DAY = ATPDate.FIELD_DAY;
    /** The month of the year. First month is 0 */
    public static final int FIELD_MONTH = ATPDate.FIELD_MONTH;
    /** The Year */
    public static final int FIELD_YEAR = ATPDate.FIELD_YEAR;

    private ATPDate date;
    private ATPTime time;

    /**
     * The current date time
     */
    public ATPDatetime() {
        this.date = new ATPDate();
        this.time = new ATPTime();
    }

    /**
     * Wraps the date
     * 
     * @param date
     *            The date to wrap
     */
    public ATPDatetime(Date date) {
        this.date = new ATPDate(date);
        this.time = new ATPTime(date);
    }

    /**
     * Wraps the calendar
     * 
     * @param cal
     *            The calendar to wrap
     */
    public ATPDatetime(Calendar cal) {
        this.date = new ATPDate(cal);
        this.time = new ATPTime(cal);
    }

    /**
     * Create an date on this timestamp. The timestamp must be in UNIX format
     * (milliseconds since 1st of January 1970)
     * 
     * @param timestamp
     *            milliseconds since 1st of January 1970
     */
    public ATPDatetime(long timestamp) {
        this.date = new ATPDate(timestamp);
        this.time = new ATPTime(timestamp);
    }

    /**
     * Date on this values
     * 
     * @param day
     *            The day
     * @param month
     *            The month
     * @param year
     *            The year
     * @param hour
     *            The hour
     * @param minute
     *            The minute
     * @param second
     *            The second
     */
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

    /**
     * @return The hour of this day. Range is from 0-23
     */
    public int getHour() {
        return time.getHour();
    }

    /**
     * Set the hour of this day
     * 
     * @param hour
     *            The hour
     */
    public void setHour(int hour) {
        time.setHour(hour);
    }

    /**
     * @return The minute of the hour. Range is from 0-59
     */
    public int getMinute() {
        return time.getMinute();
    }

    /**
     * Set the minute of the hour
     * 
     * @param minute
     *            The minute
     */
    public void setMinute(int minute) {
        time.setMinute(minute);
    }

    /**
     * @return The second of the minute. Range is from 0-59
     */
    public int getSecond() {
        return time.getSecond();
    }

    /**
     * Set the second of the minute
     * 
     * @param second
     *            The second
     */
    public void setSeconds(int seconds) {
        time.setSecond(seconds);
    }

    /**
     * @return The day of this date
     */
    public int getDay() {
        return date.getDay();
    }

    /**
     * Set the day of this date.
     * 
     * @param day
     *            The day
     */
    public void setDay(int day) {
        date.setDay(day);
    }

    /**
     * @return The month of this date
     */
    public int getMonth() {
        return date.getMonth();
    }

    /**
     * Set the month of this date.
     * 
     * @param day
     *            The month
     */
    public void setMonth(int month) {
        date.setMonth(month);
    }

    /**
     * @return The year of this date
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Set the year of this date.
     * 
     * @param day
     *            The year
     */
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
        tmp.set(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute(), time.getSecond());
        return tmp.getTime();
    }

    /**
     * @param o
     *            Must be an instance of {@link ATPDatetime}
     * @return <code>True</code> and only true, when both objects have the same
     *         day, same month, same year, same hour, same minute and same
     *         second
     */
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
    public ATPDatetime copy() {
        ATPDatetime tmp = new ATPDatetime();
        tmp.date = new ATPDate(date.asDate());
        tmp.time = new ATPTime(time.asDate());
        return tmp;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "ATPDatetime{Day=%d;Month=%d;Year=%d;Hour=%d;Minute=%d;Second=%d}", getDay(), getMonth(), getYear(), getHour(), getMinute(), getSecond());
    }

    @Override
    public ATPDatetime diff(ATPDatetime other) {
        ATPDatetime tmp = new ATPDatetime();
        tmp.date = this.date.diff(other.date);
        tmp.time = this.time.diff(other.time);
        return tmp;
    }
}
