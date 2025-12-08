package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T>
{
    private class Node
    {
        Node prev;
        Node next;
        T item;

        private Node(T item, Node prev, Node next)
        {
            this.item = item;
            this.prev = prev;
            this.next = next;

        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque()
    {
        sentinel = new Node(null, null, null);
        size = 0;
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    //Deque 接口不应实现 Iterable ，
    // 而应仅由两个实现类 LinkedListDeque 和 ArrayDeque 实现

    @Override// 可以假设 item 永远不会是 null
    public void addLast(T x)
    {
        Node oldlast = sentinel.prev;
        Node newlast = new Node(x, sentinel.prev, sentinel);
        oldlast.next = newlast;
        sentinel.prev = newlast;
        size++;
    }

    @Override  // 可以假设 item 从不为 null
    public void addFirst(T x)
    {
        Node oldfirst = sentinel.next;
        Node newfirst = new Node(x, sentinel, oldfirst);
        oldfirst.prev = newfirst;
        sentinel.next = newfirst;
        size++;
    }

    // 如果不存在这样的元素，返回 null
    // 要删干净
    @Override
    public T removeFirst()
    {
        if (size == 0)
        {
            return null;
        }
        Node temp = sentinel.next;
        sentinel.next = temp.next;
        temp.next.prev = sentinel;
        size--;
        return  temp.item;
    }

    // 移除并返回双端队列后端的元素。如果不存在这样的元素，返回 null
    @Override
    public T removeLast()
    {
        if (size == 0)
        {
            return null;
        }
        Node temp = sentinel.prev;
        sentinel.prev = temp.prev;
        temp.prev.next = sentinel;
        size--;
        return temp.item;
    }

    // 0 是前端元素，1 是下一个元素
    // 通过循环
    @Override
    public T get(int i)
    {
        if (i < 0 || i > size)
        {
            throw new IndexOutOfBoundsException("i is invalid");
        }
        Node temp = sentinel.next;
        for (int x = 0; x < i; x++)
        {
            temp = temp.next;
        }
        return temp.item;
    }

    // 1.一次的操作：创建临时节点
    // 2.循环的操作：节点向前，数字减小
    // 可以把一和二分开
    public T getRecursive(int index)
    {
        if (index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException("i is invalid");
        }
        Node p = sentinel.next;
        return getRecursiveHelper(index, p);
    }

    private T getRecursiveHelper(int index, Node p)
    {
        if (index == 0)
        {
            return p.item;
        }
        p = p.next;
        return getRecursiveHelper(index - 1, p);
    }

    // 返回双端队列中项目的数量，缓存实现
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

        StringBuffer sb = new StringBuffer("{");
        Node temp = sentinel.next;
        for (int i = 0; i < size - 1; i++)
        {
            sb.append(temp.item).append(", ");
            temp = temp.next;
        }
        sb.append(temp.item).append("}");
        System.out.println(sb);
    }

    // 看文档实现
    @Override
    public boolean equals(Object o)
    {
        // 浅层/快速比较
        if (this == o)  return true;
        if (!(o instanceof LinkedListDeque))   return false;
        if (((LinkedListDeque<?>) o).size != this.size) return false;

        // 类型转换
        LinkedListDeque<?> other = (LinkedListDeque<?>) o;

        // 实例化
        Iterator<T> iter1 = this.iterator();
        Iterator<?> iter2 = other.iterator();

        // 深层比较
        while (iter1.hasNext())
        {
            T item1 = iter1.next();
            Object item2 = iter2.next();

            if (!(item1.equals(item2)))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new linklistIter(); // 🏭 产出新书签
    }

    private class linklistIter implements Iterator<T>
    {
        private Node p = sentinel.next;

        @Override
        public boolean hasNext()
        {
            return p != sentinel;
        }

        // 取值（Get）和递进（Advance）合二为一
        @Override
        public T next()
        {
            T item = p.item;
            p = p.next;
            return item;
        }
        /**
         * 为了消除 JDK 6 兼容性警告，我们需要实现这个方法。
         * 因为我们的迭代器不支持“删除”操作，所以直接抛出异常。
         */
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }


}
