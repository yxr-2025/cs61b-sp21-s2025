package deque;

public class ArrayDeque<T> implements Deque<T>
{
    private T[] items;
    private int size;

    public ArrayDeque()
    {
        items = (T[]) new Object[8];
        size = 0;
    }

    @Override
    public void addLast(T x)
    {
        if (size == items.length)
        {
            resize(size * 2);
        }
        items[size] = x;
        size++;
    }

    @Override  // 可以假设 item 从不为 null
    public void addFirst(T x)
    {
        if (size == items.length)
        {
            resize(size * 2);
        }
        System.arraycopy(items, 0, items, 1, size);
        items[0] = x;
        size++;
    }

    // 对于长度为 16 或更长的数组，你的使用率应始终至少为 25%
    @Override
    public T removeLast()
    {
        if (size == 0)  return null;
        T temp = items[size - 1];
        items[size - 1] = null;
        size--;

        if (items.length >= 16 && items.length > 4 * size)
        {
            resize(items.length / 2);
        }
        return temp;
    }

    // 对于长度为 16 或更长的数组，你的使用率应始终至-少为 25%
    // 如果不存在这样的元素，返回 null
    // 要删干净
    @Override
    public T removeFirst()
    {
        if (size == 0)  return null;
        T temp = items[0];
        System.arraycopy(items, 1, items, 0, size - 1);
        items[size - 1] = null;
        size--;

        if (items.length >= 16 && items.length > 4 * size)
        {
            resize(items.length / 2);
        }
        return temp;
    }

    private void resize(int index)
    {
        T [] newitems = (T[]) new Object[index];
        System.arraycopy(items, 0, newitems, 0, size);
        items = newitems;
    }

    // 0 是前端元素，1 是下一个元素
    // 通过循环
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

    // 如果双端队列为空，则返回 true ，否则返回 false 。
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    //头到尾打印双端队列中的项目，项目之间用空格分隔。所有项目打印完后，换行
    @Override
    public void printDeque()
    {
        if (size == 0)
        {
            System.out.println("{}");
            return;
        }
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < size - 1; i++)
        {
            sb.append(items[i]).append(", ");
        }
        sb.append(items[size - 1]).append("}");
        System.out.println(sb);
    }

    // 看文档实现
    @Override
    public boolean equals(Object o)
    {
        if (o == this)  return true;
        if (!(o instanceof ArrayDeque)) return false;
        if (((ArrayDeque<?>) o).size != this.size)  return false;

        // 类型转换
        ArrayDeque<?> other = (ArrayDeque<?>) o;
        for (int i = 0; i < size; i++)
        {
            if (this.items[i] != other.items[i])    return false;
        }
        return true;
    }
}