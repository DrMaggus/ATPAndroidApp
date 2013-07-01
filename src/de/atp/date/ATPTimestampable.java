package de.atp.date;

import java.text.DateFormat;
import java.util.Date;

public interface ATPTimestampable<T extends ATPTimestampable<T>> {

    public boolean before(T other);

    public boolean after(T other);

    public boolean equals(Object o);

    public void up(int field);

    public void down(int field);

    public void modify(int field, int diff);

    public void setTime(Date date);

    public void setTimestamp(long time);

    public Date asDate();

    public String format(DateFormat formatter);
}
