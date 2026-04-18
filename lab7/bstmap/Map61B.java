package bstmap;

import java.util.Set;

/* 你的 BSTMap 实现类应实现此接口。
 * 实现方式：在你的 "public class..." 声明末尾加上 "implements Map61B<K,V>"，
 * 当然你也可以使用其他泛型参数名。
 */
public interface Map61B<K, V> extends Iterable<K> {

    /** 从此映射中移除所有键值对。 */
    void clear();

    /* 如果此映射包含指定键的映射关系，则返回 true。 */
    boolean containsKey(K key);

    /* 返回指定键所映射到的值；如果此映射不包含该键的映射关系，则返回 null。 */
    V get(K key);

    /* 返回此映射中键值对的数量。 */
    int size();

    /* 在此映射中将指定值与指定键关联。 */
    void put(K key, V value);

    /* 返回此映射中所包含键的一个 Set 视图。
     * Lab 7 中不要求实现。如果你不实现此方法，请抛出 UnsupportedOperationException。 */
    Set<K> keySet();

    /* 如果存在，从此映射中移除指定键的映射关系。
     * Lab 7 中不要求实现。如果你不实现此方法，请抛出 UnsupportedOperationException。 */
    V remove(K key);

    /* 仅当当前映射将指定键映射到指定值时，才移除该键的映射项。
     * Lab 7 中不要求实现。如果你不实现此方法，请抛出 UnsupportedOperationException。 */
    V remove(K key, V value);

}