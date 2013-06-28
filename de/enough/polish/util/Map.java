package de.enough.polish.util;

public abstract interface Map
{
  public abstract Object put(Object paramObject1, Object paramObject2);

  public abstract Object get(Object paramObject);

  public abstract Object remove(Object paramObject);

  public abstract boolean isEmpty();

  public abstract int size();

  public abstract boolean containsKey(Object paramObject);

  public abstract boolean containsValue(Object paramObject);

  public abstract void clear();

  public abstract Object[] values();

  public abstract Object[] values(Object[] paramArrayOfObject);

  public abstract Object[] keys();

  public abstract Object[] keys(Object[] paramArrayOfObject);

  public abstract Iterator keysIterator();
}