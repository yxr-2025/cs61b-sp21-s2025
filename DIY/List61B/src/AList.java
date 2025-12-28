public class AList<T> implements List61B<T>
{
    private T[] items;
    private int size;

    public AList()
    {
        items = (T[]) new Object[10];
        size = 0;
    }

    @Override
    public void addlast(T x)
    {
        if (size == items.length)
        {
            resize(size * 2);
        }
        items[size] = x;
        size++;
    }

    private void resize(int index)
    {
        T [] newitems = (T[]) new Object[index];
        System.arraycopy(items, 0, newitems, 0, size);
        items = newitems;
    }


    @Override
    public T removelast()
    {
        T temp = items[size - 1];
        items[size - 1] = null;
        size--;

        if (items.length / size > 4)
        {
            resize(items.length / 2);
        }

        return temp;
    }

    @Override
    public T get(int i)
    {
        if (i < 0 || i > size)
        {
            throw new IndexOutOfBoundsException("invalid get(number)");
        }
        return items[i];
    }

    @Override
    public int size()
    {
        return size;
    }
}
