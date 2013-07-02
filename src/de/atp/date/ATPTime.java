package de.atp.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Wrapper for the calendar class containing only hour, minute and second of an
 * date
 */
public class ATPTime implements ATPTimestamp<ATPTime>, Comparable<ATPTime> {

    /** The hour of the day. First hour is 0 */
    public static final int FIELD_HOUR = Calendar.HOUR_OF_DAY;
    /** The minute on an hour. First minute is 0 */
    public static final int FIELD_MINUTE = Calendar.MINUTE;
    /** The second of a minute. First second is 0 */
    public static final int FIELD_SECOND = Calendar.SECOND;

    private Calendar cal;

    /**
     * The current time
     */
    public ATPTime() {
        this(Calendar.getInstance());
    }

    /**
     * Wraps the date
     * 
     * @param date
     *            The date to wrap
     */
    public ATPTime(Date date) {
        this();
        cal.setTime(date);
    }

    /**
     * Wraps the calendar
     * 
     * @param cal
     *            The calendar to wrap
     */
    public ATPTime(Calendar cal) {
        this.cal = cal;
    }

    /**
     * Create a time on this timestamp. The timestamp must be in UNIX format
     * (milliseconds since 1st of January 1970)
     * 
     * @param timestamp
     *            milliseconds since 1st of January 1970
     */
    public ATPTime(long timestamp) {
        this();
        this.cal.setTimeInMillis(timestamp);
    }

    /**
     * Time on this values
     * 
     * @param hour
     *            The hour
     * @param minute
     *            The minute
     * @param second
     *            The second
     */
    public ATPTime(int hour, int minute, int second) {
        this();
        setHour(hour);
        setMinute(minute);
        setSecond(second);
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

    /**
     * @return The hour of this day. Range is from 0-23
     */
    public int getHour() {
        return cal.get(FIELD_HOUR);
    }

    /**
     * Set the hour of this day
     * 
     * @param hour
     *            The hour
     */
    public void setHour(int hour) {
        cal.set(FIELD_HOUR, hour);
    }

    /**
     * @return The minute of the hour. Range is from 0-59
     */
    public int getMinute() {
        return cal.get(FIELD_MINUTE);
    }

    /**
     * Set the minute of the hour
     * 
     * @param minute
     *            The minute
     */
    public void setMinute(int minute) {
        cal.set(FIELD_MINUTE, minute);
    }

    /**
     * @return The second of the minute. Range is from 0-59
     */
    public int getSecond() {
        return cal.get(FIELD_SECOND);
    }

    /**
     * Set the second of the minute
     * 
     * @param second
     *            The second
     */
    public void setSecond(int second) {
        cal.set(FIELD_SECOND, second);
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

    /**
     * @param o
     *            Must be an instance of {@link ATPTime}
     * @return <code>True</code> and only true, when both objects have the same
     *         hour, same minute and same second
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (this == o)
            return true;
        if (!(o instanceof ATPTime))
            return false;

        ATPTime other = (ATPTime) o;

        return this.getHour() == other.getHour() && this.getMinute() == other.getMinute() && this.getSecond() == other.getSecond();
    }

    @Override
    public int compareTo(ATPTime another) {
        int otherTime = another.getHour() * 60 * 60 + another.getMinute() * 60 + another.getSecond();
        int thisTime = this.getHour() * 60 * 60 + this.getMinute() * 60 + this.getSecond();
        return thisTime - otherTime;
    }

    @Override
    public ATPTime copy() {
        return new ATPTime(cal.getTimeInMillis());
    }

    @Override
    public ATPTime diff(ATPTime other) {
        int hourDiff = Math.abs(this.getHour() - other.getHour());
        int minuteDiff = Math.abs(this.getMinute() - other.getMinute());
        int secondsDiff = Math.abs(this.getSecond() - other.getSecond());

        return new ATPTime(hourDiff, minuteDiff, secondsDiff);
    }
}
