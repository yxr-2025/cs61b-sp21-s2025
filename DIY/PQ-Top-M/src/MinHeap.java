import java.util.ArrayList;

// 泛型（Generics） + 比较器（Comparator）
public class MinHeap
{
    //private
    int[] items;
    private int size;
    private static final int DEFAULT_CAPACITY = 4;


    public MinHeap()
    {
        // 1. 分配底层数组（注意：因为 index 0 不用，所以实际容量是 DEFAULT_CAPACITY）
        items = new int[DEFAULT_CAPACITY + 1];
        size = 0;
    }// 默认初始容量（如 4 或 8）

    public MinHeap(int initialCapacity)
    {
        items = new int[initialCapacity + 1];
        size = 0;
    }

    public void add(int x)
    {
        // 现在下标和size是一致的
        if (size + 1 >= items.length)   {resize(items.length * 2);}
        size += 1;
        items[size] = x;
        swim(size);
    }

    // 根节点被删除后，将末尾元素换到根部
    public int removeMin()
    {
        if (size <= 0)
        {
            throw new ArrayIndexOutOfBoundsException("can`t delect empty");
        }

        int temp = items[1];
        items[1] = items[size];
        items[size] = 0;
        size -= 1;
        sink(1);

        if ((double) size / items.length < 0.25)
        {
            resize(items.length / 2);
        }
        return temp;
    }

    public int getMin() {return items[1];}

    public int size()   {return size;}

    public boolean isEmpty()    {return size == 0;}


    private int parent(int k)   {return k / 2;}
    private int leftchild(int k)   {return k * 2;}
    private int rightchild(int k)   {return k * 2 + 1;}

    //只负责上升
    private void swim(int k)
    {
        if (k != 1 && items[k] < items[parent(k)])
        {
            swap(k, parent(k));
            swim(parent(k));
        }
    }

    private void sink(int k)
    {
        /*
            不能越界 + (父 > 子) 才换
            有两个换最小的哪一个
            有一个就换一个
            没有就不换
         */
        if (leftchild(k) <= size && rightchild(k) <= size)
        {
            // 换的是下标,所以minint是下标
            int minint = minInt(leftchild(k), rightchild(k));
            if (items[k] > items[minint])
            {
                swap(k, minint);
                swim(minint);
            }
        }

        else if (leftchild(k) <= size && rightchild(k) > size)
        {
            if (items[k] > items[leftchild(k)])
            {
                swap(k, leftchild(k));
            }
        }

    }

    private void resize(int capacity)
    {
        int[] newItem = new int[capacity];
        System.arraycopy(items, 0, newItem, 0, size + 1);
        items = newItem;
    }

    private void swap(int a, int b)
    {
        int temp = items[a];
        items[a] = items[b];
        items[b] = temp;
    }

    private int minInt(int a, int b)
    {
        if (items[a] > items[b])  {return b;}
        return a;
    }

    @Override
    public String toString()
    {
        if (size == 0)  {return "[]";}

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 1; i < size; i += 1)
        {
            sb.append(items[i]).append(", ");
        }
        sb.append(items[size]).append("]");
        return sb.toString();
    }

}
