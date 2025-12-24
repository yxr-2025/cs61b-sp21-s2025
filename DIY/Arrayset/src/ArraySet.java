import org.hamcrest.internal.ArrayIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Arrayset<T> implements Iterable<T>
{
    private T[] items;
    private int size;
    private int capacity = 100;

    public Arrayset()
    {
        this.items = (T[]) new Object[this.capacity];
        this.size = 0;
    }

    public boolean contain(T x)
    {
        if (x == null)  return false;

        for (int i = 0; i < this.size; i++)
        {
            if (items[i].equals(x))    return true;
        }
        return false;
    }

    public void add(T x)
    {
        if (x == null)
        {
            throw new IllegalArgumentException("can`t add null");
        }
        if (contain(x))
        {
            return;
        }
        if (size == capacity)   {resize(size * 2);}
        items[size++] = x;
    }

    private void resize(int x)
    {
        T[] temp = (T[]) new Object[x];
        System.arraycopy(items, 0, temp, 0, this.size);
        items = temp;
    }

    public int size()
    {
        return this.size;
    }

    /*
    @Override
    public boolean equals(Object other)
    {
        if (this == other)  return true;
        if (!(other instanceof Arrayset<?>))    return false;
        Arrayset<T> o = (Arrayset<T>) other;
    }

     */


    @Override
    public Iterator<T> iterator()
    {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T>
    {
        private int wizPos = 0;

        @Override
        public boolean hasNext()
        {
            return wizPos < size;
        }

        @Override
        public T next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            return items[this.wizPos++];
        }
    }
*/
}