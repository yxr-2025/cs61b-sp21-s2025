import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArraySet<T> implements Iterable<T>
{
    private T[] items;
    private int size;
    private int capacity = 100;

    public ArraySet()
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
        @SuppressWarnings("unchecked") //抑制泛型警告
        T[] temp = (T[]) new Object[x];
        System.arraycopy(items, 0, temp, 0, this.size);
        items = temp;
    }

    public int size()
    {
        return this.size;
    }

    @SafeVarargs
    public static <T> ArraySet<T> of(T...element)
    {
        ArraySet<T> set = new ArraySet<>();
        for (T e : element)     {set.add(e);}
        return set;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)  return true;

        if (!(other instanceof ArraySet<?>))    return false;

        ArraySet<T> o = (ArraySet<T>) other;

        if (this.size != o.size) return false;

        for (int i = 0; i < this.size; i++)
        {
            if (!(o.contain(this.items[i]))) return false;
        }
        return true;
    }

    // 用stringbuilder
    @Override
    public String toString()
    {
        if (size == 0)  {return null;}
        StringBuilder sb = new StringBuilder("{");
        for(int i = 0; i < this.size - 1; i++)
        {
            sb.append(items[i]).append(", ");
        }
        sb.append(items[size - 1]).append("}");
        return sb.toString();
    }


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

}