package de.enough.polish.util;

import de.enough.polish.io.Externalizable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntList
  implements Externalizable
{
  private int[] storedObjects;
  private int growthFactor;
  private int size;

  public IntList()
  {
    this(10, 75);
  }

  public IntList(int initialCapacity)
  {
    this(initialCapacity, 75);
  }

  public IntList(int initialCapacity, int growthFactor)
  {
    this.storedObjects = new int[initialCapacity];
    this.growthFactor = growthFactor;
  }

  public int size()
  {
    return this.size;
  }

  public boolean contains(int element)
  {
    for (int i = 0; i < this.size; ++i) {
      int object = this.storedObjects[i];
      if (object == element) {
        return true;
      }
    }
    return false;
  }

  public int indexOf(int element)
  {
    for (int i = 0; i < this.size; ++i) {
      int object = this.storedObjects[i];
      if (object == element) {
        return i;
      }
    }
    return -1;
  }

  public int get(int index)
  {
    if ((index < 0) || (index >= this.size)) {
      throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
    }
    return this.storedObjects[index];
  }

  public int removeElementAt(int index)
  {
    if ((index < 0) || (index >= this.size)) {
      throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
    }
    int removed = this.storedObjects[index];
    for (int i = index + 1; i < this.size; ++i) {
      this.storedObjects[(i - 1)] = this.storedObjects[i];
    }
    this.size -= 1;
    return removed;
  }

  public boolean removeElement(int element)
  {
    int index = -1;
    for (int i = 0; i < this.size; ++i) {
      int object = this.storedObjects[i];
      if (object == element) {
        index = i;
        break;
      }
    }
    if (index == -1) {
      return false;
    }
    for (int i = index + 1; i < this.size; ++i) {
      this.storedObjects[(i - 1)] = this.storedObjects[i];
    }
    this.size -= 1;
    return true;
  }

  public void clear()
  {
    this.size = 0;
  }

  public void add(int element)
  {
    if (this.size >= this.storedObjects.length) {
      increaseCapacity();
    }
    this.storedObjects[this.size] = element;
    this.size += 1;
  }

  public void add(int index, int element)
  {
    if ((index < 0) || (index > this.size)) {
      throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
    }
    if (this.size >= this.storedObjects.length) {
      increaseCapacity();
    }

    for (int i = this.size; i > index; --i) {
      this.storedObjects[i] = this.storedObjects[(i - 1)];
    }

    this.storedObjects[index] = element;
    this.size += 1;
  }

  public int set(int index, int element)
  {
    if ((index < 0) || (index >= this.size)) {
      throw new IndexOutOfBoundsException("the index [" + index + "] is not valid for this list with the size [" + this.size + "].");
    }
    int replaced = this.storedObjects[index];
    this.storedObjects[index] = element;
    return replaced;
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer(this.size * 2);
    buffer.append(super.toString()).append("{\n");
    for (int i = 0; i < this.size; ++i) {
      buffer.append(this.storedObjects[i]);
      buffer.append('\n');
    }
    buffer.append('}');
    return buffer.toString();
  }

  public int[] toArray()
  {
    int[] copy = new int[this.size];
    System.arraycopy(this.storedObjects, 0, copy, 0, this.size);
    return copy;
  }

  public void trimToSize()
  {
    if (this.storedObjects.length != this.size) {
      int[] newStore = new int[this.size];
      System.arraycopy(this.storedObjects, 0, newStore, 0, this.size);
      this.storedObjects = newStore;
    }
  }

  private void increaseCapacity()
  {
    int currentCapacity = this.storedObjects.length;
    int newCapacity = currentCapacity + currentCapacity * this.growthFactor / 100;
    if (newCapacity == currentCapacity) {
      ++newCapacity;
    }
    int[] newStore = new int[newCapacity];
    System.arraycopy(this.storedObjects, 0, newStore, 0, this.size);
    this.storedObjects = newStore;
  }

  public int[] getInternalArray()
  {
    return this.storedObjects;
  }

  public void read(DataInputStream in)
    throws IOException
  {
    int storeSize = in.readInt();
    int growFactor = in.readInt();
    int[] store = new int[storeSize];
    for (int i = 0; i < store.length; ++i) {
      store[i] = in.readInt();
    }
    this.storedObjects = store;
    this.size = storeSize;
    this.growthFactor = growFactor;
  }

  public void write(DataOutputStream out)
    throws IOException
  {
    out.writeInt(this.size);
    out.writeInt(this.growthFactor);
    for (int i = 0; i < this.size; ++i) {
      int o = this.storedObjects[i];
      out.writeInt(o);
    }
  }
}