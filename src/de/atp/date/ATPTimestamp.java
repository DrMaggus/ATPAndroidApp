package de.atp.date;

import java.util.Date;

public interface ATPTimestamp<T extends ATPTimestamp<T>> {

    /**
     * 
     * @param other
     *            Instance of T
     * @return <code>True</code> and only true, when this time is before the
     *         other time. When both times are equal, this method will return
     *         <code>false</code>!
     */
    public boolean before(T other);

    /**
     * 
     * @param other
     *            Instance of T
     * @return <code>True</code> and only true, when this time is after the
     *         other time. When both times are equal, this method will return
     *         <code>false</code>!
     */
    public boolean after(T other);

    /**
     * Increases this field by one. It will increase other fields influenced by
     * this increase
     * 
     * @param field
     *            The field to increase
     */
    public void up(int field);

    /**
     * Decreases this field by one. It will decrease other fields influenced by
     * this decrease
     * 
     * @param field
     *            The field to decrease
     */
    public void down(int field);

    /**
     * Add or subtract a value to the specific field. The modification have
     * influenced on implicit fields
     * 
     * @param field
     *            The field to change
     * @param diff
     *            The value (can be negativ to decrease)
     */
    public void modify(int field, int diff);

    /**
     * Wrap the date
     * 
     * @param date
     *            The date
     */
    public void setTime(Date date);

    /**
     * @param time
     *            Unix timestamp
     */
    public void setTimestamp(long time);

    /**
     * @return This time as a date.
     */
    public Date asDate();

    /**
     * @return A copy of this time
     */
    public T copy();

    /**
     * Create a time difference between this time and other time. The time
     * difference will always positive! To check, whether this is before other,
     * use {@link #before(T)}
     * 
     * @param other
     *            Time to diff
     * @return A new instance containing the difference
     */
    public T diff(T other);
}
