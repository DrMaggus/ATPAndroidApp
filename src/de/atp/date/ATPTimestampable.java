package de.atp.date;

public interface ATPTimestampable<T extends ATPTimestampable<T>> {

    public boolean before(T other);

    public boolean after(T other);

    public boolean equals(Object o);

    public void up(int field);

    public void down(int field);

    public void modify(int field, int diff);
}
