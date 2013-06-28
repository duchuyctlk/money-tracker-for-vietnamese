package de.enough.polish.util;

public class HashMap
  implements Map
{
  public static final int DEFAULT_INITIAL_CAPACITY = 16;
  public static final int DEFAULT_LOAD_FACTOR = 75;
  private final int loadFactor;
  Element[] buckets;
  private final boolean isPowerOfTwo;
  int size;

  public HashMap()
  {
    this(16, 75);
  }

  public HashMap(int initialCapacity)
  {
    this(initialCapacity, 75);
  }

  public HashMap(int initialCapacity, int loadFactor)
  {
    if (initialCapacity <= 0) {
      throw new IllegalArgumentException();
    }

    initialCapacity = initialCapacity * 100 / loadFactor;
    int capacity = 1;
    while (initialCapacity > capacity) {
      capacity <<= 1;
    }
    this.isPowerOfTwo = (capacity == initialCapacity);

    this.buckets = new Element[initialCapacity];
    this.loadFactor = loadFactor;
  }

  public Object put(Object key, Object value)
  {
    if ((key == null) || (value == null)) {
      throw new IllegalArgumentException("HashMap cannot accept null key [" + key + "] or value [" + value + "].");
    }
    if (this.size * 100 / this.buckets.length > this.loadFactor) {
      increaseSize();
    }

    int hashCode = key.hashCode();
    int index;
    if (this.isPowerOfTwo)
      index = hashCode & 0x7FFFFFFF & this.buckets.length - 1;
    else {
      index = (hashCode & 0x7FFFFFFF) % this.buckets.length;
    }
    Element element = this.buckets[index];
    if (element == null) {
      element = new Element(hashCode, key, value);
      this.buckets[index] = element;
      this.size += 1;
      return null;
    }

    Element lastElement = element;
    do {
      if (element.key.equals(key)) {
        Object oldValue = element.value;
        element.value = value;
        return oldValue;
      }
      lastElement = element;
      element = element.next;
    }while (element != null);

    element = new Element(hashCode, key, value);
    lastElement.next = element;
    this.size += 1;
    return null;
  }

  public Object get(Object key)
  {
    if (key == null)
      throw new IllegalArgumentException();
    int index;
    if (this.isPowerOfTwo)
      index = key.hashCode() & 0x7FFFFFFF & this.buckets.length - 1;
    else {
      index = (key.hashCode() & 0x7FFFFFFF) % this.buckets.length;
    }
    Element element = this.buckets[index];
    if (element == null)
      return null;
    do
    {
      if (element.key.equals(key)) {
        return element.value;
      }
      element = element.next;
    }while (element != null);
    return null;
  }

  public Object remove(Object key)
  {
    if (key == null)
      throw new IllegalArgumentException();
    int index;
    if (this.isPowerOfTwo)
      index = key.hashCode() & 0x7FFFFFFF & this.buckets.length - 1;
    else {
      index = (key.hashCode() & 0x7FFFFFFF) % this.buckets.length;
    }
    Element element = this.buckets[index];
    if (element == null)
    {
      return null;
    }
    Element lastElement = null;
    do {
      if (element.key.equals(key)) {
        if (lastElement == null)
          this.buckets[index] = element.next;
        else {
          lastElement.next = element.next;
        }
        this.size -= 1;
        return element.value;
      }
      lastElement = element;
      element = element.next;
    }while (element != null);

    return null;
  }

  public boolean isEmpty()
  {
    return this.size == 0;
  }

  public int size()
  {
    return this.size;
  }

  public boolean containsKey(Object key)
  {
    return get(key) != null;
  }

  public boolean containsValue(Object value)
  {
    for (int i = 0; i < this.buckets.length; ++i) {
      Element element = this.buckets[i];
      while (element != null) {
        if (element.value.equals(value)) {
          return true;
        }
        element = element.next;
      }
    }
    return false;
  }

  public void clear()
  {
    for (int i = 0; i < this.buckets.length; ++i) {
      this.buckets[i] = null;
    }
    this.size = 0;
  }

  public Object[] values()
  {
    return values(new Object[this.size]);
  }

  public Object[] values(Object[] objects)
  {
    int index = 0;
    for (int i = 0; i < this.buckets.length; ++i) {
      Element element = this.buckets[i];
      while (element != null) {
        objects[index] = element.value;
        ++index;
        element = element.next;
      }
    }
    return objects;
  }

  public Object[] keys()
  {
    return keys(new Object[this.size]);
  }

  public Object[] keys(Object[] objects)
  {
    int index = 0;
    for (int i = 0; i < this.buckets.length; ++i) {
      Element element = this.buckets[i];
      while (element != null) {
        objects[index] = element.key;
        ++index;
        element = element.next;
      }
    }
    return objects;
  }

  public String toString()
  {
    StringBuffer buffer = new StringBuffer(this.size * 23);
    buffer.append(super.toString()).append("{\n");
    Object[] values = values();
    for (int i = 0; i < values.length; ++i) {
      buffer.append(values[i]);
      buffer.append('\n');
    }
    buffer.append('}');
    return buffer.toString();
  }

  private void increaseSize()
  {
    int newCapacity;
    if (this.isPowerOfTwo)
      newCapacity = this.buckets.length << 1;
    else {
      newCapacity = (this.buckets.length << 1) - 1;
    }
    Element[] newBuckets = new Element[newCapacity];
    for (int i = 0; i < this.buckets.length; ++i) {
      Element element = this.buckets[i];
      while (element != null)
      {
        int index;
        if (this.isPowerOfTwo)
          index = element.hashCodeValue & 0x7FFFFFFF & newCapacity - 1;
        else {
          index = (element.hashCodeValue & 0x7FFFFFFF) % newCapacity;
        }
        Element newElement = newBuckets[index];
        if (newElement == null) {
          newBuckets[index] = element;
        }
        else {
          while (newElement.next != null) {
            newElement = newElement.next;
          }
          newElement.next = element;
        }

        Element lastElement = element;
        element = element.next;
        lastElement.next = null;
      }
    }
    this.buckets = newBuckets;
  }

  public Iterator keysIterator() {
    return new HashMapIterator();
  }

  private final class HashMapIterator
    implements Iterator
  {
    private int position;
    private HashMap.Element current;
    private int lastBucketIndex;
    private int iteratorSize;

    HashMapIterator()
    {
      this.iteratorSize = HashMap.this.size;
    }

    public boolean hasNext() {
      return this.position < this.iteratorSize;
    }

    public Object next() {
      if (this.current != null) {
        this.current = this.current.next;
      }
      if (this.current == null)
      {
        for (int i = this.lastBucketIndex; i < HashMap.this.buckets.length; ++i) {
          HashMap.Element element = HashMap.this.buckets[i];
          if (element != null) {
            this.current = element;
            this.lastBucketIndex = (i + 1);
            this.position += 1;
            return element.key;
          }
        }

        throw new IllegalStateException("no more elements");
      }

      this.position += 1;
      return this.current.key;
    }

    public void remove()
    {
      if (this.current == null)
      {
        throw new IllegalStateException("no current element");
      }

      for (int i = this.lastBucketIndex - 1; i >= 0; --i) {
        HashMap.Element element = HashMap.this.buckets[i];
        if (element == this.current)
        {
          HashMap.this.buckets[i] = this.current.next;
          HashMap.this.size -= 1;
          return;
        }
        HashMap.Element last = element;
        while (element != null) {
          if (element == this.current) {
            last.next = element.next;
            HashMap.this.size -= 1;
            return;
          }
          last = element;
          element = element.next;
        }

      }

      throw new IllegalStateException("unable to locate current element");
    }
  }

  private static final class Element
  {
    public final Object key;
    public final int hashCodeValue;
    public Object value;
    public Element next;

    public Element(int hashCode, Object key, Object value)
    {
      this.hashCodeValue = hashCode;
      this.key = key;
      this.value = value;
    }
  }
}