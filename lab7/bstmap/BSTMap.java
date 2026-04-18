package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>
{
    private BSTNode root;
    private int size;

    private class BSTNode
    {
        V value;
        K key;
        BSTNode left;
        BSTNode right;

        BSTNode(K key, V value)
        {
            this.key = key;
            this.value = value;
        }
    }

    public BSTMap()
    {
        root = null;
        size = 0;
    }

    /** 从此映射中移除所有键值对。 */
    @Override
    public void clear()
    {
        root = null;
        size = 0;
    }

    /* 如果此映射包含指定键的映射关系，则返回 true。 */
    // 有没有这个key
    @Override
    public boolean containsKey(K key)
    {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return SearchHelper(root, key) != null;
    }

    private BSTNode SearchHelper(BSTNode n, K key)
    {
        if (n == null)  return null;

        if (key.compareTo(n.key) < 0)         return SearchHelper(n.left, key);
        else if (key.compareTo(n.key) > 0)    return SearchHelper(n.right, key);
        else                                  return n;
    }

    /* 返回指定键所映射到的值；如果此映射不包含该键的映射关系，则返回 null。 */
    @Override
    public V get(K key)
    {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        BSTNode temp = SearchHelper(root, key);
        if (temp == null)   return null;
        return temp.value;
    }

    /* 返回此映射中键值对的数量。 */
    public int size()
    {
        return size;
    }

    /* 在此映射中将指定值与指定键关联。 */
    // 添加+修改
    public void put(K key, V value)
    {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        root = putHelper(root, key, value);
    }

    // 违反代码复用原则，但这个实现较简单
    private BSTNode putHelper(BSTNode n, K key, V value)
    {
        if (n == null)
        {
            size += 1;
            return new BSTNode(key, value);
        }

        int cmp = key.compareTo(n.key);
        if (cmp < 0)    n.left = putHelper(n.left, key, value);
        else if (cmp > 0)   n.right = putHelper(n.right, key, value);
        else    n.value = value;
        return n;
    }

    // 返回此映射中所包含键的一个 Set 视图。
    public Set<K> keySet()
    {
        Set<K> set = new TreeSet<>();
        for (K key : this)
        {
            set.add(key);
        }
        return set;
    }

    /* 如果存在，从此映射中移除指定键的映射关系。*/
    @Override
    public V remove(K key)
    {
        V oldvalue = get(key);
        if (oldvalue != null)
        {
            root = RemoveHelper(root, key);
            size -= 1;
        }
        return oldvalue;
    }

    /* 仅当当前映射将指定键映射到指定值时，才移除该键的映射项。 */
    public V remove(K key, V value)
    {
        V oldvalue = get(key);
        if (oldvalue != null && oldvalue.equals(value))
        {
            root = RemoveHelper(root, key);
            size -= 1;
        }
        return oldvalue;
    }

    // 只进行删除操作
    private BSTNode RemoveHelper(BSTNode n, K key)
    {
        if (n == null)  return null;

        int cmp = key.compareTo(n.key);
        if (cmp < 0)    n.left = RemoveHelper(n.left, key);
        else if (cmp > 0)   n.right = RemoveHelper(n.right, key);
        else
        {
            if (n.right == null && n.left == null)
            {
                return null;
            }
            else if (n.left == null)
            {
                return n.right;
            }
            else if (n.right == null)
            {
                return n.left;
            }
            else
            {
                BSTNode successor = FindMin(n.right);
                n.value = successor.value;
                n.key = successor.key;
                n.right = RemoveHelper(n.right, successor.key);
                return n;
            }
        }
        return n;
    }

    private BSTNode FindMin(BSTNode n)
    {
        while (n.left != null)  {n = n.left;}
        return n;
    }

    @Override
    public Iterator<K> iterator()
    {
        return new BSTMapKeyIterator();
    }

    private class BSTMapKeyIterator implements Iterator<K>
    {
        private Deque<BSTNode> stack = new ArrayDeque<>();

        private BSTMapKeyIterator()
        {
            PushLeftPath(root);
        }

        private void PushLeftPath(BSTNode n)
        {
            BSTNode temp = n;
            while (temp != null)
            {
                stack.addFirst(temp);
                temp = temp.left;
            }
        }

        @Override
        public boolean hasNext()
        {
            return !stack.isEmpty();
        }

        @Override
        public K next()
        {
            // 先弹出再加入
            BSTNode temp = stack.removeFirst();
            if (temp.right != null)     PushLeftPath(temp.right);
            return temp.key;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // 按键递增顺序打印您的 BSTMap
    public void printInOrder()
    {
        printHelper(root);
    }

    private void printHelper(BSTNode n)
    {
        if (n == null)  return;

        printHelper(n.left);
        System.out.println("(" + n.key + ", " + n.value + ")");
        printHelper(n.right);
    }
}
