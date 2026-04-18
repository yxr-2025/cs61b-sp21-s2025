public class LLRB<K extends Comparable<K>, V>
{
    private class Node
    {
        K key;
        V value;
        Node left;
        Node right;
        boolean color;
        int size;

        public Node(K key, V value, boolean color, int size)
        {
            this.key = key;
            this.value = value;
            this.color = color;
            this.size = size;
        }
    }

    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;

    // 不用写构造函数，因为没有什么东西需要初始化

    // 需要返回node才能更新树，所以需要一个辅助函数
    public void put(K key, V value)
    {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = putHelper(root, key, value);
    }

    private Node putHelper(Node n, K key, V value)
    {
        if (n == null) { return new Node(key, value, RED, 1); }

        int cmp = key.compareTo(n.key);
        if (cmp < 0)      { n.left  = putHelper(n.left,  key, value); }
        else if (cmp > 0) { n.right = putHelper(n.right, key, value); }
        else              { n.value = value;}

        /*
        三种情况修复
        1. 右红
        2. 左两红
        3. 左右红
        其中2会变3
         */
        if (isRed(n.right) && !isRed(n.left))   {n = rotateLeft(n);}
        if (isRed(n.left) && isRed(n.left.left))   {n = rotateRight(n);}
        if (isRed(n.right) && isRed(n.left))    {flipColors(n);}

        n.size = 1 + size(n.left) + size(n.right);
        return n;
    }

    private Node rotateLeft(Node n)
    {
        Node x = n.right;
        n.right = x.left;
        x.left = n;

        n.size = 1 + size(n.left) + size(n.right);
        x.size = 1 + size(x.left) + size(x.right);

        return x;
    }

    private Node rotateRight(Node n)
    {
        Node x = n.left;
        n.left = x.right;
        x.right = n;

        n.size = 1 + size(n.left) + size(n.right);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    private void flipColors(Node n)
    {
            n.right.color = BLACK;
            n.left.color = BLACK;
            n.color = RED;
    }

    private boolean isRed(Node n)
    {
        return n != null && n.color == RED;
    }

    public V get(K key)
    {

    }

    public void delete(K key)
    {

    }

    // 有序查询
    public Iterable<K> keys()
    {

    }

    public K floor(K key)
    {

    }

    public K ceiling(K key)
    {

    }

    // 工具
    public int size()
    {
        return size(root);
    }

    private int size(Node n)
    {   // 每个节点的size应该先缓存好
        return n == null ? 0 : n.size;
    }

    public boolean isEmpty()
    {

    }

    public K min()
    {

    }

    public K max()
    {

    }

    @Override
    public String toString()
    {

    }
}
