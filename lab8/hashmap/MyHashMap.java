package hashmap;

import java.util.*;

/**
 *  一个基于哈希表的 Map 实现。在最佳情况下，通过 get()、remove() 和 put() 访问元素的时间复杂度为均摊常数时间。
 *
 *  假设不会插入 null 键，并且在 remove() 时不会缩小容量。
 *  @author YOUR NAME HERE
 */

public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * 用于存储键/值对的受保护辅助类
     * protected 修饰符允许子类访问
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* 实例变量 */
    private Collection<Node>[] buckets;

    private double maxLoad;

    private int size;

    public static final int defaultSize = 16;

    private static final double defaultLoad = 0.75;

    /** 构造方法 */
    public MyHashMap() {
        this(defaultSize, defaultLoad);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, defaultLoad);
    }

    /**
     * MyHashMap 构造方法，创建一个大小为 initialSize 的底层数组。
     * 负载因子（# 条目 / # 桶）应始终 <= loadFactor
     *
     * @param initialSize 底层数组的初始大小
     * @param maxLoad 最大负载因子
     */
    public MyHashMap(int initialSize, double maxLoad) {
        createTable(initialSize);
        this.maxLoad = maxLoad;
    }

    /**
     * 返回一个要放置在哈希表桶中的新节点
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * 返回一个作为哈希表桶的数据结构
     *
     * Java 中的大多数数据结构都继承自 Collection，因此我们
     * 可以使用几乎任何数据结构作为我们的桶。
     *
     * 重写此方法以使用不同的数据结构作为
     * 底层的桶类型
     *
     * 务必调用此工厂方法，而不是使用 new 运算符创建
     * 你自己的桶数据结构！
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<Node>();
    }

    /**
     * 返回一个支撑我们哈希表的表格。如上所述，
     * 该表格可以是一个 Collection 对象的数组
     *
     * 在创建表格时，务必调用此工厂方法，以便
     * 所有桶类型均为 JAVA.UTIL.COLLECTION
     *
     * @param tableSize 要创建的表格大小
     */
    private Collection<Node>[] createTable(int tableSize) {
        buckets = (Collection<Node>[]) new Collection[tableSize];

        for (int i = 0; i < tableSize; i += 1){
            this.buckets[i] = createBucket();
        }
        return buckets;
    }

    // 在完成之前，你的代码将无法编译！
    public void clear() {
        this.buckets = createTable(defaultSize);
        this.size = 0;
    }

    /** 如果此映射包含指定键的映射关系，则返回 true。 */
    public boolean containsKey(K key) {
        if (findNode(key) == null)  return false;
        else                        return true;
    }

    // 放一个指针进去遍历
    public V get(K key) {
        Node n = findNode(key);

        if (n == null) {
            System.out.println("don`t have this key");
            return null;
        }
        return n.value;
    }

    // 增加：(# 条目 / # 桶）应始终 <= loadFactor
    // 更新：如果同一个键被多次插入，则每次都应更新对应的值
    public void put(K key, V value) {
        if (containsKey(key)) {
            findNode(key).value = value;
            return;
        }

        int index = getIndex(key);
        Collection<Node> bucket = buckets[index];
        bucket.add(createNode(key, value));
        size += 1;

        if ((double) size / buckets.length > maxLoad) {
            resize(buckets.length * 2);
        }
    }

    private void resize(int newLength) {
        // 创建
        Collection<Node>[] oldBuckets = buckets;
        buckets = createTable(newLength);

        // 搬运
        for (Collection<Node> bucket : oldBuckets){
            for (Node n : bucket) {
                // 重新审视一下自己的位置再放
                int index = getIndex(n.key);
                buckets[index].add(n);
            }
        }
    }

    private int getIndex(K key) {
        int hashID = key.hashCode();
        return Math.floorMod(hashID, buckets.length);
    }

    public int size(){
        return this.size;
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<>();

        for (Collection<Node> bucket : buckets) {
            for (Node n : bucket) {
                set.add(n.key);
            }
        }
        return set;
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        List<K> allNode = new ArrayList<>();

        for (Collection<Node> bucket : buckets) {
            for (Node n : bucket) {
                allNode.add(n.key);
            }
        }

        return allNode.iterator();
    }

    private Node findNode(K key) {
        int index = getIndex(key);
        Collection<Node> bucket = buckets[index];

        for (Node n : bucket) {
            if (n.key.equals(key)) {
                return n;
            }
        }
        return null;
    }


}