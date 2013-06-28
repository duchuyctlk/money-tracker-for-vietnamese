package de.enough.polish.util;

public abstract interface Iterator
{
  public abstract boolean hasNext();

  public abstract Object next();

  public abstract void remove();
}