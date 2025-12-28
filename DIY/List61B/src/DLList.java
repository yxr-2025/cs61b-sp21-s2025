public class DLList<T> implements List61B<T>
{
    private class Node
    {
        T item;
        Node prev;
        Node next;

        public Node(Node pre, Node next, T item)
        {
            this.prev = pre;
            this.next = next;
            this.item = item;
        }
    }

    // [0, 1, 2] -> size = 3
    private int size;
    private Node sentinel;

    public DLList()
    {
        sentinel = new Node(null, null, null);
        size = 0;
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    @Override
    public void addlast(T x)
    {
        Node oldlast = sentinel.prev;
        Node newlast = new Node(sentinel.prev, sentinel, x);
        oldlast.next = newlast;
        sentinel.prev = newlast;
        size++;
    }

    @Override
    public T removelast()
    {
        if (size == 0)
        {
            throw new RuntimeException("Cannot remove from empty list");
        }

        Node oldlast = sentinel.prev;
        T item = oldlast.item;

        Node newlast = oldlast.prev;
        newlast.next = sentinel;
        sentinel.prev = newlast;
        size--;
        return  item;
    }

    @Override
    public T get(int x)
    {
        if (x < 0 || x > size)
        {
            throw new IndexOutOfBoundsException("invalid get(number)");
        }

        Node p = sentinel.next;
        for (int i = 0; i < x; i++)
        {
            p = p.next;
        }
        return p.item;
    }

    @Override
    public int size()
    {
        return size;
    }
}
